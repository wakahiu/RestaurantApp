package restaurantapp.restaurantapp;

import android.app.Activity;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
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
    EditText email,password,newemail,newpassword;// initialize variables
    Button login,register,registerfrag,cancelfrag;
    String emailtxt,passwordtxt,newemailtxt,newpasswordtxt;
    SharedPreferences pref;
    serverRQ server_req;
    Dialog newdlg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginlayout);

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

                Intent testintent = new Intent(loginactivity.this, testmodeactivity.class);
                startActivity(testintent);


                /*
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

                        }

                        Toast.makeText(getApplication(),jsonstr,Toast.LENGTH_LONG).show();

                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                }*/
            }
        });
    }

}