package io.github.tkaczenko.taskmanager.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

import io.github.tkaczenko.taskmanager.R;
import io.github.tkaczenko.taskmanager.activities.TasksActivity;
import io.github.tkaczenko.taskmanager.database.models.dictionary.Department;
import io.github.tkaczenko.taskmanager.database.models.dictionary.Position;
import io.github.tkaczenko.taskmanager.database.models.dictionary.common.DictionaryDAOImp;
import io.github.tkaczenko.taskmanager.database.models.employee.Employee;
import io.github.tkaczenko.taskmanager.database.models.employee.EmployeeDAOImp;

/**
 * Created by tkaczenko on 27.11.16.
 */

public class AddEmpDialog extends DialogFragment {
    private EditText etID, etSurname, etMidName, etName, etPhone, etEmail;
    private Spinner sDepartment, sPosition;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_add_employee, null);

        setUpViews(v);

        builder.setView(v)
                .setTitle(R.string.dialog_add_employe_title)
                .setPositiveButton(R.string.btn_add_title, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Employee employee = new Employee();
                        String id = etID.getText().toString();
                        if (id.isEmpty()) {
                            new AlertDialog.Builder(getActivity())
                                    .setTitle(R.string.dialog_error_title)
                                    .setMessage(R.string.dialog_error_mess)
                                    .setCancelable(false)
                                    .setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            etID.requestFocus();
                                        }
                                    }).show();
                            return;
                        }
                        employee.setId(Integer.parseInt(id));
                        employee.setLastName(etSurname.getText().toString());
                        employee.setMidName(etMidName.getText().toString());
                        employee.setFirstName(etName.getText().toString());
                        employee.setContact(new Employee.Contact(
                                employee.getId(), etPhone.getText().toString(),
                                etEmail.getText().toString()
                        ));
                        employee.setDepartment((Department) sDepartment.getSelectedItem());
                        employee.setPosition((Position) sPosition.getSelectedItem());

                        EmployeeDAOImp dao = new EmployeeDAOImp(getActivity());
                        long result = dao.save(employee);
                        if (result > 0) {
                            TasksActivity activity = (TasksActivity) getActivity();
                            activity.onChangeObject();
                        }
                    }
                })
                .setNegativeButton(R.string.btn_cancel_title, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AddEmpDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    private void setUpViews(View v) {
        etID = (EditText) v.findViewById(R.id.etID);
        etSurname = (EditText) v.findViewById(R.id.etSurname);
        etMidName = (EditText) v.findViewById(R.id.etMidName);
        etName = (EditText) v.findViewById(R.id.etName);
        etPhone = (EditText) v.findViewById(R.id.etPhone);
        etEmail = (EditText) v.findViewById(R.id.etEmail);
        sDepartment = (Spinner) v.findViewById(R.id.sDepartment);
        sPosition = (Spinner) v.findViewById(R.id.sPosition);

        DictionaryDAOImp<Department> departmentDAO = new DictionaryDAOImp<>(
                getActivity(), Department.class
        );
        List<Department> departments = departmentDAO.getAll();
        ArrayAdapter<Department> departmentAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, departments);
        sDepartment.setAdapter(departmentAdapter);

        DictionaryDAOImp<Position> positionDAO = new DictionaryDAOImp<>(
                getActivity(), Position.class
        );
        List<Position> positions = positionDAO.getAll();
        ArrayAdapter<Position> positionAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, positions);
        sPosition.setAdapter(positionAdapter);
    }
}