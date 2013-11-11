package com.bruinlyfe.bruinlyfe;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;

public class SwipeCalculatorActivity extends Activity {
    public enum MealPlan {
        Premier19, Regular19, Premier14, Regular14,
        Regular11
    }

    private MealPlan mealPlan;
    private int week;
    private SharedPreferences prefs;
    private Calendar cal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_calculator);

        Spinner spinner = (Spinner) findViewById(R.id.spinner_meal_plan);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.meal_plan_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                setMealPlan(MealPlan.values()[position]);
                displaySwipesLeft(calculateSwipesLeft());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                //do nothing
            }
        });

        NumberPicker picker = (NumberPicker) findViewById(R.id.numberPickerWeek);
        picker.setMaxValue(10);
        picker.setMinValue(1);

        picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                week = newVal;
                displaySwipesLeft(calculateSwipesLeft());
            }
        });

        prefs = getSharedPreferences("com.bruinlyfe.bruinlyfe", MODE_PRIVATE);
        spinner.setSelection(prefs.getInt("mealPlan", 0));
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
            Log.w("BruinLyfe", "Calculated Week: " + String.valueOf(weekInPlan));
            if(1 <= weekInPlan && weekInPlan <= 10) {
                week = weekInPlan;
                picker.setValue(weekInPlan);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        prefs.edit().putInt("mealPlan", mealPlan.ordinal()).commit();

        int currentWeekInYear = cal.get(Calendar.WEEK_OF_YEAR);
        //Sunday belongs to the previous week
        if(cal.get(Calendar.DAY_OF_WEEK) == cal.SUNDAY && currentWeekInYear >= 1)
            currentWeekInYear -= 1;
        prefs.edit().putInt("currentWeekInYear", currentWeekInYear).commit();
        prefs.edit().putInt("currentWeekInPlan", week).commit();
    }

    private void setMealPlan(MealPlan plan) {
        mealPlan = plan;
    }

    private int calculateSwipesLeft() {
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

    private void displaySwipesLeft(int swipes) {
        TextView textView = (TextView)findViewById(R.id.textViewSwipesLeft);
        textView.setText(String.valueOf(swipes));
    }
}