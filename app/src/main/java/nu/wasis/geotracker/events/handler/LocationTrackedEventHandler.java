package nu.wasis.geotracker.events.handler;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import nu.wasis.geotracker.events.LocationTrackedEvent;
import nu.wasis.geotracker.model.DBConstants;
import nu.wasis.geotracker.model.DaoMaster;
import nu.wasis.geotracker.model.DaoMaster.DevOpenHelper;
import nu.wasis.geotracker.model.DaoSession;
import nu.wasis.geotracker.model.GeoLocationDao;

/**
 */
public class LocationTrackedEventHandler {

    private static final String TAG = LocationTrackedEventHandler.class.getName();

    public void onEventBackgroundThread(final LocationTrackedEvent e) {
        final DevOpenHelper helper = new DevOpenHelper(e.getContext(), DBConstants.DB_NAME, null);
        final SQLiteDatabase db = helper.getWritableDatabase();
        final DaoMaster daoMaster = new DaoMaster(db);
        final DaoSession session = daoMaster.newSession();
        final GeoLocationDao geoLocationDao = session.getGeoLocationDao();
        Log.d(TAG, "Storing to db...");
        geoLocationDao.insert(e.getGeoLocation());
        Log.d(TAG, "done.");
    }
}
