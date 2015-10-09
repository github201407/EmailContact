package com.example.administrator.emailcontact.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2015/10/8.
 */
public class GroupSQLiteHelper extends SQLiteOpenHelper {

    private final static String DATABASE_NAME = "GroupProvider.db";
    private final static int DATABASE_VERSION = 1;
    public final static String TABLE_NAME = "group_table";

    public static class GroupColumns {
        public static final String _ID = "_id";
        public static final String PARENT = "parent";
        public static final String ROOT = "root";
        public static final String TYPE = "type";
        public static final String _NAME = "_display_name";
        public static final String _CREATE_DATE = "_create_date";
    }
    public GroupSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE " + TABLE_NAME + " ("
                + GroupColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + GroupColumns.PARENT + " INTEGER NOT NULL, "
                + GroupColumns.ROOT + " INTEGER, "
                + GroupColumns.TYPE + " INTEGER NOT NULL, "
                + GroupColumns._NAME + " TEXT, "
                + GroupColumns._CREATE_DATE +" INTEGER);";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);
    }
}
