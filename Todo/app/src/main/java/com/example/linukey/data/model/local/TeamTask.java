package com.example.linukey.data.model.local;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.example.linukey.data.model.remote.WebTeamTask;
import com.example.linukey.data.source.local.DBHelper;
import com.example.linukey.data.source.local.SelfTaskContentProvider;
import com.example.linukey.data.source.local.TeamContentProvider;
import com.example.linukey.data.source.local.TeamTaskContentProvider;
import com.example.linukey.util.TodoHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by linukey on 12/5/16.
 */

public class TeamTask implements Serializable {
    private int id;
    private String title;
    private String content;
    private String starttime;
    private String endtime;
    private String clocktime;
    private String projectId;
    private String state;
    private String isdelete;
    private String teamId;
    private String selfId;

    public TeamTask(WebTeamTask webTeamTask){
        this.id = webTeamTask.getId();
        this.title = webTeamTask.getTitle();
        this.content = webTeamTask.getContent();
        this.starttime = webTeamTask.getStarttime();
        this.endtime = webTeamTask.getEndtime();
        this.clocktime = webTeamTask.getClocktime();
        this.projectId = webTeamTask.getProjectId();
        this.state = webTeamTask.getState();
        this.isdelete = webTeamTask.getIsdelete();
        this.teamId = webTeamTask.getTeamId();
        this.selfId = webTeamTask.getSelfId();
    }

    public TeamTask(int id, String title, String content, String starttime, String endtime,
                    String clocktime, String projectId, String teamId,
                    String state, String isdelete, String selfId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.starttime = starttime;
        this.endtime = endtime;
        this.clocktime = clocktime;
        this.projectId = projectId;
        this.state = state;
        this.isdelete = isdelete;
        this.teamId = teamId;
        this.selfId = selfId;
    }

    public String getSelfId() {
        return selfId;
    }

    public void setSelfId(String selfId) {
        this.selfId = selfId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getIsdelete() {
        return isdelete;
    }

    public void setIsdelete(String isdelete) {
        this.isdelete = isdelete;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public static int saveTeamTask(TeamTask teamTask, Context context){
        ContentValues contentValues = new ContentValues();
        contentValues.put(TeamTaskContentProvider.key_title, teamTask.getTitle());
        contentValues.put(TeamTaskContentProvider.key_content, teamTask.getContent());
        contentValues.put(TeamTaskContentProvider.key_starttime, teamTask.getStarttime());
        contentValues.put(TeamTaskContentProvider.key_endtime, teamTask.getEndtime());
        contentValues.put(TeamTaskContentProvider.key_clocktime, teamTask.getClocktime());
        contentValues.put(TeamTaskContentProvider.key_state, teamTask.getState());
        contentValues.put(TeamTaskContentProvider.key_isdelete, teamTask.getIsdelete());
        contentValues.put(TeamTaskContentProvider.key_projectId, teamTask.getProjectId());
        contentValues.put(TeamTaskContentProvider.key_teamId, teamTask.getTeamId());
        contentValues.put(TeamTaskContentProvider.key_selfId, teamTask.getSelfId());

        ContentResolver cr = context.getContentResolver();
        Uri myRowUri = cr.insert(DBHelper.ContentUri_teamtask, contentValues);

        if(myRowUri != null)
            return Integer.parseInt(myRowUri.getPathSegments().get(0));

        return -1;
    }

    public static boolean updateTeamTask(TeamTask teamTask, Context context){
        ContentValues contentValues = new ContentValues();
        contentValues.put(TeamTaskContentProvider.key_title, teamTask.getTitle());
        contentValues.put(TeamTaskContentProvider.key_content, teamTask.getContent());
        contentValues.put(TeamTaskContentProvider.key_starttime, teamTask.getStarttime());
        contentValues.put(TeamTaskContentProvider.key_endtime, teamTask.getEndtime());
        contentValues.put(TeamTaskContentProvider.key_projectId, teamTask.getProjectId());
        contentValues.put(TeamTaskContentProvider.key_state, teamTask.getState());
        contentValues.put(TeamTaskContentProvider.key_teamId, teamTask.getTeamId());
        contentValues.put(TeamTaskContentProvider.key_isdelete, teamTask.getIsdelete());
        contentValues.put(TeamTaskContentProvider.key_clocktime, teamTask.getClocktime());
        contentValues.put(TeamTaskContentProvider.key_selfId, teamTask.getSelfId());

        String where = TeamTaskContentProvider.key_id + " = " + teamTask.getId();
        String[] selectionArgs = null;

        ContentResolver cr = context.getContentResolver();
        int rid = cr.update(DBHelper.ContentUri_teamtask, contentValues, where, selectionArgs);

        if(rid > -1)
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
        int rid = cr.delete(DBHelper.ContentUri_teamtask, where, selectionArgs);
        if (rid > -1)
            return true;
        return false;
    }

    public static List<TeamTask> getTeamTasks(Context context){
        ContentResolver cr = context.getContentResolver();

        String[] result_columns = new String[]{
                TeamTaskContentProvider.keys[0],
                TeamTaskContentProvider.keys[1],
                TeamTaskContentProvider.keys[2],
                TeamTaskContentProvider.keys[3],
                TeamTaskContentProvider.keys[4],
                TeamTaskContentProvider.keys[5],
                TeamTaskContentProvider.keys[6],
                TeamTaskContentProvider.keys[7],
                TeamTaskContentProvider.keys[8],
                TeamTaskContentProvider.keys[9],
                TeamTaskContentProvider.keys[10]
        };

        String where = null;

        String whereArgs[] = null;
        String order = null;

        Cursor resultCursor = cr.query(DBHelper.ContentUri_teamtask,
                result_columns, where, whereArgs, order);

        List<TeamTask> result = null;

        if(resultCursor != null && resultCursor.getCount() > 0){
            result = new ArrayList<>();
            while(resultCursor.moveToNext()){
                TeamTask teamTask = new TeamTask(
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
                        resultCursor.getString(10)
                );
                result.add(teamTask);
            }
            resultCursor.close();
            return result;
        }
        return result;
    }

    public static boolean deleteOne(int id, Context context){
        ContentResolver cr = context.getContentResolver();
        String where = TeamTaskContentProvider.key_id + " = " + id;
        String[] selectionArgs = null;
        int rid = cr.delete(DBHelper.ContentUri_teamtask, where, selectionArgs);
        if(rid > -1)
            return true;
        return false;
    }

    public static boolean completed(int id, Context context){
        ContentValues contentValues = new ContentValues();
        contentValues.put(TeamTaskContentProvider.key_state, TodoHelper.TaskState.get("complete"));

        String where = TeamTaskContentProvider.key_id + " = " + id;
        String[] selectionArgs = null;

        ContentResolver cr = context.getContentResolver();
        int rid = cr.update(DBHelper.ContentUri_teamtask, contentValues, where, selectionArgs);
        if(rid > -1)
            return true;
        else
            return false;
    }
}
