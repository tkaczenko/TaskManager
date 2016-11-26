package io.github.tkaczenko.taskmanager.database.repository;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import io.github.tkaczenko.taskmanager.database.DatabaseHelper;

/**
 * Created by tkaczenko on 15.11.16.
 */

abstract class DAO<V> {
    protected SQLiteDatabase database;
    private DatabaseHelper databaseHelper;
    private Context mContext;

    DAO(Context mContext) {
        this.mContext = mContext;
        databaseHelper = DatabaseHelper.getHelper(mContext);
        open();
    }

    public abstract long save(V value, Integer... ids);

    public abstract int update(V value);

    public abstract long remove(V value);

    public abstract List<V> getAll();

    public void open() throws SQLException {
        if (databaseHelper == null) {
            databaseHelper = DatabaseHelper.getHelper(mContext);
        }
        database = databaseHelper.getWritableDatabase();
    }

    public Context getContext() {
        return mContext;
    }
}
