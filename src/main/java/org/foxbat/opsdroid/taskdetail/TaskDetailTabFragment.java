package org.foxbat.opsdroid.taskdetail;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.foxbat.opsdroid.R;

/**
 * Created by chlr on 10/5/14.
 */
public class TaskDetailTabFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.task_detail_tab_fragment, container, false);
        return v;
    }
}