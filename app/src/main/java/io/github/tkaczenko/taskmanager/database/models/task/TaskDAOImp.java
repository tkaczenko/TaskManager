package io.github.tkaczenko.taskmanager.database.models.task;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.github.tkaczenko.taskmanager.database.DatabaseContract;
import io.github.tkaczenko.taskmanager.database.common.BaseDAOImp;
import io.github.tkaczenko.taskmanager.database.models.dictionary.Department;
import io.github.tkaczenko.taskmanager.database.models.dictionary.Position;
import io.github.tkaczenko.taskmanager.database.models.dictionary.TaskSource;
import io.github.tkaczenko.taskmanager.database.models.dictionary.TaskType;
import io.github.tkaczenko.taskmanager.database.models.employee.Employee;
import io.github.tkaczenko.taskmanager.database.models.employee.EmployeeDAOImp;

/**
 * Created by tkaczenko on 19.11.16.
 */

public class TaskDAOImp extends BaseDAOImp<Task> implements TaskDAO {
    private static final String WHERE_ID_EQUALS = DatabaseContract.Tasks.COLUMN_ID + " =?";

    private static final String TASK_PREFIX = "task.";
    private static final String TASK_SOURCE_PREFIX = "src.";
    private static final String TASK_TYPE_PREFIX = "type.";

    private static final String TASK_ID = TASK_PREFIX + "ID";
    private static final String TASK_SOURCE_ID = TASK_SOURCE_PREFIX + "ID";
    private static final String TASK_SOURCE_NAME = TASK_SOURCE_PREFIX + "NAME";
    private static final String TASK_TYPE_ID = TASK_TYPE_PREFIX + "ID";
    private static final String TASK_TYPE_NAME = TASK_TYPE_PREFIX + "NAME";
    private static final String TASK_SHORT_NAME = TASK_PREFIX + "SHORT_NAME";
    private static final String TASK_DESCRIPTION = TASK_PREFIX + "DESCRIPTION";
    private static final String TASK_DATE_ISSUE = TASK_PREFIX + "DATE_ISSUE";
    private static final String TASK_DATE_PLANNED = TASK_PREFIX + "DATE_PLANNED";
    private static final String TASK_DATE_EXECUTION = TASK_PREFIX + "DATE_EXECUTION";
    private static final String TASK_REJECTION_REASON = TASK_PREFIX + "REJECTION_REASON";
    private static final String TASK_COMPLETED = TASK_PREFIX + "COMPLETED";
    private static final String TASK_CANCELED = TASK_PREFIX + "CANCELED";
    private static final String TASK_SOURCE_DOC = TASK_PREFIX + "SOURCE_DOC";
    private static final String TASK_SOURCE_NUM = TASK_PREFIX + "SOURCE_NUM";

    public TaskDAOImp(Context mContext) {
        super(mContext);
    }

    @Override
    public long save(Task value) {
        ContentValues values = new ContentValues();

        values.put(DatabaseContract.Tasks.COLUMN_ID_SOURCE, value.getTaskSource().getId());
        values.put(DatabaseContract.Tasks.COLUMN_ID_TYPE, value.getTaskType().getId());
        values.put(DatabaseContract.Tasks.COLUMN_SHORT_NAME, value.getShortName());
        values.put(DatabaseContract.Tasks.COLUMN_DESCRIPTION, value.getDescription());
        values.put(
                DatabaseContract.Tasks.COLUMN_DATE_ISSUE,
                DatabaseContract.Tasks.formatter.format(value.getDateIssue())
        );
        values.put(
                DatabaseContract.Tasks.COLUMN_DATE_PLANNED,
                DatabaseContract.Tasks.formatter.format(value.getDatePlanned())
        );
        values.put(
                DatabaseContract.Tasks.COLUMN_DATE_EXECUTION,
                DatabaseContract.Tasks.formatter.format(value.getDateExecution())
        );
        values.put(DatabaseContract.Tasks.COLUMN_REJECTION_REASON, value.getRejectionReason());
        values.put(DatabaseContract.Tasks.COLUMN_COMPLETED, value.isCompleted());
        values.put(DatabaseContract.Tasks.COLUMN_CANCELED, value.isCanceled());
        values.put(DatabaseContract.Tasks.COLUMN_SOURCE_DOC, value.getSourceDoc());
        values.put(DatabaseContract.Tasks.COLUMN_SOURCE_NUM, value.getSourceNum());

        long taskID = database.insert(DatabaseContract.Tasks.TABLE_NAME, null, values);

        for (Employee employee : value.getEmployees()) {
            createTaskEmp(taskID, employee.getId());
        }

        return taskID;
    }

    @Override
    public int update(Task value) {
        ContentValues values = new ContentValues();

        values.put(DatabaseContract.Tasks.COLUMN_ID_SOURCE, value.getTaskSource().getId());
        values.put(DatabaseContract.Tasks.COLUMN_ID_TYPE, value.getTaskType().getId());
        values.put(DatabaseContract.Tasks.COLUMN_SHORT_NAME, value.getShortName());
        values.put(DatabaseContract.Tasks.COLUMN_DESCRIPTION, value.getDescription());
        Date date = value.getDateIssue();
        if (date != null) {
            values.put(
                    DatabaseContract.Tasks.COLUMN_DATE_ISSUE,
                    DatabaseContract.Tasks.formatter.format(value.getDateIssue())
            );
        }
        date = value.getDatePlanned();
        if (date != null) {
            values.put(
                    DatabaseContract.Tasks.COLUMN_DATE_PLANNED,
                    DatabaseContract.Tasks.formatter.format(value.getDatePlanned())
            );
        }
        date = value.getDateExecution();
        if (date != null) {
            values.put(
                    DatabaseContract.Tasks.COLUMN_DATE_EXECUTION,
                    DatabaseContract.Tasks.formatter.format(value.getDateExecution())
            );
        }
        values.put(DatabaseContract.Tasks.COLUMN_REJECTION_REASON, value.getRejectionReason());
        values.put(DatabaseContract.Tasks.COLUMN_COMPLETED, value.isCompleted());
        values.put(DatabaseContract.Tasks.COLUMN_CANCELED, value.isCanceled());
        values.put(DatabaseContract.Tasks.COLUMN_SOURCE_DOC, value.getSourceDoc());
        values.put(DatabaseContract.Tasks.COLUMN_SOURCE_NUM, value.getSourceNum());

        int taskID = database.update(
                DatabaseContract.Tasks.TABLE_NAME, values,
                WHERE_ID_EQUALS, new String[]{String.valueOf(value.getId())}
        );

        database.delete(DatabaseContract.TaskEmployee.TABLE_NAME,
                DatabaseContract.TaskEmployee.COLUMN_TASK + "=?",
                new String[]{String.valueOf(value.getId())}
        );

        for (Employee employee : value.getEmployees()) {
            createTaskEmp(value.getId(), employee.getId());
        }
        return taskID;
    }

    @Override
    public long remove(Task value) {
        return database.delete(
                DatabaseContract.Tasks.TABLE_NAME, WHERE_ID_EQUALS,
                new String[]{String.valueOf(value.getId())}
        );
    }

    @Override
    public List<Task> getAll() {
        List<Task> tasks = new ArrayList<>();

        String query = "SELECT " + TASK_ID + "," + TASK_SOURCE_ID +
                "," + TASK_SOURCE_NAME + "," + TASK_TYPE_ID + "," + TASK_TYPE_NAME + "," +
                TASK_SHORT_NAME + "," + TASK_DESCRIPTION + "," + TASK_DATE_ISSUE + "," +
                TASK_DATE_PLANNED + "," + TASK_DATE_EXECUTION + "," + TASK_REJECTION_REASON + "," +
                TASK_COMPLETED + "," + TASK_CANCELED + "," + TASK_SOURCE_DOC + "," + TASK_SOURCE_NUM
                + "," + EmployeeDAOImp.EMP_ID_WITH_PREFIX + "," + EmployeeDAOImp.DEP_ID_WITH_PREFIX + "," +
                EmployeeDAOImp.DEP_NAME_WITH_PREFIX + "," + EmployeeDAOImp.POS_ID_WITH_PREFIX + "," +
                EmployeeDAOImp.POS_NAME_WITH_PREFIX + "," + EmployeeDAOImp.EMP_LAST_NAME_WITH_PREFIX +
                "," + EmployeeDAOImp.EMP_MID_NAME_WITH_PREFIX + "," +
                EmployeeDAOImp.EMP_FIRST_NAME_WITH_PREFIX + "," +
                EmployeeDAOImp.EMP_PHONE_NUM_WITH_PREFIX + "," + EmployeeDAOImp.EMP_EMAIL_WITH_PREFIX +
                " FROM " + DatabaseContract.Tasks.TABLE_NAME + " task" +
                " LEFT OUTER JOIN " + DatabaseContract.TaskSources.TABLE_NAME + " src" +
                " ON " + "task.ID_SOURCE = " + TASK_SOURCE_ID +
                " LEFT OUTER JOIN " + DatabaseContract.TaskTypes.TABLE_NAME + " type" +
                " ON " + "task.ID_TYPE = " + TASK_TYPE_ID +
                " LEFT OUTER JOIN " + DatabaseContract.TaskEmployee.TABLE_NAME + " te" +
                " ON " + "task.ID = te.ID_TASK" +
                " LEFT OUTER JOIN " + DatabaseContract.Employees.TABLE_NAME + " emp" +
                " ON " + "te.ID_EMPLOYEE = emp.ID" +
                " LEFT OUTER JOIN " + DatabaseContract.Departments.TABLE_NAME + " dep" +
                " ON " + "emp.ID_DEPARTMENT = dep.ID" +
                " LEFT OUTER JOIN " + DatabaseContract.Positions.TABLE_NAME + " pos" +
                " ON " + "emp.ID_POSITION = pos.ID" +
                " LEFT OUTER JOIN " + DatabaseContract.Contacts.TABLE_NAME + " con" +
                " ON " + "emp.ID = con.ID";

        Cursor cursor = database.rawQuery(query, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);

            Task task = null;
            boolean isFound = false;
            for (int i = 0; i < tasks.size(); i++) {
                Task temp = tasks.get(i);
                if (temp.getId() == id) {
                    task = temp;
                    isFound = true;
                    break;
                }
            }
            if (isFound) {
                task.getEmployees().add(parseEmployee(cursor));
            } else {
                task = new Task();
                task.setId(id);
                TaskSource taskSource = new TaskSource();
                taskSource.setId(cursor.getInt(1));
                taskSource.setName(cursor.getString(2));
                TaskType taskType = new TaskType();
                taskType.setId(cursor.getInt(3));
                taskType.setName(cursor.getString(4));
                task.setShortName(cursor.getString(5));
                task.setDescription(cursor.getString(6));
                try {
                    task.setDateIssue(DatabaseContract.Tasks.formatter.parse(cursor.getString(7)));
                } catch (ParseException | NullPointerException e) {
                    task.setDateIssue(null);
                }
                try {
                    task.setDatePlanned(DatabaseContract.Tasks.formatter.parse(cursor.getString(8)));
                } catch (ParseException | NullPointerException e) {
                    task.setDatePlanned(null);
                }
                try {
                    task.setDateExecution(DatabaseContract.Tasks.formatter.parse(cursor.getString(9)));
                } catch (ParseException | NullPointerException e) {
                    task.setDateExecution(null);
                }
                task.setRejectionReason(cursor.getString(10));
                task.setCompleted(cursor.getInt(11) != 0);
                task.setCanceled(cursor.getInt(12) != 0);
                task.setSourceDoc(cursor.getString(13));
                task.setSourceNum(cursor.getString(14));
                task.setTaskSource(taskSource);
                task.setTaskType(taskType);
                task.getEmployees().add(parseEmployee(cursor));
                tasks.add(task);
            }
        }
        cursor.close();
        return tasks;
    }

    private long createTaskEmp(long taskID, int empID) {
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.TaskEmployee.COLUMN_TASK, taskID);
        values.put(DatabaseContract.TaskEmployee.COLUMN_EMPLOYEE, empID);
        return database.insert(DatabaseContract.TaskEmployee.TABLE_NAME, null, values);
    }

    private Employee parseEmployee(Cursor cursor) {
        Employee employee = new Employee();

        employee.setId(cursor.getInt(15));
        Department department = new Department();
        department.setId(cursor.getInt(16));
        department.setName(cursor.getString(17));
        Position position = new Position();
        position.setId(cursor.getInt(18));
        position.setName(cursor.getString(19));
        employee.setLastName(cursor.getString(20));
        employee.setMidName(cursor.getString(21));
        employee.setFirstName(cursor.getString(22));
        Employee.Contact contact = new Employee.Contact();
        contact.setId(employee.getId());
        contact.setPhoneNum(cursor.getString(23));
        contact.setEmail(cursor.getString(24));

        employee.setDepartment(department);
        employee.setPosition(position);
        employee.setContact(contact);
        return employee;
    }
}
