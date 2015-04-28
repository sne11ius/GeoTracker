package nu.wasis.geotracker.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import java.util.Calendar;

import nu.wasis.geotracker.util.logging.Logger;

/**
 */
public abstract class AbstractSimpleIntentServiceReceiver<T extends IntentService> extends WakefulBroadcastReceiver {

    private static final Logger LOG = Logger.getLogger(AbstractSimpleIntentServiceReceiver.class);

    protected abstract Class<T> getServiceClass();

    @Override
    public void onReceive(final Context context, final Intent intent) {
        startWakefulService(context, new Intent(context, getServiceClass()));
    }

    protected static void schedule(final Context context, final Class<?> derivedClass, final long intervalMillis, final int requestCode) {
        final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        final Intent intent = new Intent(context, derivedClass);
        final PendingIntent alarmIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + 100, intervalMillis, alarmIntent);
    }

    protected static boolean isScheduled(final Context context, final Class<? extends AbstractSimpleIntentServiceReceiver> derivedClass, final int requestCode) {
        final Intent intent = new Intent(context, derivedClass);
        final PendingIntent alarmIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_NO_CREATE);
        return null != alarmIntent;
    }

    protected static void stop(final Context context, final Class<? extends AbstractSimpleIntentServiceReceiver> derivedClass, final int requestCode) {
        if (!isScheduled(context, derivedClass, requestCode)) {
            LOG.warn("Service is not running.");
        } else {
            final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            final Intent intent = new Intent(context, derivedClass);
            final PendingIntent alarmIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            if (alarmManager != null) {
                alarmManager.cancel(alarmIntent);
                alarmIntent.cancel();
            }
        }
        if (isScheduled(context, derivedClass, requestCode)) {
            throw new RuntimeException("Service still running.");
        }
    }
}
