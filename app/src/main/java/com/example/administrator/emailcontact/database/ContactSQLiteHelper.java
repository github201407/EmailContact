package com.example.administrator.emailcontact.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.administrator.emailcontact.model.Contact;
import com.example.administrator.emailcontact.provider.ContactProvider;

/**
 * Created by Administrator on 2015/9/18.
 */
public class ContactSQLiteHelper extends SQLiteOpenHelper {
    private final static String DATABASE_NAME = "ContactProvider.db";
    private final static int DATABASE_VERSION = 1;
    private final static String TABLE_NAME = "contacts_table";

    private static ContactSQLiteHelper mContactSQLiteHelper;

    public static ContactSQLiteHelper getInstance(Context context){
        if (mContactSQLiteHelper == null) {
            mContactSQLiteHelper = new ContactSQLiteHelper(context);
        }
        return mContactSQLiteHelper;
    }

    public ContactSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE " + TABLE_NAME + " ("
                + ContactProvider.ContactProviderColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ContactProvider.ContactProviderColumns.DISPLAY_NAME + " TEXT, "
                + ContactProvider.ContactProviderColumns.NUMBER + " TEXT, "
                + ContactProvider.ContactProviderColumns.EMAIL + " TEXT, "
                + ContactProvider.ContactProviderColumns.TYPE +" TEXT);";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);
    }

    public Cursor query(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, groupBy, having, orderBy);
        return cursor;

    }

    public long insert(Contact contact) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = contact.getContentValues();
        long row = db.insert(TABLE_NAME, null, cv);
        return row;
    }

    public void delete(int id){
        SQLiteDatabase db = getWritableDatabase();
        String whereClause  = ContactProvider.ContactProviderColumns._ID + " = ?";
        String[] whereArgs = {Integer.toString(id)};
        db.delete(TABLE_NAME, whereClause, whereArgs);
    }

    public void update(int id, String email)
    {
        SQLiteDatabase db = getWritableDatabase();
        String whereClause  = ContactProvider.ContactProviderColumns._ID + " = ?";
        String[] whereArgs = {Integer.toString(id)};
        ContentValues cv = new ContentValues();
        cv.put(ContactProvider.ContactProviderColumns.EMAIL, email);
        db.update(TABLE_NAME, cv, whereClause, whereArgs);
    }

}
