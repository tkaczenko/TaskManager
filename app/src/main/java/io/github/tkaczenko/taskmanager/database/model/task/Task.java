package io.github.tkaczenko.taskmanager.database.model.task;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.github.tkaczenko.taskmanager.database.model.dictionary.TaskSource;
import io.github.tkaczenko.taskmanager.database.model.dictionary.TaskType;
import io.github.tkaczenko.taskmanager.database.model.employee.Employee;

/**
 * Created by tkaczenko on 26.10.16.
 */

public class Task implements Parcelable {
    private int id;
    private TaskSource taskSource;
    private TaskType taskType;
    private String shortName;
    private String description;
    private Date dateIssue;
    private Date datePlanned;
    private Date dateExecution;
    private String rejectionReason;
    private boolean completed;
    private boolean canceled;
    private String sourceDoc;
    private String sourceNum;

    private List<Employee> employees = new ArrayList<>();

    public Task() {

    }

    public Task(int id, TaskSource taskSource, TaskType taskType, String shortName,
                String description, Date dateIssue, Date datePlanned, Date dateExecution,
                String rejectionReason, boolean completed, boolean canceled,
                String sourceDoc, String sourceNum) {
        this.id = id;
        this.taskSource = taskSource;
        this.taskType = taskType;
        this.shortName = shortName;
        this.description = description;
        this.dateIssue = dateIssue;
        this.datePlanned = datePlanned;
        this.dateExecution = dateExecution;
        this.rejectionReason = rejectionReason;
        this.completed = completed;
        this.canceled = canceled;
        this.sourceDoc = sourceDoc;
        this.sourceNum = sourceNum;
    }

    private Task(Parcel in) {
        id = in.readInt();
        taskSource = in.readParcelable(TaskSource.class.getClassLoader());
        taskType = in.readParcelable(TaskType.class.getClassLoader());
        shortName = in.readString();
        description = in.readString();
        long date = in.readLong();
        dateIssue = (date != -1) ? new Date(date) : null;
        date = in.readLong();
        datePlanned = (date != -1) ? new Date(date) : null;
        date = in.readLong();
        dateExecution = (date != -1) ? new Date(date) : null;
        rejectionReason = in.readString();
        completed = in.readInt() != 0;
        canceled = in.readInt() != 0;
        sourceDoc = in.readString();
        sourceNum = in.readString();
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TaskSource getTaskSource() {
        return taskSource;
    }

    public void setTaskSource(TaskSource taskSource) {
        this.taskSource = taskSource;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateIssue() {
        return dateIssue;
    }

    public void setDateIssue(Date dateIssue) {
        this.dateIssue = dateIssue;
    }

    public Date getDatePlanned() {
        return datePlanned;
    }

    public void setDatePlanned(Date datePlanned) {
        this.datePlanned = datePlanned;
    }

    public Date getDateExecution() {
        return dateExecution;
    }

    public void setDateExecution(Date dateExecution) {
        this.dateExecution = dateExecution;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public String getSourceDoc() {
        return sourceDoc;
    }

    public void setSourceDoc(String sourceDoc) {
        this.sourceDoc = sourceDoc;
    }

    public String getSourceNum() {
        return sourceNum;
    }

    public void setSourceNum(String sourceNum) {
        this.sourceNum = sourceNum;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(getId());
        dest.writeParcelable(taskSource, flags);
        dest.writeParcelable(taskType, flags);
        dest.writeString(shortName);
        dest.writeString(description);
        dest.writeLong(dateIssue != null ? dateIssue.getTime() : -1);
        dest.writeLong(datePlanned != null ? datePlanned.getTime() : -1);
        dest.writeLong(dateExecution != null ? dateExecution.getTime() : -1);
        dest.writeString(rejectionReason);
        dest.writeInt(completed ? 1 : 0);
        dest.writeInt(canceled ? 1 : 0);
        dest.writeString(sourceDoc);
        dest.writeString(sourceNum);
    }

    public List<Employee> getEmployees() {
        return employees;
    }
}
