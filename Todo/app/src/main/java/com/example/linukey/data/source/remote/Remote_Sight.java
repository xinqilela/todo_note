package com.example.linukey.data.source.remote;

import com.example.linukey.data.model.local.Project;
import com.example.linukey.data.model.local.SelfTask;
import com.example.linukey.data.model.local.Sight;
import com.example.linukey.data.model.remote.WebProject;
import com.example.linukey.data.model.remote.WebSelfTask;
import com.example.linukey.data.model.remote.WebSight;
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

public class Remote_Sight {
    /**
     * 在服务器端保存相应的情景
     */
    public void saveSight(final Sight sight, final RemoteObserver observer){
        final HttpUtils client = new HttpUtils();

        RequestParams params = new RequestParams();
        params.addBodyParameter("from", TodoHelper.From.get("client"));
        //把本地sight转化为websight
        WebSight webSight = new WebSight(sight);
        params.addBodyParameter("client_sight", new Gson().toJson(webSight));
        params.addBodyParameter("userId", TodoHelper.UserId);
        client.send(HttpMethod.POST, TodoHelper.RemoteUrl + "saveSight", params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                ClientResult result = new Gson().fromJson(responseInfo.result, ClientResult.class);
                observer.Exeute(new RemoteObserverEvent(result, "saveSight"));
            }
            @Override
            public void onFailure(HttpException e, String s) {
                ClientResult result = new ClientResult();
                result.setResult(false);
                result.setMessage("连接服务器失败!");
                observer.Exeute(new RemoteObserverEvent(result, "saveSight"));
            }
        });
    }

    /**
     * 在服务器端更新相应的情景
     * @param sight
     * @param observer
     * @return
     */
    public void updateSight(final Sight sight, final String opType, final RemoteObserver observer){
        final HttpUtils client = new HttpUtils();

        RequestParams params = new RequestParams();
        params.addBodyParameter("from", TodoHelper.From.get("client"));
        //把本地project转化为webproject
        WebSight webSight = new WebSight(sight);
        params.addBodyParameter("client_sight", new Gson().toJson(webSight));
        params.addBodyParameter("opType", opType);
        params.addBodyParameter("userId", TodoHelper.UserId);
        client.send(HttpMethod.POST, TodoHelper.RemoteUrl + "updateSight", params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                ClientResult result = new Gson().fromJson(responseInfo.result, ClientResult.class);
                observer.Exeute(new RemoteObserverEvent(result, "updateSight"));
            }
            @Override
            public void onFailure(HttpException e, String s) {
                ClientResult result = new ClientResult();
                result.setResult(false);
                result.setMessage("连接服务器失败!");
                observer.Exeute(new RemoteObserverEvent(result, "updateSight"));
            }
        });
    }

    public String DoubleStringToWebId(Object o){
        return ((int)Double.parseDouble(o.toString()))+"";
    }

    /**
     * 获取所有情景
     * @param observer
     */
    public void getSights(final RemoteObserver observer){
        HttpUtils client = new HttpUtils();
        RequestParams params = new RequestParams();

        //服务器客户端标志
        params.addBodyParameter("from", TodoHelper.From.get("client"));
        params.addBodyParameter("userId", TodoHelper.UserId);
        client.send(HttpMethod.POST, TodoHelper.RemoteUrl + "getSights", params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        ClientResult result = new Gson().fromJson(responseInfo.result, ClientResult.class);
                        observer.Exeute(new RemoteObserverEvent(result, "sight"));
                    }
                    @Override
                    public void onFailure(HttpException e, String s) {
                        ClientResult result = new ClientResult();
                        result.setResult(false);
                        result.setMessage("连接服务器失败!");
                        observer.Exeute(new RemoteObserverEvent(result, "sight"));
                    }
                });
    }

    /**
     * 刷新本地情景
     * @param sights 从服务器获取的情景
     * @return
     */
    public boolean refreshLocalSights(List<WebSight> sights){
        if(sights != null){
            //这里删除的只是isAlter字段为0的，不能删除还没和服务器同步好的数据
            if(Sight.deleteAll(TodoHelper.getInstance())){
                for(WebSight webSight : sights){
                    Sight sight = new Sight(webSight);
                    Sight.saveSight(sight, TodoHelper.getInstance());
                }
                return true;
            }
            return false;
        }
        return true;
    }
}
