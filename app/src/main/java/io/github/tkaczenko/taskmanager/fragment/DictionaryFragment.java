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
import io.github.tkaczenko.taskmanager.adapter.DictionaryObjectAdapter;
import io.github.tkaczenko.taskmanager.database.model.dictionary.DictionaryObject;

/**
 * Created by tkaczenko on 02.11.16.
 */

public class DictionaryFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private List<DictionaryObject> mObjects;

    private OnDictionaryObjectSelectedListener mListener;

    public interface OnDictionaryObjectSelectedListener {
        void onDictionaryObjectSelected(DictionaryObject object);
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

        mObjects = new ArrayList<>();

        mObjects = getArguments().getParcelableArrayList("list");
        DictionaryObjectAdapter adapter = new DictionaryObjectAdapter(mObjects,
                new DictionaryObjectAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(DictionaryObject item) {
                        mListener.onDictionaryObjectSelected(item);
                    }
                });
        mRecyclerView.setAdapter(adapter);

        return v;
    }
}
