package com.example.linukey.team;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.linukey.addedit_team.AddEditTeamActivity;
import com.example.linukey.data.model.local.Team;
import com.example.linukey.data.model.local.TeamTask;
import com.example.linukey.data.model.remote.WebTeam;
import com.example.linukey.data.source.remote.RemoteObserver;
import com.example.linukey.data.source.remote.RemoteObserverEvent;
import com.example.linukey.data.source.remote.Remote_Team;
import com.example.linukey.data.source.remote.Remote_User;
import com.example.linukey.teamsearch.TeamSearchActivity;
import com.example.linukey.teamtask.TeamTaskActivity;
import com.example.linukey.todo.Adapter.ListViewTeamAdapter;
import com.example.linukey.todo.R;
import com.example.linukey.todo.SwipeMenu.SwipeMenu;
import com.example.linukey.todo.SwipeMenu.SwipeMenuListView;
import com.example.linukey.util.BaseActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by linukey on 12/5/16.
 */

public class TeamActivity extends BaseActivity implements TeamContract.TeamPTView, RemoteObserver {

    TeamContract.TeamPTPresenter presenter = null;
    SwipeMenuListView listViewTeam = null;
    final int ResultCode_addTeam = 1;
    Team team;
    String menuname = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);
        init();
        initActionBar();
    }

    @Override
    public void showTeamDialogFragment(int position){
        Team team = presenter.getCurrentTeam(position);
        this.team = team;
        new Remote_User().getUserNameByUserID(team.getLeaderId(), this);
    }

    @Override
    public void Exeute(RemoteObserverEvent remoteObserverEvent) {
        if(remoteObserverEvent.object.toString().equals("getUserNameByUserId")){
            String username = null;
            if(remoteObserverEvent.clientResult.isResult())
                username = remoteObserverEvent.clientResult.getObject().toString();
            else
                username = remoteObserverEvent.clientResult.getMessage();
            final View dialogView = LayoutInflater.from(TeamActivity.this).inflate(R.layout.dialog_team, null);
            final AlertDialog.Builder ad = new AlertDialog.Builder(TeamActivity.this);
            ((TextView)dialogView.findViewById(R.id.title)).setText(team.getTeamname());
            ((TextView)dialogView.findViewById(R.id.content)).setText(team.getContent());
            ((TextView)dialogView.findViewById(R.id.leader)).setText(username);
            ad.setView(dialogView);
            ad.create();
            ad.show();
        }
    }

    public void init(){
        presenter = new TeamPresenter(this);
        Intent intent = getIntent();
        if(intent != null){
            menuname = intent.getStringExtra("menuname");
        }
        listViewTeam = (SwipeMenuListView)findViewById(R.id.listview_team);
        listViewTeam.setMenuCreator(presenter.getSwipeMenuCreator());
        listViewTeam.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(menuname.equals("teamName")){
                    Intent intent = new Intent(TeamActivity.this, TeamTaskActivity.class);
                    intent.putExtra("menuname", menuname);
                    intent.putExtra("teamId", presenter.getCurrentTeam(position).getTeamId());
                    intent.putExtra("actionBarTitle", presenter.getCurrentTeam(position).getTeamname());
                    startActivity(intent);
                }else {
                    showTeamDialogFragment(position);
                }
            }
        });
        listViewTeam.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index){
                    case 0:
                        presenter.editTeam(position, TeamActivity.this);
                        break;
                    case 1:
                        presenter.deleteOne(position, TeamActivity.this);
                        break;
                    default:
                        break;
                }
            }
        });

        presenter.initData(menuname, this);
    }

    public void initActionBar(){
        ActionBar actionBar = getActionBar();
        actionBar.setTitle("小组");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        int titleId = Resources.getSystem().getIdentifier("action_bar_title",
                "id", "android");
        TextView textView = (TextView)findViewById(titleId);
        textView.setTextColor(Color.parseColor("#F2F1F0"));
    }

    @Override
    public void showAddEditTeamActivity() {
        Intent intent = new Intent(this, AddEditTeamActivity.class);
        startActivityForResult(intent, ResultCode_addTeam);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == ResultCode_addTeam) {
            presenter.initData(menuname, this);
        }
    }

    @Override
    public void showEditTeam(Intent intent) {
        startActivityForResult(intent, ResultCode_addTeam);
    }

    @Override
    public void showDataSourceChangedView(List<Team> datasourceTeams) {
        ListViewTeamAdapter listViewTeamAdapter = new ListViewTeamAdapter(this, datasourceTeams);
        listViewTeam.setAdapter(listViewTeamAdapter);
    }

    @Override
    public void viewFinish(){
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        MenuItem teamJoin = menu.add(0,0,0, "搜索小组");
        teamJoin.setIcon(R.mipmap.search);
        teamJoin.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        MenuItem teamAdd = menu.add(1,1,0, "创建小组");
        teamAdd.setTitle("创建小组");
        teamAdd.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            case 1:
                showAddEditTeamActivity();
                break;
            case 0:
                showTeamSearchActivity();
                break;
        }
        return true;
    }

    @Override
    public void showTeamSearchActivity() {
        Intent intent = new Intent(this, TeamSearchActivity.class);
        startActivity(intent);
    }
}


















