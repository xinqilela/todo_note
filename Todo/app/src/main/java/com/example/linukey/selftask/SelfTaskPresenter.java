package com.example.linukey.selftask;

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
import com.example.linukey.addedit_selftask.AddEditSelfTaskActivity;
import com.example.linukey.data.source.remote.RemoteObserver;
import com.example.linukey.data.source.remote.RemoteObserverEvent;
import com.example.linukey.data.source.remote.Remote_SelfTask;
import com.example.linukey.todo.SwipeMenu.SwipeMenu;
import com.example.linukey.todo.SwipeMenu.SwipeMenuCreator;
import com.example.linukey.todo.SwipeMenu.SwipeMenuItem;
import com.example.linukey.data.model.local.Goal;
import com.example.linukey.data.model.local.Project;
import com.example.linukey.data.model.local.SelfTask;
import com.example.linukey.data.model.local.Sight;
import com.example.linukey.todo.R;
import com.example.linukey.util.TodoHelper;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by linukey on 12/8/16.
 */

public class SelfTaskPresenter implements SelfTaskContract.ActivityPresenter, RemoteObserver{
    private final SelfTaskContract.ActivityView selfTaskView;
    private List<SelfTask> datesourceTasks = null;
    private List<SelfTask> datesourceCurrent = null;
    private List<Project> projectList = null;
    private List<Goal> goalList = null;
    private List<Sight> sightList = null;
    private String PGSId = null;
    private String menuName = null;

    public SelfTaskPresenter(SelfTaskContract.ActivityView selfTaskView){
        this.selfTaskView = selfTaskView;
    }

    /**
     * 初始化数据
     * @param menuName
     * @param context
     */
    @Override
    public void initData(String menuName, String PGSId, Context context) {
        this.PGSId = PGSId;
        this.menuName = menuName;
        datesourceTasks = SelfTask.getTasks(context);
        switch (menuName) {
            case "today":
                    datesourceCurrent = getTodayDate();
                break;
            case "tomorrow":
                    datesourceCurrent = getTomorrowDate();
                break;
            case "next":
                    datesourceCurrent = getNextDate();
                break;
            case "box":
                    datesourceCurrent = getBoxDate();
                break;
            case "project":
            case "goal":
            case "sight":
                    datesourceCurrent = getSelfTaskByPGSId(menuName, PGSId);
                break;
            default:
                break;
        }
        if(datesourceCurrent != null)
            selfTaskView.showAfterDataSourceChanged(datesourceCurrent);
        else
            selfTaskView.showAfterDataSourceChanged(new ArrayList<SelfTask>());
    }

    @Override
    public List<SelfTask> getSelfTaskByPGSId(String menuName, String PGSId){
        List<SelfTask> result = null;
        if(datesourceTasks != null && datesourceTasks.size() > 0){
            result = new ArrayList<>();
            for(SelfTask selfTask : datesourceTasks){
                if(menuName.equals("project")
                        && selfTask.getProjectId() != null
                        && selfTask.getProjectId().equals(PGSId)
                        && !isBoxTaskOrOvertimeOrCompleteOrDelete(selfTask))
                    result.add(selfTask);
                else if(menuName.equals("sight")
                        && selfTask.getSightId() != null
                        && selfTask.getSightId().equals(PGSId)
                        && !isBoxTaskOrOvertimeOrCompleteOrDelete(selfTask))
                    result.add(selfTask);
                else if(menuName.equals("goal")
                        && selfTask.getGoalId() != null
                        && selfTask.getGoalId().equals(PGSId)
                        && !isBoxTaskOrOvertimeOrCompleteOrDelete(selfTask))
                    result.add(selfTask);
            }
        }
        return result;
    }

    /**
     * 获得今日任务数据
     * @return List<SelfTask>
     */
    @Override
    public List<SelfTask> getTodayDate() {
        List<SelfTask> result = null;
        if(datesourceTasks != null && datesourceTasks.size() >0){
            result = new ArrayList<>();
            Date today = getDateToday();
            SimpleDateFormat sdt = new SimpleDateFormat("yyyy-MM-dd");
            for(SelfTask selfTask : datesourceTasks){
                try {
                    if(!isBoxTaskOrOvertimeOrCompleteOrDelete(selfTask) &&
                            today.getTime() >= sdt.parse(selfTask.getStarttime()).getTime()
                            && today.getTime() <= sdt.parse(selfTask.getEndtime()).getTime()){
                        result.add(selfTask);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 获得明日任务数据
     * @return
     * @throws ParseException
     */
    @Override
    public List<SelfTask> getTomorrowDate(){
        List<SelfTask> result = null;
        if(datesourceTasks != null && datesourceTasks.size() > 0){
            result = new ArrayList<>();
            SimpleDateFormat sdt = new SimpleDateFormat("yyyy-MM-dd");
            Date tomorrow = getNextDay(getDateToday());
            for(SelfTask selfTask : datesourceTasks){
                try {
                    if(!isBoxTaskOrOvertimeOrCompleteOrDelete(selfTask) &&
                            tomorrow.getTime() >= sdt.parse(selfTask.getStarttime()).getTime()
                            && tomorrow.getTime() <= sdt.parse(selfTask.getEndtime()).getTime()){
                        result.add(selfTask);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 获得下一步行动任务数据
     * @return
     * @throws ParseException
     */
    @Override
    public List<SelfTask> getNextDate() {
        List<SelfTask> result = null;
        if(datesourceTasks != null && datesourceTasks.size() >0){
            result = new ArrayList<>();
            SimpleDateFormat sdt = new SimpleDateFormat("yyyy-MM-dd");
            Date tomorrow = getNextDay(getDateToday());
            for(SelfTask selfTask : datesourceTasks){
                try {
                    if(!isBoxTaskOrOvertimeOrCompleteOrDelete(selfTask) &&
                            tomorrow.getTime() < sdt.parse(selfTask.getStarttime()).getTime()){
                        result.add(selfTask);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 获得备忘箱任务数据
     * @return
     * @throws ParseException
     */
    @Override
    public List<SelfTask> getBoxDate(){
        List<SelfTask> result = null;
        if(datesourceTasks != null && datesourceTasks.size() > 0){
            result = new ArrayList<>();
            for(SelfTask selfTask : datesourceTasks){
                if(Integer.parseInt(selfTask.getIsTmp()) == 1){
                    result.add(selfTask);
                }
            }
        }
        return result;
    }

    /**
     * 检查任务是否符合要求
     * @param selfTask
     * @return
     */
    @Override
    public boolean isBoxTaskOrOvertimeOrCompleteOrDelete(SelfTask selfTask){
        if(Integer.parseInt(selfTask.getIsTmp()) == 1)
            return true;
        if(!selfTask.getState().equals(TodoHelper.TaskState.get("noComplete")))
            return true;
        if(selfTask.isdelete().equals("1"))
            return true;

        return false;
    }

    /**
     * 获得今天的日期
     * @return
     * @throws ParseException
     */
    @Override
    public Date getDateToday(){
        Date date = new Date();
        SimpleDateFormat sdt = new SimpleDateFormat("yyyy-MM-dd");
        Date today = null;
        try {
            today = sdt.parse(date.getYear()+1900+"-"
                    + (date.getMonth()+1) + "-" + date.getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return today;
    }

    /**
     * 获得明天的日期
     * @param date
     * @return
     */
    @Override
    public Date getNextDay(Date date){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动
        date=calendar.getTime();   //这个时间就是日期往后推一天的结果
        return date;
    }

    /**
     * 添加个人任务
     * @param context
     */
    @Override
    public void addSelfTask(Context context){
        Intent addSelfTask = new Intent(context, AddEditSelfTaskActivity.class);
        selfTaskView.showAddTask(addSelfTask);
    }

    @Override
    public void CreateMenu(Menu menu){
        MenuItem taskAdd = menu.add(0,0,0, "添加任务");
        taskAdd.setIcon(R.mipmap.add);
        taskAdd.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    }

    /**
     * 获得当前点击的任务
     * @param position
     * @return
     */
    @Override
    public SelfTask getCurrentTask(int position){
        return datesourceCurrent.get(position);
    }

    /**
     * 获得任务目标标题
     * @param goalId
     * @return
     */
    @Override
    public String getTaskGoalTitle(String goalId) {
        goalList = Goal.getGoals(TodoHelper.getInstance());
        String goalTitle = null;
        if(goalId != null){
            for(Goal g : goalList){
                if(g.getSelfId().equals(goalId))
                    goalTitle = g.getTitle();
            }
        }
        return goalTitle;
    }

    /**
     * 获得任务项目标题
     * @param projectId
     * @return
     */
    @Override
    public String getTaskProjectTitle(String projectId){
        projectList = Project.getProjects("self", TodoHelper.getInstance());
        String projectTitle = null;
        if(projectId != null){
            for(Project p : projectList){
                if(p.getSelfId().equals(projectId))
                    projectTitle = p.getTitle();
            }
        }
        return projectTitle;
    }

    /**
     * 获得任务情景标题
     * @param sightId
     * @return
     */
    @Override
    public String getTaskSightTitle(String sightId){
        sightList = Sight.getSights(TodoHelper.getInstance());
        String sightTitle = null;
        if(sightId != null){
            for(Sight s : sightList){
                if(s.getSelfId().equals(sightId))
                    sightTitle = s.getTitle();
            }
        }
        return sightTitle;
    }

    /**
     * 任务左划菜单
     * @return
     */
    @Override
    public SwipeMenuCreator getSwipeMenuCreator() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem EditItem = new SwipeMenuItem(TodoHelper.getInstance());
                EditItem.setBackground(new ColorDrawable(Color.parseColor("#C7C6CC")));
                EditItem.setWidth(200);
                EditItem.setTitle("编辑");
                EditItem.setTitleSize(18);
                EditItem.setTitleColor(Color.WHITE);
                EditItem.setId(0);
                menu.addMenuItem(EditItem);

                SwipeMenuItem deleteItem = new SwipeMenuItem(TodoHelper.getInstance());
                deleteItem.setId(1);
                deleteItem.setBackground(new ColorDrawable(Color.parseColor("#FF2730")));
                deleteItem.setWidth(200);
                deleteItem.setTitle("删除");
                deleteItem.setTitleSize(18);
                deleteItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(deleteItem);

                SwipeMenuItem completeItem = new SwipeMenuItem(TodoHelper.getInstance());
                completeItem.setBackground(new ColorDrawable(Color.parseColor("#FF9700")));
                completeItem.setWidth(200);
                completeItem.setId(2);
                completeItem.setTitle("完成");
                completeItem.setTitleSize(18);
                completeItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(completeItem);
            }
        };
        return creator;
    }

    /**
     * 编辑任务
     * @param position
     * @param context
     */
    @Override
    public void editTask(int position, Context context){
        SelfTask selfTask = datesourceCurrent.get(position);
        Intent intent = new Intent(context, AddEditSelfTaskActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("date", selfTask);
        intent.putExtra("bundle", bundle);
        selfTaskView.showEditTask(intent);
    }

    /**
     * 删除任务
     * @param position
     * @param menuName
     * @param context
     */
    @Override
    public void deleteTask(final int position, final String menuName, final Context context) {
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
                SelfTask selfTask = datesourceCurrent.get(position);
                selfTask.setIsdelete("1");
                new Remote_SelfTask().updateSelfTask(selfTask, SelfTaskPresenter.this);
            }
        });
        adDel.create();
        adDel.show();
    }

    /**
     * 完成任务
     * @param position
     * @param menuName
     * @param context
     */
    @Override
    public void completedTask(final int position, final String menuName, final Context context) {
        AlertDialog.Builder adCom = new AlertDialog.Builder(context);
        adCom.setMessage("是否已经完成?");
        adCom.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        adCom.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SelfTask selfTask = datesourceCurrent.get(position);
                selfTask.setState(TodoHelper.TaskState.get("complete"));
                new Remote_SelfTask().updateSelfTask(selfTask, SelfTaskPresenter.this);
            }
        });
        adCom.create();
        adCom.show();
    }

    /**
     * 如果在服务器端删除或者成功，修改本地的isAlter为0
     * @param remoteObserverEvent
     */
    @Override
    public void Exeute(RemoteObserverEvent remoteObserverEvent) {
        if(remoteObserverEvent.object.toString().equals("updateOrdelete")){
            if(remoteObserverEvent.clientResult.isResult()){
                SelfTask selfTask = (SelfTask)remoteObserverEvent.clientResult.getObject();
                SelfTask.updateTaskInfo(selfTask, TodoHelper.getInstance());
                initData(menuName, PGSId, TodoHelper.getInstance());
                return;
            }
        }
        Toast.makeText(TodoHelper.getInstance(), remoteObserverEvent.clientResult.getMessage(),
                Toast.LENGTH_SHORT).show();
    }
}
