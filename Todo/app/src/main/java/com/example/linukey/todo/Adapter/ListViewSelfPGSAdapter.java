package com.example.linukey.todo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.linukey.data.model.local.TaskClassify;
import com.example.linukey.todo.R;

import java.util.List;

/**
 * Created by linukey on 12/4/16.
 */

public class ListViewSelfPGSAdapter extends BaseAdapter {
    Context context;
    List<TaskClassify> datasource;
    ViewHolder viewHolder;

    class ViewHolder{
        TextView title;
        TextView content;
    }

    public ListViewSelfPGSAdapter(Context context, List<TaskClassify> datasource){
        this.context = context;
        this.datasource = datasource;
    }

    @Override
    public int getCount() {
        return datasource.size();
    }

    @Override
    public Object getItem(int position) {
        return datasource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View resultview = null;
        if(convertView == null){
            resultview = LayoutInflater.from(context).inflate(R.layout.listview_selfpgs, parent, false);
        }else{
            resultview = convertView;
        }

        viewHolder = (ViewHolder)resultview.getTag();

        if(viewHolder == null){
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView)resultview.findViewById(R.id.title);
            viewHolder.content = (TextView)resultview.findViewById(R.id.content);
        }

        viewHolder.title.setText(datasource.get(position).getTitle());
        viewHolder.content.setText(datasource.get(position).getContent());

        resultview.setTag(viewHolder);

        return resultview;
    }
}
