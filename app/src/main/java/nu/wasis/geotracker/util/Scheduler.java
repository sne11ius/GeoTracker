package nu.wasis.geotracker.util;

import android.content.Context;

import nu.wasis.geotracker.service.LocationSyncAlarmReceiver;
import nu.wasis.geotracker.service.LocationTrackerAlarmReceiver;

/**
 */
public class Scheduler {

    public static void schedule(final Context context) {
        LocationTrackerAlarmReceiver.schedule(context);
        LocationSyncAlarmReceiver.schedule(context);
    }

    //public static boolean isScheduled(final Context context) {
    //    return LocationTrackerAlarmReceiver.isScheduled(context) || LocationSyncAlarmReceiver.isScheduled(context);
    //}

    public static void stop(final Context context) {
        LocationTrackerAlarmReceiver.stop(context);
        LocationSyncAlarmReceiver.stop(context);
    }
}
