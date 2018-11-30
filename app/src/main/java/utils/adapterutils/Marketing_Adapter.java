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
 * Created by om shanti om on 4/25/15.
 */
public class Marketing_Adapter extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater;
    //public ViewClickListener mViewClickListener;
    ArrayList<MarketingSales_model> firstList=new ArrayList<MarketingSales_model>();

    public Marketing_Adapter(Context mContext, ArrayList<MarketingSales_model> data) {
        this.context = mContext;
        layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.firstList = data;
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return firstList.size();
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
        final ViewHolderMarketing holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.marketing_view, null);
            holder = new ViewHolderMarketing();
            holder.remark_holder = (TextView) convertView.findViewById(R.id.remark_marketing);
            holder.amount_holder_ = (TextView) convertView.findViewById(R.id.amount_marketing);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolderMarketing) convertView.getTag();
        }
        holder.remark_holder.setText(firstList.get(position).getRemark_model());
        holder.amount_holder_.setText(firstList.get(position).getAmount_model());

        holder.remark_holder.setTag(position);
        holder.amount_holder_.setTag(position);


        return convertView;
    }



}
class ViewHolderMarketing {
    TextView remark_holder;
    TextView amount_holder_;


}