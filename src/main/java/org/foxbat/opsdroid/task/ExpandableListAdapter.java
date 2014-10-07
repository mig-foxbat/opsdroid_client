package org.foxbat.opsdroid.task;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;
import org.foxbat.opsdroid.R;
import org.foxbat.opsdroid.model.TaskFilterModel;
import org.foxbat.opsdroid.utils.AppObjectRepository;



/**
 * Created by chlr on 10/3/14.
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter {

    String[] task_type_array,task_status_array;
    String[][] parent_array;
    Context context;
    TaskFilterModel filter_model;


    public ExpandableListAdapter() {
        this.context = AppObjectRepository.getContext();
         task_type_array = context.getResources().getStringArray(R.array.task_type_array);
         task_status_array = context.getResources().getStringArray(R.array.task_status_array);
         parent_array = new String[][] {task_type_array,task_status_array};
        filter_model = TaskFilterModel.getInstance();
    }


    @Override
    public int getGroupCount() {
        return parent_array.length;
    }

    @Override
    public int getChildrenCount(int i) {
        return parent_array[i].length;
    }

    @Override
    public Object getGroup(int i) {
        switch (i)
        {
            case 0:
                return "Task Type";
            case 1:
                return "Task Status";
            default:
                return null;
        }

    }

    @Override
    public Object getChild(int i, int i2) {
        return parent_array[i][i2];
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i2) {
        return i2;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.task_filter_parent, null);
            }
        ((TextView)convertView.findViewById(R.id.text1)).setText(headerTitle);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,boolean isLastChild, View convertView, ViewGroup parent) {
        String headerTitle = (String) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(android.R.layout.simple_list_item_multiple_choice, null);
            }
        CheckedTextView ctv = ((CheckedTextView)convertView.findViewById(android.R.id.text1));
        ctv.setText(headerTitle);
        ctv.setChecked(filter_model.getCheckStatus(groupPosition,childPosition));
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i2) {
        return true;
    }
}
