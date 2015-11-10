package com.example.administrator.emailcontact.model;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.example.administrator.emailcontact.provider.Contacts;
import com.example.administrator.emailcontact.util.CursorUtil;

import java.util.ArrayList;

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

    public int delete(int id) {
        Uri uri = ContentUris.withAppendedId(Contacts.CONTENT_URI, id);
        int rows = mContentResolver.delete(uri, null, null);
        Log.e("sql", "delete:" + rows);
        return rows;
    }

    public int update(int id, String email) {
        ContentValues cv = new ContentValues();
        cv.put(Contacts.EMAIL, email);
        Uri uri = ContentUris.withAppendedId(Contacts.CONTENT_URI, id);
        int rows = mContentResolver.update(uri, cv, null, null);
        Log.e("sql", "updateContact id:" + rows);
        return rows;
    }

    public int updateContact(Contact contact) {
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
        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        return cursor;
    }

    public ArrayList<Contact> queryContactByGroupId(int groupId){
        ArrayList<Contact> mArray = new ArrayList<>();
        String[] columns = {
                Contacts.ID,
                Contacts.DISPLAY_NAME,
                Contacts.EMAIL,
                Contacts.NUMBER};
        String selection = Contacts.TYPE_ID + " = ?";
        String[] selectionArgs = {String.valueOf(groupId)};
        Cursor cursor = mContentResolver.query(Contacts.CONTENT_URI, columns, selection, selectionArgs, Contacts.DEFAULT_SORT_ORDER);
        if(cursor == null)
            return mArray;
        Contact contact;
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String display_Name = cursor.getString(1);
            String number = cursor.getString(3);
            String email = cursor.getString(2);
            contact = new Contact(id, number, display_Name, email, groupId);
            mArray.add(contact);
        }
        cursor.close();
        return mArray;
    }

    public ArrayList<Contact> search2Array(String string) {
        ArrayList<Contact> mArray = new ArrayList<>();
        string = "%" + string + "%";
        String[] columns = {
                Contacts.ID,
                Contacts.DISPLAY_NAME,
                Contacts.EMAIL,
                Contacts.NUMBER,
                Contacts.TYPE_ID};
        String selection = Contacts.DISPLAY_NAME + " LIKE ? OR " + Contacts.EMAIL + " LIKE ?";
        String[] selectionArgs = {string, string};
        Cursor cursor = mContentResolver.query(Contacts.CONTENT_URI, columns, selection, selectionArgs, Contacts.DEFAULT_SORT_ORDER);
        if(cursor == null)
            return mArray;
        Contact contact;
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String display_Name = cursor.getString(1);
            String email = cursor.getString(2);
            String number = cursor.getString(3);
            int type = cursor.getInt(4);
            contact = new Contact(id, number, display_Name, email, type);
            mArray.add(contact);
        }
        cursor.close();
        return mArray;
    }

    public Cursor search(String string) {
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

    public int getCursorCount() {
        Bundle mBundle = mContentResolver.call(Contacts.CONTENT_URI, Contacts.METHOD_GET_ITEM_COUNT, null, null);
        return mBundle != null ? mBundle.getInt(Contacts.KEY_ITEM_COUNT, 0) : 0;
    }

    public boolean isExistByEmailorName(String email, String name){
        String selection = Contacts.DISPLAY_NAME + " = ? OR " + Contacts.EMAIL + " = ?";
        String[] selectionArgs = {email, name};
        Cursor cursor = mContentResolver.query(Contacts.CONTENT_URI, null, selection, selectionArgs, Contacts.DEFAULT_SORT_ORDER);
        if(cursor != null && cursor.getCount() > 0)
            return true;
        return false;
    }
}
