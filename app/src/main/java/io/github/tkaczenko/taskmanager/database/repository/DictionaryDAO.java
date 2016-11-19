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

public class DictionaryDAO<T extends DictionaryObject> extends DAO<T> {
    private static final String WHERE_ID_EQUALS = DatabaseHelper.COLUMN_ID + " =?";

    private String tableName;
    private Class dictionaryObjectClass;

    public DictionaryDAO(Context context, Class<T> type) {
        super(context);
        this.dictionaryObjectClass = type;
        setTableName();
    }

    @Override
    public long save(T value, Integer... ids) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_ID, value.getId());
        values.put(DatabaseHelper.COLUMN_NAME, value.getName());

        return database.insert(tableName, null, values);
    }

    @Override
    public int update(T value) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, value.getName());
        return database.update(tableName, values,
                WHERE_ID_EQUALS, new String[]{String.valueOf(value.getId())});
    }

    @Override
    public long remove(T value) {
        return database.delete(tableName, WHERE_ID_EQUALS,
                new String[]{String.valueOf(value.getId())});
    }

    @Override
    public List<T> getAll() {
        List<T> objects = new ArrayList<>();
        Cursor cursor = database.query(tableName,
                new String[]{DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_NAME},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            T object = getNewInstance();
            if (object != null) {
                object.setId(cursor.getInt(0));
                object.setName(cursor.getString(1));
                objects.add(object);
            }
        }
        cursor.close();
        return objects;
    }

    private T getNewInstance() {
        try {
            return (T) dictionaryObjectClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void setTableName() {
        if (dictionaryObjectClass == Department.class) {
            tableName = DatabaseHelper.DEPARTMENT_TABLE;
        } else if (dictionaryObjectClass == Position.class) {
            tableName = DatabaseHelper.POSITION_TABLE;
        } else if (dictionaryObjectClass == TaskSource.class) {
            tableName = DatabaseHelper.TASK_SOURCE_TABLE;
        } else {
            tableName = DatabaseHelper.TASK_TYPE_TABLE;
        }
    }
}
