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
public class MailMenu_Grid_Adapter extends BaseAdapter {

    private Context context;
    private static ArrayList<String> listOfTab = new ArrayList<String>();
    private static ArrayList<String> listOfKey = new ArrayList<String>();
    private static ArrayList<Integer> count = new ArrayList<Integer>();


    public MailMenu_Grid_Adapter(Context context, ArrayList<String> listOfTab, ArrayList<String> getKeyList, ArrayList<Integer> count) {
        this.context = context;
        this.listOfTab = listOfTab;
        this.listOfKey = getKeyList;
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
        // set Image based on selected Text
        final ImageView imgView = (ImageView) gridView.findViewById(R.id.image_src);

        String arrayLevel = listOfKey.get(position);

        final TextView count_tv = (TextView) gridView.findViewById(R.id.count);
        if(count.get(position)>0){
            count_tv.setText(""+count.get(position));
            count_tv.setVisibility(View.VISIBLE);
        }else {
            count_tv.setVisibility(View.GONE);
        }


        if (arrayLevel.equals("M_COM")) {
            imgView.setImageResource(R.drawable.compose_mail_white);
        } else if (arrayLevel.equals("M_IN")) {
            imgView.setImageResource(R.drawable.inbox_white);
        } else if (arrayLevel.equals("M_SITEM")) {
            imgView.setImageResource(R.drawable.sent_white);
        }
        else if (arrayLevel.equals("NOTIFICATION")) {
            imgView.setImageResource(R.drawable.notification);
        }

        return gridView;


    }
}
