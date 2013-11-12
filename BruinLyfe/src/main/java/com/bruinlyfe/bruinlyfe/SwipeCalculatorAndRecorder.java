package com.bruinlyfe.bruinlyfe;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Calendar;

/**
 * Created by chris on 11/11/13.
 */
public class SwipeCalculatorAndRecorder {
    public enum MealPlan {
        Premier19, Regular19, Premier14, Regular14,
        Regular11
    }

    private MealPlan mealPlan;
    private int countedSwipes;
    private int totalSwipes;
    private int week;
    private SharedPreferences prefs;
    private Context mContext;
    private Calendar cal;

    public SwipeCalculatorAndRecorder(Context context) {
        mContext = context;
        prefs = mContext.getSharedPreferences("com.bruinlyfe.bruinlyfe", 0);

        mealPlan = MealPlan.values()[prefs.getInt("mealPlan", 0)];
        week = 1;

        //Load up the week
        int weekInYear = prefs.getInt("currentWeekInYear", 1);
        int weekInPlan = prefs.getInt("currentWeekInPlan", 1);
        cal = Calendar.getInstance();
        int currentWeekInYear = cal.get(Calendar.WEEK_OF_YEAR);
        //Sunday belongs to the previous week
        if(cal.get(Calendar.DAY_OF_WEEK) == cal.SUNDAY && currentWeekInYear >= 1)
            currentWeekInYear -= 1;

        //Try to guess the week in the quarter.  Doesn't have to be perfect because the quarters
        //end at weird times and the user can easily change it.
        if(currentWeekInYear - weekInYear >= 0) {    //This will help reset between fall and winter quarters
            weekInPlan = weekInPlan + (currentWeekInYear - weekInYear);
            if(1 <= weekInPlan && weekInPlan <= 10) {
                week = weekInPlan;
            }
        }
        //Load up the counted swipes
        totalSwipes = prefs.getInt("totalSwipes", 203);
        countedSwipes = prefs.getInt("countedSwipes", 0);
    }

    public int getWeekInPlan() {
        return week;
    }

    public void setWeekInPlan(int newWeek) {
        week = newWeek;
    }

    public int getSwipesLeft() {
        if(totalSwipes - countedSwipes < 0)
            return 0;
        else
            return totalSwipes - countedSwipes;
    }

    public void swipesIncrement() {
        if(totalSwipes - countedSwipes > 0)
            countedSwipes++;
    }

    public void swipesDecrement() {
        if(countedSwipes > 0)
            countedSwipes--;
    }

    public void setMealPlan(MealPlan plan) {
        mealPlan = plan;
    }

    public void resetCountedSwipes() {
        countedSwipes = 0;
        switch(mealPlan)
        {
            case Premier19:
                totalSwipes = 203;
                break;
            case Regular19:
                totalSwipes = 19;
                break;
            case Premier14:
                totalSwipes = 149;
                break;
            case Regular14:
                totalSwipes = 14;
                break;
            case Regular11:
                totalSwipes = 11;
                break;
            default:
                //do nothing
                break;
        }
    }

    public int calculateRecordedSwipesLeft() {
        if(totalSwipes - countedSwipes >= 0)
            return totalSwipes - countedSwipes;
        else
            return 0;
    }

    public int calculateSwipesLeft() {
        int swipesLeft = 0;
        int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

        //Shift days because monday == day #1
        day = day - 1;
        if(day == 0)
            day = 7;

        switch(mealPlan)
        {
            case Premier19:
                swipesLeft = 203;   //this number includes meals for finals week
                //week: [1,10]
                swipesLeft = swipesLeft - ((week - 1) * 19);
                //day: [1,7]
                for(int i=1;i<=day;i++) {   //day counter starts at 1
                    if(i < 6)
                        swipesLeft = swipesLeft - 3;
                    else
                        swipesLeft = swipesLeft - 2;
                }
                break;
            case Regular19: //done
                swipesLeft = 19;
                //day: [1,7]
                for(int i=1;i<=day;i++) {   //day counter starts at 1
                    if(i < 6)
                        swipesLeft = swipesLeft - 3;
                    else
                        swipesLeft = swipesLeft - 2;
                }
                break;
            case Premier14: //done
                swipesLeft = 149;   //this number includes finals week
                //week: [1,10]
                //day: [1,7]
                swipesLeft = swipesLeft - ((week - 1) * 14) - (day * 2);
                break;
            case Regular14: //done
                swipesLeft = 14;
                //day: [1,7]
                swipesLeft = swipesLeft - (day * 2);
                break;
            case Regular11: //done
                swipesLeft = 11;
                //day: [1,7]
                swipesLeft = swipesLeft - (day * 2);    //assume two meals a day per weekday
                if(swipesLeft < 0)
                    swipesLeft = 0;
                break;
            default:
                //do nothing
                break;
        }
        return swipesLeft;
    }

    public void saveCurrentState() {
        prefs.edit().putInt("mealPlan", mealPlan.ordinal()).commit();

        int currentWeekInYear = cal.get(Calendar.WEEK_OF_YEAR);
        //Sunday belongs to the previous week
        if(cal.get(Calendar.DAY_OF_WEEK) == cal.SUNDAY && currentWeekInYear >= 1)
            currentWeekInYear -= 1;
        prefs.edit().putInt("currentWeekInYear", currentWeekInYear).commit();
        prefs.edit().putInt("currentWeekInPlan", week).commit();

        //Save counted swipes
        prefs.edit().putInt("countedSwipes", countedSwipes).commit();

        //Save the total number of swipes
        prefs.edit().putInt("totalSwipes", totalSwipes).commit();
    }
}