package com.example.administrator.emailcontact.activity;

import android.app.Activity;
import android.app.ExpandableListActivity;
import android.content.AsyncQueryHandler;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.CursorTreeAdapter;

import com.example.administrator.emailcontact.R;
import com.example.administrator.emailcontact.adapter.ExpandListAdapter;
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
    }
}
