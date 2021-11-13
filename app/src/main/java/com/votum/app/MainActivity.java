package com.votum.app;

import android.content.BroadcastReceiver;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.text.style.UpdateAppearance;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.messaging.FirebaseMessaging;

import java.time.temporal.ValueRange;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private static final String TAG = "MainActivity";

    //vars
    private ArrayList<String> mInformation = new ArrayList<>();
    private ArrayList<String> mTime = new ArrayList<>();
    private ArrayList<String> mTitle = new ArrayList<>();

    //UI
    //final Button dayButton = findViewById(R.id.Day);

    //Database
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("myapp", "MainActivity start");

        //Navigation Manager
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            Log.d("LOl", "no saved instance");
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container,
                            new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.home);
        }

        //Firebase Token
        String newToken = FirebaseMessaging.getInstance().getToken().toString();
        Log.d("Firebase", newToken);

        //Load Database
        DB = new DBHelper(this);
        Cursor res = DB.getdata();
        while (res.moveToNext()) {
            mTime.add(res.getString(0));
            mInformation.add(res.getString(1));
            mTitle.add(res.getString(2));
        }

    }

    //Navigation Menu
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,
                        new HomeFragment()).commit();
                break;

            case R.id.welfare:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,
                                new WelfareFragment()).commit();
                break;
            case R.id.settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onBackPressed() {

        Log.d("myapp", "BackPressed");

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //MIGRATED FROM HOMEACTIVITY **START**

    //Notifications processor
    public void initNotifications(String message, String time, String title) {
        mInformation.add(message);
        mTime.add(time);
        mTitle.add(title);

        initRecyclerView();
    }

    public void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init InformationRecyclerView");
        RecyclerView recyclerView = findViewById(R.id.notificationRecyclerView);
        NotificationRecyclerViewAdapter adapter = new NotificationRecyclerViewAdapter(mInformation, mTime, mTitle);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    //receiver from MyFirebaseMessagingService
    public BroadcastReceiver myReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = intent.getStringExtra("msg");
            String time = intent.getStringExtra("time");
            String title = intent.getStringExtra("title");
            initNotifications(msg, time, title);
            UpdateDatabase(msg, time, title);
        }

        private void UpdateDatabase(String msg, String time, String title) {
            DB = new DBHelper(MainActivity.this);
            DB.insertuserdata(msg, time, title);

        }
    };

    //Firebase
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(myReceiver, new IntentFilter("Cloud Message"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(myReceiver);
    }

    //MIGRATED FROM HOMEACTIVITY **END**
}
