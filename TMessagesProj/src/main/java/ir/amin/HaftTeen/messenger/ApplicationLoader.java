/*
 * This is the source code of aan for Android v. 3.x.x.
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Nikolai Kudashov, 2013-2017.
 */

package ir.amin.HaftTeen.messenger;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.multidex.MultiDex;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;
import android.util.Log;

import com.adpdigital.push.AdpPushClient;
import com.adpdigital.push.ChabokNotification;
import com.adpdigital.push.NotificationHandler;
import com.adpdigital.push.OnDeeplinkResponseListener;
import com.adpdigital.push.PushMessage;
import com.batch.android.Batch;
import com.batch.android.BatchActivityLifecycleHelper;
import com.batch.android.Config;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.io.File;
import java.util.UUID;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ir.amin.HaftTeen.tgnet.ConnectionsManager;
import ir.amin.HaftTeen.tgnet.TLRPC;
import ir.amin.HaftTeen.ui.Components.ForegroundDetector;
import ir.amin.HaftTeen.ui.LaunchActivity;
import ir.amin.HaftTeen.vasni.utils.OksHttpClient;
import ir.amin.HaftTeen.BuildConfig;

public class ApplicationLoader extends Application {

    @SuppressLint("StaticFieldLeak")
    public static volatile Context applicationContext;
    public static volatile Context currentContext;
    public static volatile Handler applicationHandler;
    public static volatile boolean isScreenOn = false;
    public static volatile boolean mainInterfacePaused = true;
    public static volatile boolean externalInterfacePaused = true;
    public static volatile boolean mainInterfacePausedStageQueue = true;
    public static volatile long mainInterfacePausedStageQueueTime;
    public static int g_currentAccount;
    public static Handler mDelayHandler;
    private static volatile boolean applicationInited = false;
    private static Retrofit retrofit;
    private AdpPushClient chabok = null;

    public static File getFilesDirFixed() {
        for (int a = 0; a < 10; a++) {
            File path = ApplicationLoader.applicationContext.getFilesDir();
            if (path != null) {
                return path;
            }
        }
        try {
            ApplicationInfo info = applicationContext.getApplicationInfo();
            File path = new File(info.dataDir, "files");
            path.mkdirs();
            return path;
        } catch (Exception e) {
            FileLog.e(e);
        }
        return new File("/data/data/ir.amin.HaftTeen/files");
    }

    public static void postInitApplication() {
        if (applicationInited) {
            return;
        }

        applicationInited = true;

        try {
            LocaleController.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            final IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            final BroadcastReceiver mReceiver = new ScreenReceiver();
            applicationContext.registerReceiver(mReceiver, filter);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            PowerManager pm = (PowerManager) ApplicationLoader.applicationContext.getSystemService(Context.POWER_SERVICE);
            isScreenOn = pm.isScreenOn();
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("screen state = " + isScreenOn);
            }
        } catch (Exception e) {
            FileLog.e(e);
        }

        SharedConfig.loadConfig();
        for (int a = 0; a < UserConfig.MAX_ACCOUNT_COUNT; a++) {
            UserConfig.getInstance(a).loadConfig();
            MessagesController.getInstance(a);
            ConnectionsManager.getInstance(a);
            TLRPC.User user = UserConfig.getInstance(a).getCurrentUser();
            if (user != null) {
                MessagesController.getInstance(a).putUser(user, true);
                MessagesController.getInstance(a).getBlockedUsers(true);
                SendMessagesHelper.getInstance(a).checkUnsentMessages();
            }
        }

        ApplicationLoader app = (ApplicationLoader) ApplicationLoader.applicationContext;
        app.initPlayServices();
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("app initied");
        }

        MediaController.getInstance();
        for (int a = 0; a < UserConfig.MAX_ACCOUNT_COUNT; a++) {
            ContactsController.getInstance(a).checkAppAccount();
            DownloadController.getInstance(a);
        }

        WearDataLayerListenerService.updateWatchConnectionState();
    }

    public static void startPushService() {
        SharedPreferences preferences = MessagesController.getGlobalNotificationsSettings();
        if (preferences.getBoolean("pushService", true)) {
            try {
                applicationContext.startService(new Intent(applicationContext, NotificationsService.class));
            } catch (Throwable ignore) {

            }
        } else {
            stopPushService();
        }
    }

    public static void stopPushService() {
        applicationContext.stopService(new Intent(applicationContext, NotificationsService.class));
        PendingIntent pintent = PendingIntent.getService(applicationContext, 0, new Intent(applicationContext, NotificationsService.class), 0);
        AlarmManager alarm = (AlarmManager) applicationContext.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pintent);
    }

    public static <S> S getRetrofit(Class<S> sClass) {
        return getRetrofit(sClass, BuildConfig.SERVER_URL);
    }

    public static <S> S getRetrofit(Class<S> sClass, String baseUrl) {
        okhttp3.OkHttpClient okHttpClients = OksHttpClient.getUnsafeOkHttpClient();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClients)
                .build();
        return retrofit.create(sClass);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        mDelayHandler = new Handler();
        //MultiDex.install(this);
        //Batch.setConfig(new Config("5D63A0BBDEFD0E26FA00994840AD29"));
        registerActivityLifecycleCallbacks(new BatchActivityLifecycleHelper());
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
//        if (Function.createDirIfNotExists(AppSchema.downloadAppFolder)) {
//
//        }
        applicationContext = getApplicationContext();
        NativeLoader.initNativeLibs(ApplicationLoader.applicationContext);
        ConnectionsManager.native_setJava(false);
        new ForegroundDetector(this);
        applicationHandler = new Handler(applicationContext.getMainLooper());
        AndroidUtilities.runOnUIThread(ApplicationLoader::startPushService);

        /*initPushClient();
        String userId = chabok.getUserId();
        if (userId != null && !userId.isEmpty()) {
            chabok.register(userId);
        } else {
            chabok.registerAsGuest();
        }*/
    }

    private synchronized void initPushClient() {
        if (chabok == null) {
            chabok = AdpPushClient.init(
                    getApplicationContext(),
                    LaunchActivity.class,
                    "aan-en/335472094274",
                    "ea660e3f44b7649273423d82a84dfd42722c7b36",
                    "A@NeNg_ChxB0k",
                    "a@An()*ChBk@"
            );
            chabok.setDevelopment(false);
            chabok.addListener(this);
            chabok.addNotificationHandler(getNotificationHandler());
            //chabok.setDefaultTracker("SpPXZC");
            chabok.setOnDeeplinkResponseListener(new OnDeeplinkResponseListener() {
                @Override
                public boolean launchReceivedDeeplink(Uri uri) {
                    return true;
                }
            });

        }
    }

    private NotificationHandler getNotificationHandler() {
        return new NotificationHandler() {
            @Override
            public Class getActivityClass(ChabokNotification chabokNotification) {
                // return preferred activity class to be opened on this message's notification
                return LaunchActivity.class;
            }

            @Override
            public boolean buildNotification(ChabokNotification chabokNotification, NotificationCompat.Builder builder) {
                // use builder to customize the notification object
                // return false to prevent this notification to be shown to the user, otherwise true
                getDataFromChabokNotification(chabokNotification);
                return true;
            }
        };
    }

    private void getDataFromChabokNotification(ChabokNotification chabokNotification) {
        if (chabokNotification != null) {
            if (chabokNotification.getExtras() != null) {
                Bundle payload = chabokNotification.getExtras();

                //FCM message data is here
                Object data = payload.get("data");
                if (data != null) {
                    Log.d("aan", "getDataFromChabokNotification: The ChabokNotification data is : " + String.valueOf(data));
                }
            } else if (chabokNotification.getMessage() != null) {
                PushMessage payload = chabokNotification.getMessage();

                //Chabok message data is here
                JSONObject data = payload.getData();
                if (data != null) {
                    Log.d("aan", "getDataFromChabokNotification: The ChabokNotification data is : " + data);
                }
            }
        }
    }

    public void onEvent(PushMessage message) {
        Log.d("aan", "\n\n--------------------\n\nGOT MESSAGE " + message + "\n\n");
        JSONObject data = message.getData();
        if (data != null) {
            Log.d("aan", "--------------------\n\n The message data is : " + data + "\n\n");
        }
    }

    @Override
    public void onTerminate() {
        if (chabok != null)
            chabok.dismiss();

        super.onTerminate();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        try {
            LocaleController.getInstance().onDeviceConfigurationChange(newConfig);
            AndroidUtilities.checkDisplaySize(applicationContext, newConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initPlayServices() {
        AndroidUtilities.runOnUIThread(() -> {
            if (checkPlayServices()) {
                final String currentPushString = SharedConfig.pushString;
                if (!TextUtils.isEmpty(currentPushString)) {
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("GCM regId = " + currentPushString);
                    }
                } else {
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("GCM Registration not found.");
                    }
                }
                Utilities.globalQueue.postRunnable(() -> {
                    try {
                        String token = FirebaseInstanceId.getInstance().getToken();
                        if (!TextUtils.isEmpty(token)) {
                            GcmInstanceIDListenerService.sendRegistrationToServer(token);
                        }
                    } catch (Throwable e) {
                        FileLog.e(e);
                    }
                });
            } else {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("No valid Google Play Services APK found.");
                }
            }
        }, 1000);
    }

    private boolean checkPlayServices() {
        try {
            int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
            return resultCode == ConnectionResult.SUCCESS;
        } catch (Exception e) {
            FileLog.e(e);
        }
        return true;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }
}
