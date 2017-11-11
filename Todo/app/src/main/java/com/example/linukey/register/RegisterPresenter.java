package com.example.linukey.register;

import android.content.Context;
import android.util.Log;

import com.example.linukey.data.model.local.User;
import com.example.linukey.data.model.remote.WebUser;
import com.example.linukey.data.source.remote.ClientResult;
import com.example.linukey.data.source.remote.RemoteObserver;
import com.example.linukey.data.source.remote.RemoteObserverEvent;
import com.example.linukey.data.source.remote.Remote_User;
import com.example.linukey.util.TodoHelper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by linukey on 12/20/16.
 */

public class RegisterPresenter implements RegisterContract.RegisterPresenter, RemoteObserver {
    RegisterContract.RegisterView view = null;
    private final String TAG = this.getClass().getName();

    public RegisterPresenter(RegisterContract.RegisterView view) {
        this.view = view;
    }

    @Override
    public boolean isEmail(String strEmail) {
        String strPattern =
                "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(strEmail);
        return m.matches();
    }

    @Override
    public boolean isPhoneNumber(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        System.out.println(m.matches() + "---");
        return m.matches();
    }

    @Override
    public void saveUserInfo(User user, Context context) {
        Log.i(TAG, "准备在web端保存注册的用户信息!");
        new Remote_User().saveUserInfo(user, this);
    }

    @Override
    public void Exeute(RemoteObserverEvent remoteObserverEvent) {
        if (remoteObserverEvent.clientResult.isResult()) {
            User user = (User) remoteObserverEvent.object;
            user.setIsAlter("0");
            if(User.saveUserInfo(user, TodoHelper.getInstance())){
                view.executeResult(remoteObserverEvent.clientResult);}

            else
                Log.e(TAG, "服务器端保存用户信息成功后，本地端刷新isAlter字段失败!");
            return;
        }
        view.executeResult(remoteObserverEvent.clientResult);
        Log.e(TAG, "在web端保存注册的用户信息时出错!" +
        remoteObserverEvent.clientResult.getMessage());
    }
}