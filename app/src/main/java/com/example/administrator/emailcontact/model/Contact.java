package com.example.administrator.emailcontact.model;

import android.content.ContentValues;

import com.example.administrator.emailcontact.database.ContactSQLiteHelper;
import com.example.administrator.emailcontact.provider.Contacts;

/**
 * Created by Administrator on 2015/9/18.
 */
public class Contact {
    int id;
    String number;
    String display_name;
    String email;
    int type;

    public Contact(int id, String number, String display_name, String email, int type) {
        this.id = id;
        this.number = number;
        this.display_name = display_name;
        this.email = email;
        this.type = type;
    }
    public Contact(String number, String display_name, String email, int type) {
        this.number = number;
        this.display_name = display_name;
        this.email = email;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public ContentValues getContentValues() {
        ContentValues mContentValues = new ContentValues();
        mContentValues.put(Contacts.NUMBER, number);
        mContentValues.put(Contacts.EMAIL, email);
        mContentValues.put(Contacts.DISPLAY_NAME, display_name);
        mContentValues.put(Contacts.TYPE_ID, type);
        return mContentValues;
    }

    public static Contact getContact(ContentValues contentValues) {
        return new Contact(
                contentValues.getAsInteger(Contacts.ID),
                contentValues.getAsString(Contacts.NUMBER),
                contentValues.getAsString(Contacts.DISPLAY_NAME),
                contentValues.getAsString(Contacts.EMAIL),
                contentValues.getAsInteger(Contacts.TYPE_ID)
        );
    }
}
