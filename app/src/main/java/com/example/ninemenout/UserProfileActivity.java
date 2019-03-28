package com.example.ninemenout;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class UserProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
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



    public void logout(View view)
        {
        FirebaseAuth.getInstance().signOut();
        openUserLoginActivity();
    }
}
