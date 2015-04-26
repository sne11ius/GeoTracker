package nu.wasis.geotracker.service;

import android.content.Context;

import nu.wasis.geotracker.settings.GeoTrackerSettings;

/**
 */
public class LocationSyncAlarmReceiver extends AbstractSimpleIntentServiceReceiver<LocationSyncService> {

    private static final int REQUEST_CODE = 1338;

    @Override
    protected Class<LocationSyncService> getServiceClass() {
        return LocationSyncService.class;
    }

    //public static boolean isScheduled(final Context context) {
    //    return isScheduled(context, LocationSyncAlarmReceiver.class);
    //}

    public static void schedule(final Context context) {
        schedule(context, LocationSyncAlarmReceiver.class, 1000 * new GeoTrackerSettings(context).getSyncInterval(), REQUEST_CODE);
    }

    public static void stop(final Context context) {
        stop(context, LocationSyncAlarmReceiver.class, REQUEST_CODE);
    }

}
