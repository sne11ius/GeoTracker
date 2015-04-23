package nu.wasis.geotracker.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import java.util.Calendar;

public class GeoTrackerAlarmReceiver extends WakefulBroadcastReceiver {

    private static final String TAG = GeoTrackerAlarmReceiver.class.getName();

    private static final int REQUEST_CODE = 1337;

    private AlarmManager alarmManager;
    private PendingIntent alarmIntent;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        Log.d(TAG, "onReceive");
        final Intent service = new Intent(context, GeoReportService.class);

        startWakefulService(context, service);
    }

    public boolean isScheduled(final Context context) {
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, GeoTrackerAlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(context, REQUEST_CODE, intent, PendingIntent.FLAG_NO_CREATE);
        boolean isScheduled = null != alarmIntent;
        Log.d(TAG, "isScheduled: " + isScheduled);
        return isScheduled;
    }

    public void schedule(Context context, int minutes) {
        Log.d(TAG, "Scheduling");
        alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, GeoTrackerAlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(context, REQUEST_CODE, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + 100, 1000 * 60 * minutes, alarmIntent);
    }

    public void stop(Context context) {
        Log.d(TAG, "Stopping");
        if (!isScheduled(context)) {
            Log.w(TAG, "Service is not running.");
        } else {
            alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, GeoTrackerAlarmReceiver.class);
            alarmIntent = PendingIntent.getBroadcast(context, REQUEST_CODE, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            if (alarmManager!= null) {
                alarmManager.cancel(alarmIntent);
                alarmIntent.cancel();
            }
        }
        if (isScheduled(context)) {
            throw new RuntimeException("Service still running.");
        }
    }
}
