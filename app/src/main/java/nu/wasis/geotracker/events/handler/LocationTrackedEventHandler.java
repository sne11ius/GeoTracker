package nu.wasis.geotracker.events.handler;

import nu.wasis.geotracker.events.LocationTrackedEvent;
import nu.wasis.geotracker.model.DBHelper;
import nu.wasis.geotracker.model.GeoLocationDao;

/**
 */
public class LocationTrackedEventHandler {

    private static final String TAG = LocationTrackedEventHandler.class.getName();

    public void onEventBackgroundThread(final LocationTrackedEvent e) {
        final GeoLocationDao geoLocationDao = DBHelper.getGeoLocationDao(e.getContext());
        geoLocationDao.insert(e.getGeoLocation());
    }
}
