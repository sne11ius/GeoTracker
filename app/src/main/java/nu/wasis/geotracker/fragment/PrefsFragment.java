package nu.wasis.geotracker.fragment;

import android.app.Service;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import nu.wasis.geotracker.R;
import nu.wasis.geotracker.service.LocationSyncAlarmReceiver;
import nu.wasis.geotracker.service.LocationTrackerAlarmReceiver;
import nu.wasis.geotracker.settings.GeoTrackerSettings;

/**
 */
public class PrefsFragment extends PreferenceFragment {

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
        addGpsUpdateIntervalChangeListener();
        addSyncIntervalChangeListener();
    }

    private void addGpsUpdateIntervalChangeListener() {
        final ListPreference gpsUpdateIntervalPreference = (ListPreference) findPreference(getString(R.string.KEY_GPS_TRACK_INTERVAL));
        gpsUpdateIntervalPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(final Preference preference, final Object newValue) {
                if (new GeoTrackerSettings(getActivity()).isTrackingServiceActive()) {
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(500);
                                LocationTrackerAlarmReceiver.stop(getActivity());
                                LocationTrackerAlarmReceiver.schedule(getActivity());
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

    private void addSyncIntervalChangeListener() {
        final ListPreference syncIntervalPreference = (ListPreference) findPreference(getString(R.string.KEY_SYNC_INTERVAL));
        syncIntervalPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(final Preference preference, final Object newValue) {
                if (new GeoTrackerSettings(getActivity()).isSyncServiceActive()) {
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(500);
                                LocationSyncAlarmReceiver.stop(getActivity());
                                LocationSyncAlarmReceiver.schedule(getActivity());
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
    }

    private boolean isGpsEnabled() {
        final LocationManager locationManager = (LocationManager) getActivity().getSystemService(Service.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

}
