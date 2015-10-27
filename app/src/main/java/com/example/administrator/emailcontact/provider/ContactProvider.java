package com.example.administrator.emailcontact.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.example.administrator.emailcontact.database.ContactSQLiteHelper;

import java.util.HashMap;

/**
 * Created by Administrator on 2015/9/18.
 */
public class ContactProvider extends ContentProvider {

    private static final String TAG = "ContactProvider";

    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(Contacts.AUTHORITY, "item", Contacts.ITEM);
        uriMatcher.addURI(Contacts.AUTHORITY, "item/#", Contacts.ITEM_ID);
        uriMatcher.addURI(Contacts.AUTHORITY, "pos/#", Contacts.ITEM_POS);
    }

    private ContactSQLiteHelper dbHelper = null;
    private ContentResolver resolver = null;

    private static final HashMap<String, String> contactProjectionMap;

    static {
        contactProjectionMap = new HashMap<>();
        contactProjectionMap.put(Contacts.ID, Contacts.ID);
        contactProjectionMap.put(Contacts.NUMBER, Contacts.NUMBER);
        contactProjectionMap.put(Contacts.DISPLAY_NAME, Contacts.DISPLAY_NAME);
        contactProjectionMap.put(Contacts.EMAIL, Contacts.EMAIL);
        contactProjectionMap.put(Contacts.TYPE_ID, Contacts.TYPE_ID);
    }

    @Override
    public boolean onCreate() {
        Context mContext = getContext();
        dbHelper = new ContactSQLiteHelper(mContext);
        resolver = mContext.getContentResolver();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        Log.i(TAG, "ArticlesProvider.query: " + uri);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        SQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();
        String limit = null;

        switch (uriMatcher.match(uri)) {
            case Contacts.ITEM: {
                sqlBuilder.setTables(Contacts.DATABASE_NAME);
                sqlBuilder.setProjectionMap(contactProjectionMap);
                break;
            }
            case Contacts.ITEM_ID: {
                String id = uri.getPathSegments().get(1);
                sqlBuilder.setTables(Contacts.DATABASE_NAME);
                sqlBuilder.setProjectionMap(contactProjectionMap);
                sqlBuilder.appendWhere(Contacts.ID + "=" + id);
                break;
            }
            case Contacts.ITEM_POS: {
                String pos = uri.getPathSegments().get(1);
                sqlBuilder.setTables(Contacts.DATABASE_NAME);
                sqlBuilder.setProjectionMap(contactProjectionMap);
                limit = pos + ", 1";
                break;
            }
            default:
                throw new IllegalArgumentException("Error Uri: " + uri);
        }

        Cursor cursor = sqlBuilder.query(db, projection, selection, selectionArgs, null, null,
                TextUtils.isEmpty(sortOrder) ? Contacts.DEFAULT_SORT_ORDER : sortOrder, limit);
        cursor.setNotificationUri(resolver, uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case Contacts.ITEM:
                return Contacts.CONTENT_TYPE;
            case Contacts.ITEM_ID:
            case Contacts.ITEM_POS:
                return Contacts.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Error Uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        if (uriMatcher.match(uri) != Contacts.ITEM) {
            throw new IllegalArgumentException("Error Uri: " + uri);
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long id = db.insert(Contacts.DATABASE_NAME, Contacts.ID, contentValues);
        if (id < 0) {
            throw new SQLiteException("Unable to insert " + contentValues + " for " + uri);
        }

        Uri newUri = ContentUris.withAppendedId(uri, id);
        resolver.notifyChange(newUri, null);

        return newUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count;

        switch (uriMatcher.match(uri)) {
            case Contacts.ITEM: {
                count = db.delete(Contacts.DATABASE_NAME, selection, selectionArgs);
                break;
            }
            case Contacts.ITEM_ID: {
                String id = uri.getPathSegments().get(1);
                count = db.delete(Contacts.DATABASE_NAME, Contacts.ID + "=" + id
                        + (!TextUtils.isEmpty(selection) ? " and (" + selection + ')' : ""), selectionArgs);
                break;
            }
            default:
                throw new IllegalArgumentException("Error Uri: " + uri);
        }

        resolver.notifyChange(uri, null);

        return count;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count;

        switch (uriMatcher.match(uri)) {
            case Contacts.ITEM: {
                count = db.update(Contacts.DATABASE_NAME, contentValues, selection, selectionArgs);
                break;
            }
            case Contacts.ITEM_ID: {
                String id = uri.getPathSegments().get(1);
                count = db.update(Contacts.DATABASE_NAME, contentValues, Contacts.ID + "=" + id
                        + (!TextUtils.isEmpty(selection) ? " and (" + selection + ')' : ""), selectionArgs);
                break;
            }
            default:
                throw new IllegalArgumentException("Error Uri: " + uri);
        }

        resolver.notifyChange(uri, null);

        return count;
    }

    @Override
    public Bundle call(String method, String request, Bundle args) {
        Log.i(TAG, "ContactProvider.call: " + method);

        if(method.equals(Contacts.METHOD_GET_ITEM_COUNT)) {
            return getItemCount();
        }

        throw new IllegalArgumentException("Error method call: " + method);
    }

    private Bundle getItemCount() {
        Log.i(TAG, "ArticlesProvider.getItemCount");

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from " + Contacts.TABLE_NAME, null);

        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        Bundle bundle = new Bundle();
        bundle.putInt(Contacts.KEY_ITEM_COUNT, count);

        cursor.close();
        db.close();

        return bundle;
    }
}
