package com.example.linukey.selfpgs;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.linukey.launch.LaunchPresenter;
import com.example.linukey.selftask.SelfTaskActivity;
import com.example.linukey.teamtask.TeamTaskActivity;
import com.example.linukey.todo.Adapter.ListViewSelfPGSAdapter;
import com.example.linukey.todo.SwipeMenu.SwipeMenu;
import com.example.linukey.todo.SwipeMenu.SwipeMenuListView;
import com.example.linukey.todo.R;
import com.example.linukey.data.model.local.TaskClassify;
import com.example.linukey.util.BaseActivity;
import com.example.linukey.util.TodoHelper;

import java.util.List;

/**
 * Created by linukey on 12/4/16.
 */

public class SelfPGSActivity extends BaseActivity implements SelfPGSContract.SelfPGSActivityView,
        SwipeRefreshLayout.OnRefreshListener {

    SwipeMenuListView listViewPGS = null;
    String menuName = null;
    final int addSelfPGS_ResultCode = 2;
    SelfPGSContract.SelfPGSActivityPresenter selfPGSActivityPresenter;
    SwipeRefreshLayout freshLayout = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selfpgs);

        init();
        initActionBar();
    }

    public void initRefreshLayout() {
        freshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_selfpgs);
        freshLayout.setOnRefreshListener(this);
        freshLayout.setDistanceToTriggerSync(400);
    }

    public void init() {
        initRefreshLayout();
        Intent intent = getIntent();
        menuName = intent.getStringExtra("menuname");
        selfPGSActivityPresenter = new SelfPGSPresenter(this);
        listViewPGS = (SwipeMenuListView) findViewById(R.id.listview_selfpgs);
        listViewPGS.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (menuName.equals("project") || menuName.equals("goal") || menuName.equals("sight")) {
                    Intent intent = new Intent(SelfPGSActivity.this, SelfTaskActivity.class);
                    intent.putExtra("menuname", menuName);
                    intent.putExtra("actionBarTitle", selfPGSActivityPresenter.getPGSIdByPosition(menuName, i).getTitle());
                    intent.putExtra("PGSId", selfPGSActivityPresenter.getPGSIdByPosition(menuName, i).getSelfId());
                    startActivity(intent);
                } else if (menuName.equals("teamProject")) {
                    Intent intent = new Intent(SelfPGSActivity.this, TeamTaskActivity.class);
                    intent.putExtra("menuname", menuName);
                    intent.putExtra("actionBarTitle", selfPGSActivityPresenter.getPGSIdByPosition(menuName, i).getTitle());
                    intent.putExtra("projectId", selfPGSActivityPresenter.getPGSIdByPosition(menuName, i).getSelfId());
                    startActivity(intent);
                }
            }
        });
        listViewPGS.setMenuCreator(selfPGSActivityPresenter.getSwipeMenuCreator(menuName));
        listViewPGS.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position, SwipeMenu menu, int index) {
                if (menuName.equals("teamProject") && !selfPGSActivityPresenter.isValidUser(position)) {
                    Toast.makeText(SelfPGSActivity.this, "团队项目只能由该组组长编辑!", Toast.LENGTH_SHORT).show();
                    return;
                }
                switch (index) {
                    case 0:
                        selfPGSActivityPresenter.editPGS(menuName, position, SelfPGSActivity.this);
                        break;
                    case 1:
                        selfPGSActivityPresenter.deletePGS(menuName, position, SelfPGSActivity.this);
                        break;
                    case 2:
                        selfPGSActivityPresenter.completedPGS(menuName, position, SelfPGSActivity.this);
                        break;
                }
            }
        });
        selfPGSActivityPresenter.initData(menuName, this);
    }

    @Override
    public void onRefresh() {
        if (menuName.equals("project")) {
            new LaunchPresenter(null).initLocalProjectsFromWeb(TodoHelper.getInstance());
        } else if (menuName.equals("goal")) {
            new LaunchPresenter(null).initLocalGoalsFromWeb(TodoHelper.getInstance());
        } else if (menuName.equals("sight")) {
            new LaunchPresenter(null).initLocalSightsFromWeb(TodoHelper.getInstance());
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 停止刷新
                freshLayout.setRefreshing(false);
                selfPGSActivityPresenter.initData(menuName, SelfPGSActivity.this);
            }
        }, 2000);
    }

    @Override
    public void showEditPGS(Intent intent) {
        startActivityForResult(intent, addSelfPGS_ResultCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == addSelfPGS_ResultCode) {
            selfPGSActivityPresenter.initData(menuName, SelfPGSActivity.this);
        }
    }

    public void initActionBar() {
        ActionBar actionBar = getActionBar();
        if (menuName.equals("project")) actionBar.setTitle("项目");
        if (menuName.equals("goal")) actionBar.setTitle("目标");
        if (menuName.equals("sight")) actionBar.setTitle("情景");
        if (menuName.equals("teamProject")) actionBar.setTitle("团队项目");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        int titleId = Resources.getSystem().getIdentifier("action_bar_title",
                "id", "android");
        TextView textView = (TextView) findViewById(titleId);
        textView.setTextColor(Color.parseColor("#F2F1F0"));
        textView.setTextSize(16);
    }

    @Override
    public void showDataSourceChanged(List<TaskClassify> dateSource) {
        ListViewSelfPGSAdapter listViewSelfPGSAdapter = new ListViewSelfPGSAdapter(this, dateSource);
        listViewPGS.setAdapter(listViewSelfPGSAdapter);
    }

    @Override
    public void showAddSelfPGS(Intent intent) {
        startActivityForResult(intent, addSelfPGS_ResultCode);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        selfPGSActivityPresenter.CreateMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case 0:
                selfPGSActivityPresenter.addSelfPGS(menuName, this);
                break;
        }
        return true;
    }
}
