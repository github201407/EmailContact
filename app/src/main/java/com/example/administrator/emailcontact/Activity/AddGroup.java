package com.example.administrator.emailcontact.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.emailcontact.R;
import com.example.administrator.emailcontact.model.GroupService;

/**
 * Created by Administrator on 2015/11/1.
 */
public class AddGroup extends Activity {

    private Button mBack;
    private TextView mTitle;
    private Button mOK;
    private EditText mGroupName;
    private GroupService mGroupService;

    public static void Instance(Context context) {
        Intent intent = new Intent(context, AddGroup.class);
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
        setContentView(R.layout.add_group);
        initActivity();
        mGroupService = new GroupService(this);
    }

    private void initActivity() {
        mBack = (Button) findViewById(R.id.back);
        mBack.setText(R.string.add_contact);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityFinish();
            }
        });
        mTitle = (TextView) findViewById(R.id.title);
        mTitle.setText(R.string.add_group);
        mOK = (Button) findViewById(R.id.ok);
        mGroupName = (EditText) findViewById(R.id.group_name);
        mOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertParentNode();
            }
        });
    }

    private void insertParentNode() {
        String groupName = mGroupName.getText().toString().trim();
        long id = mGroupService.insertParentNode(groupName);
        if (id > 0) {
            Toast.makeText(AddGroup.this, R.string.modify_success, Toast.LENGTH_SHORT).show();
            activityFinish();
        } else {
            Toast.makeText(AddGroup.this, R.string.modify_fail, Toast.LENGTH_SHORT).show();
        }
    }

    private void activityFinish() {
        AddGroup.this.finish();
        AddGroup.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
