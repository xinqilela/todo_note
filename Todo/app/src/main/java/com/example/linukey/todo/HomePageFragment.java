package com.example.linukey.todo;

import android.app.Fragment;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.linukey.data.source.remote.Remote_User;
import com.example.linukey.todo.Adapter.GridViewHomePageAdapter;
import com.example.linukey.util.TodoHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by linukey on 12/1/16.
 */

public class HomePageFragment extends Fragment
        implements View.OnClickListener, IMainContract.HomePageFragmentView {
    Integer[] menuImages = {R.mipmap.complete, R.mipmap.outtime, R.mipmap.delete, R.mipmap.everyday};
    HomePageFragmentPresenter presenter = new HomePageFragmentPresenter(this);
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homepage, container, false);
        ArrayList<HashMap<String, Integer>> menuList = new ArrayList<>();

        for(int i = 0; i < menuImages.length; i++){
            HashMap<String, Integer> menu = new HashMap<>();
            menu.put("image", menuImages[i]);
            menuList.add(menu);
        }

        int completedTaskCnt = presenter.getCODTaskCnt(TodoHelper.TaskState.get("complete"));
        int overTimeTaskCnt = presenter.getCODTaskCnt(TodoHelper.TaskState.get("outOfDate"));
        int deleteTaskCnt = presenter.getCODTaskCnt("isdelete");

        GridViewHomePageAdapter adapter =
                new GridViewHomePageAdapter(menuImages, getActivity(), this,
                        completedTaskCnt, overTimeTaskCnt, deleteTaskCnt);
        GridView homepage = (GridView)view.findViewById(R.id.homepage);
        homepage.setAdapter(adapter);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getTag().toString()){
            case "completed":
                presenter.initData(TodoHelper.TaskState.get("complete"), getActivity());
                break;
            case "overtime":
                presenter.initData(TodoHelper.TaskState.get("outOfDate"), getActivity());
                break;
            case "deleted":
                presenter.initData("deleted", getActivity());
                break;
            case "everyday":
                break;
        }
    }
}