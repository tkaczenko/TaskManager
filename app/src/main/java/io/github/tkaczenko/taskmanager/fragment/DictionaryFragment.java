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
import io.github.tkaczenko.taskmanager.adapter.DictionaryObjectAdapter;
import io.github.tkaczenko.taskmanager.database.model.dictionary.DictionaryObject;
import io.github.tkaczenko.taskmanager.database.repository.DictionaryDAO;
import io.github.tkaczenko.taskmanager.dialog.AddDictionaryObjectDialog;
import io.github.tkaczenko.taskmanager.fragment.interfaces.OnObjectSelectedListener;

/**
 * Created by tkaczenko on 02.11.16.
 */

public class DictionaryFragment<T extends DictionaryObject> extends Fragment {
    private RecyclerView mRecyclerView;
    private DictionaryObjectAdapter mAdapter;
    private Activity mActivity;
    private GetDicTask mTask;

    private Class<T> dictionaryObjectClass;
    private DictionaryDAO<T> dictionaryDAO;
    private List<T> mList;

    private OnObjectSelectedListener<DictionaryObject> mListener;
    private DictionaryObjectAdapter.OnItemClickListener mItemClickListener =
            new DictionaryObjectAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(DictionaryObject item) {
                    mListener.onSelectObject(item);
                }
            };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnObjectSelectedListener<DictionaryObject>) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement " +
                    "OnDictionaryObjectSelected");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        dictionaryDAO = new DictionaryDAO<>(mActivity, dictionaryObjectClass);
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

        mTask = new GetDicTask(mActivity);
        mTask.execute((Void) null);

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
        mTask = new GetDicTask(mActivity);
        mTask.execute((Void) null);
    }

    public void setDictionaryObjectClass(Class<T> type) {
        this.dictionaryObjectClass = type;
    }

    public class GetDicTask extends AsyncTask<Void, Void, List<T>> {
        private final WeakReference<Activity> activityWeakRef;

        GetDicTask(Activity context) {
            this.activityWeakRef = new WeakReference<>(context);
        }

        @Override
        protected List<T> doInBackground(Void... arg0) {
            return dictionaryDAO.getAll();
        }

        @Override
        protected void onPostExecute(List<T> dicList) {
            if (activityWeakRef.get() != null
                    && !activityWeakRef.get().isFinishing()) {
                mList = dicList;
                if (dicList != null) {
                    if (dicList.size() != 0) {
                        mAdapter = new DictionaryObjectAdapter(
                                (List<DictionaryObject>) dicList, mItemClickListener
                        );
                        mRecyclerView.setAdapter(mAdapter);
                    } else {
                        Toast.makeText(mActivity, "No " + dictionaryObjectClass.getName() +
                                " Records", Toast.LENGTH_LONG)
                                .show();
                    }
                }
            }
        }
    }

    private void showAddDialog() {
        AddDictionaryObjectDialog<T> fragment = new AddDictionaryObjectDialog<>();
        fragment.setClazz(dictionaryObjectClass);
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
            List<T> filteredList = new ArrayList<>();
            for (int i = 0; i < mList.size(); i++) {
                String name = mList.get(i).getName();
                if (name != null && name.toLowerCase().contains(newText)) {
                    filteredList.add(mList.get(i));
                }
            }
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mAdapter = new DictionaryObjectAdapter(
                    (List<DictionaryObject>) filteredList, mItemClickListener
            );
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
            return true;
        }
    };
}
