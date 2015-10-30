package com.example.administrator.emailcontact.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.emailcontact.R;
import com.example.administrator.emailcontact.adapter.RecyclerAdapter;
import com.example.administrator.emailcontact.model.Contact;
import com.example.administrator.emailcontact.model.ContactService;

/**
 * Created by Administrator on 2015/9/21.
 */
public class ContactList extends ListActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = "ContactList";
    private RecyclerAdapter mAdapter;
    private Button mOK;
    private Button mAll;
    private Button mCancelAll;
    private TextView mTitle;
    private Button mBack;
    private EditText mSearchEdt;
    private Button mSearchBtn;
    private ContactService mService;

    public static void InstanceList(Context context){
        Intent intent = new Intent(context,ContactList.class);
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
        setContentView(R.layout.contact_list);
        initActivity();
    }

    private void initActivity() {
        mTitle = (TextView) findViewById(R.id.title);
        mTitle.setText(R.string.search);
        mBack = (Button) findViewById(R.id.back);
        mBack.setText(R.string.contact);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExpandList.InstanceList(ContactList.this);
                ContactList.this.finish();
            }
        });
        mSearchEdt = (EditText) findViewById(R.id.search_edt);
        mSearchBtn = (Button) findViewById(R.id.search_btn);
        mSearchEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                Cursor mCursor = mService.search(text);
                mAdapter.changeCursor(mCursor);
                Log.e(TAG, text);
            }
        });
        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ContactList.InstanceList(ExpandList.this);
            }
        });
        mOK = (Button) findViewById(R.id.ok);
        mAll = (Button) findViewById(R.id.modify);
        mCancelAll = (Button) findViewById(R.id.delete);
        mAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.setAll();
            }
        });
        mOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doOK();
            }
        });
        mCancelAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.setCancelAll();
            }
        });
        mService = new ContactService(ContactList.this);
        Cursor mCursor = mService.defaultQuery();
        mAdapter = new RecyclerAdapter(ContactList.this, mCursor, true);
        setListAdapter(mAdapter);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //showMenuDialog(l);
        ModifyContact.Instance(ContactList.this, (int)l, ModifyContact.SEARCH_SHOW);
    }

    AlertDialog mAlertDialog = null;

    private void showMenuDialog(final long id) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(ContactList.this);
        mBuilder.setItems(new CharSequence[]{"Delete", "Modify"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case 0:
                        doDelete(id);
                        break;
                    case 1:
                        doModify(id);
                        break;
                    default:
                        break;
                }
            }
        });
        mAlertDialog = mBuilder.create();
        if (!isFinishing())
            mAlertDialog.show();
    }

    private void doModify(final long id) {
        ContactService mService = new ContactService(this);
        Contact contact = mService.find((int) id);
        View view = LayoutInflater.from(ContactList.this).inflate(R.layout.edit_dialog, null);
        EditText mId = (EditText) view.findViewById(R.id.id);
        mId.setEnabled(false);
        final EditText mDisplayName = (EditText) view.findViewById(R.id.displayName);
        final EditText mEmail = (EditText) view.findViewById(R.id.email);
        final EditText mNumber = (EditText) view.findViewById(R.id.number);
        final EditText mType = (EditText) view.findViewById(R.id.type);
        mId.setText(String.valueOf(id));
        mDisplayName.setText(contact.getDisplay_name());
        mEmail.setText(contact.getEmail());
        mNumber.setText(contact.getNumber());
        mType.setText("" + contact.getType());
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(ContactList.this);
        mBuilder.setView(view);
        mBuilder.setTitle("Edit..." + id);
        mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String displayName = mDisplayName.getText().toString();
                String email = mEmail.getText().toString();
                String number = mNumber.getText().toString();
                String type = mType.getText().toString();
                Contact mContact = new Contact((int)id, number, displayName, email, Integer.parseInt(type));
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

    private void doUpdate(Contact contact) {
        ContactService mService = new ContactService(this);
        mService.updateContact(contact);
        setListAdapter(null);
        Cursor mCursor = mService.defaultQuery();
        mAdapter = new RecyclerAdapter(ContactList.this, mCursor, true);
        setListAdapter(mAdapter);
    }

    private void doOK() {
        String str = mAdapter.getEmailStr();
        if (!TextUtils.isEmpty(str)) {
            Toast.makeText(ContactList.this, str, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.putExtra("email", str);
            setResult(10, intent);
        }
        finish();
    }

    private void doDelete(long id) {
        ContactService mService = new ContactService(ContactList.this);
        mService.delete((int) id);
        setListAdapter(null);
        Cursor mCursor = mService.defaultQuery();
        mAdapter = new RecyclerAdapter(ContactList.this, mCursor, true);
        setListAdapter(mAdapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAlertDialog != null && mAlertDialog.isShowing())
            mAlertDialog.dismiss();
    }
}
