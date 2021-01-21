package com.example.uom_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.uom_app.R;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    Button button;
    EditText et1,et2,et3,et4;
    private static final String PREFS_NAME = "preferences";
    private static final String PREF_UNAME = "Username";
    private static final String PREF_PASSWORD = "Password";
    private final String DefaultUnameValue = "";
    String Uname;

    final String DefaultPasswordValue = "";
    String PasswordValue;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        sp = getSharedPreferences("login",MODE_PRIVATE);

        button = findViewById(R.id.signin);
        et1 = findViewById(R.id.editText5);
        et2 = findViewById(R.id.editText2);
        et3 = findViewById(R.id.editText3);
        et4 = findViewById(R.id.editText4);
        if(sp.getBoolean("logged",false)){
            goToMainActivity();

        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String user_name = et1.getText().toString();
                final String email = et2.getText().toString();
                final String txt_password = et3.getText().toString();
                final String txt_mobile = et4.getText().toString();


                if (TextUtils.isEmpty(user_name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(txt_password) ||
                        TextUtils.isEmpty(txt_mobile)) {
                    Toast.makeText(SignupActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                } else {
                    if(!txt_password.equals(txt_mobile)){
                        Toast.makeText(SignupActivity.this,"Password Not matching",Toast.LENGTH_SHORT).show();

                    }
                    else {
                        register(user_name, email, txt_password, txt_mobile);
                        sp.edit().putBoolean("logged",true).apply();
                        SharedPreferences settings = getSharedPreferences(PREFS_NAME,
                                Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        System.out.println("onPause save name: " + user_name);

                        editor.putString(PREF_UNAME, user_name);

                        editor.commit();
                    }
                }
            }


        });

    }

    public void goToMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }



    private void register(final String username, final String email, final String password, final String mobile){
        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this);
        progressDialog.setTitle("Registering your account");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setIndeterminate(false);
        progressDialog.show();
        String uRl = "http://192.168.43.77/energymeter/register.php";
        StringRequest request = new StringRequest(Request.Method.POST, uRl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.equals("You are registered successfully")){
                    Toast.makeText(SignupActivity.this, response, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignupActivity.this,MobileVerification.class));
                    progressDialog.dismiss();
                    finish();
                }else {
                    Toast.makeText(SignupActivity.this, response, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SignupActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> param = new HashMap<>();
                param.put("username",username);
                param.put("email",email);
                param.put("psw",password);
                param.put("confirmpassword",mobile);

                return param;

            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(30000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getmInstance(SignupActivity.this).addToRequestQueue(request);

    }
}

