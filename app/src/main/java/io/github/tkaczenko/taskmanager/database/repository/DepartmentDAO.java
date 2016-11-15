package io.github.tkaczenko.taskmanager.database.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import io.github.tkaczenko.taskmanager.database.DatabaseHelper;
import io.github.tkaczenko.taskmanager.database.model.dictionary.Department;

/**
 * Created by tkaczenko on 15.11.16.
 */

public class DepartmentDAO extends DAO<Department> {
    private static final String WHERE_ID_EQUALS = DatabaseHelper.COLUMN_ID + " =?";

    public DepartmentDAO(Context mContext) {
        super(mContext);
    }

    @Override
    public long save(Department department) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_ID, department.getId());
        values.put(DatabaseHelper.COLUMN_NAME, department.getName());

        return database.insert(DatabaseHelper.DEPARTMENT_TABLE, null, values);
    }

    @Override
    public int update(Department department) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, department.getName());
        return database.update(DatabaseHelper.DEPARTMENT_TABLE, values,
                WHERE_ID_EQUALS, new String[]{String.valueOf(department.getId())});
    }

    @Override
    public long remove(Department department) {
        return database.delete(DatabaseHelper.DEPARTMENT_TABLE, WHERE_ID_EQUALS,
                new String[]{String.valueOf(department.getId())});
    }

    @Override
    public List<Department> getAll() {
        List<Department> departments = new ArrayList<>();
        Cursor cursor = database.query(DatabaseHelper.DEPARTMENT_TABLE,
                new String[]{DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_NAME},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            Department department = new Department(cursor.getInt(0), cursor.getString(1));
            departments.add(department);
        }
        return departments;
    }
}
