package com.rental.car.carrentalbeaverandroid.dbconnection;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.rental.car.carrentalbeaverandroid.models.User;

public class DatabaseConfig extends SQLiteOpenHelper {

    public DatabaseConfig(Context context){
        super(context, "moja-baza.db",null,2);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users (" +
                "user_id integer primary key autoincrement," +
                "user_email text," +
                "user_password text" +
                ")");
        enterUser(db, "johanna.95@o2.pl", "qwerty");
        enterUser(db, "skoczp@gmail.com", "gwerty");
        enterUser(db, "dgolob1994@gmail.com", "qwerty");
        enterUser(db, "sylwesterbon@gmail.com", "qwerty");
        enterUser(db, "jkowalski@o2.pl", "kowal");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE users;");
        onCreate(db);
    }

    private void enterUser(SQLiteDatabase db, String email, String password){
        ContentValues values = new ContentValues();
        values.put("user_email", email);
        values.put("user_password", User.hashPassword(password));
        db.insert("users", null, values);
    }
}
