package io.github.tkaczenko.taskmanager.database.models.dictionary.common;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import io.github.tkaczenko.taskmanager.database.DatabaseContract;
import io.github.tkaczenko.taskmanager.database.common.BaseDAOImp;
import io.github.tkaczenko.taskmanager.database.models.dictionary.Department;
import io.github.tkaczenko.taskmanager.database.models.dictionary.Position;
import io.github.tkaczenko.taskmanager.database.models.dictionary.TaskSource;

/**
 * Created by tkaczenko on 15.11.16.
 */

public class DictionaryDAOImp<E extends Dictionary> extends BaseDAOImp<E> implements DictionaryDAO<E> {
    private static final String WHERE_ID_EQUALS = DatabaseContract.Department.COLUMN_ID + " =?";

    private String tableName;
    private Class dictionaryObjectClass;

    public DictionaryDAOImp(Context context, Class<E> type) {
        super(context);
        this.dictionaryObjectClass = type;
        setTableName();
    }

    @Override
    public long save(E value) {
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.Department.COLUMN_NAME, value.getName());

        return database.insert(tableName, null, values);
    }

    @Override
    public int update(E value) {
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.Department.COLUMN_NAME, value.getName());
        return database.update(tableName, values,
                WHERE_ID_EQUALS, new String[]{String.valueOf(value.getId())});
    }

    @Override
    public long remove(E value) {
        return database.delete(tableName, WHERE_ID_EQUALS,
                new String[]{String.valueOf(value.getId())});
    }

    @Override
    public List<E> getAll() {
        List<E> objects = new ArrayList<>();
        Cursor cursor = database.query(tableName,
                new String[]{DatabaseContract.Department.COLUMN_ID,
                        DatabaseContract.Department.COLUMN_NAME},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            E object = getNewInstance();
            if (object != null) {
                object.setId(cursor.getInt(0));
                object.setName(cursor.getString(1));
                objects.add(object);
            }
        }
        cursor.close();
        return objects;
    }

    @SuppressWarnings("unchecked")
    private E getNewInstance() {
        try {
            return (E) dictionaryObjectClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void setTableName() {
        if (dictionaryObjectClass == Department.class) {
            tableName = DatabaseContract.Department.TABLE_NAME;
        } else if (dictionaryObjectClass == Position.class) {
            tableName = DatabaseContract.Position.TABLE_NAME;
        } else if (dictionaryObjectClass == TaskSource.class) {
            tableName = DatabaseContract.TaskSource.TABLE_NAME;
        } else {
            tableName = DatabaseContract.TaskType.TABLE_NAME;
        }
    }
}
