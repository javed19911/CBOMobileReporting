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

/**
 * Created by AKSHIT on 03/05/2016.
 */
public class ReportMenu_Grid_Adapter extends BaseAdapter {

    private Context context;
    private static ArrayList<String> listOfTab = new ArrayList<String>();
    private static ArrayList<String> listOfKey = new ArrayList<String>();


    public ReportMenu_Grid_Adapter(Context context, ArrayList<String> listOfTab, ArrayList<String> getKeyList) {
        this.context = context;
        ReportMenu_Grid_Adapter.listOfTab = listOfTab;
        listOfKey = getKeyList;
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
        // set Image based on selected Text
        final ImageView imgView = (ImageView) gridView.findViewById(R.id.image_src);

        String arrayLevel = listOfKey.get(position);


        if (arrayLevel.equals("R_LOGUNL")) {
            imgView.setImageResource(R.drawable.logged_unlogged_white);
        } else if (arrayLevel.equals("R_DCRRPT")) {
            imgView.setImageResource(R.drawable.dcr_reports_white);
        } else if (arrayLevel.equals("R_DASH")) {
            imgView.setImageResource(R.drawable.secondary_sales_white);
        } else if (arrayLevel.equals("R_DRWISE")) {
            imgView.setImageResource(R.drawable.dr_wise_reports);
        } else if (arrayLevel.equals("R_TP")) {
            imgView.setImageResource(R.drawable.tp_reports_);
        } else if (arrayLevel.equals("R_SPO")) {
            imgView.setImageResource(R.drawable.spo_reports);
        }else if (arrayLevel.equals("DOB_DOA")) {
            imgView.setImageResource(R.drawable.birthday);
        }else if (arrayLevel.equals("MSG_HO")) {
            imgView.setImageResource(R.drawable.msg_ho);
        }else if (arrayLevel.equals("GIFT_DIST")) {
            imgView.setImageResource(R.drawable.gift_distribution_status);
        }

        return gridView;


    }
}
