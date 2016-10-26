package io.github.tkaczenko.taskmanager.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import io.github.tkaczenko.taskmanager.R;
import io.github.tkaczenko.taskmanager.models.Position;

/**
 * Created by tkaczenko on 26.10.16.
 */

public class ListPositionAdapter extends BaseAdapter {
    private Context mContext;
    private List<Position> mPositionList;

    public ListPositionAdapter(Context mContext, List<Position> mPositionList) {
        this.mContext = mContext;
        this.mPositionList = mPositionList;
    }

    @Override
    public int getCount() {
        return mPositionList.size();
    }

    @Override
    public Object getItem(int position) {
        return mPositionList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mPositionList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = View.inflate(mContext, R.layout.lv_item, null);
        TextView tvName = (TextView)v.findViewById(R.id.tv_position_id);
        TextView tvPrice = (TextView)v.findViewById(R.id.tv_position_name);
        tvName.setText(mPositionList.get(position).getName());
        tvPrice.setText(String.valueOf(mPositionList.get(position).getName()) + " $");
        return v;
    }
}