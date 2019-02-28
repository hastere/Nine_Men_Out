package com.example.ninemenout;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;

public class UserProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
    }

    public void openAddFriendsActivity() {
        Intent intent = new Intent(this, AddFriendsActivity.class);
        startActivity(intent);

    }
}
