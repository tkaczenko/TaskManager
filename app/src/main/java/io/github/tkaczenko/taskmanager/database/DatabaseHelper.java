package io.github.tkaczenko.taskmanager.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by tkaczenko on 25.10.16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "tasks.sqlite";
    public static final String DBLOCATION = "/data/data/io.github.tkaczenko.taskmanager/databases/";

    public static final String EMPLOYEE_TABLE = "employees";
    public static final String DEPARTMENT_TABLE = "departments";
    public static final String POSITION_TABLE = "positions";
    public static final String TASK_SOURCE_TABLE = "task_sources";
    public static final String TASK_TYPE_TABLE = "task_types";
    public static final String CONTACTS_TABLE = "contacts";

    public static final String TASK_TABLE = "tasks";
    public static final String TABLE_TASK_EMP = "task_employee";

    public static final String COLUMN_IDSOURCE = "ID_SOURCE";
    public static final String COLUMN_IDTYPE = "ID_TYPE";
    public static final String COLUMN_SHORT_NAME = "SHORT_NAME";
    public static final String COLUMN_DESCRIPTION = "DESCRIPTION";
    public static final String COLUMN_DATE_ISSUE = "DATE_ISSUE";
    public static final String COLUMN_DATE_PLANNED = "DATE_PLANNED";
    public static final String COLUMN_DATE_EXECUTION = "DATE_EXECUTION";
    public static final String COLUMN_REJUCTION_REASON = "REJUCTION_RESON";
    public static final String COLUMN_COMPLETED = "COMPLETED";
    public static final String COLUMN_CANCELED = "CANCELED";
    public static final String COLUMN_SOURCE_DOC = "SOURCE_DOC";
    public static final String COLUMN_SOURCE_NUM = "SOURCE_NUM";

    public static final String COLUMN_IDDEPARTMENT = "ID_DEPARTMENT";
    public static final String COLUM_IDPOSITION = "ID_POSITION";
    public static final String COLUMN_LAST_NAME = "LAST_NAME";
    public static final String COLUMN_MID_NAME = "MID_NAME";
    public static final String COLUMN_FIRST_NAME = "FIRST_NAME";

    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_NAME = "NAME";

    private Context mContext;
    private SQLiteDatabase mDatabase;

    private static DatabaseHelper instance;

    public DatabaseHelper(Context context) {
        super(context, DBNAME, null, 1);
        this.mContext = context;
    }

    public static synchronized DatabaseHelper getHelper(Context context) {
        if (instance == null)
            instance = new DatabaseHelper(context);
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void openDatabase() {
        String dbPath = mContext.getDatabasePath(DBNAME).getPath();
        if (mDatabase != null && mDatabase.isOpen()) {
            return;
        }
        mDatabase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public void closeDatabase() {
        if (mDatabase != null) {
            mDatabase.close();
        }
    }

}