package com.example.administrator.emailcontact.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.administrator.emailcontact.database.ContactSQLiteHelper;
import com.example.administrator.emailcontact.database.GroupSQLiteHelper;
import com.example.administrator.emailcontact.util.CursorUtil;

/**
 * Created by Administrator on 2015/10/8.
 */
public class GroupService {
    private GroupSQLiteHelper dbOpenHelper = null;

    public GroupService(Context context) {
        if(dbOpenHelper == null)
            dbOpenHelper = new GroupSQLiteHelper(context);
    }

    public long insert(int parent, int type, String groupName){
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(GroupSQLiteHelper.GroupColumns.PARENT, parent);
        cv.put(GroupSQLiteHelper.GroupColumns.TYPE, type);
        cv.put(GroupSQLiteHelper.GroupColumns._NAME, groupName);
        long row = db.insert(GroupSQLiteHelper.TABLE_NAME, null, cv);
        db.close();
        Log.e("sql", "insert:" + row);
        return row;
    }

    public long insert(String groupName) {
        return insert(-1, 0, groupName);
    }

    public void delete(int id) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        String whereClause = GroupSQLiteHelper.GroupColumns._ID + " = ?";
        String[] whereArgs = {Integer.toString(id)};
        int rows = db.delete(GroupSQLiteHelper.TABLE_NAME, whereClause, whereArgs);
        db.close();
        Log.e("sql", "delete:" + rows);
    }

    public int update(int id, String groupName) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        String whereClause = GroupSQLiteHelper.GroupColumns._ID + " = ?";
        String[] whereArgs = {Integer.toString(id)};
        ContentValues cv = new ContentValues();
        cv.put(GroupSQLiteHelper.GroupColumns._NAME, groupName);
        int result = db.update(GroupSQLiteHelper.TABLE_NAME, cv, whereClause, whereArgs);
        db.close();
        Log.e("sql", "update:" + result);
        return result;
    }

    public Cursor query(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = db.query(GroupSQLiteHelper.TABLE_NAME, columns, selection, selectionArgs, groupBy, having, orderBy);
        CursorUtil.addCursor(cursor);
        return cursor;
    }

    public Cursor defaultQuery() {
        String[] columns = {
                GroupSQLiteHelper.GroupColumns._ID,
                GroupSQLiteHelper.GroupColumns.PARENT,
                GroupSQLiteHelper.GroupColumns.ROOT,
                GroupSQLiteHelper.GroupColumns.TYPE,
                GroupSQLiteHelper.GroupColumns._NAME,
                GroupSQLiteHelper.GroupColumns._CREATE_DATE};
        return this.query(columns, null, null, null, null, null);
    }

    public Cursor queryParent(int parent) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        String[] columns = {
                GroupSQLiteHelper.GroupColumns._ID,
                GroupSQLiteHelper.GroupColumns.ROOT,
                GroupSQLiteHelper.GroupColumns.TYPE,
                GroupSQLiteHelper.GroupColumns._NAME,
                GroupSQLiteHelper.GroupColumns._CREATE_DATE};
        String selection = GroupSQLiteHelper.GroupColumns.PARENT + " = ?";
        String[] selectionArgs = {String.valueOf(parent)};
        Cursor cursor = db.query(GroupSQLiteHelper.TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        CursorUtil.addCursor(cursor);
        if (cursor == null)
            return null;
        if (!cursor.moveToNext()) {
            cursor.close();
            return null;
        }
        return cursor;
    }

}
