package org.foxbat.opsdroid.taskdetail;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.foxbat.opsdroid.R;
import org.foxbat.opsdroid.task.TaskRecord;
import org.foxbat.opsdroid.task.TaskTypeAdapter;

/**
 * Created by chlr on 10/5/14.
 */
public class TaskDetailGeneralTab extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.task_detail_general_tab, container, false);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle bundle = this.getArguments();
        int position = bundle.getInt("position", 0);
        TaskRecord record = TaskTypeAdapter.getInstance().getTaskRecord(position);
        setContent(record);
    }



    public void setContent(TaskRecord record) {
        View v = this.getView();
        ((TextView)v.findViewById(R.id.ins_name)).setText(record.ins_name);
        ((TextView)v.findViewById(R.id.task_name)).setText(record.task_name);
        ((TextView)v.findViewById(R.id.task_type)).setText(record.sys_class_name);
        ((TextView)v.findViewById(R.id.task_summary)).setText(record.summary);
        ((TextView)v.findViewById(R.id.task_ref_count)).setText(record.task_ref_count);
        ((TextView)v.findViewById(R.id.task_status)).setText(record.status_code);
        ((TextView)v.findViewById(R.id.task_queued_time)).setText(record.queued_time);
        ((TextView)v.findViewById(R.id.task_start_time)).setText(record.start_time);
        ((TextView)v.findViewById(R.id.task_end_time)).setText(record.end_time);
        ((TextView)v.findViewById(R.id.task_duration)).setText(record.duration);
        ((TextView)v.findViewById(R.id.task_retry_interval)).setText(record.retry_interval);
        ((TextView)v.findViewById(R.id.task_retry_maximum)).setText(record.retry_maximum);
        ((TextView)v.findViewById(R.id.task_retry_indefinitely)).setText(record.retry_indefinitely);
        ((TextView)v.findViewById(R.id.task_attempt_count)).setText(record.attempt_count);
        ((TextView)v.findViewById(R.id.task_sys_update)).setText(record.sys_updated_by);
        ((TextView)v.findViewById(R.id.task_sys_created)).setText(record.sys_created_by);
        ((TextView)v.findViewById(R.id.task_execution_user)).setText(record.execution_user);
        ((TextView)v.findViewById(R.id.task_invoked_by)).setText(record.invoked_by);
        ((TextView)v.findViewById(R.id.task_agent)).setText(record.agent);
    }

}