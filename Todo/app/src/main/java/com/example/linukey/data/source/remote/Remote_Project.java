package com.example.linukey.data.source.remote;

import android.util.Log;
import android.widget.Toast;

import com.example.linukey.data.model.local.Project;
import com.example.linukey.data.model.local.SelfTask;
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

public class Remote_Project {
    private final String TAG = this.getClass().getName();


    /**
     * 在服务器端保存相应的项目
     */
    public void saveProject(final Project project, final RemoteObserver observer){
        final HttpUtils client = new HttpUtils();

        RequestParams params = new RequestParams();
        params.addBodyParameter("from", TodoHelper.From.get("client"));
        //把本地project转化为webproject
        WebProject webProject = new WebProject(project);
        params.addBodyParameter("client_project", new Gson().toJson(webProject));
        params.addBodyParameter("userId", TodoHelper.UserId);
        client.send(HttpMethod.POST, TodoHelper.RemoteUrl + "saveProject", params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                ClientResult result = new Gson().fromJson(responseInfo.result, ClientResult.class);
                observer.Exeute(new RemoteObserverEvent(result, "saveProject"));
            }
            @Override
            public void onFailure(HttpException e, String s) {
                ClientResult result = new ClientResult();
                result.setResult(false);
                result.setMessage("连接服务器失败!");
                observer.Exeute(new RemoteObserverEvent(result, "saveProject"));
            }
        });
    }

    /**
     * 在服务器端更新相应的项目
     * @param project
     * @param observer
     * @return
     */
    public void updateProject(final Project project, final String opType, final RemoteObserver observer){
        final HttpUtils client = new HttpUtils();

        RequestParams params = new RequestParams();
        params.addBodyParameter("from", TodoHelper.From.get("client"));
        //把本地project转化为webproject
        WebProject webProject = new WebProject(project);
        Log.i(TAG, "即将连接服务器更新Project,值为：" + webProject.toString());
        params.addBodyParameter("client_project", new Gson().toJson(webProject));
        params.addBodyParameter("opType", opType);
        params.addBodyParameter("userId", TodoHelper.UserId);
        client.send(HttpMethod.POST, TodoHelper.RemoteUrl + "updateProject", params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                ClientResult result = new Gson().fromJson(responseInfo.result, ClientResult.class);
                observer.Exeute(new RemoteObserverEvent(result, "updateProject"));
            }
            @Override
            public void onFailure(HttpException e, String s) {
                ClientResult result = new ClientResult();
                result.setResult(false);
                result.setMessage("连接服务器失败!");
                observer.Exeute(new RemoteObserverEvent(result, "updateProject"));
            }
        });
    }

    /**
     * 获取所有项目
     * @param observer
     */
    public void getProjects(final RemoteObserver observer){
        HttpUtils client = new HttpUtils();
        RequestParams params = new RequestParams();

        //服务器客户端标志
        params.addBodyParameter("from", TodoHelper.From.get("client"));
        params.addBodyParameter("userId", TodoHelper.UserId);
        client.send(HttpMethod.POST, TodoHelper.RemoteUrl + "getProjects", params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        ClientResult result = new Gson().fromJson(responseInfo.result, ClientResult.class);
                        observer.Exeute(new RemoteObserverEvent(result, "project"));
                    }
                    @Override
                    public void onFailure(HttpException e, String s) {
                        ClientResult result = new ClientResult();
                        result.setResult(false);
                        result.setMessage("连接服务器失败!");
                        observer.Exeute(new RemoteObserverEvent(result, "project"));
                    }
                });
    }

    public String DoubleStringToWebId(Object o){
        return ((int)Double.parseDouble(o.toString()))+"";
    }

    /**
     * 刷新本地项目
     * @param projects 用来刷新的数据
     * @return
     */
    public boolean refreshLocalProjects(List<WebProject> projects){
        if(projects != null){
            //这里删除的只是isAlter字段为0的，不能删除还没和服务器同步好的数据
            if(Project.deleteAll(TodoHelper.getInstance())){
                for(WebProject webProject : projects){
                    Project project = new Project(webProject);
                    Project.saveProject(project, TodoHelper.getInstance());
                }
                return true;
            }
            return false;
        }
        return true;
    }
}
