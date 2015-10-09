package com.example.administrator.emailcontact.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2015/9/18.
 */
public class ContactSQLiteHelper extends SQLiteOpenHelper {
    private final static String DATABASE_NAME = "ContactProvider.db";
    private final static int DATABASE_VERSION = 1;
    public final static String TABLE_NAME = "contacts_table";

    public static class ContactProviderColumns {
        public static final String _ID = "_id";
        public static final String NUMBER = "_number";
        public static final String DISPLAY_NAME = "_display_name";
        public static final String EMAIL = "_email";
        public static final String TYPE_ID = "_type_id";
    }

    public ContactSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE " + TABLE_NAME + " ("
                + ContactProviderColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ContactProviderColumns.DISPLAY_NAME + " TEXT, "
                + ContactProviderColumns.NUMBER + " TEXT, "
                + ContactProviderColumns.EMAIL + " TEXT, "
                + ContactProviderColumns.TYPE_ID +" INTEGER);";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);
    }

}
