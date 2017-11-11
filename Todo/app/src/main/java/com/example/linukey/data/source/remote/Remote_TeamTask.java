package com.example.linukey.data.source.remote;

import com.example.linukey.data.model.local.TeamTask;
import com.example.linukey.data.model.remote.WebTeamTask;
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

public class Remote_TeamTask {
    /**
     * 保存服务器端相应的团队任务
     * @param teamTask
     * @param observer
     */
    public void saveTeamTask(final TeamTask teamTask, final RemoteObserver observer) {
        final HttpUtils client = new HttpUtils();
        RequestParams params = new RequestParams();

        //服务器客户端标志
        params.addBodyParameter("from", TodoHelper.From.get("client"));
        //将本地teamtask转换为webTeamtask
        WebTeamTask webTeamTask = new WebTeamTask(teamTask);
        params.addBodyParameter("client_teamtask", new Gson().toJson(webTeamTask));
        params.addBodyParameter("userId", TodoHelper.UserId);
        client.send(HttpMethod.POST, TodoHelper.RemoteUrl + "saveTeamTask", params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        ClientResult result = new Gson().fromJson(responseInfo.result, ClientResult.class);
                        observer.Exeute(new RemoteObserverEvent(result, "saveTeamTask"));
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        ClientResult result = new ClientResult();
                        result.setResult(false);
                        result.setMessage("连接服务器失败!");
                        observer.Exeute(new RemoteObserverEvent(result, "saveTeamTask"));
                    }
                });
    }

    /**
     * 修改服务器端相应的个人任务
     * @param teamTask
     * @param observer
     */
    public void updateTeamTask(final TeamTask teamTask, final RemoteObserver observer){
        HttpUtils client = new HttpUtils();
        RequestParams params = new RequestParams();
        //服务器客户端标志
        params.addBodyParameter("from", TodoHelper.From.get("client"));
        //将本地的selftask转化为webselftask
        WebTeamTask webTeamTask = new WebTeamTask(teamTask);
        params.addBodyParameter("client_teamtask", new Gson().toJson(webTeamTask));
        params.addBodyParameter("userId", TodoHelper.UserId);
        client.send(HttpMethod.POST, TodoHelper.RemoteUrl + "updateTeamTask", params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        ClientResult result = new Gson().fromJson(responseInfo.result, ClientResult.class);
                        observer.Exeute(new RemoteObserverEvent(result, "updateTeamTask"));
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        ClientResult result = new ClientResult();
                        result.setResult(false);
                        result.setMessage("连接服务器失败!");
                        observer.Exeute(new RemoteObserverEvent(result, "updateTeamTask"));
                    }
                });
    }

    /**
     * 从web获取所有此用户的小组任务
     */
    public void getTeamTasks(final RemoteObserver observer){
        HttpUtils client = new HttpUtils();
        RequestParams params = new RequestParams();

        //服务器客户端标志
        params.addBodyParameter("from", TodoHelper.From.get("client"));
        params.addBodyParameter("userId", TodoHelper.UserId);
        client.send(HttpMethod.POST, TodoHelper.RemoteUrl + "getTeamTasks", params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        ClientResult result = new Gson().fromJson(responseInfo.result, ClientResult.class);
                        observer.Exeute(new RemoteObserverEvent(result, "getTeamTasks"));
                    }
                    @Override
                    public void onFailure(HttpException e, String s) {
                        ClientResult result = new ClientResult();
                        result.setResult(false);
                        result.setMessage("连接服务器失败!");
                        observer.Exeute(new RemoteObserverEvent(result, "getTeamTasks"));
                    }
                });
    }

    /**
     * 刷新本地个人任务
     * @param tasks 从服务器获取的小组任务
     * @return
     */
    public boolean refreshLocalTeamTasks(List<WebTeamTask> tasks){
        if(tasks != null){
            //这里删除的只是isAlter字段为0的，不能删除还没和服务器同步好的数据
            if(TeamTask.deleteAll(TodoHelper.getInstance())){
                for(WebTeamTask webTeamTask : tasks){
                    TeamTask teamTask = new TeamTask(webTeamTask);
                    TeamTask.saveTeamTask(teamTask, TodoHelper.getInstance());
                }
                return true;
            }
            return false;
        }
        return true;
    }
}
