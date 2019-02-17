package com.example.ninemenout;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;




public class RegistrationActivity extends AppCompatActivity {

    private Button button;

    int user_information_entered = 0;
    int registerd = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        button = (Button) findViewById(R.id.register);
        button.setOnClickListener((v) -> {
            if (user_information_entered == 1) {
                if (registered == 1) {
                    openUserLoginActivity()
                } else {
                    Context context = getApplicationContext();
                    CharSequence reusername = "User name taken";
                    int duration_one = TOAST.LENGTH_SHORT;
                    Toast toast = TOAST.makeText(context, reusername, duration_one);
                    toast.show();
                }
            } else {
                Context context = getApplicationContext();
                CharSequence no_info = "Please fill in both fields";
                int duration_two = TOAST.LENGTH_SHORT;
                TOAST toast_two = TOAST.makeText(context, no_info, duration_two);
                toast_two.show();
            }
        })
    }





    public void saveUser(View view) {
        EditText UserName = (EditText) findViewById(R.id.editusername);
        EditText Passcode  = (EditText) findViewById(R.id.editPassword);
        EditText User_email = (EditText) findViewById(R.id.editemail);

        String usertext = UserName.getText().toString();
        String passtext = Passcode.getText().toString();
        String U_email = User_email.getText().toString();



    }

    public void openUserLoginActivity() {
            Intent intent = new Intent(this, UserProfileActivity.class);
            startActivity(intent);
        }
}
