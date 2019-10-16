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

import java.text.ParseException;
import java.util.ArrayList;

import bill.NewOrder.mBillBatch;
import cbomobilereporting.cbo.com.cboorder.Utils.AddToCartView;
import utils_new.CustomDatePicker;


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

    @Override
    public int getCount() {
        return super.getCount();
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {

        View row = inflater.inflate(R.layout.bill_row, parent, false);
        mBillBatch tempValues;


        TextView batch_no =  row.findViewById(R.id.batch_no);
        TextView pack = row.findViewById(R.id.pack);
        TextView exp = row.findViewById(R.id.exp);
        TextView rate =  row.findViewById(R.id.rate);
        TextView stock =  row.findViewById(R.id.stock);
        TextView mfg =  row.findViewById(R.id.mfg);

            tempValues = data.get(position);
            batch_no.setText(tempValues.getBATCH_NO());
            pack.setText(tempValues.getPACK());

            try {
                exp.setText(CustomDatePicker.formatDate(CustomDatePicker.getDate(tempValues.getEXP_DATE(),CustomDatePicker.ShowFormatOld),"MMM-yy") );
            } catch (ParseException e) {
                exp.setText(tempValues.getEXP_DATE());
                e.printStackTrace();
            }
            mfg.setText(String.format("%.2f", tempValues.getMRP_RATE()));
            rate.setText(String.format("%.2f", tempValues.getSALE_RATE()));
            stock.setText(String.format("%.0f", tempValues.getSTOCK()));

        return row;
    }

}
