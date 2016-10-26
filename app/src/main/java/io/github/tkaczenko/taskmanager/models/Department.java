package io.github.tkaczenko.taskmanager.models;

import android.os.Parcel;

/**
 * Created by tkaczenko on 26.10.16.
 */

public class Department extends DictionaryObject {
    public Department(int id, String name) {
        super(id, name);
    }

    protected Department(Parcel parcel) {
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
