package com.example.administrator.emailcontact.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.example.administrator.emailcontact.BuildConfig;
import com.example.administrator.emailcontact.database.ContactSQLiteHelper;
import com.example.administrator.emailcontact.model.ContactService;

/**
 * Created by Administrator on 2015/9/18.
 */
public class ContactProvider extends ContentProvider {

    private static final String AUTHORITY = BuildConfig.APPLICATION_ID + ".contactprovider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    private static final String[] DEFAULT_PROJECTION = new String[] {
            ContactSQLiteHelper.ContactProviderColumns._ID,
            ContactSQLiteHelper.ContactProviderColumns.NUMBER,
            ContactSQLiteHelper.ContactProviderColumns.DISPLAY_NAME,
            ContactSQLiteHelper.ContactProviderColumns.EMAIL,
            ContactSQLiteHelper.ContactProviderColumns.TYPE_ID,
    };


    protected static final int NUMBER_INDEX = 1;
    protected static final int DISPLAY_NAME_INDEX = 2;
    protected static final int EMAIL_INDEX = 3;
    protected static final int TYPE_INDEX = 4;

    public enum TYPE {TYPE_WORK, TYPE_HOME};

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String[] columnNames = (projection == null) ? DEFAULT_PROJECTION : projection;
        ContactService mService = new ContactService(getContext());
        Cursor mCursor = mService.query(columnNames, null, null, null, null, null);
        return mCursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
