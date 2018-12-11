package com.rental.car.carrentalbeaverandroid.tools;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.rental.car.carrentalbeaverandroid.models.Car;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class CarTools {
    private Context context;

    //IP 10.0.2.2 is localhost for android emulator

    private static String url_all_cars = "http://carbeaver.hekko24.pl/test/cars/get_all_cars.php";
    private static String url_car_details = "http://carbeaver.hekko24.pl/test/cars/get_car_details.php";
    private static String url_create_car = "http://carbeaver.hekko24.pl/test/cars/create_car.php";
    private static String url_delete_car = "http://carbeaver.hekko24.pl/test/cars/delete_car.php";
    private static String url_update_car = "http://carbeaver.hekko24.pl/test/cars/update_car.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_CARS = "cars";
    private static final String TAG_CAR_ID = "car_id";
    private static final String TAG_CAR_NAME = "car_name";
    private static final String TAG_CAR_PRICE = "car_price";

    private boolean availableServer = true;

    private JSONArray cars = null;
    private JSONParser jParser = new JSONParser();

    private List<Car> carsList = new ArrayList<>();

    public CarTools(Context context) {
        this.context = context;
        new LoadAllCars().execute();
    }

    private boolean isAvailableServer() {
        if (!availableServer) {
            Toast.makeText(context, "Serwer niedostepny. Sprawdź połączenie z internetem.",
                    Toast.LENGTH_LONG).show();
        }
        return availableServer;
    }


    public Car addNewCar(String carName, BigDecimal carPrice) {
        Car car = null;
        if (!isAvailableServer())
            return car;

        if (!carName.isEmpty() && carPrice != null && carPrice.compareTo(new BigDecimal("0.00")) > 0) {

            String result = null;
            try {
                result = new CreateCars().execute(carName, carPrice.toString()).get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (result.equals(UserTools.Result.SUCCESS.getValue())) {
                new LoadAllCars().execute();

                for (Car tmpCar : carsList) {
                    if (tmpCar.getCarName().equals(carName) && tmpCar.getCarPrice().compareTo(carPrice) == 0) {
                        car = tmpCar;
                    }
                }
            }
        }
        return car;
    }

    public Car findCarById(int rowId) {
        Car car = null;

        if (!isAvailableServer())
            return car;

        if (rowId > -1) {
            for (Car tmpCar : carsList)
                if (tmpCar.getCarId() == rowId) {
                    car = tmpCar;
                    break;
                }
        }

        return car;
    }

    public List<Car> getAllCars() {
        isAvailableServer();
        return carsList;
    }

    class LoadAllCars extends AsyncTask<String, String, String> {
        // Progress Dialog
        private ProgressDialog pDialog;

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = ProgressDialog.show(context, "Proszę czekać","Wczytywanie listy samochodów");
        }

        /**
         * getting All cars from url
         */
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_all_cars, "GET", params);

            if (json == null) {
                availableServer = false;
                return UserTools.Result.FAILED.getValue();
            }
            availableServer = true;

            // Check your log cat for JSON reponse
            Log.d("All cars: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // users found
                    // Getting Array of Products
                    cars = json.getJSONArray(TAG_CARS);
                    carsList = new ArrayList<>();

                    // looping through All Products
                    for (int i = 0; i < cars.length(); i++) {
                        JSONObject c = cars.getJSONObject(i);

                        // Storing each json item in variable
                        int id = c.getInt(TAG_CAR_ID);
                        String name = c.getString(TAG_CAR_NAME);
                        String price = c.getString(TAG_CAR_PRICE);

                        Car car = new Car(id, name, new BigDecimal(price));

                        carsList.add(car);
                    }

                    return UserTools.Result.SUCCESS.getValue();
                } else {
                    return UserTools.Result.FAILED.getValue();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return UserTools.Result.FAILED.getValue();
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();

        }
    }


    class CreateCars extends AsyncTask<String, String, String> {
        // Progress Dialog
        private ProgressDialog pDialog;
        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = ProgressDialog.show(context, "Proszę czekać","Tworzenie nowego samochodu.");
        }

        /**
         * Creating car
         */
        protected String doInBackground(String... args) {
            if (args.length < 2) {
                return UserTools.Result.FAILED.getValue();
            }

            String name = args[0];
            String price = args[1];

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(TAG_CAR_NAME, name));
            params.add(new BasicNameValuePair(TAG_CAR_PRICE, price));

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jParser.makeHttpRequest(url_create_car,
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
}
