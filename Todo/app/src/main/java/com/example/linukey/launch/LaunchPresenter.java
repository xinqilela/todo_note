package com.example.linukey.launch;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.linukey.addedit_userinfo.AddEditUserInfoPresenter;
import com.example.linukey.data.model.local.User;
import com.example.linukey.data.model.remote.WebGoal;
import com.example.linukey.data.model.remote.WebProject;
import com.example.linukey.data.model.remote.WebSelfTask;
import com.example.linukey.data.model.remote.WebSight;
import com.example.linukey.data.model.remote.WebTeam;
import com.example.linukey.data.model.remote.WebTeamJoinInfo;
import com.example.linukey.data.model.remote.WebTeamTask;
import com.example.linukey.data.source.remote.RemoteObserver;
import com.example.linukey.data.source.remote.RemoteObserverEvent;
import com.example.linukey.data.source.remote.Remote_Goal;
import com.example.linukey.data.source.remote.Remote_Notification;
import com.example.linukey.data.source.remote.Remote_Project;
import com.example.linukey.data.source.remote.Remote_SelfTask;
import com.example.linukey.data.source.remote.Remote_Sight;
import com.example.linukey.data.source.remote.Remote_Team;
import com.example.linukey.data.source.remote.Remote_TeamTask;
import com.example.linukey.data.source.remote.Remote_User;
import com.example.linukey.login.LoginActivity;
import com.example.linukey.todo.MainActivity;
import com.example.linukey.todo.MainPresenter;
import com.example.linukey.util.BaseActivity;
import com.example.linukey.util.TodoHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * Created by linukey on 12/20/16.
 */

public class LaunchPresenter implements LaunchContract.LaunchPresenter, RemoteObserver {
    LaunchContract.LaunchView view = null;
    private final int SPLASH_DISPLAY_LENGHT = 3000; // 延迟三秒
    private static final long startTime = System.currentTimeMillis();
    private final String TAG = this.getClass().getName();
    private Bitmap.CompressFormat type = null;
    public LaunchPresenter(LaunchContract.LaunchView view) {
        this.view = view;
    }

    /**
     * 初始化开启画面
     */
    @Override
    public void initLaunchPic(final Context context) {
        long endTime = System.currentTimeMillis();
        if (endTime - startTime >= 3000) {
            boolean judgeLogin = true;
            User user = new TodoHelper().getCurrentUserInfo(context);
            if (user != null && !user.getUsername().isEmpty()) {
                judgeLogin = false;
            }
            Intent intent = null;
            if (judgeLogin)
                intent = new Intent(context, LoginActivity.class);
            else
                intent = new Intent(context, MainActivity.class);
            view.showActivity(intent);
        } else {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    boolean judgeLogin = true;
                    User user = new TodoHelper().getCurrentUserInfo(context);
                    if (user != null && !user.getUsername().isEmpty()) {
                        judgeLogin = false;
                    }
                    Intent intent = null;
                    if (judgeLogin)
                        intent = new Intent(context, LoginActivity.class);
                    else
                        intent = new Intent(context, MainActivity.class);
                    view.showActivity(intent);
                }
            }, SPLASH_DISPLAY_LENGHT - (endTime - startTime));
        }
    }

    @Override
    public void initLocalData(final Context context){
        initLocalSelfTasksFromWeb(context);
        initLocalProjectsFromWeb(context);
        initLocalGoalsFromWeb(context);
        initLocalSightsFromWeb(context);
        initLocalTeamsFromWeb(context);
        initLocalTeamJoinDatasFromWeb(context);
        initLocalTeamTasksFromWeb(context);
        initLocalUserLogoFromWeb(context);
    }

    /**
     * 从web获取个人任务初始化本地数据库
     * @param context
     */
    @Override
    public void initLocalSelfTasksFromWeb(final Context context) {
        Log.i(TAG, "准备从Web获取数据初始化本地个人任务数据!");
        new Remote_SelfTask().getSelfTasks(this);
    }

    /**
     * 从web获取项目初始化本地数据库
     * @param context
     */
    @Override
    public void initLocalProjectsFromWeb(final Context context) {
        Log.i(TAG, "准备从Web获取数据初始化本地项目数据!");
        new Remote_Project().getProjects(this);
    }

    /**
     * 从web获取目标初始化本地数据库
     * @param context
     */
    @Override
    public void initLocalGoalsFromWeb(final Context context) {
        Log.i(TAG, "准备从Web获取数据初始化本地目标数据!");
        new Remote_Goal().getGoals(this);
    }

    /**
     * 从web获取情景初始化本地数据库
     * @param context
     */
    @Override
    public void initLocalSightsFromWeb(final Context context) {
        Log.i(TAG, "准备从Web获取数据初始化本地情景数据!");
        new Remote_Sight().getSights(this);
    }

    /**
     * 从web获取小组数据初始化本地数据库
     * @param context
     */
    @Override
    public void initLocalTeamsFromWeb(final Context context){
        Log.i(TAG, "准备从web获取数据初始化本地小组信息数据!");
        new Remote_Team().getTeams(TodoHelper.UserId, this);
    }

    /**
     * 从web获取小组申请信息数据初始化本地数据库
     * @param context
     */
    @Override
    public void initLocalTeamJoinDatasFromWeb(final Context context){
        Log.i(TAG, "准备从web获取小组申请通知信息初始化本地数据!");
        new Remote_Notification().getTeamJoinDatas(this);
    }

    /**
     * 从web获取小组任务数据初始化本地数据库
     * @param context
     */
    @Override
    public void initLocalTeamTasksFromWeb(final Context context){
        Log.i(TAG, "准备从web获取小组任务数据初始化本地数据!");
        new Remote_TeamTask().getTeamTasks(this);
    }

    @Override
    public void initLocalUserLogoFromWeb(final Context context){
        Log.i(TAG, "准备从web获取用户logo地址下载用户logo!");
        new Remote_User().getUserLogoUrl(TodoHelper.UserId, this);
    }

    private InputStream OpenHttpConnection(String urlString){
        InputStream in = null;
        int response = -1;
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            Toast.makeText(TodoHelper.getInstance(), "pppppppaa", Toast.LENGTH_SHORT).show();
        }
        URLConnection conn = null;
        try {
            conn = url.openConnection();
        } catch (IOException e) {
            Toast.makeText(TodoHelper.getInstance(), "pppppppbb", Toast.LENGTH_SHORT).show();
        }
        if(!(conn instanceof HttpURLConnection))
            Toast.makeText(TodoHelper.getInstance(), "pppppppcc", Toast.LENGTH_SHORT).show();
        try{
            HttpURLConnection httpConn = (HttpURLConnection)conn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            try {
                httpConn.setRequestMethod("GET");
            } catch (ProtocolException e) {
                Toast.makeText(TodoHelper.getInstance(), "pppppppdd", Toast.LENGTH_SHORT).show();
            }
            try {
                httpConn.connect();
            } catch (IOException e) {
                Toast.makeText(TodoHelper.getInstance(), "pppppppee", Toast.LENGTH_SHORT).show();
            }
            try {
                response = httpConn.getResponseCode();
            } catch (IOException e) {
                Toast.makeText(TodoHelper.getInstance(), "pppppppff", Toast.LENGTH_SHORT).show();
            }
            if(response == HttpURLConnection.HTTP_OK){
                try {
                    in = httpConn.getInputStream();
                } catch (IOException e) {
                    Toast.makeText(TodoHelper.getInstance(), "pppppppqq", Toast.LENGTH_SHORT).show();
                }
            }
        }catch (Exception ex){
            Toast.makeText(TodoHelper.getInstance(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return in;
    }

    @Override
    public void Exeute(RemoteObserverEvent remoteObserverEvent) {
        String judge = remoteObserverEvent.object.toString();
        if (judge.equals("project")) {
            if (remoteObserverEvent.clientResult.isResult()) {
                Type type = new TypeToken<List<WebProject>>() {
                }.getType();
                String jsonProjects = (String) remoteObserverEvent.clientResult.getObject();
                List<WebProject> projects = new Gson().fromJson(jsonProjects, type);
                if (!new Remote_Project().refreshLocalProjects(projects))
                    Log.e(TAG, "刷新本地数据库项目数据失败!");
                else
                    Log.i(TAG, "刷新本地数据库项目数据成功!");
            }
        } else if (judge.equals("goal")) {
            if (remoteObserverEvent.clientResult.isResult()) {
                Type type = new TypeToken<List<WebGoal>>() {}.getType();
                String jsonGoals = (String) remoteObserverEvent.clientResult.getObject();
                List<WebGoal> goals = new Gson().fromJson(jsonGoals, type);
                if (!new Remote_Goal().refreshLocalGoals(goals))
                    Log.e(TAG, "刷新本地数据库目标数据失败!");
                else
                    Log.i(TAG, "刷新本地数据库目标数据成功!");
            }
        } else if (judge.equals("sight")) {
            if (remoteObserverEvent.clientResult.isResult()) {
                Type type = new TypeToken<List<WebSight>>() {
                }.getType();
                String jsonSights = (String) remoteObserverEvent.clientResult.getObject();
                List<WebSight> sights = new Gson().fromJson(jsonSights, type);
                if (!new Remote_Sight().refreshLocalSights(sights))
                    Log.e(TAG, "刷新本地数据库情景数据失败!");
                else
                    Log.i(TAG, "刷新本地数据库情景数据成功!");
            }
        } else if (judge.equals("selftask")) {
            if (remoteObserverEvent.clientResult.isResult()) {
                Type type = new TypeToken<List<WebSelfTask>>() {
                }.getType();
                String jsonTasks = (String) remoteObserverEvent.clientResult.getObject();
                List<WebSelfTask> tasks = new Gson().fromJson(jsonTasks, type);
                if (!new Remote_SelfTask().refreshLocalSelfTasks(tasks))
                    Log.e(TAG, "刷新本地数据库个人任务数据失败!");
                else
                    Log.i(TAG, "刷新本地数据库个人任务数据成功!");
            }
        }else if(judge.equals("team")){
            if(remoteObserverEvent.clientResult.isResult()){
                Type type = new TypeToken<List<WebTeam>>(){}.getType();
                String jsonTeams = (String)remoteObserverEvent.clientResult.getObject();
                List<WebTeam> teams = new Gson().fromJson(jsonTeams, type);
                if(!new Remote_Team().refreshLocalTeams(teams)){
                    Log.e(TAG, "刷新本地数据库个人任务数据失败!");
                }else{
                    Log.i(TAG, "刷新本地数据库个人任务数据成功!");
                }
            }
        }else if(judge.equals("getTeamJoinDatas")){
            if(remoteObserverEvent.clientResult.isResult()){
                Type type = new TypeToken<List<WebTeamJoinInfo>>(){}.getType();
                String jsonData = (String)remoteObserverEvent.clientResult.getObject();
                List<WebTeamJoinInfo> teamJoinDatas = new Gson().fromJson(jsonData, type);
                if(!new Remote_Notification().refreshLocalTeamJoinNotifications(teamJoinDatas))
                    Log.e(TAG, "刷新本地数据库个人任务数据失败!");
                else
                    Log.i(TAG, "刷新本地数据库个人任务数据成功!");
            }
        }else if(judge.equals("getTeamTasks")){
            if(remoteObserverEvent.clientResult.isResult()){
                Type type = new TypeToken<List<WebTeamTask>>(){}.getType();
                String jsonData = (String)remoteObserverEvent.clientResult.getObject();
                List<WebTeamTask> teamTasks = new Gson().fromJson(jsonData, type);
                if(!new Remote_TeamTask().refreshLocalTeamTasks(teamTasks))
                    Log.e(TAG, "刷新本地数据库个人任务数据失败!");
                else
                    Log.i(TAG, "刷新本地数据库个人任务数据成功!");
            }
        }else if(judge.equals("getUserLogoUrl")){
            if(remoteObserverEvent.clientResult.isResult()){
                String url = null;
                if(remoteObserverEvent.clientResult.getObject() != null)
                    url = remoteObserverEvent.clientResult.getObject().toString();
                if(url != null && !url.isEmpty()) {
                    url = TodoHelper.RemoteUrl + url.substring(url.indexOf("img"), url.length());

                    String imageType = url.substring(url.lastIndexOf(".") + 1, url.length());
                    if (imageType.equals("png")) type = Bitmap.CompressFormat.PNG;
                    else if (imageType.equals("jpg") || imageType.equals("jpeg"))
                        type = Bitmap.CompressFormat.JPEG;

                    //必须在另一个线程中执行
                    new DownloadImageTask().execute(url);
                }
            }
        }

        if(!remoteObserverEvent.clientResult.isResult())
        Log.e(TAG, "刷新本地数据时遇到错误：连接服务器失败!");
    }
    
    private Bitmap DownloadImage(String url){
        Bitmap bitmap = null;
        InputStream in = null;
        try{
            in = OpenHttpConnection(url);
            bitmap = BitmapFactory.decodeStream(in);
            in.close();
        }catch (IOException ex){
            Log.e(TAG, ex.getMessage());
        }
        return bitmap;
    }
    
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap>{
        protected Bitmap doInBackground(String... urls){
            return DownloadImage(urls[0]);
        }
        protected void onPostExecute(Bitmap result){
            if(result != null){
                //在本地存储图片
                File file = saveBitmap(result, type);
                //刷新数据库里面的信息
                new AddEditUserInfoPresenter().saveUserLogo(Uri.fromFile(file), TodoHelper.getInstance());
                MainActivity activity = BaseActivity.getMainActivityInstance();
                if (activity != null)
                    activity.initUserLogo();
            }
        }
    }
    
    private File saveBitmap(Bitmap bitmap, Bitmap.CompressFormat type)
    {
        String sufix = null;
        if(type == Bitmap.CompressFormat.PNG) sufix = ".png";
        else if(type == Bitmap.CompressFormat.JPEG) sufix = ".jpeg";

        File file=new File(Environment.getExternalStorageDirectory(), "/temp/todoUserLogo" + sufix);
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        FileOutputStream out;
        try{
            out = new FileOutputStream(file);
            if(bitmap.compress(type, 90, out))
            {
                out.flush();
                out.close();
            }
        }
        catch (FileNotFoundException e)
        {
            Toast.makeText(TodoHelper.getInstance(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
        catch (IOException e)
        {
            Toast.makeText(TodoHelper.getInstance(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return file;
    }
}