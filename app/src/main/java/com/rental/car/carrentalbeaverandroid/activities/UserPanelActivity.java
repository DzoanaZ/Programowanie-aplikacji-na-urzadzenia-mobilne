package com.rental.car.carrentalbeaverandroid.activities;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.rental.car.carrentalbeaverandroid.R;
import com.rental.car.carrentalbeaverandroid.models.Car;
import com.rental.car.carrentalbeaverandroid.models.Order;
import com.rental.car.carrentalbeaverandroid.models.User;
import com.rental.car.carrentalbeaverandroid.tools.CarTools;
import com.rental.car.carrentalbeaverandroid.tools.OrderTools;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserPanelActivity extends AppCompatActivity {

    private OrderTools orderTools;
    private CarTools carTools;
    DatePickerDialog datePickerDialogRent;
    DatePickerDialog datePickerDialogReturn;

    private User logedUser;
    private Car selectedCar;
    private String[] carsNames;
    private int selectedCarId = -1;
    private Map<Integer, String> carsMap;

    private Date orderStartDate;
    private Date orderEndDate;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_panel);

        logedUser = (User) getIntent().getParcelableExtra("loged_user");
        ((TextView) findViewById(R.id.textView)).setText("Hello " + logedUser.getEmail() + "!");

        carTools = new CarTools(this);
        orderTools = new OrderTools(this);

        Calendar dateOfRent = Calendar.getInstance();
        int rentYear = dateOfRent.get(Calendar.YEAR);
        int rentMonth = dateOfRent.get(Calendar.MONTH);
        int rentDay = dateOfRent.get(Calendar.DAY_OF_MONTH);

        datePickerDialogRent = new DatePickerDialog(
                UserPanelActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Log.d("DATEPICKER", "Rental date: " + year + "/" + (month + 1) + "/" + dayOfMonth);
                if (year > 0 && month > -1 && dayOfMonth > 0) {
                    orderStartDate = OrderTools.convertStringToDate(year + "-" + (month + 1) + "-" + dayOfMonth);
                }
            }
        }, rentYear, rentMonth, rentDay);
        datePickerDialogRent.getDatePicker().setMinDate(new Date().getTime());
        datePickerDialogRent.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (orderStartDate != null) {
                    datePickerDialogReturn.setTitle("Wybierz datę zwrotu");
                    datePickerDialogReturn.show();
                }
            }
        });


        datePickerDialogReturn = new DatePickerDialog(
                UserPanelActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Log.d("DATEPICKER", "Return date: " + year + "/" + (month + 1) + "/" + dayOfMonth);
                if (year > 0 && month > -1 && dayOfMonth > 0) {
                    orderEndDate = OrderTools.convertStringToDate(year + "-" + (month + 1) + "-" + dayOfMonth);
                } else {
                    orderStartDate = null;
                }
            }
        }, rentYear, rentMonth, rentDay);
        datePickerDialogReturn.setTitle("Wybierz datę zwrotu");
        datePickerDialogReturn.getDatePicker().setMinDate(new Date().getTime());
        datePickerDialogReturn.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (orderStartDate != null && orderEndDate != null) {
                    Log.d("datePickerDialogReturn", "onDismiss() Before createNewOrder()");
                    createNewOrder();
                } else {
                    orderStartDate = null;
                    orderEndDate = null;
                }
            }
        });
    } //END onCreate()

    public void click(View view) {
        switch (view.getId()) {
            case R.id.newOrderButton:
                newOrderButtonClick((Button) view);
                break;
            case R.id.myOrdersButton:
                myOrdersButtonClick((Button) view);
                break;
        }
    }

    private void myOrdersButtonClick(Button view) {
        Intent intent = new Intent(UserPanelActivity.this, MyOrdersActivity.class);
        intent.putExtra("loged_user", logedUser);
        startActivity(intent);
    }

    public void newOrderButtonClick(Button button) {
        initialCarsMapAndNames();

        if (carsNames.length > 0) {
            AlertDialog.Builder selectCarDialogBuilder = new AlertDialog.Builder(this);
            selectCarDialogBuilder.setTitle("Wybierz samochód");
            selectCarDialogBuilder.setIcon(R.drawable.car_icon);
            selectCarDialogBuilder.setSingleChoiceItems(carsNames, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    for (Map.Entry<Integer, String> entry : carsMap.entrySet()) {
                        if (entry.getValue().equals(carsNames[which])) {
                            selectedCarId = entry.getKey();
                        }
                    }
                    if (selectedCarId > -1) {
                        selectedCar = carTools.findCarById(selectedCarId);
                        Log.d("DIALOG", "Selected car: " + selectedCar.toString());
                        datePickerDialogRent.setTitle("Wybierz datę wypożyczenia");
                        datePickerDialogRent.show();
                    } else
                        Log.e("DIALOG", "Error during selecting car.");

                    dialog.dismiss();
                }
            });

            selectCarDialogBuilder.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            AlertDialog selectCarDialog = selectCarDialogBuilder.create();
            selectCarDialog.show();
        } else {
            Toast.makeText(UserPanelActivity.this, "Brak dostępnych smochodów.", Toast.LENGTH_LONG).show();
        }
    }

    private void initialCarsMapAndNames() {
        carsMap = new HashMap<>();
        List<Car> listCars = carTools.getAllCars();
        for (Car car : listCars) {
            carsMap.put(car.getCarId(), car.getCarName() + " " + car.getCarPrice().toString() + "zł");
        }
        carsNames = carsMap.values().toArray(new String[carsMap.values().size()]);
    }

    private void createNewOrder() {
        if (orderStartDate != null && orderEndDate != null
                && orderStartDate.compareTo(orderEndDate) <= 0
                && logedUser != null && selectedCar != null) {
            Log.d("createNewOrder", "All input data are correct.");
            Order order = orderTools.addNewOrder(selectedCar, logedUser, orderStartDate, orderEndDate);


            selectedCar = null;
            orderStartDate = null;
            orderEndDate = null;
        } else {
            Toast.makeText(UserPanelActivity.this, "Niepoprawne dane!", Toast.LENGTH_LONG).show();
        }
    }
}
