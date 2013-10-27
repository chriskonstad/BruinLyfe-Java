package com.bruinlyfe.bruinlyfe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends FragmentActivity {
    MenuLoader menuLoader;
    SharedPreferences prefs = null;

    public DiningHall bcafe = new DiningHall("bcafe", R.id.timeViewBcafeBreakfast, R.id.timeViewBcafeLunch, R.id.timeViewBcafeDinner, R.id.timeViewBcafeLateNight);
    public DiningHall covel = new DiningHall("covel", R.id.timeViewCovelBreakfast, R.id.timeViewCovelLunch, R.id.timeViewCovelDinner, R.id.timeViewCovelLateNight);
    public DiningHall deneve = new DiningHall("deneve", R.id.timeViewDeneveBreakfast, R.id.timeViewDeneveLunch, R.id.timeViewDeneveDinner, R.id.timeViewDeneveLateNight);
    public DiningHall feast = new DiningHall("feast", R.id.timeViewFeastBreakfast, R.id.timeViewFeastLunch, R.id.timeViewFeastDinner, R.id.timeViewFeastLateNight);
    public DiningHall hedrick = new DiningHall("hedrick", R.id.timeViewHedrickBreakfast, R.id.timeViewHedrickLunch, R.id.timeViewHedrickDinner, R.id.timeViewHedrickLateNight);
    public DiningHall nineteen = new DiningHall("nineteen", R.id.timeViewCafe1919Breakfast, R.id.timeViewCafe1919Lunch, R.id.timeViewCafe1919Dinner, R.id.timeViewCafe1919LateNight);
    public DiningHall rende = new DiningHall("rende", R.id.timeViewRenBreakfast, R.id.timeViewRenLunch, R.id.timeViewRenDinner, R.id.timeViewRenLateNight);

    List<DiningHall> halls = new ArrayList<DiningHall>();   //TODO: Append dining halls

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_dining_hours);

        //Build halls list
        halls.add(bcafe);
        halls.add(covel);
        halls.add(deneve);
        halls.add(feast);
        halls.add(hedrick);
        halls.add(nineteen);
        halls.add(rende);

        menuLoader = new MenuLoader(halls);
        loadInfo();

        //Check if first run, and if so, then load the tutorial
        prefs = getSharedPreferences("com.bruinlyfe.bruinlyfe", MODE_PRIVATE);
        if(prefs.getBoolean("firstRun", true)) {
            Intent intent = new Intent(getApplicationContext(), TutorialActivity.class);
            startActivity(intent);
        }
    }

    public void loadInfo() {
        DownloadTask dTask;
        dTask = new DownloadTask();
        dTask.setParentFragment(this);
        dTask.execute("http://secure5.ha.ucla.edu/restauranthours/dining-hall-hours-by-day.cfm");
    }

    public void fillDiningHours(List<String> stringList) {
        TimeView[] timeViews;
        timeViews = new TimeView[32];
        timeViews[0] = (TimeView)findViewById(R.id.timeViewBcafeBreakfast);
        timeViews[1] = (TimeView)findViewById(R.id.timeViewBcafeLunch);
        timeViews[2] = (TimeView)findViewById(R.id.timeViewBcafeDinner);
        timeViews[3] = (TimeView)findViewById(R.id.timeViewBcafeLateNight);

        timeViews[4] = (TimeView)findViewById(R.id.timeViewBplateBreakfast);
        timeViews[5] = (TimeView)findViewById(R.id.timeViewBplateLunch);
        timeViews[6] = (TimeView)findViewById(R.id.timeViewBplateDinner);
        timeViews[7] = (TimeView)findViewById(R.id.timeViewBplateLateNight);

        timeViews[8] = (TimeView)findViewById(R.id.timeViewCafe1919Breakfast);
        timeViews[9] = (TimeView)findViewById(R.id.timeViewCafe1919Lunch);
        timeViews[10] = (TimeView)findViewById(R.id.timeViewCafe1919Dinner);
        timeViews[11] = (TimeView)findViewById(R.id.timeViewCafe1919LateNight);

        timeViews[12] = (TimeView)findViewById(R.id.timeViewCovelBreakfast);
        timeViews[13] = (TimeView)findViewById(R.id.timeViewCovelLunch);
        timeViews[14] = (TimeView)findViewById(R.id.timeViewCovelDinner);
        timeViews[15] = (TimeView)findViewById(R.id.timeViewCovelLateNight);

        timeViews[16] = (TimeView)findViewById(R.id.timeViewDeneveBreakfast);
        timeViews[17] = (TimeView)findViewById(R.id.timeViewDeneveLunch);
        timeViews[18] = (TimeView)findViewById(R.id.timeViewDeneveDinner);
        timeViews[19] = (TimeView)findViewById(R.id.timeViewDeneveLateNight);

        timeViews[20] = (TimeView)findViewById(R.id.timeViewFeastBreakfast);
        timeViews[21] = (TimeView)findViewById(R.id.timeViewFeastLunch);
        timeViews[22] = (TimeView)findViewById(R.id.timeViewFeastDinner);
        timeViews[23] = (TimeView)findViewById(R.id.timeViewFeastLateNight);

        timeViews[24] = (TimeView)findViewById(R.id.timeViewHedrickBreakfast);
        timeViews[25] = (TimeView)findViewById(R.id.timeViewHedrickLunch);
        timeViews[26] = (TimeView)findViewById(R.id.timeViewHedrickDinner);
        timeViews[27] = (TimeView)findViewById(R.id.timeViewHedrickLateNight);

        timeViews[28] = (TimeView)findViewById(R.id.timeViewRenBreakfast);
        timeViews[29] = (TimeView)findViewById(R.id.timeViewRenLunch);
        timeViews[30] = (TimeView)findViewById(R.id.timeViewRenDinner);
        timeViews[31] = (TimeView)findViewById(R.id.timeViewRenLateNight);

        int j = 1;
        for(int i=0;i<timeViews.length;i++)
        {
            if(stringList.size() >= j) {
                timeViews[i].setOpenTime(stringList.get(j-1));
                timeViews[i].setCloseTime(stringList.get(j));
                //check to see if current time is in this time frame,
                //and if so, color the text green
                if(!stringList.get(j-1).contains("CLOSED") && !stringList.get(j).contains("CLOSED")) {
                    try {
                        Calendar currentDate = Calendar.getInstance();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mma");
                        Calendar openDate = Calendar.getInstance();
                        openDate.setTime(dateFormat.parse(stringList.get(j-1), new ParsePosition(0)));
                        openDate.set(Calendar.YEAR, currentDate.get(Calendar.YEAR));
                        openDate.set(Calendar.MONTH, currentDate.get(Calendar.MONTH));
                        openDate.set(Calendar.DAY_OF_MONTH, currentDate.get(Calendar.DAY_OF_MONTH));
                        Calendar closeDate = Calendar.getInstance();
                        closeDate.setTime(dateFormat.parse(stringList.get(j), new ParsePosition(0)));
                        closeDate.set(Calendar.YEAR, currentDate.get(Calendar.YEAR));
                        closeDate.set(Calendar.MONTH, currentDate.get(Calendar.MONTH));
                        closeDate.set(Calendar.DAY_OF_MONTH, currentDate.get(Calendar.DAY_OF_MONTH));
                        if(closeDate.get(Calendar.HOUR_OF_DAY) < 5 && currentDate.get(Calendar.HOUR_OF_DAY) >= 5) {  //if less than 5am, then it is really the next day in the early morning, i.e. 2am
                            closeDate.set(Calendar.DAY_OF_MONTH, currentDate.get(Calendar.DAY_OF_MONTH) + 1);
                            Log.w("BruinLyfe", "HAD TO ADD ONE DAY TO THE CLOSE TIME");
                        }

                        //If the openDate was really the day before
                        if(openDate.compareTo(closeDate) == 1) {    //if open date is after close date (i.e. late night)
                            openDate.set(Calendar.DAY_OF_MONTH, openDate.get(Calendar.DAY_OF_MONTH) - 1);
                        }

                        Log.w("BruinLyfe", "-------------------------------------");
                        Log.w("BruinLyfe", "OPEN DATE: " + openDate.get(Calendar.DAY_OF_MONTH) + '\t' + openDate.get(Calendar.HOUR_OF_DAY));
                        Log.w("BruinLyfe", "CURRENT DATE: " + currentDate.get(Calendar.DAY_OF_MONTH) + '\t' + currentDate.get(Calendar.HOUR_OF_DAY));
                        Log.w("BruinLyfe", "CLOSE DATE: " + closeDate.get(Calendar.DAY_OF_MONTH) + '\t' + closeDate.get(Calendar.HOUR_OF_DAY));
                        //If current time is in range [openTime, closeTime]
                        if(currentDate.compareTo(openDate) != -1 && currentDate.compareTo(closeDate) != 1)
                            timeViews[i].setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
                j = j+2;
            }
        }
    }

    public List<String> findMatches(String htmlSource) {

        //Create the string array
        Pattern p = Pattern.compile("<strong>\\s*.*\\s*</strong>");
        Matcher m = p.matcher(htmlSource);
        List<String> matches = new ArrayList<String>();
        while(m.find()){
            matches.add(m.group());
        }

        //Remove the HTML tags
        for(int i=0;i<matches.size();i++) {
            matches.set(i, matches.get(i).replace("<strong>", ""));
            matches.set(i, matches.get(i).replace("</strong>", ""));
            matches.set(i, matches.get(i).replace(" -", ""));
        }

        List<String> cleanMatches = new ArrayList<String>();
        Pattern pClean = Pattern.compile("CLOSED|\\d*:\\d*\\w\\w");

        for(int i=0;i<matches.size();i++) {
            Matcher mClean = pClean.matcher(matches.get(i));
            while(mClean.find()) {
                cleanMatches.add(mClean.group());
                int index = cleanMatches.size() - 1;
                if(cleanMatches.get(index).contains("CLOSED")) {
                    cleanMatches.add(mClean.group());
                }
            }
        }

        return cleanMatches;
    }
}