package com.alper.alperapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.HashMap;

public class AccountActivity extends AppCompatActivity {

    private Button mPostBtn;
    private TextView mAccountHeadTxtVw;
    private String userName;
    private DatabaseReference mDatabase;
    private ListView mJobList;
    private ArrayList<String> mJobs = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        mPostBtn = (Button) findViewById(R.id.acnt_new_job_btn);
        mAccountHeadTxtVw = (TextView) findViewById(R.id.acnt_head_txtvw);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("jobs");
        mJobList = (ListView) findViewById(R.id.job_list);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mJobs);
        mJobList.setAdapter(arrayAdapter);

        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String sourceAddress = dataSnapshot.child("SourceAddress").getValue(String.class);
                String destinationAddress = dataSnapshot.child("DestinatioAddress").getValue(String.class);
                String time = dataSnapshot.child("PickUpTime").getValue(String.class);
                String fare = dataSnapshot.child("ApproxFareAUD").getValue(String.class);
                String value = "From: " + sourceAddress + " : To : " + destinationAddress + "\nAt: " + time + " Aprox Fare AU$: " + fare;
                mJobs.add(value);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        Bundle extras = getIntent().getExtras();
        userName = extras.getString("UserName");
        if(extras!=null)
        {
            String headerText = "Welcome " + userName + "!\nCurrent job list!";
            mAccountHeadTxtVw.setText(headerText);
        }

        mPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent postNewJobIntent = new Intent(AccountActivity.this, PostJobActivity.class);
                postNewJobIntent.putExtra("UserName", userName);
                startActivity(postNewJobIntent);
                finish();
            }
        });

        mJobList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String value = (String)parent.getItemAtPosition(position);
                Intent acceptIntent = new Intent(AccountActivity.this, AcceptActivity.class);
                acceptIntent.putExtra("jobDetails", value);
                startActivity(acceptIntent);
                finish();
            }
        });
    }
}
