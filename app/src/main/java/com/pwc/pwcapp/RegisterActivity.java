package com.pwc.pwcapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText uId,uPwd,uVpwd;
    private FirebaseAuth mAuth;
    FirebaseFirestore fb;
    private Button reg_Button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        fb = FirebaseFirestore.getInstance();

        createUI();

        reg_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!uPwd.getText().toString().isEmpty()&&!uId.getText().toString().isEmpty()&&!uVpwd.getText().toString().isEmpty()) {

                    if (uPwd.getText().toString().equals(uVpwd.getText().toString())) {
                        mAuth.createUserWithEmailAndPassword(uId.getText().toString(), uPwd.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {

                                        if (task.isSuccessful()) {

                                            DocumentReference mRef = fb.collection("users").document(mAuth.getCurrentUser().getUid());
                                            Map<String, Object> uType = new HashMap<>();
                                            uType.put("type","user");
                                            mRef.set(uType);

                                            finish();
                                        } else
                                            Toast.makeText(RegisterActivity.this, "Error in creating user", Toast.LENGTH_LONG).show();

                                    }
                                });
                    }
                } else
                    Toast.makeText(RegisterActivity.this,"Check all fields.",Toast.LENGTH_LONG).show();

            }
        });

    }

    void createUI(){

        uId = findViewById(R.id.reg_email);
        uPwd = findViewById(R.id.reg_pass);
        uVpwd = findViewById(R.id.reg_pass);
        reg_Button = findViewById(R.id.reg_btn);

    }
}
