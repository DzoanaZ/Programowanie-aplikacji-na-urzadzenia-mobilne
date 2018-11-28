package com.rental.car.carrentalbeaverandroid;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.EditText;
import android.widget.TextView;

import com.rental.car.carrentalbeaverandroid.models.User;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private UserTools userTools;
    private User logedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userTools = new UserTools(this);
        userTools.addNewUser("user@user.pl","user");
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.logon();
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
        switch(view.getId()) {
            case R.id.logonButton :
                clickLogonButton();
                break;
        }
    }

    private void clickLogonButton(){
        EditText emailEdit = findViewById(R.id.emailEdit);
        EditText passwordEdit = findViewById(R.id.passwordEdit);
        if(!passwordEdit.getText().toString().isEmpty() && !emailEdit.getText().toString().isEmpty())
        {
            logedUser = userTools.findUserByLoginAndPassword(emailEdit.getText().toString(), User.hashPassword(passwordEdit.getText().toString())) ;
            logon();
        }
        else
        {
            List<User> allUsers = userTools.getAllUsers();
            for(User user : allUsers)
            {
                System.out.println(user.getUserId() + " "  +user.getEmail());
            }
        }
    }

    private void logon(){
        if(logedUser!=null) {
            Intent intent = new Intent(MainActivity.this, UserPanelActivity.class);
            intent.putExtra("loged_user", logedUser);
            startActivity(intent);
        }
    }
}
