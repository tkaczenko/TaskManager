package io.github.tkaczenko.taskmanager.database.common;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import io.github.tkaczenko.taskmanager.database.DatabaseHelper;

/**
 * Created by tkaczenko on 15.11.16.
 */

public abstract class BaseDAOImp<E> implements DAO<E> {
    protected SQLiteDatabase database;
    private DatabaseHelper databaseHelper;
    private Context mContext;

    public BaseDAOImp(Context mContext) {
        this.mContext = mContext;
        databaseHelper = DatabaseHelper.getHelper(mContext);
        open();
    }

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
