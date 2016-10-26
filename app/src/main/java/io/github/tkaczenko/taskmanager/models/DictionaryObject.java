package io.github.tkaczenko.taskmanager.models;

/**
 * Created by tkaczenko on 26.10.16.
 */

public abstract class DictionaryObject {
    private int id;
    private String name;

    public DictionaryObject(int id, String name) {
        this.id = id;
        this.name = name;
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
