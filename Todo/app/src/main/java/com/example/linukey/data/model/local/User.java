package com.example.linukey.data.model.local;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.WindowId;
import android.widget.Toast;

import com.example.linukey.data.model.remote.WebSight;
import com.example.linukey.data.model.remote.WebUser;
import com.example.linukey.data.source.local.DBHelper;
import com.example.linukey.data.source.local.UserContentProvider;
import com.example.linukey.util.TodoHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by linukey on 11/29/16.
 */

public class User implements Serializable {
    private int id;
    private String username;
    private String password;
    private String email;
    private String phonenumber;
    private String usergroup;
    private String userId;
    private String isAlter;
    private String webId;

    public User(WebUser webUser) {
        this.id = webUser.getId();
        this.username = webUser.getUsername();
        this.password = webUser.getPassword();
        this.email = webUser.getEmail();
        this.phonenumber = webUser.getPhonenumber();
        this.usergroup = webUser.getUsergroup();
        this.userId = webUser.getUserId();
        this.webId = webUser.getId() + "";
    }

    public User(String username, String password, String email, String phonenumber,
                String usergroup, String userId, String imgpath, String isAlter, String webId) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phonenumber = phonenumber;
        this.usergroup = usergroup;
        this.userId = userId;
        this.imgpath = imgpath;
        this.isAlter = isAlter;
        this.webId = webId;
    }

    public User(int id, String username, String password, String email, String phonenumber,
                String usergroup, String userId, String imgpath, String isAlter, String webId) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phonenumber = phonenumber;
        this.usergroup = usergroup;
        this.userId = userId;
        this.imgpath = imgpath;
        this.id = id;
        this.isAlter = isAlter;
        this.webId = webId;
    }

    public User() {
    }

    public String getWebId() {
        return webId;
    }

    public void setWebId(String webId) {
        this.webId = webId;
    }

    public String getIsAlter() {
        return isAlter;
    }

    public void setIsAlter(String isAlter) {
        this.isAlter = isAlter;
    }

    private String imgpath;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImgpath() {
        return imgpath;
    }

    public void setImgpath(String imgpath) {
        this.imgpath = imgpath;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getUsergroup() {
        return usergroup;
    }

    public void setUsergroup(String usergroup) {
        this.usergroup = usergroup;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public static boolean checkUserRepeat(String username) {
        List<User> users = getUsers(TodoHelper.getInstance());
        for (User u : users) {
            if (u.getUsername().equals(username)) {
                Toast.makeText(TodoHelper.getInstance(), "该用户名已存在!", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    /**
     * 保存用户信息
     *
     * @param user
     * @param context
     * @return
     */
    public static boolean saveUserInfo(User user, Context context) {
        ContentValues newValues = new ContentValues();
        newValues.put(UserContentProvider.key_username, user.getUsername());
        newValues.put(UserContentProvider.key_email, user.getEmail());
        newValues.put(UserContentProvider.key_password, user.getPassword());
        newValues.put(UserContentProvider.key_phonenumber, user.getPhonenumber());
        newValues.put(UserContentProvider.key_usergroup, user.getUsergroup());
        newValues.put(UserContentProvider.key_userId, user.getUserId());
        newValues.put(UserContentProvider.key_imgpath, user.getImgpath());
        newValues.put(UserContentProvider.key_isAlter, user.getIsAlter());
        newValues.put(UserContentProvider.key_webId, user.getWebId());

        ContentResolver cr = context.getContentResolver();
        Uri myRowUri = cr.insert(DBHelper.ContentUri_user, newValues);

        if (myRowUri != null)
            return true;

        return false;
    }

    public static boolean updateUserInfo(User user, Context context) {
        ContentValues newValues = new ContentValues();
        newValues.put(UserContentProvider.key_username, user.getUsername());
        newValues.put(UserContentProvider.key_email, user.getEmail());
        newValues.put(UserContentProvider.key_password, user.getPassword());
        newValues.put(UserContentProvider.key_phonenumber, user.getPhonenumber());
        newValues.put(UserContentProvider.key_usergroup, user.getUsergroup());
        newValues.put(UserContentProvider.key_userId, user.getUserId());
        newValues.put(UserContentProvider.key_imgpath, user.getImgpath());
        newValues.put(UserContentProvider.key_isAlter, user.getIsAlter());
        newValues.put(UserContentProvider.key_webId, user.getWebId());

        String where = UserContentProvider.key_id + " = " + user.getId();
        String[] selectionArgs = null;

        ContentResolver cr = context.getContentResolver();
        int id = cr.update(DBHelper.ContentUri_user, newValues, where, selectionArgs);

        if (id > 0)
            return true;

        return false;
    }

    /**
     * 获取本地数据库中所有用户信息
     *
     * @param context
     * @return
     */
    public static List<User> getUsers(Context context) {
        ContentResolver cr = context.getContentResolver();

        String[] result_columns = UserContentProvider.keys;

        String where = null;

        String selectionArgs[] = null;
        String order = null;

        Cursor resultCursor = cr.query(DBHelper.ContentUri_user,
                result_columns, where, selectionArgs, order);

        List<User> users = null;
        if (resultCursor != null && resultCursor.getCount() > 0) {
            users = new ArrayList<>();
            while (resultCursor.moveToNext()) {
                User user = new User(
                        resultCursor.getInt(0),
                        resultCursor.getString(1),
                        resultCursor.getString(2),
                        resultCursor.getString(3),
                        resultCursor.getString(4),
                        resultCursor.getString(5),
                        resultCursor.getString(6),
                        resultCursor.getString(7),
                        resultCursor.getString(8),
                        resultCursor.getString(9)
                );
                users.add(user);
            }
            resultCursor.close();
        }
        return users;
    }

    /**
     * 获取当前登录信息
     *
     * @param context
     * @return
     */
    public static User getCurrentUserInfo(String userId, Context context) {
        ContentResolver cr = context.getContentResolver();

        String[] result_columns = UserContentProvider.keys;

        String where = UserContentProvider.key_userId + " = '" + userId + "'";

        String[] selectionArgs = null;
        String order = null;

        Cursor resultCursor = cr.query(DBHelper.ContentUri_user,
                result_columns, where, selectionArgs, order);

        User user = null;
        if (resultCursor != null && resultCursor.getCount() == 1) {
            while (resultCursor.moveToNext()) {
                user = new User();
                user.setId(resultCursor.getInt(0));
                user.setUsername(resultCursor.getString(1));
                user.setPassword(resultCursor.getString(2));
                user.setEmail(resultCursor.getString(3));
                user.setPhonenumber(resultCursor.getString(4));
                user.setUsergroup(resultCursor.getString(5));
                user.setUserId(resultCursor.getString(6));
                user.setImgpath(resultCursor.getString(7));
                user.setIsAlter(resultCursor.getString(8));
                user.setWebId(resultCursor.getString(9));
                return user;
            }
        }
        return user;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", phonenumber='" + phonenumber + '\'' +
                ", usergroup='" + usergroup + '\'' +
                ", userId='" + userId + '\'' +
                ", isAlter='" + isAlter + '\'' +
                ", webId='" + webId + '\'' +
                ", imgpath='" + imgpath + '\'' +
                '}';
    }
}