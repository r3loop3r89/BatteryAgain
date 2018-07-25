package com.shra1.batteryagain.customviews;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.shra1.batteryagain.utils.FontCache;

public class ShraTextView extends AppCompatTextView {

    float RADIUS = 5f;
    float dx = 4f;
    float dy = 4f;


    public ShraTextView(Context context) {
        super(context);
        setProperties(context);
    }

    public ShraTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setProperties(context);
    }

    public ShraTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setProperties(context);
    }

    private void setProperties(Context context) {
        setShadowLayer(RADIUS, dx, dy, Color.GRAY);
        /*Typeface typeface = FontCache.get("fonts/customfont.ttf", context);
        setTypeface(typeface);*/
    }

}
