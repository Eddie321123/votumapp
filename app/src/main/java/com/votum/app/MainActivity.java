package com.votum.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "MainActivity";

    //vars
    private ArrayList<String> mInformation = new ArrayList<>();
    private ArrayList<String> mTime = new ArrayList<>();
    private ArrayList<String> mTitle = new ArrayList<>();
    //Day
    private String Day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //UI
        final Button dayButton = findViewById(R.id.Day);
        Spinner form_selector = findViewById(R.id.form_selector);


        //Form Selector
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Form, R.layout.form_selector);
        adapter.setDropDownViewResource(R.layout.form_selector);
        form_selector.setAdapter(adapter);
        //form_selector.getOnItemSelectedListener(this);


        //Show Day by Firebase
        DatabaseReference Dayref = FirebaseDatabase.getInstance().getReferenceFromUrl("https://thebruh-b412b-default-rtdb.firebaseio.com/12G8BlfCZXw5zqSYBt91vTvpW9yK27Ks_0xTnan7gCxg/Day");
        Dayref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String finalDay = dataSnapshot.child("1").child("Today").getValue().toString();
                Log.d("Firebase Database", finalDay);
                dayButton.setText(finalDay);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

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

    //Spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String form = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    class MyFailureListener implements OnFailureListener {
        @Override
        public void onFailure(@NonNull Exception exception) {
            int errorCode = ((DatabaseException) exception.getMessage();
            String errorMessage = exception.getMessage();
            // test the errorCode and errorMessage, and handle accordingly
        }
    }
}