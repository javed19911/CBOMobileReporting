package utils.adapterutils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;

import java.util.ArrayList;

/**
 * Created by Akshit on 4/17/2015.
 */
public class NewLeaveAdapter extends BaseAdapter{



        Context context;
        LayoutInflater layoutInflater;
        //public ViewClickListener mViewClickListener;
        ArrayList<LeaveModel> Rptdata=new ArrayList<LeaveModel>();

        public NewLeaveAdapter(Context mContext,ArrayList<LeaveModel>data){
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
            final HolderView holder;
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.leaverequest_view, null);
                holder = new HolderView();
                holder.LeaveId=(TextView)convertView.findViewById(R.id.leaveId);
                holder.LeaveheadeId=(TextView)convertView.findViewById(R.id.leaveHeadId);
                holder.Leaveheade=(TextView)convertView.findViewById(R.id.leaveHead);
                holder.Balqty=(EditText)convertView.findViewById(R.id.bal_leave_qty);
                holder.ReqQty=(EditText)convertView.findViewById(R.id.requriedLeave);
                convertView.setTag(holder);
            }
            else{
                holder = (HolderView) convertView.getTag();
            }
            holder.LeaveId.setText(Rptdata.get(position).getLeaveId());
            holder.LeaveId.setText(Rptdata.get(position).getLeaveHeadId());
            holder.Leaveheade.setText(Rptdata.get(position).getLeaveHead());
            holder.Balqty.setText(Rptdata.get(position).getBalQty());
            holder.ReqQty.setText(Rptdata.get(position).getReqQty());

            return convertView;
        }


    }

    class HolderView {
        TextView LeaveheadeId;
        TextView LeaveId;
        TextView Leaveheade;
        EditText Balqty;
        EditText ReqQty;



    }

