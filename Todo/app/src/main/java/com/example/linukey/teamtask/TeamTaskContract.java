package com.example.linukey.teamtask;

import android.content.Context;
import android.content.Intent;
import android.view.Menu;

import com.example.linukey.data.model.local.TeamTask;
import com.example.linukey.todo.SwipeMenu.SwipeMenuCreator;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * Created by linukey on 12/9/16.
 */

public interface TeamTaskContract {
    interface TeamTaskActivityView{

        void viewFinish();

        void showAddTeamTask(Intent intent);

        void showAfterDataSourceChanged(List<TeamTask> teamTasks);

        void showEditTask(Intent intent);
    }
    interface TeamTaskActivityPresenter{
        void CreateMenu(Menu menu);

        void addTeamTask(Context context);

        List<TeamTask> getTeamTaskbyTeamId(String teamId);

        boolean isValidUser(int position);

        List<TeamTask> getTeamTaskbyTeamProjectId(String projectId);

        List<TeamTask> getTomorrowDate() throws ParseException;

        List<TeamTask> getTodayDate() throws ParseException;

        boolean isOvertimeOrCompleteOrDelete(TeamTask teamTask);

        List<TeamTask> getNextDate() throws ParseException;

        Date getDateToday() throws ParseException;

        Date getNextDay(Date date);

        SwipeMenuCreator getSwipeMenuCreator();

        void editTask(int position, Context context);

        void deleteTask(int position, String menuName, Context context);

        void initData(String menuName, String projectId, String teamId, Context context);

        TeamTask getTeamTaskByPosition(int position);

        void completedTask(int position, String menuName, Context context);
    }
}
