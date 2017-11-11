package com.example.linukey.data.model.local;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.example.linukey.data.model.remote.WebSelfTask;
import com.example.linukey.data.source.local.DBHelper;
import com.example.linukey.data.source.local.SelfTaskContentProvider;
import com.example.linukey.util.TodoHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by linukey on 11/29/16.
 */

public class SelfTask implements Serializable {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;
    private String title;
    private String content;
    private String starttime;
    private String endtime;
    private String clocktime;
    private String projectId;
    private String goalId;
    private String sightId;
    private String userId;
    private String state;
    private String isdelete;
    private String istmp;
    private String selfId;

    public SelfTask(WebSelfTask webSelfTask) {
        this.id = webSelfTask.getId();
        this.title = webSelfTask.getTitle();
        this.content = webSelfTask.getContent();
        this.starttime = webSelfTask.getStarttime();
        this.endtime = webSelfTask.getEndtime();
        this.clocktime = webSelfTask.getClocktime();
        this.projectId = webSelfTask.getProjectId();
        this.goalId = webSelfTask.getGoalId();
        this.sightId = webSelfTask.getSightId();
        this.userId = webSelfTask.getUserId();
        this.state = webSelfTask.getState();
        this.isdelete = webSelfTask.getIsdelete();
        this.istmp = webSelfTask.getIstmp();
        this.selfId = webSelfTask.getSelfId();
    }

    public SelfTask(int id, String title, String content, String starttime, String endtime,
                    String clocktime, String projectId, String goalId, String sightId,
                    String userId, String state, String isdelete, String isTmp,
                    String selfId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.starttime = starttime;
        this.endtime = endtime;
        this.clocktime = clocktime;
        this.projectId = projectId;
        this.goalId = goalId;
        this.sightId = sightId;
        this.userId = userId;
        this.state = state;
        this.isdelete = isdelete;
        this.istmp = isTmp;
        this.selfId = selfId;
    }


    public SelfTask() {
    }

    public String getSelfId() {
        return selfId;
    }

    public void setSelfId(String selfId) {
        this.selfId = selfId;
    }

    public String getIsdelete() {
        return isdelete;
    }

    public String getIstmp() {
        return istmp;
    }

    public void setIstmp(String istmp) {
        this.istmp = istmp;
    }

    public String getIsTmp() {
        return istmp;
    }

    public void setIsTmp(String isTmp) {
        this.istmp = isTmp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getClocktime() {
        return clocktime;
    }

    public void setClocktime(String clocktime) {
        this.clocktime = clocktime;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getGoalId() {
        return goalId;
    }

    public void setGoalId(String goalId) {
        this.goalId = goalId;
    }

    public String getSightId() {
        return sightId;
    }

    public void setSightId(String sightId) {
        this.sightId = sightId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String isdelete() {
        return isdelete;
    }

    public void setIsdelete(String isdelete) {
        this.isdelete = isdelete;
    }

    /**
     * 保存单个任务
     *
     * @param selfTask
     * @param context
     * @return
     */
    public static int saveTaskInfo(SelfTask selfTask, Context context) {
        ContentValues newValues = new ContentValues();
        newValues.put(SelfTaskContentProvider.key_title, selfTask.getTitle());
        newValues.put(SelfTaskContentProvider.key_content, selfTask.getContent());
        newValues.put(SelfTaskContentProvider.key_starttime, selfTask.getStarttime());
        newValues.put(SelfTaskContentProvider.key_endtime, selfTask.getEndtime());
        newValues.put(SelfTaskContentProvider.key_clocktime, selfTask.getClocktime());
        newValues.put(SelfTaskContentProvider.key_projectId, selfTask.getProjectId());
        newValues.put(SelfTaskContentProvider.key_goalId, selfTask.getGoalId());
        newValues.put(SelfTaskContentProvider.key_sightId, selfTask.getSightId());
        newValues.put(SelfTaskContentProvider.key_userId, selfTask.getUserId());
        newValues.put(SelfTaskContentProvider.key_isdelete, selfTask.isdelete());
        newValues.put(SelfTaskContentProvider.key_state, selfTask.getState());
        newValues.put(SelfTaskContentProvider.key_istmp, selfTask.getIsTmp());
        newValues.put(SelfTaskContentProvider.key_selfId, selfTask.getSelfId());

        Log.d("error", selfTask.toString());

        ContentResolver cr = context.getContentResolver();
        Uri myRowUri = cr.insert(DBHelper.ContentUri_selftask, newValues);
        if (myRowUri != null)
            return Integer.parseInt(myRowUri.getPathSegments().get(0));
        return -1;
    }

    /**
     * 通过id删除单个任务
     *
     * @param id
     * @param context
     * @return
     */
    public static boolean deleteOne(int id, Context context) {
        ContentResolver cr = context.getContentResolver();
        String where = SelfTaskContentProvider.key_id + " = " + id;
        String[] selectionArgs = null;
        int rid = cr.delete(DBHelper.ContentUri_selftask, where, selectionArgs);
        if (rid > -1)
            return true;
        return false;
    }

    /**
     * 删除所有任务
     *
     * @return
     */
    public static boolean deleteAll(Context context) {
        ContentResolver cr = context.getContentResolver();
        String where = null;
        String[] selectionArgs = null;
        int rid = cr.delete(DBHelper.ContentUri_selftask, where, selectionArgs);
        if (rid > -1)
            return true;
        return false;
    }

    /**
     * 更新任务
     *
     * @param selfTask
     * @param context
     * @return
     */
    public static boolean updateTaskInfo(SelfTask selfTask, Context context) {
        ContentValues newValues = new ContentValues();
        newValues.put(SelfTaskContentProvider.key_title, selfTask.getTitle());
        newValues.put(SelfTaskContentProvider.key_content, selfTask.getContent());
        newValues.put(SelfTaskContentProvider.key_starttime, selfTask.getStarttime());
        newValues.put(SelfTaskContentProvider.key_endtime, selfTask.getEndtime());
        newValues.put(SelfTaskContentProvider.key_clocktime, selfTask.getClocktime());
        newValues.put(SelfTaskContentProvider.key_projectId, selfTask.getProjectId());
        newValues.put(SelfTaskContentProvider.key_goalId, selfTask.getGoalId());
        newValues.put(SelfTaskContentProvider.key_sightId, selfTask.getSightId());
        newValues.put(SelfTaskContentProvider.key_istmp, selfTask.getIsTmp());
        newValues.put(SelfTaskContentProvider.key_isdelete, selfTask.getIsdelete());
        newValues.put(SelfTaskContentProvider.key_state, selfTask.getState());
        newValues.put(SelfTaskContentProvider.key_selfId, selfTask.getSelfId());

        String where = SelfTaskContentProvider.key_id + " = " + selfTask.getId();
        String[] selectionArgs = null;

        ContentResolver cr = context.getContentResolver();
        int number = cr.update(DBHelper.ContentUri_selftask, newValues, where, selectionArgs);
        if (number > 0) {
            return true;
        }

        return false;
    }


    /**
     * 获得所有任务
     *
     * @param context
     * @return
     */
    public static List<SelfTask> getTasks(Context context) {
        ContentResolver cr = context.getContentResolver();

        String[] result_columns = SelfTaskContentProvider.keys;

        String where = null;

        String whereArgs[] = null;
        String order = null;

        Cursor resultCursor = cr.query(DBHelper.ContentUri_selftask,
                result_columns, where, whereArgs, order);

        List<SelfTask> result = null;

        if (resultCursor != null && resultCursor.getCount() > 0) {
            result = new ArrayList<>();
            while (resultCursor.moveToNext()) {
                SelfTask selfTask = new SelfTask(
                        resultCursor.getInt(0),
                        resultCursor.getString(1),
                        resultCursor.getString(2),
                        resultCursor.getString(3),
                        resultCursor.getString(4),
                        resultCursor.getString(5),
                        resultCursor.getString(6),
                        resultCursor.getString(7),
                        resultCursor.getString(8),
                        resultCursor.getString(9),
                        resultCursor.getString(10),
                        resultCursor.getString(11),
                        resultCursor.getString(12),
                        resultCursor.getString(13)
                );
                result.add(selfTask);
            }
            resultCursor.close();
        }
        return result;
    }

    /**
     * 通过Id获取任务
     *
     * @param id
     * @param context
     * @return
     */
    public static SelfTask getTaskById(int id, Context context) {
        ContentResolver cr = context.getContentResolver();

        String[] result_columns = SelfTaskContentProvider.keys;

        String where = SelfTaskContentProvider.key_id + " = " + id;
        String whereArgs[] = null;
        String order = null;

        Cursor resultCursor = cr.query(DBHelper.ContentUri_selftask,
                result_columns, where, whereArgs, order);

        SelfTask result = null;

        if (resultCursor != null && resultCursor.getCount() > 0) {
            while (resultCursor.moveToNext()) {
                result = new SelfTask(
                        resultCursor.getInt(0),
                        resultCursor.getString(1),
                        resultCursor.getString(2),
                        resultCursor.getString(3),
                        resultCursor.getString(4),
                        resultCursor.getString(5),
                        resultCursor.getString(6),
                        resultCursor.getString(7),
                        resultCursor.getString(8),
                        resultCursor.getString(9),
                        resultCursor.getString(10),
                        resultCursor.getString(11),
                        resultCursor.getString(12),
                        resultCursor.getString(13)
                );
            }
            resultCursor.close();
        }
        return result;
    }

}
