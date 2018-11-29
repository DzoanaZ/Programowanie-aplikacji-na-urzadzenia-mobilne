package com.rental.car.carrentalbeaverandroid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.rental.car.carrentalbeaverandroid.dbconnection.DatabaseConfig;
import com.rental.car.carrentalbeaverandroid.models.Car;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class CarTools {
    private Context context;

    public CarTools(Context context) {
        this.context = context;
    }

    public Car addNewCar(String carName, BigDecimal carPrice){
        Car car = null;
        if(!carName.isEmpty() && carPrice!=null && carPrice.compareTo(new BigDecimal("0.00"))>0){
            DatabaseConfig dbConf = new DatabaseConfig(this.context);
            SQLiteDatabase db = dbConf.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("car_name", carName);
            values.put("car_price", carPrice.doubleValue());
            try {
                long rowId = db.insertOrThrow("users", null, values);
                db.close();

                car = findCarById((int)rowId);
            }
            catch (SQLException ex)
            {
                Log.e("DB","CarTools.addNewCar() error! \n"+ex.getMessage());
            }
            finally {
                db.close();
                return car;
            }
        }

        return car;
    }

    public Car findCarById(int rowId){
        DatabaseConfig dbConf = new DatabaseConfig(this.context);
        SQLiteDatabase db = dbConf.getReadableDatabase();
        Cursor cursor = db.query("cars",
                new String[]{"car_id", "car_name", "car_price"},
                "car_id = ?",
                new String[]{String.valueOf(rowId)},
                null, null, null, null);

        Car car = null;
        if(cursor!=null) {
            cursor.moveToFirst();
             car = new Car(cursor.getInt(0), cursor.getString(1), new BigDecimal(String.valueOf(cursor.getFloat(2))));
        }

        db.close();
        return car;
    }

    public List<Car> getAllCars(){
        String[] columns={"car_id","car_name","car_price"};
        DatabaseConfig dbConf = new DatabaseConfig(this.context);
        SQLiteDatabase db = dbConf.getReadableDatabase();
        Cursor cursor = db.query("cars", columns, null, null, null, null, null);

        List<Car> causerList = new ArrayList<>();
        while(cursor.moveToNext()){
            causerList.add(new Car(cursor.getInt(0), cursor.getString(1), new BigDecimal(String.valueOf(cursor.getFloat(2)))));
        }
        return causerList;
    }
}
