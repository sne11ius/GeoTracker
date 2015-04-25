package nu.wasis.geotracker.events.handler;

import android.util.Log;

import nu.wasis.geotracker.events.LocationTrackedEvent;
import nu.wasis.geotracker.model.DBHelper;
import nu.wasis.geotracker.model.GeoLocationDao;

/**
 */
public class LocationTrackedEventHandler {

    private static final String TAG = LocationTrackedEventHandler.class.getName();

    public void onEventBackgroundThread(final LocationTrackedEvent e) {
        final GeoLocationDao geoLocationDao = DBHelper.getGeoLocationDao(e.getContext());
        Log.d(TAG, "Storing to db...");
        geoLocationDao.insert(e.getGeoLocation());
        Log.d(TAG, "done.");
    }
}
