package com.pwc.pwcapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    private RecyclerView complaint_list;
    private FloatingActionButton addComplaintBtn;
    private List<Complaint> complaints;
    private CustomAdapter adapter;
    FirebaseFirestore mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        if(firebaseUser==null){

            startActivity(new Intent(MainActivity.this,LoginActivity.class));
            finish();
        }
        getSupportActionBar().setTitle("Home - Complaints");

        createUI();
        complaints = new ArrayList<>();
        complaint_list.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CustomAdapter(complaints);
        complaint_list.setAdapter(adapter);

        mRef = FirebaseFirestore.getInstance();
        mRef.collection("complaints").document(firebaseUser.getUid()).collection("uComplaints").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                for (DocumentChange doc:queryDocumentSnapshots.getDocumentChanges()){

                    if (doc.getType() == DocumentChange.Type.ADDED){

                        Complaint complaint = doc.getDocument().toObject(Complaint.class);
                        complaints.add(0,complaint);
                        adapter.notifyDataSetChanged();
                    }

                }

            }
        });

    }

    private void createUI() {

        complaint_list = findViewById(R.id.complaint_list);
        addComplaintBtn = findViewById(R.id.addComplaintBtn);
        addComplaintBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this,AddComplaintActivity.class));

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar_menu, menu);

        if (menu instanceof MenuBuilder) {
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        logOut();

        return true;

    }

    private void logOut() {
        Intent intent = new Intent(this, LoginActivity.class);
        mAuth.signOut();
        startActivity(intent);
        this.finish();
    }

    public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder>{

        private List<Complaint> complaint_list;

        public CustomAdapter(List<Complaint> complaint_list){
            this.complaint_list = complaint_list;
        }

        @NonNull
        @Override
        public CustomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.complaint_item, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CustomAdapter.MyViewHolder holder, int position) {
            holder.complaint_id.setText("Reference #: "+complaint_list.get(position).getComplaintId());
            holder.complaint_status.setText("Status: "+complaint_list.get(position).getStatus());
            holder.complaint_category.setText(complaint_list.get(position).getCategory());
            holder.complaint_date.setText("Submitted On: "+complaint_list.get(position).getTimestamp());
            holder.complaint_desc.setText(complaint_list.get(position).getDescription());

            switch (complaint_list.get(position).getStatus()){
                case "Pending":
                    holder.complaint_status_logo.setImageDrawable(getResources().getDrawable(R.drawable.pending));
                    break;
                case "Resolved":
                    holder.complaint_status_logo.setImageDrawable(getResources().getDrawable(R.drawable.check));
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return complaint_list.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder{

            TextView complaint_id,complaint_date,complaint_status,complaint_desc,complaint_category;
            ImageView complaint_status_logo;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                complaint_id = itemView.findViewById(R.id.complaint_id);
                complaint_date = itemView.findViewById(R.id.complaint_date);
                complaint_desc = itemView.findViewById(R.id.complaint_deSc);
                complaint_status = itemView.findViewById(R.id.complaint_status);
                complaint_category = itemView.findViewById(R.id.complaint_category);
                complaint_status_logo = itemView.findViewById(R.id.complaint_logo);
            }
        }
    }
}
