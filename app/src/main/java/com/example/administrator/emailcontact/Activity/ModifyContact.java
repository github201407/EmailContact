package com.example.administrator.emailcontact.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.administrator.emailcontact.R;
import com.example.administrator.emailcontact.model.Contact;
import com.example.administrator.emailcontact.model.ContactService;

/**
 * Created by Administrator on 2015/10/29.
 */
public class ModifyContact extends Activity {

    private Button mBack;
    private TextView mTitle;
    private Button mOK;
    private EditText mId, mDisplayName, mEmail, mPhone, mType;

    public static void Instance(Context context, int id, int from) {
        Intent intent = new Intent(context, ModifyContact.class);
        intent.putExtra("contact_id", id);
        intent.putExtra("from", from);
        Bundle bundle;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
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
        int strId = getIntent().getIntExtra("from", 0);
        mBack.setText(strId == 0 ? R.string.back : strId);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModifyContact.this.finish();
                ModifyContact.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        mTitle = (TextView) findViewById(R.id.title);
        mTitle.setText(R.string.modifycontact);
        mOK = (Button) findViewById(R.id.ok);
        mOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doModify();
            }
        });

        mId = (EditText) findViewById(R.id.id);
        mDisplayName = (EditText) findViewById(R.id.displayName);
        mEmail = (EditText) findViewById(R.id.email);
        mPhone = (EditText) findViewById(R.id.number);
        mType = (EditText) findViewById(R.id.type);
        int id = getIntent().getIntExtra("contact_id", 0);
        if (id < 1) return;
        ContactService mService = new ContactService(ModifyContact.this);
        Contact mContact = mService.find(id);
        mId.setText("" + mContact.getId());
        mDisplayName.setText("" + mContact.getDisplay_name());
        mEmail.setText("" + mContact.getEmail());
        mPhone.setText("" + mContact.getNumber());
        mType.setText("" + mContact.getType());

    }

    private void doModify() {

    }
}
