package com.bruinlyfe.bruinlyfe;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

    HoursFragment hoursFragment;
    MenuLoader menuLoader;

    public DiningHall bcafe = new DiningHall("bcafe", R.id.timeViewBcafeBreakfast, R.id.timeViewBcafeLunch, R.id.timeViewBcafeDinner, R.id.timeViewBcafeLateNight);
    public DiningHall covel = new DiningHall("covel", R.id.timeViewCovelBreakfast, R.id.timeViewCovelLunch, R.id.timeViewCovelDinner, R.id.timeViewCovelLateNight);
    public DiningHall deneve = new DiningHall("deneve", R.id.timeViewDeneveBreakfast, R.id.timeViewDeneveLunch, R.id.timeViewDeneveDinner, R.id.timeViewDeneveLateNight);
    public DiningHall feast = new DiningHall("feast", R.id.timeViewFeastBreakfast, R.id.timeViewFeastLunch, R.id.timeViewFeastDinner, R.id.timeViewFeastLateNight);
    public DiningHall hedrick = new DiningHall("hedrick", R.id.timeViewHedrickBreakfast, R.id.timeViewHedrickLunch, R.id.timeViewHedrickDinner, R.id.timeViewHedrickLateNight);
    public DiningHall nineteen = new DiningHall("nineteen", R.id.timeViewCafe1919Breakfast, R.id.timeViewCafe1919Lunch, R.id.timeViewCafe1919Dinner, R.id.timeViewCafe1919LateNight);
    public DiningHall rende = new DiningHall("rende", R.id.timeViewRenBreakfast, R.id.timeViewRenLunch, R.id.timeViewRenDinner, R.id.timeViewRenLateNight);

    List<DiningHall> halls = new ArrayList<DiningHall>();   //TODO: Append dining halls

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
     * will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the app.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }

        //Build halls list
        halls.add(bcafe);
        halls.add(covel);
        halls.add(deneve);
        halls.add(feast);
        halls.add(hedrick);
        halls.add(nineteen);
        halls.add(rende);

        menuLoader = new MenuLoader();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a DummySectionFragment (defined as a static inner class
            // below) with the page number as its lone argument.
            if(position != 0)
            {
                Fragment fragment = new DummySectionFragment();
                Bundle args = new Bundle();
                args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
                fragment.setArguments(args);
                return fragment;
            }
            else
            {
                HoursFragment fragment = new HoursFragment();
                Bundle args = new Bundle();
                args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
                fragment.setArguments(args);
                hoursFragment = fragment;
                //hoursFragment.loadInfo();
                return fragment;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }

    /**
     * A dummy fragment representing a section of the app, but that simply
     * displays dummy text.
     */
    public class DummySectionFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        public static final String ARG_SECTION_NUMBER = "section_number";

        public DummySectionFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main_dummy, container, false);
            TextView dummyTextView = (TextView) rootView.findViewById(R.id.section_label);
            dummyTextView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A dummy fragment representing a section of the app, but that simply
     * displays dummy text.
     */
    public class HoursFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        View rootView;

        public HoursFragment() {
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.frag_dining_hours, container, false);
            loadInfo();
            return rootView;
        }

        public void loadInfo() {
            DownloadTask dTask;
            dTask = new DownloadTask();
            dTask.execute("http://secure5.ha.ucla.edu/restauranthours/dining-hall-hours-by-day.cfm");
        }

        public void fillDiningHours(List<String> stringList) {
            TimeView[] timeViews;
            timeViews = new TimeView[32];
            timeViews[0] = (TimeView)rootView.findViewById(R.id.timeViewBcafeBreakfast);
            timeViews[1] = (TimeView)rootView.findViewById(R.id.timeViewBcafeLunch);
            timeViews[2] = (TimeView)rootView.findViewById(R.id.timeViewBcafeDinner);
            timeViews[3] = (TimeView)rootView.findViewById(R.id.timeViewBcafeLateNight);

            timeViews[4] = (TimeView)rootView.findViewById(R.id.timeViewBplateBreakfast);
            timeViews[5] = (TimeView)rootView.findViewById(R.id.timeViewBplateLunch);
            timeViews[6] = (TimeView)rootView.findViewById(R.id.timeViewBplateDinner);
            timeViews[7] = (TimeView)rootView.findViewById(R.id.timeViewBplateLateNight);

            timeViews[8] = (TimeView)rootView.findViewById(R.id.timeViewCafe1919Breakfast);
            timeViews[9] = (TimeView)rootView.findViewById(R.id.timeViewCafe1919Lunch);
            timeViews[10] = (TimeView)rootView.findViewById(R.id.timeViewCafe1919Dinner);
            timeViews[11] = (TimeView)rootView.findViewById(R.id.timeViewCafe1919LateNight);

            timeViews[12] = (TimeView)rootView.findViewById(R.id.timeViewCovelBreakfast);
            timeViews[13] = (TimeView)rootView.findViewById(R.id.timeViewCovelLunch);
            timeViews[14] = (TimeView)rootView.findViewById(R.id.timeViewCovelDinner);
            timeViews[15] = (TimeView)rootView.findViewById(R.id.timeViewCovelLateNight);

            timeViews[16] = (TimeView)rootView.findViewById(R.id.timeViewDeneveBreakfast);
            timeViews[17] = (TimeView)rootView.findViewById(R.id.timeViewDeneveLunch);
            timeViews[18] = (TimeView)rootView.findViewById(R.id.timeViewDeneveDinner);
            timeViews[19] = (TimeView)rootView.findViewById(R.id.timeViewDeneveLateNight);

            timeViews[20] = (TimeView)rootView.findViewById(R.id.timeViewFeastBreakfast);
            timeViews[21] = (TimeView)rootView.findViewById(R.id.timeViewFeastLunch);
            timeViews[22] = (TimeView)rootView.findViewById(R.id.timeViewFeastDinner);
            timeViews[23] = (TimeView)rootView.findViewById(R.id.timeViewFeastLateNight);

            timeViews[24] = (TimeView)rootView.findViewById(R.id.timeViewHedrickBreakfast);
            timeViews[25] = (TimeView)rootView.findViewById(R.id.timeViewHedrickLunch);
            timeViews[26] = (TimeView)rootView.findViewById(R.id.timeViewHedrickDinner);
            timeViews[27] = (TimeView)rootView.findViewById(R.id.timeViewHedrickLateNight);

            timeViews[28] = (TimeView)rootView.findViewById(R.id.timeViewRenBreakfast);
            timeViews[29] = (TimeView)rootView.findViewById(R.id.timeViewRenLunch);
            timeViews[30] = (TimeView)rootView.findViewById(R.id.timeViewRenDinner);
            timeViews[31] = (TimeView)rootView.findViewById(R.id.timeViewRenLateNight);

            //Example code that works for later
            /*
            TableRow tableRow = (TableRow)rootView.findViewById(R.id.tableRowCovel);
            TimeView tv = (TimeView)tableRow.getChildAt(1);
            tv.setOpenTime("HELLO");
            tv.setCloseTime("GOODBYE");
            */

            int j = 1;
            int timeCount = stringList.size();
            int timeViewCount = timeViews.length;
            Log.w("BruinLyfe", "TimeCount: " + String.valueOf(timeCount));
            Log.w("BruinLyfe", "TimeViews: " + String.valueOf(timeViewCount));
            for(int i=0;i<timeViews.length;i++)
            {
                if(stringList.size() >= j) {
                timeViews[i].setOpenTime(stringList.get(j-1));
                timeViews[i].setCloseTime(stringList.get(j));
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

    public class MenuLoader {

        public MenuLoader() {
            downloadMenuData(); //temporary
        }

        public void downloadMenuData() {
            MenuDownloader dTask;
            dTask = new MenuDownloader();
            dTask.execute("http://rest.s3for.me/06161995/mealdata.json");
        }

        public void parseData(String data) {
            JSONObject allData;

            for(int j=0;j<halls.size();j++) {
                JSONObject hallData;
                JSONArray hallBreakfast;
                JSONArray hallLunch;
                JSONArray hallDinner;
                JSONArray hallLateNight;
                try {
                    allData = new JSONObject(data);
                    hallData = allData.getJSONObject(halls.get(j).name);    //get data for that hall
                    if(hallData.has("breakfast"))
                        hallBreakfast = hallData.getJSONArray("breakfast");
                    else
                        hallBreakfast = new JSONArray();
                    if(hallData.has("lunch"))
                        hallLunch = hallData.getJSONArray("lunch");
                    else
                        hallLunch = new JSONArray();
                    if(hallData.has("dinner"))
                        hallDinner = hallData.getJSONArray("dinner");
                    else
                        hallDinner = new JSONArray();
                    if(allData.has("late")) //lateNight is in the root object
                        hallLateNight = allData.getJSONArray("late");
                    else
                        hallLateNight = new JSONArray();

                    halls.get(j).breakfast.clear();
                    for(int i=0; i<hallBreakfast.length();i++) {
                        halls.get(j).breakfast.add(hallBreakfast.get(i).toString());
                    }
                    halls.get(j).lunch.clear();
                    for(int i=0;i<hallLunch.length();i++) {
                        halls.get(j).lunch.add(hallLunch.get(i).toString());
                    }

                    halls.get(j).dinner.clear();
                    for(int i=0;i<hallDinner.length();i++) {
                        halls.get(j).dinner.add(hallDinner.get(i).toString());
                    }

                    halls.get(j).lateNight.clear();
                    for(int i=0;i<hallLateNight.length();i++) {
                        halls.get(j).lateNight.add(hallLateNight.get(i).toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //Special case for special dining halls
            try {
                allData = new JSONObject(data);
                JSONArray rendeData = allData.getJSONArray("rende");
                for(int i=0;i<rendeData.length();i++) {
                    final int r = 6;
                    halls.get(r).breakfast.add(rendeData.get(i).toString());
                    halls.get(r).lunch.add(rendeData.get(i).toString());
                    halls.get(r).dinner.add(rendeData.get(i).toString());
                    halls.get(r).lateNight.add(rendeData.get(i).toString());
                }
                JSONArray bcafeData = allData.getJSONArray("bcafe");
                for(int i=0;i<bcafeData.length();i++) {
                    final int b = 0;
                    halls.get(b).breakfast.add(bcafeData.get(i).toString());
                    halls.get(b).lunch.add(bcafeData.get(i).toString());
                    halls.get(b).dinner.add(bcafeData.get(i).toString());
                    halls.get(b).lateNight.add(bcafeData.get(i).toString());
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
            Log.w("BruinLyfe", "Done parsing menu data!");
        }
    }

    public class DownloadTask extends AsyncTask<String, Void, String> {
        String finalResult = "";

        @Override
        protected String doInBackground(String... urls) {
            HttpResponse response = null;
            HttpGet httpGet = null;
            HttpClient mHttpClient = null;
            String s = "";

            try {
                if(mHttpClient == null){
                    mHttpClient = new DefaultHttpClient();
                }
                httpGet = new HttpGet(urls[0]);
                response = mHttpClient.execute(httpGet);
                s = EntityUtils.toString(response.getEntity(), "UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
            finalResult = s;
            return s;
        }

        @Override
        protected void onPostExecute(String result){
            //Do something cool?
            Log.w("BruinLyfe", "Done downloading!");
            hoursFragment.fillDiningHours(hoursFragment.findMatches(result));
        }

        public String getFinalResult() {
            return finalResult;
        }
    }

    public class MenuDownloader extends DownloadTask {
        @Override
        protected  void onPostExecute(String result) {
            Log.w("BruinLyfe", "Done downloading menu data!");
            menuLoader.parseData(result);
        }
    }
}