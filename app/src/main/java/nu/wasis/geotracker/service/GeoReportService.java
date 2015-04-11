package nu.wasis.geotracker.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.net.URL;
import java.util.Calendar;

import nu.wasis.geotracker.settings.GeoTrackerSettings;

/**
 * Created by cornelius on 11.04.15.
 */
public class GeoReportService extends IntentService {

    private static final String TAG = GeoReportService.class.getName();

    public GeoReportService() {
        super(GeoReportService.class.getName());
        Log.d(TAG, "Created");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "Started");
        GeoTrackerSettings settings = new GeoTrackerSettings(this);
        final URL url = settings.getServiceUrl();
        Log.d(TAG, "Service url: " + (null == url ? null : url.toString()));
        if (null == url) {
            Log.e(TAG, "Service url is null.");
            new GeoTrackerAlarmReceiver().stop(this);
        }
    }

}
