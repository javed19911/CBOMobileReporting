package utils_new.cboUtils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.utils.CustomSpinner;

import java.util.ArrayList;

import utils.adapterutils.SpinnerModel;

public class CalledDoctorSinner extends CustomSpinner<SpinnerModel> {
    CBO_DB_Helper cbohelp;


    public CalledDoctorSinner(Context context) {
        super(context);
        initialize();
    }

    public CalledDoctorSinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public CalledDoctorSinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CalledDoctorSinner(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize();
    }

    private void initialize(){
        setTitle("   Doctor");
    }

    @Override
    public ArrayList<SpinnerModel> getDataList() {
        cbohelp = new CBO_DB_Helper(getContext());
        ArrayList<SpinnerModel> mylist = new ArrayList<>();
        Cursor c = cbohelp.getDoctorName();
        mylist.add(new SpinnerModel("--Select--", "0"));
        try {
            if (c.moveToFirst()) {
                do {
                    mylist.add(new SpinnerModel(c.getString(c.getColumnIndex("dr_name")), c.getString(c.getColumnIndex("dr_id"))));

                } while (c.moveToNext());
            }
        }catch (Exception e){
            Log.e("Called Doctor",e.getLocalizedMessage());
        }finally {
            c.close();
        }

        return mylist;
    }

    @Override
    public int getResource() {
        return R.layout.spin_row;
    }

    @Override
    public View onBindRow(SpinnerModel spinnerModel, View row) {


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
                label.setText(spinnerModel.getName());
                sub.setText(spinnerModel.getId());
                label.setTextColor( Color.parseColor( spinnerModel.getColour()));
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
        return row;
    }

    @Override
    public void onItemSelectListener(SpinnerModel spinnerModel) {

    }


}
