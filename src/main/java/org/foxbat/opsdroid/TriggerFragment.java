package org.foxbat.opsdroid;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import org.foxbat.opsdroid.R;

/**
 * Created by chlr on 9/19/14.
 */
public class TriggerFragment extends OpsListFragment {

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.triggerfragment,container,false);
        Spinner spinner = (Spinner)view.findViewById(R.id.task_status_list);
        spinner.setAdapter(new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_spinner_item,new String[] {"Running","Failed","Defined","Resource Wait","Waiting"}));
        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),R.array.task_type_array,android.R.layout.simple_list_item_1);
        //this.setListAdapter(adapter);
        return view;
    }

    public void onRefresh(View view) {
        Log.v(this.getClass().getName(), "Refresh request detected " + view.getClass().getName());

    }

    @Override
    public void refreshData() {

    }
}