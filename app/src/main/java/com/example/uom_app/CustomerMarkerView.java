package com.example.uom_app;

import android.content.Context;
import android.graphics.Canvas;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;

import java.util.ArrayList;

public class CustomerMarkerView extends MarkerView {

    private TextView tvContent,tvContent1;
    ArrayList<String> mXLabels;


    public CustomerMarkerView(Context context, int layoutResource, ArrayList<String> xLabels) {
        super(context, layoutResource);
        // this markerview only displays a textview
        tvContent = (TextView) findViewById(R.id.tvContent);
        mXLabels = xLabels;
        tvContent1 = (TextView) findViewById(R.id.tvContent2);

    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        //tvContent.setText("" + e.getX() ); // set the entry-value as the display text

       // tvContent1.setText("" + e.describeContents());
        String xVal= mXLabels.get(e.getXIndex());
       tvContent.setText("DATE:"+xVal );
        tvContent1.setText("VALUE:"+e.getVal()  );

    }

    @Override
    public void draw(Canvas canvas, float posx, float posy)
    {
        // take offsets into consideration
        posx += getXOffset(1);
        posy=0;

        // AVOID OFFSCREEN
        if(posx<220)
            posx=220;
        if(posx>700)
            posx=700;

        // translate to the correct position and draw
        canvas.translate(posx, posy);
        draw(canvas);
        canvas.translate(-posx, -posy);
    }

    public int getXOffset(float xpos) {
        // this will center the marker-view horizontally
        return -(getWidth() / 2);
    }


    public int getYOffset(float ypos) {
        // this will cause the marker-view to be above the selected value
        return -getHeight();
    }
}

