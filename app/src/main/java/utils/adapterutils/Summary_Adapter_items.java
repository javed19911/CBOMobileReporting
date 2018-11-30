package utils.adapterutils;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.emp_tracking.MyCustomMethod;

import java.util.ArrayList;
import java.util.Map;

import utils.MyConnection;

/**
 * Created by Akshit on 11/28/2015.
 */
public class Summary_Adapter_items extends BaseAdapter {

  Context context;
    LayoutInflater layoutInflater;
    ArrayList<Map<String, String>> dataList= new ArrayList<Map<String, String>>();







    public Summary_Adapter_items(Context context, ArrayList<Map<String, String>> data){

      this.context = context;
      layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      dataList = data;


  }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null){
             holder = new ViewHolder();
            //summary_list_item
            convertView = layoutInflater.inflate(R.layout.dob_doa_doc,null);

            holder.name=(TextView) convertView.findViewById(R.id.name);
            holder.doa=(TextView) convertView.findViewById(R.id.doa);
            holder.dob=(TextView) convertView.findViewById(R.id.dob);
            holder.call=(ImageView) convertView.findViewById(R.id.call);
            holder.msg=(ImageView) convertView.findViewById(R.id.msg);

            convertView.setTag(holder);

        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(dataList.get(position).get("DR_NAME").equals("No Data Found")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.name.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark, context.getTheme()));
            } else {
                holder.name.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
            }
            holder.msg.setVisibility(View.GONE);
            holder.call.setVisibility(View.GONE);
        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.name.setTextColor(context.getResources().getColor(android.R.color.black, context.getTheme()));
            } else {
                holder.name.setTextColor(context.getResources().getColor(android.R.color.black));
            }
            holder.msg.setVisibility(View.VISIBLE);
            holder.call.setVisibility(View.VISIBLE);
        }

        holder.name.setText(dataList.get(position).get("DR_NAME"));
        holder.doa.setText(dataList.get(position).get("DOA"));
        holder.dob.setText(dataList.get(position).get("DOB"));

        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MyCustomMethod(context).CallDilog(dataList.get(position).get("MOBILE").split(",")[0],dataList.get(position).get("DR_NAME"));
            }
        });

        holder.msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    new MyCustomMethod(context).sendSMS(dataList.get(position).get("MOBILE").split(",")[0],dataList.get(position).get("DR_NAME"));

            }
        });

        return convertView;
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    public class ViewHolder{

        TextView name,dob,doa;
        ImageView call,msg;

    }
}
