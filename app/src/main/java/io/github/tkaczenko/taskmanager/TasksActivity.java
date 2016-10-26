package io.github.tkaczenko.taskmanager;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import io.github.tkaczenko.taskmanager.database.DatabaseHelper;
import io.github.tkaczenko.taskmanager.fragments.DictionaryFragments;
import io.github.tkaczenko.taskmanager.models.Department;
import io.github.tkaczenko.taskmanager.models.Position;
import io.github.tkaczenko.taskmanager.models.TaskSource;
import io.github.tkaczenko.taskmanager.models.TaskType;

public class TasksActivity extends AppCompatActivity {
    private DrawerLayout mDrawer;
    private DatabaseHelper mDBHelper;
    private SlidingUpPanelLayout mLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.mipmap.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        mDBHelper = new DatabaseHelper(this);
        File database = getApplicationContext().getDatabasePath(DatabaseHelper.DBNAME);
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
            DictionaryFragments fragment = new DictionaryFragments();

            Bundle args = new Bundle();
            List<Position> positions = mDBHelper.getListPosition();
            args.putParcelableArrayList("list", (ArrayList) positions);
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragment).commit();
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
        Fragment fragment = null;
        Class fragmentClass;
        Bundle args = new Bundle();
        switch (menuItem.getItemId()) {
            case R.id.nav_sub_positions:
                fragmentClass = DictionaryFragments.class;
                List<Position> positions = mDBHelper.getListPosition();
                args.putParcelableArrayList("list", (ArrayList) positions);
                break;
            case R.id.nav_sub_departments:
                fragmentClass = DictionaryFragments.class;
                List<Department> departments = mDBHelper.getListDepartments();
                args.putParcelableArrayList("list", (ArrayList) departments);
                break;
            case R.id.nav_sub_task_types:
                fragmentClass = DictionaryFragments.class;
                List<TaskType> taskTypes = mDBHelper.getListTaskTypes();
                args.putParcelableArrayList("list", (ArrayList) taskTypes);
                break;
            case R.id.nav_sub_task_sources:
                fragmentClass = DictionaryFragments.class;
                List<TaskSource> taskSources = mDBHelper.getListTaskSources();
                args.putParcelableArrayList("list", (ArrayList) taskSources);
                break;
            default:
                fragmentClass = DictionaryFragments.class;
                List<Position> pos = mDBHelper.getListPosition();
                args.putParcelableArrayList("list", (ArrayList) pos);
                break;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        fragment.setArguments(args);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();

        menuItem.setChecked(false);
        setTitle(menuItem.getTitle());
        mDrawer.closeDrawers();
    }

    private boolean copyDatabase(Context context) {
        try {
            InputStream inputStream = context.getAssets().open(DatabaseHelper.DBNAME);
            String outFileName = DatabaseHelper.DBLOCATION + DatabaseHelper.DBNAME;
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
}
