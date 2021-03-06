package com.example.administrator.emailcontact.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.administrator.emailcontact.R;
import com.example.administrator.emailcontact.adapter.ContactAdapter;
import com.example.administrator.emailcontact.model.Contact;
import com.example.administrator.emailcontact.model.ContactItem;
import com.example.administrator.emailcontact.model.ContactService;
import com.example.administrator.emailcontact.model.Group;
import com.example.administrator.emailcontact.model.GroupService;
import com.example.administrator.emailcontact.model.JSONContact;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * 联系人主页面
 * Created by Administrator on 2015/10/8.
 */
public class ExpandList extends ListActivity {

    private static final String TAG = "ExpandList";
    private List<String> mEmails = new ArrayList<>();
    private Button mOK;
    private Button mBack;
    private Button mModify;
    private Button mDelete;
    private EditText mSearchEdt;
    private Button mSearchBtn;
    public static int checkedId = 0;
    private TextView mTitle;
    private ContactAdapter adapter;
    private Runnable mSearchFile;

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
                String back = mBack.getText().toString().trim();
                if (back.equals(getString(R.string.add)))
                    ModifyContact.Instance(ExpandList.this, 0, ModifyContact.CONTACT_ADD);
                else if (back.equals(getString(R.string.contact))) {
                    mCurrent = -1;
                    mHandler.post(mUpdateFiles);
                    mTitle.setText(R.string.contact);
                    mBack.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    mBack.setText(R.string.add);
                }
                hideKeyboard();
            }
        });
        mOK = (Button) findViewById(R.id.ok);
        mSearchEdt = (EditText) findViewById(R.id.search_edt);
//        mSearchEdt.setFocusable(false);
        mSearchBtn = (Button) findViewById(R.id.search_btn);
        mOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doOK();
            }
        });
        mSearchEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mSearchFile == null || mHandler == null)
                    return;
                mHandler.post(mSearchFile);
            }
        });
        mSearchEdt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mTitle.setText(R.string.search);
                    mBack.setText(R.string.contact);
                    mBack.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.back, 0, 0, 0);
                    mHandler.removeCallbacks(mUpdateFiles);
                    mHandler.post(mSearchFile);
                }
                return false;
            }
        });
        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mHandler.post(mSearchFile);
//                Bundle bundle = new Bundle();
//                bundle.putString("email", "123");
//                bundle.putString("name", "344");
//                ModifyContact.Instance(ExpandList.this, 0, ModifyContact.K9_SHOW, bundle);

            }
        });
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null)
            imm.hideSoftInputFromWindow(mSearchEdt.getWindowToken(), 0);
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

    /*确定按钮：回调选中的联系人 */
    private void doOK() {
        StringBuilder mBuilder = new StringBuilder();
        if (mEmails.size() > 0) {
            for (String email : mEmails)
                mBuilder.append(email + ",");
            String emails = mBuilder.toString();
            //Toast.makeText(ExpandList.this, emails, Toast.LENGTH_SHORT).show();
//            setResult(Contacts.CONTACT_PICK, getIntent().putExtra("email", emails));
            setResult(RESULT_OK, getIntent().putExtra("email", emails));
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

    /*进度条*/
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
//            downloadContact();
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
            mCurrent = -1;
            mHandler.post(mUpdateFiles);
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

    private GroupService mGService;
    private ContactService mCService;
    static private Map<Integer, Integer> mPositions = new HashMap<>();
    private int mCurrent;
    private ArrayList<Group> mGroups;
    private ArrayList<Contact> mContacts;
    private Stack<Integer> mStack = new Stack<>();
    private Stack<String> mStackName = new Stack<>();

    /* 初始化数据 */
    private void initAdapterData() {
        // Create a list adapter...
        adapter = new ContactAdapter(getLayoutInflater(), mEmails);
        setListAdapter(adapter);

        mGService = new GroupService(this);
        mCService = new ContactService(this);

        // ...that is updated dynamically when files are scanned
        mHandler = new Handler();
        mSearchFile = new Runnable() {
            @Override
            public void run() {/*搜索联系人*/
                String text = mSearchEdt.getText().toString().trim();
                mContacts = mCService.search2Array(text);
                mGroups.clear();
                mStack.clear();
                mStackName.clear();
                adapter.clear();
                for (Contact contact : mContacts)
                    adapter.add(new ContactItem(ContactItem.Type.CONTACT, contact.getName(), contact));
                Log.e(TAG, text);
                if (mContacts.size() == 0)
                    adapter.notifyDataSetChanged();
            }
        };
        mUpdateFiles = new Runnable() {
            public void run() {/*显示联系人*/
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
                    adapter.add(new ContactItem(ContactItem.Type.PARENT, getStack(), ""));
                for (Group group : mGroups)
                    adapter.add(new ContactItem(ContactItem.Type.GROUP, group.getName(), group));
                for (Contact contact : mContacts)
                    adapter.add(new ContactItem(ContactItem.Type.CONTACT, contact.getName(), contact));

                lastPosition();
            }
        };

        String title = mTitle.getText().toString().trim();
        if (title.equals(getString(R.string.contact)))
            mHandler.post(mUpdateFiles);
        else if (title.equals(getString(R.string.search)))
            mHandler.post(mSearchFile);
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
        String title = mTitle.getText().toString().trim();
        if (title.equals(getString(R.string.contact)))
            ModifyContact.Instance(this, contactId, ModifyContact.CONTACT_SHOW);
        else if (title.equals(getString(R.string.search)))
            ModifyContact.Instance(this, contactId, ModifyContact.SEARCH_SHOW);
    }

    /* 模拟网络数据 */
    public ArrayList<Contact> parseJson(String json) {
        String json1 = "{'contacts':[{'number':'123','name':'xiaoxiao','email':'123@qq.com','type':0}," +
                "{'number':'123','name':'xiaoxiao','email':'123@qq.com','type':0}," +
                "{'number':'123','name':'xiaoxiao','email':'123@qq.com','type':0}]}";
        JSONContact contacts = JSON.parseObject(json1, JSONContact.class);
        ArrayList<Contact> mArrays = contacts.getContacts();
        for (Contact contact : mArrays)
            Log.e(TAG, contact.toString());
        return mArrays;
    }

    /* 将下载的联系人，添加到数据库 */
    public void downloadContact() {
        ContactService mService = new ContactService(this);
        String url = "http://jensvn.duapp.com/jsonServlet?action=download&dataType=json";
        try {
            String content = downloadUrl(url);
            Log.e(TAG, content);
            ArrayList<Contact> mContacts = parseJson(content);
            for (Contact contact : mContacts)
                mService.insert(contact);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /* 根据url下载联系人 */
    public String downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 500;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d(TAG, "The response is: " + response);
            String contentAsString = "";
            if (response == 200) {
                is = conn.getInputStream();
                contentAsString = readIt(is);
            }

            // Convert the InputStream into a string
            return contentAsString;
            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public String readIt(InputStream stream) throws IOException {
        Reader reader = new InputStreamReader(stream, "UTF-8");
        StringBuilder mBuilder = new StringBuilder();
        char[] buffer = new char[1024];
        while (reader.read(buffer) != -1) {
            mBuilder.append(buffer);
        }
        reader.close();
        return mBuilder.toString();
    }

    private String getStack() {
        Object[] stacks = mStackName.toArray();
        StringBuilder builder = new StringBuilder();
        for (Object object : stacks)
            builder.append("/").append(object);
        return builder.toString();
    }

    public void uploadContact() {

    }

}
