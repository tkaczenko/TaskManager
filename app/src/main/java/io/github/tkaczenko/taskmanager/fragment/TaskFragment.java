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
import io.github.tkaczenko.taskmanager.adapter.TaskAdapter;
import io.github.tkaczenko.taskmanager.database.model.Task;
import io.github.tkaczenko.taskmanager.database.repository.TaskDAO;

/**
 * Created by tkaczenko on 19.11.16.
 */

public class TaskFragment extends Fragment {
    private RecyclerView mRecyclerView;

    private Activity activity;

    private TaskDAO taskDAO;

    private GetTasksTask task;

    private OnTaskSelectedListener mListener;

    public interface OnTaskSelectedListener {
        void onTaskSelected(Task task);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        taskDAO = new TaskDAO(activity);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnTaskSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement " +
                    "OnTaskSelectedListener");
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

        task = new GetTasksTask(getActivity());
        task.execute((Void) null);

        return v;
    }

    public class GetTasksTask extends AsyncTask<Void, Void, List<Task>> {

        private final WeakReference<Activity> activityWeakRef;

        public GetTasksTask(Activity context) {
            this.activityWeakRef = new WeakReference<>(context);
        }

        @Override
        protected List<Task> doInBackground(Void... arg0) {
            return taskDAO.getAll();
        }

        @Override
        protected void onPostExecute(List<Task> tasks) {
            if (activityWeakRef.get() != null
                    && !activityWeakRef.get().isFinishing()) {
                if (tasks != null) {
                    if (tasks.size() != 0) {
                        TaskAdapter adapter = new TaskAdapter(tasks,
                                new TaskAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(Task task) {
                                        mListener.onTaskSelected(task);
                                    }
                                });
                        mRecyclerView.setAdapter(adapter);
                    } else {
                        Toast.makeText(activity, "No Tasks Records",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }

    public void updateView() {
        task = new GetTasksTask(activity);
        task.execute((Void) null);
    }
}
