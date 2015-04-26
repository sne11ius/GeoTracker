package nu.wasis.geotracker.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

import nu.wasis.geotracker.R;

/**
 */
public class GeoTrackerSettings {

    private static final String TAG = GeoTrackerSettings.class.getName();

    private final SharedPreferences prefs;
    private Context context;

    public GeoTrackerSettings(final Context context) {
        this.context = context;
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public URL getServiceUrl() {
        final String serviceUrl = prefs.getString(getString(R.string.KEY_ENDPOINT_URL), null);
        //Log.d(TAG, "serviceUrl loaded from fragment_settings: " + serviceUrl);
        try {
            return null != serviceUrl ? new URL(serviceUrl) : null;
        } catch (MalformedURLException e) {
            Log.e(TAG, "Could not restore service url from value '" + serviceUrl + "': " + e.getMessage());
            return null;
        }
    }

    public String getApiKey() {
        return prefs.getString(getString(R.string.KEY_API_KEY), "");
    }

    public int getGpsInterval() {
        return Integer.parseInt(prefs.getString(getString(R.string.KEY_GPS_TRACK_INTERVAL), "60"));
    }

    public int getSyncInterval() {
        return Integer.parseInt(prefs.getString(getString(R.string.KEY_SYNC_INTERVAL), "3600"));
    }

    public Boolean getShouldRun() {
        return prefs.getBoolean(getString(R.string.KEY_SERVICE_ACTIVE), false);
    }

    private String getString(int id) {
        return context.getString(id);
    }

}
