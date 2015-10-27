package com.example.administrator.emailcontact.provider;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.test.ProviderTestCase2;
import android.util.Log;

import com.example.administrator.emailcontact.model.Contact;

import junit.framework.TestCase;

/**
 * Created by Administrator on 2015/10/27.
 */
public class ContactProviderTest extends ProviderTestCase2<ContactProvider> {
    private static final String TAG = "ContactProviderTest";

    private ContentResolver mContentResolver = null;

    /**
     * Constructor.
     *
     * providerClass     The class name of the provider under test
     * providerAuthority The provider's authority string
     */
    public ContactProviderTest() {
        super(ContactProvider.class, Contacts.AUTHORITY);
    }

    public void setUp() throws Exception {
        super.setUp();
        mContentResolver = getContext().getContentResolver();
    }

    public void tearDown() throws Exception {

    }

    public void testQuery() throws Exception {
       Cursor mCursor = mContentResolver.query(Contacts.CONTENT_URI, new String[]{Contacts.DISPLAY_NAME, Contacts.EMAIL}, null, null, Contacts.DEFAULT_SORT_ORDER);
        assert mCursor != null;
        int mCount = mCursor.getCount();
        Log.e(TAG,"Count:" + mCount);
        while(mCursor.moveToNext()){
            String display_name = mCursor.getString(0);
            String email = mCursor.getString(1);
            Log.e(TAG,display_name + "," + email);
        }
        mCursor.close();
    }

    public void testInsert() throws Exception {
        Contact mContact = new Contact("152606","display_name","152606@139.com",1);
        ContentValues values = mContact.getContentValues();
        Uri uri = mContentResolver.insert(Contacts.CONTENT_URI, values);
        String idStr = uri.getPathSegments().get(1);
        long id = Long.valueOf(idStr);
        Log.e(TAG,"id:" + id);
    }

    public void testDelete() throws Exception {
        Uri uri = ContentUris.withAppendedId(Contacts.CONTENT_URI,2);
        int id = mContentResolver.delete(uri, null, null);
        Log.e(TAG,"delete id:" + id);
    }

    public void testUpdate() throws Exception {
        Contact mContact = new Contact("15260", "Jen Chen", "15260.com", 2);
        ContentValues values = mContact.getContentValues();
        Uri uri = ContentUris.withAppendedId(Contacts.CONTENT_URI, 1);
        int id = mContentResolver.update(uri, values, null, null);
        Log.e(TAG, "id:" + id);
    }

    public void testCall() throws Exception {
        Bundle bundle = mContentResolver.call(Contacts.CONTENT_URI,Contacts.METHOD_GET_ITEM_COUNT,null,null);
        assert bundle != null;
        int count = bundle.getInt(Contacts.KEY_ITEM_COUNT);
        Log.e(TAG,"count:" + count);
    }
}