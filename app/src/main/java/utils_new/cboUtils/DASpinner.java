package utils_new.cboUtils;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.Expense.mDA;
import com.cbo.utils.CustomSpinner;

import java.util.ArrayList;

import utils.adapterutils.SpinnerModel;

public class DASpinner extends CustomSpinner<mDA> {

    private ArrayList<mDA> DAs = new ArrayList<mDA>();

    public DASpinner(Context context) {
        super(context);
        initialize();
    }

    public DASpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public DASpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DASpinner(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize();
    }

    private void initialize(){
        setTitle("   DA Type");
    }

    public void setDAs(ArrayList<mDA> DAs){
        this.DAs = DAs;
    }

    @Override
    public ArrayList<mDA> getDataList() {
        if (DAs.size() == 0){
            ArrayList<mDA> das = new ArrayList<>();
            das.add(new mDA().setName("---Select---"));
            return das;

        }
        return DAs;
    }

    @Override
    public int getResource() {
        return R.layout.spin_row;
    }

    @Override
    public View onBindRow(mDA spinnerModel, View row) {


        TextView label =  row.findViewById(R.id.spin_name);
        TextView sub = row.findViewById(R.id.spin_id);
        TextView distance = row.findViewById(R.id.distance);
        ImageView loc_icon =  row.findViewById(R.id.loc_icon);
        LinearLayout distanceLayout = row.findViewById(R.id.distanceLayout);

        distance.setVisibility(View.GONE);
        loc_icon.setVisibility(View.GONE);
        distanceLayout.setVisibility(View.GONE);


        label.setText(spinnerModel.getName());
        sub.setText(spinnerModel.getId());


        return row;
    }
    @Override
    public void onItemSelectListener(mDA mDA) {

    }
}
