package com.alper.alperapp;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class PostJobActivity extends AppCompatActivity {

    private EditText mSourceAdrTxtEdt, mDestinAdrTxtEdt, mPickupTxtTime, mPickupTxtDate, mFareTxtCrnc, mNoteTxtMlti;
    private Button mPostJobBtn;
    private DatabaseReference mDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_job);

        mSourceAdrTxtEdt = (EditText) findViewById(R.id.source_txtedt);
        mDestinAdrTxtEdt = (EditText) findViewById(R.id.destin_txtedt);
        mPickupTxtTime = (EditText) findViewById(R.id.pickup_txttime);
        mPickupTxtDate = (EditText) findViewById(R.id.pickup_txtdate);
        mFareTxtCrnc = (EditText) findViewById(R.id.fare_txtcurrency);
        mNoteTxtMlti = (EditText) findViewById(R.id.note_txtmulti);
        mPostJobBtn = (Button) findViewById(R.id.post_job_btn);
        mDataBase = FirebaseDatabase.getInstance().getReference().child("jobs");

        mPostJobBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sourceAdrTxtEdt = mSourceAdrTxtEdt.getText().toString().trim();
                String destinAdrTxtEdt = mDestinAdrTxtEdt.getText().toString().trim();
                String pickupTxtTime = mPickupTxtTime.getText().toString().trim();
                String pickupTxtDate = mPickupTxtDate.getText().toString().trim();
                String fareTxtCrnc = mFareTxtCrnc.getText().toString().trim();
                String noteTxtMlti = mNoteTxtMlti.getText().toString().trim();

                post_job(sourceAdrTxtEdt, destinAdrTxtEdt, pickupTxtTime, pickupTxtDate, fareTxtCrnc, noteTxtMlti);
            }
        });
    }

    private void post_job(String sourceAdrTxtEdt, String destinAdrTxtEdt, String pickupTxtTime,
                          String pickupTxtDate, String fareTxtCrnc, String noteTxtMlti){

        Bundle extras = getIntent().getExtras();
        String userName = extras.getString("UserName");

        HashMap<String, String> dataFieldMap = new HashMap<String, String>();
        dataFieldMap.put("SourceAddress", sourceAdrTxtEdt);
        dataFieldMap.put("DestinatioAddress", destinAdrTxtEdt);
        dataFieldMap.put("PickUpTime", pickupTxtTime);
        dataFieldMap.put("PickUpDate", pickupTxtDate);
        dataFieldMap.put("ApproxFareAUD", fareTxtCrnc);
        dataFieldMap.put("Notes", noteTxtMlti);
        dataFieldMap.put("Status", "Initiated");
        dataFieldMap.put("OriginatedBy", userName);
        dataFieldMap.put("AssignedTo", "");

        mDataBase.push().setValue(dataFieldMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(PostJobActivity.this, "Posting New Job", Toast.LENGTH_LONG).show();
                    Intent previousIntent = new Intent(PostJobActivity.this, AccountActivity.class);
                    //startActivity(previousIntent);
                    setResult(Activity.RESULT_OK,previousIntent);
                    finish();
                }
            }
        });

    }
}
