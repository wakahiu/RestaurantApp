package restaurantapp.restaurantapp;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class shoppingbasketactivity extends ListActivity {
    // progress dialog is initiated while fetching data from server
    private ProgressDialog pDialog;

    // server URL
    private static String url = "http://dinnermate.azurewebsites.net/api/v1.0/order";

    // JSONArrays
    JSONArray orderIDArray = null;

    // JSONObjs
    JSONObject IDfoundObj;

    // Hashmap for ListView
    ArrayList<HashMap<String, String>> sbasketlist;

    // Strings
    String TAG_ID = "_id";
    String ID2look4 = "5658c6a7496fdda406d59f13";
    String IDfoundObjtxt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shoppingbasketlayout);

        new GetShoppingBasket().execute();
    }

    /** Async task class to get json by making HTTP call**/
    private class GetShoppingBasket extends AsyncTask<Void, Void, Void> {

        /*@Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog as data is fetched
            pDialog = new ProgressDialog(shoppingbasketactivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }*/

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

            Log.e("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    // initialize a JSONObject from the GET response entity
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // search within found JSONObject to get the array with (specific name)
                    orderIDArray = jsonObj.getJSONArray(TAG_ID);

                    for (int i = 0; i < orderIDArray.length(); i++) {
                        IDfoundObj = orderIDArray.getJSONObject(i);
                        IDfoundObjtxt = IDfoundObj.toString();

                        if (ID2look4 == IDfoundObjtxt) {
                            JSONObject ordereditemslist = IDfoundObj.getJSONObject("menuItems");
                            String oitemname = ordereditemslist.getString("name");
                            String oitemprice = ordereditemslist.getString("price");

                            // tmp hashmap for this specific order
                            HashMap<String,String> order_tmpmap = new HashMap<String, String>();

                            // add value to each key
                            order_tmpmap.put("name", oitemname);
                            order_tmpmap.put("price", oitemprice);

                            // add order_tmpmap to sbasket master list
                            sbasketlist.add(order_tmpmap);
                            }
                        }
                    /*
                    // loop through the found array to add objects as a shoppingbasket
                    for (int i = 0; i < ordersArray.length(); i++) {
                        JSONObject shoppingbasket = ordersArray.getJSONObject(i);

                        // Restaurant JSON object
                        String restaurantid = shoppingbasket.getString(TAG_ID);
                        String name = shoppingbasket.getString(TAG_NAME);

                        // tmp hashmap for single restaurant
                        HashMap<String, String> order_tmpmat = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        order_tmpmat.put(TAG_ID, restaurantid);
                        order_tmpmat.put(TAG_NAME, name);

                        // adding menu_tmpmat to menulist list
                        orderlist.add(order_tmpmat);

                    }*/
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
            // Updating parsed JSON data into ListView
            ListAdapter adapter = new SimpleAdapter(
                    shoppingbasketactivity.this, sbasketlist,

                    R.layout.sbasketlistlayout, new String[] {"name", "price"},
                    new int[] { R.id.foodname, R.id.foodprice}
            );

            setListAdapter(adapter);
        }
    }


}
