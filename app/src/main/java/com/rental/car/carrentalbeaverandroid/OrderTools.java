package com.rental.car.carrentalbeaverandroid;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.rental.car.carrentalbeaverandroid.models.Car;
import com.rental.car.carrentalbeaverandroid.models.Order;
import com.rental.car.carrentalbeaverandroid.models.User;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class OrderTools {
    private Context context;
    private ProgressDialog pDialog;

    private static String url_all_orders = "http://10.0.2.2:8383/test/orders/get_all_orders.php";
    private static String url_order_details = "http://10.0.2.2/test/orders/get_order_details.php";
    private static String url_create_order = "http://10.0.2.2:8383/test/orders/create_order.php";
    private static String url_delete_order = "http://10.0.2.2/test/orders/delete_order.php";
    private static String url_update_order = "http://10.0.2.2/test/orders/update_order.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_ORDERS = "orders";
    private static final String TAG_ORDER_ID = "order_id";
    private static final String TAG_USER_ID = "user_id";
    private static final String TAG_CAR_ID = "car_id";
    private static final String TAG_START_DATE = "start_date";
    private static final String TAG_END_DATE = "end_date";

    private boolean availableServer = true;

    private JSONArray orders = null;
    private JSONParser jParser = new JSONParser();

    private List<Order> ordersList = new ArrayList<>();

    public OrderTools(Context context) {
        this.context = context;
        new LoadAllOrders().execute();
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
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                start = dateFormat.parse(dateFormat.format(start));
                end = dateFormat.parse(dateFormat.format(end));


                String result = null;
                try {
                    result = new CreateOrder().execute(String.valueOf(user.getUserId()), String.valueOf(car.getCarId()), dateFormat.format(start), dateFormat.format(end)).get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                new LoadAllOrders().execute();

                for (Order tmpOrder : ordersList) {
                    if (tmpOrder.getCar().getCarId() == car.getCarId()
                            && tmpOrder.getUser().getUserId() == user.getUserId()
                            && tmpOrder.getStartDate().compareTo(start) == 0
                            && tmpOrder.getEndDate().compareTo(end) == 0) {
                        order = tmpOrder;
                        break;
                    }

                }

                if (result.equals(UserTools.Result.SUCCESS.getValue())) {
                    Toast.makeText(context, "Nowe zamówienie złożone!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "Upss! Coś poszło nie tak!", Toast.LENGTH_LONG).show();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        return order;
    }

    public List<Order> findOrderByUser(User user) {
        return findOrdersByUserId(user != null ? user.getUserId() : -1);
    }

    public List<Order> findOrdersByUserId(int userId) {
        new LoadAllOrders().execute();
        List<Order> userOrders = new ArrayList<>();
        if (userId > -1) {
            for (Order tmpOrder : ordersList) {
                if (tmpOrder.getUser() != null && tmpOrder.getUser().getUserId() == userId) {
                    userOrders.add(tmpOrder);
                }
            }
        }
        return userOrders;
    }

    public Order findOrderById(long orderID) {
        new LoadAllOrders().execute();
        Order order = null;

        if (orderID > -1) {
            for (Order tmpOrder : ordersList) {
                if (tmpOrder.getOrderId() == orderID) {
                    order = tmpOrder;
                    break;
                }
            }
        }

        return order;
    }

    class CreateOrder extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Wczytywanie listy samochodów. Proszę czekać...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * Creating product
         */
        protected String doInBackground(String... args) {
            if (args.length < 4) {
                return UserTools.Result.FAILED.getValue();
            }

            String userId = args[0];
            String carId = args[1];
            String startDate = args[2];
            String endDate = args[3];

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(TAG_USER_ID, userId));
            params.add(new BasicNameValuePair(TAG_CAR_ID, carId));
            params.add(new BasicNameValuePair(TAG_START_DATE, startDate));
            params.add(new BasicNameValuePair(TAG_END_DATE, endDate));


            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jParser.makeHttpRequest(url_create_order,
                    "POST", params);

            if (json == null) {
                availableServer = false;
                return UserTools.Result.FAILED.getValue();
            }
            availableServer = true;

            // check log cat fro response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    return UserTools.Result.SUCCESS.getValue();
                } else {
                    return UserTools.Result.FAILED.getValue();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            finally {
                pDialog.dismiss();
            }

            return UserTools.Result.FAILED.getValue();
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
        }

    }

    class LoadAllOrders extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Wczytywanie listy samochodów. Proszę czekać...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All products from url
         */
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_all_orders, "GET", params);

            if (json == null) {
                availableServer = false;
                return null;
            }
            availableServer = true;

            // Check your log cat for JSON reponse
            Log.d("All users: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // users found
                    // Getting Array of Products
                    orders = json.getJSONArray(TAG_ORDERS);
                    ordersList = new ArrayList<>();

                    // looping through All Products
                    for (int i = 0; i < orders.length(); i++) {
                        JSONObject c = orders.getJSONObject(i);

                        // Storing each json item in variable
                        int orderId = c.getInt(TAG_ORDER_ID);
                        String startDate = c.getString(TAG_START_DATE);
                        String endDate = c.getString(TAG_END_DATE);

                        JSONObject userJSON = c.getJSONArray("user").getJSONObject(0);
                        JSONObject carJSON = c.getJSONArray("car").getJSONObject(0);

                        User tmpUser = new User(userJSON.getInt("user_id"), userJSON.getString("user_name"), userJSON.getString("user_email"), userJSON.getString("user_password"));
                        Car tmpCar = new Car(carJSON.getInt("car_id"), carJSON.getString("car_name"), new BigDecimal(carJSON.getString("car_price"))); //int carId, String carName, BigDecimal carPrice

                        ordersList.add(new Order(orderId, tmpUser, tmpCar, new SimpleDateFormat("yyyy-MM-dd").parse(startDate), new SimpleDateFormat("yyyy-MM-dd").parse(endDate))); //int orderId, User user, Car car, Date startDate, Date endDate
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            finally {
                pDialog.dismiss();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();

        }
    }
}
