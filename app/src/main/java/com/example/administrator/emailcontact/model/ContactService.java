package com.example.administrator.emailcontact.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.administrator.emailcontact.database.ContactSQLiteHelper;
import com.example.administrator.emailcontact.provider.Contacts;
import com.example.administrator.emailcontact.util.CursorUtil;

/**
 * Created by Administrator on 2015/9/21.
 */
public class ContactService {
    private ContactSQLiteHelper dbOpenHelper = null;

    public ContactService(Context context) {
        if (dbOpenHelper == null)
            dbOpenHelper = new ContactSQLiteHelper(context);
    }

    public long insert(Contact contact) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        ContentValues cv = contact.getContentValues();
        long row = db.insert(Contacts.TABLE_NAME, null, cv);
        db.close();
        Log.e("sql", "insert:" + row);
        return row;
    }

    public void delete(int id) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        String whereClause = Contacts.ID + " = ?";
        String[] whereArgs = {Integer.toString(id)};
        int rows = db.delete(Contacts.TABLE_NAME, whereClause, whereArgs);
        db.close();
        Log.e("sql", "delete:" + rows);
    }

    public int update(int id, String email) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        String whereClause = Contacts.ID + " = ?";
        String[] whereArgs = {Integer.toString(id)};
        ContentValues cv = new ContentValues();
        cv.put(Contacts.EMAIL, email);
        int result = db.update(Contacts.TABLE_NAME, cv, whereClause, whereArgs);
        db.close();
        Log.e("sql", "update:" + result);
        return result;
    }

    public int updateContact(int id, Contact contact) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        String whereClause = Contacts.ID + " = ?";
        String[] whereArgs = {Integer.toString(id)};
        ContentValues cv = contact.getContentValues();
        int result = db.update(Contacts.TABLE_NAME, cv, whereClause, whereArgs);
        db.close();
        Log.e("sql", "updateContact:" + result);
        return result;
    }

    public Cursor query(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = db.query(Contacts.TABLE_NAME, columns, selection, selectionArgs, groupBy, having, orderBy);
        CursorUtil.addCursor(cursor);
        return cursor;
    }

    public Cursor defaultQuery() {
        return this.query(new String[]{Contacts.ID,
                Contacts.DISPLAY_NAME,
                Contacts.EMAIL}, null, null, null, null, null);
    }

    public Contact find(int id) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        String sql = "select "
                + Contacts.DISPLAY_NAME + ", "
                + Contacts.NUMBER + ", "
                + Contacts.EMAIL + ", "
                + Contacts.TYPE_ID
                + " from "
                + Contacts.TABLE_NAME
                + " where "
                + Contacts.ID
                + " = ?";
        Log.e("sql", sql);
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(id)});
        if (cursor.moveToNext()) {
            String diaplayName = cursor.getString(0);
            String number = cursor.getString(1);
            String email = cursor.getString(2);
            int type = cursor.getInt(3);
            return new Contact(number, diaplayName, email, type);
        }
        cursor.close();
        return null;
    }

    public Cursor findByTypeId(int typeId) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        String[] columns = {
                Contacts.ID,
                Contacts.DISPLAY_NAME,
                Contacts.EMAIL,
                Contacts.NUMBER};
        String selection = Contacts.TYPE_ID + " = ?";
        String[] selectionArgs = {String.valueOf(typeId)};
        Cursor cursor = db.query(Contacts.TABLE_NAME, columns, selection, selectionArgs, null, null, null);
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
