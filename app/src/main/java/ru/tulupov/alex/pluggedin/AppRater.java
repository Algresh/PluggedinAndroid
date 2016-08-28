package ru.tulupov.alex.pluggedin;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import static ru.tulupov.alex.pluggedin.constants.Constants.*;

public class AppRater {

    private final static int DAYS_UNTIL_PROMPT = 3;
    private final static int LAUNCHES_UNTIL_PROMPT = 10;

    public static boolean launchRater (Context context) {
        SharedPreferences pref = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        if (pref.getBoolean(APP_PREFERENCES_NOT_SHOW_RATER, false)) {
            return false;
        }

        SharedPreferences.Editor editor = pref.edit();
        long launchCount = pref.getLong(APP_PREFERENCES_COUNT_RATER, 0) + 1;
        editor.putLong(APP_PREFERENCES_COUNT_RATER, launchCount);
        Log.d(MY_TAG, launchCount + "");

        long launchDate = pref.getLong(APP_PREFERENCES_DATE_RATER, 0);
        if (launchDate == 0) {
            launchDate = System.currentTimeMillis();
            editor.putLong(APP_PREFERENCES_DATE_RATER, launchDate);
        }

        if (launchCount >= LAUNCHES_UNTIL_PROMPT) {
            if (System.currentTimeMillis() >= launchDate
                    + (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {

                editor.putLong(APP_PREFERENCES_COUNT_RATER, 0);
                editor.putLong(APP_PREFERENCES_DATE_RATER, 0);
                editor.apply();

                return true;
            }
        }
        editor.apply();

        return false;
    }

    public static void dontShowRaterDialogAgain(Context context) {
        SharedPreferences pref = context
                .getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(APP_PREFERENCES_NOT_SHOW_RATER, true);
        editor.apply();
    }
}
