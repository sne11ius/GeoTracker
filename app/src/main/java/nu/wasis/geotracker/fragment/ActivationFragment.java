package nu.wasis.geotracker.fragment;

import android.app.Fragment;
import android.app.Service;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import nu.wasis.geotracker.BuildConfig;
import nu.wasis.geotracker.R;
import nu.wasis.geotracker.service.LocationSyncAlarmReceiver;
import nu.wasis.geotracker.service.LocationTrackerAlarmReceiver;
import nu.wasis.geotracker.settings.GeoTrackerSettings;
import nu.wasis.geotracker.util.logging.Logger;

/**
 */
public class ActivationFragment extends Fragment {

    private static final Logger LOG = Logger.getLogger(ActivationFragment.class);

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.fragment_activation, container, false);
        initTrackingSwitch(linearLayout);
        initSyncSwitch(linearLayout);
        initBuildInfo(linearLayout);
        return linearLayout;
    }

    private void initTrackingSwitch(final LinearLayout listView) {
        final boolean isTrackingActive = LocationTrackerAlarmReceiver.isScheduled(getActivity());
        final GeoTrackerSettings settings = new GeoTrackerSettings(getActivity());
        final boolean shouldTrackingServiceBeActive = settings.isTrackingServiceActive();
        if (!isTrackingActive && shouldTrackingServiceBeActive) {
            LOG.error("Tracking service should be active but is not. Updating preferences according to real state.");
            settings.setTrackingServiceActive(false);
        }
        final Switch trackingActiveSwitch = (Switch) listView.findViewById(R.id.switch_tracking_active);
        trackingActiveSwitch.setChecked(isTrackingActive);
        trackingActiveSwitch.setOnCheckedChangeListener(new TrackingSwitchCheckedChangeListener());
    }

    private void initSyncSwitch(final LinearLayout listView) {
        final boolean isSyncActive = LocationSyncAlarmReceiver.isScheduled(getActivity());
        final GeoTrackerSettings settings = new GeoTrackerSettings(getActivity());
        final boolean shouldSyncServiceBeActive = settings.isSyncServiceActive();
        if (!isSyncActive && shouldSyncServiceBeActive) {
            LOG.error("Sync service should be active but is not. Updating preferences according to real state.");
            settings.setSyncServiceActive(false);
        }
        final Switch syncActiveSwitch = (Switch) listView.findViewById(R.id.switch_sync_active);
        syncActiveSwitch.setChecked(isSyncActive);
        syncActiveSwitch.setOnCheckedChangeListener(new SyncSwitchCheckedChangeListener());
    }

    private void initBuildInfo(final LinearLayout linearLayout) {
        final String buildInfo = "Built from " + BuildConfig.GIT_SHA + " at " + BuildConfig.BUILD_TIME + "\nSee https://github.com/sne11ius/GeoTracker/tree/" + BuildConfig.GIT_SHA;
        final TextView buildInfoField = (TextView) linearLayout.findViewById(R.id.buildInfo);
        buildInfoField.setText(buildInfo);
    }

    private final class TrackingSwitchCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
            final GeoTrackerSettings settings = new GeoTrackerSettings(getActivity());
            settings.setTrackingServiceActive(isChecked);
            if (isChecked) {
                if (!isGpsEnabled()) {
                    Toast.makeText(getActivity(), getString(R.string.error_gps_disabled), Toast.LENGTH_LONG).show();
                    buttonView.setChecked(false);
                } else {
                    LocationTrackerAlarmReceiver.schedule(getActivity());
                }
            } else {
                LocationTrackerAlarmReceiver.stop(getActivity());
            }
        }
    }

    private boolean isGpsEnabled() {
        final LocationManager locationManager = (LocationManager) getActivity().getSystemService(Service.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private final class SyncSwitchCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
            final GeoTrackerSettings settings = new GeoTrackerSettings(getActivity());
            settings.setSyncServiceActive(isChecked);
            if (isChecked) {
                LocationSyncAlarmReceiver.schedule(getActivity());
            } else {
                LocationSyncAlarmReceiver.stop(getActivity());
            }
        }
    }
}
