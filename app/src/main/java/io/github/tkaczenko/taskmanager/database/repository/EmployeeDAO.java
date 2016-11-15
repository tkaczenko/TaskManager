package io.github.tkaczenko.taskmanager.database.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import io.github.tkaczenko.taskmanager.database.DatabaseHelper;
import io.github.tkaczenko.taskmanager.database.model.Employee;
import io.github.tkaczenko.taskmanager.database.model.dictionary.Department;
import io.github.tkaczenko.taskmanager.database.model.dictionary.Position;

/**
 * Created by tkaczenko on 15.11.16.
 */

public class EmployeeDAO extends DAO<Employee> {
    public static final String EMP_ID_WITH_PREFIX = "emp.ID";
    public static final String DEP_ID_WITH_PREFIX = "dep.ID";
    public static final String DEP_NAME_WITH_PREFIX = "dep.NAME";
    public static final String POS_ID_WITH_PREFIX = "pos.ID";
    public static final String POS_NAME_WITH_PREFIX = "pos.NAME";
    public static final String EMP_LAST_NAME_WITH_PREFIX = "emp.LAST_NAME";
    public static final String EMP_MID_NAME_WITH_PREFIX = "emp.MID_NAME";
    public static final String EMP_FIRST_NAME_WITH_PREFIX = "emp.FIRST_NAME";

    public EmployeeDAO(Context mContext) {
        super(mContext);
    }

    @Override
    public long save(Employee employee) {
        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.COLUMN_ID, employee.getId());
        values.put(DatabaseHelper.COLUMN_IDDEPARTMENT, employee.getDepartment().getId());
        values.put(DatabaseHelper.COLUM_IDPOSITION, employee.getPosition().getId());
        values.put(DatabaseHelper.COLUMN_LAST_NAME, employee.getLastName());
        values.put(DatabaseHelper.COLUMN_MID_NAME, employee.getMidName());
        values.put(DatabaseHelper.COLUMN_FIRST_NAME, employee.getFirstName());

        return database.insert(DatabaseHelper.EMPLOYEE_TABLE, null, values);
    }

    @Override
    public int update(Employee value) {
        //// TODO: 15.11.16 Implement
        return 0;
    }

    @Override
    public long remove(Employee value) {
        //// TODO: 15.11.16 Implement
        return 0;
    }

    @Override
    public List<Employee> getAll() {
        List<Employee> employees = new ArrayList<>();
        String query = "SELECT " + EMP_ID_WITH_PREFIX + "," +
                DEP_ID_WITH_PREFIX + "," + DEP_NAME_WITH_PREFIX + "," +
                POS_ID_WITH_PREFIX + "," + POS_NAME_WITH_PREFIX + "," +
                EMP_LAST_NAME_WITH_PREFIX + "," + EMP_MID_NAME_WITH_PREFIX + "," +
                EMP_FIRST_NAME_WITH_PREFIX + "FROM " + DatabaseHelper.EMPLOYEE_TABLE + " emp," +
                DatabaseHelper.DEPARTMENT_TABLE + " dep," + DatabaseHelper.POSITION_TABLE + " pos," +
                "WHERE " + "emp.ID_DEPARTMENT = " + DEP_ID_WITH_PREFIX + "," +
                "emp.ID_POSITION = " + POS_ID_WITH_PREFIX;

        Cursor cursor = database.rawQuery(query, null);
        while (cursor.moveToNext()) {
            Employee employee = new Employee();

            employee.setId(cursor.getInt(0));
            Department department = new Department();
            department.setId(cursor.getInt(1));
            department.setName(cursor.getString(2));
            Position position = new Position();
            position.setId(cursor.getInt(3));
            position.setName(cursor.getString(4));
            employee.setLastName(cursor.getString(5));
            employee.setMidName(cursor.getString(6));
            employee.setFirstName(cursor.getString(7));

            employee.setDepartment(department);
            employee.setPosition(position);

            employees.add(employee);
        }
        return employees;
    }
}
