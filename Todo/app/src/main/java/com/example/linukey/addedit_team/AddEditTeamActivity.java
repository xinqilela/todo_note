package com.example.linukey.addedit_team;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.linukey.data.model.local.Team;
import com.example.linukey.todo.R;
import com.example.linukey.util.BaseActivity;
import com.example.linukey.util.TodoHelper;

import java.text.ParseException;
import java.util.UUID;

/**
 * Created by linukey on 12/9/16.
 */

public class AddEditTeamActivity extends BaseActivity implements AddEditTeamContract.AddEditTeamView{

    AddEditTeamContract.AddEditTeamPresenter presenter = null;
    ViewHolder viewHolder = null;
    boolean isEdit = false;
    Team preTeam = null;

    class ViewHolder{
        EditText title;
        EditText content;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addteam);

        init();
        initActionBar();
    }

    private void initActionBar(){
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setTitle("");
    }


    public void init(){
        presenter = new AddEditTeamPresenter(this);
        initViewHolder();
        Intent edit = getIntent();
        Bundle bundle = edit.getBundleExtra("bundle");
        if (bundle != null) {
            Team team = (Team) bundle.getSerializable("date");
            initEdit(team);
            preTeam = team;
            isEdit = true;
        }
    }

    @Override
    public void initEdit(Team team){
        viewHolder.title.setText(team.getTeamname());
        viewHolder.content.setText(team.getContent());
    }

    public void initViewHolder(){
        viewHolder = new ViewHolder();
        viewHolder.title = (EditText)findViewById(R.id.title);
        viewHolder.content = (EditText)findViewById(R.id.content);
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
            return presenter.MenuChoice(item, this);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    @Override
    public void showMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean checkInput() {
        if(viewHolder.title.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "请输入小组名称!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void viewFinish(){
        finish();
    }

    @Override
    public boolean saveTeam() {
        String title = viewHolder.title.getText().toString().trim();
        String content = viewHolder.content.getText().toString().trim();
        String leaderId = TodoHelper.UserId;
        String teamId = UUID.randomUUID().toString();
        String isdelete = "0";
        String teamName = TodoHelper.UserName;
        int id = -1;
        if(isEdit){
            teamId = preTeam.getTeamId();
            id = preTeam.getId();
        }
        Team team = new Team(id, title, content, leaderId, teamId, isdelete, teamName);

        return presenter.saveTeam(team, this, isEdit);
    }
}
