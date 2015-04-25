package nu.wasis.geotracker.fragment;

import android.app.Fragment;
import android.app.Service;
import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;

import nu.wasis.geotracker.R;
import nu.wasis.geotracker.service.GeoTrackerAlarmReceiver;
import nu.wasis.geotracker.settings.GeoTrackerSettings;

/**
 */
public class SettingsFragment extends Fragment {
    private static final String TAG = SettingsFragment.class.getName();

    private GeoTrackerSettings settings;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings = new GeoTrackerSettings(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        initServiceUrl(rootView);
        initApiKey(rootView);
        initServiceActive(rootView);
        initScheduleSpinner(rootView);
        if (!isGpsEnabled()) {
            Toast.makeText(getActivity(), "GPS is disabled. Please enable.", Toast.LENGTH_LONG).show();
        }
        return rootView;
    }

    private void initServiceUrl(View rootView) {
        final TextView serviceUrlField = (TextView) rootView.findViewById(R.id.txtServiceUrl);
        if (null != settings.getServiceUrl()) {
            serviceUrlField.setText(settings.getServiceUrl().toString());
        }
        final Context context = getActivity();
        serviceUrlField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {
            }

            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
            }

            @Override
            public void afterTextChanged(final Editable s) {
                final String value = s.toString();
                try {
                    final URL serviceUrl = new URL(value);
                    Log.d(TAG, "A valid serviceUrl: " + serviceUrl);
                    settings.setServiceUrl(serviceUrl);
                    if (GeoTrackerAlarmReceiver.isScheduled(context)) {
                        GeoTrackerAlarmReceiver.schedule(context, settings.getMinutes());
                    }
                } catch (MalformedURLException e) {
                    Log.d(TAG, "Not a valid url: " + value);
                    Log.d(TAG, "Stopping service...");
                    if (GeoTrackerAlarmReceiver.isScheduled(context)) {
                        GeoTrackerAlarmReceiver.stop(context);
                    }
                    settings.setServiceUrl(null);
                }
            }
        });
    }

    private void initApiKey(View rootView) {
        final TextView apiKeyField = (TextView) rootView.findViewById(R.id.txtApiKey);
        apiKeyField.setText(settings.getApiKey());
        final Context context = getActivity();
        apiKeyField.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {

            }

            @Override
            public void afterTextChanged(final Editable s) {
                final String value = s.toString();
                Log.d(TAG, "Api key: " + value);
                settings.setApiKey(value);
                if (GeoTrackerAlarmReceiver.isScheduled(context)) {
                    GeoTrackerAlarmReceiver.schedule(context, settings.getMinutes());
                }
            }
        });
    }

    private void initServiceActive(final View rootView) {
        final Switch serviceSwitch = (Switch) rootView.findViewById(R.id.serviceActive);
        final boolean isScheduled = GeoTrackerAlarmReceiver.isScheduled(getActivity());
        serviceSwitch.setChecked(isScheduled);
        serviceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(TAG, "Checked: " + isChecked);
                if (isChecked) {
                    if (!isGpsEnabled()) {
                        Toast.makeText(getActivity(), "GPS is disabled. Please enable.", Toast.LENGTH_LONG).show();
                        serviceSwitch.setChecked(false);
                    } else {
                        startService(serviceSwitch, rootView);
                    }
                } else {
                    stopService();
                }
            }
        });
    }

    private void startService(final Switch serviceSwitch, View rootView) {
        final int minutes = getScheduleMinutes(rootView);
        try {
            getUrl(rootView);
            settings.setShouldRun(true);
            GeoTrackerAlarmReceiver.schedule(getActivity(), minutes);
            Toast.makeText(getActivity(), "Service started :D", Toast.LENGTH_SHORT).show();
        } catch (MalformedURLException e) {
            serviceSwitch.setChecked(false);
            Log.d(TAG, "Cannot start service. No valid url.");
            Toast.makeText(getActivity(), "No valid URL provided.", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopService() {
        if (!GeoTrackerAlarmReceiver.isScheduled(getActivity())) {
            Log.d(TAG, "Service not running, nothing to do.");
        } else {
            settings.setShouldRun(false);
            GeoTrackerAlarmReceiver.stop(getActivity());
        }
        Toast.makeText(getActivity(), "Service stopped.", Toast.LENGTH_SHORT).show();
    }

    private int getScheduleMinutes(View rootView) {
        final Spinner spinner = (Spinner) rootView.findViewById(R.id.schedule);
        @SuppressWarnings("unchecked")
        final ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spinner.getAdapter();
        final int selectedIndex = spinner.getSelectedItemPosition();
        final String selectedText = String.valueOf(adapter.getItem(selectedIndex));
        return Integer.parseInt(selectedText);
    }

    private URL getUrl(View rootView) throws MalformedURLException {
        final TextView serviceUrlField = (TextView) rootView.findViewById(R.id.txtServiceUrl);
        final String url = String.valueOf(serviceUrlField.getText());
        return new URL(url);
    }

    private void initScheduleSpinner(final View rootView) {
        final Spinner spinner = (Spinner) rootView.findViewById(R.id.schedule);
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.gps_update_interval_titles, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        final Context context = getActivity();
        final int minutes = settings.getMinutes();
        final int targetPosition = adapter.getPosition(String.valueOf(minutes));
        spinner.setSelection(targetPosition);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {
                final int selectedValue = getScheduleMinutes(rootView);
                settings.setMinutes(selectedValue);
                if (GeoTrackerAlarmReceiver.isScheduled(context)) {
                    GeoTrackerAlarmReceiver.stop(context);
                    GeoTrackerAlarmReceiver.schedule(context, settings.getMinutes());
                }
            }

            @Override
            public void onNothingSelected(final AdapterView<?> parent) {
                Log.e(TAG, "No schedule selected D:");
            }
        });
    }

    private boolean isGpsEnabled() {
        final LocationManager locationManager = (LocationManager) getActivity().getSystemService(Service.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

}
