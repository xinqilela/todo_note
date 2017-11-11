package com.example.linukey.addedit_team;

import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;

import com.example.linukey.data.model.local.Team;

import java.text.ParseException;

/**
 * Created by linukey on 12/9/16.
 */

public class AddEditTeamContract {
    interface AddEditTeamView{

        void initEdit(Team team);

        void showMessage(String message);

        boolean checkInput();

        void viewFinish();

        boolean saveTeam();
    }
    interface AddEditTeamPresenter{

        void CreateMenu(Menu menu);

        boolean MenuChoice(MenuItem item, Activity activity) throws ParseException;

        boolean saveTeam(Team team, Context context, boolean isEdit);
    }
}
