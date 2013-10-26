package com.bruinlyfe.bruinlyfe;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * http://stackoverflow.com/questions/13590627/android-listview-headers
 */
public class ListItem implements Item {
    private final String str1;
    private final LayoutInflater inflater;

    public ListItem(LayoutInflater inflater, String text1) {
        this.str1 = text1;
        this.inflater = inflater;
    }

    @Override
    public int getViewType() {
        return TwoTextArrayAdapter.RowType.LIST_ITEM.ordinal();
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        View view;
        if (convertView == null) {
            view = (View) inflater.inflate(R.layout.my_list_item, null);
            // Do some initialization
        } else {
            view = convertView;
        }

        TextView text1 = (TextView) view.findViewById(R.id.list_content1);
        text1.setText(str1);

        return view;
    }

}
