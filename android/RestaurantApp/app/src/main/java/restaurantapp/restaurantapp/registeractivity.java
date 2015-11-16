package restaurantapp.restaurantapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

//not really an activity but a fragment
public class registeractivity extends Activity {
    EditText email, password, firstname, lastname;
    Button registerbtn;
    String emailtxt, passwordtxt, firstnametxt, lastnametxt, stafftxt;

    // server URL
    private static String url = "http://dinnermate.azurewebsites.net/api/v1.0/user/enroll";
    // users JSONArray
    JSONArray users = null;
    // new JSONObject
    JSONObject newuser = new JSONObject();
    // JSON Array name
    private static final String TAG_USERS = "users";
    // JSON OBject nodes
    private static final String TAG_FIRSTNAME = "firstName";
    private static final String TAG_LASTNAME = "lastName";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_PASSWORD = "password";
    private static final String TAG_STAFF = "staff";

    // ListPair
    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registerlayout);

        // Acts when registerbtn is pressed by the user.
        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Find user inputs when register button is pressed
                firstname = (EditText) findViewById(R.id.firstname);
                lastname = (EditText) findViewById(R.id.lastname);
                email = (EditText) findViewById(R.id.email);
                password = (EditText) findViewById(R.id.password);
                registerbtn = (Button) findViewById(R.id.registerbtn);
                // Change user inputs to strings
                firstnametxt = firstname.getText().toString();
                lastnametxt = lastname.getText().toString();
                emailtxt = email.getText().toString();
                passwordtxt = password.getText().toString();
                stafftxt = "false";

                // add the new information into nameValuePairs
                nameValuePairs.add(new BasicNameValuePair(TAG_FIRSTNAME, firstnametxt));
                nameValuePairs.add(new BasicNameValuePair(TAG_LASTNAME, lastnametxt));
                nameValuePairs.add(new BasicNameValuePair(TAG_EMAIL, emailtxt));
                nameValuePairs.add(new BasicNameValuePair(TAG_PASSWORD, passwordtxt));
                nameValuePairs.add(new BasicNameValuePair(TAG_STAFF, stafftxt));



                // Execute the SeviceHandler in post mode.
                new PostUsers().execute();
            }
        });
    }

    /**
     * Async task class to post json by making HTTP call
     **/
    private class PostUsers extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Make a request to url and get response
            sh.makeServiceCall(url, ServiceHandler.POST, nameValuePairs);
            // Log HTTP response
            /*Log.d("Response: ", "> " + jsonStr);
            // if jsonStr isn't null, then try
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    // Get JSON Array node
                    users = jsonObj.getJSONArray(TAG_USERS);
                    // Post to this specific JSON node
                    users.add(jsonStr);
                }*/
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }
}
