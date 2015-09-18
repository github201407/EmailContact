package com.example.administrator.emailcontact.model;

import android.content.ContentValues;

import com.example.administrator.emailcontact.provider.ContactProvider;

/**
 * Created by Administrator on 2015/9/18.
 */
public class Contact {
    String number;
    String display_name;
    String email;
    String type;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ContentValues getContentValues(){
        ContentValues mContentValues = new ContentValues();
        mContentValues.put(ContactProvider.ContactProviderColumns.NUMBER, number);
        mContentValues.put(ContactProvider.ContactProviderColumns.EMAIL, email);
        mContentValues.put(ContactProvider.ContactProviderColumns.DISPLAY_NAME, display_name);
        mContentValues.put(ContactProvider.ContactProviderColumns.TYPE, type);
        return mContentValues;
    }
}
