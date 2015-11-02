package com.example.administrator.emailcontact.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.emailcontact.R;
import com.example.administrator.emailcontact.model.Contact;
import com.example.administrator.emailcontact.model.ContactService;
import com.example.administrator.emailcontact.model.GroupService;
import com.example.administrator.emailcontact.provider.Groups;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2015/10/29.
 */
public class ModifyContact extends Activity {

    public static final int CONTACT_MODIFY = 1;
    public static final int SEARCH_MODIFY = CONTACT_MODIFY + 1;
    public static final int CONTACT_SHOW = CONTACT_MODIFY + 2;
    public static final int SEARCH_SHOW = CONTACT_MODIFY + 3;
    public static final int CONTACT_ADD = CONTACT_MODIFY + 4;
    public static final int K9_SHOW = CONTACT_MODIFY + 5;
    public static final String K9_CONTACT = "k9_contact";

    private Button mBack;
    private TextView mTitle;
    private Button mOK;
    private TextView mIdLabel;
    private EditText mId, mDisplayName, mEmail, mPhone;
    private Spinner mSpinner;
    private Button mAddGroup;
    private int selectedItemID = 0;
    /* <group._id,position>*/
    private HashMap<Integer, Integer> mPosition2Id = new HashMap<>();

    public static void Instance(Context context, int id, int from) {
        Intent intent = new Intent(context, ModifyContact.class);
        intent.putExtra("contact_id", id);
        intent.putExtra("from", from);
        Bundle bundle;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            bundle = ActivityOptions.makeCustomAnimation(context.getApplicationContext(), android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
            context.startActivity(intent, bundle);
        } else {
            context.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_contact);
        initActivity();
    }

    private void initActivity() {
        mBack = (Button) findViewById(R.id.back);
        mTitle = (TextView) findViewById(R.id.title);
        mOK = (Button) findViewById(R.id.ok);
        mIdLabel = (TextView) findViewById(R.id.id_label);
        mId = (EditText) findViewById(R.id.id);
        mDisplayName = (EditText) findViewById(R.id.displayName);
        mEmail = (EditText) findViewById(R.id.email);
        mPhone = (EditText) findViewById(R.id.number);
        mSpinner = (Spinner) findViewById(R.id.spinner);
        mAddGroup = (Button) findViewById(R.id.add_group);
        mAddGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddGroup.Instance(view.getContext());
            }
        });
        initSpinner();
        initTitleBar();
        initContactInfo();
    }

    private void initSpinner() {
        GroupService mGroupService = new GroupService(ModifyContact.this);
        Cursor mCursor = mGroupService.defaultQuery();
        ArrayList<String> mArrayList = new ArrayList<>();
        mArrayList.add(getString(R.string.group_null));
        int position = 0;
        mPosition2Id.put(0, position);
        while (mCursor.moveToNext()) {
            position++;
            int id = mCursor.getInt(mCursor.getColumnIndex(Groups.ID));
            mPosition2Id.put(id, position);
            String name = mCursor.getString(mCursor.getColumnIndex(Groups.NAME));
            mArrayList.add(name);
        }
        ArrayAdapter<String> mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mArrayList);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(mAdapter);
    }

    private void initTitleBar() {
        final int strId = getIntent().getIntExtra("from", 0);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityFinish();
            }
        });
        mOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (strId == CONTACT_ADD)
                    doAdd();
                else
                    doModify(v);
            }
        });
        switch (strId) {
            case SEARCH_SHOW:
                mTitle.setText(R.string.show_contact);
                mBack.setText(R.string.search);
                mOK.setText(R.string.modify);
                ShowOrEditView(false);
                break;
            case CONTACT_SHOW:
                mTitle.setText(R.string.show_contact);
                mBack.setText(R.string.contact);
                mOK.setText(R.string.modify);
                ShowOrEditView(false);
                break;
            case CONTACT_MODIFY:
                mTitle.setText(R.string.modify_contact);
                mBack.setText(R.string.contact);
                break;
            case SEARCH_MODIFY:
                mTitle.setText(R.string.modify_contact);
                mBack.setText(R.string.search);
                break;
            case CONTACT_ADD:
                mIdLabel.setVisibility(View.INVISIBLE);
                mId.setVisibility(View.INVISIBLE);
                mTitle.setText(R.string.add_contact);
                mBack.setText(R.string.contact);
                break;
        }
    }

    private void initContactInfo() {
        int id = getIntent().getIntExtra("contact_id", -1);
        if (id > 0) {
            initData(id);
        }
        int strId = getIntent().getIntExtra("from", 0);
        if (strId == K9_SHOW) {
            Contact mContact = (Contact) getIntent().getSerializableExtra(K9_CONTACT);
            addDataByContact(mContact);
        }
    }

    private void initData(int id) {
        ContactService mService = new ContactService(ModifyContact.this);
        Contact mContact = mService.find(id);
        addDataByContact(mContact);
    }

    private void addDataByContact(Contact mContact) {
        mId.setText(String.valueOf(mContact.getId()));
        mId.setEnabled(false);
        mDisplayName.setText(mContact.getDisplay_name());
        mEmail.setText(mContact.getEmail());
        mPhone.setText(mContact.getNumber());
        mSpinner.setSelection(mPosition2Id.containsKey(mContact.getType()) ? mPosition2Id.get(mContact.getType()) : 0);
    }

    private void doAdd() {
        //int id = Integer.valueOf(mId.getText().toString());
        String displayName = mDisplayName.getText().toString();
        String email = mEmail.getText().toString();
        String phone = mPhone.getText().toString();
        int type = mSpinner.getSelectedItemPosition();
        ContactService mService = new ContactService(ModifyContact.this);
        Contact contact = new Contact(phone, displayName, email, type);
        long rows = mService.insert(contact);
        int msgId;
        if (rows > 0) {
            msgId = R.string.modify_success;
            Toast.makeText(ModifyContact.this, msgId, Toast.LENGTH_SHORT).show();
            activityFinish();
        } else {
            msgId = R.string.modify_fail;
            Toast.makeText(ModifyContact.this, msgId, Toast.LENGTH_SHORT).show();
        }
    }

    private void ShowOrEditView(boolean isShow) {
        mDisplayName.setEnabled(isShow);
        mEmail.setEnabled(isShow);
        mPhone.setEnabled(isShow);
        mSpinner.setEnabled(isShow);
        mAddGroup.setEnabled(isShow);
    }

    private void activityFinish() {
        ModifyContact.this.finish();
        ModifyContact.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private void doModify(View v) {
        if (((Button) v).getText().toString().equals(getString(R.string.modify))) {
            ShowOrEditView(true);
            mTitle.setText(R.string.modify_contact);
            mOK.setText(R.string.ok);
            return;
        }
        doUpdate();
    }

    private void doUpdate() {
        int id = Integer.valueOf(mId.getText().toString());
        String displayName = mDisplayName.getText().toString();
        String email = mEmail.getText().toString();
        String phone = mPhone.getText().toString();
        int type = mSpinner.getSelectedItemPosition();
        ContactService mService = new ContactService(ModifyContact.this);
        Contact contact = new Contact(id, phone, displayName, email, type);
        int rows = mService.updateContact(contact);
        int msgId;
        if (rows > 0) {
            msgId = R.string.modify_success;
            Toast.makeText(ModifyContact.this, msgId, Toast.LENGTH_SHORT).show();
            activityFinish();
        } else {
            msgId = R.string.modify_fail;
            Toast.makeText(ModifyContact.this, msgId, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Groups.REQUEST_CREATE_GROUP)
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    initSpinner();
                    int groupId = (int) data.getLongExtra(Groups.KEY_GROUP_ID_LONG, 0);
                    mSpinner.setSelection(mPosition2Id.containsKey(groupId) ? mPosition2Id.get(groupId) : 0);
                }
            }
    }
}
