package com.bruinlyfe.bruinlyfe;

import android.view.LayoutInflater;
import android.view.View;

/**
 * http://stackoverflow.com/questions/13590627/android-listview-headers
 */
public interface Item {
    public int getViewType();
    public View getView(LayoutInflater inflater, View convertView);
}
