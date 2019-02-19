package com.example.ninemenout;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {}

/*    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // ...
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        final TextView emailLogin = (TextView) findViewById(R.id.emailLogin);
        final TextView passwordLogin = (TextView) findViewById(R.id.passwordLogin);
        final Button btnLogin = (Button) findViewById(R.id.btnLogin);
        final Button btnCreate = (Button) findViewById(R.id.btnCreate);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailLogin.getText().toString();
                String password = passwordLogin.getText().toString();

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Intent nextScreen = new Intent(view.getContext(),.class);
                                    startActivityForResult(nextScreen, 0);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(view.getContext(), "Authentication failed. Incorrect Email or Password", Toast.LENGTH_SHORT).show();
                                }

                                emailLogin.setText("");
                                passwordLogin.setText("");
                            }
                        });
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent nextScreen = new Intent(view.getContext(), RegistrationActivity.class);
                    startActivityForResult(nextScreen, 0);
            }
        });
    }
}*/
