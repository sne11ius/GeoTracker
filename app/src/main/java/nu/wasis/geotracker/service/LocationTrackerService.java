package nu.wasis.geotracker.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Looper;
import android.util.Log;

/**
 */
public class LocationTrackerService extends IntentService {

    private static final String TAG = LocationTrackerService.class.getName();

    public LocationTrackerService() {
        super(LocationTrackerService.class.getName());
        Log.d(TAG, "Created");
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        Log.d(TAG, "Started");
        //final GeoTrackerSettings settings = new GeoTrackerSettings(this);
        //final URL url = settings.getServiceUrl();
        ////Log.d(TAG, "Service url: " + (null == url ? null : url.toString()));
        //if (null == url) {
        //    Log.e(TAG, "Service url is null, stopping the service.");
        //    Scheduler.stop(this);
        //    return;
        //}
        final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
        //    Log.e(TAG, "GPS is diabled. Stopping service.");
        //    Scheduler.stop(this);
        //    return;
        //}
        locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, new GeoTrackerLocationListener(this), Looper.myLooper());
        Looper.loop();
    }

}
