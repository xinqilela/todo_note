package com.example.linukey.addedit_userinfo;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.example.linukey.data.model.local.User;
import com.example.linukey.data.source.remote.RemoteObserver;
import com.example.linukey.data.source.remote.RemoteObserverEvent;
import com.example.linukey.data.source.remote.Remote_User;
import com.example.linukey.util.TodoHelper;
import com.jph.takephoto.app.TakePhotoActivity;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by linukey on 12/23/16.
 */

public class AddEditUserInfoPresenter implements AddEditUserInfoContract.AddEditUserInfoPresenter,
        RemoteObserver{
    AddEditUserInfoContract.AddEditUserInfoView view = null;
    private final String TAG = this.getClass().getName();
    private Uri photoUri;
    public static final int PIC_FROM_CAMERA = 1;
    public static final int PIC_SET = 0;
    public static final int PIC_LOCAL = 2;

    public AddEditUserInfoPresenter(AddEditUserInfoContract.AddEditUserInfoView view) {
        this.view = view;
    }

    public AddEditUserInfoPresenter(){}

    /**
     * 初始化用户信息控件
     */
    @Override
    public User initControlDate(){
        return User.getCurrentUserInfo(TodoHelper.UserId, TodoHelper.getInstance());
    }

    /**
     * 保存用户信息
     * @param user
     * @param context
     */
    @Override
    public void saveUserInfo(User user, Context context){
        //用户信息修改后应该现在服务器上进行修改，因为可能修改的用户名已经存在
        new Remote_User().updateUserInfo(user, this);
        Log.i(TAG, "准备在web端保存修改的用户信息!");
    }

    @Override
    public void Exeute(RemoteObserverEvent remoteObserverEvent) {
        if(remoteObserverEvent.clientResult.isResult()){
            User user = (User)remoteObserverEvent.object;
            user.setIsAlter("0");
            if(User.updateUserInfo(user, TodoHelper.getInstance())){
                new TodoHelper().setCurrentUserInfo(view.getContext(), user.getUsername(), user.getUserId(),
                        user.getUsergroup(), user.getImgpath(), user.getPassword());
                Toast.makeText(TodoHelper.getInstance(), "保存成功!", Toast.LENGTH_SHORT).show();
            }else{
                Log.e(TAG, "用户信息在web端修改成功后，在本地修改isAlter字段失败!");
            }
            return;
        }
        view.showMessage(remoteObserverEvent.clientResult.getMessage());
        Log.e(TAG, "在web端保存修改的用户信息失败!" +
        remoteObserverEvent.clientResult.getMessage());
    }

    /**
     * 将最终处理好的用户头像保存
     * @param uri
     */
    @Override
    public void saveUserLogo(Uri uri, Context context){
        User user = User.getCurrentUserInfo(TodoHelper.UserId, context);
        user.setImgpath(uri.getPath());
        User.updateUserInfo(user, context);
        TodoHelper.setCurrentUserInfo(context, user.getUsername(), user.getUserId(),
                user.getUsergroup(), user.getImgpath(), user.getPassword());
    }
}
