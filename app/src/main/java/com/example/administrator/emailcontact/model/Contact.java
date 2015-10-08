package com.example.administrator.emailcontact.model;

import android.content.ContentValues;

import com.example.administrator.emailcontact.database.ContactSQLiteHelper;
import com.example.administrator.emailcontact.provider.ContactProvider;

/**
 * Created by Administrator on 2015/9/18.
 */
public class Contact {
    String number;
    String display_name;
    String email;
    int type;

    public Contact(String number, String display_name, String email, int type) {
        this.number = number;
        this.display_name = display_name;
        this.email = email;
        this.type = type;
    }

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public ContentValues getContentValues(){
        ContentValues mContentValues = new ContentValues();
        mContentValues.put(ContactSQLiteHelper.ContactProviderColumns.NUMBER, number);
        mContentValues.put(ContactSQLiteHelper.ContactProviderColumns.EMAIL, email);
        mContentValues.put(ContactSQLiteHelper.ContactProviderColumns.DISPLAY_NAME, display_name);
        mContentValues.put(ContactSQLiteHelper.ContactProviderColumns.TYPE, type);
        return mContentValues;
    }

    public static Contact getContact(ContentValues contentValues){
        return new Contact(contentValues.getAsString(ContactSQLiteHelper.ContactProviderColumns.NUMBER),
                contentValues.getAsString(ContactSQLiteHelper.ContactProviderColumns.DISPLAY_NAME),
                contentValues.getAsString(ContactSQLiteHelper.ContactProviderColumns.EMAIL),
                contentValues.getAsInteger(ContactSQLiteHelper.ContactProviderColumns.TYPE)
                );
    }
}
