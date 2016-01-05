package com.example.administrator.emailcontact;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.example.administrator.emailcontact.model.Plaxo;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    private static final String TAG = "ApplicationTest";
    private Context context;
    public ApplicationTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = getContext();
    }

    public void testQueryContact(){
        assertNotNull(context);
        ContentResolver contentResolver = context.getContentResolver();
        String[] projection = new String[]{
                ContactsContract.Profile._ID,
                ContactsContract.Profile.LOOKUP_KEY,
                ContactsContract.Profile.PHOTO_THUMBNAIL_URI
        };
        String selection = null;
        String[] selectionArgs = null;
        String sortOrder = null;
        Cursor query = contentResolver.query(ContactsContract.Profile.CONTENT_URI, projection, selection,
                selectionArgs, sortOrder);
        while (query.moveToNext()){
            String id = query.getString(0);
            String key = query.getString(1);
            String photo = query.getString(2);
            Log.e(TAG, "testQueryContact: " + id + "," + key + "," + photo);
        }
        query.close();
    }

    public void testQueryProfileContact(){
        ContentResolver contentResolver = context.getContentResolver();
        String[] projection = null;
        String selection = null;
        String[] selectionArgs = null;
        String sortOrder = null;
        Cursor query = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, selection,
                selectionArgs, sortOrder);
        while (query.moveToNext()){
            String id = query.getString(0);
            Log.e(TAG, "testQueryContact: " + id);
        }
        query.close();
    }

    public void testQueryContactCount(){
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = ContactsContract.Contacts.CONTENT_URI.buildUpon()
                .appendQueryParameter(ContactsContract.Contacts.EXTRA_ADDRESS_BOOK_INDEX, "true")
                .build();
        Cursor cursor = contentResolver.query(uri,
                new String[]{ContactsContract.Contacts.DISPLAY_NAME},
                null, null, null);
        Bundle bundle = cursor.getExtras();
        if (bundle.containsKey(ContactsContract.Contacts.EXTRA_ADDRESS_BOOK_INDEX_TITLES) &&
                bundle.containsKey(ContactsContract.Contacts.EXTRA_ADDRESS_BOOK_INDEX_COUNTS)) {
            String sections[] =
                    bundle.getStringArray(ContactsContract.Contacts.EXTRA_ADDRESS_BOOK_INDEX_TITLES);
            int counts[] = bundle.getIntArray(ContactsContract.Contacts.EXTRA_ADDRESS_BOOK_INDEX_COUNTS);
            assertNotNull(sections);
            assertNotNull(counts);
            assertTrue(sections.length > 0);
            assertTrue(counts.length > 0);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void testQueryContactFromLocal(){
        ContentResolver contentResolver = context.getContentResolver();
        Cursor query = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                new String[]{ContactsContract.Contacts.NAME_RAW_CONTACT_ID}, null,
                null, null);/* android 21 */

        if (query == null) return;

        while (query.moveToNext()){
            String id = query.getString(0);
            Log.e(TAG, "raw_contact_id: " + id);
        }
        query.close();
    }

    public void testQueryRawContactById(){
        int contactId = 2;
        ContentResolver contentResolver = context.getContentResolver();
        Cursor query = contentResolver.query(ContactsContract.RawContacts.CONTENT_URI,
                new String[]{ContactsContract.RawContacts._ID},
                ContactsContract.RawContacts.CONTACT_ID + " = ? ",
                new String[]{ String.valueOf(contactId) }, null);

        if (query == null) return;

        while (query.moveToNext()){
            String id = query.getString(0);
            Log.e(TAG, "raw_contact_id: " + id);
        }
        query.close();
    }

    public void testQueryContactFromData(){
        int raw_contact_id = 2;
        ContentResolver contentResolver = context.getContentResolver();
        Cursor query = contentResolver.query(ContactsContract.Data.CONTENT_URI,
                new String[]{ContactsContract.Data.MIMETYPE, ContactsContract.Data.DISPLAY_NAME},
                ContactsContract.Data.RAW_CONTACT_ID + " = ?",
                new String[]{String.valueOf(raw_contact_id)}, null);
        if(query == null) return;
        while (query.moveToNext()){
            String id = query.getString(0);
            Log.e(TAG, "mimeType: " + id);
        }
        query.close();
    }

    public void testContactPhoneByRawContactId(){
        int raw_contact_id = 2;
        ContentResolver contentResolver = context.getContentResolver();
        Cursor query = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone._ID,
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.TYPE,
                        ContactsContract.CommonDataKinds.Phone.NUMBER},
                ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID + " = ?",
                new String[]{String.valueOf(raw_contact_id)}, null);
        if(query == null) return;
        while (query.moveToNext()){
            String id = query.getString(0);
            String name = query.getString(1);
            String type = query.getString(2);
            String number = query.getString(3);
            Log.e(TAG, "phone: " + id + ",name:" + name + ",type:" + type + ",number:" + number);
        }
        query.close();
        /*public static final int TYPE_HOME = 1;
        public static final int TYPE_MOBILE = 2;
        public static final int TYPE_WORK = 3;*/
    }

    public void testContactEmailByRawContactId(){
        int raw_contact_id = 2;
        ContentResolver contentResolver = context.getContentResolver();
        Cursor query = contentResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Email._ID,
                        ContactsContract.CommonDataKinds.Email.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Email.TYPE,
                        ContactsContract.CommonDataKinds.Email.ADDRESS},
                ContactsContract.CommonDataKinds.Email.RAW_CONTACT_ID + " = ?",
                new String[]{String.valueOf(raw_contact_id)}, null);
        if(query == null) return;
        while (query.moveToNext()){
            String id = query.getString(0);
            String name = query.getString(1);
            String type = query.getString(2);
            String email = query.getString(3);
            Log.e(TAG, "email: " + id + ",name:" + name + ",type:" + type + ",address:" + email);
        }
        query.close();
        /*public static final int TYPE_HOME = 1;
        public static final int TYPE_WORK = 2;
        public static final int TYPE_OTHER = 3;
        public static final int TYPE_MOBILE = 4;*/
    }

    public void testQueryContactPostal(){
        ContentResolver contentResolver = context.getContentResolver();
        int contact_id = 4;
        Cursor query = contentResolver.query(
                ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI,
                new String[]{
                        ContactsContract.CommonDataKinds.StructuredPostal._ID,
                        ContactsContract.CommonDataKinds.StructuredPostal.TYPE,
                        ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS},
                ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID + " = ?",
                new String[]{String.valueOf(contact_id)},
                null);
        if(query == null) return;
        while (query.moveToNext()){
            String id = query.getString(0);
            String type = query.getString(1);
            String address = query.getString(2);
            Log.e(TAG, "Postal: " + id + ",type:" + type + ",address:" + address);
        }
        query.close();

        /*public static final int TYPE_HOME = 1;
        public static final int TYPE_WORK = 2;
        public static final int TYPE_OTHER = 3;*/
    }

    public void testQueryMimeTypeIdByMimeType(){
        Uri MIMETYPES_CONTENT_URI = Uri.withAppendedPath(ContactsContract.AUTHORITY_URI, "mimetypes");
        Uri uriFile = Uri.parse("file:///data/data/com.android.providers.contacts/databases/contact2.db/mimetypes");
        ContentResolver contentResolver = getContext().getContentResolver();
        String mimetype = "vnd.android.cursor.item/im";
        Cursor cursor = contentResolver.query(uriFile,new String[]{"_id"}, "mimetype = ?", new String[]{mimetype}, null);
        if (cursor == null) return;
        while (cursor.moveToNext()){
            int _id = cursor.getInt(0);
            Log.e(TAG, "testQueryMimeTypeIdByMimeType: id = " + _id);
        }
        cursor.close();
    }

    public void testQueryPlaxoByRawContactIdFromData(){
        Plaxo plaxo = new Plaxo();

        int raw_contact_id = 4;
        ContentResolver contentResolver = getContext().getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Data.CONTENT_URI,
                new String[]{
                        ContactsContract.Data.MIMETYPE,
                        ContactsContract.Data.DATA1,
                        ContactsContract.Data.DATA2,
                        ContactsContract.Data.DATA3,
                        ContactsContract.Data.DATA4
                },
                ContactsContract.Data.RAW_CONTACT_ID + " = ?",
                new String[]{String.valueOf(raw_contact_id)}, null);
        if(cursor == null) return;
        while (cursor.moveToNext()){
            String mimeType = cursor.getString(0);
            Log.e(TAG, "testQueryPlaxoByRawContactIdFromData:mimeType= " + mimeType );
            if(mimeType.equals(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)){
                String phone = cursor.getString(1);
                int type = cursor.getInt(2);
                if(type == ContactsContract.CommonDataKinds.Phone.TYPE_HOME){
                    plaxo.homephone = phone;
                } else if(type == ContactsContract.CommonDataKinds.Phone.TYPE_WORK){
                    plaxo.workphone = phone;
                } else {
                    plaxo.phone = phone;
                }
            }
        }
        cursor.close();
    }

    public static final int StructuredName =    1;
    public static final int Phone =   2;
    public static final int Photo =   3;
    public static final int Email =   4;
    public static final int StructuredPostal = 5;
    public static final int GroupMembership = 6;
    public static final int Identity = 7;
    public static final int Organization = 8;
    public static final int Note = 9;

    public void resolve(Cursor cursor, Plaxo plaxo){
        String mimeType = cursor.getString(0);
        switch (getMimeType(mimeType)){
            case Phone:{
                String phone = cursor.getString(1);
                int type = cursor.getInt(2);
                if(type == ContactsContract.CommonDataKinds.Phone.TYPE_HOME){
                    plaxo.homephone = phone;
                } else if(type == ContactsContract.CommonDataKinds.Phone.TYPE_WORK){
                    plaxo.workphone = phone;
                } else {
                    plaxo.phone = phone;
                }
                break;
            }
            default:
                break;
        }

    }

    private int getMimeType(String mimeType) {
        if(mimeType.equals(ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)){
            return Email;
        }else if (mimeType.equals(ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)){
            return Organization;
        }else if (mimeType.equals(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)){
            return Phone;
        }else if (mimeType.equals(ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)){
            return StructuredName;
        }else if (mimeType.equals(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)){
            return StructuredPostal;
        }else if (mimeType.equals(ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE)){
            return GroupMembership;
        }else if (mimeType.equals(ContactsContract.CommonDataKinds.Identity.CONTENT_ITEM_TYPE)){
            return Identity;
        }else if (mimeType.equals(ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)){
            return Photo;
        }else if (mimeType.equals(ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE)){
            return Note;
        }
        return 0;
    }

    public void testQueryGroups(){
        ContentResolver contentResolver = context.getContentResolver();
        Cursor query = contentResolver.query(ContactsContract.Groups.CONTENT_URI,
                new String[]{
                        ContactsContract.Groups._ID,
                        ContactsContract.Groups.TITLE,
                }, null, null, null);
        if(query == null) return;
        while (query.moveToNext()){
            String id = query.getString(0);
            String title = query.getString(1);
            Log.i(TAG, "email: " + id + ",title:" + title);
        }
        query.close();
    }

}