package com.rental.car.carrentalbeaverandroid.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.rental.car.carrentalbeaverandroid.R;
import com.rental.car.carrentalbeaverandroid.models.Order;
import com.rental.car.carrentalbeaverandroid.models.User;
import com.rental.car.carrentalbeaverandroid.tools.OrderTools;

import java.text.SimpleDateFormat;

public class MyOrdersActivity extends AppCompatActivity {

    private User logedUser;
    private OrderTools orderTools;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        logedUser = (User) getIntent().getParcelableExtra("loged_user");
        orderTools = new OrderTools(this);
        StringBuilder strBuilder = new StringBuilder();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        for (Order order : orderTools.findOrderByUser(logedUser)) {
            strBuilder.append("Samochód: " + order.getCar().getCarName())
                    .append("\nData wypożyczenia: " + dateFormat.format(order.getStartDate()))
                    .append("\nData zwrotu: " + dateFormat.format(order.getEndDate()))
                    .append("\nCena za dobę: " + order.getCar().getCarPrice().doubleValue() + "zł")
                    .append("\n\n");
        }
        TextView textView = findViewById(R.id.textView);
        if (strBuilder.length() > 0)
            textView.setText(strBuilder.toString());
        else
            textView.setText("Brak wypożyczeń na Twoim koncie");
    }
}
