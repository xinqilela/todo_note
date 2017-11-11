package com.example.linukey.data.source.remote;

import android.util.Log;
import android.widget.Toast;

import com.example.linukey.data.model.local.SelfTask;
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

public class Remote_SelfTask {

    /**
     * 保存服务器端相应的个人任务
     * @param selfTask
     * @param observer
     */
    public void saveSelfTask(final SelfTask selfTask, final RemoteObserver observer) {
        final HttpUtils client = new HttpUtils();
        RequestParams params = new RequestParams();

        //服务器客户端标志
        params.addBodyParameter("from", TodoHelper.From.get("client"));
        //将本地selftask转换为webselftask
        WebSelfTask webSelfTask = new WebSelfTask(selfTask);
        params.addBodyParameter("client_selftask", new Gson().toJson(webSelfTask));
        params.addBodyParameter("userId", TodoHelper.UserId);
        client.send(HttpMethod.POST, TodoHelper.RemoteUrl + "saveSelfTask", params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        ClientResult result = new Gson().fromJson(responseInfo.result, ClientResult.class);
                        observer.Exeute(new RemoteObserverEvent(result, "saveSelfTask"));
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        ClientResult result = new ClientResult();
                        result.setResult(false);
                        result.setMessage("连接服务器失败!");
                        observer.Exeute(new RemoteObserverEvent(result, "saveSelfTask"));
                    }
                });
    }

    /**
     * 修改服务器端相应的个人任务
     * @param selfTask
     * @param observer
     */
    public void updateSelfTask(final SelfTask selfTask, final RemoteObserver observer){
        HttpUtils client = new HttpUtils();
        RequestParams params = new RequestParams();
        //服务器客户端标志
        params.addBodyParameter("from", TodoHelper.From.get("client"));
        //将本地的selftask转化为webselftask
        WebSelfTask webSelfTask = new WebSelfTask(selfTask);
        params.addBodyParameter("client_selftask", new Gson().toJson(webSelfTask));
        params.addBodyParameter("userId", TodoHelper.UserId);
        client.send(HttpMethod.POST, TodoHelper.RemoteUrl + "updateSelfTask", params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        ClientResult result = new Gson().fromJson(responseInfo.result, ClientResult.class);
                        observer.Exeute(new RemoteObserverEvent(result, "updateOrdelete"));
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        ClientResult result = new ClientResult();
                        result.setResult(false);
                        result.setMessage("连接服务器失败!");
                        observer.Exeute(new RemoteObserverEvent(result, "updateOrdelete"));
                    }
                });
    }

    /**
     * 从web获取所有个人任务
     */
    public void getSelfTasks(final RemoteObserver observer){
        HttpUtils client = new HttpUtils();
        RequestParams params = new RequestParams();

        //服务器客户端标志
        params.addBodyParameter("from", TodoHelper.From.get("client"));
        params.addBodyParameter("userId", TodoHelper.UserId);
        client.send(HttpMethod.POST, TodoHelper.RemoteUrl + "getSelfTasks", params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        ClientResult result = new Gson().fromJson(responseInfo.result, ClientResult.class);
                        observer.Exeute(new RemoteObserverEvent(result, "selftask"));
                    }
                    @Override
                    public void onFailure(HttpException e, String s) {
                        ClientResult result = new ClientResult();
                        result.setResult(false);
                        result.setMessage("连接服务器失败!");
                        observer.Exeute(new RemoteObserverEvent(result, "selftask"));
                    }
                });
    }

    public String DoubleStringToWebId(Object o){
        return ((int)Double.parseDouble(o.toString()))+"";
    }

    /**
     * 刷新本地个人任务
     * @param tasks 从服务器获取的个人任务
     * @return
     */
    public boolean refreshLocalSelfTasks(List<WebSelfTask> tasks){
        if(tasks != null){
            //这里删除的只是isAlter字段为0的，不能删除还没和服务器同步好的数据
            if(SelfTask.deleteAll(TodoHelper.getInstance())){
                for(WebSelfTask webSelfTask : tasks){
                    SelfTask selfTask = new SelfTask(webSelfTask);
                    SelfTask.saveTaskInfo(selfTask, TodoHelper.getInstance());
                }
                return true;
            }
            return false;
        }
        return true;
    }
}