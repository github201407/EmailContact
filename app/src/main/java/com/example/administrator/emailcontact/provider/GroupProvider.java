package com.example.administrator.emailcontact.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.example.administrator.emailcontact.database.GroupSQLiteHelper;

import java.util.HashMap;

/**
 * Created by Administrator on 2015/10/30.
 */
public class GroupProvider extends ContentProvider {
    private static final String TAG = "ContactProvider";

    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(Groups.AUTHORITY, "item", Groups.ITEM);
        uriMatcher.addURI(Groups.AUTHORITY, "item/#", Groups.ITEM_ID);
        uriMatcher.addURI(Groups.AUTHORITY, "pos/#", Groups.ITEM_POS);
    }

    private GroupSQLiteHelper dbHelper = null;
    private ContentResolver resolver = null;

    private static final HashMap<String, String> contactProjectionMap;

    static {
        contactProjectionMap = new HashMap<>();
        contactProjectionMap.put(Groups.ID, Groups.ID);
        contactProjectionMap.put(Groups.PARENT, Groups.PARENT);
        contactProjectionMap.put(Groups.ROOT, Groups.ROOT);
        contactProjectionMap.put(Groups.TYPE, Groups.TYPE);
        contactProjectionMap.put(Groups.NAME, Groups.NAME);
        contactProjectionMap.put(Groups.CREATE_DATE, Groups.CREATE_DATE);
    }

    /**
     * Implement this to initialize your content provider on startup.
     * This method is called for all registered content providers on the
     * application main thread at application launch time.  It must not perform
     * lengthy operations, or application startup will be delayed.
     * <p/>
     * <p>You should defer nontrivial initialization (such as opening,
     * upgrading, and scanning databases) until the content provider is used
     * (via {@link #query}, {@link #insert}, etc).  Deferred initialization
     * keeps application startup fast, avoids unnecessary work if the provider
     * turns out not to be needed, and stops database errors (such as a full
     * disk) from halting application launch.
     * <p/>
     * <p>If you use SQLite, {@link SQLiteOpenHelper}
     * is a helpful utility class that makes it easy to manage databases,
     * and will automatically defer opening until first use.  If you do use
     * SQLiteOpenHelper, make sure to avoid calling
     * {@link SQLiteOpenHelper#getReadableDatabase} or
     * {@link SQLiteOpenHelper#getWritableDatabase}
     * from this method.  (Instead, override
     * {@link SQLiteOpenHelper#onOpen} to initialize the
     * database when it is first opened.)
     *
     * @return true if the provider was successfully loaded, false otherwise
     */
    @Override
    public boolean onCreate() {
        Context mContext = getContext();
        dbHelper = new GroupSQLiteHelper(mContext);
        if (mContext != null) {
            resolver = mContext.getContentResolver();
        }
        return true;
    }

    /**
     * Implement this to handle query requests from clients.
     * This method can be called from multiple threads, as described in
     * <a href="{@docRoot}guide/topics/fundamentals/processes-and-threads.html#Threads">Processes
     * and Threads</a>.
     * <p/>
     * Example client call:<p>
     * <pre>// Request a specific record.
     * Cursor managedCursor = managedQuery(
     * ContentUris.withAppendedId(Groups.People.CONTENT_URI, 2),
     * projection,    // Which columns to return.
     * null,          // WHERE clause.
     * null,          // WHERE clause value substitution
     * People.NAME + " ASC");   // Sort order.</pre>
     * Example implementation:<p>
     * <pre>// SQLiteQueryBuilder is a helper class that creates the
     * // proper SQL syntax for us.
     * SQLiteQueryBuilder qBuilder = new SQLiteQueryBuilder();
     * <p/>
     * // Set the table we're querying.
     * qBuilder.setTables(DATABASE_TABLE_NAME);
     * <p/>
     * // If the query ends in a specific record number, we're
     * // being asked for a specific record, so set the
     * // WHERE clause in our query.
     * if((URI_MATCHER.match(uri)) == SPECIFIC_MESSAGE){
     * qBuilder.appendWhere("_id=" + uri.getPathLeafId());
     * }
     * <p/>
     * // Make the query.
     * Cursor c = qBuilder.query(mDb,
     * projection,
     * selection,
     * selectionArgs,
     * groupBy,
     * having,
     * sortOrder);
     * c.setNotificationUri(getContext().getContentResolver(), uri);
     * return c;</pre>
     *
     * @param uri           The URI to query. This will be the full URI sent by the client;
     *                      if the client is requesting a specific record, the URI will end in a record number
     *                      that the implementation should parse and add to a WHERE or HAVING clause, specifying
     *                      that _id value.
     * @param projection    The list of columns to put into the cursor. If
     *                      {@code null} all columns are included.
     * @param selection     A selection criteria to apply when filtering rows.
     *                      If {@code null} then all rows are included.
     * @param selectionArgs You may include ?s in selection, which will be replaced by
     *                      the values from selectionArgs, in order that they appear in the selection.
     *                      The values will be bound as Strings.
     * @param sortOrder     How the rows in the cursor should be sorted.
     *                      If {@code null} then the provider is free to define the sort order.
     * @return a Cursor or {@code null}.
     */
    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.i(TAG, "GroupProvider.query: " + uri);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        SQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();
        String limit = null;

        switch (uriMatcher.match(uri)) {
            case Groups.ITEM: {
                sqlBuilder.setTables(Groups.TABLE_NAME);
                sqlBuilder.setProjectionMap(contactProjectionMap);
                break;
            }
            case Groups.ITEM_ID: {
                String id = uri.getPathSegments().get(1);
                sqlBuilder.setTables(Groups.TABLE_NAME);
                sqlBuilder.setProjectionMap(contactProjectionMap);
                sqlBuilder.appendWhere(Groups.ID + "=" + id);
                break;
            }
            case Groups.ITEM_POS: {
                String pos = uri.getPathSegments().get(1);
                sqlBuilder.setTables(Groups.TABLE_NAME);
                sqlBuilder.setProjectionMap(contactProjectionMap);
                limit = pos + ", 1";
                break;
            }
            default:
                throw new IllegalArgumentException("Error Uri: " + uri);
        }

        Cursor cursor = sqlBuilder.query(db, projection, selection, selectionArgs, null, null,
                TextUtils.isEmpty(sortOrder) ? Groups.DEFAULT_SORT_ORDER : sortOrder, limit);
        cursor.setNotificationUri(resolver, uri);

        return cursor;
    }

    /**
     * Implement this to handle requests for the MIME type of the data at the
     * given URI.  The returned MIME type should start with
     * <code>vnd.android.cursor.item</code> for a single record,
     * or <code>vnd.android.cursor.dir/</code> for multiple items.
     * This method can be called from multiple threads, as described in
     * <a href="{@docRoot}guide/topics/fundamentals/processes-and-threads.html#Threads">Processes
     * and Threads</a>.
     * <p/>
     * <p>Note that there are no permissions needed for an application to
     * access this information; if your content provider requires read and/or
     * write permissions, or is not exported, all applications can still call
     * this method regardless of their access permissions.  This allows them
     * to retrieve the MIME type for a URI when dispatching intents.
     *
     * @param uri the URI to query.
     * @return a MIME type string, or {@code null} if there is no type.
     */
    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case Groups.ITEM:
                return Groups.CONTENT_TYPE;
            case Groups.ITEM_ID:
            case Groups.ITEM_POS:
                return Groups.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Error Uri: " + uri);
        }
    }

    /**
     * Implement this to handle requests to insert a new row.
     * As a courtesy, call {@link ContentResolver#notifyChange(Uri, ContentObserver) notifyChange()}
     * after inserting.
     * This method can be called from multiple threads, as described in
     * <a href="{@docRoot}guide/topics/fundamentals/processes-and-threads.html#Threads">Processes
     * and Threads</a>.
     *
     * @param uri    The content:// URI of the insertion request. This must not be {@code null}.
     * @param values A set of column_name/value pairs to add to the database.
     *               This must not be {@code null}.
     * @return The URI for the newly inserted item.
     */
    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.i(TAG, "GroupProvider.insert: " + uri);
        if (uriMatcher.match(uri) != Groups.ITEM) {
            throw new IllegalArgumentException("Error Uri: " + uri);
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id = db.insert(Groups.TABLE_NAME, Groups.ID, values);
        if (id < 0) {
            throw new SQLiteException("Unable to insert " + values + " for " + uri);
        }
        Uri newUri = ContentUris.withAppendedId(uri, id);
        resolver.notifyChange(newUri, null);
        return newUri;
    }

    /**
     * Implement this to handle requests to delete one or more rows.
     * The implementation should apply the selection clause when performing
     * deletion, allowing the operation to affect multiple rows in a directory.
     * As a courtesy, call {@link ContentResolver#notifyChange(Uri, ContentObserver) notifyChange()}
     * after deleting.
     * This method can be called from multiple threads, as described in
     * <a href="{@docRoot}guide/topics/fundamentals/processes-and-threads.html#Threads">Processes
     * and Threads</a>.
     * <p/>
     * <p>The implementation is responsible for parsing out a row ID at the end
     * of the URI, if a specific row is being deleted. That is, the client would
     * pass in <code>content://Groups/people/22</code> and the implementation is
     * responsible for parsing the record number (22) when creating a SQL statement.
     *
     * @param uri           The full URI to query, including a row ID (if a specific record is requested).
     * @param selection     An optional restriction to apply to rows when deleting.
     * @param selectionArgs
     * @return The number of rows affected.
     * @throws SQLException
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.i(TAG, "GroupProvider.delete: " + uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count;
        switch (uriMatcher.match(uri)) {
            case Groups.ITEM: {
                count = db.delete(Groups.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case Groups.ITEM_ID: {
                String id = uri.getPathSegments().get(1);
                count = db.delete(Groups.TABLE_NAME, Groups.ID + "=" + id
                        + (!TextUtils.isEmpty(selection) ? " and (" + selection + ')' : ""), selectionArgs);
                break;
            }
            default:
                throw new IllegalArgumentException("Error Uri: " + uri);
        }
        resolver.notifyChange(uri, null);
        return count;
    }

    /**
     * Implement this to handle requests to update one or more rows.
     * The implementation should update all rows matching the selection
     * to set the columns according to the provided values map.
     * As a courtesy, call {@link ContentResolver#notifyChange(Uri, ContentObserver) notifyChange()}
     * after updating.
     * This method can be called from multiple threads, as described in
     * <a href="{@docRoot}guide/topics/fundamentals/processes-and-threads.html#Threads">Processes
     * and Threads</a>.
     *
     * @param uri           The URI to query. This can potentially have a record ID if this
     *                      is an update request for a specific record.
     * @param values        A set of column_name/value pairs to update in the database.
     *                      This must not be {@code null}.
     * @param selection     An optional filter to match rows to update.
     * @param selectionArgs
     * @return the number of rows affected.
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Log.i(TAG, "GroupProvider.update: " + uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count;
        switch (uriMatcher.match(uri)) {
            case Groups.ITEM: {
                count = db.update(Groups.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            case Groups.ITEM_ID: {
                String id = uri.getPathSegments().get(1);
                count = db.update(Groups.TABLE_NAME, values, Groups.ID + "=" + id
                        + (!TextUtils.isEmpty(selection) ? " and (" + selection + ')' : ""), selectionArgs);
                break;
            }
            default:
                throw new IllegalArgumentException("Error Uri: " + uri);
        }
        resolver.notifyChange(uri, null);
        return count;
    }

    @Override
    public Bundle call(String method, String request, Bundle args) {
        Log.i(TAG, "GroupProvider.call: " + method);
        if(method.equals(Groups.METHOD_GET_ITEM_COUNT)) {
            return getItemCount();
        }
        throw new IllegalArgumentException("Error method call: " + method);
    }

    private Bundle getItemCount() {
        Log.i(TAG, "GroupProvider.getItemCount");
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from " + Groups.TABLE_NAME, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        Bundle bundle = new Bundle();
        bundle.putInt(Groups.KEY_ITEM_COUNT, count);
        cursor.close();
        db.close();
        return bundle;
    }
}
