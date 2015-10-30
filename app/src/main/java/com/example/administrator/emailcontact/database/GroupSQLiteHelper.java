package com.example.administrator.emailcontact.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.administrator.emailcontact.provider.Groups;

/**
 * Created by Administrator on 2015/10/8.
 */
public class GroupSQLiteHelper extends SQLiteOpenHelper {

    public GroupSQLiteHelper(Context context) {
        super(context, Groups.DATABASE_NAME, null, Groups.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(Groups.DB_CREATE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql = "DROP TABLE IF EXISTS " + Groups.TABLE_NAME;
        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);
    }
}
