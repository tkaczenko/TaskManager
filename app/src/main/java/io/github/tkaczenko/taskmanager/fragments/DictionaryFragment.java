package io.github.tkaczenko.taskmanager.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.github.tkaczenko.taskmanager.R;
import io.github.tkaczenko.taskmanager.activities.TasksActivity;
import io.github.tkaczenko.taskmanager.adapters.DictionaryAdapter;
import io.github.tkaczenko.taskmanager.database.models.dictionary.common.Dictionary;
import io.github.tkaczenko.taskmanager.database.models.dictionary.common.DictionaryDAO;
import io.github.tkaczenko.taskmanager.database.models.dictionary.common.DictionaryDAOImp;
import io.github.tkaczenko.taskmanager.dialogs.AddDictionaryObjectDialog;
import io.github.tkaczenko.taskmanager.fragments.interfaces.OnObjectSelectedListener;

/**
 * Created by tkaczenko on 02.11.16.
 */

public class DictionaryFragment<T extends Dictionary> extends Fragment {
    private RecyclerView mRecyclerView;
    private DictionaryAdapter mAdapter;
    private Activity mActivity;
    private GetDicTask mTask;

    private Class<T> dictionaryClass;
    private DictionaryDAO<T> dictionaryDAO;
    private List<T> mList;

    private OnObjectSelectedListener<Dictionary> mListener;
    private DictionaryAdapter.OnItemClickListener mItemClickListener =
            new DictionaryAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Dictionary item) {
                    mListener.onSelectObject(item);
                }
            };

    @Override
    @SuppressWarnings("unchecked")
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnObjectSelectedListener<Dictionary>) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement " +
                    "OnDictionaryObjectSelected");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        dictionaryDAO = new DictionaryDAOImp<>(mActivity, dictionaryClass);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dictionary, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.rvDictionary);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(manager);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(mItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        mTask = new GetDicTask(mActivity);
        mTask.execute((Void) null);

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(mSearchListener);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                showAddDialog();
                break;
        }
        return true;
    }

    public void updateView() {
        mTask = new GetDicTask(mActivity);
        mTask.execute((Void) null);
    }

    public void setDictionaryClass(Class<T> type) {
        this.dictionaryClass = type;
    }

    public class GetDicTask extends AsyncTask<Void, Void, List<T>> {
        private final WeakReference<Activity> activityWeakRef;

        GetDicTask(Activity context) {
            this.activityWeakRef = new WeakReference<>(context);
        }

        @Override
        protected List<T> doInBackground(Void... arg0) {
            return dictionaryDAO.getAll();
        }

        @Override
        @SuppressWarnings("unchecked")
        protected void onPostExecute(List<T> dicList) {
            if (activityWeakRef.get() != null
                    && !activityWeakRef.get().isFinishing()) {
                mList = dicList;
                if (dicList != null) {
                    if (dicList.size() != 0) {
                        mAdapter = new DictionaryAdapter(
                                (List<Dictionary>) dicList, mItemClickListener
                        );
                        mRecyclerView.setAdapter(mAdapter);
                    } else {
                        Toast.makeText(mActivity, getString(R.string.no) +
                                        dictionaryClass.getSimpleName() + getString(R.string.records),
                                Toast.LENGTH_LONG)
                                .show();
                    }
                }
            }
        }
    }

    private void showAddDialog() {
        AddDictionaryObjectDialog<T> fragment = new AddDictionaryObjectDialog<>();
        fragment.setClazz(dictionaryClass);
        fragment.show(getActivity().getSupportFragmentManager(), "add_to_dictionary");
    }

    private ItemTouchHelper.Callback mItemTouchCallback = new ItemTouchHelper.Callback() {
        private Drawable background;
        private Drawable xMark;
        private int xMarkMargin;
        private boolean initiated;

        private void init() {
            background = new ColorDrawable(Color.RED);
            xMark = ContextCompat.getDrawable(getActivity(), android.R.drawable.ic_delete);
            xMark.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
            xMarkMargin = 4;
            initiated = true;
        }


        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int dragFlags = 0;
            int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
            return makeMovementFlags(dragFlags, swipeFlags);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            View itemView = viewHolder.itemView;
            if (viewHolder.getAdapterPosition() == -1) {
                return;
            }

            if (!initiated) {
                init();
            }

            // draw red background
            background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
            background.draw(c);

            // draw x mark
            int itemHeight = itemView.getBottom() - itemView.getTop();
            int intrinsicWidth = xMark.getIntrinsicWidth();
            int intrinsicHeight = xMark.getIntrinsicWidth();

            int xMarkLeft = itemView.getRight() - xMarkMargin - intrinsicWidth;
            int xMarkRight = itemView.getRight() - xMarkMargin;
            int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
            int xMarkBottom = xMarkTop + intrinsicHeight;
            xMark.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);

            xMark.draw(c);
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }

        @Override
        @SuppressWarnings("unchecked")
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            long result = dictionaryDAO.remove((T) mAdapter.getItem(position));
            if (result > 0) {
                TasksActivity activity = (TasksActivity) getActivity();
                activity.onChangeObject();
            }
        }
    };

    private SearchView.OnQueryTextListener mSearchListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        @SuppressWarnings("unchecked")
        public boolean onQueryTextChange(String newText) {
            SlidingUpPanelLayout layout = (SlidingUpPanelLayout)
                    getActivity().findViewById(R.id.sliding_layout);
            layout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
            newText = newText.toLowerCase();
            List<T> filteredList = new ArrayList<>();
            for (int i = 0; i < mList.size(); i++) {
                String name = mList.get(i).getName();
                if (name != null && name.toLowerCase().contains(newText)) {
                    filteredList.add(mList.get(i));
                }
            }
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mAdapter = new DictionaryAdapter(
                    (List<Dictionary>) filteredList, mItemClickListener
            );
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
            return true;
        }
    };
}
