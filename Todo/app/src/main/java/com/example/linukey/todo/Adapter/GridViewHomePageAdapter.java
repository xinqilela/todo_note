package com.example.linukey.todo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.linukey.todo.R;

/**
 * Created by linukey on 17-3-9.
 */

public class GridViewHomePageAdapter extends BaseAdapter {

    Context context = null;
    Integer[] menuImages = null;
    View.OnClickListener onClickListener = null;
    int completedTaskCnt = 0;
    int overTimeTaskCnt = 0;
    int deleteTaskCnt = 0;

    public GridViewHomePageAdapter(Integer[] menuImages, Context context, View.OnClickListener on,
                                   int completedTaskCnt, int overTimeTaskCnt, int deleteTaskCnt){
        this.menuImages = menuImages;
        this.context = context;
        this.onClickListener = on;
        this.completedTaskCnt = completedTaskCnt;
        this.overTimeTaskCnt = overTimeTaskCnt;
        this.deleteTaskCnt = deleteTaskCnt;
    }

    class ViewHolder{
        Button imageBtn;
        TextView title;
    }

    @Override
    public int getCount() {
        return menuImages.length;
    }

    @Override
    public Object getItem(int i) {
        return menuImages[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View resultview = null;
        if(convertView == null){
            resultview = LayoutInflater.from(context).inflate(R.layout.gridview_homepage, viewGroup, false);
        }else{
            resultview = convertView;
        }

        ViewHolder viewHolder = (ViewHolder)resultview.getTag();

        if(viewHolder == null){
            viewHolder = new ViewHolder();
            viewHolder.imageBtn = (Button) resultview.findViewById(R.id.imageBtn);
            viewHolder.title = (TextView) resultview.findViewById(R.id.title);
            viewHolder.imageBtn.setOnClickListener(onClickListener);
        }

        viewHolder.imageBtn.setBackgroundResource(menuImages[i]);

        if(menuImages[i] == R.mipmap.complete) {
            viewHolder.title.setText("已完成");
            viewHolder.imageBtn.setTag("completed");
            viewHolder.imageBtn.setText(completedTaskCnt+"");
        }
        else if(menuImages[i] == R.mipmap.outtime) {
            viewHolder.title.setText("过期任务");
            viewHolder.imageBtn.setTag("overtime");
            viewHolder.imageBtn.setText(overTimeTaskCnt+"");
        }
        else if(menuImages[i] == R.mipmap.delete) {
            viewHolder.title.setText("垃圾箱");
            viewHolder.imageBtn.setTag("deleted");
            viewHolder.imageBtn.setText(deleteTaskCnt+"");
        }
        else if(menuImages[i] == R.mipmap.everyday) {
            viewHolder.title.setText("每日任务");
            viewHolder.imageBtn.setTag("everyday");
        }

//        LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) viewHolder.back.getLayoutParams();
//        params.height=height/2;//设置当前控件布局的高度
//        viewHolder.back.setLayoutParams(params);

        resultview.setTag(viewHolder);

        return resultview;
    }
}
