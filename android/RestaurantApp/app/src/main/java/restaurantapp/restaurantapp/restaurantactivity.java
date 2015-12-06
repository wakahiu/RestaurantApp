package restaurantapp.restaurantapp;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class restaurantactivity extends ListActivity {
    // pull things from Intent and putExtra
    Intent intent=this.getIntent();

    //progress dialog for testing purposes
    private ProgressDialog pDialog;
    // server URL
    private static String url = "http://dinnermate.azurewebsites.net/api/v1.0/restaurant";
    // JSON Node names
    private static final String TAG_RESTAURANTS = "restaurants";
    private static final String TAG_ID = "_id";
    private static final String TAG_NAME = "name";
    private static final String TAG_ADDRESS = "address";
    private static final String TAG_CITY = "city";
    private static final String TAG_CUISINE = "cuisine";
    // restaurants JSONArray
    JSONArray restaurants = null;
    // Hashmap for ListView
    ArrayList<HashMap<String, String>> restaurantlist;
    // String
    String extra1, currentuseremail;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (intent != null){
            extra1 = getIntent().getStringExtra("currentuseremail");
        }

        setContentView(R.layout.restaurantlistlayout);

        //components for testing purposes
        restaurantlist = new ArrayList<HashMap<String, String>>();
        ListView lv = getListView();

        // Listview on item click listener
        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            /*@Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // getting values from selected ListItem
                String firstname = ((TextView) view.findViewById(R.id.firstname)).getText().toString();
                String lastname = ((TextView) view.findViewById(R.id.lastname)).getText().toString();
                String email = ((TextView) view.findViewById(R.id.email)).getText().toString();

                // Starting single contact activity
                Intent in = new Intent(getApplicationContext(),SingleContactActivity.class);
                in.putExtra(TAG_FIRSTNAME, firstname);
                in.putExtra(TAG_LASTNAME, lastname);
                in.putExtra(TAG_EMAIL, email);
                startActivity(in);*/

                Intent restaurantlistintent = new Intent(getApplicationContext(), menuactivity.class);
                startActivity(restaurantlistintent);

            }
        });

        // Calling async task to get json
        new GetRestaurants().execute();
    }

    /** Async task class to get json by making HTTP call**/
    private class GetRestaurants extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(restaurantactivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    restaurants = jsonObj.getJSONArray(TAG_RESTAURANTS);

                    // looping through All users
                    for (int i = 0; i < restaurants.length(); i++) {
                        JSONObject user = restaurants.getJSONObject(i);

                        // Restaurant JSON object
                        String restaurantid = user.getString(TAG_ID);
                        String name = user.getString(TAG_NAME);
                        String address = user.getString(TAG_ADDRESS);
                        String city = user.getString(TAG_CITY);
                        String cuisine = user.getString(TAG_CUISINE);

                        // tmp hashmap for single restaurant
                        HashMap<String, String> user_tmpmat = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        user_tmpmat.put(TAG_ID, restaurantid);
                        user_tmpmat.put(TAG_NAME, name);
                        user_tmpmat.put(TAG_ADDRESS, address);
                        user_tmpmat.put(TAG_CITY, city);
                        user_tmpmat.put(TAG_CUISINE,cuisine);

                        // adding user_tmpmat to testuserlist list
                        restaurantlist.add(user_tmpmat);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /** Updating parsed JSON data into ListView **/
            ListAdapter adapter = new SimpleAdapter(
                    restaurantactivity.this, restaurantlist,

                    R.layout.restaurantitemlist, new String[] { TAG_NAME, TAG_ADDRESS, TAG_CITY, TAG_CUISINE },
                    new int[] { R.id.name, R.id.address, R.id.city, R.id.cuisine}
            );

            setListAdapter(adapter);
        }

    }

}