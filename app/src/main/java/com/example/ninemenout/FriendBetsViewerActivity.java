package com.example.ninemenout;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FriendBetsViewerActivity extends AppCompatActivity {
    private static final String TAG = "PersonalBetActivity";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    static private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private CollectionReference betRef;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_bets_viewer);
    }

    public void profilePage(View view)
    {
        Intent intent = new Intent(this,UserProfileActivity.class);
        startActivity(intent);
    }


    public void seeFriends(View view){
        Intent intent = new Intent(this, ViewRequestsActivity.class);
        startActivity(intent);
    }
}
