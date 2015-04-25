package nu.wasis.geotracker.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import nu.wasis.geotracker.R;

/**
 */
public class PrefsFragment extends PreferenceFragment {

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

}
