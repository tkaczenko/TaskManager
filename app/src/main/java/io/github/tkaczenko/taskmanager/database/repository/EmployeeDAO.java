package io.github.tkaczenko.taskmanager.database.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import io.github.tkaczenko.taskmanager.database.DatabaseContract;
import io.github.tkaczenko.taskmanager.database.model.Employee;
import io.github.tkaczenko.taskmanager.database.model.dictionary.Department;
import io.github.tkaczenko.taskmanager.database.model.dictionary.Position;

/**
 * Created by tkaczenko on 15.11.16.
 */

public class EmployeeDAO extends DAO<Employee> {
    static final String EMP_ID_WITH_PREFIX = "emp.ID";
    static final String DEP_ID_WITH_PREFIX = "dep.ID";
    static final String DEP_NAME_WITH_PREFIX = "dep.NAME";
    static final String POS_ID_WITH_PREFIX = "pos.ID";
    static final String POS_NAME_WITH_PREFIX = "pos.NAME";
    static final String EMP_LAST_NAME_WITH_PREFIX = "emp.LAST_NAME";
    static final String EMP_MID_NAME_WITH_PREFIX = "emp.MID_NAME";
    static final String EMP_FIRST_NAME_WITH_PREFIX = "emp.FIRST_NAME";
    static final String EMP_PHONE_NUM_WITH_PREFIX = "con.PHONE_NUM";
    static final String EMP_EMAIL_WITH_PREFIX = "con.EMAIL";

    private static final String WHERE_ID_EQUALS = DatabaseContract.Employee.COLUMN_ID + " =?";

    public EmployeeDAO(Context mContext) {
        super(mContext);
    }

    @Override
    public long save(Employee value, Integer... ids) {
        ContentValues values = new ContentValues();

        values.put(DatabaseContract.Employee.COLUMN_ID, value.getId());
        values.put(DatabaseContract.Employee.COLUMN_ID_DEPARTMENT, value.getDepartment().getId());
        values.put(DatabaseContract.Employee.COLUM_ID_POSITION, value.getPosition().getId());
        values.put(DatabaseContract.Employee.COLUMN_LAST_NAME, value.getLastName());
        values.put(DatabaseContract.Employee.COLUMN_MID_NAME, value.getMidName());
        values.put(DatabaseContract.Employee.COLUMN_FIRST_NAME, value.getFirstName());

        return database.insert(DatabaseContract.Employee.TABLE_EMPLOYEE, null, values);
    }

    @Override
    public int update(Employee value) {
        ContentValues values = new ContentValues();

        values.put(DatabaseContract.Employee.COLUMN_ID, value.getId());
        values.put(DatabaseContract.Employee.COLUMN_ID_DEPARTMENT, value.getDepartment().getId());
        values.put(DatabaseContract.Employee.COLUM_ID_POSITION, value.getPosition().getId());
        values.put(DatabaseContract.Employee.COLUMN_LAST_NAME, value.getLastName());
        values.put(DatabaseContract.Employee.COLUMN_MID_NAME, value.getMidName());
        values.put(DatabaseContract.Employee.COLUMN_FIRST_NAME, value.getFirstName());

        return database.update(
                DatabaseContract.Employee.TABLE_EMPLOYEE, values,
                WHERE_ID_EQUALS, new String[]{String.valueOf(value.getId())}
        );
    }

    @Override
    public long remove(Employee value) {
        return database.delete(
                DatabaseContract.Employee.TABLE_EMPLOYEE, WHERE_ID_EQUALS,
                new String[]{String.valueOf(value.getId())}
        );
    }

    @Override
    public List<Employee> getAll() {
        List<Employee> employees = new ArrayList<>();

        String query = "SELECT " + EMP_ID_WITH_PREFIX + "," +
                DEP_ID_WITH_PREFIX + "," + DEP_NAME_WITH_PREFIX + "," +
                POS_ID_WITH_PREFIX + "," + POS_NAME_WITH_PREFIX + "," +
                EMP_LAST_NAME_WITH_PREFIX + "," + EMP_MID_NAME_WITH_PREFIX + "," +
                EMP_FIRST_NAME_WITH_PREFIX + "," + EMP_PHONE_NUM_WITH_PREFIX + "," +
                EMP_EMAIL_WITH_PREFIX + " FROM " + DatabaseContract.Employee.TABLE_EMPLOYEE +
                " emp" +
                " LEFT OUTER JOIN " + DatabaseContract.Department.TABLE_DEPARTMENT + " dep" +
                " ON " + "emp.ID_DEPARTMENT = " + DEP_ID_WITH_PREFIX +
                " LEFT OUTER JOIN " + DatabaseContract.Position.TABLE_POSITION + " pos" +
                " ON " + "emp.ID_POSITION = " + POS_ID_WITH_PREFIX +
                " LEFT OUTER JOIN " + DatabaseContract.Contact.TABLE_CONTACT + " con" +
                " ON " + "emp.ID = con.ID";

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
            Employee.Contact contact = new Employee.Contact();
            contact.setId(employee.getId());
            contact.setPhoneNum(cursor.getString(8));
            contact.setEmail(cursor.getString(9));

            employee.setDepartment(department);
            employee.setPosition(position);
            employee.setContact(contact);

            employees.add(employee);
        }
        cursor.close();
        return employees;
    }
}
