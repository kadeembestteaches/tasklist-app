package com.example.tasklistapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {


    private FirebaseAuth auth;
    private EditText txtEmail;
    private EditText txtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
    }

    public void loginAction(View view)
    {

        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);

        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();

        sigin(email,password);
    }

    public void registerAction(View view) {

        Intent registrationIntent = new Intent(this, RegisterActivity.class);
        startActivity(registrationIntent);
    }


    public void sigin(String email, String password)
    {

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("succesfulSigin", "signInWithEmail:success");
                            FirebaseUser user = auth.getCurrentUser();
                            redirect();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("unsuccessfulSigin", "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });

    }

    public void redirect()
    {
        Intent loginIntent = new Intent(this,DashboardActivity.class);
        startActivity(loginIntent);
    }
}
