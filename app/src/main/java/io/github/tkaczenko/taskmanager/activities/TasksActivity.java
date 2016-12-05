package io.github.tkaczenko.taskmanager.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import io.github.tkaczenko.taskmanager.R;
import io.github.tkaczenko.taskmanager.database.DatabaseContract;
import io.github.tkaczenko.taskmanager.database.DatabaseHelper;
import io.github.tkaczenko.taskmanager.database.models.dictionary.Department;
import io.github.tkaczenko.taskmanager.database.models.dictionary.Position;
import io.github.tkaczenko.taskmanager.database.models.dictionary.TaskSource;
import io.github.tkaczenko.taskmanager.database.models.dictionary.TaskType;
import io.github.tkaczenko.taskmanager.database.models.dictionary.common.Dictionary;
import io.github.tkaczenko.taskmanager.database.models.employee.Employee;
import io.github.tkaczenko.taskmanager.database.models.task.Task;
import io.github.tkaczenko.taskmanager.fragments.DictionaryFragment;
import io.github.tkaczenko.taskmanager.fragments.EmployeeFragment;
import io.github.tkaczenko.taskmanager.fragments.TaskFragment;
import io.github.tkaczenko.taskmanager.fragments.UpdateDictionaryFragment;
import io.github.tkaczenko.taskmanager.fragments.UpdateEmployeeFragment;
import io.github.tkaczenko.taskmanager.fragments.UpdateTaskFragment;
import io.github.tkaczenko.taskmanager.fragments.interfaces.OnObjectChangedListener;
import io.github.tkaczenko.taskmanager.fragments.interfaces.OnObjectSelectedListener;

public class TasksActivity extends AppCompatActivity
        implements OnObjectSelectedListener, OnObjectChangedListener {
    private DrawerLayout mDrawer;

    private SlidingUpPanelLayout mLayout;
    private Fragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        setUpViews();
        copyDatabase();

        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            mFragment = new TaskFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, mFragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tasks, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            mDrawer.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mLayout != null
                && (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED
                || mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onChangeObject() {
        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        if (mFragment != null && mFragment instanceof DictionaryFragment) {
            ((DictionaryFragment) mFragment).updateView();
        }
        if (mFragment != null && mFragment instanceof EmployeeFragment) {
            ((EmployeeFragment) mFragment).updateView();
        }
        if (mFragment != null && mFragment instanceof TaskFragment) {
            ((TaskFragment) mFragment).updateView();
        }
    }

    @Override
    public void onSelectObject(Object object) {
        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        Fragment fragment = null;
        Bundle args = new Bundle();
        if (object instanceof Task) {
            fragment = new UpdateTaskFragment();
            args.putParcelable("task", (Task) object);
            mLayout.setTouchEnabled(true);
        } else if (object instanceof Employee) {
            fragment = new UpdateEmployeeFragment();
            args.putParcelable("employee", (Employee) object);
            mLayout.setTouchEnabled(true);
        } else if (object instanceof Dictionary) {
            fragment = new UpdateDictionaryFragment();
            args.putParcelable("object", (Dictionary) object);
            mLayout.setTouchEnabled(false);
        }

        assert fragment != null;
        fragment.setArguments(args);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    private void setUpViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);

        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setHomeAsUpIndicator(R.mipmap.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        setupDrawerContent(navigationView);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    @SuppressWarnings("unchecked")
    private void selectDrawerItem(MenuItem menuItem) {
        DictionaryFragment temp;
        switch (menuItem.getItemId()) {
            case R.id.nav_tasks:
                if (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                }
                mFragment = new TaskFragment();
                break;
            case R.id.nav_employees:
                if (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                }
                mFragment = new EmployeeFragment();
                break;
            case R.id.nav_sub_positions:
                if (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                }
                temp = new DictionaryFragment<>();
                temp.setDictionaryClass(Position.class);
                mFragment = temp;
                break;
            case R.id.nav_sub_departments:
                if (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                }
                temp = new DictionaryFragment<>();
                temp.setDictionaryClass(Department.class);
                mFragment = temp;
                break;
            case R.id.nav_sub_task_types:
                if (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                }
                temp = new DictionaryFragment<>();
                temp.setDictionaryClass(TaskType.class);
                mFragment = temp;
                break;
            case R.id.nav_sub_task_sources:
                if (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                }
                temp = new DictionaryFragment<>();
                temp.setDictionaryClass(TaskSource.class);
                mFragment = temp;
                break;
            default:
                if (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                }
                temp = new DictionaryFragment<>();
                temp.setDictionaryClass(Position.class);
                mFragment = temp;
                break;
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, mFragment)
                .commit();

        menuItem.setChecked(false);
        setTitle(menuItem.getTitle());
        mDrawer.closeDrawers();
    }

    private void copyDatabase() {
        DatabaseHelper dbHelper = DatabaseHelper.getHelper(this);
        File database = getApplicationContext().getDatabasePath(DatabaseContract.DATABASE_NAME);
        if (!database.exists()) {
            dbHelper.getReadableDatabase();
            if (copyDatabase(this)) {
                Toast.makeText(this, R.string.copy_database_mess_success, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.copy_database_mess_error, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean copyDatabase(Context context) {
        try {
            InputStream inputStream = context.getAssets().open(DatabaseContract.DATABASE_NAME);
            String outFileName = DatabaseHelper.DB_LOCATION + DatabaseContract.DATABASE_NAME;
            OutputStream outputStream = new FileOutputStream(outFileName);
            byte[] buff = new byte[1024];
            int length;
            while ((length = inputStream.read(buff)) > 0) {
                outputStream.write(buff, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
