package com.example.linukey.selfpgs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.linukey.addedit_selfpgs.AddEditSelfpgsActivity;
import com.example.linukey.data.model.local.Team;
import com.example.linukey.data.model.remote.WebGoal;
import com.example.linukey.data.model.remote.WebProject;
import com.example.linukey.data.model.remote.WebSight;
import com.example.linukey.data.source.remote.RemoteObserver;
import com.example.linukey.data.source.remote.RemoteObserverEvent;
import com.example.linukey.data.source.remote.Remote_Goal;
import com.example.linukey.data.source.remote.Remote_Project;
import com.example.linukey.data.source.remote.Remote_Sight;
import com.example.linukey.launch.LaunchPresenter;
import com.example.linukey.todo.SwipeMenu.SwipeMenu;
import com.example.linukey.todo.SwipeMenu.SwipeMenuCreator;
import com.example.linukey.todo.SwipeMenu.SwipeMenuItem;
import com.example.linukey.data.model.local.Goal;
import com.example.linukey.data.model.local.Project;
import com.example.linukey.data.model.local.Sight;
import com.example.linukey.data.model.local.TaskClassify;
import com.example.linukey.todo.R;
import com.example.linukey.util.TodoHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by linukey on 12/8/16.
 */

public class SelfPGSPresenter implements SelfPGSContract.SelfPGSActivityPresenter, RemoteObserver {
    SelfPGSContract.SelfPGSActivityView selfPGSActivityView;
    List<Project> dataSourceProjects = null;
    List<Goal> dataSourceGoals = null;
    List<Sight> dataSourceSights = null;
    List<TaskClassify> dataSourcePGS = null;
    String menuName = null;

    public SelfPGSPresenter(SelfPGSContract.SelfPGSActivityView view) {
        this.selfPGSActivityView = view;
    }

    @Override
    public TaskClassify getPGSIdByPosition(String menuName, int position){
        if(menuName.equals("project")) return dataSourceProjects.get(position);
        if(menuName.equals("goal")) return dataSourceGoals.get(position);
        if(menuName.equals("sight")) return dataSourceSights.get(position);
        if(menuName.equals("teamProject")) return  dataSourceProjects.get(position);
        return null;
    }

    @Override
    public void initData(String menuName, Context context){
        this.menuName = menuName;
        switch (menuName) {
            case "project":
                dataSourceProjects = Project.getProjects("self", context);
                dataSourcePGS = getProjectDate();
                break;
            case "goal":
                dataSourceGoals = Goal.getGoals(context);
                dataSourcePGS = getGoalsDate();
                break;
            case "sight":
                dataSourceSights = Sight.getSights(context);
                dataSourcePGS = getSightDate();
                break;
            case "teamProject":
                dataSourceProjects = Project.getProjects("team", context);
                dataSourcePGS = getProjectDate();
            default:
                break;
        }

        if (dataSourcePGS != null)
            selfPGSActivityView.showDataSourceChanged(dataSourcePGS);
        else
            selfPGSActivityView.showDataSourceChanged(new ArrayList<TaskClassify>());
    }

    /**
     * 获取本地目标数据
     *
     * @return
     */
    @Override
    public List<TaskClassify> getGoalsDate() {
        List<TaskClassify> result = null;
        if (dataSourceGoals != null && dataSourceGoals.size() > 0) {
            result = new ArrayList<>();
            for (Goal goal : dataSourceGoals) {
                if (goal.getState().equals(TodoHelper.PGS_State.get("noComplete")) &&
                        goal.getIsdelete().equals("0"))
                    result.add(goal);
            }
        }
        return result;
    }

    /**
     * 获取本地情景数据
     *
     * @return
     */
    @Override
    public List<TaskClassify> getSightDate() {
        List<TaskClassify> result = null;
        if (dataSourceSights != null && dataSourceSights.size() > 0) {
            result = new ArrayList<>();
            for (Sight sight : dataSourceSights) {
                if (sight.getIsdelete().equals("0"))
                    result.add(sight);
            }
        }
        return result;
    }

    /**
     * 获取本地项目数据
     *
     * @return
     */
    @Override
    public List<TaskClassify> getProjectDate() {
        List<TaskClassify> result = null;
        if (dataSourceProjects != null && dataSourceProjects.size() > 0) {
            result = new ArrayList<>();
            for (Project project : dataSourceProjects) {
                if (project.getState().equals(TodoHelper.PGS_State.get("noComplete")) &&
                        project.getIsdelete().equals("0"))
                    result.add(project);
            }
        }
        return result;
    }

    @Override
    public void addSelfPGS(String menuName, Context context) {
        Intent intent = new Intent(context, AddEditSelfpgsActivity.class);
        intent.putExtra("menuname", menuName);
        selfPGSActivityView.showAddSelfPGS(intent);
    }

    @Override
    public void CreateMenu(Menu menu) {
        MenuItem taskAdd = menu.add(0, 0, 0, "添加");
        taskAdd.setIcon(R.mipmap.add);
        taskAdd.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    }

    @Override
    public SwipeMenuCreator getSwipeMenuCreator(final String menuName) {
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem EditItem = new SwipeMenuItem(
                        TodoHelper.getInstance());
                EditItem.setBackground(new ColorDrawable(Color.parseColor("#C7C6CC")));
                EditItem.setWidth(200);
                EditItem.setTitle("编辑");
                EditItem.setTitleSize(18);
                EditItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(EditItem);

                SwipeMenuItem deleteItem = new SwipeMenuItem(TodoHelper.getInstance());
                deleteItem.setBackground(new ColorDrawable(Color.parseColor("#FF2730")));
                deleteItem.setWidth(200);
                deleteItem.setTitle("删除");
                deleteItem.setTitleSize(18);
                deleteItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(deleteItem);

                if (!menuName.equals("sight")) {
                    SwipeMenuItem completeItem = new SwipeMenuItem(TodoHelper.getInstance());
                    completeItem.setBackground(new ColorDrawable(Color.parseColor("#FF9700")));
                    completeItem.setWidth(200);
                    completeItem.setTitle("完成");
                    completeItem.setTitleSize(18);
                    completeItem.setTitleColor(Color.WHITE);
                    menu.addMenuItem(completeItem);
                }
            }
        };
        return creator;
    }

    @Override
    public void editPGS(String menuName, int position, Context context) {
        TaskClassify taskClassify = dataSourcePGS.get(position);
        Intent intent = new Intent(context, AddEditSelfpgsActivity.class);
        intent.putExtra("menuname", menuName);
        Bundle bundle = new Bundle();
        bundle.putSerializable("date", taskClassify);
        intent.putExtra("bundle", bundle);
        selfPGSActivityView.showEditPGS(intent);
    }

    @Override
    public boolean isValidUser(int position){
        String userId = dataSourceProjects.get(position).getUserId();
        if(userId.equals(TodoHelper.UserId)) return true;
        return false;
    }

    @Override
    public void deletePGS(final String menuName, final int position, final Context context) {
        AlertDialog.Builder adDel = new AlertDialog.Builder(context);
        adDel.setMessage("是否要删除?");
        adDel.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        adDel.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (menuName) {
                    case "project":
                    case "teamProject":
                        Project project = Project.getProjectById(dataSourcePGS.get(position).getId(), context);
                        new Remote_Project().updateProject(project, "delete", SelfPGSPresenter.this);
                        break;
                    case "goal":
                        Goal goal = Goal.getGoalById(dataSourcePGS.get(position).getId(), context);
                        new Remote_Goal().updateGoal(goal, "delete", SelfPGSPresenter.this);
                        break;
                    case "sight":
                        Sight sight = Sight.getSightById(dataSourcePGS.get(position).getId(), context);
                        new Remote_Sight().updateSight(sight, "delete", SelfPGSPresenter.this);
                        break;
                    default:
                        break;
                }
            }
        });
        adDel.create();
        adDel.show();
    }

    @Override
    public void completedPGS(final String menuName, final int position, final Context context) {
        AlertDialog.Builder adDel = new AlertDialog.Builder(context);
        adDel.setMessage("相应任务也会置于完成状态，是否已经完成?");
        adDel.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        adDel.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (menuName) {
                    case "project":
                        Project project = Project.getProjectById(dataSourcePGS.get(position).getId(), context);
                        new Remote_Project().updateProject(project, "complete", SelfPGSPresenter.this);
                        break;
                    case "goal":
                        Goal goal = Goal.getGoalById(dataSourcePGS.get(position).getId(), context);
                        new Remote_Goal().updateGoal(goal, "complete", SelfPGSPresenter.this);
                        break;
                    default:
                        break;
                }
            }
        });
        adDel.create();
        adDel.show();
    }

    @Override
    public void Exeute(RemoteObserverEvent remoteObserverEvent) {
            if(remoteObserverEvent.clientResult.isResult()) {
                String judge = remoteObserverEvent.object.toString();
                String jsonData = (String)remoteObserverEvent.clientResult.getObject();
                if (judge.equals("updateProject") || judge.equals("saveProject")) {
                    Type type = new TypeToken<List<WebProject>>(){}.getType();
                    List<WebProject> projects = new Gson().fromJson(jsonData, type);
                    new Remote_Project().refreshLocalProjects(projects);
                    if(menuName.equals("project"))
                        new LaunchPresenter(null).initLocalSelfTasksFromWeb(TodoHelper.getInstance());
                    else if(menuName.equals("teamProject"))
                        new LaunchPresenter(null).initLocalTeamTasksFromWeb(TodoHelper.getInstance());
                } else if (judge.equals("updateGoal") || judge.equals("saveGoal")) {
                    Type type = new TypeToken<List<WebGoal>>(){}.getType();
                    List<WebGoal> goals = new Gson().fromJson(jsonData, type);
                    new Remote_Goal().refreshLocalGoals(goals);
                    new LaunchPresenter(null).initLocalSelfTasksFromWeb(TodoHelper.getInstance());
                } else if (judge.equals("updateSight") || judge.equals("saveSight")) {
                    Type type = new TypeToken<List<WebSight>>(){}.getType();
                    List<WebSight> sights = new Gson().fromJson(jsonData, type);
                    new Remote_Sight().refreshLocalSights(sights);
                    new LaunchPresenter(null).initLocalSelfTasksFromWeb(TodoHelper.getInstance());
                }
                initData(menuName, TodoHelper.getInstance());
                return;
            }
        Toast.makeText(TodoHelper.getInstance(), remoteObserverEvent.clientResult.getMessage(),
                Toast.LENGTH_SHORT).show();
    }
}
