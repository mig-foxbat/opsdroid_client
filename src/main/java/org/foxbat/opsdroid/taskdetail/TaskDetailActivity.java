package org.foxbat.opsdroid.taskdetail;


import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.*;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.foxbat.opsdroid.R;
import org.foxbat.opsdroid.model.OpswiseMasterManager;
import org.foxbat.opsdroid.rest.AppManager;
import org.foxbat.opsdroid.rest.UrlSynthesizer;
import org.foxbat.opsdroid.task.TaskRecord;
import org.foxbat.opsdroid.task.TaskTypeAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


/**
 * Created by chlr on 10/5/14.
 */
public class TaskDetailActivity extends FragmentActivity {

    List<TaskRecord> list;
    String ADAPTERTAG = "ADAPTERTAG";
    TaskActionFragment actionfragment = null;
    MyPagerAdapter mPagerAdapter;
    ViewPager mPager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.task_detail_activity);
        list = (TaskTypeAdapter.getInstance()).getTaskRecords();
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setCurrentItem(getIntent().getIntExtra("position",0));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.task_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_task_action: {
                showActionFragment();
                return true; }
            case R.id.action_task_refresh: {
                makeRequest();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showActionFragment() {
        if (actionfragment == null) {
            Bundle bundle = new Bundle();
            bundle.putInt("position",mPager.getCurrentItem());
            actionfragment = new TaskActionFragment();
            actionfragment.setArguments(bundle);
            FragmentTransaction transaction = this.getFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit);
            transaction.addToBackStack(null);
            transaction.add(R.id.holder, actionfragment);
            transaction.commit();
        }
    }

    @Override
    public void onBackPressed() {
        if(! (actionfragment == null)) {
            FragmentTransaction transaction = this.getFragmentManager().beginTransaction();
            transaction.remove(actionfragment);
            transaction.commit();
            actionfragment = null;
        }
        else {
            super.onBackPressed();
        }
    }


    public void makeRequest() {

        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                     JSONArray arr = new JSONArray(response);
                     JSONObject json = arr.getJSONObject(0);
                     (new OpswiseMasterManager(TaskDetailActivity.this)).updateTask(json);
                     SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(TaskDetailActivity.this);
                     TaskTypeAdapter adapter = TaskTypeAdapter.getInstance(TaskDetailActivity.this);
                     adapter.refreshData(json.getString("sys_id"),prefs.getInt("datekey",0));
                     TaskDetailMainFragment fragment = mPagerAdapter.getRegisteredFragment(mPager.getCurrentItem());
                     fragment.refreshGeneralTab(adapter.getTaskRecord(mPager.getCurrentItem()));
                }
                catch (JSONException e) {
                    Log.e(this.getClass().getName(),e.getMessage());
                    Toast.makeText(TaskDetailActivity.this.getApplicationContext(), (e.getMessage() == null ? "Client Error" : e.getMessage()), Toast.LENGTH_LONG).show();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null) {
                    Log.e(this.getClass().getName(),"Error Response code: " +  error.networkResponse.statusCode);
                    Toast.makeText(TaskDetailActivity.this.getApplicationContext(), (error.getMessage() == null ? "Request Error" : error.getMessage()), Toast.LENGTH_LONG).show();
                }
            }
        };

        StringRequest request = new StringRequest(
                Request.Method.GET,
                new UrlSynthesizer().task_info(list.get(mPager.getCurrentItem()).sys_id),
                listener,
                errorListener);

        AppManager.getInstance().addToRequestQueue(request);
    }





}
