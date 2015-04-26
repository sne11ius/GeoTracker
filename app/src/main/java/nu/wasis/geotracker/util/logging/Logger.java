package nu.wasis.geotracker.util.logging;

import android.util.Log;

/**
 */
public class Logger {

    private String className;

    private Logger(final Class<?> clazz) {
        this.className = clazz.getName();
    }

    public static Logger getLogger(final Class<?> clazz) {
        return new Logger(clazz);
    }

    public void debug(final String message) {
        Log.d(className, message);
    }

    public void warn(final String message) {
        Log.w(className, message);
    }
}
