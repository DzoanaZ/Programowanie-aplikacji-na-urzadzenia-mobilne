package com.rental.car.carrentalbeaverandroid;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.rental.car.carrentalbeaverandroid.models.User;

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

            if (!passwordString.equals(passwordRepeatString)) {
                Toast pass = Toast.makeText(SignUp.this , "Hasla sie nie zgadzaja", Toast.LENGTH_SHORT);
                pass.show();
                if(!passwordString.isEmpty() && !emailString.isEmpty())
                {
                    userTools.addNewUser(emailEdit.getText().toString(), passwordEdit.getText().toString());
                }
            }
        }
    }
}
