package com.rental.car.carrentalbeaverandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;

import com.rental.car.carrentalbeaverandroid.models.User;

public class UserPanelActivity extends AppCompatActivity {

    private User logedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_panel);
        logedUser = (User)getIntent().getParcelableExtra("loged_user");
        ((TextView)findViewById(R.id.textView)).setText("Hello " + logedUser.getEmail() + "!");
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Log.d("CDA", "onKeyDown Called");
        }
       return super.onKeyDown(keyCode, event);
    }


}
