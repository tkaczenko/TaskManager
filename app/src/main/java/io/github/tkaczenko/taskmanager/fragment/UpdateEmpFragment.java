package io.github.tkaczenko.taskmanager.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

import io.github.tkaczenko.taskmanager.R;
import io.github.tkaczenko.taskmanager.activity.TasksActivity;
import io.github.tkaczenko.taskmanager.database.model.employee.Employee;
import io.github.tkaczenko.taskmanager.database.model.dictionary.Department;
import io.github.tkaczenko.taskmanager.database.model.dictionary.Position;
import io.github.tkaczenko.taskmanager.database.model.dictionary.DictionaryDAOImp;
import io.github.tkaczenko.taskmanager.database.model.employee.EmployeeDAOImp;

/**
 * Created by tkaczenko on 17.11.16.
 */

public class UpdateEmpFragment extends Fragment implements View.OnClickListener {
    private EditText etID, etSurname, etMidName, etName, etPhone, etEmail;
    private Spinner sDepartment, sPosition;

    private Employee employee;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        employee = getArguments().getParcelable("employee");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_update_employee, container, false);
        setUpViews(v);
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnUpdate:
                EmployeeDAOImp employeeDAOImp = new EmployeeDAOImp(getActivity());
                String id = etID.getText().toString();
                if (id.isEmpty()) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Error")
                            .setMessage("Please, write id for employee")
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
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
                employee.getContact().setPhoneNum(etPhone.getText().toString());
                employee.getContact().setEmail(etEmail.getText().toString());
                Department department = (Department) sDepartment.getSelectedItem();
                employee.setDepartment(department);
                Position position = (Position) sPosition.getSelectedItem();
                employee.setPosition(position);
                long result = employeeDAOImp.update(employee);
                if (result > 0) {
                    TasksActivity activity = (TasksActivity) getActivity();
                    activity.onChangeObject();
                }
                break;
        }
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
        Button btnUpdate = (Button) v.findViewById(R.id.btnUpdate);

        if (btnUpdate != null) {
            btnUpdate.setOnClickListener(this);
        }

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

        if (employee != null) {
            etID.setText(String.valueOf(employee.getId()));
            etSurname.setText(employee.getLastName());
            etMidName.setText(employee.getMidName());
            etName.setText(employee.getFirstName());
            etPhone.setText(employee.getContact().getPhoneNum());
            etEmail.setText(employee.getContact().getEmail());
            int pos = departmentAdapter.getPosition(employee.getDepartment());
            sDepartment.setSelection(pos);
            pos = positionAdapter.getPosition(employee.getPosition());
            sPosition.setSelection(pos);
        }
    }
}
