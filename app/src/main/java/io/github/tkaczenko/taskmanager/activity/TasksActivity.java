package io.github.tkaczenko.taskmanager.activity;

import android.content.Context;
import android.os.Bundle;
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
import io.github.tkaczenko.taskmanager.database.model.Employee;
import io.github.tkaczenko.taskmanager.database.model.Task;
import io.github.tkaczenko.taskmanager.database.model.dictionary.Department;
import io.github.tkaczenko.taskmanager.database.model.dictionary.DictionaryObject;
import io.github.tkaczenko.taskmanager.database.model.dictionary.Position;
import io.github.tkaczenko.taskmanager.database.model.dictionary.TaskSource;
import io.github.tkaczenko.taskmanager.database.model.dictionary.TaskType;
import io.github.tkaczenko.taskmanager.fragment.DictionaryFragment;
import io.github.tkaczenko.taskmanager.fragment.EmployeeFragment;
import io.github.tkaczenko.taskmanager.fragment.TaskFragment;
import io.github.tkaczenko.taskmanager.fragment.UpdateDictionaryFragment;
import io.github.tkaczenko.taskmanager.fragment.UpdateEmpFragment;

//// TODO: 24.11.16 Implement insert for all tables
//// TODO: 24.11.16 Implement update DictionaryDAO
public class TasksActivity extends AppCompatActivity
        implements DictionaryFragment.OnDictionaryObjectSelectedListener,
        EmployeeFragment.OnEmployeeSelectedListener, TaskFragment.OnTaskSelectedListener,
        UpdateDictionaryFragment.OnDictionaryChangedListener, UpdateEmpFragment.OnEmployeeChangedListener {
    private DrawerLayout mDrawer;
    private DatabaseHelper mDBHelper;
    private SlidingUpPanelLayout mLayout;
    private Fragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.mipmap.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        mDBHelper = new DatabaseHelper(this);
        File database = getApplicationContext().getDatabasePath(DatabaseContract.DATABASE_NAME);
        if (false == database.exists()) {
            mDBHelper.getReadableDatabase();
            if (copyDatabase(this)) {
                Toast.makeText(this, "Copy database success", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Copy data error", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            Fragment fragment = new TaskFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
        }

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        setupDrawerContent(navigationView);
    }

    @Override
    public void onBackPressed() {
        if (mLayout != null &&
                (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED
                        || mLayout.getPanelState() ==
                        SlidingUpPanelLayout.PanelState.ANCHORED)) {

            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);

        } else {
            super.onBackPressed();
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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }

                });
    }

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
                temp.setDictionaryObjectClass(Position.class);
                mFragment = temp;
                break;
            case R.id.nav_sub_departments:
                if (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                }
                temp = new DictionaryFragment<>();
                temp.setDictionaryObjectClass(Department.class);
                mFragment = temp;
                break;
            case R.id.nav_sub_task_types:
                if (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                }
                temp = new DictionaryFragment<>();
                temp.setDictionaryObjectClass(TaskType.class);
                mFragment = temp;
                break;
            case R.id.nav_sub_task_sources:
                if (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                }
                temp = new DictionaryFragment<>();
                temp.setDictionaryObjectClass(TaskSource.class);
                mFragment = temp;
                break;
            default:
                if (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                }
                temp = new DictionaryFragment<>();
                temp.setDictionaryObjectClass(Position.class);
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

    private boolean copyDatabase(Context context) {
        try {
            InputStream inputStream = context.getAssets().open(DatabaseContract.DATABASE_NAME);
            String outFileName = DatabaseHelper.DBLOCATION + DatabaseContract.DATABASE_NAME;
            OutputStream outputStream = new FileOutputStream(outFileName);
            byte[] buff = new byte[1024];
            int length = 0;
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

    @Override
    public void onDictionaryObjectSelected(DictionaryObject object) {
        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        Fragment fragment = null;
        Bundle args = new Bundle();
        try {
            fragment = UpdateDictionaryFragment.class.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        args.putParcelable("object", object);
        fragment.setArguments(args);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
        mLayout.setTouchEnabled(false);
    }


    @Override
    public void onEmployeeSelected(Employee employee) {
        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        Fragment fragment = null;
        Bundle args = new Bundle();
        try {
            fragment = UpdateEmpFragment.class.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        args.putParcelable("employee", employee);
        fragment.setArguments(args);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
        mLayout.setTouchEnabled(true);
    }

    @Override
    public void onTaskSelected(Task task) {
        //// TODO: 19.11.16 Implement update Task

    }

    @Override
    public void onChangeDictionary() {
        if (mFragment != null && mFragment instanceof DictionaryFragment) {
            ((DictionaryFragment) mFragment).updateView();
        }
    }

    @Override
    public void onChangeEmployee() {
        if (mFragment != null && mFragment instanceof EmployeeFragment) {
            ((EmployeeFragment) mFragment).updateView();
        }
    }
}
