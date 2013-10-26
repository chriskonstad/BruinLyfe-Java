package com.bruinlyfe.bruinlyfe;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * http://stackoverflow.com/questions/13590627/android-listview-headers
 */
public class Header implements Item {
    private final String name;

    public Header(LayoutInflater inflater, String name) {
        this.name = name;
    }

    @Override
    public int getViewType() {
        return TwoTextArrayAdapter.RowType.HEADER_ITEM.ordinal();
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        View view;
        if (convertView == null) {
            view = (View) inflater.inflate(R.layout.header, null);
            // Do some initialization
        } else {
            view = convertView;
        }

        TextView text = (TextView) view.findViewById(R.id.separator);
        text.setText(name);

        return view;
    }
}