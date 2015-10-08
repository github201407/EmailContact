package com.example.administrator.emailcontact.adapter;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.widget.SimpleCursorTreeAdapter;

import com.example.administrator.emailcontact.handler.QueryHandler;

/**
 * Created by mnq on 2015/10/8.
 */
public class ExpandListAdapter extends SimpleCursorTreeAdapter {

    private QueryHandler mQueryHandler;

    private int mGroupIdColumnIndex;

    private Context mCtx;

    private String mPhoneNumberProjection[] = new String[]{
            ContactsContract.CommonDataKinds.Phone._ID,
            ContactsContract.CommonDataKinds.Phone.NUMBER,};

    public ExpandListAdapter(int mGroupIdColumnIndex,Context context, Cursor cursor, int groupLayout, String[] groupFrom, int[] groupTo, int childLayout, String[] childFrom, int[] childTo) {
        super(context, cursor, groupLayout, groupFrom, groupTo, childLayout, childFrom, childTo);
        this.mCtx = context;
        this.mGroupIdColumnIndex = mGroupIdColumnIndex;
        //mQueryHandler = new QueryHandler(context, this);
    }

    @Override
    protected Cursor getChildrenCursor(Cursor cursor) {
        //mQueryHandler.startQuery(QueryHandler.TOKEN_CHILD, cursor.getPosition(), );
        //获取联系人ID
        Long contactId = cursor.getLong(mGroupIdColumnIndex);
        // 构造电话号码URI
        Uri.Builder builder = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                .buildUpon();
        Uri phoneNumbersUri = builder.build();
        //开始查询，Android推荐我们是其自身的managedQuery()方法进行查询，也就是自动管理Cursor。
        return ((Activity)mCtx).managedQuery(
                phoneNumbersUri,
                mPhoneNumberProjection,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? ",
                new String[]{String.valueOf(contactId)}, null);
    }
}
