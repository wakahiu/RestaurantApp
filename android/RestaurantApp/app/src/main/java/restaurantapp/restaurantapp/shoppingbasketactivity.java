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
    private ProgressDialog pDialog; //progress dialog for testing purposes

    // server URL
    private static String url = "";

    // restaurants JSONArray & orders JSONArray
    JSONArray orderArray = null;

    // Hashmap for ListView
    ArrayList<HashMap<String, String>> orderlist;

    /*private String TAG_FOODNAME = "name4food";
    private String TAG_FOODPRICE = "price4food";
    // users JSONArray
    JSONArray sbasketorder = null;
    int counterint;
    ArrayList<HashMap<String, String>> sbasketarray;
    String counterstring, chosenfoodname, chosenfoodprice;*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shoppingbasketlayout);

        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //counterstring = prefs.getString("countervalue",null);
        //chosenfoodname = prefs.getString("foodnamekey", null);
        //chosenfoodprice = prefs.getString(sbasketfoodprice, null);
        //sbasketarray = new ArrayList<HashMap<String, String>>();
        //sbasketarray.clear();
        // tmp hashmap for sbasket
        /*HashMap<String, String> sbasket_tmpmat = new HashMap<String, String>();
        // adding each child node to HashMap key => value
        sbasket_tmpmat.put("counter",counterstring);
        sbasket_tmpmat.put(TAG_FOODNAME, chosenfoodname);
        //sbasket_tmpmat.put(TAG_FOODPRICE, chosenfoodprice);

        sbasketarray.add(sbasket_tmpmat);
        ListAdapter adapter = new SimpleAdapter(shoppingbasketactivity.this, sbasketarray,
                R.layout.sbasketlistlayout, new String[] {"counter", TAG_FOODNAME},
                new int[] { R.id.foodname, R.id.foodprice}
        );
        setListAdapter(adapter);*/

        //new GetShoppingBasket().execute();
    }

    /** Async task class to get json by making HTTP call**/
    /*private class GetShoppingBasket extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(shoppingbasketactivity.this);
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
                    orderArray = jsonObj.getJSONArray(TAG_MENU);

                    // looping through All users
                    for (int i = 0; i < orderArray.length(); i++) {
                        JSONObject user = orderArray.getJSONObject(i);

                        // Restaurant JSON object
                        String restaurantid = user.getString(TAG_ID);
                        String name = user.getString(TAG_NAME);

                        // tmp hashmap for single restaurant
                        HashMap<String, String> order_tmpmat = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        order_tmpmat.put(TAG_ID, restaurantid);
                        order_tmpmat.put(TAG_NAME, name);

                        // adding menu_tmpmat to menulist list
                        orderlist.add(order_tmpmat);
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
            // Updating parsed JSON data into ListView
            ListAdapter adapter = new SimpleAdapter(
                    shoppingbasketactivity.this, orderlist,

                    R.layout.sbasketlistlayout, new String[] { TAG_NAME, TAG_PRICE},
                    new int[] { R.id.foodname, R.id.foodprice}
            );

            setListAdapter(adapter);
        }
    }*/


}
