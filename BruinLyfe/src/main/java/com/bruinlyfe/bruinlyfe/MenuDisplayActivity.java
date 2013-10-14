package com.bruinlyfe.bruinlyfe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

/**
 * Created by chris on 10/10/13.
 */
public class MenuDisplayActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_display);

        Intent intent = this.getIntent();
        List<String> meal = intent.getExtras().getStringArrayList("menuData");
        ListView lv = (ListView)findViewById(R.id.listViewMenu);
        ArrayAdapter<String> menuAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, meal);
        lv.setAdapter(menuAdapter);
    }
}
