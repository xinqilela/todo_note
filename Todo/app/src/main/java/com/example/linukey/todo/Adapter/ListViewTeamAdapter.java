package com.example.linukey.todo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.linukey.data.model.local.Team;
import com.example.linukey.todo.R;

import java.util.List;

/**
 * Created by linukey on 12/9/16.
 */

public class ListViewTeamAdapter extends BaseAdapter {

    Context context = null;
    List<Team> datasources = null;

    public ListViewTeamAdapter(Context context, List<Team> datasources){
        this.context = context;
        this.datasources = datasources;
    }

    class ViewHolder{
        ImageView imageView;
        TextView teamname;
    }

    @Override
    public int getCount() {
        return datasources.size();
    }

    @Override
    public Object getItem(int position) {
        return datasources.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View resultView = null;
        if(convertView != null)
            resultView = convertView;
        else{
            resultView = LayoutInflater.from(context).inflate(R.layout.listview_team, null);
        }

        ViewHolder viewHolder = (ViewHolder)resultView.getTag();

        if(viewHolder == null){
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView)resultView.findViewById(R.id.teamImage);
            viewHolder.teamname = (TextView)resultView.findViewById(R.id.teamname);
        }

        viewHolder.imageView.setImageResource(R.mipmap.deleted);
        viewHolder.teamname.setText(datasources.get(position).getTeamname());

        resultView.setTag(viewHolder);

        return resultView;
    }
}
