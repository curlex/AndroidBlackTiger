package abt.androidblacktiger;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Nothing special here, just displays the settings from res/xml/pref_general.xml
 * Author: Diarmuid
 */
public class SettingsFragment extends PreferenceFragment{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.pref_general);
    }
}

