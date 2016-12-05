package io.github.tkaczenko.taskmanager.database.models.dictionary;

import android.os.Parcel;

import io.github.tkaczenko.taskmanager.database.models.dictionary.common.Dictionary;

/**
 * Created by tkaczenko on 26.10.16.
 */

public class Position extends Dictionary {
    public Position() {

    }

    public Position(String name) {
        super(name);
    }

    private Position(Parcel parcel) {
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
