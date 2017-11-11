package com.example.linukey.data.model.local;

/**
 * Created by linukey on 17-2-28.
 */

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.example.linukey.data.model.remote.WebTeamJoinInfo;
import com.example.linukey.data.source.local.DBHelper;
import com.example.linukey.data.source.local.TeamJoinInfoContentProvider;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * 小组申请数据封装
 */
public class TeamJoinInfo implements Serializable {
    public TeamJoinInfo(WebTeamJoinInfo webTeamJoinInfo){
        this.teamId = webTeamJoinInfo.getTeamId();
        this.toUserName = webTeamJoinInfo.getToUserName();
        this.fromUserName = webTeamJoinInfo.getFromUserName();
        this.isdelete = webTeamJoinInfo.getIsdelete();
        this.execType = webTeamJoinInfo.getExecType();
        this.teamName = webTeamJoinInfo.getTeamName();
        this.content = webTeamJoinInfo.getContent();
        this.toUserId = webTeamJoinInfo.getToUserId();
        this.fromUserId = webTeamJoinInfo.getFromUserId();
        this.selfId = webTeamJoinInfo.getSelfId();
    }

    public TeamJoinInfo(){}

    public TeamJoinInfo(int id, String fromUserName, String toUserName,
                        String isdelete, String teamId, String execType, String teamName,
                        String content, String fromUserId, String toUserId, String selfId) {
        this.teamId = teamId;
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.id = id;
        this.fromUserName = fromUserName;
        this.toUserName = toUserName;
        this.isdelete = isdelete;
        this.execType = execType;
        this.teamName = teamName;
        this.content = content;
        this.selfId = selfId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;
    private String teamId; //目标小组Id
    private String teamName; //小组名
    private String fromUserName; //申请人用户名
    private String fromUserId;
    private String toUserName; //小组组长用户名
    private String toUserId;
    private String isdelete; //是否已经删除
    private String execType; //操作类型
    private String content; //申请信息
    private String selfId;

    public String getSelfId() {
        return selfId;
    }

    public void setSelfId(String selfId) {
        this.selfId = selfId;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getExecType() {
        return execType;
    }

    public void setExecType(String execType) {
        this.execType = execType;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
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

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    /**
     * 删除所有小组申请通知信息
     * @return
     */
    public static boolean deleteAll(Context context){
        ContentResolver cr = context.getContentResolver();
        String where = null;
        String[] selectionArgs = null;
        int rid = cr.delete(DBHelper.ContentUri_teamjoindata, where, selectionArgs);
        if(rid > -1)
            return  true;
        return false;
    }

    public static boolean deleteOne(int id, Context context) {
        ContentResolver cr = context.getContentResolver();
        String where = TeamJoinInfoContentProvider.key_id + " = " + id;
        String[] selectionArgs = null;
        int rid = cr.delete(DBHelper.ContentUri_teamjoindata, where, selectionArgs);
        if (rid > -1)
            return true;
        return false;
    }

    public static int saveTeamJoinData(TeamJoinInfo teamJoinInfo, Context context){
        ContentValues contentValues = new ContentValues();
        contentValues.put(TeamJoinInfoContentProvider.key_fromUserName, teamJoinInfo.getFromUserName());
        contentValues.put(TeamJoinInfoContentProvider.key_toUserName, teamJoinInfo.getToUserName());
        contentValues.put(TeamJoinInfoContentProvider.key_isdelete, teamJoinInfo.getIsdelete());
        contentValues.put(TeamJoinInfoContentProvider.key_teamId, teamJoinInfo.getTeamId());
        contentValues.put(TeamJoinInfoContentProvider.key_execType, teamJoinInfo.getExecType());
        contentValues.put(TeamJoinInfoContentProvider.key_teamName, teamJoinInfo.getTeamName());
        contentValues.put(TeamJoinInfoContentProvider.key_content, teamJoinInfo.getContent());
        contentValues.put(TeamJoinInfoContentProvider.key_fromUserId, teamJoinInfo.getFromUserId());
        contentValues.put(TeamJoinInfoContentProvider.key_toUserId, teamJoinInfo.getToUserId());
        contentValues.put(TeamJoinInfoContentProvider.key_selfId, teamJoinInfo.getSelfId());

        Log.i("Project", "正在本地保存teamjoindata，值为:" + teamJoinInfo.toString());

        ContentResolver cr = context.getContentResolver();
        Uri myRowUri = cr.insert(DBHelper.ContentUri_teamjoindata, contentValues);

        if(myRowUri != null)
            return Integer.parseInt(myRowUri.getPathSegments().get(0));
        return -1;
    }

    public static List<TeamJoinInfo> getTeamJoinInfos(Context context) {
        ContentResolver cr = context.getContentResolver();

        String[] result_columns = new String[]{
                TeamJoinInfoContentProvider.keys[0],
                TeamJoinInfoContentProvider.keys[1],
                TeamJoinInfoContentProvider.keys[2],
                TeamJoinInfoContentProvider.keys[3],
                TeamJoinInfoContentProvider.keys[4],
                TeamJoinInfoContentProvider.keys[5],
                TeamJoinInfoContentProvider.keys[6],
                TeamJoinInfoContentProvider.keys[7],
                TeamJoinInfoContentProvider.keys[8],
                TeamJoinInfoContentProvider.keys[9],
                TeamJoinInfoContentProvider.keys[10]
        };

        String where = null;

        String whereArgs[] = null;
        String order = null;

        Cursor resultCursor = cr.query(DBHelper.ContentUri_teamjoindata,
                result_columns, where, whereArgs, order);

        List<TeamJoinInfo> result = null;

        if(resultCursor != null && resultCursor.getCount() > 0){
            result = new ArrayList<>();
            while(resultCursor.moveToNext()){
                TeamJoinInfo teamJoinInfo = new TeamJoinInfo(
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
                result.add(teamJoinInfo);
            }
            resultCursor.close();
            return result;
        }
        return result;
    }
}
