package com.example.linukey.team;

import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

import com.example.linukey.data.model.local.Team;
import com.example.linukey.todo.SwipeMenu.SwipeMenuCreator;

import java.util.List;

/**
 * Created by linukey on 12/9/16.
 */

public class TeamContract {
    interface TeamPTView{
        void showTeamDialogFragment(int position);

        void showAddEditTeamActivity();

        //void showJoinTeamDialog();

        void showEditTeam(Intent intent);

        void showDataSourceChangedView(List<Team> datasourceTeams);

        void viewFinish();

        void showTeamSearchActivity();
    }
    interface TeamPTPresenter{
        Team getCurrentTeam(int position);

        SwipeMenuCreator getSwipeMenuCreator();

        void initData(String menuname, Context context);

        void editTeam(int position, Context context);

        void deleteOne(int position, Context context);
    }
    interface TeamDialogFragmentView{

        void initView(View view);

        void initData(String title, String content, String leader);
    }
    interface TeamDialogFragmentPresenter{

        void setData(String title, String contentm, String leader);
    }
}
