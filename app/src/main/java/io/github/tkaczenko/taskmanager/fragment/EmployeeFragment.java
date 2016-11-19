package io.github.tkaczenko.taskmanager.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.List;

import io.github.tkaczenko.taskmanager.R;
import io.github.tkaczenko.taskmanager.adapter.EmployeeAdapter;
import io.github.tkaczenko.taskmanager.database.model.Employee;
import io.github.tkaczenko.taskmanager.database.repository.EmployeeDAO;

/**
 * Created by tkaczenko on 16.11.16.
 */

public class EmployeeFragment extends Fragment {
    private RecyclerView mRecyclerView;

    private Activity activity;

    private EmployeeDAO employeeDAO;

    private GetEmpTask task;

    private OnEmployeeSelectedListener mListener;

    public interface OnEmployeeSelectedListener {
        void onEmployeeSelected(Employee employee);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        employeeDAO = new EmployeeDAO(activity);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnEmployeeSelectedListener) context;
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

        task = new GetEmpTask(activity);
        task.execute((Void) null);

        return v;
    }

    public class GetEmpTask extends AsyncTask<Void, Void, List<Employee>> {

        private final WeakReference<Activity> activityWeakRef;

        public GetEmpTask(Activity context) {
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
                if (empList != null) {
                    if (empList.size() != 0) {
                        EmployeeAdapter adapter = new EmployeeAdapter(empList,
                                new EmployeeAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(Employee employee) {
                                        mListener.onEmployeeSelected(employee);
                                    }
                                });
                        mRecyclerView.setAdapter(adapter);
                    } else {
                        Toast.makeText(activity, "No Employee Records",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }

    /*
     * This method is invoked from MainActivity onFinishDialog() method. It is
     * called from CustomEmpDialogFragment when an employee record is updated.
     * This is used for communicating between fragments.
     */
    public void updateView() {
        task = new GetEmpTask(activity);
        task.execute((Void) null);
    }
}
