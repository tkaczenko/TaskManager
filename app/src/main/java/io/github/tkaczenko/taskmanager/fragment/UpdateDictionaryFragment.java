package io.github.tkaczenko.taskmanager.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import io.github.tkaczenko.taskmanager.R;
import io.github.tkaczenko.taskmanager.activity.TasksActivity;
import io.github.tkaczenko.taskmanager.database.model.dictionary.DictionaryObject;
import io.github.tkaczenko.taskmanager.database.model.dictionary.DictionaryDAOImp;

/**
 * Created by tkaczenko on 02.11.16.
 */

public class UpdateDictionaryFragment extends Fragment implements View.OnClickListener {
    private EditText etName;
    private DictionaryObject mObject;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mObject = getArguments().getParcelable("object");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_update_dictionary, container, false);
        setUpViews(v);
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnUpdate:
                Class mObjectClass = mObject.getClass();
                DictionaryDAOImp dao = new DictionaryDAOImp(getActivity(), mObjectClass);
                mObject.setName(etName.getText().toString());
                long result = dao.update(mObject);
                if (result > 0) {
                    TasksActivity activity = (TasksActivity) getActivity();
                    activity.onChangeObject();
                }
                break;
        }
    }

    private void setUpViews(View v) {
        etName = (EditText) v.findViewById(R.id.etName);
        String name = mObject.getName();
        etName.setText(name != null ? name : getString(R.string.no_name));

        Button btnUpdate = (Button) v.findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(this);
    }
}
