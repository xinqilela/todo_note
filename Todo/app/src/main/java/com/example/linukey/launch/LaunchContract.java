package com.example.linukey.launch;

import android.content.Context;
import android.content.Intent;

import com.example.linukey.data.model.remote.WebSelfTask;

import java.util.List;

/**
 * Created by linukey on 12/20/16.
 */

public interface LaunchContract {
    interface LaunchPresenter{

        void initLaunchPic(Context context);

        void initLocalData(Context context);

        void initLocalSelfTasksFromWeb(Context context);

        void initLocalProjectsFromWeb(Context context);

        void initLocalGoalsFromWeb(Context context);

        void initLocalSightsFromWeb(Context context);

        void initLocalTeamsFromWeb(Context context);

        void initLocalTeamJoinDatasFromWeb(Context context);

        void initLocalTeamTasksFromWeb(Context context);

        void initLocalUserLogoFromWeb(Context context);

        // void initLocalTeamJoinResultInfosFromWeb(Context context);
    }

    interface LaunchView{
        void showActivity(Intent intent);
    }
}
