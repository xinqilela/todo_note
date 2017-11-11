package com.example.linukey.notification;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.Toast;

import com.example.linukey.data.model.local.Team;
import com.example.linukey.data.model.local.TeamJoinInfo;
import com.example.linukey.data.model.remote.WebTeamJoinInfo;
import com.example.linukey.data.source.remote.RemoteObserver;
import com.example.linukey.data.source.remote.RemoteObserverEvent;
import com.example.linukey.data.source.remote.Remote_Notification;
import com.example.linukey.launch.LaunchPresenter;
import com.example.linukey.todo.MainActivity;
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
 * Created by linukey on 17-2-28.
 */

public class NotificationPresenter implements NotificationContract.NotificationPresenter, RemoteObserver {
    NotificationContract.NotificationView view;
    private List<TeamJoinInfo> teamJoinInfosSource;

    public NotificationPresenter(NotificationContract.NotificationView view){
        this.view = view;
    }

    /**
     * 任务左划菜单
     * @return
     */
    @Override
    public SwipeMenuCreator getSwipeMenuCreator() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem EditItem = new SwipeMenuItem(TodoHelper.getInstance());
                EditItem.setBackground(new ColorDrawable(Color.parseColor("#C7C6CC")));
                EditItem.setWidth(200);
                EditItem.setTitle("已读");
                EditItem.setTitleSize(18);
                EditItem.setTitleColor(Color.WHITE);
                EditItem.setId(0);
                menu.addMenuItem(EditItem);
            }
        };
        return creator;
    }

    @Override
    public void initData(){
        teamJoinInfosSource = TeamJoinInfo.getTeamJoinInfos(TodoHelper.getInstance());
        if(teamJoinInfosSource != null)
            view.setDataToListView(teamJoinInfosSource);
        else
            view.setDataToListView(new ArrayList<TeamJoinInfo>());

    }

    @Override
    public TeamJoinInfo getTeamJoinInfoByPosition(int position){
        return teamJoinInfosSource.get(position);
    }

    @Override
    public void agreeSomeOneToJoinTeam(int position){
        new Remote_Notification().agreeSomeOneToJoinTeam(teamJoinInfosSource.get(position), this);
    }

    @Override
    public void deleteTeamJoinInfo(int position){
        new Remote_Notification().deleteTeamJoinNotification(teamJoinInfosSource.get(position), this);
    }

    @Override
    public void refuseSomeOneToJoinTeam(int position){
        new Remote_Notification().refuseSomeOneToJoinTeam(teamJoinInfosSource.get(position), this);
    }

    @Override
    public void Exeute(RemoteObserverEvent remoteObserverEvent) {
        if (remoteObserverEvent.clientResult.isResult()) {
            if (remoteObserverEvent.object.toString().equals("agreeSomeOneToJoinTeam")) {
            } else if (remoteObserverEvent.object.toString().equals("deleteTeamJoinData")) {
            } else if (remoteObserverEvent.object.toString().equals("refuseSomeOneToJoinTeam")) {
            }
            Type type = new TypeToken<List<WebTeamJoinInfo>>(){}.getType();
            String jsonData = (String)remoteObserverEvent.clientResult.getObject();
            List<WebTeamJoinInfo> teamJoinDatas = new Gson().fromJson(jsonData, type);
            new Remote_Notification().refreshLocalTeamJoinNotifications(teamJoinDatas);
            initData();
            return;
        }
    }
}