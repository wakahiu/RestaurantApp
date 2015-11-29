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
import java.util.Iterator;

public class shoppingbasketactivity extends ListActivity {
    // progress dialog is initiated while fetching data from server
    private ProgressDialog pDialog;

    // server URL
    private static String url = "http://dinnermate.azurewebsites.net/api/v1.0/order";

    // JSONArrays
    JSONArray orderIDArray = null;
    JSONArray IDmenuitems = null;
    // JSONObjs
    JSONObject IDfoundObj;

    // Hashmap for ListView
    ArrayList<HashMap<String, String>> sbasketlist;
    // tmp hashmap for this specific order
    HashMap<String, String> order_tmpmap = new HashMap<String, String>();
    // Strings
    String ID2look4 = "565a9ff2dcdbac2015e7b84a";
    String IDfoundObjtxt, compareresult;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shoppingbasketlayout);

        new GetShoppingBasket().execute();
    }

    /**
     * Async task class to get json by making HTTP call
     **/
    private class GetShoppingBasket extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog as data is fetched
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

            Log.e("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    // initialize a JSONObject from the GET response entity
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // search within found JSONObject to get the array with (specific name)
                    orderIDArray = jsonObj.getJSONArray("orders");

                    for (int i = 0; i < orderIDArray.length(); i++) {
                        IDfoundObj = orderIDArray.getJSONObject(i);
                        //
                        Log.d("1stArray", orderIDArray.toString());
                        //
                        IDfoundObjtxt = IDfoundObj.get("_id").toString();
                        //
                        Log.d("1stArrayString", IDfoundObjtxt);
                        //
                        Log.d("ArrayStringCompare", ID2look4);
                        //
                        if (IDfoundObjtxt == ID2look4) {
                            compareresult = "true";
                        }
                    }
                    if (compareresult == "true") {
                        IDmenuitems = IDfoundObj.getJSONArray("menuItems");
                        //
                        Log.d("2ndArray", IDmenuitems.toString());
                        //
                        for (int j = 0; j < IDmenuitems.length(); j++) {
                            JSONObject menuitemobjs = IDmenuitems.getJSONObject(j);
                            //
                            Log.d("1stObj", menuitemobjs.toString());
                            //
                            String oitemname = menuitemobjs.getString("name");
                            String oitemprice = menuitemobjs.getString("price");

                            // add value to each key
                            order_tmpmap.put("name", oitemname);
                            order_tmpmap.put("price", oitemprice);

                            // add order_tmpmap to sbasket master list
                            sbasketlist.add(order_tmpmap);
                        }
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
                    shoppingbasketactivity.this, sbasketlist,

                    R.layout.sbasketlistlayout, new String[]{"name", "price"},
                    new int[]{R.id.foodname, R.id.foodprice}
            );
            setListAdapter(adapter);
        }
    }


}
