package com.example.ninemenout;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class SearchBetsBufferActivity extends AppCompatActivity {

    String[] toSend = new String[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_bets_buffer);

        toSend[0] = "Alabama";
        toSend[1] = "spread";
        toSend[2] = "odds";

    }

    public void displaySearch(View view){
        Bundle b = new Bundle();
        b.putStringArray("terms", toSend);
        Intent intent = new Intent(this, SearchBetsActivity.class);
        intent.putExtras(b);
        startActivity(intent);
    }

}
