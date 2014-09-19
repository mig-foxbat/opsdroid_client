package com.example.opsdriod;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.Menu;
import android.content.Intent;


public class MainActivity extends Activity
{
    /**
     * Called when the activity is first created.
     */
    private boolean state = false;
    Fragment tasktype,taskdate;
    GestureDetector gdectector;
    GestureEventHandler ghandler;
    CircularLinkedList<Fragment> fragment_list;

    public MainActivity() {
        initAndAddFragments();
    }

    private void initAndAddFragments() {
        fragment_list = new CircularLinkedList<Fragment>();
        fragment_list.addNode(new TaskType());
        fragment_list.addNode(new TaskDate());
        fragment_list.addNode(new TaskStatus());
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        FragmentTransaction transaction = this.getFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_holder,fragment_list.getAtIndex(0));
        transaction.commit();
        ghandler = new GestureEventHandler(this);
        gdectector = new GestureDetector(this,ghandler);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ab_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this,SettingsActivity.class);
                this.startActivity(intent);
                return true;
            case R.id.action_search:
                Log.v(this.getClass().getName(),"Search Bar Hit detected");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    public void changeFragment(boolean direction) {
        FragmentTransaction transaction = this.getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fragment_enter,R.anim.fragment_exit);
        transaction.replace(R.id.fragment_holder,direction?fragment_list.getNext():fragment_list.getPrevious());
        transaction.commit();
     }


    @Override
    public boolean dispatchTouchEvent(MotionEvent me) {
        gdectector.onTouchEvent(me);
        super.dispatchTouchEvent(me);
        return false;
    }
}
