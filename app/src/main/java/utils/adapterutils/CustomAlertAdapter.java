package utils.adapterutils;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;

public class CustomAlertAdapter extends BaseAdapter{

    Context ctx=null;
    ArrayList<String> listarray=null;
    private ArrayList data;
    private LayoutInflater mInflater=null;
    SpinnerModel tempValues=null;
    public CustomAlertAdapter(Activity activty, ArrayList<String> list)
    {
        this.ctx=activty;
        mInflater = activty.getLayoutInflater();
        this.listarray=list;
        data=list;
    }
    @Override
    public int getCount() {
     
        return listarray.size();
    }
 
    @Override
    public Object getItem(int arg0) {
        return null;
    }
 
    @Override
    public long getItemId(int arg0) {
        return 0;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {      
      final ViewHolder holder;
        if (convertView == null ) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.spin_row, null);
            tempValues = null;
            tempValues = (SpinnerModel) data.get(position);
             
            holder.titlename = (TextView) convertView.findViewById(R.id.spin_name);
            holder. sub          = (TextView)convertView.findViewById(R.id.spin_id);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
         
        String datavalue=listarray.get(position);
        holder.titlename.setText(datavalue);
         
        return convertView;
    }
     
    private static class ViewHolder {
        TextView titlename;
        TextView sub;
    }
}
