package com.example.administrator.emailcontact.activity;

import android.app.ExpandableListActivity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.emailcontact.R;
import com.example.administrator.emailcontact.adapter.MyCursorTreeAdapter;
import com.example.administrator.emailcontact.model.GroupService;

/**
 * Created by Administrator on 2015/10/8.
 */
public class ExpandList extends ExpandableListActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expand_list);
        /* 查询联系人并获取其Cursor对象*/
//        Uri mUri=ContactsContract.Contacts.CONTENT_URI;//ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE
//        Cursor cursor=getContentResolver().query(mUri,null,null,null,null);
//        /* 保存取联系人ID的列位置*/
//        int mGroupIdColumnIndex = cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID);
//        /*设置 adapter*/
//        ExpandListAdapter mAdapter = new ExpandListAdapter(
//                mGroupIdColumnIndex,
//                this,
//                cursor,
//                android.R.layout.simple_expandable_list_item_1,//组视图(联系人)
//                new String[]{ContactsContract.Contacts.DISPLAY_NAME}, //显示联系人名字
//                new int[]{android.R.id.text1},
//                android.R.layout.simple_expandable_list_item_1,//子视图(电话号码)
//                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER}, //显示电话号码
//                new int[]{android.R.id.text1});
//        setListAdapter(mAdapter);

        GroupService mGroup = new GroupService(this);
        Cursor mCursor = mGroup.queryParent(-1);
        MyCursorTreeAdapter mAdapter = new MyCursorTreeAdapter(mCursor, this);
        setListAdapter(mAdapter);

//        final ExpandableListView mListView = getExpandableListView();
//        mListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
//            @Override
//            public void onGroupExpand(int groupPosition) {
//                ViewGroup.LayoutParams params = mListView.getLayoutParams();
//                params.height = 600;
//                mListView.setLayoutParams(params);
//            }
//        });
//
//        mListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
//            @Override
//            public void onGroupCollapse(int groupPosition) {
//                ViewGroup.LayoutParams params = mListView.getLayoutParams();
//                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//                mListView.setLayoutParams(params);
//            }
//        });

//        setListAdapter(new ParentLevel());

    }

    public class ParentLevel extends BaseExpandableListAdapter {

        @Override
        public Object getChild(int arg0, int arg1) {
            return arg1;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            CustExpListview SecondLevelexplv = new CustExpListview(ExpandList.this);
            SecondLevelexplv.setAdapter(new SecondLevelAdapter());
            SecondLevelexplv.setGroupIndicator(null);
            return SecondLevelexplv;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return 3;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return groupPosition;
        }

        @Override
        public int getGroupCount() {
            return 5;
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            TextView tv = new TextView(ExpandList.this);
            tv.setText("->FirstLevel");
            tv.setTextColor(Color.BLACK);
            tv.setTextSize(20);
            tv.setBackgroundColor(Color.BLUE);
            tv.setPadding(10, 7, 7, 7);

            return tv;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
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

    public class SecondLevelAdapter extends BaseExpandableListAdapter {

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            TextView tv = new TextView(ExpandList.this);
            tv.setText("child");
            tv.setTextColor(Color.BLACK);
            tv.setTextSize(20);
            tv.setPadding(15, 5, 5, 5);
            tv.setBackgroundColor(Color.YELLOW);
            tv.setLayoutParams(new ListView.LayoutParams(
                    ListView.LayoutParams.FILL_PARENT, ListView.LayoutParams.FILL_PARENT));
            return tv;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return 5;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return groupPosition;
        }

        @Override
        public int getGroupCount() {
            return 1;
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            TextView tv = new TextView(ExpandList.this);
            tv.setText("-->Second Level");
            tv.setTextColor(Color.BLACK);
            tv.setTextSize(20);
            tv.setPadding(12, 7, 7, 7);
            tv.setBackgroundColor(Color.RED);

            return tv;
        }

        @Override
        public boolean hasStableIds() {
            // TODO Auto-generated method stub
            return true;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            // TODO Auto-generated method stub
            return true;
        }

    }
}
