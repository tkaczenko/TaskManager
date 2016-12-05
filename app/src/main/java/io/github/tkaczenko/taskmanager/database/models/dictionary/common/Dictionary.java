package io.github.tkaczenko.taskmanager.database.models.dictionary.common;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tkaczenko on 26.10.16.
 */

public abstract class Dictionary implements Parcelable {
    protected int id;
    protected String name;

    public Dictionary() {

    }

    protected Dictionary(String name) {
        this.name = name;
    }

    protected Dictionary(Parcel parcel) {
        this.id = parcel.readInt();
        this.name = parcel.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Dictionary other = (Dictionary) obj;
        return id == other.id;
    }

    @Override
    public String toString() {
        return name;
    }
}
