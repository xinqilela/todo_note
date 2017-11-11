package com.example.linukey.addedit_userinfo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import com.example.linukey.data.model.local.User;

import java.io.File;

/**
 * Created by linukey on 12/23/16.
 */

public interface AddEditUserInfoContract {
    interface AddEditUserInfoView{

        void initEdit();

        void showMessage(String message);

        Context getContext();

        boolean checkInput();

        void setLogo(Bitmap bitmap);
    }
    interface AddEditUserInfoPresenter{

        User initControlDate();

        void saveUserInfo(User user, Context context);

        void saveUserLogo(Uri uri, Context context);
    }
}
