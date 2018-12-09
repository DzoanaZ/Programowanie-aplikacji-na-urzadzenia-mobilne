package com.rental.car.carrentalbeaverandroid.activities;

import android.content.Intent;
import android.os.Bundle;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.EditText;

import com.rental.car.carrentalbeaverandroid.R;
import com.rental.car.carrentalbeaverandroid.models.User;
import com.rental.car.carrentalbeaverandroid.tools.UserTools;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private UserTools userTools;
    private User logedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userTools = new UserTools(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void click(View view) {
        switch (view.getId()) {
            case R.id.logonButton:
                clickLogonButton();
                break;
            case R.id.signupButton:
                Intent i = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(i);
                break;
        }
    }

    private void clickLogonButton() {
        EditText emailEdit = findViewById(R.id.emailEdit);
        EditText passwordEdit = findViewById(R.id.passwordEdit);
        if (!passwordEdit.getText().toString().isEmpty() && !emailEdit.getText().toString().isEmpty()) {
            logedUser = userTools.findUserByLoginAndPassword(emailEdit.getText().toString(), User.hashPassword(passwordEdit.getText().toString()));
        }
        this.logon();
    }

    private void logon() {
        if (logedUser != null) {
            Log.d("Logon", "Found loged user");
            Intent intent = new Intent(MainActivity.this, UserPanelActivity.class);
            intent.putExtra("loged_user", logedUser);
            startActivity(intent);
            finish();
        } else {
            Snackbar.make(findViewById(R.id.logonButton), "Błędny login lub hasło.", Snackbar.LENGTH_LONG)
                    .show();
            List<User> allUsers = userTools.getAllUsers();
            for (User user : allUsers) {
                Log.d("DATA", user.getUserId() + " " + user.getEmail());
            }
        }
    }
}
