package com.example.administrator.emailcontact.database;

import android.database.Cursor;
import android.test.AndroidTestCase;
import android.util.Log;

import com.example.administrator.emailcontact.model.Contact;
import com.example.administrator.emailcontact.model.ContactService;
import com.example.administrator.emailcontact.provider.Contacts;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/9/21.
 */
public class ContactServiceTest extends AndroidTestCase {
    public void testSum() {
        assertEquals(1 + 1, 2);
    }

    public void testinsert() {
        ContactService mService = new ContactService(getContext());
        Contact mContact = new Contact("123", "cmq", "123@qq.com", 1);
        mService.insert(mContact);
        mService.insert(mContact);
        mService.insert(mContact);
        mService.insert(mContact);
        mService.insert(mContact);
    }

    public void testFind() {
        ContactService mService = new ContactService(getContext());
        Contact mContact = mService.find(1);
        assertNotNull(mContact);
        assertEquals("123@qq.com", mContact.getEmail());
    }

    public void testQuery() {
        String[] columns = new String[]{
                Contacts.ID,
                Contacts.NUMBER,
                Contacts.DISPLAY_NAME,
                Contacts.EMAIL,
                Contacts.TYPE_ID,
        };
        ContactService mService = new ContactService(getContext());
        Cursor mCursor = mService.query(columns, null, null, null, null, null);
        while (mCursor.moveToNext()) {
            int contactId = mCursor.getInt(0);
            String number = mCursor.getString(1);
            String displayName = mCursor.getString(2);
            String email = mCursor.getString(3);
            String type = mCursor.getString(4);
            Log.e("sql", "[" + contactId + "," + number + "," + displayName + "," + email + "," + type + "]");
        }
    }

    public void testUpdate() {
        ContactService mService = new ContactService(getContext());
        int result = mService.update(1, "123@163.com");
        assertEquals(1, result);
    }

    public void testQueryStar(){
        ContactService mService = new ContactService(getContext());
        ArrayList<Contact> mStars = mService.queryStarContact();
        Log.e("sql", "" + mStars.size());
    }

    public void testSetStar(){
        ContactService mService = new ContactService(getContext());
        mService.setStarContact(1);
    }

    public void testQueryRecent(){
        ContactService mService = new ContactService(getContext());
        ArrayList<Contact> mStars = mService.queryRecentContact();
        Log.e("sql_recent", "" + mStars.size());
    }

    public void testSetRecent(){
        ContactService mService = new ContactService(getContext());
        mService.setRecentContact(2);
    }
}
