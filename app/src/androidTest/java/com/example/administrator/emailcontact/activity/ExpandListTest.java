package com.example.administrator.emailcontact.activity;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
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

    public void testURLEncodeWithDecode(){
        String cn = "中文";
        try {
            String encode = URLEncoder.encode(cn, "utf-8");
            String decode = URLDecoder.decode(encode, "utf-8");
            assertEquals(cn, decode);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

}