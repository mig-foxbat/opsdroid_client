package org.foxbat.opsdroid.taskdetail;


import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import org.foxbat.opsdroid.R;
import org.foxbat.opsdroid.task.TaskRecord;
import org.foxbat.opsdroid.task.TaskTypeAdapter;
import java.util.List;


/**
 * Created by chlr on 10/5/14.
 */
public class TaskDetailActivity extends FragmentActivity {

    List<TaskRecord> list;
    String ADAPTERTAG = "ADAPTERTAG";
    TaskActionFragment actionfragment;
    boolean action_fragment_flag = false;
    PagerAdapter mPagerAdapter;
    ViewPager mPager;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.task_detail_activity);
        list = (TaskTypeAdapter.getInstance()).getTaskRecords();
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showActionFragment() {
        if (!action_fragment_flag) {
            actionfragment = new TaskActionFragment(list.get(mPager.getCurrentItem()));
            FragmentTransaction transaction = this.getFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit);
            transaction.addToBackStack(null);
            transaction.add(R.id.holder, actionfragment);
            transaction.commit();
            this.action_fragment_flag = true;
        }
    }

    @Override
    public void onBackPressed() {

        if(action_fragment_flag) {
            FragmentTransaction transaction = this.getFragmentManager().beginTransaction();
            transaction.remove(actionfragment);
            transaction.commit();
            this.action_fragment_flag = false;
        }
        else {
            super.onBackPressed();
        }

    }



    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new TaskDetailMainFragment(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }


}
