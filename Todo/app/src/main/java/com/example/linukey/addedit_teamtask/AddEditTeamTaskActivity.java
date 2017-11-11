package com.example.linukey.addedit_teamtask;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.linukey.data.model.local.TeamTask;
import com.example.linukey.todo.R;
import com.example.linukey.util.BaseActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by linukey on 12/9/16.
 */

public class AddEditTeamTaskActivity extends BaseActivity implements AddEditTeamTaskContract.AddEditTeamTaskView {
    AddEditTeamTaskContract.AddEditTeamTaskPresenter presenter = null;
    ViewHolder viewHolder = null;
    TeamTask preTeamTask = null;
    boolean isEdit = false;

    class ViewHolder {
        EditText title;
        EditText content;
        TextView starttime;
        TextView endtime;
        Spinner projects;
        Spinner teamnames;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addteamtask);

        init();
    }


    @Override
    public void setSpinerTeamsAdapter(List<String> teamNames){
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, teamNames);
        viewHolder.teamnames.setAdapter(arrayAdapter);
    }

    @Override
    public void setSpinerProjectsAdapter(List<String> projectTitles){
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, projectTitles);
        viewHolder.projects.setAdapter(arrayAdapter);
    }

    private void initActionBar(){
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setTitle("团队任务");
    }

    public void init() {
        initActionBar();
        presenter = new AddEditTeamTaskPresenter(this);
        initViewHolder();
        initControl();
        Intent edit = getIntent();
        Bundle bundle = edit.getBundleExtra("bundle");
        if (bundle != null) {
            TeamTask teamTask = (TeamTask) bundle.getSerializable("date");
            initEdit(teamTask);
            preTeamTask = teamTask;
            isEdit = true;
        }
    }

    @Override
    public void initEdit(TeamTask teamTask) {
        viewHolder.title.setText(teamTask.getTitle());
        viewHolder.content.setText(teamTask.getContent());
        viewHolder.starttime.setText(teamTask.getStarttime());
        viewHolder.endtime.setText(teamTask.getEndtime());

        if(teamTask.getProjectId() != null){
            viewHolder.projects.setSelection(presenter.getSpinerProjectsSelection(teamTask));
        }
        if(teamTask.getTeamId() != null){
            viewHolder.teamnames.setSelection(presenter.getSpinerTeamsSelection(teamTask));
        }
    }

    public void initViewHolder() {
        viewHolder = new ViewHolder();
        viewHolder.title = (EditText) findViewById(R.id.title);
        viewHolder.content = (EditText) findViewById(R.id.content);
        viewHolder.starttime = (TextView) findViewById(R.id.starttime);
        viewHolder.endtime = (TextView) findViewById(R.id.endtime);
        viewHolder.projects = (Spinner) findViewById(R.id.projects);
        viewHolder.teamnames = (Spinner) findViewById(R.id.teamnames);
    }

    public void onClick_dateSelect(View view) {
        presenter.getDateSeleteDialog(view, this).show();
    }

    @Override
    public void initControl() {
        Date date = new Date();
        String today = date.getYear() + 1900 + "-" + (date.getMonth() + 1) + "-" + date.getDate();

        TextView starttime = (TextView) findViewById(R.id.starttime);
        starttime.setText(today);

        TextView endtime = (TextView) findViewById(R.id.endtime);
        endtime.setText(today);

        //初始化小组选择栏
        presenter.initTeamSpiner();
        //初始化项目选择栏
        presenter.initProjectSpiner();
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
    public boolean checkInput() throws ParseException {
        if (viewHolder.title.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "请输入任务标题!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (new SimpleDateFormat("yyyy-MM-dd")
                    .parse(viewHolder.starttime.getText().toString()).getTime() >
                    new SimpleDateFormat("yyyy-MM-dd")
                            .parse(viewHolder.endtime.getText().toString()).getTime()) {
                Toast.makeText(this, "请输入有效时间范围!", Toast.LENGTH_LONG).show();
                return false;
            //必须选择小组，而且是自己创建的小组，因为小组任务的一切权利在组长
        }else if(viewHolder.teamnames.getSelectedItem() == null ||
                viewHolder.teamnames.getSelectedItem().toString().isEmpty()){
            Toast.makeText(this, "请选择任务所属小组!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    public void viewFinish(){
        finish();
    }

    @Override
    public boolean saveTask() {
        String title = viewHolder.title.getText().toString().trim();
        String content = viewHolder.content.getText().toString().trim();
        String starttime = viewHolder.starttime.getText().toString().trim();
        String endtime = viewHolder.endtime.getText().toString().trim();
        String clocktime = "0:0";
        String projectId = null;
        String teamId = null;
        String isdelete = "0";
        String selfId = UUID.randomUUID().toString();
        int id = -1;
        if(isEdit){
            projectId = preTeamTask.getProjectId();
            teamId = preTeamTask.getTeamId();
            id = preTeamTask.getId();
            selfId = preTeamTask.getSelfId();
        }

        if(viewHolder.projects.getSelectedItem() != null
                && !viewHolder.projects.getSelectedItem().toString().isEmpty()){
            projectId = presenter.getSelectedProjectId((int)viewHolder.projects.getSelectedItemId()-1);
        }
        if(viewHolder.teamnames.getSelectedItem() != null
                && !viewHolder.teamnames.getSelectedItem().toString().isEmpty()){
            teamId = presenter.getSelectedTeamId((int)viewHolder.teamnames.getSelectedItemId()-1);
        }

        return presenter.saveTask(isEdit, id, title, content,
                starttime, endtime, clocktime, projectId, teamId, isdelete, selfId);
    }
}
