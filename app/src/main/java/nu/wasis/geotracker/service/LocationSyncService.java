package nu.wasis.geotracker.service;

import android.app.IntentService;
import android.content.Intent;

import com.google.gson.Gson;

import java.util.List;

import nu.wasis.geotracker.model.DBHelper;
import nu.wasis.geotracker.model.GeoLocation;
import nu.wasis.geotracker.model.GeoLocationDao;
import nu.wasis.geotracker.util.logging.Logger;

/**
 */
public class LocationSyncService extends IntentService {

    private static final Logger LOG = Logger.getLogger(LocationSyncService.class);

    public LocationSyncService() {
        super(LocationSyncService.class.getName());
        LOG.debug("Created");
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        // playbook:
        // get locations from db
        // convert to json array
        // post to service
        // if success: remove from db.
        // ...
        // profit
        final GeoLocationDao geoLocationDao = DBHelper.getGeoLocationDao(this);

        final List<GeoLocation> geoLocations = geoLocationDao.loadAll();
        LOG.debug("# of items to send: " + geoLocations.size());
        final String json = new Gson().toJson(geoLocations);
        LOG.debug(json);
        //...
        //geoLocationDao.deleteInTx(geoLocations);
    }

}
