package com.bruinlyfe.bruinlyfe;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

public class SwipeCalculatorActivity extends Activity {
    private SharedPreferences prefs;

    private SwipeCalculatorAndRecorder swipeCalc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_calculator);

        swipeCalc = new SwipeCalculatorAndRecorder(this.getApplicationContext());

        Spinner spinner = (Spinner) findViewById(R.id.spinner_meal_plan);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.meal_plan_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                swipeCalc.setMealPlan(SwipeCalculatorAndRecorder.MealPlan.values()[position]);
                displaySwipesLeft(swipeCalc.calculateSwipesLeft());
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
                swipeCalc.setWeekInPlan(newVal);
                displaySwipesLeft(swipeCalc.calculateSwipesLeft());
            }
        });

        prefs = getSharedPreferences("com.bruinlyfe.bruinlyfe", MODE_PRIVATE);
        spinner.setSelection(prefs.getInt("mealPlan", 0));

        picker.setValue(swipeCalc.getWeekInPlan());
        displayCountedSwipesLeft(swipeCalc.calculateRecordedSwipesLeft());
    }

    @Override
    protected void onPause() {
        super.onPause();
        swipeCalc.saveCurrentState();
    }

    private void displaySwipesLeft(int swipes) {
        TextView textView = (TextView)findViewById(R.id.textViewSwipesLeft);
        textView.setText(String.valueOf(swipes));
    }

    public void onSwipe(View view) {
        swipeCalc.swipesIncrement();
        displayCountedSwipesLeft(swipeCalc.calculateRecordedSwipesLeft());
    }

    public void onUnswipe(View view) {
        swipeCalc.swipesDecrement();
        displayCountedSwipesLeft(swipeCalc.calculateRecordedSwipesLeft());
    }

    private void displayCountedSwipesLeft(int swipes) {
        TextView countedSwipesView = (TextView) findViewById(R.id.textViewCountedSwipesLeft);
        countedSwipesView.setText(String.valueOf(swipes));
    }

    public void onReset(View view) {
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setMessage("Are you sure?").setPositiveButton("Yes", resetDialogClickListener)
                .setNegativeButton("No", resetDialogClickListener).show();
    }

    DialogInterface.OnClickListener resetDialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    swipeCalc.resetCountedSwipes();
                    displayCountedSwipesLeft(swipeCalc.calculateRecordedSwipesLeft());
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //"No" button clicked, so do nothing
                    break;
            }
        }
    };
}