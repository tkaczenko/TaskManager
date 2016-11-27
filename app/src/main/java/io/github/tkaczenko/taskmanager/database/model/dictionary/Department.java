package io.github.tkaczenko.taskmanager.database.model.dictionary;

import android.os.Parcel;

/**
 * Created by tkaczenko on 26.10.16.
 */

public class Department extends DictionaryObject {
    public Department() {

    }

    public Department(String name) {
        super(name);
    }

    public Department(int id, String name) {
        super(id, name);
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
