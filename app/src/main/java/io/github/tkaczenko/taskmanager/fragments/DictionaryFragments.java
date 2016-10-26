package io.github.tkaczenko.taskmanager.fragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;

import java.util.ArrayList;

import io.github.tkaczenko.taskmanager.adapter.ListDictionaryObjectAdapter;
import io.github.tkaczenko.taskmanager.models.DictionaryObject;

/**
 * Created by tkaczenko on 26.10.16.
 */

public class DictionaryFragments extends ListFragment {
    ArrayList<DictionaryObject> mPositions;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPositions = getArguments().getParcelableArrayList("list");
        ListDictionaryObjectAdapter adapter1 = new ListDictionaryObjectAdapter(getActivity(), mPositions);
        setListAdapter(adapter1);
    }
}
