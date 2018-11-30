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
public class Util_Grid_Adapter extends BaseAdapter {

    private Context context;
    private static ArrayList<String> dataList = new ArrayList<String>();
    private static ArrayList<String> getKeyList = new ArrayList<String>();


    public Util_Grid_Adapter(Context context, ArrayList<String> dataList, ArrayList<String> getKeyList) {
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


        if (arrayLevel.equals("U_UPPIC")) {
            imgView.setImageResource(R.drawable.upload_image_white);
        } else if (arrayLevel.equals("U_UPDOWN")) {
            imgView.setImageResource(R.drawable.upload_download_white);
        } else if (arrayLevel.equals("U_RESET")) {
            imgView.setImageResource(R.drawable.reset_day_plan_white);
        } else if (arrayLevel.equals("U_CAPSIGN")) {
            imgView.setImageResource(R.drawable.capture_image_white);
        } else if (arrayLevel.equals("U_MAP")) {
            imgView.setImageResource(R.drawable.map_view_white);
        } else if (arrayLevel.equals("U_WEB")) {
            imgView.setImageResource(R.drawable.web_login_white);
        } else if (arrayLevel.equals("U_DOWNAID")) {
            imgView.setImageResource(R.drawable.upload_download_white);
        } else if (arrayLevel.equals("U_SHOWAID")) {
            imgView.setImageResource(R.drawable.dcr_reports_white);
        } else if (arrayLevel.equals("U_PI")) {
            imgView.setImageResource(R.drawable.perso_info);
        }

        else if (arrayLevel.equals("Show Demo Kilometer")) {
            imgView.setImageResource(R.drawable.map_view_white);
        }




        return gridView;


    }

}
