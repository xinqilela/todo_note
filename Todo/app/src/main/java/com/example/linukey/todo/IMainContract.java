package com.example.linukey.todo;

import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.example.linukey.data.model.local.SelfTask;
import com.example.linukey.data.model.local.TaskAll;
import com.example.linukey.data.model.local.TeamTask;

import java.util.List;

/**
 * Created by linukey on 12/8/16.
 */

public interface IMainContract {
    interface View{

        void addSelfTask();

        void addTeamTask();

        void changeIcon();
    }
    interface Presenter{
        void CreateMenu(Menu menu);
        boolean MenuChoice(MenuItem item, Context context);

    }
    interface HomePageFragmentView{

    }
    interface HomePageFragmentPresenter{

        void initData(String type, Context context);

        List<TaskAll> getCODTasks(List<SelfTask> dataSourceSelfTasks, List<TeamTask> dataSourceTeamTasks, String taskType);

        int getCODTaskCnt(String taskType);
    }
}
