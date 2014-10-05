package org.foxbat.opsdroid;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
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

    private ArrayList<Record> static_filter_records;
    private ArrayList<Record> master_records;
    private ArrayList<Record> final_filtered_records;
    private TaskNameFilter task_name_filter;

    public TaskTypeAdapter() {
        static_filter_records = new ArrayList<>(100);
        master_records = new ArrayList<>(100);
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
        Context context = AppObjectRepository.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View list_content_view = inflater.inflate(R.layout.listtextview, null);
        ((TextView) list_content_view.findViewById(R.id.list_text_view)).setText(final_filtered_records.get(i).taskname);
        return list_content_view;
    }

    public void refreshData(int datekey) {
        String sql = "SELECT distinct name,task_id,sys_class_name,status_code FROM opswise_master WHERE datekey = %d";
        DatabaseHandler handler = new DatabaseHandler();
        Cursor cur = handler.executeQuery(String.format(sql, datekey), null);
        master_records.clear();
        for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
            master_records.add(new Record(cur.getString(0), cur.getString(1), cur.getString(2), cur.getInt(3)));
        }
        cur.close();
        generateFilteredRecords();
    }

    public  void generateFilteredRecords() {
        static_filter_records.clear();
        for (Record record : master_records) {
            if (doesRecordQualify(record)) {
                static_filter_records.add(record);
            }
        }
        final_filtered_records = static_filter_records;
    }

    private boolean doesRecordQualify(Record record) {
        TaskFilterModel model = TaskFilterModel.getInstance();
        ArrayList<String> sel_task_types = model.getSelectedTaskTypes();
        ArrayList<Integer> sel_status_types = model.getSelectedStatusTypes();
        return sel_task_types.contains(record.sys_class_name)
                && sel_status_types.contains(record.status_code);

    }


    private class Record {
        String taskname, sys_id, sys_class_name;
        int status_code;

        public Record(String taskname, String sys_id, String sys_class_name, int status_code) {
            this.taskname = taskname.toLowerCase();
            this.sys_id = sys_id;
            this.sys_class_name = sys_class_name;
            this.status_code = status_code;
        }

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
                List<Record> textFilteredResult = new ArrayList<>();
                for ( Record record : static_filter_records) {
                    if (record.taskname.toLowerCase().contains(constraint.toString().toLowerCase())) {
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
                   final_filtered_records = (ArrayList<Record>)filterResults.values;
                   notifyDataSetChanged();
            }
        }



}
