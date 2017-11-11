package com.example.linukey.system_setting;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.linukey.addedit_userinfo.AddEditUserInfoActivity;
import com.example.linukey.data.model.local.Project;
import com.example.linukey.data.source.local.DBHelper;
import com.example.linukey.data.source.local.ProjectContentProvider;
import com.example.linukey.data.source.local.UserContentProvider;
import com.example.linukey.login.LoginActivity;
import com.example.linukey.todo.IMainContract;
import com.example.linukey.todo.R;
import com.example.linukey.util.BaseActivity;
import com.example.linukey.util.TodoHelper;

/**
 * Created by linukey on 12/23/16.
 */

public class SystemSettingActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_systemsetting);
        init();
    }

    /**
     * 初始化标题栏
     */
    public void initActionBar(){
        ActionBar actionBar = getActionBar();
        actionBar.setTitle("设置");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        int titleId = Resources.getSystem().getIdentifier("action_bar_title",
                "id", "android");
        TextView textView = (TextView)findViewById(titleId);
        textView.setTextColor(Color.parseColor("#F2F1F0"));
        textView.setTextSize(16);
    }

    public void init(){
        initActionBar();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return true;
    }

    /**
     * 用户个人信息修改
     * @param view
     */
    public void btnUserInfo(View view){
        Intent intent = new Intent(SystemSettingActivity.this, AddEditUserInfoActivity.class);
        startActivity(intent);
    }

    /**
     * 用户退出登录
     * @param view
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void UserExit(View view){
        //删除首选项里面的内容
        try {
            SharedPreferences preferences = TodoHelper.getInstance().getSharedPreferences("data", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.commit();
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        //删除所有数据库中的信息
        DBHelper.dropAllTables();
        //启动登录Activity
        Intent intent = new Intent(SystemSettingActivity.this, LoginActivity.class);
        startActivity(intent);
        //结束所有活动
        BaseActivity.FinishAllActivities();
    }
}
