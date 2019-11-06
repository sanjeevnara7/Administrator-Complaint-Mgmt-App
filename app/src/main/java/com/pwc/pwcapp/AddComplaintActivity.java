package com.pwc.pwcapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class AddComplaintActivity extends AppCompatActivity {

    private String entered_category;
    private EditText entered_desc;
    private TextView complaint_ref;
    private Spinner category_Selector;

    private Button addNewComplaintBtn;
    private String newComplaintRefId;

    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore fb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_complaint);
        getSupportActionBar().setTitle("Add Complaint");

        createUI();
        fb = FirebaseFirestore.getInstance();
        final DocumentReference newComplaintRef = fb.collection("complaints").document(firebaseUser.getUid()).collection("uComplaints").document();
        final DocumentReference adminComplaintRef = fb.collection("complaints").document("ZHNx24Hwhvbwx3HrzTMJj4lVLg72").collection("uComplaints").document();
        newComplaintRefId = newComplaintRef.getId();
        complaint_ref.setText(newComplaintRefId);

        addNewComplaintBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!entered_category.isEmpty()&&!entered_desc.getText().toString().isEmpty()){

                    Complaint newComplaint = new Complaint(newComplaintRefId,firebaseUser.getEmail(),entered_desc.getText().toString(),"Pending",entered_category,getCurrentDate(),false,firebaseUser.getUid());
                    newComplaintRef.set(newComplaint);
                    adminComplaintRef.set(newComplaint);
                    AddComplaintActivity.this.finish();

                }else{
                    Toast.makeText(AddComplaintActivity.this,"Please enter all fields.",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void createUI() {

        category_Selector = findViewById(R.id.complaint_category_selector);
        entered_desc = findViewById(R.id.entered_complaint_desc);
        complaint_ref = findViewById(R.id.complaint_ref);

        addNewComplaintBtn = findViewById(R.id.addNewComplaintBtn);
        final String[] categories = {"General","Flight Cancellation","Refund","Rescheduling","Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, categories);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        category_Selector.setAdapter(adapter);
        category_Selector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(AddComplaintActivity.this,"Selected Category: "+categories[position],Toast.LENGTH_SHORT).show();
                entered_category = categories[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy 'at' HH:mm a");
        dateFormat.setTimeZone(TimeZone.getDefault());
        Date today = Calendar.getInstance().getTime();
        return dateFormat.format(today);
    }
}
