package com.example.administrator.emailcontact;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.emailcontact.activity.ContactList;
import com.example.administrator.emailcontact.activity.ExpandList;
import com.example.administrator.emailcontact.model.Contact;
import com.example.administrator.emailcontact.model.ContactService;
import com.example.administrator.emailcontact.model.GroupService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mId;
    private EditText mDisplayName;
    private EditText mEmail;
    private EditText mNumber;
    private EditText mType;
    private Button mShow;
    private Button mExpand;
    private Button mAdd;
    private Button mSearch;
    private Button mDelete;
    private Button mUpdate;
    private Button mDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        initActivity();
    }

    private void initActivity() {
        mId = (EditText) findViewById(R.id.id);
        mDisplayName = (EditText) findViewById(R.id.displayName);
        mEmail = (EditText) findViewById(R.id.email);
        mNumber = (EditText) findViewById(R.id.number);
        mType = (EditText) findViewById(R.id.type);
        mShow = (Button) findViewById(R.id.show);
        mExpand = (Button) findViewById(R.id.expand);
        mAdd = (Button) findViewById(R.id.add);
        mSearch = (Button) findViewById(R.id.search);
        mDelete = (Button) findViewById(R.id.delete);
        mUpdate = (Button) findViewById(R.id.update);
        mDownload = (Button) findViewById(R.id.download);
        mShow.setOnClickListener(this);
        mExpand.setOnClickListener(this);
        mAdd.setOnClickListener(this);
        mSearch.setOnClickListener(this);
        mDelete.setOnClickListener(this);
        mUpdate.setOnClickListener(this);
        mDownload.setOnClickListener(this);
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
            case R.id.show:
                doShow();
                break;
            case R.id.expand:
                doExpand();
                break;
            case R.id.add:
                doAdd();
                break;
            case R.id.search:
                doSearch();
                break;
            case R.id.delete:
                doDelete();
                break;
            case R.id.update:
                doUpdate();
                break;
            case R.id.download:
                doDownload();
                break;
            default:
                break;
        }
    }

    private void doExpand() {
        Intent intent = new Intent(this, ExpandList.class);
        startActivityForResult(intent, 110);
    }

    private void doDownload() {
        new MyAsyncTask().execute();
    }

    private ProgressDialog dialog;
    private int progress = 0;
    private void showProgress(){
        dialog = new ProgressDialog(MainActivity.this);
        dialog.setTitle("DownLoading...");
        dialog.setCancelable(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setMax(100);
        dialog.show();
    }

    private class MyAsyncTask extends AsyncTask<Void,Integer,Void>{

        int rate = 0;

        @Override
        protected void onPreExecute() {
            showProgress();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            doBackWork();
            while (rate < 100) {
                rate += 10;
                publishProgress(rate);

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            dialog.setProgress(values[0].intValue());
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            dialog.dismiss();
        }
    }
    private void doBackWork() {
        Contact c1 = new Contact("123", "张三", "mail11@qq.com", 1);
        Contact c2 = new Contact("123", "李四", "mail22@qq.com", 2);
        Contact c3 = new Contact("123", "王五", "mail23@qq.com", 1);
        Contact c4 = new Contact("123", "老六", "mail24@qq.com", 3);
        Contact c5 = new Contact("123", "小七", "mail25@qq.com", 5);
        Contact c6 = new Contact("123", "小七1", "1mail25@qq.com", 7);
        Contact c7 = new Contact("123", "小七2", "2mail25@qq.com", 7);
        Contact c8 = new Contact("123", "小七3", "3mail25@qq.com", 7);
        ContactService mService = new ContactService(MainActivity.this);
        List<Contact> mList = new ArrayList<Contact>();
        mList.add(c1);
        mList.add(c2);
        mList.add(c3);
        mList.add(c4);
        mList.add(c5);
        mList.add(c6);
        mList.add(c7);
        mList.add(c8);
        for(Contact contact : mList)
            mService.insert(contact);

        GroupService mGroupService = new GroupService(this);
        mGroupService.insert(-1, 1, "dev 1");
        mGroupService.insert(-1, 1, "dev 2");
        mGroupService.insert(-1, 1, "dev 3");
        mGroupService.insert(-1, 1, "dev 4");
        mGroupService.insert( 1, 0, "dev 5");
        mGroupService.insert( 2, 0, "dev 6");
        mGroupService.insert( 2, 0, "dev 7");
        mGroupService.insert( 2, 0, "dev 8");
        mGroupService.insert( 3, 0, "dev 9");
        mGroupService.insert( 3, 0, "dev 10");

    }

    private void doUpdate() {
        String email = mEmail.getText().toString().trim();
        String strId = mId.getText().toString().trim();
        int id = Integer.valueOf(strId).intValue();
        ContactService mService = new ContactService(MainActivity.this);
        mService.update(id, email);
    }

    private void doDelete() {
        String strId = mId.getText().toString().trim();
        int id = Integer.valueOf(strId).intValue();
        ContactService mService = new ContactService(MainActivity.this);
        mService.delete(id);
    }

    private void doShow() {
        // Todo
        Intent intent = new Intent(MainActivity.this, ContactList.class);
        //startActivity(intent);
        startActivityForResult(intent,101);

    }

    private void doSearch() {
        String strId = mId.getText().toString().trim();
        int id = Integer.valueOf(strId).intValue();
        ContactService mService = new ContactService(MainActivity.this);
        Contact mContact = mService.find(id);
        if (mContact == null)
            return;
        mDisplayName.setText(mContact.getDisplay_name());
        mEmail.setText(mContact.getEmail());
        mNumber.setText(mContact.getNumber());
        mType.setText(mContact.getType());
    }

    private void doAdd() {
        String displayName = mDisplayName.getText().toString();
        String email = mEmail.getText().toString();
        String number = mNumber.getText().toString();
        String type = mType.getText().toString();
        ContactService mService = new ContactService(MainActivity.this);
        long id = mService.insert(new Contact(number, displayName, email, Integer.parseInt(type)));
        mId.setText("" + id);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 101) {
            if (resultCode == RESULT_OK)
                if (data != null)
                    Toast.makeText(MainActivity.this, "Result:" + data.getStringExtra("email"), Toast.LENGTH_SHORT).show();
        }
        else if(requestCode == 110){
            if (resultCode == RESULT_OK)
                if (data != null)
                    Toast.makeText(MainActivity.this, "Result:" + data.getStringExtra("email"), Toast.LENGTH_SHORT).show();
        }

    }
}
