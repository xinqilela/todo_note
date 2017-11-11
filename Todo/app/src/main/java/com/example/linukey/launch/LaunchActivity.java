package com.example.linukey.launch;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.linukey.data.model.local.User;
import com.example.linukey.login.LoginActivity;
import com.example.linukey.todo.MainActivity;
import com.example.linukey.todo.R;
import com.example.linukey.util.BaseActivity;
import com.example.linukey.util.TodoHelper;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.util.List;

/**
 * Created by linukey on 12/2/16.
 */

public class LaunchActivity extends BaseActivity implements LaunchContract.LaunchView{
    LaunchContract.LaunchPresenter presenter = null;
    private final String TAG = this.getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        init();
    }

    public void init(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // 用户拒绝过这个权限了，应该提示用户，为什么需要这个权限。
                Toast.makeText(this, "您已经拒绝过此权限!", Toast.LENGTH_SHORT).show();
            } else {
                // 申请授权。
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
            }
        }else{
            presenter = new LaunchPresenter(this);
            User user = TodoHelper.getCurrentUserInfo(this);
            Log.i(TAG, "检查本地是否有默认登录账号，以及是否有网络连接！");
            if(user != null && !user.getUsername().isEmpty() && TodoHelper.getNetWorkState(this)) {
                //初始化本地数据库数据
                presenter.initLocalData(this);
            }else{
                Log.i(TAG, "本地没有默认登录账号或者网络未连接!");
            }
            presenter.initLaunchPic(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 100: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    presenter = new LaunchPresenter(this);
                    User user = TodoHelper.getCurrentUserInfo(this);
                    Log.i(TAG, "检查本地是否有默认登录账号，以及是否有网络连接！");
                    if(user != null && !user.getUsername().isEmpty() && TodoHelper.getNetWorkState(this)) {
                        //初始化本地数据库数据
                        presenter.initLocalData(this);
                    }else{
                        Log.i(TAG, "本地没有默认登录账号或者网络未连接!");
                    }
                    presenter.initLaunchPic(this);

                } else {
                    finish();
                }
                return;
            }
        }
    }

    @Override
    public void showActivity(Intent intent){
        startActivity(intent);
        this.finish();
    }
}
