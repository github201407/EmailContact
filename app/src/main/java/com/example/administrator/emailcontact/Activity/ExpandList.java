package com.example.administrator.emailcontact.activity;

import android.app.ExpandableListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.administrator.emailcontact.R;
import com.example.administrator.emailcontact.adapter.MyCursorTreeAdapter;
import com.example.administrator.emailcontact.model.GroupService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/10/8.
 */
public class ExpandList extends ExpandableListActivity implements View.OnClickListener {

    private List<String> mEmails = new ArrayList<String>();
    private Button mOK;
    private Button mAll;
    private Button mCancelAll;
    public static int checkedId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expand_list);

        GroupService mGroup = new GroupService(this);
        Cursor mCursor = mGroup.queryParent(-1);
        MyCursorTreeAdapter mAdapter = new MyCursorTreeAdapter(mCursor, this, mEmails);
        setListAdapter(mAdapter);
        initActivity();

    }

    private void initActivity() {
        mOK = (Button) findViewById(R.id.ok);
        mAll = (Button) findViewById(R.id.modify);
        mCancelAll = (Button) findViewById(R.id.delete);
        mAll.setOnClickListener(this);
        mOK.setOnClickListener(this);
        mCancelAll.setOnClickListener(this);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ok:
                doOK();
                break;
            case R.id.modify:
                doModify();
                break;
            case R.id.delete:
                doDelete();
                break;
            default:
                break;
        }
    }

    private void doDelete() {
        if (checkedId != 0)
            Toast.makeText(ExpandList.this, "" + checkedId, Toast.LENGTH_SHORT).show();
    }

    private void doModify() {
//        if(mEmails.size() > 0)
        if (checkedId != 0)
            Toast.makeText(ExpandList.this, "" + checkedId, Toast.LENGTH_SHORT).show();

    }

    private void doOK() {
        StringBuilder mBuilder = new StringBuilder();
        if (mEmails.size() > 0) {
            for (String email : mEmails)
                mBuilder.append(email + ",");
            String emails = mBuilder.toString();
            Toast.makeText(ExpandList.this, emails, Toast.LENGTH_SHORT).show();
        }
    }
}
