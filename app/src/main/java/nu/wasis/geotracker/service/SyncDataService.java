package nu.wasis.geotracker.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;

import nu.wasis.geotracker.model.DBHelper;
import nu.wasis.geotracker.model.GeoLocationDao;

/**
 */
public class SyncDataService extends IntentService {

    private static final String TAG = SyncDataService.class.getName();

    public SyncDataService() {
        super(SyncDataService.class.getName());
        Log.d(TAG, "Created");
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        final GeoLocationDao geoLocationDao = DBHelper.getGeoLocationDao(this);
        String json = new Gson().toJson(geoLocationDao.loadAll());
        Log.d(TAG, json);
        // get locations from db
        // convert to json array
        // post to service
        // if success: remove from db.
        // ...
        // profit
    }

}
