package com.example.linukey.addedit_selfpgs;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.linukey.todo.R;
import com.example.linukey.util.BaseActivity;
import com.example.linukey.util.TodoHelper;
import com.example.linukey.data.model.local.TaskClassify;

import java.text.ParseException;
import java.util.UUID;

/**
 * Created by linukey on 12/3/16.
 */

public class AddEditSelfpgsActivity extends BaseActivity implements AddEditSelfpgsContract.AddEditSelfpgsView {
    private boolean isEdit = false;
    private String menuName = null;
    private TaskClassify preEntity = null;
    AddEditSelfpgsContract.AddEditSelfpgsPresenter presenter = null;
    private final String TAG = this.getClass().getName();

    class ViewHolder {
        TextView title;
        TextView content;
    }

    ViewHolder viewHolder;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addselfpgs);
        init();
        initActionBar();
    }

    private void initActionBar(){
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        if(menuName.equals("project")) actionBar.setTitle("项目");
        else if(menuName.equals("goal")) actionBar.setTitle("目标");
        else if(menuName.equals("sight")) actionBar.setTitle("情景");
    }

    private void init(){
        presenter = new AddEditSelfpgsPresenter(this);
        initViewHolder();
        initDate();
    }

    private void initDate() {
        Intent edit = getIntent();
        menuName = edit.getStringExtra("menuname");
        Bundle bundle = edit.getBundleExtra("bundle");
        if (bundle != null) {
            TaskClassify taskClassify = (TaskClassify) bundle.getSerializable("date");
            preEntity = taskClassify;
            initEdit(taskClassify);
            isEdit = true;
        }
    }

    @Override
    public void viewFinish(){
        finish();
    }

    @Override
    public void initEdit(TaskClassify taskClassify) {
        viewHolder.title.setText(taskClassify.getTitle());
        viewHolder.content.setText(taskClassify.getContent());
        Log.i(TAG, "初始化需要编辑的" + menuName + "数据! 值为:" + taskClassify.toString());
    }

    private void initViewHolder() {
        viewHolder = new ViewHolder();
        viewHolder.title = (TextView) findViewById(R.id.title);
        viewHolder.content = (TextView) findViewById(R.id.content);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        presenter.CreateMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            return presenter.MenuChoice(item, this, menuName);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean checkInput() {
        if (viewHolder.title.getText().toString().isEmpty()) {
            Toast.makeText(this, "请输入标题!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public boolean saveProject() {
        String title = viewHolder.title.getText().toString();
        String content = viewHolder.content.getText().toString();
        String userId = TodoHelper.UserId;

        //更新时无需变动的属性
        String selfId = UUID.randomUUID().toString();
        String state = TodoHelper.PGS_State.get("noComplete");
        int id = -1;
        String isdelete = "0";

        if(isEdit){
            id = preEntity.getId();
            state = preEntity.getState();
            selfId = preEntity.getSelfId();
            isdelete = preEntity.getIsdelete();
        }
        return presenter.saveProject(isEdit, menuName, this, title, content, selfId, state, userId,
                id, isdelete);
    }

    @Override
    public boolean saveGoal() {
        String title = viewHolder.title.getText().toString();
        String content = viewHolder.content.getText().toString();
        String userId = TodoHelper.UserId;

        //更新时无需变动的字段
        String selfId = UUID.randomUUID().toString();
        String state = TodoHelper.PGS_State.get("noComplete");
        int id = -1;
        String isdelete = "0";

        if(isEdit){
            id = preEntity.getId();
            state = preEntity.getState();
            selfId = preEntity.getSelfId();
            isdelete = preEntity.getIsdelete();
        }
        return presenter.saveGoal(isEdit, this, title, content, selfId, state,
                userId, id, isdelete);
    }

    @Override
    public boolean saveSight() {
        String title = viewHolder.title.getText().toString();
        String content = viewHolder.content.getText().toString();
        String userId = TodoHelper.UserId;

        //更新时不应该变动的属性
        String isdelete = "0";
        String selfId = UUID.randomUUID().toString();
        int id = -1;

        if(isEdit){
            id = preEntity.getId();
            selfId = preEntity.getSelfId();
            isdelete = preEntity.getIsdelete();
        }
        return presenter.saveSight(isEdit, this, title, content, selfId, userId,
                id, isdelete);
    }

    @Override
    public boolean savePGS(String menuName) {
        switch (menuName) {
            case "project":
            case "teamProject":
                return saveProject();
            case "goal":
                return saveGoal();
            case "sight":
                return saveSight();
            default:
                return false;
        }
    }
}