package io.github.tkaczenko.taskmanager.dialogs;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.github.tkaczenko.taskmanager.R;
import io.github.tkaczenko.taskmanager.activities.TasksActivity;
import io.github.tkaczenko.taskmanager.database.models.dictionary.TaskSource;
import io.github.tkaczenko.taskmanager.database.models.dictionary.TaskType;
import io.github.tkaczenko.taskmanager.database.models.dictionary.common.DictionaryDAO;
import io.github.tkaczenko.taskmanager.database.models.dictionary.common.DictionaryDAOImp;
import io.github.tkaczenko.taskmanager.database.models.employee.Employee;
import io.github.tkaczenko.taskmanager.database.models.employee.EmployeeDAO;
import io.github.tkaczenko.taskmanager.database.models.employee.EmployeeDAOImp;
import io.github.tkaczenko.taskmanager.database.models.task.Task;
import io.github.tkaczenko.taskmanager.database.models.task.TaskDAO;
import io.github.tkaczenko.taskmanager.database.models.task.TaskDAOImp;
import io.github.tkaczenko.taskmanager.views.KeyPairBoolData;
import io.github.tkaczenko.taskmanager.views.MultiSelectSpinner;
import io.github.tkaczenko.taskmanager.views.SpinnerListener;

/**
 * Created by tkaczenko on 27.11.16.
 */

public class AddTaskDialog extends DialogFragment {
    private EditText etShortName, etDescription, etRejection, etSourceDoc, etSourceNum;
    private Button btnDateIssue, btnDatePlanned, btnDateExecution;
    private SwitchCompat switchCompleted, switchCanceled;
    private MultiSelectSpinner searchSpinner;
    private Spinner sSource, sType;

    private final DateFormat formatter = DateFormat.getDateInstance(DateFormat.SHORT);
    private List<Employee> mEmployees = new ArrayList<>();

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
                        Task task = new Task();
                        TaskDAO taskDAO = new TaskDAOImp(getActivity());
                        task.setShortName(etShortName.getText().toString());
                        task.setRejectionReason(etRejection.getText().toString());
                        task.setDescription(etDescription.getText().toString());
                        task.setSourceDoc(etSourceDoc.getText().toString());
                        task.setSourceNum(etSourceNum.getText().toString());
                        String date = btnDateIssue.getText().toString();
                        try {
                            task.setDateIssue((!date.isEmpty()) ? formatter.parse(date) : null);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        date = btnDatePlanned.getText().toString();
                        try {
                            task.setDatePlanned((!date.isEmpty()) ? formatter.parse(date) : null);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        date = btnDateExecution.getText().toString();
                        try {
                            task.setDateExecution((!date.isEmpty()) ? formatter.parse(date) : null);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        task.setTaskSource((TaskSource) sSource.getSelectedItem());
                        task.setTaskType((TaskType) sType.getSelectedItem());
                        task.setCompleted(switchCompleted.isChecked());
                        task.setCanceled(switchCanceled.isChecked());
                        task.setEmployees(mEmployees);

                        long result = taskDAO.save(task);
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

        switchCompleted = (SwitchCompat) v.findViewById(R.id.switchCompleted);
        switchCanceled = (SwitchCompat) v.findViewById(R.id.switchCanceled);

        DictionaryDAO<TaskSource> taskSourceDAO = new DictionaryDAOImp<>(
                getActivity(), TaskSource.class
        );
        List<TaskSource> sources = taskSourceDAO.getAll();
        ArrayAdapter<TaskSource> sourceAdapter = new ArrayAdapter<>(
                getActivity(), android.R.layout.simple_list_item_1, sources
        );
        sSource.setAdapter(sourceAdapter);

        DictionaryDAO<TaskType> taskTypeDAO = new DictionaryDAOImp<>(
                getActivity(), TaskType.class
        );
        List<TaskType> types = taskTypeDAO.getAll();
        ArrayAdapter<TaskType> typeAdapter = new ArrayAdapter<>(
                getActivity(), android.R.layout.simple_list_item_1, types
        );
        sType.setAdapter(typeAdapter);

        searchSpinner = (MultiSelectSpinner) v.findViewById(R.id.searchMultiSpinner);
        EmployeeDAO employeeDAO = new EmployeeDAOImp(getActivity());
        List<Employee> employees = employeeDAO.getAll();
        List<KeyPairBoolData> listArray = new ArrayList<>();
        for (int i = 0; i < employees.size(); i++) {
            KeyPairBoolData h = new KeyPairBoolData();
            h.setId(i + 1);
            h.setName(employees.get(i).getLastName() + employees.get(i).getFirstName());
            h.setObject(employees.get(i));
            h.setSelected(false);
            listArray.add(h);
        }
        searchSpinner.setLimit(20, new MultiSelectSpinner.LimitExceedListener() {
            @Override
            public void onLimitListener(KeyPairBoolData data) {
                Toast.makeText(getActivity(), R.string.limit_mess, Toast.LENGTH_SHORT)
                        .show();
            }
        });

        searchSpinner.setItems(listArray, -1, new SpinnerListener() {
            @Override
            public void onItemsSelected(List<KeyPairBoolData> items) {
                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).isSelected()) {
                        Employee employee = (Employee) items.get(i).getObject();
                        if (!mEmployees.contains(employee)) {
                            mEmployees.add(employee);
                        }
                    } else {
                        Employee employee = (Employee) items.get(i).getObject();
                        mEmployees.remove(employee);
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
