package com.example.linukey.data.source.remote;

import android.widget.Toast;

import com.example.linukey.data.model.local.Team;
import com.example.linukey.data.model.remote.WebTeam;
import com.example.linukey.util.TodoHelper;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import java.util.List;

/**
 * Created by linukey on 12/20/16.
 */

public class Remote_Team {

    public void updateTeam(final Team team, final String opType, final RemoteObserver observer) {
        final HttpUtils client = new HttpUtils();
        RequestParams params = new RequestParams();

        //服务器客户端标志
        params.addBodyParameter("from", TodoHelper.From.get("client"));
        //将本地team转换为webteam
        WebTeam webteam = new WebTeam(team);
        params.addBodyParameter("client_team", new Gson().toJson(webteam));
        params.addBodyParameter("userId", TodoHelper.UserId);
        params.addBodyParameter("opType", opType);
        client.send(HttpMethod.POST, TodoHelper.RemoteUrl + "updateTeam", params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        ClientResult result = new Gson().fromJson(responseInfo.result, ClientResult.class);
                        observer.Exeute(new RemoteObserverEvent(result, "updateTeam"));
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        ClientResult result = new ClientResult();
                        result.setResult(false);
                        result.setMessage("连接服务器失败!");
                        observer.Exeute(new RemoteObserverEvent(result, "updateTeam"));
                    }
                });
    }

    /**
     * 在服务器上保存小组数据
     * @param team
     * @param observer
     */
    public void saveTeam(final Team team, final RemoteObserver observer) {
        final HttpUtils client = new HttpUtils();
        RequestParams params = new RequestParams();

        //服务器客户端标志
        params.addBodyParameter("from", TodoHelper.From.get("client"));
        //将本地team转换为webteam
        WebTeam webteam = new WebTeam(team);
        params.addBodyParameter("client_team", new Gson().toJson(webteam));
        params.addBodyParameter("userId", TodoHelper.UserId);
        client.send(HttpMethod.POST, TodoHelper.RemoteUrl + "saveTeam", params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        ClientResult result = new Gson().fromJson(responseInfo.result, ClientResult.class);
                        observer.Exeute(new RemoteObserverEvent(result, "saveTeam"));
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        ClientResult result = new ClientResult();
                        result.setResult(false);
                        result.setMessage("连接服务器失败!");
                        observer.Exeute(new RemoteObserverEvent(result, "saveTeam"));
                    }
                });
    }

    /**
     * 通过userId从web上获取用户小组信息
     * @param observer
     */
    public void getTeams(final String userId, final RemoteObserver observer){
        final HttpUtils client = new HttpUtils();
        RequestParams params = new RequestParams();

        //服务器客户端标志
        params.addBodyParameter("from", TodoHelper.From.get("client"));
        params.addBodyParameter("userId", userId);
        client.send(HttpMethod.POST, TodoHelper.RemoteUrl + "getTeams", params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        ClientResult result = new Gson().fromJson(responseInfo.result, ClientResult.class);
                        observer.Exeute(new RemoteObserverEvent(result, "team"));
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        ClientResult result = new ClientResult();
                        result.setResult(false);
                        result.setMessage("连接服务器失败!");
                        observer.Exeute(new RemoteObserverEvent(result, "team"));
                    }
                });
    }

    /**
     * 通过小组名称来从web上搜索所有满足要求的小组信息
     * @param teamName
     * @param observer
     */
    public void getTeamsByTeamName(final String teamName, final RemoteObserver observer){
        final HttpUtils client = new HttpUtils();
        RequestParams params = new RequestParams();

        //服务器客户端标志
        params.addBodyParameter("from", TodoHelper.From.get("client"));
        params.addBodyParameter("teamName", teamName);
        params.addBodyParameter("userId", TodoHelper.UserId);
        client.send(HttpMethod.POST, TodoHelper.RemoteUrl + "getTeamsByTeamName", params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        ClientResult result = new Gson().fromJson(responseInfo.result, ClientResult.class);
                        observer.Exeute(new RemoteObserverEvent(result, "getTeamsByTeamName"));
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        ClientResult result = new ClientResult();
                        result.setResult(false);
                        result.setMessage("连接服务器失败!");
                        observer.Exeute(new RemoteObserverEvent(result, "getTeamsByTeamName"));
                    }
                });
    }

    /**
     * 刷新本地小组信息
     * @param teams 从服务器获取的小组信息
     * @return
     */
    public boolean refreshLocalTeams(List<WebTeam> teams){
        if(teams != null){
            //这里删除的只是isAlter字段为0的，不能删除还没和服务器同步好的数据
            if(Team.deleteAll(TodoHelper.getInstance())){
                for(WebTeam webTeam : teams){
                    Team team = new Team(webTeam);
                    Team.saveTeam(team, TodoHelper.getInstance());
                }
                return true;
            }
            return false;
        }
        return true;
    }
}
