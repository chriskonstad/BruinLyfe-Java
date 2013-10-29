package com.bruinlyfe.bruinlyfe;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chris on 9/29/13.
 */
public class TimeView extends LinearLayout {
    private Vibrator vib;

    public TimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.time_view_res, this);

        vib = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);

        this.setOnClickListener(onClickListener);
        this.setOnTouchListener(onTouchListener);
    }

    public TimeView(Context context) {
        this(context, null);
    }

    public void setOpenTime(String openTime) {
        TextView openView = (TextView)findViewById(R.id.textViewOpen);
        openView.setText(openTime);
        if(openTime.contains("CLOSED")) {
            openView.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        }
        else {
            openView.setTextColor(getResources().getColor(android.R.color.black));
        }
    }

    public void setCloseTime(String closeTime) {
        TextView closeView = (TextView)findViewById(R.id.textViewClose);
        closeView.setText(closeTime);
        if(closeTime.contains("CLOSED")) {
            closeView.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        }
        else {
            closeView.setTextColor(getResources().getColor(android.R.color.black));
        }
    }

    public void setBackgroundColor(int color) {
        TextView openView = (TextView)findViewById(R.id.textViewOpen);
        TextView closeView = (TextView)findViewById(R.id.textViewClose);

        openView.setBackgroundColor(color);
        closeView.setBackgroundColor(color);

    }

    public void resetBackgroundColor() {
        TextView openView = (TextView)findViewById(R.id.textViewOpen);
        TextView closeView = (TextView)findViewById(R.id.textViewClose);

        openView.setBackgroundColor(Integer.getInteger("#fff3f3f3", 16));
        closeView.setBackgroundColor(Integer.getInteger("#fff3f3f3", 16));
    }

    public void setTextColor(int color) {
        TextView openView = (TextView)findViewById(R.id.textViewOpen);
        TextView closeView = (TextView)findViewById(R.id.textViewClose);

        openView.setTextColor(color);
        closeView.setTextColor(color);
    }

    // Create an anonymous implementation of OnClickListener
    private OnClickListener onClickListener = new OnClickListener() {
        public void onClick(View v) {
            Log.w("BruinLyfe", "TIMEVIEWCLICKED");
            Log.w("BruinLyfe", String.valueOf(v.getId()));
            int id = v.getId();
            MainActivity main = (MainActivity)v.getRootView().getContext();

            Intent intent = new Intent(v.getRootView().getContext(), MenuDisplayActivity.class);

            Map<String, String> hallMap = new HashMap<String, String>();
            hallMap.put("bcafe", getResources().getString(R.string.bcafe));
            hallMap.put("covel", getResources().getString(R.string.covel));
            hallMap.put("deneve", getResources().getString(R.string.deneve));
            hallMap.put("feast", getResources().getString(R.string.feast));
            hallMap.put("hedrick", getResources().getString(R.string.hedrick));
            hallMap.put("nineteen", getResources().getString(R.string.cafe1919));
            hallMap.put("rende", getResources().getString(R.string.rendezvous));

            //TODO: First, check to see if there is menu data to load!
            for(int i=0;i<main.halls.size();i++) {
                for(int j=0;j<main.halls.get(i).timeViews.size();j++) {
                    if(main.halls.get(i).timeViews.get(j) == id) {
                        String hallMeal = hallMap.get(main.halls.get(i).name);
                        hallMeal += " - ";
                        switch(j) {
                            case 0:
                                if(!main.halls.get(i).breakfast.isEmpty())
                                    intent.putStringArrayListExtra("menuData", (ArrayList<String>)main.halls.get(i).breakfast);
                                    hallMeal += getResources().getString(R.string.breakfast);
                                break;
                            case 1:
                                if(!main.halls.get(i).lunch.isEmpty())
                                    intent.putStringArrayListExtra("menuData", (ArrayList<String>)main.halls.get(i).lunch);
                                    hallMeal += getResources().getString(R.string.lunch);
                                break;
                            case 2:
                                if(!main.halls.get(i).dinner.isEmpty())
                                    intent.putStringArrayListExtra("menuData", (ArrayList<String>)main.halls.get(i).dinner);
                                    hallMeal += getResources().getString(R.string.dinner);
                                break;
                            case 3:
                                if(!main.halls.get(i).lateNight.isEmpty())
                                    intent.putStringArrayListExtra("menuData", (ArrayList<String>)main.halls.get(i).lateNight);
                                    hallMeal += getResources().getString(R.string.late_night);
                                break;
                        }
                        intent.putExtra("hallMeal", hallMeal);
                    }
                }
            }

            TextView openView = (TextView)v.findViewById(R.id.textViewOpen);

            //Only launch menu-display activity if there is data to load and location is open
            if(intent.hasExtra("menuData") && !openView.getText().toString().contains("CLOSED")) {
                main.startActivity(intent);
            }
            else if(intent.hasExtra("menuData") && openView.getText().toString().contains("CLOSED")) {
                Toast toast = Toast.makeText(v.getContext(), "Location closed", Toast.LENGTH_SHORT);
                toast.show();
            }
            else if(!intent.hasExtra("menuData") && openView.getText().toString().contains("CLOSED")) {
                Toast toast = Toast.makeText(v.getContext(), "Location closed", Toast.LENGTH_SHORT);
                toast.show();
            }
            else {
                Toast toast = Toast.makeText(v.getContext(), "No menu data", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    };

    private OnTouchListener onTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN ) {
                //if pressed
                vib.vibrate(50);
                setBackgroundColor(getResources().getColor(R.color.ucla_gold));
            }
            else if(event.getAction() == MotionEvent.ACTION_UP) {
                //if released
                resetBackgroundColor();
            }
            return false;
        }
    };
}

