package bill.Cart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;

import java.util.ArrayList;

import bill.NewOrder.mBillBatch;


public class aBillBatch extends ArrayAdapter<String> {
    public ArrayList<mBillBatch> data;
    private LayoutInflater inflater;

    public aBillBatch(Context activitySpinner, int textViewResourceId, ArrayList objects) {
        super(activitySpinner, textViewResourceId, objects);
        super.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        data = objects;

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

        View row = inflater.inflate(R.layout.spin_row, parent, false);
        mBillBatch tempValues;
        tempValues = data.get(position);

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
                label.setText(tempValues.getBATCH_NO());
                //sub.setText(tempValues.getId());
                //label.setTextColor( Color.parseColor( tempValues.getColour()));
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        return row;
    }

}
