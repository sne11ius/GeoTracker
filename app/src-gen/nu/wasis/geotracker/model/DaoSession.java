package nu.wasis.geotracker.model;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import nu.wasis.geotracker.model.GeoLocation;

import nu.wasis.geotracker.model.GeoLocationDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig geoLocationDaoConfig;

    private final GeoLocationDao geoLocationDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        geoLocationDaoConfig = daoConfigMap.get(GeoLocationDao.class).clone();
        geoLocationDaoConfig.initIdentityScope(type);

        geoLocationDao = new GeoLocationDao(geoLocationDaoConfig, this);

        registerDao(GeoLocation.class, geoLocationDao);
    }
    
    public void clear() {
        geoLocationDaoConfig.getIdentityScope().clear();
    }

    public GeoLocationDao getGeoLocationDao() {
        return geoLocationDao;
    }

}
