package io.github.tkaczenko.taskmanager.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import io.github.tkaczenko.taskmanager.R;
import io.github.tkaczenko.taskmanager.models.DictionaryObject;

/**
 * Created by tkaczenko on 26.10.16.
 */

public class ListDictionaryObjectAdapter extends BaseAdapter {
    private Context mContext;
    private List<DictionaryObject> dictionaryObjects;

    public ListDictionaryObjectAdapter(Context mContext, List<DictionaryObject> dictionaryObjects) {
        this.mContext = mContext;
        this.dictionaryObjects = dictionaryObjects;
    }

    @Override
    public int getCount() {
        return dictionaryObjects.size();
    }

    @Override
    public Object getItem(int position) {
        return dictionaryObjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return dictionaryObjects.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(mContext, R.layout.dictionary_item, null);
        TextView tvName = (TextView)v.findViewById(R.id.tv_name);
        tvName.setText(dictionaryObjects.get(position).getName());
        return v;
    }
}