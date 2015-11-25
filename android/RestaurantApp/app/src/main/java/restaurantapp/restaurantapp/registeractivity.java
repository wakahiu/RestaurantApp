package restaurantapp.restaurantapp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class registeractivity extends Activity {

    EditText email, password, firstname, lastname;
    Button registerbtn,cancelbtn;
    String emailtxt, passwordtxt, firstnametxt, lastnametxt, stafftxt;
    Integer responsecode;
    CheckBox staffindicator;
    Boolean staffboolean;

    // server URL
    private static String url = "http://dinnermate.azurewebsites.net/api/v1.0/user";
    // JSON OBject nodes
    private static final String TAG_FIRSTNAME = "firstName";
    private static final String TAG_LASTNAME = "lastName";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_PASSWORD = "password";
    private static final String TAG_STAFF = "staff";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registerlayout);
        registerbtn = (Button) findViewById(R.id.registerbtn);
        cancelbtn = (Button) findViewById(R.id.cancelbtn);

        // Set Intent for Cancel button
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
        public void onClick (View view) {
                Intent intent2quit = new Intent(registeractivity.this,loginactivity.class);
                startActivity(intent2quit);
            }
        });
        // Acts when registerbtn is pressed by the user.
        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Find user inputs when register button is pressed
                firstname = (EditText) findViewById(R.id.firstname);
                lastname = (EditText) findViewById(R.id.lastname);
                email = (EditText) findViewById(R.id.email);
                password = (EditText) findViewById(R.id.password);
                staffindicator = (CheckBox) findViewById(R.id.staffstatus);
                staffboolean = staffindicator.isChecked();

                // Change user inputs to strings
                firstnametxt = firstname.getText().toString();
                lastnametxt = lastname.getText().toString();
                emailtxt = email.getText().toString();
                passwordtxt = password.getText().toString();
                stafftxt = staffboolean.toString();

                // Execute HTTP POST.
                new PostUsers().execute();
            }
        });
    }

    /**
     * Async task class to POST json by making HTTP call
     **/
    private class PostUsers extends AsyncTask<String, Void, Void> {
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
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
                // add the new information into nameValuePairs
                nameValuePairs.add(new BasicNameValuePair(TAG_FIRSTNAME, firstnametxt));
                nameValuePairs.add(new BasicNameValuePair(TAG_LASTNAME, lastnametxt));
                nameValuePairs.add(new BasicNameValuePair(TAG_EMAIL, emailtxt));
                nameValuePairs.add(new BasicNameValuePair(TAG_PASSWORD, passwordtxt));
                nameValuePairs.add(new BasicNameValuePair(TAG_STAFF, stafftxt));

                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "utf-8"));

                // Execute HTTP Request and return response
                HttpResponse response = httpClient.execute(httpPost);
                HttpEntity httpEntity = response.getEntity();
                responsecode = response.getStatusLine().getStatusCode();
                // Log the results for debugging  information
                Log.e("httpEntity",httpEntity.toString());
                Log.e("Status Code",responsecode.toString());

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

            if  (responsecode.equals(201)) {
                Toast.makeText(registeractivity.this, "Registration successful!",Toast.LENGTH_LONG).show();
                Intent back2loginintent = new Intent(registeractivity.this,loginactivity.class);
                startActivity(back2loginintent);
            } else {
                Toast.makeText(registeractivity.this, "Failed registration. Please try again!",Toast.LENGTH_LONG).show();
            }
        }
    }
}
