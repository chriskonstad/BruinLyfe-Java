package com.bruinlyfe.bruinlyfe;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by chris on 9/29/13.
 */
public class TimeView extends LinearLayout {

    public TimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.time_view_res, this);
    }

    public TimeView(Context context) {
        this(context, null);
    }

    public void setOpenTime(String openTime) {
        TextView openView = (TextView)findViewById(R.id.textViewOpen);
        openView.setText(openTime);
    }

    public void setCloseTime(String closeTime) {
        TextView closeView = (TextView)findViewById(R.id.textViewClose);
        closeView.setText(closeTime);
    }
}
