package com.example.ninemenout;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class RegistrationActivity extends AppCompatActivity {

    public static final String BALANCE_KEY = "balance";
    public static final String EMAIL_KEY = "email";
    public static final String USERNAME_KEY = "name";
    public static final String PASSWORD_KEY = "password";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth myAuth;
    private static final String TAG = "RegistrationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
        myAuth = FirebaseAuth.getInstance();

    }


    @Override
    public void onStart(){
        super.onStart();
        FirebaseUser currentUser = myAuth.getCurrentUser();
        if(currentUser != null)
        {
            Context context = getApplicationContext();
            CharSequence reusername = "You already have an account";
            int duration_one = Toast.LENGTH_SHORT;
            Toast toast_2 = Toast.makeText(context, reusername, duration_one);
            toast_2.show();
        }
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

        myAuth.createUserWithEmailAndPassword(U_email, passtext)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = myAuth.getCurrentUser();
                            Toast.makeText(RegistrationActivity.this, "Authentication Success.",
                                    Toast.LENGTH_SHORT).show();
                            //openHomePageActivivty();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegistrationActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
        Log.d(TAG, "We got past the auth user");
        FirebaseUser user = myAuth.getCurrentUser();
        Map<String, Object> dataToSave = new HashMap<String, Object>();
        dataToSave.put(BALANCE_KEY, initial);
        dataToSave.put(EMAIL_KEY, U_email);
        dataToSave.put(USERNAME_KEY, usertext);
        dataToSave.put(PASSWORD_KEY, passtext);
        String UID = user.getUid();

        db.collection("users").document(UID).set(dataToSave)
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
