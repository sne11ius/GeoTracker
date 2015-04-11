package nu.wasis.geotracker.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

import nu.wasis.geotracker.Activity.SimpleSettingsActivity;

/**
 * Created by cornelius on 11.04.15.
 */
public class GeoTrackerSettings {

    private static final String TAG = GeoTrackerSettings.class.getName();

    private static final String PREFS_NAME = GeoTrackerSettings.class.getName() + "_Settings";

    private static final String KEY_SERVICE_URL = GeoTrackerSettings.class.getName() + "_service_url";
    private static final String KEY_MINUTES = GeoTrackerSettings.class.getName() + "_minutes";

    private SharedPreferences prefs;

    public GeoTrackerSettings(final Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public URL getServiceUrl() {
        final String serviceUrl = prefs.getString(KEY_SERVICE_URL, null);
        Log.d(TAG, "serviceUrl loaded from settings: " + (null == serviceUrl ? null : serviceUrl.toString()));
        try {
            return null != serviceUrl ? new URL(serviceUrl) : null;
        } catch (MalformedURLException e) {
            Log.e(TAG, "Could not restore service url from value '" + serviceUrl + "': " + e.getMessage());
            return null;
        }
    }

    public void setServiceUrl(final URL serviceUrl) {
        Log.d(TAG, "Updating serviceUrl to " + (null == serviceUrl ? null : serviceUrl.toString()));
        if (null == serviceUrl) {
            prefs.edit().remove(KEY_SERVICE_URL);
        } else {
            prefs.edit().putString(KEY_SERVICE_URL, serviceUrl.toString()).commit();
        }
    }

    public int getMinutes() {
        int minutes = prefs.getInt(KEY_MINUTES, 1);
        Log.d(TAG, "minutes loaded from settings: " + minutes);
        return minutes;
    }

    public void setMinutes(final int minutes) {
        Log.d(TAG, "Updating minutes to " + minutes);
        prefs.edit().putInt(KEY_MINUTES, minutes).commit();
    }
}
