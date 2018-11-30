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
public class Personal_info_Adapter extends BaseAdapter {

    private Context context;
    private static ArrayList<String> dataList = new ArrayList<String>();
    private static ArrayList<String> getKeyList = new ArrayList<String>();


    public Personal_info_Adapter(Context context, ArrayList<String> dataList, ArrayList<String> getKeyList) {
        this.context = context;
        this.dataList = dataList;
        this.getKeyList = getKeyList;
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
        // set Image based on selected Text
        final ImageView imgView = (ImageView) gridView.findViewById(R.id.image_src);

        String arrayLevel = getKeyList.get(position);


        if (arrayLevel.equals("CP")) {
            imgView.setImageResource(R.drawable.change_pwd);
        } else if (arrayLevel.equals("SS")) {
            imgView.setImageResource(R.drawable.salary_slip);
        } else if (arrayLevel.equals("CIR")) {
            imgView.setImageResource(R.drawable.circular);
        } else if (arrayLevel.equals("DECL")) {
            imgView.setImageResource(R.drawable.decl_ofsaving);
        } else if (arrayLevel.equals("IP")) {
            imgView.setImageResource(R.drawable.perso_info);
        } else if (arrayLevel.equals("FORM16")) {
            imgView.setImageResource(R.drawable.form16);
        } else if (arrayLevel.equals("HL")) {
            imgView.setImageResource(R.drawable.dcr_reports_white);
        } else if (arrayLevel.equals("LEAVE")) {
            imgView.setImageResource(R.drawable.request_leave_white);
        } else if (arrayLevel.equals("LEAVE1")) {
            imgView.setImageResource(R.drawable.request_leave_white);
        }




        return gridView;


    }

}
