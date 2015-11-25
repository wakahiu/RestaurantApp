package restaurantapp.restaurantapp;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class testmodeactivity2 extends Activity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testmode2layout);
        new pleaserun().execute();
    }

    private class pleaserun extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... arg0) {
            // Creating HTTP client
            HttpParams params = new BasicHttpParams();
            params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
            HttpClient httpClient = new DefaultHttpClient(params);
            // Creating HTTP Post
            HttpPost httpPost = new HttpPost("http://dinnermate.azurewebsites.net/api/v1.0/user");

            // Url Encoding the POST parameters
            try {
                // Building post parameters

                // key and value pair
                List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(5);
                nameValuePair.add(new BasicNameValuePair("email", "user@gmail.com"));
                nameValuePair.add(new BasicNameValuePair("firstName", "John"));
                nameValuePair.add(new BasicNameValuePair("lastName", "Cena"));
                nameValuePair.add(new BasicNameValuePair("password", "128174893471393aAAAsd@#@!!"));
                nameValuePair.add(new BasicNameValuePair("staff", "False"));

                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair, "utf-8"));
                // Making HTTP Request
                HttpResponse response = httpClient.execute(httpPost);
                HttpEntity httpEntity = response.getEntity();
                Log.d("Response: ", "> " + response);
            } catch (UnsupportedEncodingException e) {
                // writing error to Log
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                // writing exception to log
                e.printStackTrace();
            } catch (IOException e) {
                // writing exception to log
                e.printStackTrace();

            }

            return true;
        }
    }

}