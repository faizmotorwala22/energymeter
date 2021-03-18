package com.example.uom_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
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
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    TextView t1, t2, t3, t4, t5;
    Button b1;
    private static final String PREFS_NAME = "preferences";
    private static final String PREF_UNAME = "Username";
    private static final String PREF_PASSWORD = "Password";
    private final String DefaultUnameValue = "";
    String txt_email;
    private static final String PREFS_NAME1 = "preferences1";
    private static final String PREF_UNAME1 = "Username1";
    private static final String PREFS_NAME2 = "preferences2";
    private static final String PREF_UNAME2 = "Username2";

    final String DefaultPasswordValue = "";
    String PasswordValue;
    private BroadcastReceiver mRegistrationBroadcastReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {


                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);


                }
                if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {

                    // new push notification is received

                    String message = intent.getStringExtra("message");
                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();
                    //txtMessage.setText(message);

                }
            }};
                    t1 = findViewById(R.id.t1);
        t2 = findViewById(R.id.t2);
        t3 = findViewById(R.id.t3);
        t4 = findViewById(R.id.t4);
        t5 = findViewById(R.id.textView12);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
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

        CardView card_view1 = (CardView) findViewById(R.id.card2);
        card_view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, bill.class);
                startActivity(intent);
            }
        });

        int noOfSecond = 1;
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                //TODO Set your button auto perform click.
                JSONProccess2("http://192.168.0.110/energymeter/index2.php");
                String value = t1.getText().toString();
                SharedPreferences settings1 = getSharedPreferences(PREFS_NAME1,
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor editor1 = settings1.edit();
                System.out.println("onPause save name: " + value);

                editor1.putString(PREF_UNAME1, value);

                editor1.commit();

                String value1 = t4.getText().toString();
                SharedPreferences settings2 = getSharedPreferences(PREFS_NAME2,
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor editor2 = settings2.edit();
                System.out.println("onPause save name: " + value1);

                editor2.putString(PREF_UNAME2, value1);

                editor2.commit();


            }
        }, noOfSecond * 1000);
        final Thread thread = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(20000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                JSONProccess2("http://192.168.0.110:80/energymeter/index2.php");
                                String value = t1.getText().toString();
                                SharedPreferences settings1 = getSharedPreferences(PREFS_NAME1,
                                        Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor1 = settings1.edit();
                                System.out.println("onPause save name: " + value);

                                editor1.putString(PREF_UNAME1, value);

                                editor1.commit();

                                // update TextView here!
                                String value1 = t4.getText().toString();
                                SharedPreferences settings2 = getSharedPreferences(PREFS_NAME2,
                                        Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor2 = settings2.edit();
                                System.out.println("onPause save name: " + value1);

                                editor2.putString(PREF_UNAME2, value1);

                                editor2.commit();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        thread.start();


    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void JSONProccess2(String loginURL) {


        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, loginURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            JSONArray ja = response.getJSONArray("results");


                            JSONObject jsonObject = ja.getJSONObject(0);

                            String name = jsonObject.getString("volt");
                            String number = jsonObject.getString("unitscalculated");
                            String name1 = jsonObject.getString("lastmonth");
                            String name2 = jsonObject.getString("estimatedamount");


                            t1.setText(name);
                            t2.setText(number);
                            t3.setText(name1);
                            t4.setText(name2);
                            Toast.makeText(MainActivity.this, number + ", " + name, Toast.LENGTH_SHORT).show();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error");
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            //This indicates that the reuest has either time out or there is no connection
                            Log.e("Volley1", "Error");
                        } else if (error instanceof AuthFailureError) {
                            // Error indicating that there was an Authentication Failure while performing the request
                            Log.e("Volley2", "Error");
                        } else if (error instanceof ServerError) {
                            //Indicates that the server responded with a error response
                            Log.e("Volley3", "Error");
                        } else if (error instanceof NetworkError) {
                            //Indicates that there was network error while performing the request
                            Log.e("Volley4", "Error");
                        } else if (error instanceof ParseError) {
                            // Indicates that the server response could not be parsed
                            Log.e("Volley5", "Error");
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
        requestQueue.add(jor);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        //

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        JSONProccess3 ("http://192.168.0.110:80/energymeter/refresh.php");
        return super.onOptionsItemSelected(item);
    }
    public void JSONProccess3(String loginURL) {


        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, loginURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error");
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            //This indicates that the reuest has either time out or there is no connection
                            Log.e("Volley1", "Error");
                        } else if (error instanceof AuthFailureError) {
                            // Error indicating that there was an Authentication Failure while performing the request
                            Log.e("Volley2", "Error");
                        } else if (error instanceof ServerError) {
                            //Indicates that the server responded with a error response
                            Log.e("Volley3", "Error");
                        } else if (error instanceof NetworkError) {
                            //Indicates that there was network error while performing the request
                            Log.e("Volley4", "Error");
                        } else if (error instanceof ParseError) {
                            // Indicates that the server response could not be parsed
                            Log.e("Volley5", "Error");
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
        requestQueue.add(jor);

    }
    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));
        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());


    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);

        super.onPause();
    }


}





