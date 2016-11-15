package io.github.tkaczenko.taskmanager.database.model.dictionary;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tkaczenko on 26.10.16.
 */

public abstract class DictionaryObject implements Parcelable {
    protected int id;
    protected String name;

    protected DictionaryObject(int id, String name) {
        this.id = id;
        this.name = name;
    }

    protected DictionaryObject (Parcel parcel) {
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
}
