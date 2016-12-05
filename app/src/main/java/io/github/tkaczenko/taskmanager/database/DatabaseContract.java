package io.github.tkaczenko.taskmanager.database;

import android.provider.BaseColumns;

import java.text.SimpleDateFormat;

/**
 * Created by tkaczenko on 19.11.16.
 */

public class DatabaseContract {
    public static final String DATABASE_NAME = "tasks.sqlite";

    private DatabaseContract() {
    }

    public static abstract class Tasks implements BaseColumns {
        public static final String TABLE_NAME = "tasks";
        public static final String COLUMN_ID = "ID";
        public static final String COLUMN_ID_SOURCE = "ID_SOURCE";
        public static final String COLUMN_ID_TYPE = "ID_TYPE";
        public static final String COLUMN_SHORT_NAME = "SHORT_NAME";
        public static final String COLUMN_DESCRIPTION = "DESCRIPTION";
        public static final String COLUMN_DATE_ISSUE = "DATE_ISSUE";
        public static final String COLUMN_DATE_PLANNED = "DATE_PLANNED";
        public static final String COLUMN_DATE_EXECUTION = "DATE_EXECUTION";
        public static final String COLUMN_REJECTION_REASON = "REJECTION_REASON";
        public static final String COLUMN_COMPLETED = "COMPLETED";
        public static final String COLUMN_CANCELED = "CANCELED";
        public static final String COLUMN_SOURCE_DOC = "SOURCE_DOC";
        public static final String COLUMN_SOURCE_NUM = "SOURCE_NUM";

        public static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        public static final String CREATE_TABLE = "CREATE TABLE tasks (\n" +
                "    ID               INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                "                             NOT NULL,\n" +
                "    ID_SOURCE        INTEGER CONSTRAINT taskSourceIDFK REFERENCES task_sources (ID),\n" +
                "    ID_TYPE          INTEGER REFERENCES task_types (ID),\n" +
                "    SHORT_NAME       TEXT    UNIQUE\n" +
                "                             NOT NULL\n" +
                "                             DEFAULT defaulttask,\n" +
                "    DESCRIPTION      TEXT,\n" +
                "    DATE_ISSUE       STRING  NOT NULL,\n" +
                "    DATE_PLANNED     TEXT    NOT NULL,\n" +
                "    DATE_EXECUTION   TEXT,\n" +
                "    REJECTION_REASON TEXT,\n" +
                "    COMPLETED        BOOLEAN,\n" +
                "    CANCELED         BOOLEAN,\n" +
                "    SOURCE_DOC       TEXT,\n" +
                "    SOURCE_NUM       INTEGER\n" +
                ")";
    }

    public static abstract class TaskEmployee implements BaseColumns {
        public static final String TABLE_NAME = "task_employee";
        public static final String COLUMN_TASK = "ID_TASK";
        public static final String COLUMN_EMPLOYEE = "ID_EMPLOYEE";

        public static final String CREATE_TABLE = "CREATE TABLE task_employee (\n" +
                "    ID_TASK     INTEGER REFERENCES tasks (ID) \n" +
                "                        NOT NULL,\n" +
                "    ID_EMPLOYEE INTEGER NOT NULL\n" +
                "                        REFERENCES employees (ID),\n" +
                "    PRIMARY KEY (\n" +
                "        ID_TASK,\n" +
                "        ID_EMPLOYEE\n" +
                "    )\n" +
                ")";
    }

    public static abstract class Employees implements BaseColumns {
        public static final String TABLE_NAME = "employees";
        public static final String COLUMN_ID = "ID";
        public static final String COLUMN_ID_DEPARTMENT = "ID_DEPARTMENT";
        public static final String COLUM_ID_POSITION = "ID_POSITION";
        public static final String COLUMN_LAST_NAME = "LAST_NAME";
        public static final String COLUMN_MID_NAME = "MID_NAME";
        public static final String COLUMN_FIRST_NAME = "FIRST_NAME";

        public static final String CREATE_TABLE = "CREATE TABLE employees (\n" +
                "    ID            INTEGER PRIMARY KEY\n" +
                "                          UNIQUE\n" +
                "                          NOT NULL,\n" +
                "    ID_DEPARTMENT INTEGER CONSTRAINT employeeDepartmentIDFK REFERENCES departments (ID),\n" +
                "    ID_POSITION   INTEGER CONSTRAINT employeePositionIDFK REFERENCES positions (ID),\n" +
                "    LAST_NAME     TEXT    DEFAULT surname,\n" +
                "    MID_NAME      INTEGER DEFAULT middlename,\n" +
                "    FIRST_NAME    TEXT    DEFAULT name\n" +
                ")";
    }

    public static abstract class Contacts implements BaseColumns {
        public static final String TABLE_NAME = "contacts";
        public static final String COLUMN_ID = "ID";
        public static final String COLUMN_PHONE = "PHONE_NUM";
        public static final String COLUMN_EMAIL = "EMAIL";

        public static final String CREATE_TABLE = "CREATE TABLE contacts (\n" +
                "    ID        INTEGER PRIMARY KEY\n" +
                "                      CONSTRAINT contactEmployeeIDFK REFERENCES employees (ID),\n" +
                "    PHONE_NUM TEXT    DEFAULT phone,\n" +
                "    EMAIL     TEXT    DEFAULT email\n" +
                ")";
    }

    public static abstract class Departments implements BaseColumns {
        public static final String TABLE_NAME = "departments";
        public static final String COLUMN_ID = "ID";
        public static final String COLUMN_NAME = "NAME";

        public static final String CREATE_TABLE = "CREATE TABLE departments (\n" +
                "    ID   INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                "                 UNIQUE\n" +
                "                 NOT NULL,\n" +
                "    NAME TEXT    UNIQUE\n" +
                "                 DEFAULT departmentof\n" +
                ")";
    }

    public static abstract class Positions implements BaseColumns {
        public static final String TABLE_NAME = "positions";
        public static final String COLUMN_ID = "ID";
        public static final String COLUMN_NAME = "NAME";

        public static final String CREATE_TABLE = "CREATE TABLE positions (\n" +
                "    ID   INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                "                 UNIQUE\n" +
                "                 NOT NULL,\n" +
                "    NAME TEXT    DEFAULT position\n" +
                "                 UNIQUE\n" +
                ")";
    }

    public static abstract class TaskSources implements BaseColumns {
        public static final String TABLE_NAME = "task_sources";
        public static final String COLUMN_ID = "ID";
        public static final String COLUMN_NAME = "NAME";

        public static final String CREATE_TABLE = "CREATE TABLE task_sources (\n" +
                "    ID   INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                "                 UNIQUE\n" +
                "                 NOT NULL,\n" +
                "    NAME TEXT    UNIQUE\n" +
                "                 DEFAULT tasksource\n" +
                ")";
    }

    public static abstract class TaskTypes implements BaseColumns {
        public static final String TABLE_NAME = "task_types";
        public static final String COLUMN_ID = "ID";
        public static final String COLUMN_NAME = "NAME";

        public static final String CREATE_TABLE = "CREATE TABLE task_types (\n" +
                "    ID   INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                "                 UNIQUE\n" +
                "                 NOT NULL,\n" +
                "    NAME TEXT    UNIQUE\n" +
                "                 DEFAULT tasktype\n" +
                ")";
    }
}
