package com.example.linukey.data.source.local;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.example.linukey.util.TodoHelper;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.util.List;

/**
 * Created by linukey on 11/29/16.
 */

public class DBHelper extends SQLiteOpenHelper {
    public static final Uri ContentUri_selftask = Uri.parse("content://com.linukey.Todo.self_task");
    public static final Uri ContentUri_user = Uri.parse("content://com.linukey.Todo.user");
    public static final Uri ContentUri_project = Uri.parse("content://com.linukey.Todo.project");
    public static final Uri ContentUri_goal = Uri.parse("content://com.linukey.Todo.goal");
    public static final Uri ContentUri_sight = Uri.parse("content://com.linukey.Todo.sight");
    public static final Uri ContentUri_team = Uri.parse("content://com.linukey.Todo.team");
    public static final Uri ContentUri_teamtask = Uri.parse("content://com.linukey.Todo.teamtask");
    public static final Uri ContentUri_teamjoindata = Uri.parse("content://com.linukey.Todo.teamjoindata");

    public static final String dbName = "Todo.db";
    public static final int dbVersion = 1;

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SelfTaskContentProvider.sql_createTable);
        db.execSQL(UserContentProvider.sql_createTable);
        db.execSQL(ProjectContentProvider.sql_createTable);
        db.execSQL(GoalContentProvider.sql_createTable);
        db.execSQL(SightContentProvider.sql_createTable);
        db.execSQL(TeamContentProvider.sql_createTable);
        db.execSQL(TeamTaskContentProvider.sql_createTable);
        db.execSQL(TeamJoinInfoContentProvider.sql_createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if it exists" + SelfTaskContentProvider.tableName);
        db.execSQL("drop table if it exists" + UserContentProvider.tableName);
        db.execSQL("drop table if it exists" + ProjectContentProvider.tableName);
        db.execSQL("drop table if it exists" + GoalContentProvider.tableName);
        db.execSQL("drop table if it exists" + SightContentProvider.tableName);
        db.execSQL("drop table if it exists" + TeamContentProvider.tableName);
        db.execSQL("drop table if it exists" + TeamTaskContentProvider.tableName);
        db.execSQL("drop table if it exists" + TeamJoinInfoContentProvider.tableName);
        onCreate(db);
    }

    public static void dropAllTables(){
        DBHelper dbHelper = new DBHelper(TodoHelper.getInstance(), DBHelper.dbName, null, DBHelper.dbVersion);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.delete(SelfTaskContentProvider.tableName, null, null);
        database.delete(UserContentProvider.tableName, null, null);
        database.delete(ProjectContentProvider.tableName, null, null);
        database.delete(GoalContentProvider.tableName, null, null);
        database.delete(SightContentProvider.tableName, null, null);
        database.delete(TeamContentProvider.tableName, null, null);
        database.delete(TeamTaskContentProvider.tableName, null ,null);
        database.delete(TeamJoinInfoContentProvider.tableName, null, null);
    }
}