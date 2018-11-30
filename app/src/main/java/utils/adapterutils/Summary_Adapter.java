package utils.adapterutils;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.emp_tracking.MyCustomMethod;

import java.util.ArrayList;
import java.util.Map;

import utils.MyConnection;

/**
 * Created by Akshit on 11/28/2015.
 */
public class Summary_Adapter extends BaseAdapter {

  Context context;
    LayoutInflater layoutInflater;
    Map<String,ArrayList<Map<String, String>>> dataList;







    public Summary_Adapter(Context context, Map<String,ArrayList<Map<String, String>>> data){

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
            convertView = layoutInflater.inflate(R.layout.summary_list_item,null);

            holder.header=(TextView) convertView.findViewById(R.id.header);
            holder.call_list=(ListView) convertView.findViewById(R.id.doctor_list);

            convertView.setTag(holder);

        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        String header_name=dataList.keySet().toArray()[position].toString();



        holder.header.setText(header_name);

        Summary_Adapter_items doctorAdapter = new Summary_Adapter_items(context, dataList.get(header_name));
        holder.call_list.setAdapter(doctorAdapter);



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

        TextView header;
        ListView call_list;

    }
}
