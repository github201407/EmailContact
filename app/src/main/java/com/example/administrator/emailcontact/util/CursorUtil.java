package com.example.administrator.emailcontact.util;

import android.database.Cursor;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/10/14.
 */
public class CursorUtil {
    private static ArrayList<Cursor> mArrayList = new ArrayList<>();

    public static void addCursor(Cursor mCurosr){
        mArrayList.add(mCurosr);
    }
    public static void closeDB(){
        if (!mArrayList.isEmpty()){
            for (Cursor cursor: mArrayList){
                if (cursor != null && !cursor.isClosed()){
                    cursor.close();
                }
            }
            mArrayList.clear();
        }
    }


}
