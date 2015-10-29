package com.example.administrator.emailcontact.model;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.administrator.emailcontact.database.ContactSQLiteHelper;
import com.example.administrator.emailcontact.provider.Contacts;
import com.example.administrator.emailcontact.util.CursorUtil;

/**
 * Created by Administrator on 2015/9/21.
 */
public class ContactService {
    private ContentResolver mContentResolver = null;

    public ContactService(Context context) {
        mContentResolver = context.getContentResolver();
    }

    public long insert(Contact contact) {
        ContentValues cv = contact.getContentValues();
        Uri uri = mContentResolver.insert(Contacts.CONTENT_URI, cv);
        String idStr = uri.getPathSegments().get(1);
        long row = Long.valueOf(idStr);
        Log.e("sql", "insert:" + row);
        return row;
    }

    public void delete(int id) {
        Uri uri = ContentUris.withAppendedId(Contacts.CONTENT_URI, id);
        int rows = mContentResolver.delete(uri, null, null);
        Log.e("sql", "delete:" + rows);
    }

    public int update(int id, String email) {
        ContentValues cv = new ContentValues();
        cv.put(Contacts.EMAIL, email);
        Uri uri = ContentUris.withAppendedId(Contacts.CONTENT_URI, id);
        int rows = mContentResolver.update(uri, cv, null, null);
        Log.e("sql", "updateContact id:" + rows);
        return rows;
    }

    public int updateContact(int id, Contact contact) {
        contact.setId(id);
        ContentValues values = contact.getContentValues();
        Uri uri = ContentUris.withAppendedId(Contacts.CONTENT_URI, contact.getId());
        int rows = mContentResolver.update(uri, values, null, null);
        Log.e("sql", "updateContact id:" + rows);
        return rows;
    }

    public Cursor query(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        Cursor mCursor = mContentResolver.query(Contacts.CONTENT_URI, columns, selection, selectionArgs, Contacts.DEFAULT_SORT_ORDER);
        CursorUtil.addCursor(mCursor);
        return mCursor;
    }

    public Cursor defaultQuery() {
        return this.query(new String[]{Contacts.ID,
                Contacts.DISPLAY_NAME,
                Contacts.EMAIL}, null, null, null, null, null);
    }

    public Contact find(int id) {
        Uri uri = ContentUris.withAppendedId(Contacts.CONTENT_URI, id);
        String[] projection = {Contacts.DISPLAY_NAME, Contacts.NUMBER, Contacts.EMAIL, Contacts.TYPE_ID};
        Cursor cursor = mContentResolver.query(uri, projection, null, null, null);
        if (cursor.moveToNext()) {
            String display_Name = cursor.getString(0);
            String number = cursor.getString(1);
            String email = cursor.getString(2);
            int type = cursor.getInt(3);
            return new Contact(id, number, display_Name, email, type);
        }
        cursor.close();
        return null;
    }

    public Cursor findByTypeId(int typeId) {
        String[] columns = {
                Contacts.ID,
                Contacts.DISPLAY_NAME,
                Contacts.EMAIL,
                Contacts.NUMBER};
        String selection = Contacts.TYPE_ID + " = ?";
        String[] selectionArgs = {String.valueOf(typeId)};
        Cursor cursor = mContentResolver.query(Contacts.CONTENT_URI, columns, selection, selectionArgs, Contacts.DEFAULT_SORT_ORDER);
        CursorUtil.addCursor(cursor);
        if (cursor == null)
            return null;
        if (!cursor.moveToNext()) {
            cursor.close();
            return null;
        }
        return cursor;
    }

    public Cursor search(String string){
        string = "%" + string + "%";
        String[] columns = {
                Contacts.ID,
                Contacts.DISPLAY_NAME,
                Contacts.EMAIL,
                Contacts.NUMBER};
        String selection = Contacts.DISPLAY_NAME + " LIKE ? OR " + Contacts.EMAIL + " LIKE ?";
        String[] selectionArgs = {string, string};
        Cursor cursor = mContentResolver.query(Contacts.CONTENT_URI, columns, selection, selectionArgs, Contacts.DEFAULT_SORT_ORDER);
        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        return cursor;
    }

}
