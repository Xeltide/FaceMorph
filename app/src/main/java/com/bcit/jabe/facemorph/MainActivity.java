package com.bcit.jabe.facemorph;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        store = new ActivityStore();
        if (savedInstanceState != null) {
            store.loadFromBundle(savedInstanceState);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        setFragment(store.getLastItem());
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        //store.saveToBundle(bundle);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        if (item.getItemId() == R.id.nav_new) {
            recreate();
            int lastScreen = store.getLastItem();
            store = new ActivityStore();
            setFragment(lastScreen);
            store.setLastItem(lastScreen);
        } else if (item.getItemId() == R.id.nav_open) {
            ActivityStore saved = store.load(getFilesDir().getPath().toString() + "/test.mor");
            if (saved != null) {
                store = saved;
                setFragment(R.id.nav_choose_pictures);
            } else {
                android.util.Log.d("NULL STORE REF", "Failed to load store");
            }
        } else if (item.getItemId() == R.id.nav_save) {
            store.save(getFilesDir().getPath().toString() + "/test.mor");
            android.util.Log.d("SAVING", "Completed save");
        } else {
            store.setLastItem(item.getItemId());
            setFragment(store.getLastItem());
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setFragment(int id) {
        FragmentManager fragmentManager = getFragmentManager();

        if (id == R.id.nav_choose_pictures) {
            setPhotoPreviewFragment(fragmentManager);
        } else if (id == R.id.nav_edit_lines) {
            setPhotoEditFragment(fragmentManager);
        } else if (id == R.id.nav_face_morph) {
            setFaceMorphFragment(fragmentManager);
        }
    }

    private void setPhotoPreviewFragment(FragmentManager fragmentManager) {
        PhotoPreviewFragment fragment = new PhotoPreviewFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
    }

    private void setPhotoEditFragment(FragmentManager fragmentManager) {
        PhotoEditFragment fragment = new PhotoEditFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
    }

    private void setFaceMorphFragment(FragmentManager fragmentManager) {
        MorphFragment fragment = new MorphFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
    }

    public ActivityStore getStore() {
        return store;
    }

    private ActivityStore store;
}
