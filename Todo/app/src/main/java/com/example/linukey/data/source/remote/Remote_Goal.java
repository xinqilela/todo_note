package com.example.linukey.data.source.remote;

import android.util.Log;

import com.example.linukey.data.model.local.Goal;
import com.example.linukey.data.model.local.Project;
import com.example.linukey.data.model.local.SelfTask;
import com.example.linukey.data.model.remote.WebGoal;
import com.example.linukey.data.model.remote.WebProject;
import com.example.linukey.data.model.remote.WebSelfTask;
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

public class Remote_Goal {
    /**
     * 在服务器端保存相应的目标
     */
    public void saveGoal(final Goal goal, final RemoteObserver observer){
        final HttpUtils client = new HttpUtils();

        RequestParams params = new RequestParams();
        params.addBodyParameter("from", TodoHelper.From.get("client"));
        //把本地goal转化为webgoal
        WebGoal webGoal = new WebGoal(goal);
        params.addBodyParameter("client_goal", new Gson().toJson(webGoal));
        params.addBodyParameter("userId", TodoHelper.UserId);
        client.send(HttpMethod.POST, TodoHelper.RemoteUrl + "saveGoal", params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                ClientResult result = new Gson().fromJson(responseInfo.result, ClientResult.class);
                observer.Exeute(new RemoteObserverEvent(result, "saveGoal"));
            }
            @Override
            public void onFailure(HttpException e, String s) {
                ClientResult result = new ClientResult();
                result.setResult(false);
                result.setMessage("连接服务器失败!");
                observer.Exeute(new RemoteObserverEvent(result, "saveGoal"));
            }
        });
    }

    /**
     * 在服务器端更新相应的目标
     * @param goal
     * @param observer
     * @return
     */
    public void updateGoal(final Goal goal, final String opType, final RemoteObserver observer){
        final HttpUtils client = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("from", TodoHelper.From.get("client"));
        //把本地goal转化为webgoal
        WebGoal webGoal = new WebGoal(goal);
        params.addBodyParameter("client_goal", new Gson().toJson(webGoal));
        params.addBodyParameter("opType", opType);
        params.addBodyParameter("userId", TodoHelper.UserId);
        client.send(HttpMethod.POST, TodoHelper.RemoteUrl + "updateGoal", params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                ClientResult result = new Gson().fromJson(responseInfo.result, ClientResult.class);
                observer.Exeute(new RemoteObserverEvent(result, "updateGoal"));
            }
            @Override
            public void onFailure(HttpException e, String s) {
                ClientResult result = new ClientResult();
                result.setResult(false);
                result.setMessage("连接服务器失败!");
                observer.Exeute(new RemoteObserverEvent(result, "updateGoal"));
            }
        });
    }

    public String DoubleStringToWebId(Object o){
        return ((int)Double.parseDouble(o.toString()))+"";
    }

    /**
     * 获取所有目标
     * @param observer
     */
    public void getGoals(final RemoteObserver observer){
        HttpUtils client = new HttpUtils();
        RequestParams params = new RequestParams();

        //服务器客户端标志
        params.addBodyParameter("from", TodoHelper.From.get("client"));
        params.addBodyParameter("userId", TodoHelper.UserId);
        client.send(HttpMethod.POST, TodoHelper.RemoteUrl + "getGoals", params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        ClientResult result = new Gson().fromJson(responseInfo.result, ClientResult.class);
                        observer.Exeute(new RemoteObserverEvent(result, "goal"));
                    }
                    @Override
                    public void onFailure(HttpException e, String s) {
                        ClientResult result = new ClientResult();
                        result.setResult(false);
                        result.setMessage("连接服务器失败!");
                        observer.Exeute(new RemoteObserverEvent(result, "goal"));
                    }
                });
    }

    /**
     * 刷新本地目标
     * @param goals 从服务器获取的目标
     * @return
     */
    public boolean refreshLocalGoals(List<WebGoal> goals){
        if(goals != null){
            //这里删除的只是isAlter字段为0的，不能删除还没和服务器同步好的数据
            if(Goal.deleteAll(TodoHelper.getInstance())){
                for(WebGoal webGoal : goals){
                    Goal goal = new Goal(webGoal);
                    Goal.saveGoal(goal, TodoHelper.getInstance());
                }
                return true;
            }
            return false;
        }
        return true;
    }
}
