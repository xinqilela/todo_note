package com.example.linukey.todo;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.linukey.selfpgs.SelfPGSActivity;
import com.example.linukey.selfpgs.SelfPGSContract;
import com.example.linukey.selftask.SelfTaskActivity;

/**
 * Created by linukey on 12/1/16.
 */

public class SelfTaskMenuFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_selftask, container, false);

        Button btnToday = (Button)view.findViewById(R.id.btnToday);
        btnToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SelfTaskActivity.class);
                intent.putExtra("menuname", "today");
                startActivity(intent);
            }
        });

        Button btnTomorrow = (Button)view.findViewById(R.id.btnTomorrow);
        btnTomorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SelfTaskActivity.class);
                intent.putExtra("menuname", "tomorrow");
                startActivity(intent);
            }
        });

        Button btnNext = (Button)view.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SelfTaskActivity.class);
                intent.putExtra("menuname", "next");
                startActivity(intent);
            }
        });

        Button btnBox = (Button)view.findViewById(R.id.btnBox);
        btnBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SelfTaskActivity.class);
                intent.putExtra("menuname", "box");
                startActivity(intent);
            }
        });

        Button btnProject = (Button)view.findViewById(R.id.btnProject);
        btnProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SelfPGSActivity.class);
                intent.putExtra("menuname", "project");
                startActivity(intent);
            }
        });

        Button btnGoal = (Button)view.findViewById(R.id.btnGoal);
        btnGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SelfPGSActivity.class);
                intent.putExtra("menuname", "goal");
                startActivity(intent);
            }
        });

        Button btnSight = (Button)view.findViewById(R.id.btnSight);
        btnSight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SelfPGSActivity.class);
                intent.putExtra("menuname", "sight");
                startActivity(intent);
            }
        });

        return view;
    }
}
