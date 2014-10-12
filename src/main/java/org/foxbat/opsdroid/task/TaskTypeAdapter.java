package org.foxbat.opsdroid.task;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.foxbat.opsdroid.R;
import org.foxbat.opsdroid.model.DatabaseHandler;
import org.foxbat.opsdroid.model.TaskFilterModel;
import org.foxbat.opsdroid.utils.AppObjectRepository;
import java.util.ArrayList;
import java.util.List;



/**
 * Created by chlr on 10/1/14.
 */
public class TaskTypeAdapter extends BaseAdapter implements Filterable {

    private ArrayList<TaskRecord> static_filter_records;
    private ArrayList<TaskRecord> master_records;
    private ArrayList<TaskRecord> final_filtered_records;
    private TaskNameFilter task_name_filter;
    private static TaskTypeAdapter instance;
    private static Context context;

    enum task_type  {
        ops_task_application_control
        ,ops_task_email
        ,ops_task_file_monitor
        ,ops_task_ftp
        ,ops_task_ftp_file_monitor
        ,ops_task_indesca
        ,ops_task_manual
        ,ops_task_monitor
        ,ops_task_run_criteria
        ,ops_task_sap
        ,ops_task_sleep
        ,ops_task_sql
        ,ops_task_stored_proc
        ,ops_task_system_monitor
        ,ops_task_to_exclusive
        ,ops_task_to_resource
        ,ops_task_unix
        ,ops_task_windows
        ,ops_task_workflow
        ,ops_task_zos
    };

    private TaskTypeAdapter() {
        static_filter_records = new ArrayList<>(100);
        master_records = new ArrayList<>(100);
    }

    public static TaskTypeAdapter getInstance(Context context) {
        TaskTypeAdapter.context = context;
        if (instance == null) {
            instance = new TaskTypeAdapter();
        }
        return instance;

    }

    public static TaskTypeAdapter getInstance() {
        if (instance == null) {
            instance = new TaskTypeAdapter();
        }
        return instance;
    }

    @Override
    public int getCount() {
        return final_filtered_records.size();
    }

    @Override
    public Object getItem(int i) {
        return final_filtered_records.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view ==  null) {
            Context context = AppObjectRepository.getContext();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_image_view, null);
        }
        ((ImageView) view.findViewById(R.id.image)).setImageResource(getDrawableResource(task_type.valueOf(final_filtered_records.get(i).sys_class_name)));
        ((TextView) view.findViewById(R.id.list_text_view)).setText(final_filtered_records.get(i).ins_name);
        return view;
    }

    public List<TaskRecord> getTaskRecords() {
        return this.final_filtered_records;
    }


    private int getDrawableResource(task_type type) {
        switch (type) {
            case ops_task_unix:
                return R.drawable.unix;
            case ops_task_workflow:
                return R.drawable.workflow;
            case ops_task_monitor:
                return R.drawable.monitor;
            default:
                return R.drawable.others;
        }
    }



    public void refreshData(int datekey) {
        String sql = "SELECT \n" +
                "ins_name,sys_id,task_id,sys_class_name,task_name,summary,task_ref_count,status_code,queued_time,start_time,end_time,duration,retry_interval,retry_maximum,retry_indefinitely,attempt_count,sys_updated_by,sys_created_by,execution_user,invoked_by,agent" +
                " FROM opswise_master where datekey = %d;";
        DatabaseHandler handler = new DatabaseHandler();
        Cursor cur = handler.executeQuery(String.format(sql, datekey), null);
        this.buildMasterArrayList(cur);
        generateFilteredRecords();
    }



    private void buildMasterArrayList(Cursor cur) {
        master_records.clear();
        for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
            TaskRecord record = new TaskRecord(cur.getString(0), cur.getString(1), cur.getString(2), cur.getString(3));
            record.setMainBlock(cur.getString(4),cur.getString(5),cur.getString(6),cur.getString(7));
            record.setTimeBlock(cur.getString(8), cur.getString(9), cur.getString(10), cur.getString(11));
            record.setRetryBlock(cur.getString(12), cur.getString(13), cur.getString(14), cur.getString(15));
            record.setUserBlock(cur.getString(16), cur.getString(17), cur.getString(18), cur.getString(19),cur.getString(20));
            master_records.add(record);
        }
        cur.close();
    }


    public  void generateFilteredRecords() {
        static_filter_records.clear();
        for (TaskRecord record : master_records) {
            if (doesRecordQualify(record)) {
                static_filter_records.add(record);
            }
        }
        final_filtered_records = static_filter_records;
    }

    public TaskRecord getTaskRecord(int position) {
       return this.final_filtered_records.get(position);
    }


    private boolean doesRecordQualify(TaskRecord record) {
        TaskFilterModel model = TaskFilterModel.getInstance();
        ArrayList<String> sel_task_types = model.getSelectedTaskTypes();
        ArrayList<Integer> sel_status_types = model.getSelectedStatusTypes();
        return sel_task_types.contains(record.sys_class_name)
                && sel_status_types.contains(Integer.parseInt(record.status_code));

    }

    @Override
    public Filter getFilter() {
        if (task_name_filter == null) {
            task_name_filter = new TaskNameFilter();
        }
        return task_name_filter;
    }


    private class TaskNameFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint == null || constraint.length() == 0) {
               synchronized (this) {
                   results.values = static_filter_records;
                   results.count = static_filter_records.size();
               }
            }
            else {
                List<TaskRecord> textFilteredResult = new ArrayList<>();
                for ( TaskRecord record : static_filter_records) {
                    if (record.ins_name.toLowerCase().contains(constraint.toString().toLowerCase())) {
                        textFilteredResult.add(record);
                    }
                }
                results.values = textFilteredResult;
                results.count = textFilteredResult.size();
            }
            return results;
        }

           @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                   final_filtered_records = (ArrayList<TaskRecord>)filterResults.values;
                   notifyDataSetChanged();
            }
        }



}
