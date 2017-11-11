package com.example.linukey.data.model.local;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.example.linukey.data.model.remote.WebTeam;
import com.example.linukey.data.source.local.DBHelper;
import com.example.linukey.data.source.local.SightContentProvider;
import com.example.linukey.data.source.local.TeamContentProvider;
import com.example.linukey.data.source.local.TeamTaskContentProvider;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by linukey on 12/5/16.
 */

public class Team implements Serializable {
    private int id;
    private String teamname;
    private String content;
    private String leaderId;
    private String teamId;
    private String isdelete;
    private String leaderName;

    public Team(int id, String teamname, String content, String leaderId,
                String teamId, String isdelete, String leaderName) {
        this.id = id;
        this.teamname = teamname;
        this.content = content;
        this.leaderId = leaderId;
        this.teamId = teamId;
        this.isdelete = isdelete;
        this.leaderName = leaderName;
    }

    public Team(WebTeam webTeam){
        this.setTeamname(webTeam.getTeamname());
        this.setContent(webTeam.getContent());
        this.setLeaderId(webTeam.getLeaderId());
        this.setTeamId(webTeam.getTeamId());
        this.isdelete = webTeam.getIsdelete();
        this.leaderName = webTeam.getLeaderName();
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    public String getIsdelete() {
        return isdelete;
    }

    public void setIsdelete(String isdelete) {
        this.isdelete = isdelete;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTeamname() {
        return teamname;
    }

    public void setTeamname(String teamname) {
        this.teamname = teamname;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(String leaderId) {
        this.leaderId = leaderId;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public static int saveTeam(Team team, Context context){
        ContentValues contentValues = new ContentValues();
        contentValues.put(TeamContentProvider.key_name, team.getTeamname());
        contentValues.put(TeamContentProvider.key_teamId, team.getTeamId());
        contentValues.put(TeamContentProvider.key_content, team.getContent());
        contentValues.put(TeamContentProvider.key_leaderId, team.getLeaderId());
        contentValues.put(TeamContentProvider.key_isdelete, team.getIsdelete());
        contentValues.put(TeamContentProvider.key_leaderName, team.getLeaderName());

        ContentResolver cr = context.getContentResolver();
        Uri myRowUri = cr.insert(DBHelper.ContentUri_team, contentValues);

        if(myRowUri != null)
            return Integer.parseInt(myRowUri.getPathSegments().get(0));
        return -1;
    }

    /**
     * 根据小组的id来删除小组
     * @param id
     * @param context
     * @return
     */
    public static boolean deleteOne(int id, Context context){
        ContentResolver cr = context.getContentResolver();
        String where = TeamContentProvider.key_id + " = " + id;
        String[] selectionArgs = null;
        int rid = cr.delete(DBHelper.ContentUri_team, where, selectionArgs);
        if (rid > -1)
            return true;
        return false;
    }

    /**
     * 删除所有已保存服务器的用户小组信息
     *
     * @return
     */
    public static boolean deleteAll(Context context) {
        ContentResolver cr = context.getContentResolver();
        String where = null;
        String[] selectionArgs = null;
        int rid = cr.delete(DBHelper.ContentUri_team, where, selectionArgs);
        if (rid > -1)
            return true;
        return false;
    }

    public static boolean updateTeam(Team team, Context context) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TeamContentProvider.key_name, team.getTeamname());
        contentValues.put(TeamContentProvider.key_teamId, team.getTeamId());
        contentValues.put(TeamContentProvider.key_content, team.getContent());
        contentValues.put(TeamContentProvider.key_leaderId, team.getLeaderId());
        contentValues.put(TeamContentProvider.key_isdelete, team.getIsdelete());
        contentValues.put(TeamContentProvider.key_leaderName, team.getLeaderName());

        String where = TeamContentProvider.key_id + " = " + team.getId();
        String[] selectionArgs = null;

        ContentResolver cr = context.getContentResolver();
        int rid = cr.update(DBHelper.ContentUri_team, contentValues, where, selectionArgs);
        if(rid > -1)
            return true;
        else
            return false;
    }

    public static Team getTeamByTeamId(String teamId, Context context){
        ContentResolver cr = context.getContentResolver();

        String[] result_columns = TeamContentProvider.keys;

        String where = TeamContentProvider.key_teamId + " = '" + teamId + "'";

        String selectionArgs[] = null;
        String order = null;

        Cursor resultCursor = cr.query(DBHelper.ContentUri_team,
                result_columns, where, selectionArgs, order);

        Team team = null;
        if(resultCursor != null && resultCursor.getCount() > 0){
            while(resultCursor.moveToNext()){
                team = new Team(
                        resultCursor.getInt(0),
                        resultCursor.getString(1),
                        resultCursor.getString(2),
                        resultCursor.getString(3),
                        resultCursor.getString(4),
                        resultCursor.getString(5),
                        resultCursor.getString(6)
                );
            }
        }

        return team;
    }

    public static List<Team> getTeams(Context context) {
        ContentResolver cr = context.getContentResolver();

        String[] result_columns = new String[]{
                TeamContentProvider.keys[0],
                TeamContentProvider.keys[1],
                TeamContentProvider.keys[2],
                TeamContentProvider.keys[3],
                TeamContentProvider.keys[4],
                TeamContentProvider.keys[5],
                TeamContentProvider.keys[6]
        };

        String where = null;

        String whereArgs[] = null;
        String order = null;

        Cursor resultCursor = cr.query(DBHelper.ContentUri_team,
                result_columns, where, whereArgs, order);

        List<Team> result = null;

        if(resultCursor != null && resultCursor.getCount() > 0){
            result = new ArrayList<>();
            while(resultCursor.moveToNext()){
                Team team = new Team(
                        resultCursor.getInt(0),
                        resultCursor.getString(1),
                        resultCursor.getString(2),
                        resultCursor.getString(3),
                        resultCursor.getString(4),
                        resultCursor.getString(5),
                        resultCursor.getString(6)
                );
                result.add(team);
            }
            resultCursor.close();
            return result;
        }
        return result;
    }
}
