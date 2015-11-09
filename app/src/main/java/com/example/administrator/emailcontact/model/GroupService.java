package com.example.administrator.emailcontact.model;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.example.administrator.emailcontact.database.GroupSQLiteHelper;
import com.example.administrator.emailcontact.provider.Contacts;
import com.example.administrator.emailcontact.provider.Groups;
import com.example.administrator.emailcontact.util.CursorUtil;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/10/8.
 */
public class GroupService {
    private GroupSQLiteHelper dbOpenHelper = null;
    private ContentResolver mContentResolver = null;

    public GroupService(Context context) {
        if(dbOpenHelper == null)
            dbOpenHelper = new GroupSQLiteHelper(context);
        mContentResolver = context.getContentResolver();
    }

    public long insert(int parent, int type, String groupName){
        ContentValues cv = new ContentValues();
        cv.put(Groups.PARENT, parent);
        cv.put(Groups.TYPE, type);
        cv.put(Groups.NAME, groupName);
        Uri uri = mContentResolver.insert(Groups.CONTENT_URI, cv);
        String idStr = uri.getPathSegments().get(1);
        long row = Long.valueOf(idStr);
        Log.e("sql", "insert:" + row);
        return row;
    }

    public long insertParentNode(String groupName) {
        return insert(-1, 0, groupName);
    }

    public void delete(int id) {
        Uri uri = ContentUris.withAppendedId(Groups.CONTENT_URI, id);
        int rows = mContentResolver.delete(uri, null, null);
        Log.e("sql", "delete:" + rows);
    }

    public int update(int id, String groupName) {
        ContentValues cv = new ContentValues();
        cv.put(Groups.NAME, groupName);
        Uri uri = ContentUris.withAppendedId(Groups.CONTENT_URI, id);
        int rows = mContentResolver.update(uri, cv, null, null);
        Log.e("sql", "updateGroup id:" + rows);
        return rows;
    }

    public Cursor query(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        Cursor mCursor = mContentResolver.query(Groups.CONTENT_URI, columns, selection, selectionArgs, Contacts.DEFAULT_SORT_ORDER);
        CursorUtil.addCursor(mCursor);
        return mCursor;
    }

    public Cursor defaultQuery() {
        String[] columns = {
                Groups.ID,
                Groups.PARENT,
                Groups.ROOT,
                Groups.TYPE,
                Groups.NAME,
                Groups.CREATE_DATE};
        return this.query(columns, null, null, null, null, null);
    }

    public ArrayList<Group> queryTopParent(int parent) {
        ArrayList<Group> groups = new ArrayList<>();
        Cursor mCursor =  queryParent(parent);
        if (mCursor == null)
            return groups;
        Group group;
        while (mCursor.moveToNext()){
            int id = mCursor.getInt(0);
            int root = mCursor.getInt(1);
            int type = mCursor.getInt(2);
            String name = mCursor.getString(3);
            group = new Group(id, parent, type, root, name);
            groups.add(group);
        }
        mCursor.close();
        return groups;
    }
    public Cursor queryParent(int parent) {
        String[] columns = {
                Groups.ID,
                Groups.ROOT,
                Groups.TYPE,
                Groups.NAME,
                Groups.CREATE_DATE};
        String selection = Groups.PARENT + " = ?";
        String[] selectionArgs = {String.valueOf(parent)};
        Cursor cursor = mContentResolver.query(Groups.CONTENT_URI, columns, selection, selectionArgs, Groups.DEFAULT_SORT_ORDER);
        return cursor;
    }

    public int getCursorCount(){
        Bundle mBundle = mContentResolver.call(Groups.CONTENT_URI,Groups.METHOD_GET_ITEM_COUNT,null,null);
        return mBundle != null ? mBundle.getInt(Groups.KEY_ITEM_COUNT, 0) : 0;
    }

}
