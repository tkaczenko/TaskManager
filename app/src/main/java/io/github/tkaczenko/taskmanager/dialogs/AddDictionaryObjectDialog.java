package io.github.tkaczenko.taskmanager.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import io.github.tkaczenko.taskmanager.R;
import io.github.tkaczenko.taskmanager.activities.TasksActivity;
import io.github.tkaczenko.taskmanager.database.models.dictionary.Department;
import io.github.tkaczenko.taskmanager.database.models.dictionary.Position;
import io.github.tkaczenko.taskmanager.database.models.dictionary.TaskSource;
import io.github.tkaczenko.taskmanager.database.models.dictionary.TaskType;
import io.github.tkaczenko.taskmanager.database.models.dictionary.common.Dictionary;
import io.github.tkaczenko.taskmanager.database.models.dictionary.common.DictionaryDAOImp;

/**
 * Created by tkaczenko on 27.11.16.
 */

public class AddDictionaryObjectDialog<T extends Dictionary> extends DialogFragment {
    private EditText etName;
    private Class<T> clazz;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_add_dictionary, null);
        etName = (EditText) v.findViewById(R.id.etName);

        builder.setView(v)
                .setTitle(R.string.dialog_add_dictionary)
                .setPositiveButton(R.string.btn_add_title, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = etName.getText().toString();
                        long result = 0;
                        if (clazz == Position.class) {
                            Position position = new Position(name);
                            DictionaryDAOImp<Position> dao = new DictionaryDAOImp<>(
                                    getActivity(), Position.class
                            );
                            result = dao.save(position);
                        } else if (clazz == Department.class) {
                            Department department = new Department(name);
                            DictionaryDAOImp<Department> dao = new DictionaryDAOImp<>(
                                    getActivity(), Department.class
                            );
                            result = dao.save(department);
                        } else if (clazz == TaskSource.class) {
                            TaskSource taskSource = new TaskSource(name);
                            DictionaryDAOImp<TaskSource> dao = new DictionaryDAOImp<>(
                                    getActivity(), TaskSource.class
                            );
                            result = dao.save(taskSource);
                        } else if (clazz == TaskType.class) {
                            TaskType taskType = new TaskType(name);
                            DictionaryDAOImp<TaskType> dao = new DictionaryDAOImp<>(
                                    getActivity(), TaskType.class
                            );
                            result = dao.save(taskType);
                        }
                        if (result > 0) {
                            TasksActivity activity = (TasksActivity) getActivity();
                            activity.onChangeObject();
                        }
                    }
                })
                .setNegativeButton(R.string.btn_cancel_title, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AddDictionaryObjectDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    public void setClazz(Class<T> clazz) {
        this.clazz = clazz;
    }
}
