package com.rental.car.carrentalbeaverandroid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.rental.car.carrentalbeaverandroid.dbconnection.DatabaseConfig;
import com.rental.car.carrentalbeaverandroid.models.Car;
import com.rental.car.carrentalbeaverandroid.models.Order;
import com.rental.car.carrentalbeaverandroid.models.User;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderTools {
    private Context context;

    public OrderTools(Context context) {
        this.context = context;
    }

    /**
     * Convert String in format yyyy-MM-dd to date.
     *
     * @param input Excpected  in format yyyy-MM-dd.
     * @return Date.
     */
    public static Date convertStringToDate(String input) {
        if (input == null)
            return null;

        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        Date output = null;
        try {
            output = ft.parse(input);
        } catch (ParseException e) {
            Log.e("OrderTools", e.getMessage());
        } finally {
            return output;
        }
    }

    public Order addNewOrder(Car car, User user, Date start, Date end) {
        Order order = null;
        if (car != null && car.getCarId() > -1
                && user != null && user.getUserId() > -1
                && start != null && end != null
                && start.compareTo(end) <= 0) {

            DatabaseConfig dbConf = new DatabaseConfig(this.context);
            SQLiteDatabase db = dbConf.getWritableDatabase();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            ContentValues values = new ContentValues();
            values.put("order_user", user.getUserId());
            values.put("order_car", car.getCarId());
            values.put("order_start_date", dateFormat.format(start));
            values.put("order_end_date", dateFormat.format(end));
            try {
                long orderID = db.insertOrThrow("orders", null, values);
                db.close();

                order = findOrderById(orderID);
            } catch (android.database.SQLException ex) {
                Log.e("ORDERTOOLS", "addNewOrder() error! \n" + ex.getMessage());
            } finally {
                db.close();
                return order;
            }
        }
        return order;
    }

    public List<Order> findOrderByUser(User user) {
        return findOrderByUserId(user != null ? user.getUserId() : -1);
    }

    public List<Order> findOrderByUserId(int userId) {
        List<Order> ordersList = new ArrayList<>();
        if (userId > 0) {
            DatabaseConfig dbConf = new DatabaseConfig(this.context);
            SQLiteDatabase db = dbConf.getReadableDatabase();
            Cursor cursor = db.query("orders",
                    new String[]{"order_id"},
                    "order_user = ?",
                    new String[]{String.valueOf(userId)},
                    null, null, null, null);

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    Order temp = findOrderById(cursor.getLong(0));
                    if (temp != null)
                        ordersList.add(temp);
                }
            }
        }

        return ordersList;
    }

    public Order findOrderById(long orderID) {
        DatabaseConfig dbConf = new DatabaseConfig(this.context);
        SQLiteDatabase db = dbConf.getReadableDatabase();
        int fUserID = -1;
        int fCarID = -1;
        Order order = null;
        Car fCar = null;
        User fUser = null;

        Cursor cursor = db.query("orders",
                //new String[]{"order_id", "order_user", "order_car", "order_start_date", "order_end_date" },
                new String[]{"order_user", "order_car"},
                "order_id = ?",
                new String[]{String.valueOf(orderID)},
                null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            fUserID = cursor.getInt(0);
            fCarID = cursor.getInt(1);
        }

        if (fUserID > -1 && fCarID > -1) {
            cursor = db.query("cars",
                    new String[]{"car_id", "car_name", "car_price"},
                    "car_id = ?",
                    new String[]{String.valueOf(fCarID)},
                    null, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();
                fCar = new Car(cursor.getInt(0), cursor.getString(1), new BigDecimal(cursor.getString(2)));
            }

            if (fCar != null) {
                cursor = db.query("users",
                        new String[]{"user_id", "user_email", "user_password"},
                        "user_id = ?",
                        new String[]{String.valueOf(fUserID)},
                        null, null, null, null);

                if (cursor != null) {
                    cursor.moveToFirst();
                    fUser = new User(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
                }
            }

            if (fCar != null && fUser != null) {
                cursor = db.query("orders",
                        new String[]{"order_start_date", "order_end_date"},
                        "order_id = ?",
                        new String[]{String.valueOf(orderID)},
                        null, null, null, null);

                if (cursor != null) {
                    cursor.moveToFirst();
                    order = new Order((int) orderID, fUser, fCar,
                            OrderTools.convertStringToDate(cursor.getString(0)),
                            OrderTools.convertStringToDate(cursor.getString(1)));
                }
            }
        }

        db.close();
        return order;
    }
}
