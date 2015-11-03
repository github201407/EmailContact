package com.example.administrator.emailcontact.model;

import android.content.ContentValues;

import com.example.administrator.emailcontact.provider.Groups;

/**
 * Created by Administrator on 2015/10/30.
 */
public class Group {
    int id;
    int parent;
    int type;
    int root;
    String name;

    public Group(int id, int parent, int type, int root, String name) {
        this.id = id;
        this.parent = parent;
        this.type = type;
        this.root = root;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getRoot() {
        return root;
    }

    public void setRoot(int root) {
        this.root = root;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ContentValues getContentValues() {
        ContentValues mContentValues = new ContentValues();
        mContentValues.put(Groups.PARENT, parent);
        mContentValues.put(Groups.ROOT, root);
        mContentValues.put(Groups.NAME, name);
        mContentValues.put(Groups.TYPE, type);
        return mContentValues;
    }
}
