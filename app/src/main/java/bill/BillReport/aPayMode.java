package bill.BillReport;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.Expense.mExpHead;

import java.util.ArrayList;

public class aPayMode extends ArrayAdapter<mPay> {
    public ArrayList<mPay> payModes;
    private LayoutInflater inflater;

    public aPayMode(Context activitySpinner, int textViewResourceId, ArrayList  payModes) {
        super(activitySpinner, textViewResourceId, payModes);
        super.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);

        /********** Take passed values **********/
        this.payModes = payModes;


        /***********  Layout inflator to call external xml layout () **********************/
        inflater = (LayoutInflater) activitySpinner.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {

        /********** Inflate spinner_rows.xml file for each row ( Defined below ) ************/
        View row = inflater.inflate(R.layout.spin_row, parent, false);

        /***** Get each Model object from Arraylist ********/
        mPay tempValues;
        tempValues = payModes.get(position);

        TextView label =  row.findViewById(R.id.spin_name);
        TextView sub = row.findViewById(R.id.spin_id);
        TextView distance = row.findViewById(R.id.distance);
        ImageView loc_icon =  row.findViewById(R.id.loc_icon);
        LinearLayout distanceLayout = row.findViewById(R.id.distanceLayout);

        distance.setVisibility(View.GONE);
        loc_icon.setVisibility(View.GONE);
        distanceLayout.setVisibility(View.GONE);

        {
            // Set values for spinner each row
            try {
                label.setText(tempValues.getName());
                sub.setText(tempValues.getId());
                //label.setTextColor( Color.parseColor( tempValues.getColour()));
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        return row;
    }

}