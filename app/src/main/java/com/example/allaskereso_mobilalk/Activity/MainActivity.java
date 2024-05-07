package com.example.allaskereso_mobilalk.Activity;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.allaskereso_mobilalk.Models.Offers;
import com.example.allaskereso_mobilalk.Models.jobApp;
import com.example.allaskereso_mobilalk.R;
import com.example.allaskereso_mobilalk.Recyclerview.OffersAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private final static String LOG_TAG = MainActivity.class.getName();

    private ArrayList<Offers> offers;
    private OffersAdapter offersAdapter;
    private FirebaseFirestore firestore;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        if (FirebaseAuth.getInstance().getCurrentUser() == null)
            finish();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? 1 : 2));

        offers = new ArrayList<>();

        offersAdapter = new OffersAdapter(this, offers);
        recyclerView.setAdapter(offersAdapter);

        firestore = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        selectOffers();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void selectOffers()
    {
        offers.clear();
        ArrayList<String> myApplications = new ArrayList<>();
        firestore.collection("AppliedJobs").whereEqualTo("seekerID", user.getUid()).get().addOnSuccessListener(
                documents -> documents.forEach(
                        doc ->  myApplications.add(((String) Objects.requireNonNull(doc.get("jobID"))).trim())
                )
        ).addOnSuccessListener(None -> firestore.collection("JobOffers").orderBy("name").get().addOnSuccessListener(queryDocumentSnapshots ->
        {
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                String name = (String) document.get("name");
                String description = (String) document.get("description");
                String employerId = (String) document.get("empId");
                boolean applied = false;
                for (String s: myApplications) {
                    if (document.getId().equals(s)) {
                        applied = true;
                        break;
                    }
                }
                assert employerId != null;
                boolean finalApplied = applied;
                firestore.collection("Employers").document(employerId).get().addOnSuccessListener(
                        doc -> {
                            String employerName = (String) doc.get("company");
                            Offers offer = new Offers(document.getId(), name, employerName, description, finalApplied);
                            offers.add(offer);
                            offersAdapter.notifyDataSetChanged();
                        }
                );
            }
        }));
    }

    public void delete(Offers application) {
        firestore.collection("AppliedJobs")
                .whereEqualTo("seekerID", user.getUid())
                .whereEqualTo("jobID", application.getId())
                .get()
                .addOnSuccessListener(documents -> {
                    for (QueryDocumentSnapshot doc : documents) {
                        doc.getReference().delete();
                    }
                    selectOffers();
                    Toast.makeText(this, "Jelentkezés törölve!", Toast.LENGTH_LONG).show();
                })
                .addOnFailureListener(exception -> {
                    Toast.makeText(this, "Hiba a jelentkezés törlésekor!", Toast.LENGTH_LONG).show();
                });
    }

    public void add(Offers application) {
        jobApp newApplication = new jobApp(application.getId(), user.getUid());

        firestore.collection("AppliedJobs").add(newApplication)
                .addOnSuccessListener(documentReference -> {
                    selectOffers();
                    Toast.makeText(this, "Jelentkezés hozzáadva!", Toast.LENGTH_LONG).show();
                    Log.d(LOG_TAG, "Application added with ID: " + documentReference.getId());
                })
                .addOnFailureListener(exception -> {
                    Log.w(LOG_TAG, "Error adding document: ", exception);
                    Toast.makeText(this, "Hiba a jelentkezés hozzáadásakor!", Toast.LENGTH_LONG).show();
                });
    }
}