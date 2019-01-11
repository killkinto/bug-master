package com.killkinto.bugmaster.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.widget.TextView;

import com.killkinto.bugmaster.R;

public class DangerLevelView extends TextView {

    public DangerLevelView(Context context) {
        super(context);
    }

    public DangerLevelView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DangerLevelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DangerLevelView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setDangerLevel(int dangerLevel) {
        setText(String.valueOf(dangerLevel));
        String color = getContext().getResources().getStringArray(R.array.dangerColors)[dangerLevel - 1];
        getBackground().setColorFilter(Color.parseColor(color), PorterDuff.Mode.SRC_ATOP);
    }

    public int getDangerLevel() {
        if (getText().length() != 0) {
            return Integer.parseInt(getText().toString());
        } else {
            return -1;
        }
    }
}
