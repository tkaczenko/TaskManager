package io.github.tkaczenko.taskmanager.database.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import io.github.tkaczenko.taskmanager.database.DatabaseHelper;
import io.github.tkaczenko.taskmanager.database.model.dictionary.Department;
import io.github.tkaczenko.taskmanager.database.model.dictionary.DictionaryObject;
import io.github.tkaczenko.taskmanager.database.model.dictionary.Position;
import io.github.tkaczenko.taskmanager.database.model.dictionary.TaskSource;

/**
 * Created by tkaczenko on 15.11.16.
 */

public class DictionaryDAO extends DAO<DictionaryObject> {
    private static final String WHERE_ID_EQUALS = DatabaseHelper.COLUMN_ID + " =?";

    private String tableName = null;
    private Class dictionaryObjectClass = null;

    public DictionaryDAO(Context context) {
        super(context);
    }

    @Override
    public long save(DictionaryObject value) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_ID, value.getId());
        values.put(DatabaseHelper.COLUMN_NAME, value.getName());

        tableName = checkTable(value);

        return database.insert(tableName, null, values);
    }

    @Override
    public int update(DictionaryObject value) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, value.getName());
        String tableName = checkTable(value);
        return database.update(tableName, values,
                WHERE_ID_EQUALS, new String[]{String.valueOf(value.getId())});
    }

    @Override
    public long remove(DictionaryObject value) {
        String tableName = checkTable(value);
        return database.delete(tableName, WHERE_ID_EQUALS,
                new String[]{String.valueOf(value.getId())});
    }

    @Override
    public List<DictionaryObject> getAll() {
        /*List<DictionaryObject> dictionaryObjects = new ArrayList<>();
        Cursor cursor = database.query(DatabaseHelper.DEPARTMENT_TABLE,
                new String[]{DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_NAME},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            Department department = new Department(cursor.getInt(0), cursor.getString(1));
            dictionaryObjects.add(department);
        }
        return departments;*/
        return null;
    }

    public List<DictionaryObject> getAll(Class dictionaryObjectClass) {
        List<DictionaryObject> dictionaryObjects = new ArrayList<>();
        String tableName;
        if (dictionaryObjectClass == Department.class) {
            tableName = DatabaseHelper.DEPARTMENT_TABLE;
        } else if (dictionaryObjectClass == Position.class) {
            tableName = DatabaseHelper.POSITION_TABLE;
        } else if (dictionaryObjectClass == TaskSource.class) {
            tableName = DatabaseHelper.TASK_SOURCE_TABLE;
        } else {
            tableName = DatabaseHelper.TASK_TYPE_TABLE;
        }
        Cursor cursor = database.query(tableName,
                new String[]{DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_NAME},
                null, null, null, null, null);
        //// FIXME: 16.11.16 Implement getAll for DictionaryDAO
        /*while (cursor.moveToNext()) {
            try {
                DictionaryObject object = dictionaryObjectClass.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            dictionaryObjects.add(department);
        }*/
        return null;
    }

    private String checkTable(DictionaryObject value) {
        String tableName;
        if (value instanceof Department) {
            tableName = DatabaseHelper.DEPARTMENT_TABLE;
        } else if (value instanceof Position) {
            tableName = DatabaseHelper.POSITION_TABLE;
        } else if (value instanceof TaskSource) {
            tableName = DatabaseHelper.TASK_SOURCE_TABLE;
        } else {
            tableName = DatabaseHelper.TASK_TYPE_TABLE;
        }
        return tableName;
    }
}
