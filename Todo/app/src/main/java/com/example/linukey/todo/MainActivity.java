package com.example.linukey.todo;

import android.Manifest;
import android.app.ActionBar;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.textservice.TextInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.linukey.addedit_selftask.AddEditSelfTaskActivity;
import com.example.linukey.addedit_team.AddEditTeamActivity;
import com.example.linukey.addedit_teamtask.AddEditTeamTaskActivity;
import com.example.linukey.addedit_userinfo.AddEditUserInfoActivity;
import com.example.linukey.addedit_userinfo.CircleImageView;
import com.example.linukey.notification.NotificationActivity;
import com.example.linukey.notification.PointToPoint;
import com.example.linukey.system_setting.SystemSettingActivity;
import com.example.linukey.util.BaseActivity;
import com.example.linukey.util.TodoHelper;
import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.NetUtils;

import java.util.Iterator;
import java.util.List;

public class MainActivity extends BaseActivity implements IMainContract.View {

    public boolean homeSelect, selfSelect, teamSelect;
    public boolean addSelf, addTeam;
    HomePageFragment homePageFragment;
    SelfTaskMenuFragment selfTaskMenuFragment;
    TeamTaskMenuFragment teamTaskMenuFragment;
    IMainContract.Presenter todoPresenter;
    long firstTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initActionBar();
        todoPresenter = new MainPresenter(this);
        initFragment();
        initLeftMenu();
        getFragmentManager().beginTransaction().add(R.id.menuFragment, homePageFragment).commit();
        initEMClient();
        LoginEM(TodoHelper.UserName, TodoHelper.PassWord);
        homeSelect = true;
        changeIcon();
    }

    private void initActionBar(){
        ActionBar actionBar = getActionBar();
        actionBar.setTitle("");
        int titleId = Resources.getSystem().getIdentifier("action_bar_title",
                "id", "android");
        TextView textView = (TextView)findViewById(titleId);
        // 获取屏幕的相关尺寸，用以转换单位
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        float density = metric.density;
        int widthMax = metric.widthPixels;
        textView.setWidth(widthMax);
        textView.setGravity(Gravity.CENTER);
    }

    /**
     * 初始化三个碎片主页面
     */
    private void initFragment(){
        homePageFragment = new HomePageFragment();
        selfTaskMenuFragment = new SelfTaskMenuFragment();
        teamTaskMenuFragment = new TeamTaskMenuFragment();
    }

    /**
     * 初始化左划菜单
     */
    public void initLeftMenu(){
        TextView textView = (TextView)findViewById(R.id.username);
        textView.setText(new TodoHelper().getCurrentUserInfo(this).getUsername());
    }

    public void initUserLogo(){
        if(TodoHelper.UserLogo != null){
            CircleImageView circleImageView = (CircleImageView)findViewById(R.id.userlogo);
            circleImageView.setImageBitmap(BitmapFactory.decodeFile(TodoHelper.UserLogo));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        todoPresenter.CreateMenu(menu);
        return true;
    }

    @Override
    public void onResume(){
        super.onResume();
        initLeftMenu();
        initUserLogo();
    }

    /**
     * 添加个人任务
     */
    @Override
    public void addSelfTask(){
        if(addSelf){
            Intent intent = new Intent(MainActivity.this, AddEditSelfTaskActivity.class);
            startActivity(intent);
        }
    }

    /**
     * 添加团队任务
     */
    @Override
    public void addTeamTask(){
        if(addTeam){
            Intent intent = new Intent(MainActivity.this, AddEditTeamTaskActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        return todoPresenter.MenuChoice(item, this);
    }

    /**
     * 首页按钮点击
     * @param view
     */
    public void onClick_Home(View view){
        homeSelect = true;
        addSelf = addTeam = false;
        getFragmentManager().beginTransaction().replace(R.id.menuFragment, homePageFragment).commit();
        changeIcon();
    }

    /**
     * 个人任务点击
     * @param view
     */
    public void onClick_Self(View view){
        selfSelect = true;
        addSelf = true;
        addTeam = false;
        getFragmentManager().beginTransaction().replace(R.id.menuFragment, selfTaskMenuFragment).commit();
        changeIcon();
    }

    /**
     * 团队任务点击
     * @param view
     */
    public void onClick_Team(View view){
        teamSelect = true;
        addTeam = true;
        addSelf = false;
        getFragmentManager().beginTransaction().replace(R.id.menuFragment, teamTaskMenuFragment).commit();
        changeIcon();
    }

    /**
     * 用户logo点击
     */
    public void onClick_userlogo(View view){
        Intent intent = new Intent(MainActivity.this, AddEditUserInfoActivity.class);
        startActivity(intent);
    }

    /**
     * 点击两次退出
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        switch(keyCode)
        {
            case KeyEvent.KEYCODE_BACK:
                long secondTime = System.currentTimeMillis();
                if (secondTime - firstTime > 2000) {
                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    firstTime = secondTime;//更新firstTime
                    return true;
                } else {
                    this.finish();
                    System.exit(0);
                }
                break;
        }
        return super.onKeyUp(keyCode, event);
    }

    /**
     * 系统主菜单点击变换效果
     */
    @Override
    public void changeIcon(){
        Button btnHome = (Button)findViewById(R.id.btnHome);
        Button btnSelf = (Button)findViewById(R.id.btnSelf);
        Button btnTeam = (Button)findViewById(R.id.btnTeam);
        btnHome.setBackgroundColor(Color.parseColor("#1A78B8"));
        btnSelf.setBackgroundColor(Color.parseColor("#1A78B8"));
        btnTeam.setBackgroundColor(Color.parseColor("#1A78B8"));
        if(homeSelect) btnHome.setBackgroundColor(Color.parseColor("#98D2F3"));
        else if(selfSelect) btnSelf.setBackgroundColor(Color.parseColor("#98D2F3"));
        else if(teamSelect) btnTeam.setBackgroundColor(Color.parseColor("#98D2F3"));
        homeSelect = false;
        selfSelect = false;
        teamSelect = false;
    }

    /**
     * 系统设置
     * @param view
     */
    public void systemSetting(View view){
        Intent intent = new Intent(MainActivity.this, SystemSettingActivity.class);
        startActivity(intent);
    }

    /**
     * 消息中心
     * @param view
     */
    public void messageCenter(View view){
        Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
        startActivity(intent);
    }

    public void initEMClient(){
        EMOptions options = new EMOptions();
        options.setAcceptInvitationAlways(false);

        Context appContext = this;
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        // 如果APP启用了远程的service，此application:onCreate会被调用2次
        // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
        // 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回

        if (processAppName == null || !processAppName.equalsIgnoreCase(appContext.getPackageName())) {
            Log.e("TAG", "enter the service process!");
            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }

        EMClient.getInstance().init(this, options);
        EMClient.getInstance().setDebugMode(true);
    }

    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }

    private void LoginEM(final String username, String password){
        EMClient.getInstance().login(username, password, new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("TAG", "通讯云登录成功!");
                    }
                });
            }
            @Override
            public void onProgress(int progress, String status) {

            }
            @Override
            public void onError(int code, String message) {
                Toast.makeText(MainActivity.this, "登录即时通讯云失败" + message, Toast.LENGTH_SHORT).show();
            }
        });

        //注册一个监听连接状态的listener
        EMClient.getInstance().addConnectionListener(new MyConnectionListener());

        //注册接受消息的监听器
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
    }

    public static void sendMessage(String username, String content){
        //创建一条文本消息，content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此
        EMMessage message = EMMessage.createTxtSendMessage(content, username);
        message.setAttribute("data", content);
        EMClient.getInstance().chatManager().sendMessage(message);
    }

    EMMessageListener msgListener = new EMMessageListener() {
        @Override
        public void onMessageReceived(final List<EMMessage> messages) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for(EMMessage message : messages){
                        PointToPoint pointToPoint = null;
                        try {
                            pointToPoint = new Gson().fromJson(message.getStringAttribute("data"), PointToPoint.class);
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                        }
                        if(pointToPoint != null && pointToPoint.getExecType().equals(TodoHelper.PtoPExecType.get("joinTeam"))) {
                            Log.i("TAG", "有一个即时申请!");
                        }else if(pointToPoint != null && pointToPoint.getExecType().equals(TodoHelper.PtoPExecType.get("joinTeamResult"))){
                            Log.i("TAG", "有一个即时回复!");
                        }
                    }
                }
            });
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            //收到透传消息
        }

        @Override
        public void onMessageRead(List<EMMessage> list) {

        }

        @Override
        public void onMessageDelivered(List<EMMessage> list) {

        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
            //消息状态变动
        }
    };

    //实现ConnectionListener接口
    private class MyConnectionListener implements EMConnectionListener {
        @Override
        public void onConnected() {
        }

        @Override
        public void onDisconnected(final int error) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (error == EMError.USER_REMOVED) {
                        // 显示帐号已经被移除
                    } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                        // 显示帐号在其他设备登录
                    } else {
                        if (NetUtils.hasNetwork(MainActivity.this)){

                        }
                        //连接不到聊天服务器
                        else{
                            //当前网络不可用，请检查网络设置
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onDestroy(){
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
        EMClient.getInstance().logout(true);
        super.onDestroy();
    }
}
