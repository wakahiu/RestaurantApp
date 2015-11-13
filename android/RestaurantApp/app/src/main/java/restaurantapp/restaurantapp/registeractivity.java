package restaurantapp.restaurantapp;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
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

//not really an activity but a fragment
//due to change of layout

public class registeractivity extends Activity {
    EditText email,password;
    Button register;
    String emailtxt,passwordtxt;
    List<NameValuePair> params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registerlayout);

        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        register = (Button)findViewById(R.id.registerbtn);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent regactivity = new Intent(registeractivity.this,loginactivity.class);
                startActivity(regactivity);
                finish();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                emailtxt = email.getText().toString();
                passwordtxt = password.getText().toString();

                /*ContentValues values = new ContentValues();
                values.put("email",emailtxt);
                values.put("password",passwordtxt);*/

                params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("email", emailtxt));
                params.add(new BasicNameValuePair("password", passwordtxt));
                serverRQ sr = new serverRQ();
                JSONObject json = sr.getJSON("http://10.0.2.2:8080/register",params);

                if(json != null){
                    try{
                        String jsonstr = json.getString("response");
                        Toast.makeText(getApplication(),jsonstr,Toast.LENGTH_LONG).show();

                        Log.d("Hello", jsonstr);
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
