package com.votum.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Intent intent = new Intent(getActivity(), HomeActivity.class);
        //getActivity().startActivity(intent);

        View view = inflater.inflate(R.layout.home, container, false);

        //UI
        Spinner form_selector = view.findViewById(R.id.form_selector);

        //Form Selector
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(), R.array.Form, R.layout.form_selector);
        adapter.setDropDownViewResource(R.layout.form_selector);
        form_selector.setAdapter(adapter);
        //form_selector.getOnItemSelectedListener(this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("myapp", "HomeActivity start");


    }

    //Spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String form = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
