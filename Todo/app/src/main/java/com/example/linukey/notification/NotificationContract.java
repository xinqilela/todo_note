package com.example.linukey.notification;

import android.view.View;
import android.view.Window;

import com.example.linukey.data.model.local.TeamJoinInfo;
import com.example.linukey.todo.SwipeMenu.SwipeMenuCreator;

import java.util.List;

/**
 * Created by linukey on 17-2-28.
 */

public class NotificationContract {
    interface NotificationView{


        void setDataToListView(List<TeamJoinInfo> dataSource);
    }
    interface NotificationPresenter{

        SwipeMenuCreator getSwipeMenuCreator();

        void initData();

        TeamJoinInfo getTeamJoinInfoByPosition(int position);

        void agreeSomeOneToJoinTeam(int position);

        void deleteTeamJoinInfo(int position);

        void refuseSomeOneToJoinTeam(int position);
    }
}
