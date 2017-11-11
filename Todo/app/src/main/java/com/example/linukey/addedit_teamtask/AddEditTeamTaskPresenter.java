package com.example.linukey.addedit_teamtask;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.linukey.data.model.local.Project;
import com.example.linukey.data.model.local.Team;
import com.example.linukey.data.model.local.TeamTask;
import com.example.linukey.data.model.remote.WebTeamTask;
import com.example.linukey.data.source.remote.RemoteObserver;
import com.example.linukey.data.source.remote.RemoteObserverEvent;
import com.example.linukey.data.source.remote.Remote_TeamTask;
import com.example.linukey.util.TodoHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by linukey on 12/9/16.
 */

public class AddEditTeamTaskPresenter implements AddEditTeamTaskContract.AddEditTeamTaskPresenter,
        RemoteObserver{
    AddEditTeamTaskContract.AddEditTeamTaskView view = null;
    List<Team> dataSourceTeams;
    List<Project> dataSourceProjects;

    public AddEditTeamTaskPresenter(AddEditTeamTaskContract.AddEditTeamTaskView view){
        this.view = view;
    }

    @Override
    public DatePickerDialog getDateSeleteDialog(View view, Context context) {
        final TextView textView = (TextView) view;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = simpleDateFormat.parse(textView.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String date = year + "-" + ++month + "-" + dayOfMonth;
                        textView.setText(date);
                    }
                }, date.getYear() + 1900, date.getMonth(), date.getDate());

        return datePickerDialog;
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
                    if (view.saveTask()) {
                        activity.setResult(RESULT_OK);
                    }
                }
                return true;
        }
        return false;
    }

    @Override
    public void initTeamSpiner(){
        dataSourceTeams = Team.getTeams(TodoHelper.getInstance());
        List<String> teamNames = new ArrayList<>();
        teamNames.add("");
        if(dataSourceTeams != null)
            for(int i = 0; i < dataSourceTeams.size(); i++){
                if(!dataSourceTeams.get(i).getLeaderId().equals(TodoHelper.UserId)){
                    dataSourceTeams.remove(i);
                    i--;
                }else{
                    teamNames.add(dataSourceTeams.get(i).getTeamname());
                }
            }
        view.setSpinerTeamsAdapter(teamNames);
    }

    @Override
    public void initProjectSpiner(){
        dataSourceProjects = Project.getProjects("team", TodoHelper.getInstance());
        List<String> projectTitles = new ArrayList<>();
        projectTitles.add("");
        if(dataSourceProjects != null)
            for(int i = 0; i < dataSourceProjects.size(); i++){
                if(!dataSourceProjects.get(i).getUserId().equals(TodoHelper.UserId)){
                    dataSourceProjects.remove(i);
                    i--;
                }else{
                    projectTitles.add(dataSourceProjects.get(i).getTitle());
                }
            }
        view.setSpinerProjectsAdapter(projectTitles);
    }

    @Override
    public int getSpinerProjectsSelection(TeamTask teamTask){
        if(teamTask != null){
            for(int i = 0; i < dataSourceProjects.size(); i++){
                if(dataSourceProjects.get(i).getSelfId().equals(teamTask.getProjectId())){
                    return i+1;
                }
            }
        }
        return 0;
    }

    @Override
    public int getSpinerTeamsSelection(TeamTask teamTask){
        if(teamTask != null){
            for(int i = 0; i < dataSourceTeams.size(); i++){
                if(dataSourceTeams.get(i).getTeamId().equals(teamTask.getTeamId())){
                    return i+1;
                }
            }
        }
        return 0;
    }

    @Override
    public String getSelectedProjectId(int position){
        return dataSourceProjects.get(position).getSelfId();
    }

    @Override
    public String getSelectedTeamId(int position){
        return dataSourceTeams.get(position).getTeamId();
    }

    @Override
    public boolean saveTask(boolean isEdit, int preId, String title, String content, String starttime, String endtime,
                           String clocktime, String projectId, String teamId, String isdelete, String selfId){
        TeamTask teamTask = new TeamTask(preId, title, content, starttime, endtime, clocktime, projectId,
                teamId, TodoHelper.TaskState.get("noComplete"), isdelete, selfId);
        if(isEdit){
                new Remote_TeamTask().updateTeamTask(teamTask, this);
                return true;
        }else{
                new Remote_TeamTask().saveTeamTask(teamTask, this);
                return true;
            }
    }

    @Override
    public void Exeute(RemoteObserverEvent remoteObserverEvent) {
        if(remoteObserverEvent.clientResult.isResult()) {
            if (remoteObserverEvent.object.toString().equals("saveTeamTask")
                    || remoteObserverEvent.object.toString().equals("updateTeamTask")) {
                Type type = new TypeToken<List<WebTeamTask>>(){}.getType();
                String jsonData = (String)remoteObserverEvent.clientResult.getObject();
                List<WebTeamTask> teamTasks = new Gson().fromJson(jsonData, type);
                new Remote_TeamTask().refreshLocalTeamTasks(teamTasks);
            }
            view.viewFinish();
            return;
        }
        Toast.makeText(TodoHelper.getInstance(), remoteObserverEvent.clientResult.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
