package com.example.linukey.notification;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.linukey.data.model.local.TeamJoinInfo;
import com.example.linukey.data.source.remote.Remote_Notification;
import com.example.linukey.todo.Adapter.ListViewTeamJoinInfoAdapter;
import com.example.linukey.todo.R;
import com.example.linukey.todo.SwipeMenu.SwipeMenu;
import com.example.linukey.todo.SwipeMenu.SwipeMenuListView;
import com.example.linukey.util.BaseActivity;
import com.example.linukey.util.TodoHelper;

import java.util.List;

/**
 * Created by linukey on 17-2-28.
 */

/**
 * 系统的各种通知：
 * 1.加入小组申请
 * 2.app升级通知
 * 3.任务提醒通知
 */
public class NotificationActivity extends BaseActivity
        implements NotificationContract.NotificationView, View.OnClickListener {

    NotificationContract.NotificationPresenter presenter;
    private SwipeMenuListView swipeMenuListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        init();
        initActionBar();
    }



    public void init(){
        presenter = new NotificationPresenter(this);
        swipeMenuListView = (SwipeMenuListView)findViewById(R.id.listview_teamjoindata);
        swipeMenuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int pisition, long l) {
                TeamJoinInfo teamJoinInfo = presenter.getTeamJoinInfoByPosition(pisition);
                if(teamJoinInfo.getExecType().equals(TodoHelper.TeamJoinType.get("2"))) {
                    final View dialogView = LayoutInflater.from(NotificationActivity.this).inflate(R.layout.dialog_teamjoininfo, null);
                    final AlertDialog.Builder ad = new AlertDialog.Builder(NotificationActivity.this);
                    ad.setView(dialogView);
                    TextView teamName = (TextView) dialogView.findViewById(R.id.teamName);
                    TextView userName = (TextView) dialogView.findViewById(R.id.userName);
                    TextView content = (TextView) dialogView.findViewById(R.id.content);
                    teamName.setText(teamJoinInfo.getTeamName());
                    userName.setText(teamJoinInfo.getFromUserName());
                    content.setText(teamJoinInfo.getContent());
                    ad.setPositiveButton("同意", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            presenter.agreeSomeOneToJoinTeam(pisition);
                        }
                    });
                    ad.setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            presenter.refuseSomeOneToJoinTeam(pisition);
                        }
                    });
                    ad.create();
                    ad.show();
                }
            }
        });
        swipeMenuListView.setMenuCreator(presenter.getSwipeMenuCreator());
        swipeMenuListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index){
                    case 0:
                        presenter.deleteTeamJoinInfo(position);
                        break;
                }
            }
        });
        presenter.initData();
    }

    @Override
    public void setDataToListView(List<TeamJoinInfo> dataSource){
        ListViewTeamJoinInfoAdapter listViewTeamJoinInfoAdapter =
                new ListViewTeamJoinInfoAdapter(TodoHelper.getInstance(), dataSource);
        listViewTeamJoinInfoAdapter.setAgreeOnClick(this);
        swipeMenuListView.setAdapter(listViewTeamJoinInfoAdapter);
    }

    public void initActionBar(){
        ActionBar actionBar = getActionBar();
        actionBar.setTitle("消息中心");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        int titleId = Resources.getSystem().getIdentifier("action_bar_title",
                "id", "android");
        TextView textView = (TextView)findViewById(titleId);
        textView.setTextColor(Color.parseColor("#F2F1F0"));
        textView.setTextSize(16);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.result:
                int position = (int)view.getTag();
                if(presenter.getTeamJoinInfoByPosition(position).getExecType().equals(TodoHelper.TeamJoinType.get("2")))
                    presenter.agreeSomeOneToJoinTeam(position);
                break;
        }
    }
}
