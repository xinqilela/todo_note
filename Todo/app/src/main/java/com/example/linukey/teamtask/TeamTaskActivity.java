package com.example.linukey.teamtask;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.linukey.data.model.local.Project;
import com.example.linukey.data.model.local.Team;
import com.example.linukey.data.model.local.TeamTask;
import com.example.linukey.todo.Adapter.ListViewTeamTaskAdapter;
import com.example.linukey.todo.R;
import com.example.linukey.todo.SwipeMenu.SwipeMenu;
import com.example.linukey.todo.SwipeMenu.SwipeMenuListView;
import com.example.linukey.util.BaseActivity;
import com.example.linukey.util.TodoHelper;

import java.util.List;

/**
 * Created by linukey on 12/5/16.
 */

public class TeamTaskActivity extends BaseActivity implements TeamTaskContract.TeamTaskActivityView {

    SwipeMenuListView listViewTask = null;
    final int ResultCode_addTeamTask = 1;
    TeamTaskContract.TeamTaskActivityPresenter presenter;
    String menuName = null;
    String projectId = null;
    String teamId = null;
    String actionBarTitle = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teamtask);

        init();
        initActionBar();
    }

    @Override
    public void viewFinish(){
        finish();
    }

    public void init() {
        presenter = new TeamTaskPresenter(this);
        listViewTask = (SwipeMenuListView) findViewById(R.id.listview_teamtask);
        Intent intent = getIntent();
        menuName = intent.getStringExtra("menuname");
        if(menuName.equals("teamProject")) {
            projectId = intent.getStringExtra("projectId");
            actionBarTitle = intent.getStringExtra("actionBarTitle");
        }
        if(menuName.equals("teamName")) {
            teamId = intent.getStringExtra("teamId");
            actionBarTitle = intent.getStringExtra("actionBarTitle");
        }
        listViewTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final View dialogView = LayoutInflater.from(TeamTaskActivity.this).inflate(R.layout.dialog_teamtask, null);
                final AlertDialog.Builder ad = new AlertDialog.Builder(TeamTaskActivity.this);
                TeamTask teamTask = presenter.getTeamTaskByPosition(position);
                ((TextView)dialogView.findViewById(R.id.title)).setText(teamTask.getTitle());
                ((TextView)dialogView.findViewById(R.id.content)).setText(teamTask.getContent());
                ((TextView)dialogView.findViewById(R.id.starttime)).setText(teamTask.getStarttime());
                ((TextView)dialogView.findViewById(R.id.endtime)).setText(teamTask.getEndtime());
                if(teamTask.getProjectId() != null && !teamTask.getProjectId().isEmpty())
                    ((TextView)dialogView.findViewById(R.id.project)).setText(Project.getProjectByProjectId(teamTask.getProjectId(), TodoHelper.getInstance()).getTitle());
                ((TextView)dialogView.findViewById(R.id.teamName)).setText(Team.getTeamByTeamId(teamTask.getTeamId(), TeamTaskActivity.this).getTeamname());
                ad.setView(dialogView);
                ad.create();
                ad.show();
            }
        });
        listViewTask.setMenuCreator(presenter.getSwipeMenuCreator());
        listViewTask.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position, SwipeMenu menu, int index) {
                if(presenter.isValidUser(position)) {
                    switch (index) {
                        case 0:
                            presenter.editTask(position, TeamTaskActivity.this);
                            break;
                        case 1:
                            presenter.deleteTask(position, menuName, TeamTaskActivity.this);
                            presenter.initData(menuName, projectId, teamId, TeamTaskActivity.this);
                            break;
                        case 2:
                            presenter.completedTask(position, menuName, TeamTaskActivity.this);
                            presenter.initData(menuName, projectId, teamId, TeamTaskActivity.this);
                            break;
                        default:
                            break;
                    }
                }else{
                    Toast.makeText(TeamTaskActivity.this, "团队任务只能由该组组长编辑!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        presenter.initData(menuName, projectId, teamId, this);
    }

    public void initActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        switch (menuName) {
            case "today":
                actionBar.setTitle("今日待办");
                break;
            case "tomorrow":
                actionBar.setTitle("明日待办");
                break;
            case "next":
                actionBar.setTitle("下一步行动");
                break;
            case "teamProject":
                actionBar.setTitle(actionBarTitle);
                break;
            case "teamName":
                actionBar.setTitle(actionBarTitle);
                break;
            default:
                break;
        }
        int titleId = Resources.getSystem().getIdentifier("action_bar_title",
                "id", "android");
        TextView textView = (TextView)findViewById(titleId);
        textView.setTextColor(Color.parseColor("#F2F1F0"));
        textView.setTextSize(16);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == ResultCode_addTeamTask) {
            presenter.initData(menuName, projectId, teamId, this);
        }
    }

    @Override
    public void showAddTeamTask(Intent intent) {
        startActivityForResult(intent, ResultCode_addTeamTask);
    }

    @Override
    public void showAfterDataSourceChanged(List<TeamTask> teamTasks) {
        ListViewTeamTaskAdapter lva = new ListViewTeamTaskAdapter(this, teamTasks);
        listViewTask.setAdapter(lva);
    }

    @Override
    public void showEditTask(Intent intent) {
        startActivityForResult(intent, ResultCode_addTeamTask);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        presenter.CreateMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case 0:
                presenter.addTeamTask(this);
                break;
        }
        return true;
    }
}
