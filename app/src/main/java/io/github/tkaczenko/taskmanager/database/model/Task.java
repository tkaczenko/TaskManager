package io.github.tkaczenko.taskmanager.database.model;

/**
 * Created by tkaczenko on 26.10.16.
 */

public class Task {
    private int id;
    private int idSource;
    private int idType;
    private String shortName;
    private String description;
    private String dateIssue;
    private String datePlanned;
    private String dateExecution;
    private String rejectionReason;
    private boolean completed;
    private boolean canceled;
    private String sourceDoc;
    private String sourceNum;

    public Task(int id, int idSource, int idType, String shortName, String description,
                String dateIssue, String datePlanned, String dateExecution, String rejectionReason,
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

    public String getDateIssue() {
        return dateIssue;
    }

    public void setDateIssue(String dateIssue) {
        this.dateIssue = dateIssue;
    }

    public String getDatePlanned() {
        return datePlanned;
    }

    public void setDatePlanned(String datePlanned) {
        this.datePlanned = datePlanned;
    }

    public String getDateExecution() {
        return dateExecution;
    }

    public void setDateExecution(String dateExecution) {
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
}
