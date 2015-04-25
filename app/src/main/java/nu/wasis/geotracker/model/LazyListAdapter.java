package nu.wasis.geotracker.model;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import de.greenrobot.dao.query.LazyList;

/**
 * Copypasta @ https://groups.google.com/forum/?fromgroups=#!topic/greendao/vYTV2dd5aLw
 *
 * @param <T>
 */
public abstract class LazyListAdapter<T extends DomainObject> extends BaseAdapter {

    protected boolean dataValid;
    protected LazyList<T> lazyList;
    protected Context context;

    public LazyListAdapter(Context context, LazyList<T> lazyList) {
        this.lazyList = lazyList;
        this.dataValid = lazyList != null;
        this.context = context;
    }

    /**
     * Returns the list.
     *
     * @return the list.
     */
    public LazyList<T> getLazyList() {
        return lazyList;
    }

    /**
     * @see android.widget.ListAdapter#getCount()
     */
    @Override
    public int getCount() {
        if (dataValid && lazyList != null) {
            return lazyList.size();
        } else {
            return 0;
        }
    }

    /**
     * @see android.widget.ListAdapter#getItem(int)
     */
    @Override
    public T getItem(int position) {
        if (dataValid && lazyList != null) {
            return lazyList.get(position);
        } else {
            return null;
        }
    }

    /**
     * @see android.widget.ListAdapter#getItemId(int)
     */
    @Override
    public long getItemId(int position) {
        if (dataValid && lazyList != null) {
            T item = lazyList.get(position);
            if (item != null) {
                return item.getId();
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    /**
     * @see android.widget.ListAdapter#getView(int, View, ViewGroup)
     */
    public View getView(int position, View convertView, ViewGroup parent) {

        if (!dataValid) {
            throw new IllegalStateException("this should only be called when lazylist is populated");
        }

        T item = lazyList.get(position);
        if (item == null) {
            throw new IllegalStateException("Item at position " + position + " is null");
        }

        View v;
        if (convertView == null) {
            v = newView(context, item, parent);
        } else {
            v = convertView;
        }
        bindView(v, context, item);
        return v;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        if (dataValid) {
            T item = lazyList.get(position);
            View v;
            if (convertView == null) {
                v = newDropDownView(context, item, parent);
            } else {
                v = convertView;
            }
            bindView(v, context, item);
            return v;
        } else {
            return null;
        }
    }

    /**
     * Makes a new view to hold the data contained in the item.
     *
     * @param context Interface to application's global information
     * @param item    The object that contains the data
     * @param parent  The parent to which the new view is attached to
     * @return the newly created view.
     */
    public abstract View newView(Context context, T item, ViewGroup parent);

    /**
     * Makes a new drop down view to hold the data contained in the item.
     *
     * @param context Interface to application's global information
     * @param item    The object that contains the data
     * @param parent  The parent to which the new view is attached to
     * @return the newly created view.
     */
    public View newDropDownView(Context context, T item, ViewGroup parent) {
        return newView(context, item, parent);
    }

    /**
     * Bind an existing view to the data data contained in the item.
     *
     * @param view    Existing view, returned earlier by newView
     * @param context Interface to application's global information
     * @param item    The object that contains the data
     */
    public abstract void bindView(View view, Context context, T item);

}
