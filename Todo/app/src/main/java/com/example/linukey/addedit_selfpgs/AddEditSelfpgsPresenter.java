package com.example.linukey.addedit_selfpgs;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.linukey.data.model.local.Goal;
import com.example.linukey.data.model.local.Project;
import com.example.linukey.data.model.local.Sight;
import com.example.linukey.data.model.remote.WebGoal;
import com.example.linukey.data.model.remote.WebProject;
import com.example.linukey.data.model.remote.WebSight;
import com.example.linukey.data.source.remote.RemoteObserver;
import com.example.linukey.data.source.remote.RemoteObserverEvent;
import com.example.linukey.data.source.remote.Remote_Goal;
import com.example.linukey.data.source.remote.Remote_Project;
import com.example.linukey.data.source.remote.Remote_Sight;
import com.example.linukey.util.TodoHelper;
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

public class AddEditSelfpgsPresenter implements AddEditSelfpgsContract.AddEditSelfpgsPresenter,
        RemoteObserver {

    AddEditSelfpgsContract.AddEditSelfpgsView view = null;
    private final String TAG = this.getClass().getName();

    public AddEditSelfpgsPresenter(AddEditSelfpgsContract.AddEditSelfpgsView view) {
        this.view = view;
    }

    /**
     * 保存情景
     * @return
     */
    @Override
    public boolean saveSight(boolean isEdit, Context context, String title, String content,
                             String selfId, String userId, int preId, String isdelete) {
        Sight sight = new Sight();
        sight.setTitle(title);
        sight.setContent(content);
        sight.setSelfId(selfId);
        sight.setUserId(userId);
        sight.setId(preId);
        sight.setIsdelete(isdelete);

        Log.i(TAG, "需要保存或更新的情景数据为:" + sight.toString());
        if (isEdit) {
                new Remote_Sight().updateSight(sight, "edit", this);
                return true;
        }else{
                new Remote_Sight().saveSight(sight, this);
                return true;
        }
    }

    /**
     * 保存目标
     * @return
     */
    @Override
    public boolean saveGoal(boolean isEdit, Context context, String title, String content, String selfId
            , String state, String userId, int preId, String isdelete) {
        Goal goal = new Goal();
        goal.setTitle(title);
        goal.setContent(content);
        goal.setSelfId(selfId);
        goal.setState(state);
        goal.setUserId(userId);
        goal.setId(preId);
        goal.setIsdelete(isdelete);

        Log.i(TAG, "需要保存或更新的目标数据为:" + goal.toString());
        if (isEdit) {
                new Remote_Goal().updateGoal(goal, "edit", this);
                return true;
        } else {
                new Remote_Goal().saveGoal(goal, this);
                return true;
        }
    }

    /**
     * 保存项目
     * @return
     */
    @Override
    public boolean saveProject(boolean isEdit, String menuName, Context context, String title, String content, String selfId,
                               String state, String userId, int preId, String isdelete) {
        Project project = new Project();
        project.setTitle(title);
        project.setContent(content);
        project.setSelfId(selfId);
        project.setState(state);
        project.setUserId(userId);
        project.setId(preId);
        project.setIsdelete(isdelete);
        if (menuName.equals("teamProject"))
            project.setType(TodoHelper.ProjectType.get("team"));
        else if (menuName.equals("project"))
            project.setType(TodoHelper.ProjectType.get("self"));

        Log.i(TAG, "需要保存或更新的" + menuName + "数据为:" + project.toString());
        if (isEdit) {
            new Remote_Project().updateProject(project, "edit", this);
            return true;
        } else {
            new Remote_Project().saveProject(project, this);
            return true;
        }
    }

    @Override
    public void Exeute(RemoteObserverEvent remoteObserverEvent) {
        String menuname = null;
            if (remoteObserverEvent.clientResult.isResult()) {
                String judge = remoteObserverEvent.object.toString();
                String jsonData = (String)remoteObserverEvent.clientResult.getObject();
                if (judge.equals("updateProject") || judge.equals("saveProject")) {
                    Type type = new TypeToken<List<WebProject>>(){}.getType();
                    List<WebProject> projects = new Gson().fromJson(jsonData, type);
                    new Remote_Project().refreshLocalProjects(projects);
                } else if (judge.equals("updateGoal") || judge.equals("saveGoal")) {
                    Type type = new TypeToken<List<WebGoal>>(){}.getType();
                    List<WebGoal> goals = new Gson().fromJson(jsonData, type);
                    new Remote_Goal().refreshLocalGoals(goals);
                } else if (judge.equals("updateSight") || judge.equals("saveSight")) {
                    Type type = new TypeToken<List<WebSight>>(){}.getType();
                    List<WebSight> sights = new Gson().fromJson(jsonData, type);
                    new Remote_Sight().refreshLocalSights(sights);
                }
                view.viewFinish();
                return;
            }
        Log.e(TAG, "在服务器上添加或保存" + menuname + "数据失败!");
    }

    /**
     * 取消 or 保存
     * @param item
     * @param activity
     * @param menuName
     * @return
     * @throws ParseException
     */
    @Override
    public boolean MenuChoice(MenuItem item, Activity activity, String menuName) throws ParseException {
        switch (item.getItemId()) {
            case 0:
                activity.setResult(RESULT_CANCELED);
                activity.finish();
                return true;
            case 1:
                if (view.checkInput()) {
                    if (view.savePGS(menuName)) {
                        activity.setResult(RESULT_OK);
                    } else {
                        Log.e(TAG, "在本地添加或保存" + menuName + "数据失败!");
                    }
                }
                return true;
        }
        return false;
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
}
