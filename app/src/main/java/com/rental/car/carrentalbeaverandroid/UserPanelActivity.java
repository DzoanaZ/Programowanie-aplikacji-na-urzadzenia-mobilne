package com.rental.car.carrentalbeaverandroid;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rental.car.carrentalbeaverandroid.models.Car;
import com.rental.car.carrentalbeaverandroid.models.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserPanelActivity extends AppCompatActivity {

    private User logedUser;
    private CarTools carTools;
    private String[] carsNames;
    private int selectedCarId=-1;

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

    public void newOrderButtonClick(Button button){
        Map<Integer, String> carsMap = new HashMap<>();
        List<Car> listCars =  carTools.getAllCars();
        for (Car car : listCars)
        {
            carsMap.put(car.getCarId(), car.getCarName() + " " +car.getCarPrice().toString()+"zł");
        }

        carsNames = carsMap.values().toArray(new String[carsMap.values().size()]);
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        mBuilder.setTitle("Wybierz samochód");
        mBuilder.setIcon(R.drawable.car_icon);
        mBuilder.setSingleChoiceItems(carsNames, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for(Map.Entry<Integer, String> entry : carsMap.entrySet()){
                    if(entry.getValue().equals( carsNames[which])){
                        selectedCarId = entry.getKey();
                    }
                }
                if(selectedCarId>-1) {
                    Car selectedCar = carTools.findCarById(selectedCarId);
                    Log.d("DIALOG", "Selected car: " + selectedCar.toString());
                }
                else
                    Log.e("DIALOG", "Error during selecting car.");

                dialog.dismiss();
            }
        });

        mBuilder.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();

    }
}
