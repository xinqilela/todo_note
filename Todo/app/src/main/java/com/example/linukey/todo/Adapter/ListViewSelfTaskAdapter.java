package com.example.linukey.todo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.linukey.data.model.local.SelfTask;
import com.example.linukey.todo.R;

import java.util.List;

/**
 * Created by linukey on 11/17/16.
 */

public class ListViewSelfTaskAdapter extends BaseAdapter {
    Context context;
    List<SelfTask> sourceDate;
    String menuname;

    class ViewHolder{
        ImageView image;
        TextView title;
        TextView time;
    }

    public ListViewSelfTaskAdapter(Context context, List<SelfTask> sourceDate, String menuname){
        this.context = context;
        this.sourceDate = sourceDate;
        this.menuname = menuname;
    }

    public ListViewSelfTaskAdapter(Context context, List<SelfTask> sourceDate){
        this.context = context;
        this.sourceDate = sourceDate;
    }

    @Override
    public int getCount() {
        return sourceDate.size();
    }

    @Override
    public Object getItem(int position) {
        return sourceDate.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View resultView;
        ViewHolder viewHolder;

        if(convertView != null)
            resultView = convertView;
        else{
            resultView = LayoutInflater.from(context).inflate(R.layout.listview_selftask, parent, false);
        }

        viewHolder = (ViewHolder)resultView.getTag();

        if(viewHolder == null){
            viewHolder = new ViewHolder();
            viewHolder.image = (ImageView)resultView.findViewById(R.id.taskImage);
            viewHolder.time = (TextView)resultView.findViewById(R.id.time);
            viewHolder.title = (TextView)resultView.findViewById(R.id.title);
        }

        viewHolder.image.setImageResource(R.mipmap.deleted);
        viewHolder.title.setText(sourceDate.get(position).getTitle());
        if(menuname == null || !menuname.equals("box")) {
            viewHolder.time.setText(sourceDate.get(position).getStarttime() + "  " +
                    sourceDate.get(position).getEndtime());
        }else if(menuname.equals("box")){
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)viewHolder.title.getLayoutParams();
            lp.setMargins(0,30,0,0);
            viewHolder.title.setLayoutParams(lp);
        }

        resultView.setTag(viewHolder);
        return resultView;
    }
}
