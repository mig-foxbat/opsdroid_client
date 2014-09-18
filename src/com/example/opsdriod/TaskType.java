package com.example.opsdriod;

import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Created by chlr on 9/17/14.
 */
public class TaskType extends ListFragment {

    GestureDetector gdectector;
    GestureEventHandler ghandler;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        ghandler = new GestureEventHandler(this);
        gdectector = new GestureDetector(this.getActivity(),ghandler);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tasktype,container,false);
        Spinner spinner = (Spinner)view.findViewById(R.id.task_type_list);
        spinner.setAdapter(new  ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_spinner_item,new String[] {"Linux/Unix","Monitors","Windows","z/OS","Monitor","Task 6","Task 7"}));
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),R.array.task_type_array,android.R.layout.simple_list_item_1);
        this.setListAdapter(adapter);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gdectector.onTouchEvent(event);
            }

        });
        return view;
    }


    public void onRefresh(View view) {
        Log.v(this.getClass().getName(), "Refresh request detected "+view.getClass().getName());
    }




}