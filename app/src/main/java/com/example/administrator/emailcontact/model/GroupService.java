package com.example.administrator.emailcontact.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.administrator.emailcontact.database.ContactSQLiteHelper;
import com.example.administrator.emailcontact.database.GroupSQLiteHelper;

/**
 * Created by Administrator on 2015/10/8.
 */
public class GroupService {
    private GroupSQLiteHelper dbOpenHelper = null;

    public GroupService(Context context) {
        dbOpenHelper = new GroupSQLiteHelper(context);
    }

    public long insert(String groupName) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(GroupSQLiteHelper.GroupColumns._NAME, groupName);
        long row = db.insert(GroupSQLiteHelper.TABLE_NAME, null, cv);
        Log.e("sql", "insert:" + row);
        return row;
    }

    public void delete(int id) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        String whereClause = GroupSQLiteHelper.GroupColumns._ID + " = ?";
        String[] whereArgs = {Integer.toString(id)};
        int rows = db.delete(GroupSQLiteHelper.TABLE_NAME, whereClause, whereArgs);
        Log.e("sql", "delete:" + rows);
    }

    public int update(int id, String groupName) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        String whereClause = GroupSQLiteHelper.GroupColumns._ID + " = ?";
        String[] whereArgs = {Integer.toString(id)};
        ContentValues cv = new ContentValues();
        cv.put(GroupSQLiteHelper.GroupColumns._NAME, groupName);
        int result = db.update(GroupSQLiteHelper.TABLE_NAME, cv, whereClause, whereArgs);
        Log.e("sql", "update:" + result);
        return result;
    }

    public Cursor query(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = db.query(GroupSQLiteHelper.TABLE_NAME, columns, selection, selectionArgs, groupBy, having, orderBy);
        return cursor;
    }

    public Cursor defaultQuery() {
        return this.query(new String[]{GroupSQLiteHelper.GroupColumns._ID,
                GroupSQLiteHelper.GroupColumns._NAME,
                GroupSQLiteHelper.GroupColumns._CREATE_DATE}, null, null, null, null, null);
    }

}
