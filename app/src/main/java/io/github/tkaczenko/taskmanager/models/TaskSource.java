package io.github.tkaczenko.taskmanager.models;

import android.os.Parcel;

/**
 * Created by tkaczenko on 26.10.16.
 */

public class TaskSource extends DictionaryObject {
    public TaskSource(int id, String name) {
        super(id, name);
    }

    protected TaskSource(Parcel parcel) {
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
