package org.foxbat.opsdroid.taskdetail;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;
import org.foxbat.opsdroid.task.TaskRecord;
import org.foxbat.opsdroid.task.TaskTypeAdapter;

import java.util.List;

/**
 * Created by chlr on 10/15/14.
 */
public class MyPagerAdapter extends FragmentStatePagerAdapter {
    SparseArray<TaskDetailMainFragment> registeredFragments = new SparseArray<TaskDetailMainFragment>();
    List<TaskRecord> list;

    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
        list =  (TaskTypeAdapter.getInstance()).getTaskRecords();

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Fragment getItem(int position) {
       TaskDetailMainFragment fragment = new TaskDetailMainFragment(position);
        return fragment;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, (TaskDetailMainFragment)fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public TaskDetailMainFragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }
}