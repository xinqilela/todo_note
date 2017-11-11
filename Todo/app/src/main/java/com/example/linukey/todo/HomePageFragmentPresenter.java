package com.example.linukey.todo;

import android.content.Context;

import com.example.linukey.data.model.local.SelfTask;
import com.example.linukey.data.model.local.TaskAll;
import com.example.linukey.data.model.local.TaskClassify;
import com.example.linukey.data.model.local.TeamTask;
import com.example.linukey.util.TodoHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linukey on 17-3-9.
 */

public class HomePageFragmentPresenter implements IMainContract.HomePageFragmentPresenter {

    IMainContract.HomePageFragmentView view = null;
    List<TaskAll> dataSourceTasks = null;
    List<SelfTask> dataSourceSelfTasks = null;
    List<TeamTask> dataSourceTeamTasks = null;

    public HomePageFragmentPresenter(IMainContract.HomePageFragmentView view){
        this.view = view;
    }

    @Override
    public void initData(String type, Context context){
        dataSourceSelfTasks = SelfTask.getTasks(context);
        dataSourceTeamTasks = TeamTask.getTeamTasks(context);
        switch (type){
            case "completed":
                dataSourceTasks = getCODTasks(dataSourceSelfTasks, dataSourceTeamTasks, type);
                break;
            case "overtime":
                dataSourceTasks = getCODTasks(dataSourceSelfTasks, dataSourceTeamTasks, type);
                break;
            case "deleted":
                dataSourceTasks = getCODTasks(dataSourceSelfTasks, dataSourceTeamTasks, "isdelete");
                break;
            case "everyday":
                break;
        }
    }

    /**
     * 获取 完成 过期 删除 的任务
     * @param dataSourceSelfTasks
     * @param dataSourceTeamTasks
     * @param  taskType
     * @return
     */
    @Override
    public List<TaskAll> getCODTasks(List<SelfTask> dataSourceSelfTasks, List<TeamTask> dataSourceTeamTasks, String taskType){
        List<TaskAll> result = new ArrayList<>();

        if(dataSourceSelfTasks != null && dataSourceSelfTasks.size() > 0){
            for(SelfTask selfTask : dataSourceSelfTasks){
                if(taskType.equals("isdelete")){
                    if(selfTask.getIsdelete().equals("1"))
                        result.add(new TaskAll(selfTask.getTitle(), selfTask.getStarttime(),
                                selfTask.getEndtime(), selfTask.getId()+"", "self"));
                }
                else if(selfTask.getState().equals(taskType))
                    result.add(new TaskAll(selfTask.getTitle(), selfTask.getStarttime(),
                            selfTask.getEndtime(), selfTask.getId()+"", "self"));
            }
        }
        if(dataSourceTeamTasks != null && dataSourceTeamTasks.size() > 0){
            for(TeamTask teamTask : dataSourceTeamTasks){
                if(taskType.equals("isdelete")){
                    if(teamTask.getIsdelete().equals("1"))
                        result.add(new TaskAll(teamTask.getTitle(), teamTask.getStarttime(),
                                teamTask.getEndtime(), teamTask.getId()+"", "team"));
                }
                else if(teamTask.getState().equals(taskType))
                    result.add(new TaskAll(teamTask.getTitle(), teamTask.getStarttime(),
                            teamTask.getEndtime(), teamTask.getId()+"", "team"));
            }
        }

        return result;
    }

    @Override
    public int getCODTaskCnt(String taskType){
        int result = 0;
        if(dataSourceSelfTasks == null) dataSourceSelfTasks = SelfTask.getTasks(TodoHelper.getInstance());
        if(dataSourceTeamTasks == null) dataSourceTeamTasks = TeamTask.getTeamTasks(TodoHelper.getInstance());
        if(dataSourceSelfTasks != null && dataSourceSelfTasks.size() > 0){
            for(SelfTask selfTask : dataSourceSelfTasks){
                if(taskType.equals("isdelete")){
                    if(selfTask.getIsdelete().equals("1"))
                        result++;
                }
                else if(selfTask.getState().equals(taskType))
                        result++;
            }
        }
        if(dataSourceTeamTasks != null && dataSourceTeamTasks.size() > 0){
            for(TeamTask teamTask : dataSourceTeamTasks){
                if(taskType.equals("isdelete")){
                    if(teamTask.getIsdelete().equals("1"))
                        result++;
                }
                else if(teamTask.getState().equals(taskType))
                        result++;
            }
        }

        return result;
    }
}
