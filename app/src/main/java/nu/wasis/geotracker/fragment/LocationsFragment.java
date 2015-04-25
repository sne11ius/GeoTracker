package nu.wasis.geotracker.fragment;

import android.app.Fragment;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.joda.time.DateTime;

import de.greenrobot.dao.query.LazyList;
import nu.wasis.geotracker.R;
import nu.wasis.geotracker.model.DBConstants;
import nu.wasis.geotracker.model.DaoMaster;
import nu.wasis.geotracker.model.DaoSession;
import nu.wasis.geotracker.model.GeoLocation;
import nu.wasis.geotracker.model.GeoLocationDao;
import nu.wasis.geotracker.model.LazyListAdapter;
import nu.wasis.geotracker.util.DateUtil;

/**
 */
public class LocationsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final ListView rootView = (ListView) inflater.inflate(R.layout.fragment_locations, container, false);
        final GeoLocationDao geoLocationDao = getGeoLocationDao(getActivity());
        final LazyList<GeoLocation> locations = geoLocationDao.queryBuilder().orderDesc(GeoLocationDao.Properties.Time).listLazy();
        rootView.setAdapter(new GeoLocationLazyListAdapter(getActivity(), locations));
        return rootView;
    }

    private static GeoLocationDao getGeoLocationDao(final Context context) {
        final DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, DBConstants.DB_NAME, null);
        final SQLiteDatabase db = helper.getWritableDatabase();
        final DaoMaster daoMaster = new DaoMaster(db);
        final DaoSession session = daoMaster.newSession();
        return session.getGeoLocationDao();
    }

    private static final class GeoLocationLazyListAdapter extends LazyListAdapter<GeoLocation> {
        public GeoLocationLazyListAdapter(final Context context, final LazyList<GeoLocation> lazyList) {
            super(context, lazyList);
        }

        @Override
        public View newView(final Context context, final GeoLocation geoLocation, final ViewGroup parent) {
            if (geoLocation == null) return null;
            final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            return inflater.inflate(R.layout.location_list_item, parent, false);

        }

        @Override
        public void bindView(final View view, final Context context, final GeoLocation loc) {
            final TextView textView = (TextView) view;
            final DateTime dateTime = new DateTime(loc.getTime());
            final String date = dateTime.toString(DateUtil.FORMAT_SHORT);
            textView.setText("@ " + date + "\n" + loc.getLatitude() + "\\" + loc.getLongitude());
        }
    }

}
