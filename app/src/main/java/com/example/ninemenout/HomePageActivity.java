package com.example.ninemenout;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomePageActivity extends AppCompatActivity {

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        button =(Button) findViewById(R.id.profileButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUserProfileActivity();
            }
        });
        button =(Button) findViewById(R.id.createBetsBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreateBetsActivity();
            }
        });
        button =(Button) findViewById(R.id.suggestedBetsBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSuggestedBetsActivity();
            }
        });
        button =(Button) findViewById(R.id.searchBetsBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSearchBetsActivity();
            }
        });
        button =(Button) findViewById(R.id.leaderboardsBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLeaderboardActivity();
            }
        });
    }

    public void openUserProfileActivity() {
        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
    }

    public void openCreateBetsActivity() {
        Intent intent = new Intent(this, CreateBetsActivity.class);
        startActivity(intent);
    }

    public void openSearchBetsActivity() {
        Intent intent = new Intent(this, SearchBetsBufferActivity.class);
        startActivity(intent);
    }

    public void openSuggestedBetsActivity() {
        Intent intent = new Intent(this, SuggestedBetsActivity.class);
        startActivity(intent);
    }

    public void openLeaderboardActivity() {
        Intent intent = new Intent(this,LeaderboardActivity.class);
        startActivity(intent);
    }

}
