package io.github.tkaczenko.taskmanager.models;

import android.os.Parcel;

/**
 * Created by tkaczenko on 26.10.16.
 */

public final class Position extends DictionaryObject {
    public Position(int id, String name) {
        super(id, name);
    }

    protected Position(Parcel parcel) {
        super(parcel);
    }

    public static Creator<Position> CREATOR = new Creator<Position>() {

        @Override
        public Position createFromParcel(Parcel source) {
            return new Position(source);
        }

        @Override
        public Position[] newArray(int size) {
            return new Position[size];
        }

    };
}
