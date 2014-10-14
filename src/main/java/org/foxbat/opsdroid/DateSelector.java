package org.foxbat.opsdroid;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Dialog;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.DatePicker;


import java.util.Calendar;
import java.util.Date;

/**
 * Created by chlr on 9/19/14.
 */
public class DateSelector extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        int datekey = prefs.getInt("datekey",0);

        // Create a new instance of DatePickerDialog and return it
        if(datekey == 0) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }
        else {
            String datekey_str = String.valueOf(datekey);
            int year = Integer.parseInt(datekey_str.substring(0,4));
            int month = Integer.parseInt(datekey_str.substring(4,6));
            int day = Integer.parseInt(datekey_str.substring(6,8));
            return new DatePickerDialog(getActivity(), this, year, month-1, day);
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
         SharedPreferences perfs = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
         SharedPreferences.Editor editor = perfs.edit();
         editor.putInt("datekey",Integer.parseInt(String.format("%04d",year)+String.format("%02d",month+1)+String.format("%02d",day)));
         editor.commit();
    }
}
