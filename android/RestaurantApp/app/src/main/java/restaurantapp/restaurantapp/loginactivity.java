package restaurantapp.restaurantapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.ArrayList;
import java.util.List;

public class loginactivity extends Activity {
    //
    Bundle passthis = new Bundle();
    //
    EditText email,password;
    Button login,registerbutton,loginBypass, loginStaff;
    String emailtxt,passwordtxt, isStafftxt;
    Integer responsecode;
    Boolean isStaff;
    TextView loginmaintitle;

    private static final String TAG_EMAIL = "email";
    private static final String TAG_PASSWORD = "password";
    private static final String TAG_Staff = "isStaff";
    // server URL
    private static String url = "http://dinnermate.azurewebsites.net/api/v1.0/user/authenticate";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginlayout);
        final Typeface loginfont = Typeface.createFromAsset(getAssets(),"txtfont1.ttf");
        // title & set font type
        loginmaintitle = (TextView)findViewById(R.id.loginpagetitle);
        loginmaintitle.setTypeface(loginfont);
        // Buttons & set font type
        login = (Button)findViewById(R.id.log_in_button);
        login.setTypeface(loginfont);
        registerbutton = (Button)findViewById(R.id.register);
        registerbutton.setTypeface(loginfont);
        loginBypass = (Button)findViewById(R.id.loginBypass);
        loginBypass.setTypeface(loginfont);
        loginStaff = ( Button ) findViewById( R.id.loginStaff );
        loginStaff.setTypeface(loginfont);

        // Start an intent for the GCM Registration. This generates tokens in the background.
        // GCM ensures that orders and invoices are received continuously the background.
        Intent gcmServiceIntent = new Intent(this, GCMRegistrationIntentService.class);
        startService(gcmServiceIntent);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // User input texts, set Typeface and get user input
                email = (EditText)findViewById(R.id.email);
                email.setTypeface(loginfont);
                password = (EditText)findViewById(R.id.password);
                password.setTypeface(loginfont);
                emailtxt = email.getText().toString().toLowerCase();
                passwordtxt = password.getText().toString();
                isStaff = false;
                isStafftxt = isStaff.toString();

                Log.e("emailtxt3",emailtxt);
                Log.e("passwordtxt3",passwordtxt);

                new PostUserCredential().execute();
            }
        });

        loginBypass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                emailtxt = "test@fake.com";
                passwordtxt = "888iii$$$III";

                new PostUserCredential().execute();
            }
        });

        loginStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // User input texts
                email = (EditText)findViewById(R.id.email);
                password = (EditText)findViewById(R.id.password);
                emailtxt = email.getText().toString().toLowerCase();
                passwordtxt = password.getText().toString();
                isStaff = true;
                isStafftxt = isStaff.toString();

                Log.e("emailtxt3",emailtxt);
                Log.e("passwordtxt3",passwordtxt);

                new PostUserCredential().execute();
            }
        });

        // Check to see whether a network connection is available
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            Log.e(getClass().getEnclosingClass().getName(),"Network unavailable");
        }

        final String usernameX = "38KBYFLH2MID9SY55GI4FEXFB";
        final String passwordX = "2eyLz5LKmB/AoD+g77gHOw6RRThLjucZblIq9cGlSno";
        // Unless paired with HTTPS, this is not a secure mechanism for user authentication.
        // In particular, the username, password, request and response are all
        // transmitted over the network without encryption.
        Authenticator.setDefault(new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(usernameX, passwordX.toCharArray());
            }
        });

        registerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerintent = new Intent(loginactivity.this,registeractivity.class);
                startActivity(registerintent);
            }
        });
    }

    /* Use HTTP POST to request credential check */
    private class PostUserCredential extends AsyncTask<String, Void, Void> {
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
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                // add the new information into nameValuePairs
                nameValuePairs.add(new BasicNameValuePair(TAG_EMAIL, emailtxt));
                nameValuePairs.add(new BasicNameValuePair(TAG_PASSWORD, passwordtxt));
                nameValuePairs.add ( new BasicNameValuePair( TAG_Staff, isStafftxt ) );

                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "utf-8"));

                // Execute HTTP Request and return response
                HttpResponse response = httpClient.execute(httpPost);
                HttpEntity httpEntity = response.getEntity();
                responsecode = response.getStatusLine().getStatusCode();
                // Log the results for debugging  information
                Log.e("httpEntity",httpEntity.toString());
                Log.e("Status Code", responsecode.toString());
                Log.d("email name", emailtxt);
                Log.d("email tag", TAG_EMAIL);
                Log.d("password name", passwordtxt);
                Log.d("password tag",TAG_PASSWORD);
                //Log.e("isStaff", isStafftxt);
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
                Toast.makeText(loginactivity.this, "Login successful!",Toast.LENGTH_LONG).show();
               // if ( isstfftxt.equals("false ) {
                    Intent forward2restaurantintent = new Intent(loginactivity.this, restaurantactivity.class);
                    passthis.putString("currentuseremail",emailtxt);
                    forward2restaurantintent.putExtras(passthis);
                    startActivity(forward2restaurantintent);
                    finish();
                /*} else {
                    Intent forward2stafforderintent = new Intent(loginactivity.this, stafforderactivity.class);
                    forward2stafforderintent.putExtra( "email", emailtxt );
                    startActivity( forward2stafforderintent );
                    finish();
                }*/
            } else {
                Toast.makeText(loginactivity.this, "Failed login. Please try again!",Toast.LENGTH_LONG).show();
            }
        }
    }
}