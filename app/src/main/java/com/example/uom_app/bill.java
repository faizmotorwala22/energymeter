package com.example.uom_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;


public class bill extends AppCompatActivity {
    TextView t1,t2,t3,t4,t5,t6,t7,t8;
    private static final String PREFS_NAME = "preferences";
    private static final String PREFS_NAME1= "preferences1";
    private static final String PREFS_NAME2= "preferences2";
    private static final String PREF_UNAME = "Username";
    private static final String PREF_UNAME1 = "Username1";
    private static final String PREF_UNAME2 = "Username2";
    private static final String PREF_PASSWORD = "Password";
    private final String DefaultUnameValue = "";
    private final String DefaultUnameValue1 = "";
    private final String DefaultUnameValue2 = "";
    String txt_email,txt_email1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        SharedPreferences settings1 = getSharedPreferences(PREFS_NAME1,
                Context.MODE_PRIVATE);

        // Get value
        t1 = findViewById(R.id.consumed1);
        t2 = findViewById(R.id.amount1);
        t3 = findViewById(R.id.your1);

        txt_email = settings1.getString(PREF_UNAME1, DefaultUnameValue1);
        System.out.println("onResume load name: " + txt_email);
        t1.setText(txt_email);
        SharedPreferences settings2 = getSharedPreferences(PREFS_NAME2,
                Context.MODE_PRIVATE);
        txt_email1 = settings2.getString(PREF_UNAME2, DefaultUnameValue2);
        System.out.println("onResume load name: " + txt_email1);
        t2.setText(txt_email1);


        t3.setText(txt_email1);

    }
}
