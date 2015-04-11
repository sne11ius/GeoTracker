package nu.wasis.geotracker.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
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
import nu.wasis.geotracker.service.GeoReportService;
import nu.wasis.geotracker.service.GeoTrackerAlarmReceiver;
import nu.wasis.geotracker.settings.GeoTrackerSettings;

public class SimpleSettingsActivity extends Activity {

    private static final String TAG = SimpleSettingsActivity.class.getName();

    private GeoTrackerAlarmReceiver tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tracker = new GeoTrackerAlarmReceiver();
        setContentView(R.layout.main);
        initServiceUrl();
        initServiceActive();
        initScheduleSpinner();
    }

    private void initServiceUrl() {
        TextView serviceUrlField = (TextView) findViewById(R.id.txtServiceUrl);
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
                        tracker.schedule(context, settings.getMinutes(), settings.getServiceUrl());
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

    private void initServiceActive() {
        final Switch serviceSwitch = (Switch) findViewById(R.id.serviceActive);
        boolean isScheduled = tracker.isScheduled(this);
        serviceSwitch.setChecked(isScheduled);
        serviceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(TAG, "Checked: " + isChecked);
                if (isChecked) {
                    startService(serviceSwitch);
                } else {
                    stopService();
                }

            }
        });
    }

    private void startService(final Switch serviceSwitch) {
        final int minutes = getScheduleMinutes();
        try {
            final URL url = getUrl();
            tracker.schedule(SimpleSettingsActivity.this, minutes, url);
            String text = "Service started :D";
            Toast.makeText(SimpleSettingsActivity.this, text, Toast.LENGTH_SHORT).show();
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
        Spinner spinner = (Spinner) findViewById(R.id.schedule);
        ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spinner.getAdapter();
        int selectedIndex = spinner.getSelectedItemPosition();
        String selectedText = String.valueOf(adapter.getItem(selectedIndex));
        return Integer.parseInt(selectedText);
    }

    private URL getUrl() throws MalformedURLException {
        TextView serviceUrlField = (TextView) findViewById(R.id.txtServiceUrl);
        final String url = String.valueOf(serviceUrlField.getText());
        return new URL(url);
    }

    private void initScheduleSpinner() {
        final Spinner spinner = (Spinner) findViewById(R.id.schedule);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.schedules_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        final SimpleSettingsActivity context = SimpleSettingsActivity.this;
        final GeoTrackerSettings settings = new GeoTrackerSettings(context);
        int minutes = settings.getMinutes();
        int targetPosition = adapter.getPosition(String.valueOf(minutes));
        spinner.setSelection(targetPosition);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final int selectedValue = getScheduleMinutes();
                settings.setMinutes(selectedValue);
                if (tracker.isScheduled(context)) {
                    tracker.stop(context);
                    tracker.schedule(context, settings.getMinutes(), settings.getServiceUrl());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.e(TAG, "No schedule selected D:");
            }
        });
    }

}
