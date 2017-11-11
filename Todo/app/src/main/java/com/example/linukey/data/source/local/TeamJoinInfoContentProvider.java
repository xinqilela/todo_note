package com.example.linukey.data.source.local;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by linukey on 17-3-3.
 */

public class TeamJoinInfoContentProvider extends ContentProvider {

    public static final String key_id = "id";
    public static final String key_fromUserName = "fromUserName";
    public static final String key_toUserName = "toUserName";
    public static final String key_isdelete = "isdelete";
    public static final String key_teamId = "teamId";
    public static final String key_execType = "execType";
    public static final String key_teamName = "teamName";
    public static final String key_content = "content";
    public static final String key_fromUserId = "fromUserId";
    public static final String key_toUserId = "toUserId";
    public static final String key_selfId = "selfId";
    public static final String tableName = "TeamJoinInfo";

    public static final String[] keys = {
            key_id,
            key_fromUserName,
            key_toUserName,
            key_isdelete,
            key_teamId,
            key_execType,
            key_teamName,
            key_content,
            key_fromUserId,
            key_toUserId,
            key_selfId
    };

    public final static String sql_createTable = "create table " + tableName + "(" +
            keys[0] + " integer primary key autoincrement," +
            keys[1] + " varchar," +
            keys[2] + " varchar," +
            keys[3] + " varchar not null," +
            keys[4] + " varchar not null," +
            keys[5] + " varchar not null," +
            keys[6] + " varchar not null," +
            keys[7] + " varchar," +
            keys[8] + " varchar," +
            keys[9] + " varchar," +
            keys[10] + " varchar not null);";

    DBHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext(), DBHelper.dbName, null, DBHelper.dbVersion);
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String groupBy = null;
        String having = null;

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(tableName);

        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, groupBy, having, sortOrder);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {

        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String nullColumnHack = null;

        long id = db.insert(tableName, nullColumnHack, values);

        if (id > -1) {
            Uri insertedId = ContentUris.withAppendedId(uri, id);
            getContext().getContentResolver().notifyChange(insertedId, null);
            return insertedId;
        } else
            return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        if (selection == null)
            selection = "1";

        int deleteCount = db.delete(tableName, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);

        return deleteCount;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int updateCount = db.update(tableName, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return updateCount;
    }
}
