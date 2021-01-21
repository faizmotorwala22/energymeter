package com.example.uom_app;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.android.volley.VolleyLog.TAG;

public class graph extends AppCompatActivity implements  AdapterView.OnItemSelectedListener
        ,DatePickerDialog.OnDateSetListener, View.OnClickListener {
    TextView camera, message, profile;
    // ViewPager viewPager;
    // PagerViewAdapter pagerViewAdapter;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    EditText selectDate,select;
    public Button graph;
    int i=0;
    String t1;
    String t2;
    String spi;

    private static View rootView;
    // private OnFragmentInteractionListener mListener;
    private SeekBar mSeekBarX, mSeekBarY;
    private TextView tvX, tvY;
    ArrayList<String> xVals = new ArrayList<String>();
    ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
    ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();
    DatePickerDialog datePickerDialog;
    ArrayList<Entry> x = new ArrayList<Entry>();;
    List<BarEntry> x1 = new ArrayList<>();;
    ArrayList<String> y=new ArrayList<String>();

    private LineChart mChart;
    private Spinner spinner;
    private static final String[] paths = {"LIGHT", "FAN"};
    //private Typeface tf;
    //private Fragment_Camera mInstance;
    RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
       // getSupportActionBar().setTitle("History");
        y.clear();
        x.clear();

        ArrayAdapter<String>adapter = new ArrayAdapter<String>(graph.this,
                android.R.layout.simple_spinner_item,paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);
       // spinner.setOnItemSelectedListener(this);
        selectDate = (EditText)findViewById(R.id.editText1); // getting the image button in fragment_blank.xml
        select = (EditText)findViewById(R.id.editText2);
        graph=(Button)findViewById(R.id.generate) ;
        graph.setOnClickListener(this);
        mChart = (LineChart) findViewById(R.id.chart1);


        selectDate.setFocusable(false);
        //Disable Your EditText

        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(graph.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                selectDate.setText(year + "-"
                                        + (monthOfYear + 1) + "-" + dayOfMonth);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        select.setFocusable(false);      //Disable Your EditText
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(graph.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                select.setText(year+ "-"
                                        + (monthOfYear + 1) + "-" + dayOfMonth);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });






        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
    @Override
    public void onClick(View v) {
        BackgroundTask backgroundTask = new BackgroundTask ();
        backgroundTask.execute (t1,t2,spi);


        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();

        t1=select.getText().toString();
        Log.d(TAG, "light"+t1);
        t2=selectDate.getText().toString();
//        spi = spinner.getSelectedItem().toString();
        try {
            //set time in mili
            Thread.sleep(3000);

        }catch (Exception e){
            e.printStackTrace();
        }
        jsonParse();
    }



    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) { // what should be done when a date is selected
        StringBuilder sb = new StringBuilder().append(year).append("-").append(monthOfYear + 1).append("-").append(dayOfMonth);
        String formattedDate = sb.toString();
        if(i==1){

            selectDate.setText(formattedDate);

        }
        else {
            select.setText(formattedDate);

        }
    }


    private void jsonParse() {


        String url = "http://192.168.43.77/energymeter/viewgraph.php";
        final RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            mChart.clear();
                            y.clear();
                            x.clear();


                            JSONArray ja = response.getJSONArray ("results");
                            for (int i = 0; i < ja.length(); i++) {
                                JSONObject jsonObject = ja.getJSONObject(i);
                                int value = jsonObject.getInt("used");
                                String date = jsonObject.getString("date");
                                x.add(new Entry(value, i));
                                y.add(date);

                            }





                            LineDataSet set1 = new LineDataSet(x,"FAN");
                            LineData data = new LineData(y,set1);
                            CustomerMarkerView mv=new CustomerMarkerView(graph.this,R.layout.content,y);
// set the marker to the chart
                            mChart.setMarkerView(mv);
                            mChart.setDrawMarkerViews(true);
                            set1.setDrawValues(false);



                            //PieData data = new PieData(y, dataSet);
                            // data.setValueFormatter(new DefaultValueFormatter(0));
                            mChart.setData(data);

                            //set1.setColors(ColorTemplate.COLORFUL_COLORS);

                            mChart.setTouchEnabled(true);

                            // enable scaling and dragging
                            mChart.setDragEnabled(true);
                            mChart.setScaleEnabled(true);

                            // if disabled, scaling can be done on x- and y-axis separately
                            mChart.setPinchZoom(false);


                            mChart.setDrawGridBackground(false);

                            XAxis x = mChart.getXAxis();


                            x.setEnabled(true);
                            x.setDrawGridLines(false);
                            x.setTextColor(Color.BLACK);
                            x.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);

                            YAxis y = mChart.getAxisLeft();
                            y.setLabelCount(6, false);
                            y.setTextColor(Color.BLACK);
                            y.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
                            y.setDrawGridLines(false);
                            y.setAxisLineColor(Color.WHITE);

                            mChart.getAxisRight().setEnabled(false);
                            mChart.getXAxis().setEnabled(true);
                            set1.setDrawCubic(true);
                            set1.setCubicIntensity(0.2f);
                            set1.setDrawCircles(true);
                            set1.setLineWidth(1.8f);set1.setCircleRadius(4f);
                            set1.setCircleColor(Color.WHITE);
                            set1.setHighLightColor(Color.rgb(244, 117, 117));
                            //set1.setColor(Color.BLUE);
                            set1.setFillColor(Color.BLUE);
                            set1.setFillAlpha(100);
                            set1.setDrawHorizontalHighlightIndicator(false);
                            set1.setDrawFilled(true);
                            if (Utils.getSDKInt() >= 18) {
                                // fill drawable only supported on api level 18 and above
                                Drawable drawable = ContextCompat.getDrawable(graph.this, R.drawable.fade_red);
                                set1.setFillDrawable(drawable);
                            }
                            else {
                                set1.setFillColor(Color.BLACK);
                            }



                            // add data


                            mChart.getLegend().setEnabled(false);

                            mChart.animateXY(2000, 2000);


                            // dont forget to refresh the drawing
                            mChart.invalidate();








                            ;
                            /*final String[] rate = new String[ja.length()];
                            final String[] date1 = new String[ja.length()];
                            for (int j = 0; j < ja.length(); j++) {
                                JSONObject jsonObject = ja.getJSONObject(j);
                                int value = jsonObject.getInt("temp1");
                                yVals1.add(new BarEntry(value, j));
                            }
                            for (int j = 0; j < ja.length(); j++) {
                                JSONObject jsonObject = ja.getJSONObject(j);
                                int value1 = jsonObject.getInt("temp2");
                                yVals2.add(new BarEntry(value1, j));
                            }
                            for (int j = 0; j < ja.length(); j++) {
                                JSONObject jsonObject = ja.getJSONObject(j);
                                String y = jsonObject.getString("data");
                                xVals.add(y);
                            }
                            BarDataSet set1 = new BarDataSet(yVals1, "Company A");
                            set1.setColor(Color.rgb(104, 241, 175));
                            BarDataSet set2 = new BarDataSet(yVals2, "Company B");
                            set2.setColor(Color.rgb(164, 228, 251));
                            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
                            dataSets.add((IBarDataSet) set1);
                            dataSets.add((IBarDataSet) set2);
                            BarData Data = new BarData(xVals, dataSets);




                            // add space between the dataset groups in percent of bar-width
                            Data.setGroupSpace(0);

                            mChart.setData(Data);
                            mChart.invalidate();*/


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        //creating a request queue
        //RequestQueue requestQueue = Volley.newRequestQueue(this);


        //adding the string request to request queue
        mQueue.add(request);
    }



   /* public void sending(View view) {
        BackgroundTask backgroundTask = new BackgroundTask ();
        backgroundTask.execute (t1,t2);
        Log.e("hiwee",t1);
    }*/




    class BackgroundTask extends AsyncTask<String,Void,String>
    {
        String add_info_url;
        @Override
        protected void onPreExecute( ) {
            add_info_url = "http://192.168.43.77/energymeter/viewchart.php";
        }


        @Override
        protected String doInBackground(String... args) {
            String t1,t2;

            //t1 = args[0];
            t1=select.getText().toString();
            t2=selectDate.getText().toString();

            Log.d(TAG, "light"+t1);
            //t2 = args[1];
            Log.d(TAG, "light"+t2);




            try {
                URL url = new URL (add_info_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection ();
                httpURLConnection.setRequestMethod ("POST");
                httpURLConnection.setDoOutput (true);
                OutputStream outputStream = httpURLConnection.getOutputStream ();
                BufferedWriter bufferedWriter = new BufferedWriter (new OutputStreamWriter(outputStream,"UTF-8"));
                String data_string = URLEncoder.encode ("light","UTF-8")+"="+URLEncoder.encode (t2,"UTF-8")+"&"+
                        URLEncoder.encode ("Fan","UTF-8")+"="+URLEncoder.encode (t1,"UTF-8");

                bufferedWriter.write (data_string);
                Log.d(TAG, "light"+data_string);
                bufferedWriter.flush ();
                bufferedWriter.close ();
                outputStream.close ();
                InputStream inputStream = httpURLConnection.getInputStream ();
                inputStream.close ();
                httpURLConnection.disconnect ();
                return "One row of data inserted..";
            } catch (MalformedURLException e) {
                e.printStackTrace ();
            } catch (IOException e) {
                e.printStackTrace ();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate (values);
        }
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText (getApplicationContext (),result,Toast.LENGTH_LONG).show();
        }




    }

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        switch (position) {
            case 0:
                // Whatever you want to happen when the first item gets selected
                break;
            case 1:
                // Whatever you want to happen when the second item gets selected
                break;
            case 2:
                // Whatever you want to happen when the thrid item gets selected
                break;

        }
    }


    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
    }

}








