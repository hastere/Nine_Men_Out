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

// controls user input and uses it to tell the search bets activity what to display
public class SearchBetsBufferActivity extends AppCompatActivity {

    String[] toSend = new String[3];
    private TextView homeTeam;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_bets_buffer);

        homeTeam = findViewById(R.id.homeTeam);

        // default values mainly used for testing
        toSend[0] = "any";
        toSend[1] = "spread";
        toSend[2] = "odds";

    }

    // controls the first set of radio buttons
    public void chooseBetType(View view){
        boolean checked = ((RadioButton) view).isChecked();

        // update the array that will be sent to search bets based on button clicks
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

    // controls the second set of radio buttons
    public void chooseSortType(View view){
        boolean checked = ((RadioButton) view).isChecked();

        // update array to be sent based on button presses
        switch(view.getId()) {
            case R.id.oddsButton:
                if(checked)
                    toSend[2] = "odds";
                break;
            case R.id.betAmountButton:
                if(checked)
                    toSend[2] = "amount";
                break;
        }
    }

    // packages up all of the information and sends it to the new activity
    public void displaySearch(View view){
        String teamName = homeTeam.getText().toString();
        if(!teamName.equals("")) // eliminate entering nothing, will default to Alabama if nothing entered
            toSend[0] = teamName;

        // send the array of information to the new search activity to be displayed
        Bundle b = new Bundle();
        b.putStringArray("terms", toSend);
        Intent intent = new Intent(this, SearchBetsActivity.class);
        intent.putExtras(b);
        startActivity(intent);
    }

}
