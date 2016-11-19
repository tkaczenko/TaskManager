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
import io.github.tkaczenko.taskmanager.adapter.DictionaryObjectAdapter;
import io.github.tkaczenko.taskmanager.database.model.dictionary.DictionaryObject;
import io.github.tkaczenko.taskmanager.database.repository.DictionaryDAO;

/**
 * Created by tkaczenko on 02.11.16.
 */

public class DictionaryFragment<T extends DictionaryObject> extends Fragment {
    private RecyclerView mRecyclerView;

    private Activity mActivity;

    private DictionaryDAO<T> dictionaryDAO;
    private Class<T> dictionaryObjectClass;

    private OnDictionaryObjectSelectedListener mListener;

    public interface OnDictionaryObjectSelectedListener {
        void onDictionaryObjectSelected(DictionaryObject object);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        dictionaryDAO = new DictionaryDAO<T>(mActivity, dictionaryObjectClass);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnDictionaryObjectSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement " +
                    "OnDictionaryObjectSelected");
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

        GetDicTask task = new GetDicTask(mActivity);
        task.execute((Void) null);

        return v;
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
                if (dicList != null) {
                    if (dicList.size() != 0) {
                        DictionaryObjectAdapter adapter = new DictionaryObjectAdapter(
                                (List<DictionaryObject>) dicList,
                                new DictionaryObjectAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(DictionaryObject item) {
                                        mListener.onDictionaryObjectSelected(item);
                                    }
                                });
                        mRecyclerView.setAdapter(adapter);
                    } else {
                        Toast.makeText(mActivity, "No " + dictionaryObjectClass.getName() +
                                " Records", Toast.LENGTH_LONG)
                                .show();
                    }
                }
            }
        }
    }
}
