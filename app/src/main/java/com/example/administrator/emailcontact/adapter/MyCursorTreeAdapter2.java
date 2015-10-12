package com.example.administrator.emailcontact.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorTreeAdapter;
import android.widget.TextView;

import com.example.administrator.emailcontact.database.ContactSQLiteHelper;
import com.example.administrator.emailcontact.database.GroupSQLiteHelper;
import com.example.administrator.emailcontact.model.ContactService;

/**
 * Created by Administrator on 2015/10/10.
 */
public class MyCursorTreeAdapter2 extends CursorTreeAdapter {

    private Context mCtx;
    private LayoutInflater mInflater;
    /**
     * Constructor. The adapter will call {@link Cursor#requery()} on the cursor whenever
     * it changes so that the most recent data is always displayed.
     *
     * @param cursor  The cursor from which to get the data for the groups.
     * @param context
     */
    public MyCursorTreeAdapter2(Cursor cursor, Context context) {
        super(cursor, context);
        this.mCtx = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * Gets the Cursor for the children at the given group. Subclasses must
     * implement this method to return the children data for a particular group.
     * <p/>
     * If you want to asynchronously query a provider to prevent blocking the
     * UI, it is possible to return null and at a later time call
     * {@link #setChildrenCursor(int, Cursor)}.
     * <p/>
     * It is your responsibility to manage this Cursor through the Activity
     * lifecycle. It is a good idea to use {@link Activity#managedQuery} which
     * will handle this for you. In some situations, the adapter will deactivate
     * the Cursor on its own, but this will not always be the case, so please
     * ensure the Cursor is properly managed.
     *
     * @param groupCursor The cursor pointing to the group whose children cursor
     *                    should be returned
     * @return The cursor for the children of a particular group, or null.
     */
    @Override
    protected Cursor getChildrenCursor(Cursor groupCursor) {
        int contact_type_id = groupCursor.getInt(groupCursor.getColumnIndexOrThrow(GroupSQLiteHelper.GroupColumns._ID));
        ContactService mContact = new ContactService(mCtx);
        return mContact.findByTypeId(contact_type_id);
    }

    /**
     * Makes a new group view to hold the group data pointed to by cursor.
     *
     * @param context    Interface to application's global information
     * @param cursor     The group cursor from which to get the data. The cursor is
     *                   already moved to the correct position.
     * @param isExpanded Whether the group is expanded.
     * @param parent     The parent to which the new view is attached to
     * @return The newly created view.
     */
    @Override
    protected View newGroupView(Context context, Cursor cursor, boolean isExpanded, ViewGroup parent) {
        View view = mInflater.inflate(android.R.layout.simple_expandable_list_item_1, parent, false);
        return view;
    }

    /**
     * Bind an existing view to the group data pointed to by cursor.
     *
     * @param view       Existing view, returned earlier by newGroupView.
     * @param context    Interface to application's global information
     * @param cursor     The cursor from which to get the data. The cursor is
     *                   already moved to the correct position.
     * @param isExpanded Whether the group is expanded.
     */
    @Override
    protected void bindGroupView(View view, Context context, Cursor cursor, boolean isExpanded) {
        ((TextView) view).setText(cursor.getString(cursor.getColumnIndex(GroupSQLiteHelper.GroupColumns._NAME)));
    }

    /**
     * Makes a new child view to hold the data pointed to by cursor.
     *
     * @param context     Interface to application's global information
     * @param cursor      The cursor from which to get the data. The cursor is
     *                    already moved to the correct position.
     * @param isLastChild Whether the child is the last child within its group.
     * @param parent      The parent to which the new view is attached to
     * @return the newly created view.
     */
    @Override
    protected View newChildView(Context context, Cursor cursor, boolean isLastChild, ViewGroup parent) {
        View view = mInflater.inflate(android.R.layout.simple_expandable_list_item_1, parent, false);
        return view;
    }

    /**
     * Bind an existing view to the child data pointed to by cursor
     *
     * @param view        Existing view, returned earlier by newChildView
     * @param context     Interface to application's global information
     * @param cursor      The cursor from which to get the data. The cursor is
     *                    already moved to the correct position.
     * @param isLastChild Whether the child is the last child within its group.
     */
    @Override
    protected void bindChildView(View view, Context context, Cursor cursor, boolean isLastChild) {
        ((TextView) view).setText(cursor.getString(cursor.getColumnIndex(ContactSQLiteHelper.ContactProviderColumns.EMAIL)));
    }
}
