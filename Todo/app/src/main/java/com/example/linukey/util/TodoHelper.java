package com.example.linukey.util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.example.linukey.data.model.local.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by linukey on 11/29/16.
 */

public class TodoHelper extends Application {

    /**
     * web服务器地址
     */
    public static String RemoteUrl = "http://192.168.100.7:8080/Todoweb/";
    /**
     * web端接收客户端的判断字段
     */
    public static Map<String, String> From = new HashMap<String, String>() {{
        put("server", "server");
        put("client", "client");
    }};
    /**
     * 任务状态
     */
    public static Map<String, String> TaskState = new HashMap<String, String>() {{
        put("complete", "complete");
        put("noComplete", "noComplete");
        put("outOfDate", "outOfDate");
    }};
    /**
     * 项目状态
     */
    public static Map<String, String> ProjectType = new HashMap<String, String>() {{
        put("self", "self");
        put("team", "team");
    }};
    /**
     * 用户组
     */
    public static Map<String, String> UserGroup = new HashMap<String, String>() {{
        put("normal", "normal");
        put("administrator", "administrator");
    }};
    /**
     * 项目，目标 的状态
     */
    public static Map<String, String> PGS_State = new HashMap<String, String>() {{
        put("complete", "complete");
        put("noComplete", "noComplete");
    }};
    /**
     * Android端与端之间通信的操作命令
     */
    public static Map<String, String> PtoPExecType = new HashMap<String, String>() {{
        put("joinTeam", "joinTeam");
        put("joinTeamResult", "joinTeamResult");
        put("quit", "quit");
    }};

    /**
     * TeamJoinData的操作命令
     */
    public static Map<String, String> TeamJoinType = new HashMap<String, String>() {{
        put("0", "0"); //不同意
        put("1", "1"); //同意
        put("2", "2"); //搁置
        put("3", "3"); //退出
    }};

    public static String UserName = null;
    public static String PassWord = null;
    public static String UserId = null;
    public static String Usergroup = null;
    public static String UserLogo = null;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        instance = this;
    }

    private static TodoHelper instance;

    /**
     * 在别的地方获取到Application
     *
     * @return
     */
    public static TodoHelper getInstance() {
        return instance;
    }

    /**
     * 获取当前登录的用户信息
     *
     * @return
     */
    public static User getCurrentUserInfo(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("data", Activity.MODE_PRIVATE);

        if (preferences != null && preferences.contains("userId")) {
            User user = User.getCurrentUserInfo(preferences.getString("userId", ""), context);

            TodoHelper.UserName = user.getUsername();
            TodoHelper.UserId = user.getUserId();
            TodoHelper.Usergroup = user.getUsergroup();
            TodoHelper.UserLogo = user.getImgpath();
            TodoHelper.PassWord = user.getPassword();

            return user;
        } else
            return null;
    }

    /**
     * 保存当前登录的用户信息
     *
     * @return
     */
    public static void setCurrentUserInfo(Context context, String username, String userId,
                                          String usergroup, String userLogo, String passWord) {
        SharedPreferences.Editor editor = context.getSharedPreferences("data", MODE_PRIVATE).edit();

        editor.putString("username", username);
        editor.putString("userId", userId);
        editor.putString("usergroup", usergroup);
        editor.putString("userLogo", userLogo);
        editor.putString("password", passWord);
        editor.commit();

        TodoHelper.UserName = username;
        TodoHelper.UserId = userId;
        TodoHelper.Usergroup = usergroup;
        TodoHelper.UserLogo = userLogo;
        TodoHelper.PassWord = passWord;
    }

    /**
     * 检查是否联网
     *
     * @return
     */
    public static boolean getNetWorkState(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null) {
                boolean j = networkInfo.isAvailable();
                if (j) return true;
                return false;
            }
        }
        return false;
    }
}