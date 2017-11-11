package com.example.linukey.addedit_userinfo;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.linukey.data.model.local.User;
import com.example.linukey.data.source.remote.RemoteObserver;
import com.example.linukey.data.source.remote.RemoteObserverEvent;
import com.example.linukey.data.source.remote.Remote_User;
import com.example.linukey.register.RegisterPresenter;
import com.example.linukey.todo.R;
import com.example.linukey.util.BaseActivity;
import com.example.linukey.util.TodoHelper;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.model.TResult;

import java.io.File;
import java.io.FileNotFoundException;


/**
 * Created by linukey on 12/23/16.
 */

public class AddEditUserInfoActivity extends TakePhotoActivity
        implements AddEditUserInfoContract.AddEditUserInfoView, RemoteObserver {

    private AddEditUserInfoContract.AddEditUserInfoPresenter presenter = null;
    ViewHolder viewHolder = null;
    private final String TAG = this.getClass().getName();
    private final int PERMISSIONS_REQUEST_CALL_CAMERA = 1;
    private CustomHelper customHelper;

    class ViewHolder {
        EditText userName;
        EditText email;
        EditText phoneNumber;
        CircleImageView userlogo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adduserinfo);
        BaseActivity.setIn(this);
        init();
    }

    /**
     * 初始化标题栏
     */
    public void initActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setTitle("返回");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        int titleId = Resources.getSystem().getIdentifier("action_bar_title",
                "id", "android");
        TextView textView = (TextView)findViewById(titleId);
        textView.setTextColor(Color.parseColor("#F2F1F0"));
        textView.setTextSize(16);
    }

    public void init() {
        presenter = new AddEditUserInfoPresenter(this);
        initActionBar();
        initViewHolder();

        initEdit();
    }

    public void initViewHolder() {
        viewHolder = new ViewHolder();
        viewHolder.userName = (EditText) findViewById(R.id.username);
        viewHolder.email = (EditText) findViewById(R.id.email);
        viewHolder.phoneNumber = (EditText) findViewById(R.id.phone);
        viewHolder.userlogo = (CircleImageView) findViewById(R.id.userlogo);
    }

    /**
     * 初始化用户已有信息
     */
    @Override
    public void initEdit() {
        User user = presenter.initControlDate();
        if (user != null) {
            Log.i(TAG, "显示要修改的用户信息!");
            viewHolder.userName.setText(user.getUsername());
            viewHolder.phoneNumber.setText(user.getPhonenumber());
            viewHolder.email.setText(user.getEmail());
            if (TodoHelper.UserLogo != null) {
                viewHolder.userlogo.setImageBitmap(BitmapFactory.decodeFile(TodoHelper.UserLogo));
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return true;
    }

    /**
     * 保存修改信息
     *
     * @param view
     */
    public void onClick_submit(View view) {
        if (checkInput()) {
            User user = User.getCurrentUserInfo(TodoHelper.UserId, this);
            if (user != null) {
                user.setUsername(viewHolder.userName.getText().toString().trim());
                user.setEmail(viewHolder.email.getText().toString().trim());
                user.setPhonenumber(viewHolder.phoneNumber.getText().toString().trim());
                user.setIsAlter("1");
                presenter.saveUserInfo(user, this);
            }
        }
    }

    @Override
    public void Exeute(RemoteObserverEvent remoteObserverEvent) {
        if(remoteObserverEvent.object.toString().equals("updateUserLogo")){
            if (remoteObserverEvent.clientResult.isResult()) {
                Toast.makeText(this, "web端更新成功!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void takeSuccess(TResult result){
        File file = new File(result.getImage().getCompressPath());
        Uri uri = Uri.fromFile(file);
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(this.getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        setLogo(bitmap);
        presenter.saveUserLogo(uri, this);

        String fileName = result.getImage().getCompressPath();
        String imageType = "image/"+fileName.substring(fileName.lastIndexOf(".")+1, fileName.length());
        Toast.makeText(this, imageType, Toast.LENGTH_SHORT).show();
        new Remote_User().updateUserLogo(file, imageType, this);
    }

    @Override
    public void takeFail(TResult result, String msg){
        Toast.makeText(TodoHelper.getInstance(), "fail", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void takeCancel(){
        Toast.makeText(TodoHelper.getInstance(), "cancel", Toast.LENGTH_SHORT).show();
    }


    public void onClick_setLogo(View view) {
        final View config = LayoutInflater.from(this).inflate(R.layout.taskphotoconfig, null);
        customHelper = CustomHelper.of(config);

        final CharSequence[] items = {"相册", "拍照"};
        AlertDialog dlg = new AlertDialog.Builder(this).setTitle("更换头像")
                .setItems(items, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 0) {
                            customHelper.onClick(config.findViewById(R.id.btnPickBySelect), getTakePhoto());
                        } else {
                            customHelper.onClick(config.findViewById(R.id.btnPickByTake), getTakePhoto());
                        }
                    }

                }).create();
        dlg.show();
    }

    /**
     * 显示信息
     *
     * @param message
     */
    @Override
    public void showMessage(String message) {
        if (message != null && !message.isEmpty())
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Context getContext() {
        return this;
    }

    /**
     * 检查用户输入
     *
     * @return
     */
    @Override
    public boolean checkInput() {
        if (viewHolder.userName.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "请输入用户名!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (viewHolder.email.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "请输入邮箱!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!new RegisterPresenter(null).isEmail(viewHolder.email.getText().toString().trim())) {
            Toast.makeText(this, "请输入合法的邮箱!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (viewHolder.phoneNumber.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "请输入手机号码!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!new RegisterPresenter(null).isPhoneNumber(viewHolder.phoneNumber.getText().toString().trim())) {
            Toast.makeText(this, "请输入合法的手机号码!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * 处理完最终的照片后，显示照片
     *
     * @param bitmap
     */
    @Override
    public void setLogo(Bitmap bitmap) {
        if (bitmap != null)
            viewHolder.userlogo.setImageBitmap(bitmap);
    }
}
