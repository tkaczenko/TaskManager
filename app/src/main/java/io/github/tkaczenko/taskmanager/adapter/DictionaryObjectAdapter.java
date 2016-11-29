package io.github.tkaczenko.taskmanager.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import io.github.tkaczenko.taskmanager.R;
import io.github.tkaczenko.taskmanager.database.model.dictionary.DictionaryObject;

/**
 * Created by tkaczenko on 02.11.16.
 */

public class DictionaryObjectAdapter extends
        RecyclerView.Adapter<DictionaryObjectAdapter.DictionaryViewHolder> {
    private List<DictionaryObject> mData;
    private OnItemClickListener mListener;

    public DictionaryObjectAdapter(List<DictionaryObject> mData, OnItemClickListener mListener) {
        this.mData = mData;
        this.mListener = mListener;
    }

    public interface OnItemClickListener {
        void onItemClick(DictionaryObject item);
    }

    @Override
    public DictionaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DictionaryViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_dictionary, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(DictionaryViewHolder holder, int position) {
        holder.bind(mData.get(position), mListener);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public DictionaryObject getItem(int position) {
        return mData.get(position);
    }

    class DictionaryViewHolder extends RecyclerView.ViewHolder {
        TextView name, id;

        DictionaryViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.tv_name);
            id = (TextView) itemView.findViewById(R.id.tv_id);
        }

        void bind(
                final DictionaryObject dictionaryObject, final OnItemClickListener listener) {
            name.setText(dictionaryObject.getName());
            id.setText(String.valueOf(dictionaryObject.getId()));
            this.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(dictionaryObject);
                }
            });
        }
    }
}
