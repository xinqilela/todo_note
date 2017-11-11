package com.example.linukey.addedit_team;

import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;

import com.example.linukey.data.model.local.Team;
import com.example.linukey.data.model.remote.WebTeam;
import com.example.linukey.data.source.remote.RemoteObserver;
import com.example.linukey.data.source.remote.RemoteObserverEvent;
import com.example.linukey.data.source.remote.Remote_Team;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by linukey on 12/9/16.
 */

public class AddEditTeamPresenter implements AddEditTeamContract.AddEditTeamPresenter, RemoteObserver{
    AddEditTeamContract.AddEditTeamView view = null;

    public AddEditTeamPresenter(AddEditTeamContract.AddEditTeamView view){
        this.view = view;
    }

    @Override
    public void CreateMenu(Menu menu) {
        MenuItem taskAdd = menu.add(0, 0, 0, "cancel");
        taskAdd.setTitle("取消");
        taskAdd.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        MenuItem setting = menu.add(0, 1, 1, "ok");
        setting.setTitle("保存");
        setting.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    }

    @Override
    public boolean MenuChoice(MenuItem item, Activity activity) throws ParseException {
        switch (item.getItemId()) {
            case 0:
                activity.setResult(RESULT_CANCELED);
                activity.finish();
                return true;
            case 1:
                if (view.checkInput()) {
                    if (view.saveTeam()) {
                        activity.setResult(RESULT_OK);
                    }
                }
                return true;
        }
        return false;
    }

    @Override
    public boolean saveTeam(Team team, Context context, boolean isEdit){
        if(isEdit) {
            new Remote_Team().updateTeam(team, "edit", this);
            return true;
        }else{
            new Remote_Team().saveTeam(team, this);
            return true;
        }
    }

    @Override
    public void Exeute(RemoteObserverEvent remoteObserverEvent) {
        if(remoteObserverEvent.clientResult.isResult()){
            if(remoteObserverEvent.object.toString().equals("saveTeam") ||
                    remoteObserverEvent.object.toString().equals("updateTeam")){
                Type type = new TypeToken<List<WebTeam>>(){}.getType();
                String jsonTeams = (String)remoteObserverEvent.clientResult.getObject();
                List<WebTeam> teams = new Gson().fromJson(jsonTeams, type);
                new Remote_Team().refreshLocalTeams(teams);
            }
            view.viewFinish();
            return;
        }
        view.showMessage(remoteObserverEvent.clientResult.getMessage());
    }
}
