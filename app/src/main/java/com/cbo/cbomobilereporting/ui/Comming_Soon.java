package com.cbo.cbomobilereporting.ui;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cbo.cbomobilereporting.R;

/**
 * Created by Akshit on 5/7/2015.
 */
public class Comming_Soon extends AppCompatActivity {


    TextView textView_welcome,login_text,signup_text,reset_pin;
    Button bt_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.enter_pin_2016);


    //    textView_welcome = (TextView) findViewById(R.id.welcome_text_enter_pin);
       // login_text =(TextView) findViewById(R.id.submit_enter_pin);
      //  signup_text =(TextView) findViewById(R.id.signup_text_enter_pin);
        reset_pin =(TextView) findViewById(R.id.reset_pin_enter_pin);

        Typeface mTypeface = Typeface.createFromAsset(getAssets(),"font/FORTE.TTF");

        textView_welcome.setTypeface(mTypeface);
        login_text.setTypeface(mTypeface);
        signup_text.setTypeface(mTypeface);
        reset_pin.setTypeface(mTypeface);






    }


}
