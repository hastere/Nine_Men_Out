package com.example.ninemenout;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class FriendBetsViewerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_bets_viewer);
    }



    public void seeFriends(View view){
        Intent intent = new Intent(this, ViewRequestsActivity.class);
        startActivity(intent);
    }
}
