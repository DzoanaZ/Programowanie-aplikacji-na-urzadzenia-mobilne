package com.rental.car.carrentalbeaverandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class SignUp extends Activity {

    private UserTools userTools;

    protected void onCreate(Bundle savedInstanceState) {
        userTools = new UserTools(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
    }

    public void onButtonClick(View v) {
        if (v.getId() == R.id.signupButton) {
            EditText emailEdit = findViewById(R.id.emailEdit);
            EditText passwordEdit = findViewById(R.id.passwordEdit);
            EditText passwordRepeatEdit = findViewById(R.id.passwordRepeatEdit);

            String emailString = emailEdit.getText().toString();
            String passwordString = passwordEdit.getText().toString();
            String passwordRepeatString = passwordRepeatEdit.getText().toString();

            if (!passwordString.isEmpty() && !emailString.isEmpty()) {
                if (passwordString.equals(passwordRepeatString)) {
                    userTools.addNewUser(emailEdit.getText().toString(), passwordEdit.getText().toString());
                    Toast pass = Toast.makeText(SignUp.this, "Rejestracja zakończona sukcesem :)", Toast.LENGTH_SHORT);
                    pass.show();
                    Log.d("REGISTER", "New user is created.");
                    Intent intent = new Intent(SignUp.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast pass = Toast.makeText(SignUp.this, "Hasła się nie zgadzają", Toast.LENGTH_SHORT);
                    pass.show();
                }
            } else {
                Toast pass = Toast.makeText(SignUp.this, "Nie wypełniłeś wszystkich pól", Toast.LENGTH_SHORT);
                pass.show();
            }
        }
    }
}
