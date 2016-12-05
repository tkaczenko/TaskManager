package io.github.tkaczenko.taskmanager.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import io.github.tkaczenko.taskmanager.R;
import io.github.tkaczenko.taskmanager.database.models.dictionary.common.Dictionary;

/**
 * Created by tkaczenko on 02.11.16.
 */

public class DictionaryAdapter extends
        RecyclerView.Adapter<DictionaryAdapter.DictionaryViewHolder> {
    private List<Dictionary> mData;
    private OnItemClickListener mListener;

    public DictionaryAdapter(List<Dictionary> mData, OnItemClickListener mListener) {
        this.mData = mData;
        this.mListener = mListener;
    }

    public interface OnItemClickListener {
        void onItemClick(Dictionary item);
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

    public Dictionary getItem(int position) {
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
                final Dictionary dictionary, final OnItemClickListener listener) {
            name.setText(dictionary.getName());
            id.setText(String.valueOf(dictionary.getId()));
            this.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(dictionary);
                }
            });
        }
    }
}
