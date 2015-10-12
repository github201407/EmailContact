package com.example.administrator.emailcontact.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorTreeAdapter;
import android.widget.TextView;

import com.example.administrator.emailcontact.database.GroupSQLiteHelper;
import com.example.administrator.emailcontact.model.GroupService;
import com.example.administrator.emailcontact.view.MyExpandableListView;

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
        GroupService mGroupService = new GroupService(mCtx);
        Cursor mCursor = mGroupService.queryParent(contact_type_id);
        return mCursor;
    }

    @Override
    protected View newGroupView(Context context, Cursor cursor, boolean b, ViewGroup viewGroup) {
        View view = mInflater.inflate(android.R.layout.simple_expandable_list_item_1, viewGroup, false);
        return view;
    }

    @Override
    protected void bindGroupView(View view, Context context, Cursor cursor, boolean b) {
        ((TextView) view).setText(cursor.getString(cursor.getColumnIndex(GroupSQLiteHelper.GroupColumns._NAME)));
    }

    @Override
    protected View newChildView(Context context, Cursor cursor, boolean b, ViewGroup viewGroup) {
        MyExpandableListView view = new MyExpandableListView(context);
        view.setPadding(20, 0, 0, 0);
        return view;
    }

    @Override
    protected void bindChildView(View view, Context context, Cursor cursor, boolean b) {
        if (b) {
            MyExpandableListView mListView = (MyExpandableListView) view;
            MyCursorTreeAdapter2 myCursorTreeAdapter2 = new MyCursorTreeAdapter2(cursor, context);
            mListView.setAdapter(myCursorTreeAdapter2);
        }
    }

}