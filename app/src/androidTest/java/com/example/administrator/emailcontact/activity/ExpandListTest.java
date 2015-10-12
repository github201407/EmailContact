package com.example.administrator.emailcontact.activity;

import android.util.Log;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mnq on 2015/10/12.
 */
public class ExpandListTest extends TestCase {

    public void testOnCreate() throws Exception {
        List<String> mEmails = new ArrayList<>();
        mEmails.add("Hello");
        if(mEmails.size() > 0){
            String str = mEmails.get(0);
            Log.e("Log", str);
        }
    }
}