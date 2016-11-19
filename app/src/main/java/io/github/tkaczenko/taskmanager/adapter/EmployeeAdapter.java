package io.github.tkaczenko.taskmanager.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import io.github.tkaczenko.taskmanager.R;
import io.github.tkaczenko.taskmanager.database.model.Employee;

/**
 * Created by tkaczenko on 16.11.16.
 */

public class EmployeeAdapter extends
        RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder> {
    private List<Employee> mData;
    private OnItemClickListener mListener;

    public EmployeeAdapter(List<Employee> data, OnItemClickListener listener) {
        this.mData = data;
        this.mListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(Employee employee);
    }

    @Override
    public EmployeeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EmployeeViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_employee, parent, false
                )
        );
    }

    @Override
    public void onBindViewHolder(EmployeeViewHolder holder, int position) {
        holder.bind(mData.get(position), mListener);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class EmployeeViewHolder extends RecyclerView.ViewHolder {
        TextView tvSurname, tvName, tvPosition, tvDepartment, tvID;

        EmployeeViewHolder(View itemView) {
            super(itemView);

            tvSurname = (TextView) itemView.findViewById(R.id.tvSurname);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvPosition = (TextView) itemView.findViewById(R.id.tvPosition);
            tvDepartment = (TextView) itemView.findViewById(R.id.tvDepartment);
            tvID = (TextView) itemView.findViewById(R.id.tvNum);
        }

        void bind(
                final Employee employee, final OnItemClickListener listener
        ) {
            tvSurname.setText(employee.getLastName());
            tvName.setText(employee.getFirstName());
            tvPosition.setText(employee.getPosition().getName());
            tvDepartment.setText(employee.getDepartment().getName());
            tvID.setText(String.valueOf(employee.getId()));
            this.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(employee);
                }
            });
        }
    }
}
