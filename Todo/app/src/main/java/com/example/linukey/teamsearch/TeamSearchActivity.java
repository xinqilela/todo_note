package com.example.linukey.teamsearch;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.linukey.data.model.local.TeamJoinInfo;
import com.example.linukey.data.model.remote.WebTeam;
import com.example.linukey.todo.Adapter.ListViewTeamSearchAdapter;
import com.example.linukey.todo.R;
import com.example.linukey.util.BaseActivity;
import com.example.linukey.util.TodoHelper;

import java.util.List;
import java.util.UUID;

/**
 * Created by linukey on 17-2-27.
 */

public class TeamSearchActivity extends BaseActivity implements TeamSearchContract.TeamSearchView,
        View.OnClickListener{

    TeamSearchContract.TeamSearchPresenter presenter;
    TextView searchContent;
    ListView searchResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_teamsearch);
        init();
    }

    public void init(){
        presenter = new TeamSearchPresenter(this);
        searchResult = (ListView)findViewById(R.id.searchResult);
        searchContent = (TextView)findViewById(R.id.content);
        searchContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                presenter.getTeamsByTeamName(searchContent.getText().toString().trim());
            }
        });
    }

    @Override
    public void setSearchResult(List<WebTeam> dataSource){
        ListViewTeamSearchAdapter listViewTeamSearchAdapter =
                new ListViewTeamSearchAdapter(this, dataSource, this);
        searchResult.setAdapter(listViewTeamSearchAdapter);
        searchResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(TeamSearchActivity.this, i + "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onClick_back(View view){
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.teamJoin:
                WebTeam team = presenter.getTeamByPosition((int)view.getTag());
                showJoinTeamDialog(team);
                break;
        }
    }

    @Override
    public void showJoinTeamDialog(final WebTeam team){
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_searchteam, null);
        final AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ad.setView(dialogView);
        ad.setPositiveButton("发送申请", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                EditText eContent = (EditText)dialogView.findViewById(R.id.content);
                if(eContent.getText().toString().trim().isEmpty()) {
                    Toast.makeText(TeamSearchActivity.this, "请输入申请留言!", Toast.LENGTH_SHORT).show();
                    showJoinTeamDialog(team);
                }else {//把申请留言信息存入服务器
                    TeamJoinInfo teamJoinInfo = new TeamJoinInfo();
                    teamJoinInfo.setToUserId(team.getLeaderId());
                    teamJoinInfo.setFromUserId(TodoHelper.UserId);
                    teamJoinInfo.setFromUserName(TodoHelper.UserName);
                    teamJoinInfo.setToUserName(team.getLeaderName());
                    teamJoinInfo.setTeamId(team.getTeamId());
                    teamJoinInfo.setExecType(TodoHelper.TeamJoinType.get("2"));
                    teamJoinInfo.setIsdelete("0");
                    teamJoinInfo.setTeamName(team.getTeamname());
                    teamJoinInfo.setContent(eContent.getText().toString().trim());
                    teamJoinInfo.setSelfId(UUID.randomUUID().toString());
                    presenter.saveJoinTeamInfoToWeb(teamJoinInfo);
                }
            }
        });
        ad.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        ad.create();
        ad.show();
    }
}