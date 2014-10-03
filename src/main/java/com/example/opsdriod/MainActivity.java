package com.example.opsdriod;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.Menu;
import android.content.Intent;
import com.example.opsdriod.rest.UrlSynthesizer;
import com.example.opsdriod.utils.AppObjectRepository;
import com.example.opsdriod.utils.CircularLinkedList;


import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends Activity implements ActionBar.TabListener
{
    /**
     * Called when the activity is first created.
     */

    GestureDetector gdectector;
    GestureEventHandler ghandler;
    CircularLinkedList<OpsListFragment> fragment_list;

    public MainActivity() {
        initAndAddFragments();
    }

    private void initAndAddFragments() {
        fragment_list = new CircularLinkedList<OpsListFragment>();
        fragment_list.addNode(new TaskType());
        //fragment_list.addNode(new TaskDate());
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
        AppObjectRepository.setAppSharedPreference(PreferenceManager.getDefaultSharedPreferences(this));
        AppObjectRepository.setContextObject(this);

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
            case R.id.action_refresh:
                Log.v(this.getClass().getName(),"Refresh detected");
                initiateRefresh();
                return true;
            case R.id.action_calendar:
                DialogFragment fragment = new DateSelector();
                fragment.show(getFragmentManager(),"datepicker");
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

    private void initiateRefresh() {
        int datekey = Integer.parseInt((new SimpleDateFormat("yyyyMMdd")).format(new Date()));
        String url = new UrlSynthesizer().task_date(datekey);
        new RefreshData().execute(datekey);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent me) {
        gdectector.onTouchEvent(me);
        super.dispatchTouchEvent(me);
        return false;
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }


    private class RefreshData extends AsyncTask<Integer,Void,Void> {
        @Override
        protected Void doInBackground(Integer... datekey) {
            ServiceWorker worker = new ServiceWorker();
            worker.refreshDataForDate(datekey[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void a) {
            OpsListFragment frag = fragment_list.getCurrent();
            frag.refreshData();
        }
    }

}
