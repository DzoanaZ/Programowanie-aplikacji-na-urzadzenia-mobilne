package com.rental.car.carrentalbeaverandroid.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.rental.car.carrentalbeaverandroid.R;
import com.rental.car.carrentalbeaverandroid.models.User;
import com.rental.car.carrentalbeaverandroid.tools.UserTools;


public class SignUpActivity extends Activity {

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
                    User newUser = userTools.addNewUser(emailEdit.getText().toString(), emailEdit.getText().toString(), passwordEdit.getText().toString());

                    if (newUser != null) {
                        Toast pass = Toast.makeText(SignUpActivity.this, "Rejestracja zakończona sukcesem :)", Toast.LENGTH_SHORT);
                        pass.show();
                        Log.d("REGISTER", "New user is created.");
                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast pass = Toast.makeText(SignUpActivity.this, "Taki użytkownik już istnieje :(", Toast.LENGTH_SHORT);
                        pass.show();
                    }

                } else {
                    Toast pass = Toast.makeText(SignUpActivity.this, "Hasła się nie zgadzają", Toast.LENGTH_SHORT);
                    pass.show();
                }
            } else {
                Toast pass = Toast.makeText(SignUpActivity.this, "Nie wypełniłeś wszystkich pól", Toast.LENGTH_SHORT);
                pass.show();
            }
        }
    }
}
