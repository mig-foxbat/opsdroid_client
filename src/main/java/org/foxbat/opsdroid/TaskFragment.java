package org.foxbat.opsdroid;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.*;
import android.widget.EditText;
import org.foxbat.opsdroid.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by chlr on 9/17/14.
 */
public class TaskFragment extends OpsListFragment {

    TaskTypeAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstance) {

        super.onCreate(savedInstance);
        adapter = new TaskTypeAdapter();
        SharedPreferences perfs = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        int datekey = perfs.getInt("datekey",Integer.parseInt(new SimpleDateFormat("yyyyMMdd").format(new Date())));
        adapter.refreshData(datekey);
        this.setListAdapter(adapter);
        Log.v(this.getClass().getName(),"checkpoint1");

    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.generateFilteredRecords();
        adapter.notifyDataSetChanged();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.taskfragment,container,false);
        EditText et  = (EditText)view.findViewById(R.id.tasknamefilter);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return view;
    }



    @Override
    public void refreshData() {
        SharedPreferences perfs = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        int datekey = perfs.getInt("datekey",Integer.parseInt(new SimpleDateFormat("yyyyMMdd").format(new Date())));
        adapter.refreshData(datekey);
        adapter.notifyDataSetChanged();
    }



}