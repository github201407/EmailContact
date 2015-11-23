package com.example.administrator.emailcontact.activity;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2015/11/10.
 */
public class ModifyContactTest extends ActivityInstrumentationTestCase2<ModifyContact> {

    public ModifyContactTest() {
        super(ModifyContact.class);
    }

    public void testInstance() throws Exception {
       // ModifyContact.Instance();
    }

    public void testArgular(){
        String email = "qwe@qq.qq.com";
        String str = "^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        boolean matcher =  m.matches();
        Log.e("matcher", "" + matcher);
    }
}