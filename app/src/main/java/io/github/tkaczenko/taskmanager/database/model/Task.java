package io.github.tkaczenko.taskmanager.database.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.Set;

/**
 * Created by tkaczenko on 26.10.16.
 */

public class Task implements Parcelable {
    private int id;
    private int idSource;
    private int idType;
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

    private Set<Employee> employees;

    public Task() {

    }

    public Task(int id, int idSource, int idType, String shortName, String description,
                Date dateIssue, Date datePlanned, Date dateExecution, String rejectionReason,
                boolean completed, boolean canceled, String sourceDoc, String sourceNum) {
        this.id = id;
        this.idSource = idSource;
        this.idType = idType;
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
        idSource = in.readInt();
        idType = in.readInt();
        shortName = in.readString();
        description = in.readString();
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

    public int getIdSource() {
        return idSource;
    }

    public void setIdSource(int idSource) {
        this.idSource = idSource;
    }

    public int getIdType() {
        return idType;
    }

    public void setIdType(int idType) {
        this.idType = idType;
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
        dest.writeInt(getIdSource());
        dest.writeInt(getIdType());
        dest.writeString(getShortName());
        dest.writeString(getDescription());
        dest.writeLong(getDateIssue().getTime());
        dest.writeLong(getDatePlanned().getTime());
        dest.writeLong(getDateExecution().getTime());
        dest.writeString(getRejectionReason());
        dest.writeInt(isCompleted() ? 1 : 0);
        dest.writeInt(isCanceled() ? 1 : 0);
        dest.writeString(getSourceDoc());
        dest.writeString(getSourceNum());
    }
}
