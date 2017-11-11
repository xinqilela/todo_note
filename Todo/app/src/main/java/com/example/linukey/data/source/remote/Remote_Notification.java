package com.example.linukey.data.source.remote;

import com.example.linukey.data.model.local.TeamJoinInfo;
import com.example.linukey.data.model.remote.WebTeamJoinInfo;
import com.example.linukey.notification.PointToPoint;
import com.example.linukey.todo.MainActivity;
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
 * Created by linukey on 17-3-3.
 */

public class Remote_Notification {
    /**
     * 获取所有申请小组的通知
     * @param observer
     */
    public void getTeamJoinDatas(final RemoteObserver observer){
        HttpUtils client = new HttpUtils();
        RequestParams params = new RequestParams();

        //服务器客户端标志
        params.addBodyParameter("from", TodoHelper.From.get("client"));
        params.addBodyParameter("userName", TodoHelper.UserName);
        client.send(HttpMethod.POST, TodoHelper.RemoteUrl + "getTeamJoinNotifications", params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        ClientResult result = new Gson().fromJson(responseInfo.result, ClientResult.class);
                        observer.Exeute(new RemoteObserverEvent(result, "getTeamJoinDatas"));
                    }
                    @Override
                    public void onFailure(HttpException e, String s) {
                        ClientResult result = new ClientResult();
                        result.setResult(false);
                        result.setMessage("连接服务器失败!");
                        observer.Exeute(new RemoteObserverEvent(result, "getTeamJoinDatas"));
                    }
                });
    }

    public void deleteTeamJoinNotification(final TeamJoinInfo teamJoinInfo, final RemoteObserver observer){
        final HttpUtils client = new HttpUtils();
        RequestParams params = new RequestParams();

        params.addBodyParameter("from", TodoHelper.From.get("client"));
        WebTeamJoinInfo webTeamJoinInfo = new WebTeamJoinInfo(teamJoinInfo);
        params.addBodyParameter("client_TeamJoinData", new Gson().toJson(webTeamJoinInfo));
        client.send(HttpMethod.POST, TodoHelper.RemoteUrl + "deleteTeamJoinData", params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        ClientResult result = new Gson().fromJson(responseInfo.result, ClientResult.class);
                        observer.Exeute(new RemoteObserverEvent(result, "deleteTeamJoinData"));
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        ClientResult result = new ClientResult();
                        result.setResult(false);
                        result.setMessage("连接服务器失败!");
                        observer.Exeute(new RemoteObserverEvent(result, "deleteTeamJoinData"));
                    }});
    }

    /**
     * 在服务器上保存小组加入申请
     * @param teamJoinInfo
     * @param observer
     */
    public void saveTeamJoinNotification(final TeamJoinInfo teamJoinInfo, final RemoteObserver observer){
        final HttpUtils client = new HttpUtils();
        RequestParams params = new RequestParams();

        params.addBodyParameter("from", TodoHelper.From.get("client"));
        WebTeamJoinInfo webTeamJoinInfo = new WebTeamJoinInfo(teamJoinInfo);
        params.addBodyParameter("client_TeamJoinData", new Gson().toJson(webTeamJoinInfo));
        client.send(HttpMethod.POST, TodoHelper.RemoteUrl + "saveTeamJoinNotification", params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        ClientResult result = new Gson().fromJson(responseInfo.result, ClientResult.class);
                        result.setObject(teamJoinInfo);
                        observer.Exeute(new RemoteObserverEvent(result, "saveTeamJoinNotification"));
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        ClientResult result = new ClientResult();
                        result.setResult(false);
                        result.setMessage("连接服务器失败!");
                        observer.Exeute(new RemoteObserverEvent(result, "saveTeamJoinNotification"));
                    }});
    }

    /**
     * 同意某人加入小组
     * @param teamJoinInfo
     * @param observer
     */
    public void agreeSomeOneToJoinTeam(final TeamJoinInfo teamJoinInfo, final RemoteObserver observer){
        final HttpUtils client = new HttpUtils();
        RequestParams params = new RequestParams();

        WebTeamJoinInfo webTeamJoinInfo = new WebTeamJoinInfo(teamJoinInfo);
        params.addBodyParameter("from", TodoHelper.From.get("client"));
        params.addBodyParameter("client_TeamJoinData", new Gson().toJson(webTeamJoinInfo));
        client.send(HttpMethod.POST, TodoHelper.RemoteUrl + "agreeSomeOneToJoinTeam", params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        ClientResult result = new Gson().fromJson(responseInfo.result, ClientResult.class);
                        if(result.isResult()){
                            //用即时通讯云即时告知对方
                            PointToPoint pointToPoint = new PointToPoint();
                            pointToPoint.setExecType(TodoHelper.PtoPExecType.get("joinTeamResult"));
                            pointToPoint.setObject("1");
                            String josnData = new Gson().toJson(pointToPoint);
                            MainActivity.sendMessage(teamJoinInfo.getFromUserName(), josnData);
                        }
                        observer.Exeute(new RemoteObserverEvent(result, "agreeSomeOneToJoinTeam"));
                    }
                    @Override
                    public void onFailure(HttpException e, String s) {
                        ClientResult clientResult = new ClientResult();
                        clientResult.setResult(false);
                        clientResult.setMessage("连接服务器失败!");
                        observer.Exeute(new RemoteObserverEvent(clientResult, "agreeSomeOneToJoinTeam"));
                    }
                });
    }

    /**
     * 用户退出小组
     * @param teamJoinInfo
     * @param observer
     */
    public void userQuitTeam(final TeamJoinInfo teamJoinInfo, final RemoteObserver observer){
        final HttpUtils client = new HttpUtils();
        RequestParams params = new RequestParams();

        WebTeamJoinInfo webTeamJoinInfo = new WebTeamJoinInfo(teamJoinInfo);
        params.addBodyParameter("from", TodoHelper.From.get("client"));
        params.addBodyParameter("client_TeamJoinData", new Gson().toJson(webTeamJoinInfo));
        client.send(HttpMethod.POST, TodoHelper.RemoteUrl + "userQuitTeam", params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        ClientResult result = new Gson().fromJson(responseInfo.result, ClientResult.class);
                        observer.Exeute(new RemoteObserverEvent(result, "userQuitTeam"));
                    }
                    @Override
                    public void onFailure(HttpException e, String s) {
                        ClientResult clientResult = new ClientResult();
                        clientResult.setResult(false);
                        clientResult.setMessage("连接服务器失败!");
                        observer.Exeute(new RemoteObserverEvent(clientResult, "userQuitTeam"));
                    }
                });
    }

    /**
     * 拒绝某人加入
     * @param teamJoinInfo
     * @param observer
     */
    public void refuseSomeOneToJoinTeam(final TeamJoinInfo teamJoinInfo, final RemoteObserver observer){
        final HttpUtils client = new HttpUtils();
        RequestParams params = new RequestParams();

        WebTeamJoinInfo webTeamJoinInfo = new WebTeamJoinInfo(teamJoinInfo);
        params.addBodyParameter("from", TodoHelper.From.get("client"));
        params.addBodyParameter("client_TeamJoinData", new Gson().toJson(webTeamJoinInfo));
        client.send(HttpMethod.POST, TodoHelper.RemoteUrl + "refuseSomeOneToJoinTeam", params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        ClientResult result = new Gson().fromJson(responseInfo.result, ClientResult.class);
                        observer.Exeute(new RemoteObserverEvent(result, "refuseSomeOneToJoinTeam"));
                    }
                    @Override
                    public void onFailure(HttpException e, String s) {
                        ClientResult clientResult = new ClientResult();
                        clientResult.setResult(false);
                        clientResult.setMessage("连接服务器失败!");
                        observer.Exeute(new RemoteObserverEvent(clientResult, "refuseSomeOneToJoinTeam"));
                    }
                });
    }

    /**
     * 刷新本地申请小组通知信息
     * @param webTeamJoinInfos 用来刷新的数据
     * @return
     */
    public boolean refreshLocalTeamJoinNotifications(List<WebTeamJoinInfo> webTeamJoinInfos){
        if(webTeamJoinInfos != null){
            //这里删除的只是isAlter字段为0的，不能删除还没和服务器同步好的数据
            if(TeamJoinInfo.deleteAll(TodoHelper.getInstance())){
                for(WebTeamJoinInfo webData : webTeamJoinInfos){
                    TeamJoinInfo teamdata = new TeamJoinInfo(webData);
                    if(TeamJoinInfo.saveTeamJoinData(teamdata, TodoHelper.getInstance())==-1)
                        return false;
                }
                return true;
            }
            return false;
        }
        return true;
    }
}