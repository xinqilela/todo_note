package com.example.linukey.selftask;

import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

import com.example.linukey.todo.SwipeMenu.SwipeMenuCreator;
import com.example.linukey.data.model.local.SelfTask;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * Created by linukey on 12/8/16.
 */

public interface SelfTaskContract {
    interface ActivityView{
        void showAddTask(Intent intent);
        void showSelfTaskDialogFragment(int position);
        void showEditTask(Intent intent);
        void showAfterDataSourceChanged(List<SelfTask> dataSource);
    }

    interface SelfTaskDialogView{
        void initDate(String title, String content, String starttime, String endtime,
                             String clocktime, String project, String goal, String sight);
        void initView(View view);
    }

    interface SelfTaskDialogPresenter{
        void setData(String title, String content, String starttime, String endtime,
                     String clocktime, String project, String goal, String sight);
    }

    interface ActivityPresenter{
        void initData(String menuName, String PGSId, Context context);

        List<SelfTask> getSelfTaskByPGSId(String menuName, String PGSId);

        List<SelfTask> getTodayDate() throws ParseException;
        List<SelfTask> getTomorrowDate() throws ParseException;
        List<SelfTask> getNextDate() throws ParseException;
        List<SelfTask> getBoxDate() throws ParseException;
        boolean isBoxTaskOrOvertimeOrCompleteOrDelete(SelfTask selfTask);
        Date getDateToday() throws ParseException;
        Date getNextDay(Date date);
        void addSelfTask(Context context);
        void CreateMenu(Menu menu);
        SelfTask getCurrentTask(int position);
        String getTaskProjectTitle(String projectId);
        String getTaskGoalTitle(String goalId);
        String getTaskSightTitle(String sightId);
        SwipeMenuCreator getSwipeMenuCreator();
        void editTask(int position, Context context);
        void deleteTask(final int position, final String menuName, final Context context);
        void completedTask(final int position, final String menuName, final Context context);
    }
}
