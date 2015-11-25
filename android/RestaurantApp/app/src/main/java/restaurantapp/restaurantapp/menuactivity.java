package restaurantapp.restaurantapp;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class menuactivity extends ListActivity {
    FloatingActionButton fab; // floating action button
    private ProgressDialog pDialog; //progress dialog for testing purposes

    // server URL
    private static String url = "http://dinnermate.azurewebsites.net/api/v1.0/menu";
    private static String orderurl = "";
    // JSON Node names
    private static final String TAG_MENU = "menuItems";
    private static final String TAG_ID = "_id";
    private static final String TAG_NAME = "name";
    private static final String TAG_PRICE = "price";
    private static final String TAG_CUISINE = "cuisine";

    private String TAG_FOODNAME = "chosenfoodname";
    private String TAG_FOODPRICE = "chosenfoodprice";

    // restaurants JSONArray & orders JSONArray
    JSONArray menuArray = null;

    // Hashmap for ListView
    ArrayList<HashMap<String, String>> menulist;

    // orderresponsecode initialization
    Integer orderresponsecode;

    // String Initialization
    String chosenname,chosenprice;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menulistlayout);

        // Shared preference
        //final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        // find the fab in the layout
        fab = (FloatingActionButton)findViewById(R.id.fab);
        // set on click action for fab
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sbasketintent = new Intent(menuactivity.this, shoppingbasketactivity.class);
                startActivity(sbasketintent);
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
                chosenname = ((TextView) view.findViewById(R.id.foodname))
                        .getText().toString();
                chosenprice = ((TextView) view.findViewById(R.id.foodprice))
                        .getText().toString();

                /*SharedPreferences.Editor editor = prefs.edit();
                editor.putString("foodnamekey", chosenfoodname);
                editor.putString("foodpricekey", chosenfoodprice);
                editor.commit();*/
                new PostOrders().execute();
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
            pDialog = new ProgressDialog(menuactivity.this);
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
                    menuArray = jsonObj.getJSONArray(TAG_MENU);

                    // looping through All users
                    for (int i = 0; i < menuArray.length(); i++) {
                        JSONObject user = menuArray.getJSONObject(i);

                        // Restaurant JSON object
                        String restaurantid = user.getString(TAG_ID);
                        String name = user.getString(TAG_NAME);
                        String price = user.getString(TAG_PRICE);
                        String cuisine = user.getString(TAG_CUISINE);

                        // tmp hashmap for single restaurant
                        HashMap<String, String> menu_tmpmat = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        menu_tmpmat.put(TAG_ID, restaurantid);
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
                    menuactivity.this, menulist,

                    R.layout.menuitemlist, new String[] { TAG_NAME, TAG_PRICE, TAG_CUISINE },
                    new int[] { R.id.foodname, R.id.foodprice, R.id.cuisine}
            );

            setListAdapter(adapter);
        }
    }

    //Async task class to POST order json by making HTTP call
    private class PostOrders extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... arg0) {
            // Creating HTTP client
            HttpParams params = new BasicHttpParams();
            params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
            HttpClient httpClient = new DefaultHttpClient(params);
            // Creating HTTP Post
            HttpPost httpPost = new HttpPost(url);

            try {
                // Initialize List
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                // add the new information into nameValuePairs
                nameValuePairs.add(new BasicNameValuePair(TAG_FOODNAME, chosenname));
                nameValuePairs.add(new BasicNameValuePair(TAG_FOODPRICE, chosenprice));

                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "utf-8"));

                // Execute HTTP Request and return response
                HttpResponse response = httpClient.execute(httpPost);
                HttpEntity httpEntity = response.getEntity();
                orderresponsecode = response.getStatusLine().getStatusCode();
                // Log the results for debugging  information
                Log.e("Order httpEntity",httpEntity.toString());
                Log.e("Order Status Code",orderresponsecode.toString());

            } catch (UnsupportedEncodingException err) {
                // writing error to Log
                err.printStackTrace();
                Log.e("Unsupp Enc Except",err.toString());
            } catch (ClientProtocolException err) {
                // writing exception to log
                err.printStackTrace();
                Log.e("Client Prot Except",err.toString());
            } catch (IOException err) {
                // writing exception to log
                err.printStackTrace();
                Log.e("IO Exception",err.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if  (orderresponsecode.equals(201)) {
                Toast.makeText(menuactivity.this, "Order added", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(menuactivity.this, "Oh no! Order did not go through! Please try again!",Toast.LENGTH_LONG).show();
            }
        }
    }


}