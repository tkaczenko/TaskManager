package io.github.tkaczenko.taskmanager.dialogs;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.github.tkaczenko.taskmanager.R;
import io.github.tkaczenko.taskmanager.activities.TasksActivity;
import io.github.tkaczenko.taskmanager.database.models.dictionary.TaskSource;
import io.github.tkaczenko.taskmanager.database.models.dictionary.TaskType;
import io.github.tkaczenko.taskmanager.database.models.dictionary.common.DictionaryDAOImp;

/**
 * Created by tkaczenko on 27.11.16.
 */

public class AddTaskDialog extends DialogFragment {
    private EditText etShortName, etDescription, etRejection, etSourceDoc, etSourceNum;
    private Button btnDateIssue, btnDatePlanned, btnDateExecution;
    private Spinner sSource, sType;

    private final DateFormat formatter = DateFormat.getDateInstance(DateFormat.SHORT);

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_add_task, null);

        setUpViews(v);

        builder.setView(v)
                .setTitle(R.string.dialog_add_task_title)
                .setPositiveButton(R.string.btn_add_title, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //// TODO: 27.11.16 Implement
                        long result = 0;
                        if (result > 0) {
                            TasksActivity activity = (TasksActivity) getActivity();
                            activity.onChangeObject();
                        }
                    }
                })
                .setNegativeButton(R.string.btn_cancel_title, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AddTaskDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    private void setUpViews(View v) {
        etShortName = (EditText) v.findViewById(R.id.etShortName);
        etDescription = (EditText) v.findViewById(R.id.etDescription);
        etRejection = (EditText) v.findViewById(R.id.etRejection);
        etSourceDoc = (EditText) v.findViewById(R.id.etSourceDoc);
        etSourceNum = (EditText) v.findViewById(R.id.etSourceNum);

        btnDateIssue = (Button) v.findViewById(R.id.btnDateIssue);
        btnDatePlanned = (Button) v.findViewById(R.id.btnDatePlanned);
        btnDateExecution = (Button) v.findViewById(R.id.btnDateExecution);
        btnDateIssue.setOnClickListener(mBtnPickDateListener);
        btnDatePlanned.setOnClickListener(mBtnPickDateListener);
        btnDateExecution.setOnClickListener(mBtnPickDateListener);

        sSource = (Spinner) v.findViewById(R.id.sSource);
        sType = (Spinner) v.findViewById(R.id.sType);

        DictionaryDAOImp<TaskSource> sourceDictionaryDAOImp = new DictionaryDAOImp<>(
                getActivity(), TaskSource.class
        );
        List<TaskSource> sources = sourceDictionaryDAOImp.getAll();
        ArrayAdapter<TaskSource> sourceAdapter = new ArrayAdapter<>(
                getActivity(), android.R.layout.simple_list_item_1, sources
        );
        sSource.setAdapter(sourceAdapter);

        DictionaryDAOImp<TaskType> typeDictionaryDAOImp = new DictionaryDAOImp<>(
                getActivity(), TaskType.class
        );
        List<TaskType> types = typeDictionaryDAOImp.getAll();
        ArrayAdapter<TaskType> typeAdapter = new ArrayAdapter<>(
                getActivity(), android.R.layout.simple_list_item_1, types
        );
        sType.setAdapter(typeAdapter);
    }

    private View.OnClickListener mBtnPickDateListener = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    TextView textView = (TextView) view;
                    textView.setText(formatter.format(calendar.getTime()));
                }
            };

            int year, month, day;
            Calendar calendar = Calendar.getInstance();
            try {
                Date date = formatter.parse(((TextView) view).getText().toString());
                calendar.setTime(date);
            } catch (ParseException | NullPointerException e) {
                e.printStackTrace();
            }

            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog pickerDialog = new DatePickerDialog(
                    getActivity(), listener, year, month, day
            );

            pickerDialog.show();
        }
    };
}
