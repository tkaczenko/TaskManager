package io.github.tkaczenko.taskmanager.database.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.github.tkaczenko.taskmanager.database.DatabaseContract;
import io.github.tkaczenko.taskmanager.database.model.Employee;
import io.github.tkaczenko.taskmanager.database.model.Task;
import io.github.tkaczenko.taskmanager.database.model.dictionary.Department;
import io.github.tkaczenko.taskmanager.database.model.dictionary.Position;
import io.github.tkaczenko.taskmanager.database.model.dictionary.TaskSource;
import io.github.tkaczenko.taskmanager.database.model.dictionary.TaskType;

/**
 * Created by tkaczenko on 19.11.16.
 */
//// TODO: 29.11.16 Test DAO
public class TaskDAO extends DAO<Task> {
    private static final String WHERE_ID_EQUALS = DatabaseContract.Task.COLUMN_ID + " =?";

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
    private static final String TASK_COMPLETED = TASK_PREFIX + "COMPLETED";
    private static final String TASK_CANCELED = TASK_PREFIX + "CANCELED";
    private static final String TASK_SOURCE_DOC = TASK_PREFIX + "SOURCE_DOC";
    private static final String TASK_SOURCE_NUM = TASK_PREFIX + "SOURCE_NUM";

    public TaskDAO(Context mContext) {
        super(mContext);
    }

    @Override
    public long save(Task value, Integer... ids) {
        ContentValues values = new ContentValues();

        values.put(DatabaseContract.Task.COLUMN_ID_SOURCE, value.getTaskSource().getId());
        values.put(DatabaseContract.Task.COLUMN_ID_TYPE, value.getTaskType().getId());
        values.put(DatabaseContract.Task.COLUMN_SHORT_NAME, value.getShortName());
        values.put(DatabaseContract.Task.COLUMN_DESCRIPTION, value.getDescription());
        values.put(
                DatabaseContract.Task.COLUMN_DATE_ISSUE,
                DatabaseContract.Task.formatter.format(value.getDateIssue())
        );
        values.put(
                DatabaseContract.Task.COLUMN_DATE_PLANNED,
                DatabaseContract.Task.formatter.format(value.getDatePlanned())
        );
        values.put(
                DatabaseContract.Task.COLUMN_DATE_EXECUTION,
                DatabaseContract.Task.formatter.format(value.getDateExecution())
        );
        values.put(DatabaseContract.Task.COLUMN_REJECTION_REASON, value.getRejectionReason());
        values.put(DatabaseContract.Task.COLUMN_COMPLETED, value.isCompleted());
        values.put(DatabaseContract.Task.COLUMN_CANCELED, value.isCanceled());
        values.put(DatabaseContract.Task.COLUMN_SOURCE_DOC, value.getSourceDoc());
        values.put(DatabaseContract.Task.COLUMN_SOURCE_NUM, value.getSourceNum());

        long taskID = database.insert(DatabaseContract.Task.TABLE_TASK, null, values);
        List<Integer> ints = Arrays.asList(ids);

        for (Integer id : ints) {
            createTaskEmp(taskID, id);
        }

        return taskID;
    }

    @Override
    public int update(Task value, Integer... ids) {
        ContentValues values = new ContentValues();

        values.put(DatabaseContract.Task.COLUMN_ID, value.getId());
        values.put(DatabaseContract.Task.COLUMN_ID_SOURCE, value.getTaskSource().getId());
        values.put(DatabaseContract.Task.COLUMN_ID_TYPE, value.getTaskType().getId());
        values.put(DatabaseContract.Task.COLUMN_SHORT_NAME, value.getShortName());
        values.put(DatabaseContract.Task.COLUMN_DESCRIPTION, value.getDescription());
        values.put(
                DatabaseContract.Task.COLUMN_DATE_ISSUE,
                DatabaseContract.Task.formatter.format(value.getDateIssue())
        );
        values.put(
                DatabaseContract.Task.COLUMN_DATE_PLANNED,
                DatabaseContract.Task.formatter.format(value.getDatePlanned())
        );
        values.put(
                DatabaseContract.Task.COLUMN_DATE_EXECUTION,
                DatabaseContract.Task.formatter.format(value.getDateExecution())
        );
        values.put(DatabaseContract.Task.COLUMN_REJECTION_REASON, value.getRejectionReason());
        values.put(DatabaseContract.Task.COLUMN_COMPLETED, value.isCompleted());
        values.put(DatabaseContract.Task.COLUMN_CANCELED, value.isCanceled());
        values.put(DatabaseContract.Task.COLUMN_SOURCE_DOC, value.getSourceDoc());
        values.put(DatabaseContract.Task.COLUMN_SOURCE_NUM, value.getSourceNum());

        int taskID = database.update(
                DatabaseContract.Task.TABLE_TASK, values,
                WHERE_ID_EQUALS, new String[]{String.valueOf(value.getId())}
        );

        List<Integer> ints = Arrays.asList(ids);

        for (Integer id : ints) {
            createOrUpdateTaskEmp(taskID, id);
        }

        return taskID;
    }

    @Override
    public long remove(Task value) {
        return database.delete(
                DatabaseContract.Task.TABLE_TASK, WHERE_ID_EQUALS,
                new String[]{String.valueOf(value.getId())}
        );
    }

    @Override
    public List<Task> getAll() {
        List<Task> tasks = new ArrayList<>();

        String query = "SELECT " + TASK_ID + "," + TASK_SOURCE_ID +
                "," + TASK_SOURCE_NAME + "," + TASK_TYPE_ID + "," + TASK_TYPE_NAME + "," +
                TASK_SHORT_NAME + "," + TASK_DESCRIPTION + "," + TASK_DATE_ISSUE + "," +
                TASK_DATE_PLANNED + "," + TASK_DATE_EXECUTION + "," + TASK_COMPLETED + "," +
                TASK_CANCELED + "," + TASK_SOURCE_DOC + "," + TASK_SOURCE_NUM + "," +
                EmployeeDAO.EMP_ID_WITH_PREFIX + "," + EmployeeDAO.DEP_ID_WITH_PREFIX + "," +
                EmployeeDAO.DEP_NAME_WITH_PREFIX + "," + EmployeeDAO.POS_ID_WITH_PREFIX + "," +
                EmployeeDAO.POS_NAME_WITH_PREFIX + "," + EmployeeDAO.EMP_LAST_NAME_WITH_PREFIX +
                "," + EmployeeDAO.EMP_MID_NAME_WITH_PREFIX + "," +
                EmployeeDAO.EMP_FIRST_NAME_WITH_PREFIX + "," +
                EmployeeDAO.EMP_PHONE_NUM_WITH_PREFIX + "," + EmployeeDAO.EMP_EMAIL_WITH_PREFIX +
                " FROM " + DatabaseContract.Task.TABLE_TASK + " task" +
                " LEFT OUTER JOIN " + DatabaseContract.TaskSource.TABLE_TASK_TOURCE + " src" +
                " ON " + "task.ID_SOURCE = " + TASK_SOURCE_ID +
                " LEFT OUTER JOIN " + DatabaseContract.TaskType.TABLE_TASK_TYPE + " type" +
                " ON " + "task.ID_TYPE = " + TASK_TYPE_ID +
                " LEFT OUTER JOIN " + DatabaseContract.TaskEmployee.TABLE_NAME + " te" +
                " ON " + "task.ID = te.ID_TASK" +
                " LEFT OUTER JOIN " + DatabaseContract.Employee.TABLE_EMPLOYEE + " emp" +
                " ON " + "te.ID_EMPLOYEE = emp.ID" +
                " LEFT OUTER JOIN " + DatabaseContract.Department.TABLE_DEPARTMENT + " dep" +
                " ON " + "emp.ID_DEPARTMENT = dep.ID" +
                " LEFT OUTER JOIN " + DatabaseContract.Position.TABLE_POSITION + " pos" +
                " ON " + "emp.ID_POSITION = pos.ID" +
                " LEFT OUTER JOIN " + DatabaseContract.Contact.TABLE_CONTACT + " con" +
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
                    task.setDateIssue(DatabaseContract.Task.formatter.parse(cursor.getString(7)));
                } catch (ParseException | NullPointerException e) {
                    task.setDateIssue(null);
                }
                try {
                    task.setDatePlanned(DatabaseContract.Task.formatter.parse(cursor.getString(8)));
                } catch (ParseException | NullPointerException e) {
                    task.setDatePlanned(null);
                }
                try {
                    task.setDateExecution(DatabaseContract.Task.formatter.parse(cursor.getString(9)));
                } catch (ParseException | NullPointerException e) {
                    task.setDateExecution(null);
                }
                task.setCompleted(cursor.getInt(10) != 0);
                task.setCanceled(cursor.getInt(11) != 0);
                task.setSourceDoc(cursor.getString(12));
                task.setSourceNum(cursor.getString(13));
                task.setTaskSource(taskSource);
                task.setTaskType(taskType);
                task.getEmployees().add(parseEmployee(cursor));
                tasks.add(task);
            }
        }
        cursor.close();
        return tasks;
    }

    private long createOrUpdateTaskEmp(long taskID, int empID) {
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.TaskEmployee.COLUMN_TASK, taskID);
        values.put(DatabaseContract.TaskEmployee.COLUMN_EMPLOYEE, empID);

        long count = database.update(
                DatabaseContract.TaskEmployee.TABLE_NAME, values,
                DatabaseContract.TaskEmployee.COLUMN_EMPLOYEE + "=?",
                new String[]{String.valueOf(empID)}
        );
        if (count <= 0) {
            count = database.insert(
                    DatabaseContract.TaskEmployee.TABLE_NAME, null, values
            );
        }
        return count;
    }

    private long createTaskEmp(long taskID, int empID) {
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.TaskEmployee.COLUMN_TASK, taskID);
        values.put(DatabaseContract.TaskEmployee.COLUMN_EMPLOYEE, empID);
        return database.insert(DatabaseContract.TaskEmployee.TABLE_NAME, null, values);
    }

    private Employee parseEmployee(Cursor cursor) {
        Employee employee = new Employee();

        employee.setId(cursor.getInt(14));
        Department department = new Department();
        department.setId(cursor.getInt(15));
        department.setName(cursor.getString(16));
        Position position = new Position();
        position.setId(cursor.getInt(17));
        position.setName(cursor.getString(18));
        employee.setLastName(cursor.getString(19));
        employee.setMidName(cursor.getString(20));
        employee.setFirstName(cursor.getString(21));
        Employee.Contact contact = new Employee.Contact();
        contact.setId(employee.getId());
        contact.setPhoneNum(cursor.getString(22));
        contact.setEmail(cursor.getString(23));

        employee.setDepartment(department);
        employee.setPosition(position);
        employee.setContact(contact);
        return employee;
    }
}
