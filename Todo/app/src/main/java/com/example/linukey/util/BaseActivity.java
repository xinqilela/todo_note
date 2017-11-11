package com.example.linukey.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.linukey.todo.MainActivity;
import com.example.linukey.todo.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by linukey on 17-3-4.
 */

public abstract class BaseActivity extends Activity {
    private static List<Activity> activities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activities.add(this);
    }

    public static MainActivity getMainActivityInstance(){
        for(Activity activity : activities){
            if(activity instanceof MainActivity)
                return (MainActivity)activity;
        }
        return null;
    }

    public static void setIn(Activity activity){
        activities.add(activity);
    }

    public static void FinishAllActivities(){
        for(int i = 0; i < activities.size(); i++){
            if(!activities.get(i).isFinishing()){
                activities.get(i).finish();
            }
        }
    }
}