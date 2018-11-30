package com.rental.car.carrentalbeaverandroid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.rental.car.carrentalbeaverandroid.dbconnection.DatabaseConfig;
import com.rental.car.carrentalbeaverandroid.models.User;

import java.util.ArrayList;
import java.util.List;

public class UserTools {

    private Context context;

    public UserTools(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public User addNewUser(String email, String password) {
        User user = null;
        if (!email.isEmpty() && !password.isEmpty()) {
            password = User.hashPassword(password);

            DatabaseConfig dbConf = new DatabaseConfig(this.context);
            SQLiteDatabase db = dbConf.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("user_email", email);
            values.put("user_password", password);
            try {
                db.insertOrThrow("users", null, values);
                db.close();

                user = findUserByLoginAndPassword(email, password);
            } catch (android.database.SQLException ex) {
                Log.e("USERTOOLS", "addNewUser() error! \n" + ex.getMessage());
            } finally {
                db.close();
                return user;
            }
        }
        return user;
    }

    public List<User> getAllUsers() {
        String[] columns = {"user_id", "user_email", "user_password"};
        DatabaseConfig dbConf = new DatabaseConfig(this.context);
        SQLiteDatabase db = dbConf.getReadableDatabase();
        Cursor cursor = db.query("users", columns, null, null, null, null, null);

        List<User> userList = new ArrayList<>();
        while (cursor.moveToNext()) {
            userList.add(new User(cursor.getInt(0), cursor.getString(1), cursor.getString(2)));
        }
        return userList;
    }

    public User findUserById(int userId) {
        DatabaseConfig dbConf = new DatabaseConfig(this.context);
        SQLiteDatabase db = dbConf.getReadableDatabase();
        Cursor cursor = db.query("users",
                new String[]{"user_id", "user_email", "user_password"},
                "user_id = ?",
                new String[]{String.valueOf(userId)},
                null, null, null, null);

        User user = null;
        if (cursor != null) {
            cursor.moveToFirst();
            user = new User(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
        }

        db.close();
        return user;
    }

    public User findUserByLoginAndPassword(String login, String password) {
        DatabaseConfig dbConf = new DatabaseConfig(this.context);
        SQLiteDatabase db = dbConf.getReadableDatabase();
        Cursor cursor = db.query("users",
                new String[]{"user_id", "user_email", "user_password"},
                "user_email = ? AND user_password = ?",
                new String[]{login, password},
                null, null, null, null);

        User user = null;
        if (cursor.moveToFirst()) {
            user = new User(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
        }
        cursor.close();
        db.close();
        return user;
    }
}



