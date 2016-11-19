package io.github.tkaczenko.taskmanager.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import io.github.tkaczenko.taskmanager.R;
import io.github.tkaczenko.taskmanager.database.model.dictionary.DictionaryObject;

/**
 * Created by tkaczenko on 02.11.16.
 */

public class UpdateDictionaryFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_update_dictionary, container, false);
        EditText editText = (EditText) v.findViewById(R.id.et_name);
        DictionaryObject dictionaryObject = getArguments().getParcelable("object");
        String name = dictionaryObject.getName();
        editText.setText(name != null ? name : getString(R.string.no_name));
        return v;
    }
}
