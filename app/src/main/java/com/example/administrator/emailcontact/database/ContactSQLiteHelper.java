package com.example.administrator.emailcontact.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.administrator.emailcontact.provider.Contacts;

/**
 * 联系人表数据库帮助类
 * Created by Administrator on 2015/9/18.
 */
public class ContactSQLiteHelper extends SQLiteOpenHelper {

    public ContactSQLiteHelper(Context context) {
        super(context, Contacts.DATABASE_NAME, null, Contacts.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(Contacts.DB_CREATE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql = "DROP TABLE IF EXISTS " + Contacts.TABLE_NAME;
        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);
    }

}
