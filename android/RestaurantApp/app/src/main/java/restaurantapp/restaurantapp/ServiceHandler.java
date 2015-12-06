package restaurantapp.restaurantapp;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class ServiceHandler {
    Integer responsecode = null;
    static String response = null;
    public final static int GET = 1;
    public final static int POST = 2;

    public ServiceHandler() {
    }
    /* Making service call
     *
     * @url - url to make request
     * @method - http request method
     */
    public String makeServiceCall(String url, int method) {
        return this.makeServiceCall(url, method, null);
    }
    /* Making service call
     *
     * @url - url to make request
     * @method - http request method
     * @params - http request params
     */
    public String makeServiceCall(String url, int method, List<NameValuePair> params) {
        try {
            // http client
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpEntity httpEntity = null;
            HttpResponse httpResponse = null;

            // Checking http request method type
            if (method == POST) {
                HttpPost httpPost = new HttpPost(url);
                // adding post params
                if (params != null) {
                    httpPost.setEntity(new UrlEncodedFormEntity(params, "utf-8")); //sets the post request as the resulting string
                }

                httpResponse = httpClient.execute(httpPost);

            } else if (method == GET) {
                // appending params to url
                if (params != null) {
                    String paramString = URLEncodedUtils.format(params, "utf-8");
                    url += "?" + paramString;
                }
                HttpGet httpGet = new HttpGet(url);

                httpResponse = httpClient.execute(httpGet);

            }
            httpEntity = httpResponse.getEntity();
            response = EntityUtils.toString(httpEntity);
            responsecode = httpResponse.getStatusLine().getStatusCode();
            // Log the results for debugging  information
            Log.d("Get Response", response.toString());
            Log.e("httpEntity",httpEntity.toString());
            Log.e("Status Code",responsecode.toString());

        } catch (UnsupportedEncodingException err) {
            // log exception
            err.printStackTrace();
            Log.e("Unsupp Enc Except", err.toString());
        } catch (ClientProtocolException err) {
            err.printStackTrace();
            Log.e("Client Prot Except", err.toString());
        } catch (IOException err) {
            err.printStackTrace();
            Log.e("IO Exception", err.toString());
        }

        return response;

    }
}