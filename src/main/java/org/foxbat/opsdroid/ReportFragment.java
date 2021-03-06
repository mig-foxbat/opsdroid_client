package org.foxbat.opsdroid;


import android.app.DialogFragment;
import android.os.Bundle;
import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.Button;
import org.foxbat.opsdroid.R;


/**
 * Created by chlr on 9/18/14.
 */
public class ReportFragment extends OpsListFragment {



    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reportfragment,container,false);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),R.array.task_type_array,android.R.layout.simple_list_item_1);
        this.setListAdapter(adapter);
        Button button = (Button)view.findViewById(R.id.date_dialog_button);
        button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment fragment = new DateSelector();
                fragment.show(getFragmentManager(),"datepicker");
            }
        });
        return view;
    }


    @Override
    public void refreshData() {


    }
}