package com.pwc.pwcapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore firebaseFirestore;

    private EditText enteredID, enteredPwd;
    private Button loginBtn, regiterBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        currentUser = mAuth.getCurrentUser();

        createUI();
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!enteredID.getText().toString().isEmpty()&&!enteredPwd.getText().toString().isEmpty()){

                    String userId = enteredID.getText().toString();
                    String userPwd = enteredPwd.getText().toString();
                    mAuth.signInWithEmailAndPassword(userId,userPwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()){
                                currentUser = FirebaseAuth.getInstance().getCurrentUser();
                                firebaseFirestore.collection("users").document(currentUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        DocumentSnapshot doc = task.getResult();
                                        String type = doc.get("type").toString();
                                        if (type.equals("user")){
                                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }else{
                                            Intent intent = new Intent(LoginActivity.this,AdminActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                });
                            }
                            else Toast.makeText(LoginActivity.this,"Error Logging in.",Toast.LENGTH_SHORT).show();

                        }
                    });



                }else{
                    Toast.makeText(LoginActivity.this,"Please enter all fields.",Toast.LENGTH_LONG).show();
                }

            }
        });

        regiterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                finish();

            }
        });

    }


    private void createUI(){
        enteredID = findViewById(R.id.loginID);
        enteredPwd = findViewById(R.id.loginPWD);
        loginBtn = findViewById(R.id.loginBtn);
        regiterBtn = findViewById(R.id.registerBtn);
    }


}
