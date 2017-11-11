package com.example.linukey.login;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.linukey.data.model.local.User;
import com.example.linukey.data.model.remote.WebUser;
import com.example.linukey.data.source.remote.RemoteObserver;
import com.example.linukey.data.source.remote.RemoteObserverEvent;
import com.example.linukey.data.source.remote.Remote_User;
import com.example.linukey.launch.LaunchPresenter;
import com.example.linukey.todo.MainActivity;
import com.example.linukey.util.TodoHelper;

import java.util.List;
import java.util.Map;

/**
 * Created by linukey on 12/20/16.
 */

public class LoginPresenter implements LoginContract.LoginPresenter, RemoteObserver {
    LoginContract.LoginView view = null;
    private final String TAG = this.getClass().getName();

    public LoginPresenter(LoginContract.LoginView view) {
        this.view = view;
    }

    @Override
    public void checkLoginInfo(String username, String password, Context context) {
        new Remote_User().checkUserInfo(username, password, this);
    }

    @Override
    public void Exeute(RemoteObserverEvent remoteObserverEvent) {
        if (remoteObserverEvent.clientResult.isResult()) {

            Log.i(TAG, "在本地数据库添加登录用户信息!");
            //在本地记录用户登录信息
            WebUser webUser = (WebUser) remoteObserverEvent.object;
            String isAlter = "0";
            User user = new User(webUser);
            user.setIsAlter(isAlter);
            if(User.saveUserInfo(user, TodoHelper.getInstance())){
                Log.i(TAG, "用户信息添加成功:" + user.toString());
            }

            Log.i(TAG, "将用户信息加入到本地SharedPreference!");
            //在临时文件中记录用户登录信息，以备下次登录查看
            new TodoHelper().setCurrentUserInfo(view.getContext(), user.getUsername(),
                    user.getUserId(), user.getUsergroup(), user.getImgpath(), user.getPassword());

            //初始化本地数据
            if(TodoHelper.getNetWorkState(view.getContext())) {
                new LaunchPresenter(null).initLocalData(TodoHelper.getInstance());
            }

            //进入APP主界面
            Intent intent = new Intent(TodoHelper.getInstance(), MainActivity.class);
            view.showMainActivity(intent);
        } else {
            view.showMessage(remoteObserverEvent.clientResult.getMessage());
        }
    }
}
