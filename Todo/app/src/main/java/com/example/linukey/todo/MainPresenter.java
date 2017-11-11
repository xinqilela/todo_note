package com.example.linukey.todo;

import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;

import com.example.linukey.util.TodoHelper;

/**
 * Created by linukey on 12/8/16.
 */

public class MainPresenter implements IMainContract.Presenter {
    private final IMainContract.View todoView;

    public MainPresenter(IMainContract.View todoView){
        this.todoView = todoView;
    }

    @Override
    public void CreateMenu(Menu menu){
        MenuItem taskAdd = menu.add(0,0,0, "添加任务");
        taskAdd.setIcon(R.mipmap.add);
        taskAdd.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    }

    @Override
    public boolean MenuChoice(MenuItem item, Context context){
        switch (item.getItemId()){
            case 0:
                todoView.addSelfTask();
                todoView.addTeamTask();
                return true;
        }
        return false;
    }
}
