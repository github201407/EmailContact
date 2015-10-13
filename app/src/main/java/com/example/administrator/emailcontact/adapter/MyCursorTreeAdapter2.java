package com.example.administrator.emailcontact.adapter;

import android.content.Context;
import android.database.Cursor;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CursorTreeAdapter;
import android.widget.TextView;

import com.example.administrator.emailcontact.R;
import com.example.administrator.emailcontact.activity.ExpandList;
import com.example.administrator.emailcontact.database.ContactSQLiteHelper;
import com.example.administrator.emailcontact.database.GroupSQLiteHelper;
import com.example.administrator.emailcontact.model.ContactService;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by Administrator on 2015/10/10.
 */
public class MyCursorTreeAdapter2 extends CursorTreeAdapter {

    private Context mCtx;
    private LayoutInflater mInflater;
    private List<String> mEmails;
    private Stack<Integer> mCheckedHistory = new Stack<>();

    /**
     * Constructor. The adapter will call {@link Cursor#requery()} on the cursor whenever
     * it changes so that the most recent data is always displayed.
     *
     * @param cursor  The cursor from which to get the data for the groups.
     * @param context
     * @param mEmails
     */
    public MyCursorTreeAdapter2(Cursor cursor, Context context, List<String> mEmails) {
        super(cursor, context);
        this.mCtx = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mEmails = mEmails;
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
        View view = mInflater.inflate(R.layout.contact_list_item, parent, false);
        ViewHolder mHolder = new ViewHolder(view);
        view.setTag(mHolder);
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
        int id = cursor.getInt(cursor.getColumnIndex(ContactSQLiteHelper.ContactProviderColumns._ID));
        String name = cursor.getString(cursor.getColumnIndex(ContactSQLiteHelper.ContactProviderColumns.DISPLAY_NAME));
        String email = cursor.getString(cursor.getColumnIndex(ContactSQLiteHelper.ContactProviderColumns.EMAIL));
        ViewHolder mHolder = (ViewHolder) view.getTag();
        mHolder.setId(id);
        mHolder.setName(name);
        mHolder.setEmail(email);
        mHolder.setCheckBox(mEmails.contains(email) ? true : false);
    }

    class ViewHolder {
        int id;
        TextView text1;
        TextView text2;
        CheckBox checkBox;

        public ViewHolder(View view) {
            text1 = (TextView) view.findViewById(R.id.text1);
            text2 = (TextView) view.findViewById(R.id.text2);
            checkBox = (CheckBox) view.findViewById(R.id.checkbox);

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        if (!mEmails.contains(getEmail()))
                            mEmails.add(getEmail());
                        ExpandList.checkedId = getId();
                        mCheckedHistory.push(getId());
                    } else {
                        if (mEmails.contains(getEmail()))
                            mEmails.remove(getEmail());
                        if(!mCheckedHistory.isEmpty() && mCheckedHistory.peek() == getId()){
                            mCheckedHistory.pop();
                            if(!mCheckedHistory.isEmpty())
                                ExpandList.checkedId = mCheckedHistory.peek();
                        }

                    }

                }
            });
        }
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
        public void setName(String name) {
            text1.setText(name);
        }

        public String getName() {
            return text1.getText().toString();
        }

        public void setEmail(String email) {
            text2.setText(email);
        }

        public String getEmail() {
            return text2.getText().toString();
        }

        public void setCheckBox(boolean checked) {
            checkBox.setChecked(checked);
        }


    }
}
