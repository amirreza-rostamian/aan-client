/*
 * This is the source code of aan for Android v. 4.x.x.
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Nikolai Kudashov, 2013-2017.
 */

package ir.amin.HaftTeen.messenger; import ir.amin.HaftTeen.BuildConfig; import ir.amin.HaftTeen.R;

import android.content.Context;
import android.content.SharedPreferences;

public class BuildVars {

    public static boolean DEBUG_VERSION = true;
    public static boolean DEBUG_PRIVATE_VERSION = true;
    public static boolean LOGS_ENABLED = true;
    public static boolean CHECK_UPDATES = false;
    public static int BUILD_VERSION = 1359;
    public static String BUILD_VERSION_STRING = "1.0";
    public static int APP_ID = 430091; //obtain your own APP_ID at https://core.aan.org/api/obtaining_api_id
    public static String APP_HASH = "340d58c8fd1a1688fac37e42f3534fc6"; //obtain your own APP_HASH at https://core.aan.org/api/obtaining_api_id
//    public static String HOCKEY_APP_HASH = "your-hockeyapp-api-key-here";
//    public static String HOCKEY_APP_HASH_DEBUG = "your-hockeyapp-api-key-here";
    public static String PLAYSTORE_APP_URL = "";

    static {
        if (ApplicationLoader.applicationContext != null) {
            SharedPreferences sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("systemConfig", Context.MODE_PRIVATE);
            LOGS_ENABLED = sharedPreferences.getBoolean("logsEnabled", DEBUG_VERSION);
        }
    }
}
