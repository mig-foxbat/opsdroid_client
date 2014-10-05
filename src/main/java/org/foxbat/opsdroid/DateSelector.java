package org.foxbat.opsdroid;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Dialog;
import android.preference.PreferenceManager;
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

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
         SharedPreferences perfs = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
         SharedPreferences.Editor editor = perfs.edit();
         editor.putInt("datekey",Integer.parseInt(String.format("%04d",year)+String.format("%02d",month)+String.format("%02d",day)));
    }
}
