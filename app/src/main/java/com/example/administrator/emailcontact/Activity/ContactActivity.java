package com.example.administrator.emailcontact.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.administrator.emailcontact.R;
import com.example.administrator.emailcontact.adapter.LocalContactsAdapter;

/**
 * 本地联系人界面
 * Created by Administrator on 2015/11/30.
 */
public class ContactActivity extends Activity implements View.OnClickListener{
    Button mPhoneLableTv ;
    Button mGroundLableTv ;
    Button mCollectLableTv ;
    Button mContactLableTv ;
    private LinearLayout mSearchlayout ;
    EditText mSearchEdt;
    Button mSearchBtn;
    ListView mListView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_local);
        initView();
    }


    private void initView(){
        mPhoneLableTv = (Button) findViewById(R.id.table_phone);
        mGroundLableTv = (Button) findViewById(R.id.table_ground);
        mCollectLableTv = (Button) findViewById(R.id.table_collect);
        mContactLableTv = (Button) findViewById(R.id.table_contact);


        mSearchlayout = (LinearLayout) findViewById(R.id.search_layout);
        mSearchEdt = (EditText) findViewById(R.id.search_edt);
        mSearchBtn = (Button) findViewById(R.id.search_btn);
        mListView = (ListView) findViewById(R.id.listview_contact);
        LocalContactsAdapter localContactsAdapter = new LocalContactsAdapter();
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.table_phone :
                mPhoneLableTv.setTextSize(22);
                mGroundLableTv.setTextSize(20);
                mCollectLableTv.setTextSize(20);
                mContactLableTv.setTextSize(20);
                mSearchlayout.setVisibility(View.GONE);
                break;

            case R.id.table_ground :
                mPhoneLableTv.setTextSize(20);
                mGroundLableTv.setTextSize(22);
                mCollectLableTv.setTextSize(20);
                mContactLableTv.setTextSize(20);
                mSearchlayout.setVisibility(View.GONE);
                break;

            case R.id.table_collect :
                mPhoneLableTv.setTextSize(20);
                mGroundLableTv.setTextSize(20);
                mCollectLableTv.setTextSize(22);
                mContactLableTv.setTextSize(20);
                mSearchlayout.setVisibility(View.GONE);
                break;

            case R.id.table_contact :
                mPhoneLableTv.setTextSize(20);
                mGroundLableTv.setTextSize(20);
                mCollectLableTv.setTextSize(20);
                mContactLableTv.setTextSize(22);
                mSearchlayout.setVisibility(View.VISIBLE);
                break;
        }
    }
}
