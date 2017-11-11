package com.example.linukey.addedit_selftask;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.linukey.data.model.local.Goal;
import com.example.linukey.data.model.local.Project;
import com.example.linukey.data.model.local.SelfTask;
import com.example.linukey.data.model.local.Sight;
import com.example.linukey.data.model.remote.WebSelfTask;
import com.example.linukey.data.source.remote.RemoteObserver;
import com.example.linukey.data.source.remote.RemoteObserverEvent;
import com.example.linukey.data.source.remote.Remote_SelfTask;
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

public class AddEditSelfTaskPresenter implements AddEditSelfTaskContract.AddEditSelfTaskPresenter,
        RemoteObserver{
    AddEditSelfTaskContract.AddEditSelfTaskView view = null;

    List<Project> projectList = null;
    List<Goal> goalList = null;
    List<Sight> sightList = null;

    public AddEditSelfTaskPresenter(AddEditSelfTaskContract.AddEditSelfTaskView view) {
        this.view = view;
    }

    /**
     * 初始化后项目选择栏
     */
    @Override
    public void initSpinerProjects() {
        projectList = Project.getProjects("self", TodoHelper.getInstance());
        List<String> projectNames = new ArrayList<>();
        projectNames.add("");
        if (projectList != null) {
            for (Project project : projectList) {
                if(project.getIsdelete().equals("0"))
                    projectNames.add(project.getTitle());
            }
            view.setSpinerProjectsAdapter(projectNames);
        }
    }

    /**
     * 初始化目标选择栏
     */
    @Override
    public void initGoalSpiner() {
        goalList = Goal.getGoals(TodoHelper.getInstance());
        List<String> goalNames = new ArrayList<>();
        goalNames.add("");
        if (goalList != null) {
            for (Goal goal : goalList) {
                if(goal.getIsdelete().equals("0"))
                    goalNames.add(goal.getTitle());
            }
            view.setSpinerGoalsAdapter(goalNames);
        }
    }

    /**
     * 初始化情景选择栏
     */
    @Override
    public void initSightSpiner() {
        sightList = Sight.getSights(TodoHelper.getInstance());
        List<String> sightNames = new ArrayList<>();
        sightNames.add("");
        if (sightList != null) {
            for (Sight sight : sightList) {
                if(sight.getIsdelete().equals("0"))
                    sightNames.add(sight.getTitle());
            }
            view.setSpinerSightsAdapter(sightNames);
        }
    }

    /**
     * 获得选择的任务项目
     * @param selfTask
     * @return
     */
    @Override
    public int getSpinerProjectSelection(SelfTask selfTask) {
        if (projectList != null)
            for (int i = 0; i < projectList.size(); i++) {
                if (projectList.get(i).getSelfId().equals(selfTask.getProjectId())) {
                    return i + 1;
                }
            }
        return 0;
    }

    /**
     * 获得选择的任务目标
     * @param selfTask
     * @return
     */
    @Override
    public int getSpinerGoalSelection(SelfTask selfTask) {
        if (goalList != null)
            for (int i = 0; i < goalList.size(); i++) {
                if (goalList.get(i).getSelfId().equals(selfTask.getGoalId())) {
                    return i + 1;
                }
            }
        return 0;
    }

    /**
     * 获得选择的任务情景
     * @param selfTask
     * @return
     */
    @Override
    public int getSpinerSightSelection(SelfTask selfTask) {
        if (sightList != null)
            for (int i = 0; i < sightList.size(); i++) {
                if (sightList.get(i).getSelfId().equals(selfTask.getSightId())) {
                    return i + 1;
                }
            }
        return 0;
    }

    /**
     *任务日期选择
     */
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

    /**
     * 任务闹钟时间选择
     */
    @Override
    public TimePickerDialog getTimeSelectDialog(View view, Context context) {
        final TextView textView = (TextView) view;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        Date date = null;
        try {
            date = simpleDateFormat.parse(textView.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String time = hourOfDay + ":" + minute;
                        textView.setText(time);
                    }
                }, date.getHours(), date.getMinutes(), true);
        return timePickerDialog;
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

    /**
     * 保存取消按钮事件
     */
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

    /**
     * 保存修改个人任务
     * @param selfTask
     * @param isEdit
     * @param context
     * @return
     */
    @Override
    public boolean saveTask(SelfTask selfTask, boolean isEdit, Context context) {
        //编辑
        if (isEdit) {
                new Remote_SelfTask().updateSelfTask(selfTask, this);
                return true;
        } else {//添加
                new Remote_SelfTask().saveSelfTask(selfTask, this);
                return true;
        }
    }

    /**
     * 服务器端的添加修改成功后，把本地相应任务的isAlter属性改为0
     * @param remoteObserverEvent
     */
    @Override
    public void Exeute(RemoteObserverEvent remoteObserverEvent) {
        if(remoteObserverEvent.object.toString().equals("saveSelfTask") ||
                remoteObserverEvent.object.toString().equals("updateOrdelete")){
            if(remoteObserverEvent.clientResult.isResult()){
                Type type = new TypeToken<List<WebSelfTask>>() {}.getType();
                String jsonTasks = (String) remoteObserverEvent.clientResult.getObject();
                List<WebSelfTask> tasks = new Gson().fromJson(jsonTasks, type);
                new Remote_SelfTask().refreshLocalSelfTasks(tasks);
                view.viewFinish();
            }
            return;
        }
        view.showMessage(remoteObserverEvent.clientResult.getMessage());
    }

    /**
     * 获得项目projectId
     * @param index
     * @return
     */
    @Override
    public String getProjectId(int index) {
        return projectList.get(index).getSelfId();
    }

    /**
     * 获得目标GoalId
     * @param index
     * @return
     */
    @Override
    public String getGoalId(int index) {
        return goalList.get(index).getSelfId();
    }

    /**
     * 获得情景SightId
     * @param index
     * @return
     */
    @Override
    public String getSightId(int index) {
        return sightList.get(index).getSelfId();
    }

}
