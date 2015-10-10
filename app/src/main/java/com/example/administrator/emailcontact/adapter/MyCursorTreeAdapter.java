package com.example.administrator.emailcontact.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.CursorTreeAdapter;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.administrator.emailcontact.R;
import com.example.administrator.emailcontact.database.ContactSQLiteHelper;
import com.example.administrator.emailcontact.database.GroupSQLiteHelper;
import com.example.administrator.emailcontact.model.ContactService;
import com.example.administrator.emailcontact.model.GroupService;

/**
 * Created by Administrator on 2015/10/9.
 */
public class MyCursorTreeAdapter extends CursorTreeAdapter {

    private Context mCtx;
    private LayoutInflater mInflater;
    private boolean isFirst = true;

    public MyCursorTreeAdapter(Cursor cursor, Context context) {
        super(cursor, context);
        this.mCtx = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    protected Cursor getChildrenCursor(Cursor cursor) {
        int child_parent = cursor.getInt(cursor.getColumnIndexOrThrow(GroupSQLiteHelper.GroupColumns._ID));
        GroupService mGroupService = new GroupService(mCtx);
        Cursor mCursor = mGroupService.queryParent(child_parent);
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
        CustExpListview view = new CustExpListview(mCtx);
        view.setPadding(20,0,0,0);
        return view;
    }

    @Override
    protected void bindChildView(View view, Context context, Cursor cursor, boolean b) {
        if (isFirst) {
            isFirst = false;
            MyCursorTreeAdapter2 myCursorTreeAdapter2 = new MyCursorTreeAdapter2(cursor, mCtx);
            view.setPadding(20,0,0,0);
            final CustExpListview mListView = (CustExpListview) view;//.findViewById(R.id.expandListView);
            mListView.setAdapter(myCursorTreeAdapter2);
        }
        if (b)
            isFirst = true;
    }

    public class CustExpListview extends ExpandableListView {

        public CustExpListview(Context context) {
            super(context);
        }

        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(960,
                    MeasureSpec.AT_MOST);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(600,
                    MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

}
