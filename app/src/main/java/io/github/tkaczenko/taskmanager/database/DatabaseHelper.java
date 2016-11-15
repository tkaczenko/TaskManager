package io.github.tkaczenko.taskmanager.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import io.github.tkaczenko.taskmanager.database.model.dictionary.Department;
import io.github.tkaczenko.taskmanager.database.model.Employee;
import io.github.tkaczenko.taskmanager.database.model.dictionary.Position;
import io.github.tkaczenko.taskmanager.database.model.dictionary.TaskSource;
import io.github.tkaczenko.taskmanager.database.model.dictionary.TaskType;

/**
 * Created by tkaczenko on 25.10.16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "tasks.sqlite";
    public static final String DBLOCATION = "/data/data/io.github.tkaczenko.taskmanager/databases/";
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public DatabaseHelper(Context context) {
        super(context, DBNAME, null, 1);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void openDatabase() {
        String dbPath = mContext.getDatabasePath(DBNAME).getPath();
        if(mDatabase != null && mDatabase.isOpen()) {
            return;
        }
        mDatabase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public void closeDatabase() {
        if(mDatabase!=null) {
            mDatabase.close();
        }
    }

    public List<Position> getListPosition() {
        Position position = null;
        List<Position> positions = new ArrayList<>();
        openDatabase();
        Cursor cursor = mDatabase.query("positions", null, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            position = new Position(cursor.getInt(0), cursor.getString(1));
            positions.add(position);
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return positions;
    }

    public List<Department> getListDepartments() {
        Department department;
        List<Department> departments = new ArrayList<>();
        openDatabase();
        Cursor cursor = mDatabase.query("departments", null, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            department = new Department(cursor.getInt(0), cursor.getString(1));
            departments.add(department);
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return departments;
    }

    public List<TaskType> getListTaskTypes() {
        TaskType taskType;
        List<TaskType> taskTypes = new ArrayList<>();
        openDatabase();
        Cursor cursor = mDatabase.query("task_types", null, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            taskType = new TaskType(cursor.getInt(0), cursor.getString(1));
            taskTypes.add(taskType);
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return taskTypes;
    }

    public List<TaskSource> getListTaskSources() {
        TaskSource taskSource;
        List<TaskSource> taskSources = new ArrayList<>();
        openDatabase();
        Cursor cursor =  mDatabase.query("task_sources", null, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            taskSource = new TaskSource(cursor.getInt(0), cursor.getString(1));
            taskSources.add(taskSource);
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return taskSources;
    }

    public List<Employee> getListEmployees() {
        String table = "employees as Emp inner join departments as Dep on Emp.ID_DEPARTMENT = Dep.ID" +
                "inner join positions as Pos on Emp.ID_POSITION = Pos.ID";
        String columns[] = { "Emp.LAST_NAME as Surname", "Emp.FIRST_NAME as Name",
                "Dep.NAME as Department", "Pos.NAME as Position", "salary as Salary" };
        Cursor c = mDatabase.query(table, columns, null, null, null, null, null);
        c.close();
        closeDatabase();
        return null;
    }
}