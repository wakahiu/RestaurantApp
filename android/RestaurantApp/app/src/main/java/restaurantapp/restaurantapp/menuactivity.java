package restaurantapp.restaurantapp;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class menuactivity extends ListActivity {
    // initialize bundle that will be sent to the basket
    Bundle passthis2basket = new Bundle();

    // fab initialization
    FloatingActionButton fab, fab2goback; // floating action button
    // Progress dialog
    private ProgressDialog pDialog; //progress dialog for testing purposes
    // server URL
    private static String menuUrlStr = Util.rootUrl + "/menu";
    private static String orderUrlStr = Util.rootUrl + "/order";

    private static URL menuUrl = null;
    private static URL orderURL = null;

    // JSON Node names
    private static final String TAG_MENU = "menuItems";
    private static final String TAG_ID = "_id";
    private static final String TAG_NAME = "name";
    private static final String TAG_PRICE = "price";
    private static final String TAG_CUISINE = "cuisine";
    private static final String TAG_RESTAURANT = "restaurant";

    // restaurants JSONArray & orders JSONArray
    JSONArray menuArray = null;
    // Hashmap for ListView
    ArrayList<HashMap<String, String>> menulist;
    // orderresponsecode initialization
    Integer orderresponsecode;
    // String Initialization
    String chosenfoodname,chosenfoodprice, chosenfoodid, extrarestaurantID,extrarestaurantname, orderId, jtxt;
    // TextView
    TextView menumaintitle;
    // Intent
    Intent menu2sbasketintent;
    // int
    int j = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menulistlayout);
        // new intent for going over to sbasket
        menu2sbasketintent = new Intent(getApplicationContext(), shoppingbasketactivity.class);
        // pull things from Intent that had putExtra
        Intent fromrestaurantintent = this.getIntent();
        // get the value of key from intent's extra
        if (fromrestaurantintent != null){
                extrarestaurantID = getIntent().getStringExtra("chosenrestaurantID"); // chosenrestaurant is the name of the restaurant chosen by the user
                extrarestaurantname = getIntent().getStringExtra("chosenrestaurant");
            Log.d("extra resta ID", extrarestaurantID);
            Log.d("extra resta name",extrarestaurantname);
        }
        // find menu title
        menumaintitle = (TextView)findViewById(R.id.mainmenutitle);
        menumaintitle.setText(extrarestaurantname);

        // find the fab in the layout
        fab = (FloatingActionButton)findViewById(R.id.fab);
        fab2goback = (FloatingActionButton)findViewById(R.id.fab2goback);

        // set on click action for fab
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(menu2sbasketintent);
                j = 1;
                finish();
            }
        });
        // set on click action for fab2goback
        fab2goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backtorestaurantintent = new Intent(menuactivity.this, restaurantactivity.class);
                startActivity(backtorestaurantintent);
                finish();
            }
        });

        //initialize menu list and get list view
        menulist = new ArrayList<HashMap<String, String>>();
        ListView lv = getListView();

        // Listview on item click listener
        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem to HTTP POST
                chosenfoodname = ((TextView) view.findViewById(R.id.foodname))
                        .getText().toString();
                chosenfoodprice = ((TextView) view.findViewById(R.id.foodprice))
                        .getText().toString();
                chosenfoodid = ((TextView) view.findViewById(R.id.foodid))
                        .getText().toString();

                new PostOrders().execute();
            }
        });

        // Calling async task to get json
        new GetRestaurantsMenu().execute();
    }

    /** Async task class to get json by making HTTP call**/
    private class GetRestaurantsMenu extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(menuactivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            String logPrefix = getClass().getEnclosingClass().getName();
            HttpURLConnection urlConnection = null;
            try {

                menuUrl = new URL(menuUrlStr);
                urlConnection = (HttpURLConnection) menuUrl.openConnection();
                urlConnection.setDoInput(true);
                urlConnection.setUseCaches(false);
                urlConnection.setAllowUserInteraction(false);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setChunkedStreamingMode(0);
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                StringBuffer sb = new StringBuffer();

                // if there is a response code AND that response code is 200 OK, do
                // stuff in the first if block
                orderresponsecode = urlConnection.getResponseCode();
                //
                Log.d(logPrefix + " Order Response code ", orderresponsecode.toString());
                //
                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                    InputStream is = new BufferedInputStream(urlConnection.getInputStream());
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    String inputLine = "";
                    //
                    while ((inputLine = br.readLine()) != null) {
                        sb.append(inputLine);
                    }
                    String result = sb.toString();

                    JSONObject jasonResultObject = new JSONObject(result);
                    JSONArray jasonMenuList = jasonResultObject.getJSONArray(TAG_MENU);

                    for(int n = 0; n < jasonMenuList.length(); n++)
                    {
                        JSONObject menuItemObject = jasonMenuList.getJSONObject(n);

                        String restaurant = menuItemObject.getString(TAG_RESTAURANT);
                        Log.d(logPrefix, extrarestaurantID);

                        if (restaurant.equals(extrarestaurantID)) {
                            // Restaurant JSON object
                            String foodid = menuItemObject.getString(TAG_ID);
                            String name = menuItemObject.getString(TAG_NAME);
                            String price = menuItemObject.getString(TAG_PRICE);
                            String cuisine = menuItemObject.getString(TAG_CUISINE);

                            HashMap<String, String> menu_tmpmat = new HashMap<String, String>();

                            // adding each child node to HashMap key => value
                            menu_tmpmat.put(TAG_ID, foodid);
                            menu_tmpmat.put(TAG_NAME, name);
                            menu_tmpmat.put(TAG_PRICE, price);
                            menu_tmpmat.put(TAG_CUISINE, cuisine);

                            // adding menu_tmpmat to menulist list
                            menulist.add(menu_tmpmat);

                            Log.d(logPrefix, restaurant);
                        }
                    }


                    // otherwise, if any other status code is returned, or no status
                    // code is returned, do stuff in the else block
                } else {
                    InputStream is = new BufferedInputStream(urlConnection.getErrorStream());
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    String inputLine = "";
                    //
                    while ((inputLine = br.readLine()) != null) {
                        sb.append(inputLine);
                    }
                    String result = sb.toString();
                    Log.d(logPrefix, result);

                    // Server returned HTTP error code description.
                    Log.w(logPrefix, "" + urlConnection.getResponseMessage());
                }

            } catch (MalformedURLException exception) {
                Log.e(logPrefix, exception.toString());
            } catch (IOException exception) {
                Log.e(logPrefix, exception.toString());
            } catch (JSONException exception) {
                Log.e(logPrefix, exception.toString());
            } finally {
                if (urlConnection != null ) {
                    urlConnection.disconnect();
                }
            }
            return null;
/*
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

            Log.d("Response: ", ">\n" + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    menuArray = jsonObj.getJSONArray(TAG_MENU);

                    // looping through All users
                    for (int i = 0; i < menuArray.length(); i++) {
                        JSONObject user = menuArray.getJSONObject(i);

                        // Restaurant JSON object
                        String foodid = user.getString(TAG_ID);
                        String name = user.getString(TAG_NAME);
                        String price = user.getString(TAG_PRICE);
                        String cuisine = user.getString(TAG_CUISINE);

                        // tmp hashmap for single restaurant
                        HashMap<String, String> menu_tmpmat = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        menu_tmpmat.put(TAG_ID, foodid);
                        menu_tmpmat.put(TAG_NAME, name);
                        menu_tmpmat.put(TAG_PRICE, price);
                        menu_tmpmat.put(TAG_CUISINE,cuisine);

                        // adding menu_tmpmat to menulist list
                        menulist.add(menu_tmpmat);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
            return null;*/
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /** Updating parsed JSON data into ListView **/
            ListAdapter adapter = new SimpleAdapter(
                    menuactivity.this, menulist,

                    R.layout.menuitemlist, new String[] { TAG_NAME, TAG_PRICE, TAG_CUISINE, TAG_ID },
                    new int[] { R.id.foodname, R.id.foodprice, R.id.cuisine, R.id.foodid}
            );

            setListAdapter(adapter);
        }
    }

    //Async task class to POST order json by making HTTP call
    private class PostOrders extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... arg0) {
            String logPrefix = getClass().getEnclosingClass().getName();
            HttpURLConnection urlConnection = null;
            try {

                final String username = "38KBYFLH2MID9SY55GI4FEXFB";
                final String password = "2eyLz5LKmB/AoD+g77gHOw6RRThLjucZblIq9cGlSno";
                // Unless paired with HTTPS, this is not a secure mechanism for user authentication.
                // In particular, the username, password, request and response are all
                // transmitted over the network without encryption.
                String userPassword = username + ":" + password;
                String basicAuth = Base64.encodeToString(userPassword.getBytes(), Base64.NO_WRAP);

                // Compose the JSON object.
                JSONObject jsonObj = new JSONObject();
                List<String> menuItemIdList = new ArrayList<String>();
                menuItemIdList.add(chosenfoodid);
                jsonObj.put("menuItemIdList", new JSONArray(menuItemIdList));
                Log.d(logPrefix, jsonObj.toString());

                orderURL = new URL(orderUrlStr);
                urlConnection = (HttpURLConnection) orderURL.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setUseCaches(false);
                urlConnection.setAllowUserInteraction(false);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestProperty("Authorization", "Basic " + basicAuth);
                urlConnection.setChunkedStreamingMode(0);
                urlConnection.setRequestMethod("POST");
                urlConnection.connect();

                // instantiate OutputStreamWriter using the output stream, returned
                // from getOutputStream, that writes to this connection.
                OutputStreamWriter writer = new OutputStreamWriter(
                        urlConnection.getOutputStream());

                writer.write(jsonObj.toString());

                writer.flush();
                writer.close();

                StringBuffer sb = new StringBuffer();
                InputStream is = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String inputLine = "";
                while ((inputLine = br.readLine()) != null) {
                    sb.append(inputLine);
                }
                String result = sb.toString();
                Log.d("Order result",result);

                // if there is a response code AND that response code is 200 OK, do
                // stuff in the first if block
                orderresponsecode = urlConnection.getResponseCode();
                //



                //
                Log.d("orderresponsecode",orderresponsecode.toString());
                Log.d(getClass().getEnclosingClass().getName(), urlConnection.getResponseMessage());
                //

                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_CREATED) {
                    // OK
                    JSONObject jasonResultObject = new JSONObject(result);
                    JSONObject jasonOrderObject = jasonResultObject.getJSONObject("order");

                    orderId = jasonOrderObject.getString("_id");

                    // log order ID for debugging
                    Log.d("order ID", orderId);

                    Log.d(logPrefix, urlConnection.getResponseMessage());
                    // otherwise, if any other status code is returned, or no status
                    // code is returned, do stuff in the else block
                } else {
                    // Server returned HTTP error code.
                    Log.w(logPrefix, "" + urlConnection.getResponseCode());
                }

            } catch (MalformedURLException exception) {
                Log.e(logPrefix, exception.toString());
            } catch (IOException exception) {
                Log.e(logPrefix, exception.toString());
            } catch (JSONException exception) {
                Log.e(logPrefix, exception.toString());
            } finally {
                if (urlConnection != null ) {
                    urlConnection.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if  (orderresponsecode.equals(201)) {
                passthis2basket.putString("chosenorderID"+j,orderId);
                jtxt = String.valueOf(j);
                passthis2basket.putString("jvalue", jtxt);
                menu2sbasketintent.putExtras(passthis2basket);
                //
                Log.d("passthis2basket",passthis2basket.toString());
                //
                Toast.makeText(menuactivity.this, j + " order(s) added in total", Toast.LENGTH_LONG).show();
                j = j + 1;

                //GetUserID().execute();
            } else {
                Toast.makeText(menuactivity.this, "Oh no! Order did not go through! Please try again!",Toast.LENGTH_LONG).show();
            }
        }
    }
    /*
    private class GetUserID extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

            Log.e("GetUserID response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    Boolean compareresult = false;
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
                        Log.d("ArrayStringCompare", ID2look4);
                        //
                        if (IDfoundObjtxt.equals(ID2look4)) {
                            compareresult = true;
                            index = i;
                        }
                    }
                    // if desired ID object is found
                    if (compareresult.equals(true)) {
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
                            oitempriceint = menuitemobjs.getInt("price");
                            //
                            Log.d("oitemname",oitemname);
                            Log.d("oitemprice",oitemprice);
                            // add value to each key
                            order_tmpmap.put("name", oitemname);
                            order_tmpmap.put("price", oitemprice);
                            oitempriceinttotal += oitempriceint;

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
            //
        }
    } // end the Asynctask of GetUserID
    */

}// end menuactivity