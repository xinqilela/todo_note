package com.example.linukey.todo.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.linukey.data.model.local.TeamJoinInfo;
import com.example.linukey.todo.R;
import com.example.linukey.util.TodoHelper;

import java.util.List;

/**
 * Created by linukey on 17-3-3.
 */

public class ListViewTeamJoinInfoAdapter extends BaseAdapter {

    private List<TeamJoinInfo> dataSource;
    private Context context;
    View.OnClickListener onClickListener;

    public ListViewTeamJoinInfoAdapter(Context context, List<TeamJoinInfo> dataSource){
        this.context = context;
        this.dataSource = dataSource;
    }

    class ViewHolder{
        ImageView notificationImage;
        TextView teamname;
        TextView man;
        Button result;
    }

    public void setAgreeOnClick(View.OnClickListener onClick){
        this.onClickListener = onClick;
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
        View resultView = null;
        if(view != null)
            resultView = view;
        else{
            resultView = LayoutInflater.from(context).inflate(R.layout.listview_teamjoininfo, null);
        }

        ViewHolder viewHolder = (ViewHolder)resultView.getTag();

        if(viewHolder == null){
            viewHolder = new ViewHolder();
            viewHolder.notificationImage = (ImageView)resultView.findViewById(R.id.notificationImage);
            viewHolder.teamname = (TextView)resultView.findViewById(R.id.teamname);
            viewHolder.man = (TextView)resultView.findViewById(R.id.man);
            viewHolder.result = (Button)resultView.findViewById(R.id.result);
            viewHolder.result.setOnClickListener(onClickListener);
        }

        String type = dataSource.get(i).getExecType();
        viewHolder.notificationImage.setImageResource(R.mipmap.deleted);
        viewHolder.teamname.setText(dataSource.get(i).getTeamName());

        //申请信息
        if(type.equals(TodoHelper.TeamJoinType.get("2"))){
            viewHolder.man.setText(dataSource.get(i).getFromUserName());
        }else{//结果信息
            viewHolder.man.setText(dataSource.get(i).getToUserName());
            viewHolder.result.setBackgroundResource(R.drawable.teamjoinresult0or1back);
            viewHolder.result.setTextColor(Color.parseColor("#3B3D41"));
            if(type.equals(TodoHelper.TeamJoinType.get("1")))
                viewHolder.result.setText("已同意");
            else if(type.equals(TodoHelper.TeamJoinType.get("0")))
                viewHolder.result.setText("已拒绝");
            else if(type.equals(TodoHelper.TeamJoinType.get("3"))) {
                viewHolder.result.setText("已退出");
                viewHolder.man.setText(dataSource.get(i).getFromUserName());
            }
        }
        viewHolder.result.setTag(i);


        resultView.setTag(viewHolder);

        return resultView;
    }
}
