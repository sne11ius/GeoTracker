package nu.wasis.geotracker.service;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import de.greenrobot.event.EventBus;
import nu.wasis.geotracker.events.LocationTrackedEvent;
import nu.wasis.geotracker.model.GeoLocation;
import nu.wasis.geotracker.settings.GeoTrackerSettings;

/**
 */
public class GeoTrackerLocationListener implements LocationListener {

    private static final String TAG = GeoTrackerLocationListener.class.getName();

    private final GeoTrackerSettings settings;
    private final Context context;

    public GeoTrackerLocationListener(final Context context) {
        this.context = context;
        this.settings = new GeoTrackerSettings(context);
    }

    @Override
    public void onLocationChanged(final Location location) {
        Log.d(TAG, "Location: " + location);
        try {
            EventBus.getDefault().post(new LocationTrackedEvent(context, new GeoLocation(location)));
            //final HttpClient httpclient = new DefaultHttpClient();
            //final String serviceUrl = null == settings.getServiceUrl() ? null : settings.getServiceUrl().toString();
            //if (null == serviceUrl) {
            //    Log.e(TAG, "Service url is null, stopping the service.");
            //    GeoTrackerAlarmReceiver.stop(context);
            //    return;
            //}
            //final HttpPost httpPost = new HttpPost(serviceUrl);
            //final List<NameValuePair> nameValuePairs = toNameValuePairs(location, settings.getApiKey());
            //httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            //Log.d(TAG, "Posting data: " + nameValuePairs);
            //final HttpResponse response = httpclient.execute(httpPost);
            //if (HttpStatus.SC_OK != response.getStatusLine().getStatusCode()) {
            //    Log.e(TAG, "Error: " + response.getStatusLine());
            //}
        } catch (Exception e) {
            Log.d(TAG, "Could not post location: " + e.getMessage());
        } finally {
            Looper.myLooper().quit();
        }
    }

    @Override
    public void onStatusChanged(final String provider, final int status, final Bundle extras) {
        Log.d(TAG, "onStatusChanged: " + status);
    }

    @Override
    public void onProviderEnabled(final String provider) {
        Log.d(TAG, "onProviderEnabled: " + provider);
    }

    @Override
    public void onProviderDisabled(final String provider) {
        Log.d(TAG, "onProviderDisabled: " + provider);
    }

    //private List<NameValuePair> toNameValuePairs(final Location location, final String apiKey) {
    //    final List<NameValuePair> nameValuePairs = new ArrayList<>(2);
    //    nameValuePairs.add(new BasicNameValuePair("latitude", String.valueOf(location.getLatitude())));
    //    nameValuePairs.add(new BasicNameValuePair("longitude", String.valueOf(location.getLongitude())));
    //    nameValuePairs.add(new BasicNameValuePair("altitude", String.valueOf(location.getAltitude())));
    //    nameValuePairs.add(new BasicNameValuePair("accuracy", String.valueOf(location.getAccuracy())));
    //    nameValuePairs.add(new BasicNameValuePair("speed", String.valueOf(location.getSpeed())));
    //    nameValuePairs.add(new BasicNameValuePair("time", String.valueOf(location.getTime())));
    //    nameValuePairs.add(new BasicNameValuePair("apiKey", apiKey));
    //    return nameValuePairs;
    //}

}
