package com.example.ninemenout;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

        private TextView emailLogin;
        private TextView passwordLogin;
        private FirebaseAuth mAuth;
        private static final String TAG = "MainActivity";

        @Override
        protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // ...
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        emailLogin = findViewById(R.id.emailLogin);
        passwordLogin = findViewById(R.id.passwordLogin);

        findViewById(R.id.btnLogin).setOnClickListener(this);
        findViewById(R.id.btnCreate).setOnClickListener(this);
    }

        @Override
        public void onStart () {
            super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

        private void login (String email, String password){
            if (!checkForm()) {
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(MainActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }

                }
            });
        }


        private boolean checkForm () {
        boolean check = true;

        String email = emailLogin.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailLogin.setError("Required.");
            check = false;
        } else {
            emailLogin.setError(null);
        }

        String password = passwordLogin.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passwordLogin.setError("Required.");
            check = false;
        } else {
            passwordLogin.setError(null);
        }

        return check;
    }

        private void updateUI (FirebaseUser user){
        if (user != null) {
            Intent nextScreen = new Intent(this, HomePageActivity.class);
            this.startActivityForResult(nextScreen, 0);
        } else {
            Intent nextScreen = new Intent(this, MainActivity.class);
            this.startActivityForResult(nextScreen, 0);
        }
    }

        @Override
        public void onClick (View v){
        int i = v.getId();
        if (i == R.id.btnCreate) {
            Intent nextScreen = new Intent(this, HomePageActivity.class);
            this.startActivityForResult(nextScreen, 0);
        } else if (i == R.id.btnLogin) {
            login(emailLogin.getText().toString(), passwordLogin.getText().toString());
        }
    }
}
