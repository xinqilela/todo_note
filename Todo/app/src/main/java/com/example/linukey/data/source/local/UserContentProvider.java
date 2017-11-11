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
 * Created by linukey on 11/29/16.
 */

public class UserContentProvider extends ContentProvider {

    public static final String key_id = "id";
    public static final String key_username = "username";
    public static final String key_password = "password";
    public static final String key_email = "email";
    public static final String key_phonenumber = "phonenumber";
    public static final String key_usergroup = "usergroup";
    public static final String key_userId = "userId";
    public static final String key_imgpath = "imgpath";
    public static final String key_isAlter = "isAlter";
    public static final String key_webId = "webId";

    public static final String[] keys = new String[]{
            key_id, key_username, key_password, key_email, key_phonenumber, key_usergroup,
            key_userId, key_imgpath, key_isAlter, key_webId
    };

    public static final String tableName = "user";

    public static final String sql_createTable = "create table " + tableName + "(" +
            keys[0] + " integer primary key autoincrement," +
            keys[1] + " varchar not null," +
            keys[2] + " varchar not null," +
            keys[3] + " varchar not null," +
            keys[4] + " varchar not null," +
            keys[5] + " varchar not null," +
            keys[6] + " varchar not null," +
            keys[7] + " varchar," +
            keys[8] + " varchar not null," +
            keys[9] + " varchar not null);";


    DBHelper dbHelper = null;

    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext(), DBHelper.dbName, null, DBHelper.dbVersion);
        return true;
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
            Uri insertedId = ContentUris.withAppendedId(DBHelper.ContentUri_user, id);
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
