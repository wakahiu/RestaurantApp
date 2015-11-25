package restaurantapp.restaurantapp;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
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

public class testmodeactivity extends ListActivity {

    //progress dialog for testing purposes
    private ProgressDialog pDialog;
    // server URL
    private static String url = "http://dinnermate.azurewebsites.net/api/v1.0/user";
    // JSON Node names
    private static final String TAG_USERS = "users";
    private static final String TAG_ID = "_id";
    private static final String TAG_FIRSTNAME = "firstName";
    private static final String TAG_LASTNAME = "lastName";
    private static final String TAG_EMAIL = "email";
    // users JSONArray
    JSONArray users = null;
    // Hashmap for ListView
    ArrayList<HashMap<String, String>> testuserlist;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testlayout);

        //components for testing purposes
        testuserlist = new ArrayList<HashMap<String, String>>();
        //ListView lv = getListView();

        /*// Listview on item click listener
        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String firstname = ((TextView) view.findViewById(R.id.firstname)).getText().toString();
                String lastname = ((TextView) view.findViewById(R.id.lastname)).getText().toString();
                String email = ((TextView) view.findViewById(R.id.email)).getText().toString();

                // Starting single contact activity
                Intent in = new Intent(getApplicationContext(),SingleContactActivity.class);
                in.putExtra(TAG_FIRSTNAME, firstname);
                in.putExtra(TAG_LASTNAME, lastname);
                in.putExtra(TAG_EMAIL, email);
                startActivity(in);

            }
        }); */

        // Calling async task to get json
        new GetUsers().execute();
    }

    /** Async task class to get json by making HTTP call**/
    private class GetUsers extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(testmodeactivity.this);
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
                    users = jsonObj.getJSONArray(TAG_USERS);

                    // looping through All users
                    for (int i = 0; i < users.length(); i++) {
                        JSONObject user = users.getJSONObject(i);

                        // Personal detail is JSON object
                        String id = user.getString(TAG_ID);
                        String firstname = user.getString(TAG_FIRSTNAME);
                        String lastname = user.getString(TAG_LASTNAME);
                        String email = user.getString(TAG_EMAIL);

                        // tmp hashmap for single user
                        HashMap<String, String> user_tmpmat = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        user_tmpmat.put(TAG_ID, id);
                        user_tmpmat.put(TAG_FIRSTNAME, firstname);
                        user_tmpmat.put(TAG_LASTNAME, lastname);
                        user_tmpmat.put(TAG_EMAIL, email);

                        // adding user_tmpmat to testuserlist list
                        testuserlist.add(user_tmpmat);
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
                    testmodeactivity.this, testuserlist,

                    R.layout.testitemlist, new String[] { TAG_FIRSTNAME, TAG_LASTNAME, TAG_EMAIL },
                    new int[] { R.id.firstname, R.id.lastname, R.id.email}
            );

            setListAdapter(adapter);
        }

    }

}