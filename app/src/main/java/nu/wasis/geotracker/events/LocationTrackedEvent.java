package nu.wasis.geotracker.events;

import android.content.Context;

import nu.wasis.geotracker.model.GeoLocation;

/**
 */
public class LocationTrackedEvent {


    private final Context context;
    private final GeoLocation geoLocation;

    public LocationTrackedEvent(final Context context, final GeoLocation geoLocation) {
        this.context = context;
        this.geoLocation = geoLocation;
    }

    public Context getContext() {
        return context;
    }

    public GeoLocation getGeoLocation() {
        return geoLocation;
    }

}
