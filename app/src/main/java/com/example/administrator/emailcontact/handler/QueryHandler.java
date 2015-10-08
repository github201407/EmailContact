package com.example.administrator.emailcontact.handler;

/**
 * Created by mnq on 2015/10/8.
 */

import android.content.AsyncQueryHandler;
import android.content.Context;
import android.database.Cursor;
import android.widget.CursorTreeAdapter;


public class QueryHandler extends AsyncQueryHandler {
    public static final int TOKEN_GROUP = 0;
    public static final int TOKEN_CHILD = 1;
    private CursorTreeAdapter mAdapter;

    public QueryHandler(Context context, CursorTreeAdapter adapter) {
        super(context.getContentResolver());
        this.mAdapter = adapter;
    }

    @Override
    protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
        switch (token) {
            case TOKEN_GROUP:
                mAdapter.setGroupCursor(cursor);
                break;

            case TOKEN_CHILD:
                int groupPosition = (Integer) cookie;
                mAdapter.setChildrenCursor(groupPosition, cursor);
                break;
        }
    }
}
