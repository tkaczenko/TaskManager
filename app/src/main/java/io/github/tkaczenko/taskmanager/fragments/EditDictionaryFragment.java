package io.github.tkaczenko.taskmanager.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import io.github.tkaczenko.taskmanager.R;
import io.github.tkaczenko.taskmanager.models.DictionaryObject;

/**
 * Created by tkaczenko on 02.11.16.
 */

public class EditDictionaryFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_dictionary, container, false);
        EditText editText = (EditText) v.findViewById(R.id.et_name);
        DictionaryObject dictionaryObject = getArguments().getParcelable("object");
        editText.setText(dictionaryObject.getName());
        return v;
    }
}
