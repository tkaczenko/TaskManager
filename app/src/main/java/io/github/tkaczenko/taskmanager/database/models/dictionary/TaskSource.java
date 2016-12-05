package io.github.tkaczenko.taskmanager.database.models.dictionary;

import android.os.Parcel;

import io.github.tkaczenko.taskmanager.database.models.dictionary.common.Dictionary;

/**
 * Created by tkaczenko on 26.10.16.
 */

public class TaskSource extends Dictionary {
    public TaskSource() {

    }

    public TaskSource(String name) {
        super(name);
    }

    private TaskSource(Parcel parcel) {
        super(parcel);
    }

    public static Creator<TaskSource> CREATOR = new Creator<TaskSource>() {

        @Override
        public TaskSource createFromParcel(Parcel source) {
            return new TaskSource(source);
        }

        @Override
        public TaskSource[] newArray(int size) {
            return new TaskSource[size];
        }

    };
}
