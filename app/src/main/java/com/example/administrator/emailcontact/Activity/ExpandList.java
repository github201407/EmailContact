package com.example.administrator.emailcontact.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.ExpandableListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.emailcontact.R;
import com.example.administrator.emailcontact.adapter.MyCursorTreeAdapter;
import com.example.administrator.emailcontact.model.Contact;
import com.example.administrator.emailcontact.model.ContactService;
import com.example.administrator.emailcontact.model.GroupService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/10/8.
 */
public class ExpandList extends ExpandableListActivity {

    private List<String> mEmails = new ArrayList<String>();
    private Button mOK;
    private Button mBack;
    private Button mModify;
    private Button mDelete;
    private EditText mSearchEdt;
    private Button mSearchBtn;
    public static int checkedId = 0;
    private MyCursorTreeAdapter mAdapter;
    private TextView mTitle;

    public static void InstanceList(Context context){
        Intent intent = new Intent(context,ExpandList.class);
        Bundle bundle;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            bundle = ActivityOptions.makeCustomAnimation(context.getApplicationContext(), android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
            context.startActivity(intent, bundle);
            ((Activity)context).finish();
        }else {
            context.startActivity(intent);
        }
    }

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

        initActivity();

    }

    private void initActivity() {
        mTitle = (TextView) findViewById(R.id.title);
        mTitle.setText(R.string.contact);
        mBack = (Button) findViewById(R.id.back);
        mBack.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        mBack.setText(R.string.add);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModifyContact.Instance(ExpandList.this, 0, ModifyContact.CONTACT_ADD);
            }
        });
        mOK = (Button) findViewById(R.id.ok);
        mSearchEdt = (EditText) findViewById(R.id.search_edt);
        mSearchEdt.setFocusable(false);
        mSearchBtn = (Button) findViewById(R.id.search_btn);
        mModify = (Button) findViewById(R.id.modify);
        mDelete = (Button) findViewById(R.id.delete);
        mModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doModify();
            }
        });
        mOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doOK();
            }
        });
        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doDelete();
            }
        });
        mSearchEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactList.InstanceList(ExpandList.this);
            }
        });
        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactList.InstanceList(ExpandList.this);
            }
        });
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
        } else if (id == R.id.action_download) {
            doDownload();
        }

        return super.onOptionsItemSelected(item);
    }

    private void reInitExpandlistView() {
        GroupService mGroup = new GroupService(this);
        Cursor mCursor = mGroup.queryParent(-1);
        MyCursorTreeAdapter mAdapter = new MyCursorTreeAdapter(mCursor, this, mEmails);
        setListAdapter(mAdapter);
    }

    private void doDelete() {
        if (checkedId == 0)
            return;
        ContactService mService = new ContactService(this);
        mService.delete(checkedId);
        reInitExpandlistView();
    }

    private void doUpdate(Contact contact) {
        ContactService mService = new ContactService(this);
        mService.updateContact(contact);
        reInitExpandlistView();
    }

    AlertDialog mAlertDialog = null;

    private void doModify() {
        if (checkedId == 0)
            return;
        ContactService mService = new ContactService(this);
        Contact contact = mService.find(checkedId);
        View view = LayoutInflater.from(ExpandList.this).inflate(R.layout.edit_dialog, null);
        EditText mId = (EditText) view.findViewById(R.id.id);
        mId.setEnabled(false);
        final EditText mDisplayName = (EditText) view.findViewById(R.id.displayName);
        final EditText mEmail = (EditText) view.findViewById(R.id.email);
        final EditText mNumber = (EditText) view.findViewById(R.id.number);
        final EditText mType = (EditText) view.findViewById(R.id.type);
        mId.setText(String.valueOf(checkedId));
        mDisplayName.setText(contact.getDisplay_name());
        mEmail.setText(contact.getEmail());
        mNumber.setText(contact.getNumber());
        mType.setText("" + contact.getType());

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(ExpandList.this);
        mBuilder.setView(view);
        mBuilder.setTitle("Edit:" + checkedId);
        mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String displayName = mDisplayName.getText().toString();
                String email = mEmail.getText().toString();
                String number = mNumber.getText().toString();
                String type = mType.getText().toString();
                Contact mContact = new Contact(checkedId, number, displayName, email, Integer.parseInt(type));
                doUpdate(mContact);
            }
        });
        mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        mAlertDialog = mBuilder.create();
        if (!isFinishing())
            mAlertDialog.show();
    }

    private void doOK() {
        StringBuilder mBuilder = new StringBuilder();
        if (mEmails.size() > 0) {
            for (String email : mEmails)
                mBuilder.append(email + ",");
            String emails = mBuilder.toString();
            Toast.makeText(ExpandList.this, emails, Toast.LENGTH_SHORT).show();
            setResult(10, getIntent().putExtra("email", emails));
        }
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAlertDialog != null && mAlertDialog.isShowing())
            mAlertDialog.dismiss();
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }

    private ProgressDialog dialog;

    private void showProgress() {
        dialog = new ProgressDialog(this);
        dialog.setTitle("DownLoading...");
        dialog.setCancelable(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setMax(100);
        dialog.show();
    }

    private void doDownload() {
        new MyAsyncTask().execute();
    }

    private class MyAsyncTask extends AsyncTask<Void, Integer, Void> {

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
        ContactService mService = new ContactService(this);
        List<Contact> mList = new ArrayList<Contact>();
        mList.add(c1);
        mList.add(c2);
        mList.add(c3);
        mList.add(c4);
        mList.add(c5);
        mList.add(c6);
        mList.add(c7);
        mList.add(c8);
        for (Contact contact : mList)
            mService.insert(contact);

        GroupService mGroupService = new GroupService(this);
        mGroupService.insert(-1, 1, "dev 1");
        mGroupService.insert(-1, 1, "dev 2");
        mGroupService.insert(-1, 1, "dev 3");
        mGroupService.insert(-1, 1, "dev 4");
        mGroupService.insert(1, 0, "dev 5");
        mGroupService.insert(2, 0, "dev 6");
        mGroupService.insert(2, 0, "dev 7");
        mGroupService.insert(2, 0, "dev 8");
        mGroupService.insert(3, 0, "dev 9");
        mGroupService.insert(3, 0, "dev 10");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GroupService mGroup = new GroupService(this);
        Cursor mCursor = mGroup.queryParent(-1);
        mAdapter = new MyCursorTreeAdapter(mCursor, this, mEmails);
        setListAdapter(mAdapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAdapter != null)
            mAdapter.changeCursor(null);
        mAdapter = null;
    }
}
