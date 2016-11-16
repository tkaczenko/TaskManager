package io.github.tkaczenko.taskmanager.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import io.github.tkaczenko.taskmanager.database.model.Employee;

/**
 * Created by tkaczenko on 16.11.16.
 */
//// TODO: 16.11.16 Implement adapter
public class EmployeeAdapter extends
        RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder> {
    private List<Employee> mData;
    private OnItemClickListener onItemClickListener;

    public EmployeeAdapter(List<Employee> data, OnItemClickListener listener) {
        this.mData = data;
        this.onItemClickListener = listener;
    }

    @Override
    public EmployeeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(EmployeeViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public interface OnItemClickListener {
        void OnItemClick(Employee employee);
    }

    public class EmployeeViewHolder extends RecyclerView.ViewHolder {

        public EmployeeViewHolder(View itemView) {
            super(itemView);
        }
    }
}
