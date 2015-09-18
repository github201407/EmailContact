package com.example.administrator.emailcontact.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.example.administrator.emailcontact.BuildConfig;
import com.example.administrator.emailcontact.database.ContactSQLiteHelper;

/**
 * Created by Administrator on 2015/9/18.
 */
public class ContactProvider extends ContentProvider {

    private static final String AUTHORITY = BuildConfig.APPLICATION_ID + ".contactprovider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    private static final String[] DEFAULT_PROJECTION = new String[] {
            ContactProviderColumns._ID,
            ContactProviderColumns.NUMBER,
            ContactProviderColumns.DISPLAY_NAME,
            ContactProviderColumns.EMAIL,
            ContactProviderColumns.TYPE,
    };

    public static class ContactProviderColumns {
        public static final String _ID = "_id";
        public static final String NUMBER = "_number";
        public static final String DISPLAY_NAME = "_display_name";
        public static final String EMAIL = "_email";
        public static final String TYPE = "_type";
    }

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
        ContactSQLiteHelper mSQLiteHelper = ContactSQLiteHelper.getInstance(getContext());
        Cursor mCursor = mSQLiteHelper.query(columnNames, selection, selectionArgs, null, null, sortOrder);
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
