package com.example.linukey.data.source.remote;

import com.example.linukey.data.model.local.User;
import com.example.linukey.data.model.remote.WebUser;
import com.example.linukey.util.TodoHelper;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import java.io.File;

/**
 * Created by linukey on 12/20/16.
 */

public class Remote_User {

    /**
     * 在web端保存用户信息
     * @param user
     * @param observer
     */
    public void saveUserInfo(final User user, final RemoteObserver observer) {
        final HttpUtils client = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("from", TodoHelper.From.get("client"));
        WebUser webUser = new WebUser(user);
        params.addBodyParameter("client_user", new Gson().toJson(webUser));
        client.send(HttpMethod.POST, TodoHelper.RemoteUrl + "regist", params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        ClientResult result = new Gson().fromJson(responseInfo.result, ClientResult.class);
                        //本地注册上传服务器成功后，保存服务器上该用户的id到本地数据库
                        String webId = DoubleStringToWebId(result.getObject());
                        user.setWebId(webId);
                        observer.Exeute(new RemoteObserverEvent(result, user));
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        ClientResult result = new ClientResult();
                        result.setResult(false);
                        result.setMessage("连接服务器失败!");
                        observer.Exeute(new RemoteObserverEvent(result, null));
                    }
                });
    }

    /**
     * 在web端更新用户图片
     * @param file
     * @param observer
     */
    public void updateUserLogo(File file, String imageType, final RemoteObserver observer){
        final HttpUtils client = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("from", TodoHelper.From.get("client"));
        params.addBodyParameter("username", TodoHelper.UserName);
        params.addBodyParameter("userId", TodoHelper.UserId);
        params.addBodyParameter("upload", file, imageType);
        client.send(HttpMethod.POST, TodoHelper.RemoteUrl + "upLoadImg", params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        ClientResult result = new Gson().fromJson(responseInfo.result, ClientResult.class);
                        observer.Exeute(new RemoteObserverEvent(result, "updateUserLogo"));
                    }
                    @Override
                    public void onFailure(HttpException e, String s) {
                        ClientResult result = new ClientResult();
                        result.setResult(false);
                        result.setMessage("连接服务器失败!");
                        observer.Exeute(new RemoteObserverEvent(result, "updateUserLogo"));
                    }
                });
    }

    /**
     * 获取用户的web logo地址
     * @param userId
     * @param observer
     */
    public void getUserLogoUrl(String userId, final RemoteObserver observer){
        final HttpUtils client = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("from", TodoHelper.From.get("client"));
        params.addBodyParameter("userId", userId);
        client.send(HttpMethod.POST, TodoHelper.RemoteUrl + "obtainUserLogoUrl", params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        ClientResult result = new Gson().fromJson(responseInfo.result, ClientResult.class);
                        observer.Exeute(new RemoteObserverEvent(result, "getUserLogoUrl"));
                    }
                    @Override
                    public void onFailure(HttpException e, String s) {
                        ClientResult result = new ClientResult();
                        result.setResult(false);
                        result.setMessage("连接服务器失败!");
                        observer.Exeute(new RemoteObserverEvent(result, "getUserLogoUrl"));
                    }
                });
    }

    /**
     * 在web端更新用户信息
     * @param user
     * @param observer
     */
    public void updateUserInfo(final User user, final RemoteObserver observer){
        final HttpUtils client = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("from", TodoHelper.From.get("client"));
        WebUser webUser = new WebUser(user);
        params.addBodyParameter("client_user", new Gson().toJson(webUser));
        client.send(HttpMethod.POST, TodoHelper.RemoteUrl + "updateUserInfo", params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        ClientResult result = new Gson().fromJson(responseInfo.result, ClientResult.class);
                        observer.Exeute(new RemoteObserverEvent(result, user));
                    }
                    @Override
                    public void onFailure(HttpException e, String s) {
                        ClientResult result = new ClientResult();
                        result.setResult(false);
                        result.setMessage("连接服务器失败!");
                        observer.Exeute(new RemoteObserverEvent(result, null));
                    }
                });
    }

    /**
     * 在web端验证用户信息并获得该用户的详细信息
     * @param username
     * @param password
     * @param observer
     */
    public void checkUserInfo(String username, String password, final RemoteObserver observer){
        HttpUtils client = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("from", TodoHelper.From.get("client"));
        params.addBodyParameter("username", new Gson().toJson(username));
        params.addBodyParameter("password", new Gson().toJson(password));
        client.send(HttpMethod.POST, TodoHelper.RemoteUrl + "login", params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String result = new Gson().fromJson(responseInfo.result, String.class);
                        String[] str = result.split("\\*\\*\\*");
                        ClientResult clientResult = (ClientResult) new Gson().fromJson(str[0], ClientResult.class);
                        //从web端获取该登录用户的详细信息
                        WebUser webUser = (WebUser) new Gson().fromJson(str[1], WebUser.class);
                        observer.Exeute(new RemoteObserverEvent(clientResult, webUser));
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        ClientResult clientResult = new ClientResult();
                        clientResult.setResult(false);
                        clientResult.setMessage("连接服务器失败!");
                        observer.Exeute(new RemoteObserverEvent(clientResult, null));
                    }
                });
    }

    /**
     * 通过用户id来获取用户名
     * @param userId
     */
    public void getUserNameByUserID(final String userId, final RemoteObserver observer){
        HttpUtils client = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("from", TodoHelper.From.get("client"));
        params.addBodyParameter("userId", userId);
        client.send(HttpMethod.POST, TodoHelper.RemoteUrl + "getUserNameByUserId", params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        ClientResult clientResult =
                                (ClientResult) new Gson().fromJson(responseInfo.result, ClientResult.class);
                        observer.Exeute(new RemoteObserverEvent(clientResult, "getUserNameByUserId"));
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        ClientResult clientResult = new ClientResult();
                        clientResult.setResult(false);
                        clientResult.setMessage("连接服务器失败!");
                        observer.Exeute(new RemoteObserverEvent(clientResult, "getUserNameByUserId"));
                    }
                });
    }

    public String DoubleStringToWebId(Object o){
        return ((int)Double.parseDouble(o.toString()))+"";
    }
}