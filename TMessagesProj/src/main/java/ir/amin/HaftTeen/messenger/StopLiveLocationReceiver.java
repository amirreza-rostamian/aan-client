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

public class StopLiveLocationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        for (int a = 0; a < UserConfig.MAX_ACCOUNT_COUNT; a++) {
            LocationController.getInstance(a).removeAllLocationSharings();
        }
    }
}
