package com.bruinlyfe.bruinlyfe;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by chris on 9/29/13.
 */
public class TimeView extends LinearLayout {

    public TimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.time_view_res, this);
        this.setOnClickListener(onClickListener);
    }

    public TimeView(Context context) {
        this(context, null);
    }

    public void setOpenTime(String openTime) {
        TextView openView = (TextView)findViewById(R.id.textViewOpen);
        openView.setText(openTime);
    }

    public void setCloseTime(String closeTime) {
        TextView closeView = (TextView)findViewById(R.id.textViewClose);
        closeView.setText(closeTime);
    }

    // Create an anonymous implementation of OnClickListener
    private OnClickListener onClickListener = new OnClickListener() {
        public void onClick(View v) {
            Log.w("BruinLyfe", "TIMEVIEWCLICKED");
            Log.w("BruinLyfe", String.valueOf(v.getId()));
            int id = v.getId();
            MainActivity main = (MainActivity)v.getRootView().getContext();

            Intent intent = new Intent(v.getRootView().getContext(), MenuDisplayActivity.class);

            //TODO: First, check to see if there is menu data to load!
            for(int i=0;i<main.halls.size();i++) {
                for(int j=0;j<main.halls.get(i).timeViews.size();j++) {
                    if(main.halls.get(i).timeViews.get(j) == id) {
                        switch(j) {
                            case 0:
                                if(!main.halls.get(i).breakfast.isEmpty())
                                    intent.putStringArrayListExtra("menuData", (ArrayList<String>)main.halls.get(i).breakfast);
                                break;
                            case 1:
                                if(!main.halls.get(i).lunch.isEmpty())
                                    intent.putStringArrayListExtra("menuData", (ArrayList<String>)main.halls.get(i).lunch);
                                break;
                            case 2:
                                if(!main.halls.get(i).dinner.isEmpty())
                                    intent.putStringArrayListExtra("menuData", (ArrayList<String>)main.halls.get(i).dinner);
                                break;
                            case 3:
                                if(!main.halls.get(i).lateNight.isEmpty())
                                    intent.putStringArrayListExtra("menuData", (ArrayList<String>)main.halls.get(i).lateNight);
                                break;
                        }
                    }
                }
            }

            /*
            if(id == R.id.timeViewCafe1919Breakfast) {
                intent.putStringArrayListExtra("menuData", (ArrayList<String>)main.nineteen.breakfast);
            }
            else if(id == R.id.timeViewCovelBreakfast) {
                intent.putStringArrayListExtra("menuData", (ArrayList<String>)main.covel.breakfast);
            }
            else if(id == R.id.timeViewCovelLunch) {
                intent.putStringArrayListExtra("menuData", (ArrayList<String>)main.covel.lunch);
            }
            else if(id == R.id.timeViewCovelDinner) {
                intent.putStringArrayListExtra("menuData", (ArrayList<String>)main.covel.dinner);
            }
            */


            if(intent.hasExtra("menuData"))   //only launch activity if there is data to load
                main.startActivity(intent);

        }
    };
}

