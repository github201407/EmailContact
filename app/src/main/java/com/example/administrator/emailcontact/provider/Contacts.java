package com.example.administrator.emailcontact.provider;

import android.net.Uri;

import com.example.administrator.emailcontact.BuildConfig;

/**
 * Created by Administrator on 2015/10/27.
 */
public class Contacts {
    /*Data Field*/
    public static final String ID = "_id";
    public static final String NUMBER = "_number";
    public static final String DISPLAY_NAME = "_display_name";
    public static final String EMAIL = "_email";
    public static final String TYPE_ID = "_type_id";
    public static final String ASYNC = "async";/* 0-未同步，1-已同步 */
    public static final String START = "start";/* 0-未收藏，1-收藏 */
    public static final String RECENT = "recent";/* 是否最近记录 0-不显示，1-显示*/
    public static final String RECENT_DATE_TIME = "recent_time";/*最近记录的时间*/

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
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.rj.soft.contact";
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.rj.soft.contact";

    /*Authority*/
    public static final String AUTHORITY = BuildConfig.APPLICATION_ID + ".ContactProvider";

    /*Content URI*/
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/item");
    public static final Uri CONTENT_POS_URI = Uri.parse("content://" + AUTHORITY + "/pos");

    /*DataBase Info */
    public final static String DATABASE_NAME = "contact.db";
    public final static String TABLE_NAME = "contacts_table";
    public final static int DATABASE_VERSION = 1;

    public final static String DB_CREATE_SQL = "CREATE TABLE " + TABLE_NAME + " ("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DISPLAY_NAME + " TEXT, "
            + NUMBER + " TEXT, "
            + EMAIL + " TEXT, "
            + TYPE_ID + " INTEGER,"
            + ASYNC + " INTEGER DEFAULT 0 ,"
            + START + " INTEGER DEFAULT 0 ,"
            + RECENT + " INTEGER DEFAULT 0 ,"
            + RECENT_DATE_TIME + " DATETIME DEFAULT CURRENT_TIMESTAMP"
            + ");";
    
    public final static int CONTACT_PICK = 10;
}
