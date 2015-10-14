package com.example.administrator.emailcontact.activity;

import android.app.AlertDialog;
import android.app.ExpandableListActivity;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
public class ExpandList extends ExpandableListActivity{

    private List<String> mEmails = new ArrayList<String>();
    private Button mOK;
    private Button mModify;
    private Button mDelete;
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

    private void doUpdate(long id, Contact contact) {
        ContactService mService = new ContactService(this);
        mService.updateContact((int) id, contact);
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
                Contact mContact = new Contact(number, displayName, email, Integer.parseInt(type));
                doUpdate(checkedId, mContact);
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
            setResult(RESULT_OK, getIntent().putExtra("email", emails));
        }
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAlertDialog != null && mAlertDialog.isShowing())
            mAlertDialog.dismiss();
    }
}
