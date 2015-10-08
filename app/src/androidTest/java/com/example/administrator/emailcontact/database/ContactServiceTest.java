package com.example.administrator.emailcontact.database;

import android.database.Cursor;
import android.test.AndroidTestCase;
import android.util.Log;

import com.example.administrator.emailcontact.model.Contact;
import com.example.administrator.emailcontact.model.ContactService;

/**
 * Created by Administrator on 2015/9/21.
 */
public class ContactServiceTest extends AndroidTestCase {
    public void testSum() {
        assertEquals(1 + 1, 2);
    }

    public void testinsert() {
        ContactService mService = new ContactService(getContext());
        Contact mContact = new Contact("123", "cmq", "123@qq.com",1);
        long rowid = mService.insert(mContact);
        assertEquals(-1l, rowid);
    }

    public void testFind(){
        ContactService mService = new ContactService(getContext());
        Contact mContact = mService.find(1);
        assertNotNull(mContact);
        assertEquals("123@qq.com", mContact.getEmail());
    }

    public void testQuery(){
        String[] columns = new String[] {
                ContactSQLiteHelper.ContactProviderColumns._ID,
                ContactSQLiteHelper.ContactProviderColumns.NUMBER,
                ContactSQLiteHelper.ContactProviderColumns.DISPLAY_NAME,
                ContactSQLiteHelper.ContactProviderColumns.EMAIL,
                ContactSQLiteHelper.ContactProviderColumns.TYPE,
        };
        ContactService mService = new ContactService(getContext());
        Cursor mCursor = mService.query(columns, null, null, null, null, null);
        while(mCursor.moveToNext()){
            int contactId = mCursor.getInt(0);
            String number = mCursor.getString(1);
            String displayName = mCursor.getString(2);
            String email = mCursor.getString(3);
            String type = mCursor.getString(4);
            Log.e("sql","[" + contactId + "," + number + "," + displayName + "," + email + "," + type + "]");
        }
    }

    public void testUpdate(){
        ContactService mService = new ContactService(getContext());
        int result = mService.update(1, "123@163.com");
        assertEquals(1,result);
    }
}
