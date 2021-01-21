package com.example.uom_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.uom_app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    TextView t1,t2,t3,t4,t5;
    Button b1;
    private static final String PREFS_NAME = "preferences";
    private static final String PREF_UNAME = "Username";
    private static final String PREF_PASSWORD = "Password";
    private final String DefaultUnameValue = "";
    String txt_email;

    final String DefaultPasswordValue = "";
    String PasswordValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        t1 = findViewById(R.id.t1);
        t2 = findViewById(R.id.t2);
        t3 = findViewById(R.id.t3);
        t4 = findViewById(R.id.t4);
        t5 = findViewById(R.id.textView12);
        /*Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
            String j =(String) b.get("name");
            t5.setText(j);
        }*/
        SharedPreferences settings = getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        // Get value
        txt_email = settings.getString(PREF_UNAME, DefaultUnameValue);
        System.out.println("onResume load name: " + txt_email);

        t5.setText(txt_email);

        CardView card_view = (CardView) findViewById(R.id.card1);
        card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, graph.class);
                startActivity(intent);
            }
        });

        int noOfSecond = 1;
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                //TODO Set your button auto perform click.
                JSONProccess2 ("http://192.168.43.77:80/energymeter/index2.php");




            }
        }, noOfSecond * 1000);

    }

    public void JSONProccess2  ( String loginURL ) {






        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, loginURL, null,
                new Response.Listener<JSONObject> () {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            JSONArray ja = response.getJSONArray ("results");


                            JSONObject jsonObject = ja.getJSONObject (0);

                            String name = jsonObject.getString ("currentmonth");
                            String number = jsonObject.getString ("unitscalculated");
                            String name1 = jsonObject.getString ("lastmonth");
                            String name2 = jsonObject.getString ("estimatedamount");



                            t1.setText (name);
                            t2.setText (number);
                            t3.setText (name1);
                            t4.setText (name2);
                            Toast.makeText (MainActivity.this, number + ", " + name, Toast.LENGTH_SHORT).show ();



                        } catch (JSONException e) {
                            e.printStackTrace ();
                        }
                    }

                },
                new Response.ErrorListener () {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e ("Volley", "Error");
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            //This indicates that the reuest has either time out or there is no connection
                            Log.e ("Volley1", "Error");
                        } else if (error instanceof AuthFailureError) {
                            // Error indicating that there was an Authentication Failure while performing the request
                            Log.e ("Volley2", "Error");
                        } else if (error instanceof ServerError) {
                            //Indicates that the server responded with a error response
                            Log.e ("Volley3", "Error");
                        } else if (error instanceof NetworkError) {
                            //Indicates that there was network error while performing the request
                            Log.e ("Volley4", "Error");
                        } else if (error instanceof ParseError) {
                            // Indicates that the server response could not be parsed
                            Log.e ("Volley5", "Error");
                        }
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.getCache().clear();
        jor.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add (jor);

    }

}