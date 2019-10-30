package ir.amin.HaftTeen.vasni.utils;


import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import me.himanshusoni.chatmessageview.util.AppSchema;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import ir.amin.HaftTeen.messenger.ApplicationLoader;
import ir.amin.HaftTeen.BuildConfig;

public class OksHttpClient {
    public static okhttp3.OkHttpClient getUnsafeOkHttpClient() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[]{};
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            okhttp3.OkHttpClient.Builder builder = new okhttp3.OkHttpClient.Builder();
            builder.sslSocketFactory(sc.getSocketFactory(), (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okhttp3.OkHttpClient okHttpClient = builder.
                    addInterceptor(httpLoggingInterceptor)
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            String token = "Bearer " + MSharePk.getString(ApplicationLoader.applicationContext, AppSchema.token, "");

//                            String UserToken = "";
//                            String DeviceID = "";
//                            String Mobile = "";
//                            try {
//                                Map<String, String> hashMap = Function.getuserToken(ApplicationLoader.applicationContext, MSharePk.getString(ApplicationLoader.applicationContext, AppSchema.phoneNumber, "").replace("98", "0"));
//                                UserToken = hashMap.get("User-Token");
//                                DeviceID = hashMap.get("Device-Id");
//                                Mobile = hashMap.get("Mobile");
//
//
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
                            Request original = chain.request();
                            Request request = original.newBuilder()
                                    .header("Authorization", token)
                                    .header("serviceId", BuildConfig.APP_ID)
                                    .method(original.method(), original.body())
                                    .build();

                            return chain.proceed(request);
                        }
                    }).build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}