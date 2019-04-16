package com.example.ninemenout;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserProfileActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference userRef = db.collection("users");
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private int userpoints = 0;
    private String uname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        TextView userBalance = findViewById(R.id.accountBalance);
        TextView userName = findViewById(R.id.userName);
        TextView userEmail = findViewById(R.id.userEmail);
        String name = user.getEmail();
        if (name != null) {
            DocumentReference docRef = userRef.document(name);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // update home page display to represent user data
                            userpoints = ((Long) document.get("points")).intValue();
                            uname = (String) document.get("name");
                            userBalance.setText(((Integer) userpoints).toString());
                            userEmail.setText(name);
                            userName.setText(uname);
                        } else {
                            Log.d("document error", "No such document");
                        }
                    } else {
                        Log.d("task error", "get failed with ", task.getException());
                    }
                }
            });
        }

    }

    public void openAddFriendsActivity(View view) {
        Intent intent = new Intent(this, AddFriendsActivity.class);
        startActivity(intent);
    }

     public void openUserLoginActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    public void openViewRequestsActivity(View view) {
        Intent intent = new Intent(this, ViewRequestsActivity.class);
        startActivity(intent);
    }

    public void openPersonalBetActivity(View view) {
        Intent intent = new Intent(this, PersonalBetActivity.class);
        startActivity(intent);
    }


    public void logout(View view)
        {
        FirebaseAuth.getInstance().signOut();
        openUserLoginActivity();
    }
}
