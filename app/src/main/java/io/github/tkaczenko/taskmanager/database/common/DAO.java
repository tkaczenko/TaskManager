package io.github.tkaczenko.taskmanager.database.common;

import java.util.List;

/**
 * Created by tkaczenko on 04.12.16.
 */

public interface DAO<E> {
    long save(E value);

    int update(E value);

    long remove(E value);

    List<E> getAll();
}
