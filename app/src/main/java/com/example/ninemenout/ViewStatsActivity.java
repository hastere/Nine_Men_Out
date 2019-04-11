package com.example.ninemenout;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ViewStatsActivity extends AppCompatActivity {

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_stats);

        button = findViewById(R.id.betHistoryBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBetHistoryActivity();
            }
        });
    }

    public void openBetHistoryActivity() {
        Intent intent = new Intent(this,  BetHistoryActivity.class);
        startActivity(intent);
    }
}
