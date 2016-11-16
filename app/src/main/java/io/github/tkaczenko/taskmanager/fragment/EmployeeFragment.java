package io.github.tkaczenko.taskmanager.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.github.tkaczenko.taskmanager.R;
import io.github.tkaczenko.taskmanager.adapter.EmployeeAdapter;
import io.github.tkaczenko.taskmanager.database.model.Employee;

/**
 * Created by tkaczenko on 16.11.16.
 */
//// TODO: 16.11.16 Implement
public class EmployeeFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private List<Employee> mData;

    private OnItemSelectedListener mListener;

    public interface OnItemSelectedListener {
        void onItemSelected(Employee employee);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnItemSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement " +
                    "OnItemSelected");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dictionary, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.rvDictionary);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(manager);

        mData = new ArrayList<>();

        mData = getArguments().getParcelableArrayList("list");

        //// FIXME: 16.11.16 Rewrite for employee adapter

        return v;
    }
}
