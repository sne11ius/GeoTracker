package nu.wasis.geotracker.application;

import android.app.Application;

import net.danlew.android.joda.JodaTimeAndroid;

import de.greenrobot.event.EventBus;
import nu.wasis.geotracker.events.handler.LocationTrackedEventHandler;

/**
 */
public class GeoTrackerApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
        EventBus.getDefault().register(new LocationTrackedEventHandler());
    }
    
}
