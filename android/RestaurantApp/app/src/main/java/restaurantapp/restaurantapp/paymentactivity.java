package restaurantapp.restaurantapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class paymentactivity extends Activity{
    Button makepayment,cancelpayment;
    EditText lastname,firstname, cardnumber, expmonth, expyear;
    String lastnametxt,firstnametxt, cardnumbertxt, expmonthtxt, expyeartxt;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paymentlayout);

        lastname = (EditText)findViewById(R.id.lastname);
        firstname = (EditText)findViewById(R.id.firstname);
        cardnumber = (EditText)findViewById(R.id.cardnumber);
        expmonth = (EditText)findViewById(R.id.expirymonth);
        expyear = (EditText)findViewById(R.id.expiryyear);

        makepayment = (Button)findViewById(R.id.paynowbtn);
        cancelpayment = (Button)findViewById(R.id.cancelpaymentbtn);

        makepayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get things from user input texts
                lastnametxt = lastname.getText().toString().toLowerCase();
                firstnametxt = firstname.getText().toString().toLowerCase();
                cardnumbertxt = cardnumber.getText().toString();
                expmonthtxt = expmonth.getText().toString();
                expyeartxt = expyear.getText().toString();

                Toast.makeText(paymentactivity.this, "Payment Successful!", Toast.LENGTH_LONG).show();

                Intent buycompleteintent = new Intent(paymentactivity.this, shoppingbasketactivity.class);
                startActivity(buycompleteintent);
                finish();

            }
        });

        cancelpayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cancelbuyintent = new Intent(paymentactivity.this, shoppingbasketactivity.class);
                startActivity(cancelbuyintent);
                finish();
            }

        });
    }

}
