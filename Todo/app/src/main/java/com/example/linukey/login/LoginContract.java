package com.example.linukey.login;

import android.content.Context;
import android.content.Intent;

/**
 * Created by linukey on 12/20/16.
 */

public interface LoginContract {
    interface LoginPresenter{

        void checkLoginInfo(String username, String password, Context context);
    }
    interface LoginView{

        void showMessage(String message);

        void showMainActivity(Intent intent);

        Context getContext();

        boolean checkInput();
    }
}