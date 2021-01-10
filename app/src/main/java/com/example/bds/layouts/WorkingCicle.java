package com.example.bds.layouts;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class WorkingCicle extends View {

    public WorkingCicle(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        AnimationDrawable animation = (AnimationDrawable) getBackground();
        animation.start();
    }
}
