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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.github.tkaczenko.taskmanager.R;
import io.github.tkaczenko.taskmanager.adapter.TaskAdapter;
import io.github.tkaczenko.taskmanager.database.model.Task;
import io.github.tkaczenko.taskmanager.database.repository.TaskDAO;
import io.github.tkaczenko.taskmanager.fragment.interfaces.OnObjectSelectedListener;

/**
 * Created by tkaczenko on 19.11.16.
 */

public class TaskFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private TaskAdapter mAdapter;
    private Activity activity;
    private GetTasksTask task;

    private TaskDAO taskDAO;
    private List<Task> mList;

    private OnObjectSelectedListener<Task> mListener;
    private TaskAdapter.OnItemClickListener mItemClickListener =
            new TaskAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Task task) {
                    mListener.onSelectObject(task);
                }
            };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnObjectSelectedListener<Task>) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement " +
                    "OnTaskSelectedListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        taskDAO = new TaskDAO(activity);
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

        task = new GetTasksTask(getActivity());
        task.execute((Void) null);

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(mSearchListener);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void updateView() {
        task = new GetTasksTask(activity);
        task.execute((Void) null);
    }

    public class GetTasksTask extends AsyncTask<Void, Void, List<Task>> {
        private final WeakReference<Activity> activityWeakRef;

        GetTasksTask(Activity context) {
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
                mList = tasks;
                if (tasks != null) {
                    if (tasks.size() != 0) {
                        mAdapter = new TaskAdapter(tasks, mItemClickListener);
                        mRecyclerView.setAdapter(mAdapter);
                    } else {
                        Toast.makeText(activity, "No Tasks Records",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }

    private SearchView.OnQueryTextListener mSearchListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            newText = newText.toLowerCase();
            List<Task> filteredList = new ArrayList<>();
            for (int i = 0; i < mList.size(); i++) {
                String taskSource = mList.get(i).getTaskSource().getName();
                String taskType = mList.get(i).getTaskType().getName();
                String shortName = mList.get(i).getShortName();
                String description = mList.get(i).getDescription();
                String rejectionReason = mList.get(i).getRejectionReason();
                String sourceDoc = mList.get(i).getSourceDoc();
                String sourceNum = mList.get(i).getSourceNum();
                if (taskSource != null && taskSource.toLowerCase().contains(newText)) {
                    filteredList.add(mList.get(i));
                } else if (taskType != null && taskType.toLowerCase().contains(newText)) {
                    filteredList.add(mList.get(i));
                } else if (shortName != null && shortName.toLowerCase().contains(newText)) {
                    filteredList.add(mList.get(i));
                } else if (sourceDoc != null && sourceDoc.toLowerCase().contains(newText)) {
                    filteredList.add(mList.get(i));
                } else if (sourceNum != null && sourceNum.toLowerCase().contains(newText)) {
                    filteredList.add(mList.get(i));
                } else if (description != null && description.toLowerCase().contains(newText)) {
                    filteredList.add(mList.get(i));
                } else if (rejectionReason != null &&
                        rejectionReason.toLowerCase().contains(newText)) {
                    filteredList.add(mList.get(i));
                }
            }
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mAdapter = new TaskAdapter(filteredList, mItemClickListener);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
            return true;
        }
    };
}
