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
import com.example.administrator.emailcontact.model.GroupService;

import org.w3c.dom.Text;

/**
 * Created by Administrator on 2015/10/9.
 */
public class MyCursorTreeAdapter extends CursorTreeAdapter {

    private Context mCtx;
    private LayoutInflater mInflater;

    public MyCursorTreeAdapter(Cursor cursor, Context context) {
        super(cursor, context);
        this.mCtx = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    protected Cursor getChildrenCursor(Cursor cursor) {
        int contact_type_id = cursor.getInt(cursor.getColumnIndexOrThrow(GroupSQLiteHelper.GroupColumns._ID));
        int group_parent = cursor.getInt(cursor.getColumnIndexOrThrow(GroupSQLiteHelper.GroupColumns.PARENT));
        if (group_parent == -1) {
            ContactService mContact = new ContactService(mCtx);
            return mContact.findByTypeId(contact_type_id);
        } else {
            setGroupCursor(cursor);
            return cursor;
//            GroupService mGroup = new GroupService(mCtx);
//            return mGroup.queryParent(group_parent);
        }
    }

    @Override
    protected View newGroupView(Context context, Cursor cursor, boolean b, ViewGroup viewGroup) {
        View view = mInflater.inflate(android.R.layout.simple_expandable_list_item_1, viewGroup, false);
        ((TextView) view).setText(cursor.getString(cursor.getColumnIndex(GroupSQLiteHelper.GroupColumns._NAME)));
        return view;
    }

    @Override
    protected void bindGroupView(View view, Context context, Cursor cursor, boolean b) {
        ((TextView) view).setText(cursor.getString(cursor.getColumnIndex(GroupSQLiteHelper.GroupColumns._NAME)));
    }

    @Override
    protected View newChildView(Context context, Cursor cursor, boolean b, ViewGroup viewGroup) {
        TextView view = (TextView) mInflater.inflate(android.R.layout.simple_expandable_list_item_1, viewGroup, false);
        view.setText(cursor.getString(cursor.getColumnIndex(ContactSQLiteHelper.ContactProviderColumns.EMAIL)));
        return view;
    }

    @Override
    protected void bindChildView(View view, Context context, Cursor cursor, boolean b) {
        ((TextView) view).setText(cursor.getString(cursor.getColumnIndex(ContactSQLiteHelper.ContactProviderColumns.EMAIL)));
    }
}
