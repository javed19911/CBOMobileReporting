package utils.adapterutils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;

import java.util.ArrayList;


public class DcrMenu_Grid_Adapter extends BaseAdapter {

    private Context context;
    private static ArrayList<String> listOfTab = new ArrayList<String>();
    private static ArrayList<String> getKey = new ArrayList<String>();
    private static ArrayList<Integer> count = new ArrayList<Integer>();

    public DcrMenu_Grid_Adapter(Context context, ArrayList<String> listOfTab, ArrayList<String> getKeyList, ArrayList<Integer> count) {
        this.context = context;
        this.listOfTab = listOfTab;
        this.getKey = getKeyList;
        this.count = count;

    }


    @Override
    public int getCount() {
        return listOfTab.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridView;

        if (convertView == null) {

            gridView = new View(context);

            gridView = layoutInflater.inflate(R.layout.grid_item, null);
        } else {
            gridView = (View) convertView;
        }

        final TextView textView = (TextView) gridView.findViewById(R.id.text_src);
        textView.setText(listOfTab.get(position));

        final TextView count_tv = (TextView) gridView.findViewById(R.id.count);
        if(count.get(position)>-1){
            count_tv.setText(""+count.get(position));
            count_tv.setVisibility(View.VISIBLE);
        }else {
            count_tv.setVisibility(View.GONE);
        }

        // set Image based on selected Text
        final ImageView imgView = (ImageView) gridView.findViewById(R.id.image_src);

        String arrayLevel = getKey.get(position);
        //  "day Planing","Reminder Call","Dr.Call","Dr. Sample","Retailer Call",
        //        "Stockist Call","Non Listed Calls","Expanse","Summary","Final Submit"


        if (arrayLevel.equals("D_DP")) {
            imgView.setImageResource(R.drawable.dcr_reports_white);
        } else if (arrayLevel.equals("D_RCCALL")) {
            imgView.setImageResource(R.drawable.reminder_card_white);
        } else if (arrayLevel.equals("D_DRCALL")) {
            imgView.setImageResource(R.drawable.doctor_call_white);
        }
        else if (arrayLevel.equals("D_DR_RX") || arrayLevel.equals("D_RX_GEN") || arrayLevel.equals("D_RX_GEN_NA")) {
            imgView.setImageResource(R.drawable.add_doctor);
        }
        else if (arrayLevel.equals("D_DRSAM")) {
            imgView.setImageResource(R.drawable.doctor_sameple_white);
        } else if (arrayLevel.equals("D_DRSAM")) {
            imgView.setImageResource(R.drawable.chemist_call_white);
        } else if (arrayLevel.equals("D_CHEMCALL")) {
            imgView.setImageResource(R.drawable.chemist_call_white);
        }else if (arrayLevel.equals("D_FAR")) {
            imgView.setImageResource(R.drawable.farmer_meeting_white);
        } else if (arrayLevel.equals("D_RETCALL")) {
            imgView.setImageResource(R.drawable.chemist_call_white);
        } else if (arrayLevel.equals("D_STK_CALL")) {
            imgView.setImageResource(R.drawable.stockist_call_white);
        } else if (arrayLevel.equals("D_NLC_CALL")) {
            imgView.setImageResource(R.drawable.non_listedcall);
        } else if (arrayLevel.equals("D_AP")) {
            imgView.setImageResource(R.drawable.appraisal);
        } else if (arrayLevel.equals("D_EXP")) {
            imgView.setImageResource(R.drawable.expense_white);
        } else if (arrayLevel.equals("D_SUM")) {
            imgView.setImageResource(R.drawable.summary_white);
        } else if (arrayLevel.equals("D_FINAL")) {
            imgView.setImageResource(R.drawable.final_submit_white);
        }else {
            imgView.setImageResource(R.drawable.reset_day_plan_white);
        }

        return gridView;


    }
}
