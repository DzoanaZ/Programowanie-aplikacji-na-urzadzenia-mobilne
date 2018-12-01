package com.rental.car.carrentalbeaverandroid.dbconnection;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.rental.car.carrentalbeaverandroid.models.User;

public class DatabaseConfig extends SQLiteOpenHelper {

    public DatabaseConfig(Context context){
        super(context, "moja-baza.db",null,1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users (" +
                "user_id integer primary key autoincrement," +
                "user_email text," +
                "user_password text" +
                ")");

        db.execSQL("CREATE UNIQUE INDEX user_email_unique_index ON users(user_email)");

        db.execSQL("CREATE TABLE cars (" +
                "car_id integer primary key autoincrement," +
                "car_name text not null," +
                "car_price DECIMAL(6,2) not null" +
                ")");

        db.execSQL("CREATE TABLE orders(" +
                " order_id integer primary key autoincrement, " +
                " order_user integer not null, " +
                " order_car integer not null, " +
                " order_start_date text not null, " +
                " order_end_date text not null, " +
                " FOREIGN KEY (order_user) REFERENCES users (user_id) " +
                " ON DELETE CASCADE ON UPDATE NO ACTION, " +
                " FOREIGN KEY (order_car) REFERENCES cars (car_id) " +
                " ON DELETE CASCADE ON UPDATE NO ACTION" +
                ")");

        enterUser(db, "johanna.95@o2.pl", "qwerty");
        enterUser(db, "skoczp@gmail.com", "qwerty");
        enterUser(db, "dgolob1994@gmail.com", "qwerty");
        enterUser(db, "sylwesterbon@gmail.com", "qwerty");
        enterUser(db, "jkowalski@o2.pl", "kowal");

        enterCar(db,"Opel Corsa C 2003 1.2", 115.99);
        enterCar(db,"Audi A3 1.9 TDI", 155.50);
        enterCar(db,"Fiat Panda", 85.00);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void enterUser(SQLiteDatabase db, String email, String password){
        ContentValues values = new ContentValues();
        values.put("user_email", email);
        values.put("user_password", User.hashPassword(password));
        db.insert("users", null, values);
    }

    private void enterCar(SQLiteDatabase db, String name, Double price)
    {
        ContentValues values = new ContentValues();
        values.put("car_name", name);
        values.put("car_price", price);
        db.insert("cars", null, values);
    }
}
