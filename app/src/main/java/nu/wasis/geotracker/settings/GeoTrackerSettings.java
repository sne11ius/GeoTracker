package nu.wasis.geotracker.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

/**
 */
public class GeoTrackerSettings {

    private static final String TAG = GeoTrackerSettings.class.getName();

    private static final String KEY_SERVICE_URL = GeoTrackerSettings.class.getName() + "_service_url";
    private static final String KEY_API_KEY = GeoTrackerSettings.class.getName() + "_api_key";
    private static final String KEY_MINUTES = GeoTrackerSettings.class.getName() + "_minutes";
    private static final String KEY_SHOULD_RUN = GeoTrackerSettings.class.getName() + "_shoud_run";

    private final SharedPreferences prefs;

    public GeoTrackerSettings(final Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public URL getServiceUrl() {
        final String serviceUrl = prefs.getString(KEY_SERVICE_URL, null);
        Log.d(TAG, "serviceUrl loaded from fragment_settings: " + serviceUrl);
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
            prefs.edit().putString(KEY_SERVICE_URL, serviceUrl.toString()).apply();
        }
    }

    public String getApiKey() {
        return prefs.getString(KEY_API_KEY, "");
    }

    public void setApiKey(final String apiKey) {
        prefs.edit().putString(KEY_API_KEY, apiKey).apply();
    }

    public int getMinutes() {
        int minutes = prefs.getInt(KEY_MINUTES, 1);
        Log.d(TAG, "minutes loaded from fragment_settings: " + minutes);
        return minutes;
    }

    public void setMinutes(final int minutes) {
        Log.d(TAG, "Updating minutes to " + minutes);
        prefs.edit().putInt(KEY_MINUTES, minutes).apply();
    }

    public Boolean getShouldRun() {
        boolean shouldRun = prefs.getBoolean(KEY_SHOULD_RUN, false);
        Log.d(TAG, "Should run: " + shouldRun);
        return shouldRun;
    }

    public void setShouldRun(final Boolean shouldRun) {
        prefs.edit().putBoolean(KEY_SHOULD_RUN, shouldRun).apply();
    }

}
