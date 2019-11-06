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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class AdminActivity extends AppCompatActivity implements OnComplaintListener{

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firebaseFirestore, mRef;
    private RecyclerView adminRecycler;
    private List<Complaint> complaints;
    private CustomAdapter adapter;
    private List<String> users = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        getSupportActionBar().setTitle("Admin - Complaints");
        adminRecycler = findViewById(R.id.admin_list);
        adminRecycler.setLayoutManager(new LinearLayoutManager(this));
        complaints = new ArrayList<>();
        adapter = new CustomAdapter(complaints,this);
        adminRecycler.setAdapter(adapter);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("complaints").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("uComplaints").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (DocumentChange doc: queryDocumentSnapshots.getDocumentChanges()){

                    if (doc.getType() == DocumentChange.Type.ADDED){

                        Complaint complaint = doc.getDocument().toObject(Complaint.class);
                        complaints.add(0,complaint);
                        adapter.notifyDataSetChanged();

                    }

                }
            }
        });

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar_menu, menu);

        if (menu instanceof MenuBuilder) {
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }
        return super.onCreateOptionsMenu(menu);

    }
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

    @Override
    public void onComplaintClick(int position) {

        Complaint complaint = complaints.get(position);

    }

    public class CustomAdapter extends RecyclerView.Adapter<AdminActivity.CustomAdapter.MyViewHolder>{

        private List<Complaint> complaint_list;
        private OnComplaintListener onComplaintListener;

        public CustomAdapter(List<Complaint> complaint_list, OnComplaintListener onComplaintListener){
            this.onComplaintListener = onComplaintListener;
            this.complaint_list = complaint_list;
        }

        @NonNull
        @Override
        public AdminActivity.CustomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.admin_item, parent, false);
            return new AdminActivity.CustomAdapter.MyViewHolder(view, onComplaintListener);
        }

        @Override
        public void onBindViewHolder(@NonNull AdminActivity.CustomAdapter.MyViewHolder holder, int position) {
            holder.complaint_id.setText("Reference #: "+complaint_list.get(position).getComplaintId());
            holder.complaint_status.setText("Status: "+complaint_list.get(position).getStatus());
            holder.complaint_category.setText(complaint_list.get(position).getCategory());
            holder.complaint_desc.setText(complaint_list.get(position).getDescription());
            holder.complaint_uid.setText("Submitted by: "+complaint_list.get(position).getUserId());

        }

        @Override
        public int getItemCount() {
            return complaint_list.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder{

            OnComplaintListener onComplaintListener;

            TextView complaint_id,complaint_status,complaint_desc,complaint_category,complaint_uid;

            public MyViewHolder(@NonNull View itemView, OnComplaintListener onComplaintListener) {
                super(itemView);
                complaint_id = itemView.findViewById(R.id.admin_ref);
                complaint_desc = itemView.findViewById(R.id.admin_dec);
                complaint_status = itemView.findViewById(R.id.admin_status);
                complaint_category = itemView.findViewById(R.id.admin_cat);
                complaint_uid = itemView.findViewById(R.id.admin_userid);
            }

            public void onClick(View v){
                onComplaintListener.onComplaintClick(getAdapterPosition());
            }
        }
    }
}
