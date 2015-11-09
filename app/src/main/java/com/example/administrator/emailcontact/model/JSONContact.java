package com.example.administrator.emailcontact.model;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/11/9.
 */
public class JSONContact {
    private ArrayList<Contact> contacts = new ArrayList<>();

    public ArrayList<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(ArrayList<Contact> contacts) {
        this.contacts = contacts;
    }
}
