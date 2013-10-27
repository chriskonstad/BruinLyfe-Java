package com.bruinlyfe.bruinlyfe;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

/**
 * Created by chris on 10/27/13.
 */
public class TutorialActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
    }

    public void onGotIt(View view) {
        //Disable the tutorial on the next run
        SharedPreferences prefs = getSharedPreferences("com.bruinlyfe.bruinlyfe", MODE_PRIVATE);
        prefs.edit().putBoolean("firstRun", false).commit();
        finish();
    }

    public void onShowNextTime(View view) {
        //Don't change anything
        finish();
    }
}
