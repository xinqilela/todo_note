package com.example.linukey.team;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Toast;

import com.example.linukey.addedit_team.AddEditTeamActivity;
import com.example.linukey.data.model.local.Team;
import com.example.linukey.data.model.local.TeamJoinInfo;
import com.example.linukey.data.model.remote.WebTeam;
import com.example.linukey.data.source.remote.RemoteObserver;
import com.example.linukey.data.source.remote.RemoteObserverEvent;
import com.example.linukey.data.source.remote.Remote_Notification;
import com.example.linukey.data.source.remote.Remote_Team;
import com.example.linukey.launch.LaunchPresenter;
import com.example.linukey.todo.SwipeMenu.SwipeMenu;
import com.example.linukey.todo.SwipeMenu.SwipeMenuCreator;
import com.example.linukey.todo.SwipeMenu.SwipeMenuItem;
import com.example.linukey.util.TodoHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by linukey on 12/9/16.
 */

public class TeamPresenter implements TeamContract.TeamPTPresenter, RemoteObserver {

    TeamContract.TeamPTView view = null;
    List<Team> datasourceTeams = null;
    String menuname = null;

    public TeamPresenter(TeamContract.TeamPTView view) {
        this.view = view;
    }

    @Override
    public void initData(String menuname, Context context) {
        this.menuname = menuname;
        datasourceTeams = Team.getTeams(context);
        if (datasourceTeams != null) {
            for (int i = 0; i < datasourceTeams.size(); i++) {
                if (datasourceTeams.get(i).getIsdelete().equals("1"))
                    datasourceTeams.remove(i--);
            }
            view.showDataSourceChangedView(datasourceTeams);
        } else
            view.showDataSourceChangedView(new ArrayList<Team>());
    }

    @Override
    public Team getCurrentTeam(int position) {
        return datasourceTeams.get(position);
    }

    @Override
    public SwipeMenuCreator getSwipeMenuCreator() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem EditItem = new SwipeMenuItem(TodoHelper.getInstance());
                EditItem.setBackground(new ColorDrawable(Color.parseColor("#C7C6CC")));
                EditItem.setWidth(200);
                EditItem.setTitle("编辑");
                EditItem.setTitleSize(18);
                EditItem.setTitleColor(Color.WHITE);
                EditItem.setId(0);
                menu.addMenuItem(EditItem);

                SwipeMenuItem deleteItem = new SwipeMenuItem(TodoHelper.getInstance());
                deleteItem.setId(1);
                deleteItem.setBackground(new ColorDrawable(Color.parseColor("#FF2730")));
                deleteItem.setWidth(200);
                deleteItem.setTitle("退出");
                deleteItem.setTitleSize(18);
                deleteItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(deleteItem);
            }
        };
        return creator;
    }

    @Override
    public void editTeam(int position, Context context) {
        Team team = datasourceTeams.get(position);
        Intent intent = new Intent(context, AddEditTeamActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("date", team);
        intent.putExtra("bundle", bundle);
        view.showEditTeam(intent);
    }

    @Override
    public void deleteOne(final int position, final Context context) {
        AlertDialog.Builder adCom = new AlertDialog.Builder(context);
        Team team = datasourceTeams.get(position);
        if(team.getLeaderId().equals(TodoHelper.UserId))
            adCom.setMessage("是否解散该小组?");
        else
            adCom.setMessage("是否退出该小组?");
        adCom.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        adCom.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Team team = datasourceTeams.get(position);
                //解散小组
                if(team.getLeaderId().equals(TodoHelper.UserId)){
                    new Remote_Team().updateTeam(team, "delete", TeamPresenter.this);
                }//退出小组
                else{
                    TeamJoinInfo teamJoinInfo = new TeamJoinInfo();
                    teamJoinInfo.setToUserId(team.getLeaderId());
                    teamJoinInfo.setToUserName(team.getLeaderName());
                    teamJoinInfo.setFromUserId(TodoHelper.UserId);
                    teamJoinInfo.setFromUserName(TodoHelper.UserName);
                    teamJoinInfo.setIsdelete("0");
                    teamJoinInfo.setExecType(TodoHelper.TeamJoinType.get("3"));
                    teamJoinInfo.setTeamName(team.getTeamname());
                    teamJoinInfo.setTeamId(team.getTeamId());
                    new Remote_Notification().userQuitTeam(teamJoinInfo, TeamPresenter.this);
                }
            }
        });
        adCom.create();
        adCom.show();
    }

    @Override
    public void Exeute(RemoteObserverEvent remoteObserverEvent) {
        if (remoteObserverEvent.clientResult.isResult()) {
            if (remoteObserverEvent.object.equals("updateTeam")) {
                Type type = new TypeToken<List<WebTeam>>() {
                }.getType();
                String jsonTeams = (String) remoteObserverEvent.clientResult.getObject();
                List<WebTeam> teams = new Gson().fromJson(jsonTeams, type);
                new Remote_Team().refreshLocalTeams(teams);
                new LaunchPresenter(null).initLocalTeamTasksFromWeb(TodoHelper.getInstance());
            }else if(remoteObserverEvent.object.toString().equals("userQuitTeam")){
                Type type = new TypeToken<List<WebTeam>>(){}.getType();
                String jsonTeams = (String)remoteObserverEvent.clientResult.getObject();
                List<WebTeam> teams = new Gson().fromJson(jsonTeams, type);
                new Remote_Team().refreshLocalTeams(teams);
                new LaunchPresenter(null).initLocalTeamTasksFromWeb(TodoHelper.getInstance());
                new LaunchPresenter(null).initLocalProjectsFromWeb(TodoHelper.getInstance());
            }
            initData(menuname, TodoHelper.getInstance());
            return;
        }
        Toast.makeText(TodoHelper.getInstance(), remoteObserverEvent.clientResult.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
