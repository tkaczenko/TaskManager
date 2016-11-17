package io.github.tkaczenko.taskmanager.fragment;

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
import io.github.tkaczenko.taskmanager.database.model.Employee;
import io.github.tkaczenko.taskmanager.database.model.dictionary.Department;
import io.github.tkaczenko.taskmanager.database.repository.DepartmentDAO;

/**
 * Created by tkaczenko on 17.11.16.
 */

public class UpdateEmpFragment extends Fragment {
    private EditText etID, etSurname, etMidName, etName, etPhone, etEmail;
    private Spinner sDepartment, sPosition;
    private Button btnUpdate;

    private Employee employee;

    private ArrayAdapter<Department> departmentAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_update_employee, container, false);

        employee = getArguments().getParcelable("employee");
        setUpViews(v);

        return v;
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
        btnUpdate = (Button) v.findViewById(R.id.btnUpdate);

        DepartmentDAO departmentDAO = new DepartmentDAO(getActivity());
        List<Department> departments = departmentDAO.getAll();
        departmentAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, departments);
        sDepartment.setAdapter(departmentAdapter);
        //// TODO: 17.11.16 Add PositionDAO

        if (employee != null) {
            etID.setText(String.valueOf(employee.getId()));
            etSurname.setText(employee.getLastName());
            etMidName.setText(employee.getMidName());
            etName.setText(employee.getFirstName());
            etPhone.setText(employee.getContact().getPhoneNum());
            etEmail.setText(employee.getContact().getEmail());
            int pos = departmentAdapter.getPosition(employee.getDepartment());
            sDepartment.setSelection(pos);
        }
    }
}
