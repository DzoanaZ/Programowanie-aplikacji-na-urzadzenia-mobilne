package com.rental.car.carrentalbeaverandroid.models;

import java.io.Serializable;
import java.math.BigDecimal;

public class Car implements Serializable {
    /*
    car_id integer primary key autoincrement
    car_name text not null
    car_price DECIMAL(6,2) not null
    */
    private int carId;
    private String carName;
    private BigDecimal carPrice;

    public Car(int carId, String carName, BigDecimal carPrice) {
        this.carId = carId;
        this.carName = carName;
        this.carPrice = carPrice;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public BigDecimal getCarPrice() {
        return carPrice;
    }

    public void setCarPrice(BigDecimal carPrice) {
        this.carPrice = carPrice;
    }

    @Override
    public String toString() {
        return carId + " " + carName + " " + carPrice + "zł";
    }
}
