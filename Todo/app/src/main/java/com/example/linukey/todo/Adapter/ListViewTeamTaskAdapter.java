package com.example.linukey.todo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.linukey.data.model.local.TeamTask;
import com.example.linukey.todo.R;

import java.util.List;

/**
 * Created by linukey on 12/9/16.
 */

public class ListViewTeamTaskAdapter extends BaseAdapter {
    final Context context;
    List<TeamTask> sourceDate = null;

    class ViewHolder {
        ImageView image;
        TextView title;
        TextView time;
    }

    public ListViewTeamTaskAdapter(Context context, List<TeamTask> teamTasks) {
        this.context = context;
        this.sourceDate = teamTasks;
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

        if (convertView != null)
            resultView = convertView;
        else {
            resultView = LayoutInflater.from(context).inflate(R.layout.listview_teamtask, parent, false);
        }

        viewHolder = (ViewHolder) resultView.getTag();

        if (viewHolder == null) {
            viewHolder = new ViewHolder();
            viewHolder.image = (ImageView) resultView.findViewById(R.id.taskImage);
            viewHolder.time = (TextView) resultView.findViewById(R.id.time);
            viewHolder.title = (TextView) resultView.findViewById(R.id.title);
        }

        viewHolder.image.setImageResource(R.mipmap.deleted);
        viewHolder.title.setText(sourceDate.get(position).getTitle());
        viewHolder.time.setText(sourceDate.get(position).getStarttime() + "  " +
                sourceDate.get(position).getEndtime());

        resultView.setTag(viewHolder);
        return resultView;
    }
}
