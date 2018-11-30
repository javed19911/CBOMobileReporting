package utils.adapterutils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;


import java.util.ArrayList;

/**
 * Created by Akshit on 2/8/2016.
 */
public class Spinner_Apprisal_Adapter extends BaseAdapter {

    Context context;
    ArrayList<Apprisal_Spinner_Model> apprisalSpinnerModelArrayList = new ArrayList<Apprisal_Spinner_Model>();
    LayoutInflater layoutInflater;
   public static String Apprisal_Spinner_Name_Id ="";

    public Spinner_Apprisal_Adapter(Context context,ArrayList<Apprisal_Spinner_Model> apprisalSpinnerModelArrayList){

        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        this.apprisalSpinnerModelArrayList = apprisalSpinnerModelArrayList;
     }





    @Override
    public int getCount()
    {
        return apprisalSpinnerModelArrayList.size();
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
    public View getView(int position, View convertView, ViewGroup parent)
    {
       final ViewHolder holder;


        if (convertView == null){
            apprisalSpinnerModelArrayList.get(position);
            convertView = layoutInflater.inflate(R.layout.apprasial_spinner_row,null);
            holder = new ViewHolder();
            holder.id=(TextView) convertView.findViewById(R.id.spin_item_id);
            holder.name =(TextView) convertView.findViewById(R.id.spin_item_name);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.name.setText(apprisalSpinnerModelArrayList.get(position).getName());
        holder.id.setText(apprisalSpinnerModelArrayList.get(position).getId());
        Apprisal_Spinner_Name_Id = apprisalSpinnerModelArrayList.get(position).getId();

       /* if (position==0){

          holder.name.setText("---Select Name---");
            holder.id.setText("");
        }else {

            holder.name.setText(apprisalSpinnerModelArrayList.get(position).getName());
            holder.id.setText(apprisalSpinnerModelArrayList.get(position).getId());

        }*/





        return convertView;
    }



    private class ViewHolder{

        TextView name;
        TextView id;


    }



}
