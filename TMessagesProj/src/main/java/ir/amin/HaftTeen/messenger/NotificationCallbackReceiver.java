/*
 * This is the source code of aan for Android v. 3.x.x.
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Nikolai Kudashov, 2013-2017.
 */

package ir.amin.HaftTeen.messenger; import ir.amin.HaftTeen.BuildConfig; import ir.amin.HaftTeen.R;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationCallbackReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            return;
        }
        ApplicationLoader.postInitApplication();
        int currentAccount = intent.getIntExtra("currentAccount", UserConfig.selectedAccount);
        long did = intent.getLongExtra("did", 777000);
        byte[] data = intent.getByteArrayExtra("data");
        int mid = intent.getIntExtra("mid", 0);
        SendMessagesHelper.getInstance(currentAccount).sendNotificationCallback(did, mid, data);
    }
}