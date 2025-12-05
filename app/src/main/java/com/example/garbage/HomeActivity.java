package com.example.garbage;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    EditText locationInput, descriptionInput;
    Button submitReportBtn;

    FirebaseFirestore db;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        locationInput = findViewById(R.id.locationInput);
        descriptionInput = findViewById(R.id.descriptionInput);
        submitReportBtn = findViewById(R.id.submitReportBtn);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        submitReportBtn.setOnClickListener(v -> {
            String location = locationInput.getText().toString().trim();
            String description = descriptionInput.getText().toString().trim();

            if (location.isEmpty() || description.isEmpty()) {
                Toast.makeText(HomeActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create report map
            Map<String, Object> report = new HashMap<>();
            report.put("location", location);
            report.put("description", description);
            report.put("userEmail", mAuth.getCurrentUser().getEmail());

            // Save to Firestore
            db.collection("garbageReports")
                    .add(report)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(HomeActivity.this, "Report submitted successfully!", Toast.LENGTH_SHORT).show();
                        locationInput.setText("");
                        descriptionInput.setText("");
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(HomeActivity.this, "Failed to submit report: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                    );
        });
    }
}
