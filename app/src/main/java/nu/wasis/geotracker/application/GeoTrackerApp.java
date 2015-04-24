package nu.wasis.geotracker.application;

import android.app.Application;

import de.greenrobot.event.EventBus;
import nu.wasis.geotracker.events.handler.LocationTrackedEventHandler;

/**
 */
public class GeoTrackerApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(new LocationTrackedEventHandler());
    }
    
}
