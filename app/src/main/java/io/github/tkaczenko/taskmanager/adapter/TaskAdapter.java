package io.github.tkaczenko.taskmanager.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.List;

import io.github.tkaczenko.taskmanager.R;
import io.github.tkaczenko.taskmanager.database.model.task.Task;

/**
 * Created by tkaczenko on 19.11.16.
 */

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private List<Task> mData;
    private OnItemClickListener mListener;

    public TaskAdapter(List<Task> data, OnItemClickListener listener) {
        this.mData = data;
        this.mListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(Task task);
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TaskViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_task, parent, false
                )
        );
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        holder.bind(mData.get(position), mListener);
    }

    public Task getItem(int position) {
        return mData.get(position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {
        private TextView tvShortName, tvCompleted, tvCanceled, tvDateIssue, tvDatePlanned;

        private final DateFormat formatter = DateFormat.getDateInstance(DateFormat.SHORT);

        TaskViewHolder(View itemView) {
            super(itemView);

            tvShortName = (TextView) itemView.findViewById(R.id.tvShortName);
            tvCompleted = (TextView) itemView.findViewById(R.id.tvCompleted);
            tvCanceled = (TextView) itemView.findViewById(R.id.tvCanceled);
            tvDateIssue = (TextView) itemView.findViewById(R.id.tvDateIssue);
            tvDatePlanned = (TextView) itemView.findViewById(R.id.tvDatePlanned);
        }

        void bind(final Task task, final OnItemClickListener listener) {
            tvShortName.setText(task.getShortName());
            tvCompleted.setText(task.isCompleted() ? "Completed" : "Not completed");
            tvCanceled.setText(task.isCanceled() ? "Canceled" : "");
            tvDateIssue.setText(formatter.format(task.getDateIssue()));
            tvDatePlanned.setText(formatter.format(task.getDatePlanned()));
            this.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(task);
                }
            });
        }

    }
}
