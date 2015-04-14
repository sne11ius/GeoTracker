package nu.wasis.geotracker.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
        final Context context = this;
        Log.d(TAG, "Started");
        final GeoTrackerSettings settings = new GeoTrackerSettings(this);
        final URL url = settings.getServiceUrl();
        Log.d(TAG, "Service url: " + (null == url ? null : url.toString()));
        if (null == url) {
            Log.e(TAG, "Service url is null, stopping the service.");
            new GeoTrackerAlarmReceiver().stop(this);
            return;
        }
        LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.e(TAG, "GPS is diabled. Stopping service.");
            new GeoTrackerAlarmReceiver().stop(this);
            return;
        }
        locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, new LocationListener() {
            @Override
            public void onLocationChanged(final Location location) {
                Log.d(TAG, "Location: " + location);
                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    final String serviceUrl = null == settings.getServiceUrl() ? null : settings.getServiceUrl().toString();
                    if (null == serviceUrl) {
                        Log.e(TAG, "Service url is null, stopping the service.");
                        new GeoTrackerAlarmReceiver().stop(context);
                        return;
                    }
                    HttpPost httppost = new HttpPost(serviceUrl);
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                    nameValuePairs.add(new BasicNameValuePair("latitude", String.valueOf(location.getLatitude())));
                    nameValuePairs.add(new BasicNameValuePair("longitude", String.valueOf(location.getLongitude())));
                    nameValuePairs.add(new BasicNameValuePair("altitude", String.valueOf(location.getAltitude())));
                    nameValuePairs.add(new BasicNameValuePair("accuracy", String.valueOf(location.getAccuracy())));
                    nameValuePairs.add(new BasicNameValuePair("speed", String.valueOf(location.getSpeed())));
                    nameValuePairs.add(new BasicNameValuePair("time", String.valueOf(location.getTime())));
                    nameValuePairs.add(new BasicNameValuePair("apiKey", settings.getApiKey()));
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    Log.d(TAG, "Posting data: " + nameValuePairs);
                    HttpResponse response = httpclient.execute(httppost);
                    if (200 != response.getStatusLine().getStatusCode()) {
                        Log.e(TAG, "Error: " + response.getStatusLine());
                    }
                }
                catch (Exception e) {
                    Log.d(TAG, "Could not post location: " + e.getMessage());
                }
                finally {
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
        }, Looper.myLooper());
        Looper.loop();
    }

}
