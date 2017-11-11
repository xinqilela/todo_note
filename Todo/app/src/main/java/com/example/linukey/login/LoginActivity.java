package com.example.linukey.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.linukey.register.RegisterActivity;
import com.example.linukey.todo.R;
import com.example.linukey.util.BaseActivity;
import com.example.linukey.util.TodoHelper;

/**
 * Created by linukey on 11/24/16.
 */

public class LoginActivity extends BaseActivity implements LoginContract.LoginView {

    final int register_ResultCode = 1;
    UserInputModel userInputModel;
    LoginContract.LoginPresenter presenter = null;
    long firstTime = 0;

    class UserInputModel {
        EditText username;
        EditText password;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    public void init() {
        presenter = new LoginPresenter(this);
        initUserInputModel();
    }

    public void initUserInputModel() {
        userInputModel = new UserInputModel();
        userInputModel.username = (EditText) findViewById(R.id.username);
        userInputModel.password = (EditText) findViewById(R.id.pwd);
    }

    /**
     * 注册
     * @param view
     */
    public void onClick_register(View view) {
        if (TodoHelper.getNetWorkState(this)) {
            Intent register = new Intent(this, RegisterActivity.class);
            startActivityForResult(register, register_ResultCode);
        }else{
            Toast.makeText(this, "没有检测到网络连接!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 忘记密码
     * @param view
     */
    public void onClick_reSetPass(View view) {
        Toast.makeText(this, "本功能等待开发!", Toast.LENGTH_LONG).show();
    }

    /**
     * 提交登录信息
     * @param view
     */
    public void onClick_submit(View view) {
        if (checkInput()) {
            presenter.checkLoginInfo(userInputModel.username.getText().toString().trim(),
                    userInputModel.password.getText().toString().trim(), this);
        }
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                long secondTime = System.currentTimeMillis();
                if (secondTime - firstTime > 2000) {
                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    firstTime = secondTime;//更新firstTime
                    return true;
                } else {
                    this.finish();
                    System.exit(0);
                }
                break;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void showMainActivity(Intent intent) {
        startActivity(intent);
        this.finish();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public boolean checkInput() {
        if (userInputModel.username.getText().toString().isEmpty()) {
            Toast.makeText(this, "请输入用户名!", Toast.LENGTH_LONG).show();
            return false;
        } else if (userInputModel.password.getText().toString().isEmpty()) {
            Toast.makeText(this, "请输入密码!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}