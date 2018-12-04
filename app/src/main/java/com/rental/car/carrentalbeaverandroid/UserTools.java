package com.rental.car.carrentalbeaverandroid;

import org.apache.http.NameValuePair;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.rental.car.carrentalbeaverandroid.models.User;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class UserTools {

    private Context context;

    // Progress Dialog
    private ProgressDialog pDialog;

    //IP 10.0.2.2 is localhost for android emulator
    //:8383 is the custom port
    private static String url_all_users = "http://10.0.2.2/test/users/get_all_users.php";
    private static String url_user_details = "http://10.0.2.2/test/users/get_user_details.php";
    private static String url_create_user = "http://10.0.2.2/test/users/create_user.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_USERS = "users";
    private static final String TAG_USER_ID = "user_id";
    private static final String TAG_USER_NAME = "user_name";
    private static final String TAG_USER_EMAIL = "user_email";
    private static final String TAG_USER_PASSWORD = "user_password";

    private boolean availableServer = true;

    private JSONArray users = null;
    JSONParser jParser = new JSONParser();

    List<User> usersList = new ArrayList<>();


    public UserTools(Context context) {

        this.context = context;
        new LoadAllUsers().execute();
    }

    public Context getContext() {
        return context;
    }

    private boolean isAvailableServer() {
        if(!availableServer) {
            Toast.makeText(context, "Serwer niedostepny. Sprawdź połączenie z internetem.",
                    Toast.LENGTH_LONG).show();
        }
        return availableServer;
    }

    public User addNewUser(String name, String email, String password) {
        User user = null;

        if(!isAvailableServer())
            return user;

        if (!email.isEmpty() && !password.isEmpty()) {
            password = User.hashPassword(password);

            String result = null;
            try {
                result = new CreateUsers().execute(name, email, password).get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (result.equals(Result.SUCCESS.getValue())) {
                new LoadAllUsers().execute();
                user = findUserByLoginAndPassword(email, password);
                if (user != null)
                    usersList.add(user);
            }
        }
        return user;
    }

    public List<User> getAllUsers() {
        isAvailableServer();
        return usersList;
    }

    public User findUserById(int userId) {
        User user = null;

        if(!isAvailableServer())
            return user;

        if (usersList != null && !usersList.isEmpty()) {
            for (User temp : usersList) {
                if (temp.getUserId() == userId) {
                    user = temp;
                }
            }

        }
        return user;
    }

    public User findUserByLoginAndPassword(String login, String password) {
        User user = null;

        if(!isAvailableServer())
            return user;

        if (usersList != null && !usersList.isEmpty()) {
            for (User temp : usersList) {
                if ((temp.getEmail().equals(login) || temp.getName().equals(login)) && temp.getPassword().equals(password)) {
                    user = temp;
                }
            }
        }
        return user;
    }


    class LoadAllUsers extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Loading users. Please wait...");
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
            JSONObject json = jParser.makeHttpRequest(url_all_users, "GET", params);

            if(json == null) {
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
                    users = json.getJSONArray(TAG_USERS);
                    usersList = new ArrayList<>();

                    // looping through All Products
                    for (int i = 0; i < users.length(); i++) {
                        JSONObject c = users.getJSONObject(i);

                        // Storing each json item in variable
                        int id = c.getInt(TAG_USER_ID);
                        String name = c.getString(TAG_USER_NAME);
                        String email = c.getString(TAG_USER_EMAIL);
                        String password = c.getString(TAG_USER_PASSWORD);

                        User user = new User(id, name, email, password);

                        usersList.add(user);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();

        }
    }


    class CreateUsers extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Creating User..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         */
        protected String doInBackground(String... args) {
            if (args.length < 3) {
                return Result.FAILED.getValue();
            }

            String name = args[0];
            String email = args[1];
            String password = args[2];

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("user_name", name));
            params.add(new BasicNameValuePair("user_email", email));
            params.add(new BasicNameValuePair("user_password", password));

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jParser.makeHttpRequest(url_create_user,
                    "POST", params);

            if(json == null) {
                availableServer = false;
                return Result.FAILED.getValue();
            }
            availableServer = true;

            // check log cat fro response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    return Result.SUCCESS.getValue();
                } else {
                    return Result.FAILED.getValue();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return Result.FAILED.getValue();
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
        }

    }

    private enum Result {
        SUCCESS("Success"),
        FAILED("Failed");

        private final String value;

        Result(final String whatResult) {
            value = whatResult;
        }

        public String getValue() {
            return value;
        }
    }
}




