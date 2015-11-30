package com.example.administrator.emailcontact.Activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.emailcontact.R;
import com.example.administrator.emailcontact.model.GroupService;
import com.example.administrator.emailcontact.provider.Groups;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 添加分组页面
 * Created by Administrator on 2015/11/1.
 */
public class AddGroup extends Activity {

    private Button mBack;
    private TextView mTitle;
    private Button mOK;
    private EditText mGroupName;
    private GroupService mGroupService;
    private Spinner mSpinnerParent;
    private HashMap<Integer, Integer> mPosition2Id = new HashMap<>();

    public static void Instance(Context context) {
        Intent intent = new Intent(context, AddGroup.class);
        Bundle bundle;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {/* 跳转渐变效果 */
            bundle = ActivityOptions.makeCustomAnimation(context.getApplicationContext(), android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
//            context.startActivity(intent, bundle);
            ((Activity) context).startActivityForResult(intent, Groups.REQUEST_CREATE_GROUP, bundle);
        } else {
//            context.startActivity(intent);
            ((Activity) context).startActivityForResult(intent, Groups.REQUEST_CREATE_GROUP);
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
        mSpinnerParent = (Spinner) findViewById(R.id.group_parent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initSpinner();
    }

    private void initSpinner() {
        GroupService mGroupService = new GroupService(AddGroup.this);
        Cursor mCursor = mGroupService.defaultQuery();
        ArrayList<String> mArrayList = new ArrayList<>();
        mArrayList.add(getString(R.string.group_top));
        int position = 0;
        mPosition2Id.put(position, 0);
        while (mCursor.moveToNext()) {
            position++;
            int id = mCursor.getInt(mCursor.getColumnIndex(Groups.ID));
            mPosition2Id.put(position, id);
            String name = mCursor.getString(mCursor.getColumnIndex(Groups.NAME));
            mArrayList.add(name);
        }
//        SimpleCursorAdapter mAdatper = new SimpleCursorAdapter(AddGroup.this,
//                android.R.layout.simple_spinner_item, mCursor,new String[]{Groups.NAME},new int[]{android.R.id.text1}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        ArrayAdapter<String> mAdatper = new ArrayAdapter<String>(this, R.layout.layout, mArrayList);
        mAdatper.setDropDownViewResource(R.layout.layout_drop_down);
        mSpinnerParent.setAdapter(mAdatper);
    }

    private void insertParentNode() {
        if (TextUtils.isEmpty(mGroupName.getText().toString())) {
            Toast.makeText(AddGroup.this, R.string.hint_input_group_name, Toast.LENGTH_SHORT).show();
            return;
        }
        int parent = mPosition2Id.get(mSpinnerParent.getSelectedItemPosition());
        String groupName = mGroupName.getText().toString().trim();
        long id = mGroupService.insert(parent == 0 ? -1 : parent, 0, groupName);
        if (id > 0) {
            Toast.makeText(AddGroup.this, R.string.modify_success, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.putExtra(Groups.KEY_GROUP_ID_LONG, id);
            setResult(RESULT_OK, intent);
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
