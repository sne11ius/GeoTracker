package nu.wasis.geotracker.util;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

/**
 */
public final class DateUtil {

    private DateUtil() {
        // static only
    }

    public static final DateTimeFormatter FORMAT_SHORT = new DateTimeFormatterBuilder()
            .appendYear(4, 4)
            .appendLiteral("-")
            .appendMonthOfYear(2)
            .appendLiteral("-")
            .appendDayOfMonth(2)
            .appendLiteral("T")
            .appendHourOfDay(2)
            .appendLiteral(":")
            .appendMinuteOfHour(2)
            .toFormatter();

}
