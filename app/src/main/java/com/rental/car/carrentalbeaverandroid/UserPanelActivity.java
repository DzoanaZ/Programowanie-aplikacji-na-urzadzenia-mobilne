package com.rental.car.carrentalbeaverandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rental.car.carrentalbeaverandroid.models.Car;
import com.rental.car.carrentalbeaverandroid.models.User;

public class UserPanelActivity extends AppCompatActivity {

    private User logedUser;
    private CarTools carTools;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        carTools = new CarTools(this);
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


    public void click(View view) {
        switch(view.getId()){
            case R.id.newOrderButton:
                newOrderButtonClick((Button)view);
                break;
            case R.id.myOrdersButton:
                break;
        }
    }

    private void newOrderButtonClick(Button button){
        for (Car car : carTools.getAllCars())
        {
            Log.d("CAR",car.getCarId() + " "+ car.getCarName() + " " +car.getCarPrice().toString()+" zł ");
        }
    }
}
