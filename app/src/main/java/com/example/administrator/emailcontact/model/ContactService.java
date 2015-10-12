package com.example.administrator.emailcontact.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.administrator.emailcontact.database.ContactSQLiteHelper;

/**
 * Created by Administrator on 2015/9/21.
 */
public class ContactService {
    private ContactSQLiteHelper dbOpenHelper = null;

    public ContactService(Context context) {
        if(dbOpenHelper == null)
            dbOpenHelper = new ContactSQLiteHelper(context);
    }

    public long insert(Contact contact) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        ContentValues cv = contact.getContentValues();
        long row = db.insert(ContactSQLiteHelper.TABLE_NAME, null, cv);
        db.close();
        Log.e("sql", "insert:" + row);
        return row;
    }

    public void delete(int id) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        String whereClause = ContactSQLiteHelper.ContactProviderColumns._ID + " = ?";
        String[] whereArgs = {Integer.toString(id)};
        int rows = db.delete(ContactSQLiteHelper.TABLE_NAME, whereClause, whereArgs);
        db.close();
        Log.e("sql", "delete:" + rows);
    }

    public int update(int id, String email) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        String whereClause = ContactSQLiteHelper.ContactProviderColumns._ID + " = ?";
        String[] whereArgs = {Integer.toString(id)};
        ContentValues cv = new ContentValues();
        cv.put(ContactSQLiteHelper.ContactProviderColumns.EMAIL, email);
        int result = db.update(ContactSQLiteHelper.TABLE_NAME, cv, whereClause, whereArgs);
        db.close();
        Log.e("sql", "update:" + result);
        return result;
    }

    public int updateContact(int id, Contact contact){
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        String whereClause = ContactSQLiteHelper.ContactProviderColumns._ID + " = ?";
        String[] whereArgs = {Integer.toString(id)};
        ContentValues cv = contact.getContentValues();
        int result = db.update(ContactSQLiteHelper.TABLE_NAME, cv, whereClause, whereArgs);
        db.close();
        Log.e("sql", "updateContact:" + result);
        return result;
    }

    public Cursor query(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = db.query(ContactSQLiteHelper.TABLE_NAME, columns, selection, selectionArgs, groupBy, having, orderBy);
        return cursor;
    }

    public Cursor defaultQuery() {
        return this.query(new String[]{ContactSQLiteHelper.ContactProviderColumns._ID,
                ContactSQLiteHelper.ContactProviderColumns.DISPLAY_NAME,
                ContactSQLiteHelper.ContactProviderColumns.EMAIL}, null, null, null, null, null);
    }

    public Contact find(int id) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        String sql = "select "
                + ContactSQLiteHelper.ContactProviderColumns.DISPLAY_NAME + ", "
                + ContactSQLiteHelper.ContactProviderColumns.NUMBER + ", "
                + ContactSQLiteHelper.ContactProviderColumns.EMAIL + ", "
                + ContactSQLiteHelper.ContactProviderColumns.TYPE_ID
                + " from "
                + ContactSQLiteHelper.TABLE_NAME
                + " where "
                + ContactSQLiteHelper.ContactProviderColumns._ID
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
        return null;
    }

    public Cursor findByTypeId(int typeId){
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        String[] columns = {
                ContactSQLiteHelper.ContactProviderColumns._ID ,
                ContactSQLiteHelper.ContactProviderColumns.DISPLAY_NAME,
                ContactSQLiteHelper.ContactProviderColumns.EMAIL ,
                ContactSQLiteHelper.ContactProviderColumns.NUMBER};
        String selection = ContactSQLiteHelper.ContactProviderColumns.TYPE_ID + " = ?";
        String[] selectionArgs = {String.valueOf(typeId)};
        Cursor cursor = db.query(ContactSQLiteHelper.TABLE_NAME, columns,selection,selectionArgs,null,null,null );
        if(cursor == null)
            return null;
        if (!cursor.moveToNext()) {
            cursor.close();
            return null;
        }
        return cursor;
    }

}
