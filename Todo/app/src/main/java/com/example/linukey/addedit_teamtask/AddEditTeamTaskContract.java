package com.example.linukey.addedit_teamtask;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.linukey.data.model.local.TeamTask;

import java.text.ParseException;
import java.util.List;

/**
 * Created by linukey on 12/9/16.
 */

public interface AddEditTeamTaskContract {
    interface AddEditTeamTaskView{

        void setSpinerTeamsAdapter(List<String> datas);

        void setSpinerProjectsAdapter(List<String> projectTitles);

        void initEdit(TeamTask teamTask);

        void initControl();

        boolean checkInput() throws ParseException;

        void viewFinish();

        boolean saveTask();
    }
    interface AddEditTeamTaskPresenter{

        DatePickerDialog getDateSeleteDialog(View view, Context context);

        void CreateMenu(Menu menu);

        boolean MenuChoice(MenuItem item, Activity activity) throws ParseException;

        void initTeamSpiner();

        void initProjectSpiner();

        int getSpinerProjectsSelection(TeamTask teamTask);

        int getSpinerTeamsSelection(TeamTask teamTask);

        String getSelectedProjectId(int position);

        String getSelectedTeamId(int position);

        boolean saveTask(boolean isEdit, int preId, String title, String content, String starttime, String endtime,
                        String clocktime, String projectId, String teamId, String isdelete, String selfId);
    }
}
