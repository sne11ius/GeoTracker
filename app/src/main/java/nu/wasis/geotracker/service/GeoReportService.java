package nu.wasis.geotracker.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Looper;
import android.util.Log;

import java.net.URL;

import nu.wasis.geotracker.settings.GeoTrackerSettings;

/**
 */
public class GeoReportService extends IntentService {

    private static final String TAG = GeoReportService.class.getName();

    public GeoReportService() {
        super(GeoReportService.class.getName());
        Log.d(TAG, "Created");
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        Log.d(TAG, "Started");
        final GeoTrackerSettings settings = new GeoTrackerSettings(this);
        final URL url = settings.getServiceUrl();
        Log.d(TAG, "Service url: " + (null == url ? null : url.toString()));
        if (null == url) {
            Log.e(TAG, "Service url is null, stopping the service.");
            GeoTrackerAlarmReceiver.stop(this);
            return;
        }
        final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.e(TAG, "GPS is diabled. Stopping service.");
            GeoTrackerAlarmReceiver.stop(this);
            return;
        }
        locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, new GeoTrackerLocationListener(this), Looper.myLooper());
        Looper.loop();
    }

}
