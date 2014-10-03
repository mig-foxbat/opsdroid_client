package com.example.opsdriod;

import android.app.Activity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created by chlr on 9/18/14.
 */
public class GestureEventHandler implements GestureDetector.OnGestureListener {

    Activity source;

    public GestureEventHandler(Activity source) {
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
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float v, float v2) {
        return true;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent me1, MotionEvent me2, float v, float v2) {
        if (Math.abs(me1.getX() - me2.getX()) > Math.abs(me1.getY() - me2.getY())) {
       //     ((MainActivity) this.source).changeFragment(me1.getX() < me1.getY()?true:false);
            Log.v(this.getClass().getName(), "OnFling Motion detected");
        }
        return false;
    }
}
