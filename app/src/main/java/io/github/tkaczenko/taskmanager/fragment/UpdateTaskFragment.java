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
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.github.tkaczenko.taskmanager.R;
import io.github.tkaczenko.taskmanager.database.model.Employee;
import io.github.tkaczenko.taskmanager.database.model.Task;
import io.github.tkaczenko.taskmanager.database.model.dictionary.TaskSource;
import io.github.tkaczenko.taskmanager.database.model.dictionary.TaskType;
import io.github.tkaczenko.taskmanager.database.repository.DictionaryDAO;
import io.github.tkaczenko.taskmanager.database.repository.EmployeeDAO;
import io.github.tkaczenko.taskmanager.view.KeyPairBoolData;
import io.github.tkaczenko.taskmanager.view.MultiSelectSpinner;
import io.github.tkaczenko.taskmanager.view.SpinnerListener;

/**
 * Created by tkaczenko on 27.11.16.
 */

public class UpdateTaskFragment extends Fragment implements View.OnClickListener {
    private EditText etShortName, etDescription, etRejection, etSourceDoc, etSourceNum;
    private Button btnDateIssue, btnDatePlanned, btnDateExecution;
    private Spinner sSource, sType;

    private final DateFormat formatter = DateFormat.getDateInstance(DateFormat.SHORT);

    private Task task;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        task = getArguments().getParcelable("task");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
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

        btnDateIssue = (Button) v.findViewById(R.id.btnDateIssue);
        btnDatePlanned = (Button) v.findViewById(R.id.btnDatePlanned);
        btnDateExecution = (Button) v.findViewById(R.id.btnDateExecution);
        btnDateIssue.setOnClickListener(mBtnPickDateListener);
        btnDatePlanned.setOnClickListener(mBtnPickDateListener);
        btnDateExecution.setOnClickListener(mBtnPickDateListener);

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

        MultiSelectSpinner searchSpinner = (MultiSelectSpinner) v.findViewById(R.id.searchMultiSpinner);
        EmployeeDAO employeeDAO = new EmployeeDAO(getActivity());
        List<Employee> employees = employeeDAO.getAll();
        final List<KeyPairBoolData> listArray = new ArrayList<>();
        for (int i = 0; i < employees.size(); i++) {
            KeyPairBoolData h = new KeyPairBoolData();
            h.setId(i + 1);
            h.setName(employees.get(i).getLastName() + employees.get(i).getFirstName());
            h.setObject(employees.get(i));
            h.setSelected(false);
            listArray.add(h);
        }

        if (task != null) {
            etShortName.setText(task.getShortName());
            etDescription.setText(task.getDescription());
            etRejection.setText(task.getRejectionReason());
            etSourceDoc.setText(task.getSourceDoc());
            etSourceNum.setText(task.getSourceNum());
            Date date = task.getDateIssue();
            String formated = (date != null) ? formatter.format(date) : null;
            btnDateIssue.setText((formated != null) ? formated : "not");
            date = task.getDatePlanned();
            formated = (date != null) ? formatter.format(date) : null;
            btnDatePlanned.setText((formated != null) ? formated : "not");
            date = task.getDateExecution();
            formated = (date != null) ? formatter.format(date) : null;
            btnDateExecution.setText((formated != null) ? formated : "not");
            int pos = sourceAdapter.getPosition(task.getTaskSource());
            sSource.setSelection(pos);
            pos = typeAdapter.getPosition(task.getTaskType());
            sType.setSelection(pos);
            List<Employee> employeeSet = task.getEmployees();
            for (KeyPairBoolData keyPairBoolData : listArray) {
                if (employeeSet.contains(keyPairBoolData.getObject())) {
                    keyPairBoolData.setSelected(true);
                }
            }
        }

        searchSpinner.setLimit(20, new MultiSelectSpinner.LimitExceedListener() {
            @Override
            public void onLimitListener(KeyPairBoolData data) {
                Toast.makeText(getActivity(), "LIMIT", Toast.LENGTH_SHORT)
                        .show();
            }
        });

        searchSpinner.setItems(listArray, -1, new SpinnerListener() {
            @Override
            public void onItemsSelected(List<KeyPairBoolData> items) {
                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).isSelected()) {
                        task.getEmployees().add((Employee) items.get(i).getObject());
                    }
                }
            }
        });

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

                    Button button = (Button) view;
                    button.setText(formatter.format(calendar.getTime()));
                }
            };

            int year, month, day;
            Calendar calendar = Calendar.getInstance();
            try {
                Date date = formatter.parse(((Button) view).getText().toString());
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
