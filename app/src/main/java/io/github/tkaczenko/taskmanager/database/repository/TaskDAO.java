package io.github.tkaczenko.taskmanager.database.repository;

import android.content.ContentValues;
import android.content.Context;

import java.util.Arrays;
import java.util.List;

import io.github.tkaczenko.taskmanager.database.model.Task;

/**
 * Created by tkaczenko on 19.11.16.
 */
//// TODO: 19.11.16 Implement DAO
public class TaskDAO extends DAO<Task> {
    public TaskDAO(Context mContext) {
        super(mContext);
    }

    @Override
    public long save(Task value, Integer... ids) {
        ContentValues values = new ContentValues();

        List<Integer> ints = Arrays.asList(ids);

        return 0;
    }

    @Override
    public int update(Task value) {
        return 0;
    }

    @Override
    public long remove(Task value) {
        return 0;
    }

    @Override
    public List<Task> getAll() {
        return null;
    }
}
