package com.example.administrator.emailcontact.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.emailcontact.R;
import com.example.administrator.emailcontact.adapter.ContactAdapter;
import com.example.administrator.emailcontact.adapter.GroupExpandAdapter;
import com.example.administrator.emailcontact.adapter.MyCursorTreeAdapter;
import com.example.administrator.emailcontact.model.Contact;
import com.example.administrator.emailcontact.model.ContactItem;
import com.example.administrator.emailcontact.model.ContactService;
import com.example.administrator.emailcontact.model.Group;
import com.example.administrator.emailcontact.model.GroupService;
import com.example.administrator.emailcontact.provider.Contacts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * Created by Administrator on 2015/10/8.
 */
public class ExpandList extends ListActivity {

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
    private GroupExpandAdapter mAdapter2;
    private ContactAdapter adapter;

    public static void InstanceList(Context context) {
        Intent intent = new Intent(context, ExpandList.class);
        Bundle bundle;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            bundle = ActivityOptions.makeCustomAnimation(context.getApplicationContext(), android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
            context.startActivity(intent, bundle);
            ((Activity) context).finish();
        } else {
            context.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expand_list);
        mCurrent = -1;
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
//                doModify();
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
//        setListAdapter(mAdapter);
    }

    private void doDelete() {
        if (checkedId == 0)
            return;
        ContactService mService = new ContactService(this);
        mService.delete(checkedId);
        reInitExpandlistView();
    }

    private void doOK() {
        StringBuilder mBuilder = new StringBuilder();
        if (mEmails.size() > 0) {
            for (String email : mEmails)
                mBuilder.append(email + ",");
            String emails = mBuilder.toString();
            Toast.makeText(ExpandList.this, emails, Toast.LENGTH_SHORT).show();
            setResult(Contacts.CONTACT_PICK, getIntent().putExtra("email", emails));
        }
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
        mPositions.put(mCurrent, getListView().getFirstVisiblePosition());
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

    private Handler mHandler;
    private Runnable mUpdateFiles;

    @Override
    protected void onResume() {
        super.onResume();
        initAdapterData();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAdapter != null)
            mAdapter.changeCursor(null);
        mAdapter = null;
    }

    private GroupService mGService;
    private ContactService mCService;
    static private Map<Integer, Integer> mPositions = new HashMap<>();
    private int mCurrent;
    private ArrayList<Group> mGroups;
    private ArrayList<Contact> mContacts;
    private Stack<Integer> mStack = new Stack<>();
    private Stack<String> mStackName = new Stack<>();

    private void initAdapterData() {
        // Create a list adapter...
        adapter = new ContactAdapter(getLayoutInflater(), mEmails);
        setListAdapter(adapter);

        mGService = new GroupService(this);
        mCService = new ContactService(this);

        // ...that is updated dynamically when files are scanned
        mHandler = new Handler();
        mUpdateFiles = new Runnable() {
            public void run() {
//                mChild = 0;
                mGroups = mGService.queryTopParent(mCurrent);
                mContacts = mCService.queryContactByGroupId(mCurrent == -1 ? 0 : mCurrent);

                Collections.sort(mGroups, new Comparator<Group>() {
                    @Override
                    public int compare(Group lhs, Group rhs) {
                        return lhs.getName().compareToIgnoreCase(rhs.getName());
                    }
                });

                Collections.sort(mContacts, new Comparator<Contact>() {
                    @Override
                    public int compare(Contact lhs, Contact rhs) {
                        return lhs.getName().compareToIgnoreCase(rhs.getName());
                    }
                });

                adapter.clear();
                if (mStack.size() > 0)
                    adapter.add(new ContactItem(ContactItem.Type.PARENT, mStackName.peek(), ""));
                for (Group group : mGroups)
                    adapter.add(new ContactItem(ContactItem.Type.GROUP, group.getName(), group));
                for (Contact contact : mContacts)
                    adapter.add(new ContactItem(ContactItem.Type.CONTACT, contact.getName(), contact));

                lastPosition();
            }
        };

        // Start initial file scan...
        mHandler.post(mUpdateFiles);

    }

    private void lastPosition() {
        if (mPositions.containsKey(mCurrent))
            getListView().setSelection(mPositions.get(mCurrent));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        int firstVisiblePosition = getListView().getFirstVisiblePosition();
        mPositions.put(mCurrent, firstVisiblePosition);

        if (position < (mStack.size() > 0 ? 1 : 0)) {
            mCurrent = mStack.pop();
            mStackName.pop();
            mHandler.post(mUpdateFiles);
            return;
        }

        position -= (mStack.size() > 0 ? 1 : 0);

        if (position < mGroups.size()) {
            mStack.add(mCurrent);
            mStackName.add(mGroups.get(position).getName());
            mCurrent = mGroups.get(position).getId();
            mHandler.post(mUpdateFiles);
            return;
        }

        position -= mGroups.size();

        int contactId = mContacts.get(position).getId();
        ModifyContact.Instance(this, contactId, ModifyContact.CONTACT_SHOW);
    }

}
