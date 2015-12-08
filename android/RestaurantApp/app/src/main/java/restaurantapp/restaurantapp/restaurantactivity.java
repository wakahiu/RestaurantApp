package restaurantapp.restaurantapp;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class restaurantactivity extends ListActivity {
    //
    Bundle passthis2menu = new Bundle();
    //progress dialog for testing purposes
    private ProgressDialog pDialog;
    // server URL
    private static String url = Util.rootUrl + "/restaurant";
    // JSON Node names
    private static final String TAG_RESTAURANTS = "restaurants";
    private static final String TAG_ID = "_id";
    private static final String TAG_NAME = "displayName";
    private static final String TAG_ADDRESS = "address";
    private static final String TAG_CITY = "city";
    private static final String TAG_CUISINE = "cuisine";
    // restaurants JSONArray
    JSONArray restaurants = null;
    // Hashmap for ListView
    ArrayList<HashMap<String, String>> restaurantlist;
    // String
    String extra1,chosenrestaurantname;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurantlistlayout);

        // pull things from Intent and putExtra
        Intent intent=this.getIntent();
        if (intent != null){
            extra1 = getIntent().getStringExtra("currentuseremail");
        }
        //components for testing purposes
        restaurantlist = new ArrayList<HashMap<String, String>>();
        ListView lv = getListView();

        // Listview on item click listener
        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                chosenrestaurantname = ((TextView) view.findViewById(R.id.name))
                        .getText().toString();
                //
                Log.d("chosenrestaurantname",chosenrestaurantname);
                //
                passthis2menu.putString("chosenrestaurant",chosenrestaurantname);
                Log.d("resta name",chosenrestaurantname);
                Intent restaurant2menuintent = new Intent(getApplicationContext(), MenuActivity.class);
                restaurant2menuintent.putExtras(passthis2menu);
                startActivity(restaurant2menuintent);
                finish();

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
                        JSONObject restaurantinfo = restaurants.getJSONObject(i);

                        // Restaurant JSON object
                        String restaurantid = restaurantinfo.getString(TAG_ID);
                        String restaurantname = restaurantinfo.getString(TAG_NAME);
                        String address = restaurantinfo.getString(TAG_ADDRESS);
                        String city = restaurantinfo.getString(TAG_CITY);
                        String cuisine = restaurantinfo.getString(TAG_CUISINE);

                        // tmp hashmap for single restaurant
                        HashMap<String, String> user_tmpmat = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        user_tmpmat.put(TAG_ID, restaurantid);
                        user_tmpmat.put(TAG_NAME, restaurantname);
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