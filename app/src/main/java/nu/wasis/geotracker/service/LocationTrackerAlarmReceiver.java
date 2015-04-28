package nu.wasis.geotracker.service;

import android.content.Context;

import nu.wasis.geotracker.settings.GeoTrackerSettings;

public final class LocationTrackerAlarmReceiver extends AbstractSimpleIntentServiceReceiver<LocationTrackerService> {

    private static final int REQUEST_CODE = 1337;

    @Override
    protected Class<LocationTrackerService> getServiceClass() {
        return LocationTrackerService.class;
    }

    public static boolean isScheduled(final Context context) {
        return isScheduled(context, LocationTrackerAlarmReceiver.class, REQUEST_CODE);
    }

    public static void schedule(final Context context) {
        schedule(context, LocationTrackerAlarmReceiver.class, 1000 * new GeoTrackerSettings(context).getGpsInterval(), REQUEST_CODE);
    }

    public static void stop(final Context context) {
        stop(context, LocationTrackerAlarmReceiver.class, REQUEST_CODE);
    }
}
