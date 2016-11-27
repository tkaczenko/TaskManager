package io.github.tkaczenko.taskmanager.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.github.tkaczenko.taskmanager.R;
import io.github.tkaczenko.taskmanager.adapter.EmployeeAdapter;
import io.github.tkaczenko.taskmanager.database.model.Employee;
import io.github.tkaczenko.taskmanager.database.repository.EmployeeDAO;
import io.github.tkaczenko.taskmanager.dialog.AddEmpDialog;
import io.github.tkaczenko.taskmanager.fragment.interfaces.OnObjectSelectedListener;

/**
 * Created by tkaczenko on 16.11.16.
 */

public class EmployeeFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private EmployeeAdapter mAdapter;
    private Activity activity;
    private GetEmpTask task;

    private EmployeeDAO employeeDAO;
    private List<Employee> mList;

    private OnObjectSelectedListener<Employee> mListener;
    private EmployeeAdapter.OnItemClickListener mItemClickListener =
            new EmployeeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Employee employee) {
                    mListener.onSelectObject(employee);
                }
            };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnObjectSelectedListener<Employee>) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement " +
                    "OnItemSelected");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        employeeDAO = new EmployeeDAO(activity);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dictionary, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.rvDictionary);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(manager);

        task = new GetEmpTask(activity);
        task.execute((Void) null);

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(mSearchListener);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                showAddDialog();
                break;
        }
        return true;
    }

    public void updateView() {
        task = new GetEmpTask(activity);
        task.execute((Void) null);
    }

    public class GetEmpTask extends AsyncTask<Void, Void, List<Employee>> {
        private final WeakReference<Activity> activityWeakRef;

        GetEmpTask(Activity context) {
            this.activityWeakRef = new WeakReference<>(context);
        }

        @Override
        protected List<Employee> doInBackground(Void... arg0) {
            return employeeDAO.getAll();
        }

        @Override
        protected void onPostExecute(List<Employee> empList) {
            if (activityWeakRef.get() != null
                    && !activityWeakRef.get().isFinishing()) {
                mList = empList;
                if (empList != null) {
                    if (empList.size() != 0) {
                        mAdapter = new EmployeeAdapter(empList, mItemClickListener);
                        mRecyclerView.setAdapter(mAdapter);
                    } else {
                        Toast.makeText(activity, "No Employee Records",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }

    private void showAddDialog() {
        AddEmpDialog fragment = new AddEmpDialog();
        fragment.show(getActivity().getSupportFragmentManager(), "add_to_dictionary");
    }

    private SearchView.OnQueryTextListener mSearchListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            newText = newText.toLowerCase();
            List<Employee> filteredList = new ArrayList<>();
            for (int i = 0; i < mList.size(); i++) {
                String id = Integer.toString(mList.get(i).getId());
                String lastName = mList.get(i).getLastName();
                String middleName = mList.get(i).getMidName();
                String firstName = mList.get(i).getFirstName();
                String phoneNum = mList.get(i).getContact().getPhoneNum();
                String email = mList.get(i).getContact().getEmail();
                String position = mList.get(i).getPosition().getName();
                String department = mList.get(i).getDepartment().getName();
                if (id != null && id.contains(newText)) {
                    filteredList.add(mList.get(i));
                } else if (lastName != null && lastName.toLowerCase().contains(newText)) {
                    filteredList.add(mList.get(i));
                } else if (middleName != null && middleName.toLowerCase().contains(newText)) {
                    filteredList.add(mList.get(i));
                } else if (firstName != null && firstName.toLowerCase().contains(newText)) {
                    filteredList.add(mList.get(i));
                } else if (phoneNum != null && phoneNum.contains(newText)) {
                    filteredList.add(mList.get(i));
                } else if (email != null && email.contains(newText)) {
                    filteredList.add(mList.get(i));
                } else if (position != null && position.toLowerCase().contains(newText)) {
                    filteredList.add(mList.get(i));
                } else if (department != null && department.toLowerCase().contains(newText)) {
                    filteredList.add(mList.get(i));
                }
            }
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mAdapter = new EmployeeAdapter(filteredList, mItemClickListener);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
            return true;
        }
    };
}
