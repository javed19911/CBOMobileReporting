package utils.adapterutils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;

import java.util.ArrayList;

/**
 * Created by Akshit on 4/2/2015.
 */
public class WorkwithAdapter extends BaseAdapter{
    Context context;
    LayoutInflater layoutInflater;
    //public ViewClickListener mViewClickListener;
    ArrayList<Dcr_Workwith_Model> Rptdata=new ArrayList<Dcr_Workwith_Model>();

    public WorkwithAdapter(Context mContext,ArrayList<Dcr_Workwith_Model>data){
        this.context=mContext;
        layoutInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.Rptdata=data;
    }

   /* public interface ViewClickListener {
        void onDoctorClicked(int position);
        void onChemistClicked(int position);
        void onStockistClicked(int position);
        void onExpanseClicked(int position);

    }

    public void setViewClickListener(ViewClickListener viewClickListener) {
        mViewClickListener = viewClickListener;
    }*/

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return Rptdata.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.dcr_workwith_row, null);
            holder = new Holder();
            holder.id=(TextView)convertView.findViewById(R.id.dcr_workwith_name);
            holder.checkBox=(CheckBox)convertView.findViewById(R.id.workwith_select);

           holder.checkBox.setChecked(true);

            convertView.setTag(holder);
        }
        else{
            holder = (Holder) convertView.getTag();
        }
        holder.id.setText(Rptdata.get(position).getId());
        holder.checkBox.setText(Rptdata.get(position).getName());

        holder.id.setTag(position);
        holder.checkBox.setTag(position);



        return convertView;
    }


}

class Holder {

    TextView id;
   CheckBox checkBox;
}
