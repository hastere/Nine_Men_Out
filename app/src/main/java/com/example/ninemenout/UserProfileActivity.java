package com.example.ninemenout;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UserProfileActivity extends AppCompatActivity {

    private Button betsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        betsBtn =(Button) findViewById(R.id.personalBetsBtn);
        betsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPersonalBetActivity();
            }
        });
    }

    public void openPersonalBetActivity() {
        Intent intent = new Intent(this, PersonalBetActivity.class);
        startActivity(intent);
    }
}
