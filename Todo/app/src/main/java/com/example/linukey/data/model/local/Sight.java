package com.example.linukey.data.model.local;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.example.linukey.data.model.remote.WebGoal;
import com.example.linukey.data.model.remote.WebSight;
import com.example.linukey.data.source.local.DBHelper;
import com.example.linukey.data.source.local.ProjectContentProvider;
import com.example.linukey.data.source.local.SelfTaskContentProvider;
import com.example.linukey.data.source.local.SightContentProvider;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by linukey on 12/3/16.
 */

public class Sight extends TaskClassify implements Serializable {
    public Sight(int id, String title, String content, String sightId, String userId, String isdelete) {
        setId(id);
        setTitle(title);
        setContent(content);
        setUserId(userId);
        setSelfId(sightId);
        setIsdelete(isdelete);
    }

    public Sight(WebSight webSight) {
        setId(webSight.getId());
        setTitle(webSight.getTitle());
        setContent(webSight.getContent());
        setUserId(webSight.getUserId());
        setSelfId(webSight.getSelfId());
        setIsdelete(webSight.getIsdelete());
    }

    public Sight() {
    }

    public static int saveSight(Sight sight, Context context) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SightContentProvider.key_title, sight.getTitle());
        contentValues.put(SightContentProvider.key_content, sight.getContent());
        contentValues.put(SightContentProvider.key_sightId, sight.getSelfId());
        contentValues.put(SightContentProvider.key_userId, sight.getUserId());
        contentValues.put(SightContentProvider.key_isdelete, sight.getIsdelete());

        ContentResolver cr = context.getContentResolver();
        Uri myRowUri = cr.insert(DBHelper.ContentUri_sight, contentValues);

        if (myRowUri != null)
            return Integer.parseInt(myRowUri.getPathSegments().get(0));
        return -1;
    }

    public static boolean updateSight(Sight sight, Context context) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SightContentProvider.key_title, sight.getTitle());
        contentValues.put(SightContentProvider.key_content, sight.getContent());
        contentValues.put(SightContentProvider.key_sightId, sight.getSelfId());
        contentValues.put(SightContentProvider.key_userId, sight.getUserId());
        contentValues.put(SightContentProvider.key_isdelete, sight.getIsdelete());

        String where = SightContentProvider.key_id + " = " + sight.getId();
        String[] selectionArgs = null;

        ContentResolver cr = context.getContentResolver();
        int id = cr.update(DBHelper.ContentUri_sight, contentValues, where, selectionArgs);
        if (id > 0)
            return true;
        return false;
    }

    public static boolean deleteOne(int id, Context context) {
        ContentResolver cr = context.getContentResolver();
        String where = SightContentProvider.key_id + " = " + id;
        String[] selectionArgs = null;
        int rid = cr.delete(DBHelper.ContentUri_sight, where, selectionArgs);
        if (rid > -1)
            return true;
        return false;
    }


    /**
     * 删除所有已保存服务器的情景
     *
     * @return
     */
    public static boolean deleteAll(Context context) {
        ContentResolver cr = context.getContentResolver();
        String where = null;
        String[] selectionArgs = null;
        int rid = cr.delete(DBHelper.ContentUri_sight, where, selectionArgs);
        if (rid > -1)
            return true;
        return false;
    }

    public static List<Sight> getSights(Context context) {
        ContentResolver cr = context.getContentResolver();

        String[] result_columns = SightContentProvider.keys;

        String where = null;

        String whereArgs[] = null;
        String order = null;

        Cursor resultCursor = cr.query(DBHelper.ContentUri_sight,
                result_columns, where, whereArgs, order);

        List<Sight> result = null;
        if (resultCursor != null && resultCursor.getCount() > 0) {
            result = new ArrayList<>();
            while (resultCursor.moveToNext()) {
                Sight sight = new Sight(
                        resultCursor.getInt(0),
                        resultCursor.getString(1),
                        resultCursor.getString(2),
                        resultCursor.getString(3),
                        resultCursor.getString(4),
                        resultCursor.getString(5)
                );
                result.add(sight);
            }
        }
        return result;
    }

    /**
     * 通过id查找sight
     * @param id
     * @param context
     * @return
     */
    public static Sight getSightById(int id, Context context) {
        ContentResolver cr = context.getContentResolver();

        String[] result_columns = SightContentProvider.keys;

        String where = SightContentProvider.key_id + " = " + id;

        String whereArgs[] = null;
        String order = null;

        Cursor resultCursor = cr.query(DBHelper.ContentUri_sight,
                result_columns, where, whereArgs, order);

        Sight sight = null;
        if (resultCursor != null && resultCursor.getCount() > 0) {
            while (resultCursor.moveToNext()) {
                sight = new Sight(
                        resultCursor.getInt(0),
                        resultCursor.getString(1),
                        resultCursor.getString(2),
                        resultCursor.getString(3),
                        resultCursor.getString(4),
                        resultCursor.getString(5)
                );
            }
        }
        return sight;
    }

    @Override
    public String toString() {
        return super.toString() + "Sight{}";
    }
}
