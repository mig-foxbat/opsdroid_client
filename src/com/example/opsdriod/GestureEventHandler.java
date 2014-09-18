package com.example.opsdriod;

import android.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created by chlr on 9/18/14.
 */
public class GestureEventHandler implements GestureDetector.OnGestureListener {

    Fragment source;

    public GestureEventHandler(Fragment source) {
        this.source = source;
    }


    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float v, float v2) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float v, float v2) {
        Log.v(this.getClass().getName(),"OnFling Motion detected");
        return false;
    }
}
