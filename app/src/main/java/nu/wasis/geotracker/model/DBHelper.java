package nu.wasis.geotracker.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 */
public class DBHelper {

    public static GeoLocationDao getGeoLocationDao(final Context context) {
        final DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, DBConstants.DB_NAME, null);
        final SQLiteDatabase db = helper.getWritableDatabase();
        final DaoMaster daoMaster = new DaoMaster(db);
        final DaoSession session = daoMaster.newSession();
        return session.getGeoLocationDao();
    }

}
