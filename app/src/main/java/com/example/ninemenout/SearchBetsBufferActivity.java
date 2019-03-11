package com.example.ninemenout;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

public class SearchBetsBufferActivity extends AppCompatActivity {

    String[] toSend = new String[3];
    private TextView homeTeam;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_bets_buffer);

        homeTeam = findViewById(R.id.homeTeam);

        toSend[0] = "Alabama";
        toSend[1] = "spread";
        toSend[2] = "odds";

    }

    public void chooseBetType(View view){
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.spreadButton:
                if(checked)
                    toSend[1] = "spread";
                break;
            case R.id.straightButton:
                if(checked)
                    toSend[1] = "straight";
                break;
        }
    }

    public void displaySearch(View view){
        String teamName = homeTeam.getText().toString();

        toSend[0] = teamName;

        Bundle b = new Bundle();
        b.putStringArray("terms", toSend);
        Intent intent = new Intent(this, SearchBetsActivity.class);
        intent.putExtras(b);
        startActivity(intent);
    }

}
