package abt.androidblacktiger;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

/**
 * Created by Diarmuid on 02/11/2015.
 */
public class SettingsListener implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static String DESTINATION_LANGUAGE = "lang_setting";
    public static String GPS_ON = "track_setting";
    Context context;

    public SettingsListener(Context context) {
        this.context = context;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(GPS_ON)) {
            String toastText;
            if (sharedPreferences.getBoolean(GPS_ON, true)) {
                toastText = "GPS Enabled";
            } else {
                toastText = "GPS Disabled";
//        stopUsingGPS

            }
            System.out.println(toastText);
            Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();
        }
    }
}
