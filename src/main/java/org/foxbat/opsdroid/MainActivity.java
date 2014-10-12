package org.foxbat.opsdroid;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.*;
import android.content.Intent;
import android.widget.Toast;
import org.foxbat.opsdroid.service.ServiceWorker;
import org.foxbat.opsdroid.task.TaskFilterFragment;
import org.foxbat.opsdroid.task.TaskFragment;
import org.foxbat.opsdroid.trigger.TriggerFragment;
import org.foxbat.opsdroid.utils.AppObjectRepository;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



public class MainActivity extends Activity implements ActionBar.TabListener
{
    /**
     * Called when the activity is first created.
     */

    GestureDetector gdectector;
    GestureEventHandler ghandler;
    List<OpsListFragment> fraglist = new ArrayList<>(3);

    private void setUpActionBarTabs() {

        fraglist.add(new TaskFragment());
        fraglist.add(new TriggerFragment());
     //   fraglist.add(new ReportFragment());

        ActionBar actionbar = this.getActionBar();
        actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionbar.addTab(actionbar.newTab().setText("Task").setTabListener(this),true);
        actionbar.addTab(actionbar.newTab().setText("Trigger").setTabListener(this));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setUpActionBarTabs();
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
//            case R.id.action_search:
//                Log.v(this.getClass().getName(),"Search Bar Hit detected");
//                return true;
            case R.id.action_refresh:
                Log.v(this.getClass().getName(),"Refresh detected");
                SharedPreferences perf = PreferenceManager.getDefaultSharedPreferences(this);
                Log.v(this.getClass().getName(),String.valueOf(perf.getInt("datekey",0)));
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



    private void changeFragment(int i) {
        FragmentTransaction transaction = this.getFragmentManager().beginTransaction();
    //    transaction.setCustomAnimations(R.anim.fragment_enter,R.anim.fragment_exit);
        transaction.replace(R.id.fragment_holder,fraglist.get(i));
        transaction.commit();
     }

    private void initiateRefresh() {
        int curr_date = Integer.parseInt((new SimpleDateFormat("yyyyMMdd")).format(new Date()));
        int datekey = PreferenceManager.getDefaultSharedPreferences(this).getInt("datekey",curr_date);
        new RefreshData().execute(datekey);
    }

    private void showSettingsFragment() {
        FragmentTransaction transaction = this.getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fragment_enter,R.anim.fragment_exit);
            transaction.addToBackStack(null);
        transaction.replace(R.id.fragment_holder,new TaskFilterFragment());
        transaction.commit();
    }





    @Override
    public boolean dispatchTouchEvent(MotionEvent me) {
        gdectector.onTouchEvent(me);
        super.dispatchTouchEvent(me);
        return false;
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        this.changeFragment(tab.getPosition());
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
            ServiceWorker worker = new ServiceWorker(MainActivity.this);
            worker.refreshDataForDate(datekey[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void a) {
            OpsListFragment frag = fraglist.get(getActionBar().getSelectedTab().getPosition());
            frag.refreshData();
            Toast.makeText(MainActivity.this, "Data Refresh", Toast.LENGTH_LONG).show();
        }
    }

    public void onButtonClick(View view) {
        Log.v(this.getClass().getName(),"Filter Settings");
        showSettingsFragment();
    }


}
