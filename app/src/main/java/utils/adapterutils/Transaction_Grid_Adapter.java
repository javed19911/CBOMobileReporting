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
public class Transaction_Grid_Adapter extends BaseAdapter {

    private Context context;
    private static ArrayList<String> dataList = new ArrayList<String>();
    private static ArrayList<String> keyList = new ArrayList<String>();


    public Transaction_Grid_Adapter(Context context, ArrayList<String>dataList, ArrayList<String>keyList) {
        this.context = context;
        this.dataList = dataList;
        this.keyList = keyList;
    }


    @Override
    public int getCount() {
        return dataList.size();
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
        textView.setText(dataList.get(position));


        final ImageView imgView = (ImageView) gridView.findViewById(R.id.image_src);

        String arrayLevel = keyList.get(position);
           /* {"Leave Request", "Secondery Sale","Complaint","Complaint View", "R.C.P.A. Call","Farmer Registor"
                    ,"Add Doctor","Add Chemist",
                    "Dr. Wise Sales","Add T.P.","Challan Acknoledegemt"}
*/

        if (arrayLevel.equals("T_LR")) {
            imgView.setImageResource(R.drawable.request_leave_white);
        } else if (arrayLevel.equals("T_LR1")) {
            imgView.setImageResource(R.drawable.request_leave_white);
        } else  if (arrayLevel.equals("T_SS")) {
            imgView.setImageResource(R.drawable.secondary_sale);
        } else if (arrayLevel.equals("T_COMP")) {
            imgView.setImageResource(R.drawable.view_last_complaint_white);
        } else if (arrayLevel.equals("T_CV")) {
            imgView.setImageResource(R.drawable.complaint_view);
        } else if (arrayLevel.equals("T_RCPA")) {
            imgView.setImageResource(R.drawable.rcpa_white);
        } else if (arrayLevel.equals("T_FAR")) {
            imgView.setImageResource(R.drawable.farmer_meeting_white);
        } else if (arrayLevel.equals("T_ADDDOC")) {
            imgView.setImageResource(R.drawable.add_doctor);
        } else if (arrayLevel.equals("T_DRSHALE")) {
            imgView.setImageResource(R.drawable.secondary_sales_white);
        } else if (arrayLevel.equals("T_ADDCHEM")) {
            imgView.setImageResource(R.drawable.add_chemist);
        } else if (arrayLevel.equals("T_ADDTP")) {
            imgView.setImageResource(R.drawable.add_tourp);
        }
        else if (arrayLevel.equals("T_TPAPROVE")) {
            imgView.setImageResource(R.drawable.tp_reports_);
        }
        else if (arrayLevel.equals("T_CHALACK")) {
            imgView.setImageResource(R.drawable.challan_reciv);
        }
        else if (arrayLevel.equals("T_RM")) {
            imgView.setImageResource(R.drawable.route_master);
        }
        else if (arrayLevel.equals("T_EXP")) {
            imgView.setImageResource(R.drawable.expense_statement);
        }else if (arrayLevel.equals("T_EDITDOC")) {
            imgView.setImageResource(R.drawable.doctor_edit_approval);
        }




        return gridView;


    }

}
