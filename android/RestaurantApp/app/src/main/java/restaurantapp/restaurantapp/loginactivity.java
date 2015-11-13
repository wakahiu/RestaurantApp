package restaurantapp.restaurantapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.AsyncTask;

public class loginactivity extends Activity {
    // initialize variables
    EditText email,password,newemail,newpassword;
    Button login,register,registerfrag,cancelfrag;
    String emailtxt,passwordtxt,newemailtxt,newpasswordtxt;
    SharedPreferences pref;
    serverRQ server_req;
    Dialog newdlg;

    List<NameValuePair> params;

    // Possible solution for deprecated org.apache.http class
    /*
    private String getEncodedData(Map<String,String> data) {
        StringBuilder sb = new StringBuilder();
        for(String key : data.keySet()) {
            String value = null;
            try {
                value = URLEncoder.encode(data.get(key), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            if(sb.length()>0)
                sb.append("&");

            sb.append(key + "=" + value);
        }
        return sb.toString();
    }
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginlayout); // Focus on loginlayout.xml

        server_req = new serverRQ();
        email = (EditText)findViewById(R.id.email_text);
        password = (EditText)findViewById(R.id.password_text);
        login = (Button)findViewById(R.id.log_in_button);
        register = (Button)findViewById(R.id.register);

        pref = getSharedPreferences("AppPref", MODE_PRIVATE);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newdlg = new Dialog(loginactivity.this);
                newdlg.setContentView(R.layout.registerlayout);
                newdlg.setTitle("Register");
                registerfrag = (Button) newdlg.findViewById(R.id.registerbtn);

                registerfrag.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        newemail = (EditText) newdlg.findViewById(R.id.email);
                        newpassword = (EditText) newdlg.findViewById(R.id.password);
                        newemailtxt = newemail.getText().toString();
                        newpasswordtxt = newpassword.getText().toString();

                    }
                });

                //cancel to leave dialogue fragment
                cancelfrag = (Button) newdlg.findViewById(R.id.cancelbtn);
                cancelfrag.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        newdlg.dismiss();
                    }
                });
                newdlg.show();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailtxt = email.getText().toString();
                passwordtxt = password.getText().toString();

                //server request starts here - still needs new method since apache is deprecated
                //commented out possible solution
                /*Map<String,String> data2send = new HashMap<>();
                data2send.put("email",emailtxt);
                data2send.put("password",passwordtxt);
                String encodeddata = getEncodedData(data2send);*/
                params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("email", emailtxt));
                params.add(new BasicNameValuePair("password", passwordtxt));

                /*
                //Will be used if we want to read some data from server
                BufferedReader reader = null;

                //Connection Handling
                try {
                    //Converting address String to URL
                    URL url = new URL(SERVER_ADDRESS + "Register.php");
                    //Opening the connection (Not setting or using CONNECTION_TIMEOUT)
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();

                    //Post Method
                    con.setRequestMethod("POST");
                    //To enable inputting values using POST method
                    //(Basically, after this we can write the dataToSend to the body of POST method)
                    con.setDoOutput(true);
                    OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
                    //Writing dataToSend to outputstreamwriter
                    writer.write(encodeddata);
                    //Sending the data to the server - This much is enough to send data to server
                    //But to read the response of the server, you will have to implement the procedure below
                    writer.flush();

                    //Data Read Procedure - Basically reading the data comming line by line
                    StringBuilder sb = new StringBuilder();
                    reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String line;
                    while((line = reader.readLine()) != null) { //Read till there is something available
                        sb.append(line + "\n");     //Reading and saving line by line - not all at once
                    }
                    line = sb.toString();           //Saving complete data received in string, you can do it differently

                    //Just check to the values received in Logcat
                    Log.i("custom_check", "The values received in the store part are as follows:");
                    Log.i("custom_check",line);

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if(reader != null) {
                        try {
                            reader.close();     //Closing the
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                //Same return null, but if you want to return the read string (stored in line)
                //then change the parameters of AsyncTask and return that type, by converting
                //the string - to say JSON or user in your case
                return null;
            }*/


                serverRQ sr = new serverRQ();
                JSONObject json = sr.getJSON("http://10.0.2.2:8080/login",params);
                if(json != null){
                    try{
                        String jsonstr = json.getString("response");
                        if(json.getBoolean("res")){
                            String token = json.getString("token");
                            String grav = json.getString("grav");
                            SharedPreferences.Editor edit = pref.edit();
                            //Storing Data using SharedPreferences
                            edit.putString("token", token);
                            edit.putString("grav", grav);
                            edit.apply();

                            /*
                            //if email/password combo matches move onto next activity.
                            Intent restaurantlistactivity = new Intent(loginactivity.this,RestaurantItemListFragment.class);
                            startActivity(restaurantlistactivity);
                            finish();*/
                        }

                        Toast.makeText(getApplication(),jsonstr,Toast.LENGTH_LONG).show();

                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}