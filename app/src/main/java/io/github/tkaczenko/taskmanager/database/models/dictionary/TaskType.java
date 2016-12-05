package io.github.tkaczenko.taskmanager.database.models.dictionary;

import android.os.Parcel;

import io.github.tkaczenko.taskmanager.database.models.dictionary.common.Dictionary;

/**
 * Created by tkaczenko on 26.10.16.
 */

public class TaskType extends Dictionary {
    public TaskType() {

    }

    public TaskType(String name) {
        super(name);
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
