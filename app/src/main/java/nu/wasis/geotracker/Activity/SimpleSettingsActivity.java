package nu.wasis.geotracker.Activity;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
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

public class SimpleSettingsActivity extends Activity {

    private static final String TAG = SimpleSettingsActivity.class.getName();

    private GeoTrackerAlarmReceiver tracker;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tracker = new GeoTrackerAlarmReceiver();
        setContentView(R.layout.main);
        initServiceUrl();
        initApiKey();
        initServiceActive();
        initScheduleSpinner();
        if (!isGpsEnabled()) {
            Toast.makeText(this, "Gps is disabled. Please enable.", Toast.LENGTH_LONG).show();
        }
    }

    private void initServiceUrl() {
        final TextView serviceUrlField = (TextView) findViewById(R.id.txtServiceUrl);
        final GeoTrackerSettings settings = new GeoTrackerSettings(this);
        if (null != settings.getServiceUrl()) {
            serviceUrlField.setText(settings.getServiceUrl().toString());
        }
        final Context context = SimpleSettingsActivity.this;
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
                    if (tracker.isScheduled(context)) {
                        tracker.schedule(context, settings.getMinutes());
                    }
                } catch (MalformedURLException e) {
                    Log.d(TAG, "Not a valid url: " + value);
                    Log.d(TAG, "Stopping service...");
                    if (tracker.isScheduled(context)) {
                        tracker.stop(context);
                    }
                    settings.setServiceUrl(null);
                }
            }
        });
    }

    private void initApiKey() {
        final TextView apiKeyField = (TextView) findViewById(R.id.txtApiKey);
        final GeoTrackerSettings settings = new GeoTrackerSettings(this);
        apiKeyField.setText(settings.getApiKey());
        final Context context = SimpleSettingsActivity.this;
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
                if (tracker.isScheduled(context)) {
                    tracker.schedule(context, settings.getMinutes());
                }
            }
        });
    }

    private void initServiceActive() {
        final Switch serviceSwitch = (Switch) findViewById(R.id.serviceActive);
        final boolean isScheduled = tracker.isScheduled(this);
        serviceSwitch.setChecked(isScheduled);
        serviceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(TAG, "Checked: " + isChecked);
                if (isChecked) {
                    if (!isGpsEnabled()) {
                        Toast.makeText(SimpleSettingsActivity.this, "Gps is disabled. Please enable.", Toast.LENGTH_LONG).show();
                        serviceSwitch.setChecked(false);
                    } else {
                        startService(serviceSwitch);
                    }
                } else {
                    stopService();
                }

            }
        });
    }

    private void startService(final Switch serviceSwitch) {
        final int minutes = getScheduleMinutes();
        try {
            getUrl();
            tracker.schedule(SimpleSettingsActivity.this, minutes);
            Toast.makeText(SimpleSettingsActivity.this, "Service started :D", Toast.LENGTH_SHORT).show();
        } catch (MalformedURLException e) {
            serviceSwitch.setChecked(false);
            Log.d(TAG, "Cannot start service. No valid url.");
            Toast.makeText(SimpleSettingsActivity.this, "No valid URL provided.", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopService() {
        if (!tracker.isScheduled(this)) {
            Log.d(TAG, "Service not running, nothing to do.");
        } else {
            tracker.stop(this);
        }
        Toast.makeText(SimpleSettingsActivity.this, "Service stopped.", Toast.LENGTH_SHORT).show();
    }

    private int getScheduleMinutes() {
        final Spinner spinner = (Spinner) findViewById(R.id.schedule);
        @SuppressWarnings("unchecked")
        final ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spinner.getAdapter();
        final int selectedIndex = spinner.getSelectedItemPosition();
        final String selectedText = String.valueOf(adapter.getItem(selectedIndex));
        return Integer.parseInt(selectedText);
    }

    private URL getUrl() throws MalformedURLException {
        final TextView serviceUrlField = (TextView) findViewById(R.id.txtServiceUrl);
        final String url = String.valueOf(serviceUrlField.getText());
        return new URL(url);
    }

    private void initScheduleSpinner() {
        final Spinner spinner = (Spinner) findViewById(R.id.schedule);
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.schedules_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        final SimpleSettingsActivity context = SimpleSettingsActivity.this;
        final GeoTrackerSettings settings = new GeoTrackerSettings(context);
        final int minutes = settings.getMinutes();
        final int targetPosition = adapter.getPosition(String.valueOf(minutes));
        spinner.setSelection(targetPosition);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {
                final int selectedValue = getScheduleMinutes();
                settings.setMinutes(selectedValue);
                if (tracker.isScheduled(context)) {
                    tracker.stop(context);
                    tracker.schedule(context, settings.getMinutes());
                }
            }

            @Override
            public void onNothingSelected(final AdapterView<?> parent) {
                Log.e(TAG, "No schedule selected D:");
            }
        });
    }

    private boolean isGpsEnabled() {
        final LocationManager locationManager = (LocationManager) this.getSystemService(Service.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

}
