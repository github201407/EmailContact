package com.example.administrator.emailcontact.activity;

import android.app.ActionBar;
import android.app.ExpandableListActivity;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.administrator.emailcontact.R;
import com.example.administrator.emailcontact.adapter.MyCursorTreeAdapter;
import com.example.administrator.emailcontact.model.GroupService;

import static android.R.color.white;

/**
 * Created by Administrator on 2015/10/8.
 */
public class ExpandList extends ExpandableListActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expand_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.expandToolbar);
        toolbar.setTitle(getClass().getSimpleName());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            toolbar.setTitleTextColor(getColor(white));
        }
        toolbar.showOverflowMenu();
        toolbar.inflateMenu(R.menu.menu_expand_list);

        GroupService mGroup = new GroupService(this);
        Cursor mCursor = mGroup.queryParent(-1);
        MyCursorTreeAdapter mAdapter = new MyCursorTreeAdapter(mCursor, this);
        setListAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
