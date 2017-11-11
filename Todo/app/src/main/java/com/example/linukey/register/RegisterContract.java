package com.example.linukey.register;

import android.content.Context;

import com.example.linukey.data.model.local.User;
import com.example.linukey.data.source.remote.ClientResult;

/**
 * Created by linukey on 12/20/16.
 */

public interface RegisterContract {
    interface RegisterPresenter{

        boolean isEmail(String strEmail);

        boolean isPhoneNumber(String phoneNumber);

        void saveUserInfo(User user, Context context);
    }
    interface RegisterView{

        void executeResult(ClientResult clientResult);

        boolean checkInput();

        void saveUserInfo();
    }
}
