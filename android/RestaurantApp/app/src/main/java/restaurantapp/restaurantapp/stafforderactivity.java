package restaurantapp.restaurantapp;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class stafforderactivity extends ListActivity {
    // progress dialog is initiated while fetching data from server
    private ProgressDialog pDialog;

    // server URL
    private static String url = "http://dinnermate.azurewebsites.net/api/v1.0/order";

    // JSONArrays
    JSONArray orderIDArray = null;
    JSONArray IDmenuitems = null;

    // JSONObjs
    JSONObject IDfoundObj;
    JSONObject IDorder;

    // Hashmap for ListView
    ArrayList<HashMap<String, String>> sbasketlist = new ArrayList<HashMap<String, String>>();
    // tmp hashmap for this specific order
    HashMap<String, String> order_tmpmap = new HashMap<String, String>();
    // Strings
    String ID2look4 = "";
    String counterlimit = null;

    String IDfoundObjtxt, oitemname, oitemprice;
    // int
    int index = 0;
    int counterint = 0;


    String emailtxt;

//    FloatingActionButton staffback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stafforderlayout);

        emailtxt = getIntent().getExtras().getString("email");
        new GetShoppingBasket().execute();

//        staffback = (FloatingActionButton)findViewById(R.id.staffback);
//        staffback.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent backtologinintent = new Intent(stafforderactivity.this, loginactivity.class);
//                startActivity(backtologinintent);
//                finish();
//            }
//        });
    }

    /**
     * Async task class to get json by making HTTP call
     **/
    private class GetShoppingBasket extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog as data is fetched
            pDialog = new ProgressDialog(stafforderactivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();


            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("email", emailtxt));

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET, nameValuePairs );

            Log.e("Response: ", "> " + jsonStr);

//            if ( counterlimit != null )
//                counterint = Integer.parseInt(counterlimit);

            if (jsonStr != null) {
                try { //counterint = 10;
//                    for (int jk = 1; jk < counterint + 1; jk++) {
//                        ID2look4 = getIntent().getStringExtra("chosenorderID" + jk);
                        //
                        Boolean compareresult = false;
                        // initialize a JSONObject from the GET response entity
                        JSONObject jsonObj = new JSONObject(jsonStr);

                        // search within found JSONObject to get the array with (specific name)
                        orderIDArray = jsonObj.getJSONArray("orders");
                        //
                        Log.d("orderID", orderIDArray.toString());
                        //
                        for (int i = 0; i < orderIDArray.length(); i++) {
                            IDfoundObj = orderIDArray.getJSONObject(i);
                            //
                            Log.d("orderID Array", String.valueOf(orderIDArray.length()));
                            //
                            IDfoundObjtxt = IDfoundObj.get("_id").toString();

                            //
                            Log.d("orderID Array as String", IDfoundObjtxt);
                            Log.d("ArrayString2Compare", ID2look4 + "");
                            //
                            if (IDfoundObjtxt.equals(ID2look4)) {
                                compareresult = true;
                                index = i;
                            }
                        }
                        // if desired ID object is found
//                        if (compareresult.equals(true)) {
                            IDorder = orderIDArray.getJSONObject(index);
                            IDmenuitems = IDorder.getJSONArray("menuItems");
                            //IDmenuitems = IDfoundObj.getJSONArray("menuItems");
                            //
                            Log.d("ID order Obj", IDorder.toString());
                            Log.d("ID menuitem array", IDmenuitems.toString());
                            //

                            for (int j = 0; j < IDmenuitems.length(); j++) {
                                JSONObject menuitemobjs = IDmenuitems.getJSONObject(j);
                                //
                                Log.d("menuItems Obj", menuitemobjs.toString());
                                //
                                oitemname = menuitemobjs.get("name").toString();
                                oitemprice = menuitemobjs.get("price").toString();
                                //
                                Log.d("oitemname", oitemname);
                                Log.d("oitemprice", oitemprice);
                                // add value to each key
                                order_tmpmap.put("name", oitemname);
                                order_tmpmap.put("price", oitemprice);
                                // refresh total price of the order
                                // add order_tmpmap to sbasket master list
                                sbasketlist.add(order_tmpmap);
                                //
                                Log.d("sbasketlist", sbasketlist.toString());
                            }
//                        }
//                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } // catch JSONException here

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
                    stafforderactivity.this, sbasketlist,

                    R.layout.stafforderlistlayout, new String[]{"name", "price"},


//                    procedded.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            ordertotalcosttxt = ordertotalcost.getText().toString();
//
//                            if (ordertotalcosttxt.equals("0.00"))
//                            {
//                                Toast.makeText(shoppingbasketactivity.this, "Please add order!", Toast.LENGTH_SHORT).show();
//                            } else {
//                                Intent purchaseintent = new Intent(shoppingbasketactivity.this, paymentactivity.class);
//                                startActivity(purchaseintent);
//                                finish();
//                            }
//                        }
//                    });


                    new int[]{R.id.foodname, R.id.foodprice}

            );

            setListAdapter(adapter);
        }
    }


}
