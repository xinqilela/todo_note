package com.example.linukey.selftask;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.example.linukey.launch.LaunchPresenter;
import com.example.linukey.todo.Adapter.ListViewSelfTaskAdapter;
import com.example.linukey.todo.SwipeMenu.SwipeMenu;
import com.example.linukey.todo.SwipeMenu.SwipeMenuListView;
import com.example.linukey.todo.R;
import com.example.linukey.data.model.local.SelfTask;
import com.example.linukey.util.BaseActivity;

import java.util.List;

/**
 * Created by linukey on 12/2/16.
 */

public class SelfTaskActivity extends BaseActivity implements SelfTaskContract.ActivityView, SwipeRefreshLayout.OnRefreshListener{
    SwipeMenuListView listViewTask;
    String menuName = null;
    //空间分类时当前选择的PGS
    String PGSId = null;
    String actionBarTitle = null;
    final int addSelfTask_ResultCode = 1;
    SwipeRefreshLayout freshLayout = null;
    SelfTaskContract.ActivityPresenter selfTaskPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selftask);
        init();
        initActionBar();
    }

    public void initActionBar(){
        ActionBar actionBar = getActionBar();
        if(menuName.equals("today")) actionBar.setTitle("今日待办");
        if(menuName.equals("tomorrow")) actionBar.setTitle("明日待办");
        if(menuName.equals("next")) actionBar.setTitle("下一步行动");
        if(menuName.equals("box")) actionBar.setTitle("备忘箱");
        if(menuName.equals("project") || menuName.equals("sight") || menuName.equals("goal"))
            actionBar.setTitle(actionBarTitle);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        int titleId = Resources.getSystem().getIdentifier("action_bar_title",
                "id", "android");
        TextView textView = (TextView)findViewById(titleId);
        textView.setTextColor(Color.parseColor("#F2F1F0"));
        textView.setTextSize(16);
    }

    @Override
    public void showSelfTaskDialogFragment(int position){
        SelfTask selfTask = selfTaskPresenter.getCurrentTask(position);
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_selftask, null);
        final AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ((TextView)dialogView.findViewById(R.id.title)).setText(selfTask.getTitle());
        ((TextView)dialogView.findViewById(R.id.content)).setText(selfTask.getContent());
        if(!menuName.equals("box")){
            ((TextView)dialogView.findViewById(R.id.project)).setText(selfTaskPresenter.getTaskProjectTitle(selfTask.getProjectId()));
            ((TextView)dialogView.findViewById(R.id.goal)).setText(selfTaskPresenter.getTaskGoalTitle(selfTask.getGoalId()));
            ((TextView)dialogView.findViewById(R.id.sight)).setText(selfTaskPresenter.getTaskSightTitle(selfTask.getSightId()));
            ((TextView)dialogView.findViewById(R.id.starttime)).setText(selfTask.getStarttime());
            ((TextView)dialogView.findViewById(R.id.endtime)).setText(selfTask.getEndtime());
            ((TextView)dialogView.findViewById(R.id.clocktime)).setText(selfTask.getClocktime());
        }
        ad.setView(dialogView);
        ad.create();
        ad.show();
    }

    @Override
    public void showEditTask(Intent intent){
        startActivityForResult(intent, addSelfTask_ResultCode);
    }

    public void initRefreshLayout(){
        freshLayout = (SwipeRefreshLayout)findViewById(R.id.activity_selftask);
        freshLayout.setOnRefreshListener(this);
        freshLayout.setDistanceToTriggerSync(400);
    }

    public void init() {
        initRefreshLayout();
        Intent intent = getIntent();
        menuName = intent.getStringExtra("menuname");
        if(menuName.equals("project") || menuName.equals("goal") || menuName.equals("sight")) {
            PGSId = intent.getStringExtra("PGSId");
            actionBarTitle = intent.getStringExtra("actionBarTitle");
        }
        selfTaskPresenter = new SelfTaskPresenter(this);
        listViewTask = (SwipeMenuListView) findViewById(R.id.listview_selftask);
        listViewTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showSelfTaskDialogFragment(position);
            }
        });
        listViewTask.setMenuCreator(selfTaskPresenter.getSwipeMenuCreator());
        listViewTask.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(final int position, final SwipeMenu menu, int index) {
                switch (index){
                    case 0:
                        selfTaskPresenter.editTask(position, SelfTaskActivity.this);
                        break;
                    case 1:
                        selfTaskPresenter.deleteTask(position, menuName, SelfTaskActivity.this);
                        selfTaskPresenter.initData(menuName, PGSId, SelfTaskActivity.this);
                        break;
                    case 2:
                        selfTaskPresenter.completedTask(position, menuName, SelfTaskActivity.this);
                        selfTaskPresenter.initData(menuName, PGSId, SelfTaskActivity.this);
                        break;
                    default:
                        break;
                }
            }
        });

        selfTaskPresenter.initData(menuName, PGSId, this);
    }

    @Override
    public void showAfterDataSourceChanged(List<SelfTask> selfTasks){
        ListViewSelfTaskAdapter lva = new ListViewSelfTaskAdapter(this, selfTasks, menuName);
        listViewTask.setAdapter(lva);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == addSelfTask_ResultCode) {
            selfTaskPresenter.initData(menuName, PGSId, this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        selfTaskPresenter.CreateMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            case 0:
                selfTaskPresenter.addSelfTask(this);
                break;
        }
        return true;
    }

    @Override
    public void showAddTask(Intent intent){
        startActivityForResult(intent, addSelfTask_ResultCode);
    }

    @Override
    public void onRefresh() {
        new LaunchPresenter(null).initLocalSelfTasksFromWeb(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 停止刷新
                freshLayout.setRefreshing(false);
                selfTaskPresenter.initData(menuName, PGSId, SelfTaskActivity.this);
            }
        }, 2000);
    }
}