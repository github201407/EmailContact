package com.example.administrator.emailcontact.provider;

import android.net.Uri;

import com.example.administrator.emailcontact.BuildConfig;

/**
 * Created by Administrator on 2015/10/30.
 */
public class Groups {
    /*Data Field*/
    public static final String ID = "_id";
    public static final String PARENT = "parent";
    public static final String ROOT = "root";
    public static final String TYPE = "type";
    public static final String NAME = "_display_name";
    public static final String CREATE_DATE = "_create_date";

    /*Default sort order*/
    public static final String DEFAULT_SORT_ORDER = "_id asc";

    /*Call Method*/
    public static final String METHOD_GET_ITEM_COUNT = "METHOD_GET_ITEM_COUNT";
    public static final String KEY_ITEM_COUNT = "KEY_ITEM_COUNT";

    /*Match Code*/
    public static final int ITEM = 1;
    public static final int ITEM_ID = 2;
    public static final int ITEM_POS = 3;

    /*MIME 格式一般为vnd.[company name].[resource type] */
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.rj.soft.group";
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.rj.soft.group";

    /*Authority*/
    public static final String AUTHORITY = BuildConfig.APPLICATION_ID + ".GroupProvider";

    /*Content URI*/
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/item");
    public static final Uri CONTENT_POS_URI = Uri.parse("content://" + AUTHORITY + "/pos");

    /*DataBase Info */
    public final static String DATABASE_NAME = "group.db";
    public final static String TABLE_NAME = "group_table";
    public final static int DATABASE_VERSION = 1;

    public final static String DB_CREATE_SQL = "CREATE TABLE " + TABLE_NAME + " ("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + PARENT + " INTEGER NOT NULL, "
            + ROOT + " INTEGER DEFAULT 0, "
            + TYPE + " INTEGER NOT NULL, "
            + NAME + " TEXT, "
            + CREATE_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP);";
}
