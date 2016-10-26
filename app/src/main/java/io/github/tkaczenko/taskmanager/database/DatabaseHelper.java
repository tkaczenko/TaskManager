package io.github.tkaczenko.taskmanager.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import io.github.tkaczenko.taskmanager.models.Department;
import io.github.tkaczenko.taskmanager.models.Position;
import io.github.tkaczenko.taskmanager.models.Product;
import io.github.tkaczenko.taskmanager.models.TaskSource;
import io.github.tkaczenko.taskmanager.models.TaskType;

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

    public List<Product> getListProduct() {
        Product product = null;
        List<Product> productList = new ArrayList<>();
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM product", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            product = new Product(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getString(3));
            productList.add(product);
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return productList;
    }

    public List<Position> getListPosition() {
        Position position = null;
        List<Position> positions = new ArrayList<>();
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM positions", null);
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
        Department department = null;
        List<Department> departments = new ArrayList<>();
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM departments", null);
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
        TaskType taskType = null;
        List<TaskType> taskTypes = new ArrayList<>();
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM task_types", null);
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
        TaskSource taskSource = null;
        List<TaskSource> taskSources = new ArrayList<>();
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM task_sources", null);
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
}