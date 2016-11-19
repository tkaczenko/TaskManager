package io.github.tkaczenko.taskmanager.database;

import android.provider.BaseColumns;

/**
 * Created by tkaczenko on 19.11.16.
 */

public class DatabaseContract {
    public static final String DATABASE_NAME = "tasks.sqlite";

    private DatabaseContract() {
    }

    public static abstract class Task implements BaseColumns {
        public static final String TABLE_TASK = "tasks";
        public static final String COLUMN_ID = "ID";
        public static final String COLUMN_ID_SOURCE = "ID_SOURCE";
        public static final String COLUMN_ID_TYPE = "ID_TYPE";
        public static final String COLUMN_SHORT_NAME = "SHORT_NAME";
        public static final String COLUMN_DESCRIPTION = "DESCRIPTION";
        public static final String COLUMN_DATE_ISSUE = "DATE_ISSUE";
        public static final String COLUMN_DATE_PLANNED = "DATE_PLANNED";
        public static final String COLUMN_DATE_EXECUTION = "DATE_EXECUTION";
        public static final String COLUMN_REJECTION_REASON = "REJUCTION_RESON";
        public static final String COLUMN_COMPLETED = "COMPLETED";
        public static final String COLUMN_CANCELED = "CANCELED";
        public static final String COLUMN_SOURCE_DOC = "SOURCE_DOC";
        public static final String COLUMN_SOURCE_NUM = "SOURCE_NUM";
    }

    public static abstract class TaskEmployee implements BaseColumns {
        public static final String TABLE_NAME = "task_employee";
        public static final String COLUMN_TASK = "ID_TASK";
        public static final String COLUMN_EMPLOYEE = "ID_EMPLOYEE";
    }

    public static abstract class Employee implements BaseColumns {
        public static final String TABLE_EMPLOYEE = "employees";
        public static final String COLUMN_ID = "ID";
        public static final String COLUMN_ID_DEPARTMENT = "ID_DEPARTMENT";
        public static final String COLUM_ID_POSITION = "ID_POSITION";
        public static final String COLUMN_LAST_NAME = "LAST_NAME";
        public static final String COLUMN_MID_NAME = "MID_NAME";
        public static final String COLUMN_FIRST_NAME = "FIRST_NAME";
    }

    public static abstract class Contact implements BaseColumns {
        public static final String TABLE_CONTACT = "contacts";
        public static final String COLUMN_PHONE = "PHONE_NUM";
        public static final String COLUMN_EMAIL = "EMAIL";
    }

    public static abstract class Department implements BaseColumns {
        public static final String TABLE_DEPARTMENT = "departments";
        public static final String COLUMN_ID = "ID";
        public static final String COLUMN_NAME = "NAME";
    }

    public static abstract class Position implements BaseColumns {
        public static final String TABLE_POSITION = "positions";
        public static final String COLUMN_ID = "ID";
        public static final String COLUMN_NAME = "NAME";
    }

    public static abstract class TaskSource implements BaseColumns {
        public static final String TABLE_TASK_TOURCE = "task_sources";
        public static final String COLUMN_ID = "ID";
        public static final String COLUMN_NAME = "NAME";
    }

    public static abstract class TaskType implements BaseColumns {
        public static final String TABLE_TASK_TYPE = "task_types";
        public static final String COLUMN_ID = "ID";
        public static final String COLUMN_NAME = "NAME";
    }
}
