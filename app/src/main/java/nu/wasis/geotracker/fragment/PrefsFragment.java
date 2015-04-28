package nu.wasis.geotracker.fragment;

import android.app.Service;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

import nu.wasis.geotracker.R;
import nu.wasis.geotracker.settings.GeoTrackerSettings;
import nu.wasis.geotracker.util.Scheduler;

/**
 */
public class PrefsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = PrefsFragment.class.getName();

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public void onResume() {
        super.onResume();
        addChangeListeners();
    }

    private void addChangeListeners() {
        PreferenceManager.getDefaultSharedPreferences(getActivity()).registerOnSharedPreferenceChangeListener(this);
        /*
        final SwitchPreference serviceSwitch = (SwitchPreference) findPreference(getString(R.string.KEY_SERVICE_ACTIVE));
        serviceSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(final Preference preference, final Object newValue) {
                final boolean shouldActivate = (boolean) newValue;
                if (shouldActivate && !isGpsEnabled()) {
                    Toast.makeText(getActivity(), getString(R.string.error_gps_disabled), Toast.LENGTH_LONG).show();
                    return false;
                }
                if (shouldActivate) {
                    Scheduler.schedule(getActivity());
                } else {
                    Scheduler.stop(getActivity());
                }
                return true;
            }
        });
        */
        final ListPreference gpsUpdateIntervalPreference = (ListPreference) findPreference(getString(R.string.KEY_GPS_TRACK_INTERVAL));
        gpsUpdateIntervalPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(final Preference preference, final Object newValue) {
                if (new GeoTrackerSettings(getActivity()).getShouldRun()) {
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(500);
                                Scheduler.stop(getActivity());
                                Scheduler.schedule(getActivity());
                            } catch (final InterruptedException e) {
                                // Should not happen (tm)
                            }
                        }
                    }.start();
                }
                return true;
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        //removeChangeListeners();
    }
    /*
    private void removeChangeListeners() {
        PreferenceManager.getDefaultSharedPreferences(getActivity()).unregisterOnSharedPreferenceChangeListener(this);
        SwitchPreference serviceSwitch = (SwitchPreference) findPreference(getString(R.string.KEY_SERVICE_ACTIVE));
        serviceSwitch.setOnPreferenceChangeListener(null);
    }
    */

    @Override
    public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, final String key) {
        logChangeEvent(sharedPreferences, key);
    }

    private boolean isGpsEnabled() {
        final LocationManager locationManager = (LocationManager) getActivity().getSystemService(Service.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void logChangeEvent(final SharedPreferences sharedPreferences, final String key) {
        Log.d(TAG, "prefs[" + key + "]: " + sharedPreferences.getAll().get(key));
    }
}
