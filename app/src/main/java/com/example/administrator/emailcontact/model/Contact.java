package com.example.administrator.emailcontact.model;

import android.content.ContentValues;

import com.example.administrator.emailcontact.provider.Contacts;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/9/18.
 */
public class Contact implements Serializable{
    int id;
    String number;
    String name;
    String email;
    int type;
    String imageUrl;

    public Contact(int id, String number, String name, String email, int type, String imageUrl) {
        this.id = id;
        this.number = number;
        this.name = name;
        this.email = email;
        this.type = type;
        this.imageUrl = imageUrl;
    }

    public Contact(int id, String number, String name, String email, int type) {
        this(id, number, name, email, type, "");
    }

    public Contact(String number, String name, String email, int type) {
        this(0, number, name, email, type);
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ContentValues getContentValues() {
        ContentValues mContentValues = new ContentValues();
        mContentValues.put(Contacts.NUMBER, number);
        mContentValues.put(Contacts.EMAIL, email);
        mContentValues.put(Contacts.DISPLAY_NAME, name);
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
