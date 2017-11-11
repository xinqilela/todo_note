package com.example.linukey.register;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.linukey.data.source.remote.ClientResult;
import com.example.linukey.todo.R;
import com.example.linukey.util.BaseActivity;
import com.example.linukey.util.TodoHelper;
import com.example.linukey.data.model.local.User;

import java.util.UUID;

/**
 * Created by linukey on 11/24/16.
 */

public class RegisterActivity extends BaseActivity implements RegisterContract.RegisterView {

    RegisterContract.RegisterPresenter presenter = null;
    UserInputModel userInputModel;

    class UserInputModel{
        EditText username;
        EditText email;
        EditText phonenumber;
        EditText password;
        EditText rePassword;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    public void init(){
        presenter = new RegisterPresenter(this);
        initUserInputModel();
    }

    public void initUserInputModel(){
        userInputModel = new UserInputModel();
        userInputModel.username = (EditText)findViewById(R.id.username);
        userInputModel.password = (EditText)findViewById(R.id.pwd);
        userInputModel.rePassword = (EditText)findViewById(R.id.repwd);
        userInputModel.email = (EditText)findViewById(R.id.email);
        userInputModel.phonenumber = (EditText)findViewById(R.id.phone);
    }

    public void onClick_submit(View view){
        if(checkInput()){
            saveUserInfo();
        }
    }

    @Override
    public void executeResult(ClientResult result){
        if(result.isResult()){
            Toast.makeText(this, "注册成功！", Toast.LENGTH_LONG).show();
            finish();
        }else{
            Toast.makeText(this, result.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean checkInput(){
        if(userInputModel.username.getText().toString().isEmpty()){
            Toast.makeText(this, "请输入用户名!", Toast.LENGTH_LONG).show();
            return false;
        }else if(userInputModel.email.getText().toString().isEmpty()){
            Toast.makeText(this, "请输入邮箱!", Toast.LENGTH_LONG).show();
            return false;
        }else if(!presenter.isEmail(userInputModel.email.getText().toString())){
            Toast.makeText(this, "请输入合法邮箱!", Toast.LENGTH_LONG).show();
            return false;
        }else if(userInputModel.phonenumber.getText().toString().isEmpty()){
            Toast.makeText(this, "请输入电话号码!", Toast.LENGTH_LONG).show();
            return false;
        }else if(!presenter.isPhoneNumber(userInputModel.phonenumber.getText().toString())){
            Toast.makeText(this, "请输入合法的电话号码!", Toast.LENGTH_SHORT).show();
            return false;
        } else if(userInputModel.password.getText().toString().isEmpty()){
            Toast.makeText(this, "请输入密码!", Toast.LENGTH_LONG).show();
            return false;
        }else if(userInputModel.rePassword.getText().toString().isEmpty()){
            Toast.makeText(this, "请输入验证密码!", Toast.LENGTH_LONG).show();
            return false;
        }else if(!userInputModel.password.getText().toString().equals(userInputModel.rePassword.getText().toString())){
            Toast.makeText(this, "两次输入的密码不一致!", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    @Override
    public void saveUserInfo(){
        User user = new User();
        user.setUsername(userInputModel.username.getText().toString());
        user.setEmail(userInputModel.email.getText().toString());
        user.setPassword(userInputModel.password.getText().toString());
        user.setPhonenumber(userInputModel.phonenumber.getText().toString());
        user.setUsergroup(TodoHelper.UserGroup.get("normal"));
        user.setUserId(UUID.randomUUID().toString());
        user.setIsAlter("1");
        user.setWebId("-1");
        user.setImgpath("");
        presenter.saveUserInfo(user, this);
    }
}
