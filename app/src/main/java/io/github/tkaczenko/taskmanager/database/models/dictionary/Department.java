package io.github.tkaczenko.taskmanager.database.models.dictionary;

import android.os.Parcel;

import io.github.tkaczenko.taskmanager.database.models.dictionary.common.Dictionary;

/**
 * Created by tkaczenko on 26.10.16.
 */

public class Department extends Dictionary {
    public Department() {

    }

    public Department(String name) {
        super(name);
    }

    private Department(Parcel parcel) {
        super(parcel);
    }

    public static Creator<Department> CREATOR = new Creator<Department>() {

        @Override
        public Department createFromParcel(Parcel source) {
            return new Department(source);
        }

        @Override
        public Department[] newArray(int size) {
            return new Department[size];
        }

    };
}
