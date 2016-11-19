package io.github.tkaczenko.taskmanager.database.model.dictionary;

import android.os.Parcel;

/**
 * Created by tkaczenko on 26.10.16.
 */

public class TaskType extends DictionaryObject {
    public TaskType() {

    }

    public TaskType(int id, String name) {
        super(id, name);
    }

    private TaskType(Parcel parcel) {
        super(parcel);
    }

    public static Creator<TaskType> CREATOR = new Creator<TaskType>() {

        @Override
        public TaskType createFromParcel(Parcel source) {
            return new TaskType(source);
        }

        @Override
        public TaskType[] newArray(int size) {
            return new TaskType[size];
        }

    };
}
