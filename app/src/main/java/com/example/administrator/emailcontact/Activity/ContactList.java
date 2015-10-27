package com.example.administrator.emailcontact.activity;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.emailcontact.R;
import com.example.administrator.emailcontact.adapter.RecyclerAdapter;
import com.example.administrator.emailcontact.model.Contact;
import com.example.administrator.emailcontact.model.ContactService;

/**
 * Created by Administrator on 2015/9/21.
 */
public class ContactList extends ListActivity implements AdapterView.OnItemClickListener {

    private RecyclerAdapter mAdapter;
    private Button mOK;
    private Button mAll;
    private Button mCancelAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_list);
        initActivity();
    }

    private void initActivity() {
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
        ContactService mService = new ContactService(ContactList.this);
        Cursor mCursor = mService.defaultQuery();
        mAdapter = new RecyclerAdapter(ContactList.this, mCursor, true);
        setListAdapter(mAdapter);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        showMenuDialog(l);
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
                Contact mContact = new Contact(number, displayName, email, Integer.parseInt(type));
                doUpdate(id, mContact);
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

    private void doUpdate(long id, Contact contact) {
        ContactService mService = new ContactService(this);
        mService.updateContact((int) id, contact);
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
