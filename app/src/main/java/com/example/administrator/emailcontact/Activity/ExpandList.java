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
import android.view.ViewGroup;
import android.widget.CursorTreeAdapter;

import com.example.administrator.emailcontact.R;
import com.example.administrator.emailcontact.adapter.ExpandListAdapter;
import com.example.administrator.emailcontact.adapter.MyCursorTreeAdapter;
import com.example.administrator.emailcontact.model.GroupService;
import com.example.administrator.emailcontact.view.MyExpandableListView;

/**
 * Created by Administrator on 2015/10/8.
 */
public class ExpandList extends ExpandableListActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expand_list);
        GroupService mGroup = new GroupService(this);
        Cursor mCursor = mGroup.queryParent(-1);
        MyCursorTreeAdapter mAdapter = new MyCursorTreeAdapter(mCursor, this);
        setListAdapter(mAdapter);
    }
}
