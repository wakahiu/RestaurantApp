package restaurantapp.restaurantapp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.nio.charset.Charset;
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
import android.util.Base64;
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
    private static URL orderURL = null;
    private static String orderUrlStr = "http://dinnermate.azurewebsites.net/api/v1.0/order";
    // JSON Node names
    private static final String TAG_MENU = "menuItems";
    private static final String TAG_ID = "_id";
    private static final String TAG_NAME = "name";
    private static final String TAG_PRICE = "price";
    private static final String TAG_CUISINE = "cuisine";

    // restaurants JSONArray & orders JSONArray
    JSONArray menuArray = null;

    // Hashmap for ListView
    ArrayList<HashMap<String, String>> menulist;

    // orderresponsecode initialization
    Integer orderresponsecode;

    // String Initialization
    String chosenfoodname,chosenfoodprice, chosenfoodid;

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
                //menuItemIdList.add("5651bdc3b6798a8c11ee65fa");
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
                urlConnection.setRequestProperty("Authorization","Basic " + basicAuth);
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

                // if there is a response code AND that response code is 200 OK, do
                // stuff in the first if block
                orderresponsecode = urlConnection.getResponseCode();
                Log.e("orderresponsecode",orderresponsecode.toString());
                Log.d(getClass().getEnclosingClass().getName(), urlConnection.getResponseMessage());
                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    // OK
                    Log.d(getClass().getEnclosingClass().getName(),
                            urlConnection.getResponseMessage());
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
                Toast.makeText(menuactivity.this, "Order added", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(menuactivity.this, "Oh no! Order did not go through! Please try again!",Toast.LENGTH_LONG).show();
            }
        }
    }


}