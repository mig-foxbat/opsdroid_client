package com.example.opsdriod;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;



public class MyActivity extends Activity implements GestureDetector.OnGestureListener
{
    /**
     * Called when the activity is first created.
     */
    private boolean state = false;
    Fragment tasktype,taskdate;
    private GestureDetector mDetector;
    public MyActivity() {
         tasktype = new TaskType();
         taskdate = new TaskDate();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        FragmentTransaction transaction = this.getFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_holder,tasktype);
        transaction.commit();
        mDetector = new GestureDetector(this,this);


    }


    public void changeFragment(View view) {
        FragmentTransaction transaction = this.getFragmentManager().beginTransaction();


        if (!state) {
            transaction.setCustomAnimations(R.anim.fragment_enter,R.anim.fragment_exit);

         //   transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            transaction.replace(R.id.fragment_holder, taskdate);
            transaction.commit();
            state = true;
        }
        else {
           transaction.setCustomAnimations(R.anim.fragment_enter,R.anim.fragment_exit);
           // transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            transaction.replace(R.id.fragment_holder, tasktype);
            transaction.commit();
            state = false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent me) {
        return mDetector.onTouchEvent(me);
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
        Log.v("Gesture","SingleTap Gesture detected");
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
        Log.v("Gesture","Fling Gesture detected");
        return false;
    }
}
