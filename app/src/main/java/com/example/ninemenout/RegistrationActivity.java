package com.example.ninemenout;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


public class RegistrationActivity extends AppCompatActivity {

    public static final String BALANCE_KEY = "balance";
    public static final String EMAIL_KEY = "email";
    public static final String USERNAME_KEY = "name";
    public static final String PASSWORD_KEY = "password";

    private DatabaseReference db = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        //button = (Button) findViewById(R.id.register);
//        button.setOnClickListener((v) -> {  )
         /*   if (user_information_entered == 1) {
                if (register_complete == 1) {
                    openUserLoginActivity();
                } else {
                    Context context = getApplicationContext();
                    CharSequence reusername = "User name taken";
                    int duration_one = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, reusername, duration_one);
                    toast.show();
                }
            } else {
                Context context = getApplicationContext();
                CharSequence no_info = "Please fill in both fields";
                int duration_two = Toast.LENGTH_SHORT;
                Toast toast_two = Toast.makeText(context, no_info, duration_two);
                toast_two.show();
            }
        }); */
    }


    /*public void openUserLoginActivity() {
        Intent intent = new Intent(this, UserLoginActivity.class);
        startActivity(intent);
    }*/

    public void openHomePageActivivty() {
            Intent intent = new Intent(this, HomePageActivity.class);
            startActivity(intent);

    }



    public void saveUser(View view) {
        EditText UserName = (EditText) findViewById(R.id.username);
        EditText Passcode  = (EditText) findViewById(R.id.Password);
        EditText User_email = (EditText) findViewById(R.id.email);

        String usertext = UserName.getText().toString();
        String passtext = Passcode.getText().toString();
        String U_email = User_email.getText().toString();
        int initial = 100;

        if(usertext.isEmpty() || passtext.isEmpty() || U_email.isEmpty()) {return; }
        //register_complete = 1;
        Map<String, Object> dataToSave = new HashMap<String, Object>();
        dataToSave.put(BALANCE_KEY, initial);
        dataToSave.put(EMAIL_KEY, U_email);
        dataToSave.put(USERNAME_KEY, usertext);
        dataToSave.put(PASSWORD_KEY, passtext);

        db.child("users")
            .child(USERNAME_KEY).setValue(dataToSave)
            .addOnSuccessListener( new OnSuccessListener<Void> () {
            @Override
            public void onSuccess(Void aVoid) {
                openHomePageActivivty();
            }
        }).addOnFailureListener( new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Context context = getApplicationContext();
                CharSequence reusername = "Registration Failed";
                int duration_one = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, reusername, duration_one);
                toast.show();
            }
        } );



    }


}
