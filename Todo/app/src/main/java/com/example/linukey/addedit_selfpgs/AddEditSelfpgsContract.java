package com.example.linukey.addedit_selfpgs;

import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;

import com.example.linukey.data.model.local.TaskClassify;

import java.text.ParseException;

/**
 * Created by linukey on 12/9/16.
 */

public interface AddEditSelfpgsContract {
    interface AddEditSelfpgsView{
        void viewFinish();

        void initEdit(TaskClassify taskClassify);

        boolean checkInput();

        boolean saveProject();

        boolean saveGoal();

        boolean saveSight();

        boolean savePGS(String menuName);
    }
    interface AddEditSelfpgsPresenter{
        boolean saveSight(boolean isEdit, Context context, String title, String content,
                          String selfId, String userId, int preId, String isdelete);

        boolean saveGoal(boolean isEdit, Context context, String title, String content, String selfId
                , String state, String userId, int preId, String isdelete);

        boolean saveProject(boolean isEdit, String menuName, Context context, String title, String content, String selfId,
                            String state, String userId, int preId, String isdelete);

        boolean MenuChoice(MenuItem item, Activity activity, String menuName) throws ParseException;

        void CreateMenu(Menu menu);
    }
}
