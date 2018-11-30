package utils.adapterutils;

import java.util.ArrayList;


import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;


public class SpinAdapter2 extends ArrayAdapter<String>{
    private Context activity;
    private ArrayList data;
    public Resources res;
    SpinnerModel tempValues=null;
    LayoutInflater inflater;

    public SpinAdapter2(
            Context activitySpinner,
            int textViewResourceId,
            ArrayList objects

    )
    {
        super(activitySpinner, textViewResourceId, objects);
        super.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);

        /********** Take passed values **********/
        activity = activitySpinner;
        data     = objects;


        /***********  Layout inflator to call external xml layout () **********************/
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {

        /********** Inflate spinner_rows.xml file for each row ( Defined below ) ************/
        View row = inflater.inflate(R.layout.spin_row2, parent, false);

        /***** Get each Model object from Arraylist ********/
        tempValues = null;
        tempValues = (SpinnerModel) data.get(position);

        TextView label        = (TextView)row.findViewById(R.id.spin_name2);
        TextView sub          = (TextView)row.findViewById(R.id.spin_id2);




        {
            // Set values for spinner each row 
            label.setText(tempValues.getName());
            sub.setText(tempValues.getId());


        }

        return row;
    }

}
