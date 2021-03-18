package com.example.uom_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.uom_app.R;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    TextView textView;
    EditText e1,e2;
    Button b1;
    SharedPreferences sp;
    private static final String PREFS_NAME = "preferences";
    private static final String PREF_UNAME = "Username";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textView = findViewById(R.id.textView10);
        e1 = findViewById(R.id.editText);
        e2 = findViewById(R.id.editTextTextPassword);
        b1 = findViewById(R.id.button2);
        sp = getSharedPreferences("login",MODE_PRIVATE);


        String tex_email = e1.getText().toString();
        String tex_password = e2.getText().toString();




        if(sp.getBoolean("logged",false)){
            goToMainActivity();

        }

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String tex_email = e1.getText().toString();
                String tex_password = e2.getText().toString();
                if (TextUtils.isEmpty(tex_email) || TextUtils.isEmpty(tex_password)){
                    Toast.makeText(LoginActivity.this, "All Fields Required", Toast.LENGTH_SHORT).show();
                }
                else{

                    login(tex_email,tex_password);
                    SharedPreferences settings = getSharedPreferences(PREFS_NAME,
                            Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = settings.edit();

                    System.out.println("onPause save name: " + tex_email);

                    editor.putString(PREF_UNAME, tex_email);

                    editor.commit();



                   /* String s=e1.getText().toString();

                    Intent ii=new Intent(LoginActivity.this, MainActivity.class);
                    ii.putExtra("name", s);
                    startActivity(ii);*/



                }
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);

            }
        });


    }
    public void goToMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
    public void login(final String email, final String password){
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setTitle("Singing in");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setIndeterminate(false);
        progressDialog.show();
        String uRl = "http://192.168.0.110/energymeter/login.php";
        StringRequest request = new StringRequest(Request.Method.POST, uRl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.equals("Login Success")){
                    Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();

                    sp.edit().putBoolean("logged",true).apply();
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    progressDialog.dismiss();
                    finish();
                }
                else {
                    Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> param = new HashMap<>();
                param.put("email",email);
                param.put("psw",password);
                return param;

            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(30000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getmInstance(LoginActivity.this).addToRequestQueue(request);
    }
}


