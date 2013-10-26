package com.bruinlyfe.bruinlyfe;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by chris on 10/26/13.
 */
public class MenuLoader {
    private List<DiningHall> halls;

    public MenuLoader(List<DiningHall> allHalls) {
        halls = allHalls;
        downloadMenuData(); //temporary
    }

    public void downloadMenuData() {
        MenuDownloader dTask;
        dTask = new MenuDownloader();
        dTask.setMenuLoader(this);
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