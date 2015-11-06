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

import java.util.ArrayList;
import java.util.List;


public class loginactivity extends Activity {
    /* initialize variables*/
    /* forpass deleted*/
    EditText email,password,res_email,code,newpass;
    Button login,cont,cont_code,cancel,cancel1,register;
    String emailtxt,passwordtxt,email_res_txt,code_txt,npass_txt;
    List<NameValuePair> params;
    SharedPreferences pref;
    Dialog reset;
    serverRQ server_req;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginlayout); /* Focus on loginlayout.xml */

        server_req = new serverRQ();
        email = (EditText)findViewById(R.id.email_text);
        password = (EditText)findViewById(R.id.password_text);
        login = (Button)findViewById(R.id.log_in_button);
        register = (Button)findViewById(R.id.register);

        pref = getSharedPreferences("AppPref", MODE_PRIVATE);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent register_mode = new Intent(loginactivity.this,registeractivity.class);
                startActivity(register_mode);
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailtxt = email.getText().toString();
                passwordtxt = password.getText().toString();
                params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("email", emailtxt));
                params.add(new BasicNameValuePair("password", passwordtxt));
                ServerRequest sr = new ServerRequest();
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
                            edit.commit();
                            Intent profactivity = new Intent(LoginActivity.this,ProfileActivity.class);

                            startActivity(profactivity);
                            finish();
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