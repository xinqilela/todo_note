package com.example.linukey.todo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.linukey.data.model.remote.WebTeam;
import com.example.linukey.todo.R;
import com.example.linukey.util.TodoHelper;

import java.util.List;

/**
 * Created by linukey on 17-2-27.
 */

public class ListViewTeamSearchAdapter extends BaseAdapter {
    Context context;
    List<WebTeam> dataSource;
    View.OnClickListener onClickListener;

    class ViewHolder{
        ImageView teamImage;
        TextView teamName;
        TextView leader;
        Button btnJoin;
    }

    public ListViewTeamSearchAdapter(Context context, List<WebTeam> dataSource, View.OnClickListener onClickListener){
        this.context = context;
        this.dataSource = dataSource;
        this.onClickListener = onClickListener;
    }

    @Override
    public int getCount() {
        return dataSource.size();
    }

    @Override
    public Object getItem(int i) {
        return dataSource.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View resultView;
        ViewHolder viewHolder;

        if(view != null)
            resultView = view;
        else{
            resultView = LayoutInflater.from(context).inflate(R.layout.listview_teamsearchresult,
                    viewGroup, false);
        }

        viewHolder = (ViewHolder)resultView.getTag();

        if(viewHolder == null){
            viewHolder = new ViewHolder();
            viewHolder.teamImage = (ImageView)resultView.findViewById(R.id.teamImage);
            viewHolder.teamName = (TextView)resultView.findViewById(R.id.teamname);
            viewHolder.leader = (TextView)resultView.findViewById(R.id.leader);
            viewHolder.btnJoin = (Button)resultView.findViewById(R.id.teamJoin);
            viewHolder.btnJoin.setOnClickListener(onClickListener);
        }

        viewHolder.teamImage.setImageResource(R.mipmap.deleted);
        viewHolder.teamName.setText(dataSource.get(i).getTeamname());
        viewHolder.leader.setText(dataSource.get(i).getLeaderName());
        viewHolder.btnJoin.setTag(i);

        resultView.setTag(viewHolder);
        return resultView;
    }

}
