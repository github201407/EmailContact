package com.example.administrator.emailcontact.activity;

import android.test.ActivityInstrumentationTestCase2;
import android.test.AndroidTestCase;
import android.util.Log;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mnq on 2015/10/12.
 */
public class ExpandListTest extends ActivityInstrumentationTestCase2<ExpandList> {

    public ExpandListTest() {
        super(ExpandList.class);
    }

    public void testOnCreate() throws Exception {
        List<String> mEmails = new ArrayList<>();
        mEmails.add("Hello");
        if(mEmails.size() > 0){
            String str = mEmails.get(0);
            Log.e("Log", str);
        }
    }

    public void testDownloadContact(){
        getActivity().downloadContact();
    }

    public void testparseJson() throws Exception {
        getActivity().parseJson("");
    }
}