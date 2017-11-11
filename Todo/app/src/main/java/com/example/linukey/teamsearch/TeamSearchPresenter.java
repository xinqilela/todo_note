package com.example.linukey.teamsearch;

import android.widget.Toast;

import com.example.linukey.data.model.local.TeamJoinInfo;
import com.example.linukey.data.model.remote.WebTeam;
import com.example.linukey.data.source.remote.RemoteObserver;
import com.example.linukey.data.source.remote.RemoteObserverEvent;
import com.example.linukey.data.source.remote.Remote_Notification;
import com.example.linukey.data.source.remote.Remote_Team;
import com.example.linukey.notification.PointToPoint;
import com.example.linukey.todo.MainActivity;
import com.example.linukey.util.TodoHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by linukey on 17-2-27.
 */

public class TeamSearchPresenter implements TeamSearchContract.TeamSearchPresenter, RemoteObserver {
    TeamSearchContract.TeamSearchView view;
    List<WebTeam> dataSource;

    public TeamSearchPresenter(TeamSearchContract.TeamSearchView view){
        this.view = view;
    }

    @Override
    public void getTeamsByTeamName(String teamName){
        new Remote_Team().getTeamsByTeamName(teamName, this);
    }

    @Override
    public void Exeute(RemoteObserverEvent remoteObserverEvent) {
        if(remoteObserverEvent.object.toString().equals("getTeamsByTeamName")) {
            if (remoteObserverEvent.clientResult.isResult()) {
                Type type = new TypeToken<List<WebTeam>>() {
                }.getType();
                String jsonTeams = (String) remoteObserverEvent.clientResult.getObject();
                List<WebTeam> teams = new Gson().fromJson(jsonTeams, type);
                dataSource = teams;
                view.setSearchResult(teams);
            } else {
                view.setSearchResult(new ArrayList<WebTeam>());
                Toast.makeText(TodoHelper.getInstance(),
                        remoteObserverEvent.clientResult.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }else if(remoteObserverEvent.object.toString().equals("saveTeamJoinNotification")){
            if(remoteObserverEvent.clientResult.isResult()){
                TeamJoinInfo teamJoinInfo = (TeamJoinInfo) remoteObserverEvent.clientResult.getObject();

                //封装发给另一个Android客户端的消息
                PointToPoint pointToPoint = new PointToPoint();
                pointToPoint.setExecType(TodoHelper.PtoPExecType.get("joinTeam"));
                pointToPoint.setFromUserName(teamJoinInfo.getFromUserName());
                pointToPoint.setToUserName(teamJoinInfo.getToUserName());
                //用json的形式传输
                String jsonData = new Gson().toJson(pointToPoint);
                MainActivity.sendMessage(pointToPoint.getToUserName(), jsonData);

                Toast.makeText(TodoHelper.getInstance(), "申请发送成功!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void saveJoinTeamInfoToWeb(TeamJoinInfo teamJoinInfo){
        new Remote_Notification().saveTeamJoinNotification(teamJoinInfo, this);
    }

    @Override
    public WebTeam getTeamByPosition(int position){
        return dataSource.get(position);
    }
}