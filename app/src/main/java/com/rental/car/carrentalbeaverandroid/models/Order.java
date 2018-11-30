package com.rental.car.carrentalbeaverandroid.models;

import java.util.Date;

public class Order {
    private int orderId;
    private User user;
    private Car car;
    private Date startDate;
    private Date endDate;

    public Order(int orderId, User user, Car car, Date startDate, Date endDate) {
        this.orderId = orderId;
        this.user = user;
        this.car = car;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /*
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
    */
}
