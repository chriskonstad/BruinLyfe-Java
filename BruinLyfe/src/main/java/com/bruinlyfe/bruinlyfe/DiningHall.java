package com.bruinlyfe.bruinlyfe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chris on 10/13/13.
 */
public class DiningHall {
    public String name = "";
    public List<String> breakfast = new ArrayList<String>();
    public List<String> lunch = new ArrayList<String>();
    public List<String> dinner = new ArrayList<String>();
    public List<String> lateNight = new ArrayList<String>();
    public int timeViewIdB = 0;
    public int timeViewIdL = 0;
    public int timeViewIdD = 0;
    public int timeViewIdLN = 0;
    public List<Integer> timeViews = new ArrayList<Integer>();

    public DiningHall(String id, int timeViewB, int timeViewL, int timeViewD, int timeViewLN) {
        name = id;
        timeViews.add(timeViewB);
        timeViews.add(timeViewL);
        timeViews.add(timeViewD);
        timeViews.add(timeViewLN);
    }

}

