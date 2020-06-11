package com.example.tasklistapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DashboardActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private EditText txtTitle;
    private ListView taskListView;
    private ArrayList<String > tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        auth = FirebaseAuth.getInstance();
        db =  FirebaseFirestore.getInstance();

        loadTasks();

    }

    public void addTaskAction(View view) {

    txtTitle = findViewById(R.id.txtTaskTitle);

    String title = txtTitle.getText().toString();

// Create a new user with a first and last name
        Map<String, Object> task = new HashMap<>();
        task.put("title", title);
       task.put("description", "Dummy data");
        task.put("priority", "high");

// Add a new document with a generated ID
        db.collection("tasks")
                .add(task)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("addedSuccessfully", "DocumentSnapshot added with ID: " + documentReference.getId());

                        Toast.makeText(DashboardActivity.this, "Added to the database.",
                                Toast.LENGTH_SHORT).show();

                        loadTasks();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("failed", "Error adding document", e);
                    }
                });    }


    public void logoutAction(View view) {

        auth.signOut();
        Intent logoutIntent  = new Intent(this, MainActivity.class);
        startActivity(logoutIntent);
    }

    public void loadTasks()
    {
        taskListView = findViewById(R.id.taskListView);

        tasks = new ArrayList<>();

        final ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,tasks);

        taskListView.setAdapter(adapter);

        db.collection("tasks")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("successfulPUllOfData", document.getId() + " => " + document.getData());

                               tasks.add(document.getData().get("title").toString());

                            }

                            adapter.notifyDataSetChanged();
                        } else {
                            Log.w("errorPulling", "Error getting documents.", task.getException());
                        }
                    }
                });





    }
}
