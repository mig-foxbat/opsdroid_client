package org.foxbat.opsdroid.taskdetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
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



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.task_detail_activity);
        list = (TaskTypeAdapter.getInstance()).getTaskRecords();
        ViewPager mPager = (ViewPager) findViewById(R.id.pager);
        PagerAdapter mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
    }


    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new TaskDetailMainFragment(list.get(position));
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }


}
