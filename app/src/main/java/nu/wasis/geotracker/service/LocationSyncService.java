package nu.wasis.geotracker.service;

import android.app.IntentService;
import android.content.Intent;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.UnsupportedEncodingException;
import java.util.List;

import nu.wasis.geotracker.model.DBHelper;
import nu.wasis.geotracker.model.GeoLocation;
import nu.wasis.geotracker.model.GeoLocationDao;
import nu.wasis.geotracker.settings.GeoTrackerSettings;
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
        final GeoTrackerSettings settings = new GeoTrackerSettings(this);
        final String serviceUrl = null == settings.getServiceUrl() ? null : settings.getServiceUrl().toString();
        if (null == serviceUrl) {
            LOG.error("Service url is null, stopping the service.");
            LocationTrackerAlarmReceiver.stop(this);
            return;
        }

        final GeoLocationDao geoLocationDao = DBHelper.getGeoLocationDao(this);
        final List<GeoLocation> geoLocations = geoLocationDao.loadAll();
        LOG.debug("# of items to send: " + geoLocations.size());
        if (0 == geoLocations.size()) {
            LOG.debug("Nothing to do.");
            return;
        }
        final String json = new Gson().toJson(geoLocations);
        LOG.debug(json);
        try {
            final HttpClient httpclient = new DefaultHttpClient();
            final HttpPost httpPost = createHttpPost(geoLocations);
            final HttpResponse response = httpclient.execute(httpPost);
            if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                geoLocationDao.deleteInTx(geoLocations);
            } else {
                LOG.error("Error: " + response.getStatusLine());
            }
        } catch (final Exception e) {
            LOG.error("Could not post data: " + e.getMessage());
        }
    }

    private HttpPost createHttpPost(List<GeoLocation> geoLocations) throws UnsupportedEncodingException {
        final GeoTrackerSettings settings = new GeoTrackerSettings(this);
        final String serviceUrl = "" + settings.getServiceUrl();
        final String json = new Gson().toJson(geoLocations);
        final HttpPost httpPost = new HttpPost(serviceUrl);
        final StringEntity entity = new StringEntity(json);
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        httpPost.setHeader("Api-Key", settings.getApiKey());
        return httpPost;
    }

}
