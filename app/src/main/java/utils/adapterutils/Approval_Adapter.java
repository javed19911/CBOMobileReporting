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


public class Approval_Adapter extends BaseAdapter {

    private Context context;
    private static ArrayList<String> dataList = new ArrayList<String>();
    private static ArrayList<String> getKeyList = new ArrayList<String>();
    private static ArrayList<Integer> count = new ArrayList<Integer>();

    public Approval_Adapter(Context context, ArrayList<String> dataList, ArrayList<String> getKeyList, ArrayList<Integer> count) {
        this.context = context;
        this.dataList = dataList;
        this.getKeyList = getKeyList;
        this.count = count;
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

        final TextView count_tv = (TextView) gridView.findViewById(R.id.count);
        if(count.get(position)>0){
            count_tv.setText(""+count.get(position));
            count_tv.setVisibility(View.VISIBLE);
        }else {
            count_tv.setVisibility(View.GONE);
        }

        // set Image based on selected Text
        final ImageView imgView = (ImageView) gridView.findViewById(R.id.image_src);

        String arrayLevel = getKeyList.get(position);


        if (arrayLevel.equals("A_TP")) {
            imgView.setImageResource(R.drawable.tour_pa);
        }else if (arrayLevel.equals("A_DRADD")) {
            imgView.setImageResource(R.drawable.doctor_add_approval);
        }else if (arrayLevel.equals("A_DREDIT")) {
            imgView.setImageResource(R.drawable.doctor_edit_approval);
        }else if (arrayLevel.equals("ROUTE_APP")) {
            imgView.setImageResource(R.drawable.route_approval);
        }else if (arrayLevel.equals("DCR_APP")) {
            imgView.setImageResource(R.drawable.dcr_approval);
        }else if (arrayLevel.equals("LEAVE_APP")) {
            imgView.setImageResource(R.drawable.leave_approval);
        }else if (arrayLevel.equals("CHADD_APP")) {
            imgView.setImageResource(R.drawable.chemist_add_approval);
        }else if (arrayLevel.equals("CHEDIT_APP")) {
            imgView.setImageResource(R.drawable.chemist_edit_approval);
        }else if (arrayLevel.equals("CHEDEL_APP")) {
            imgView.setImageResource(R.drawable.chemist_delete_approval);
        }else {
            imgView.setImageResource(R.drawable.reset_day_plan_white);
        }




        return gridView;


    }

}
