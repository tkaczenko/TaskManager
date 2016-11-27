package io.github.tkaczenko.taskmanager.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.github.tkaczenko.taskmanager.R;
import io.github.tkaczenko.taskmanager.database.model.Task;
import io.github.tkaczenko.taskmanager.database.model.dictionary.TaskSource;
import io.github.tkaczenko.taskmanager.database.model.dictionary.TaskType;
import io.github.tkaczenko.taskmanager.database.repository.DictionaryDAO;

/**
 * Created by tkaczenko on 27.11.16.
 */

public class UpdateTaskFragment extends Fragment implements View.OnClickListener {
    private EditText etShortName, etDescription, etRejection, etSourceDoc, etSourceNum;
    private TextView tvDateIssue, tvDatePlanned, tvDateExecution;
    private Spinner sSource, sType;

    private final SimpleDateFormat formatter = new SimpleDateFormat(
            "yyyy-MM-dd", Locale.ENGLISH);

    private Task task;

    public interface OnTaskChangedListener {
        void onChangeTask();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        task = getArguments().getParcelable("task");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_update_task, container, false);

        setUpViews(v);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnUpdate:
                break;
            case R.id.tvDateIssue:
                break;
            case R.id.tvDatePlanned:
                break;
            case R.id.tvDateExecution:
                break;
        }
    }

    private void setUpViews(View v) {
        etShortName = (EditText) v.findViewById(R.id.etShortName);
        etDescription = (EditText) v.findViewById(R.id.etDescription);
        etRejection = (EditText) v.findViewById(R.id.etRejection);
        etSourceDoc = (EditText) v.findViewById(R.id.etSourceDoc);
        etSourceNum = (EditText) v.findViewById(R.id.etSourceNum);

        tvDateIssue = (TextView) v.findViewById(R.id.tvDateIssue);
        tvDatePlanned = (TextView) v.findViewById(R.id.tvDatePlanned);
        tvDateExecution = (TextView) v.findViewById(R.id.tvDateExecution);
        tvDateIssue.setOnClickListener(mBtnPickDateListener);
        tvDatePlanned.setOnClickListener(mBtnPickDateListener);
        tvDateExecution.setOnClickListener(mBtnPickDateListener);

        sSource = (Spinner) v.findViewById(R.id.sSource);
        sType = (Spinner) v.findViewById(R.id.sType);

        Button btnUpdate = (Button) v.findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(this);

        DictionaryDAO<TaskSource> sourceDictionaryDAO = new DictionaryDAO<>(
                getActivity(), TaskSource.class
        );
        List<TaskSource> sources = sourceDictionaryDAO.getAll();
        ArrayAdapter<TaskSource> sourceAdapter = new ArrayAdapter<>(
                getActivity(), android.R.layout.simple_list_item_1, sources
        );
        sSource.setAdapter(sourceAdapter);

        DictionaryDAO<TaskType> typeDictionaryDAO = new DictionaryDAO<>(
                getActivity(), TaskType.class
        );
        List<TaskType> types = typeDictionaryDAO.getAll();
        ArrayAdapter<TaskType> typeAdapter = new ArrayAdapter<>(
                getActivity(), android.R.layout.simple_list_item_1, types
        );
        sType.setAdapter(typeAdapter);

        if (task != null) {
            etShortName.setText(task.getShortName());
            etDescription.setText(task.getDescription());
            etRejection.setText(task.getRejectionReason());
            etSourceDoc.setText(task.getSourceDoc());
            etSourceNum.setText(task.getSourceNum());
            Date date = task.getDateIssue();
            String formated = (date != null) ? formatter.format(date) : null;
            tvDateIssue.setText((formated != null) ? formated : "not");
            date = task.getDatePlanned();
            formated = (date != null) ? formatter.format(date) : null;
            tvDatePlanned.setText((formated != null) ? formated : "not");
            date = task.getDateExecution();
            formated = (date != null) ? formatter.format(date) : null;
            tvDateExecution.setText((formated != null) ? formated : "not");
            int pos = sourceAdapter.getPosition(task.getTaskSource());
            sSource.setSelection(pos);
            pos = typeAdapter.getPosition(task.getTaskType());
            sType.setSelection(pos);
        }

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
            try {
                Date date = formatter.parse(((TextView) view).getText().toString());
                year = date.getYear();
                month = date.getMonth();
                day = date.getDay();
            } catch (ParseException | NullPointerException e) {
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
            }

            DatePickerDialog pickerDialog = new DatePickerDialog(
                    getActivity(), listener, year, month, day
            );

            pickerDialog.show();
        }
    };
}
