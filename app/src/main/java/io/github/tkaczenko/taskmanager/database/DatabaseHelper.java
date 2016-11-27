package io.github.tkaczenko.taskmanager.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by tkaczenko on 25.10.16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DB_LOCATION = "/data/data/io.github.tkaczenko.taskmanager/databases/";

    private static DatabaseHelper sInstance;

    public static synchronized DatabaseHelper getHelper(Context context) {
        if (sInstance == null)
            sInstance = new DatabaseHelper(context);
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private DatabaseHelper(Context context) {
        super(context, DatabaseContract.DATABASE_NAME, null, 1);
    }
}