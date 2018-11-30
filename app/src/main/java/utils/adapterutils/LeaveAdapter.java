package utils.adapterutils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cbo.cbomobilereporting.R;

import java.util.ArrayList;

/**
 * Created by Akshit on 4/17/2015.
 */
public class LeaveAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;

    ArrayList<LeaveModel> data=new ArrayList<LeaveModel>();


    public LeaveAdapter(Context context,ArrayList<LeaveModel>arrayList ){

        this.context = context;
       this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data = arrayList;

    }





    public int getCount() {
        // TODO Auto-generated method stub
        return data.size();
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
        final ViewHolderLeave holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.leaverequest_view, null);
            holder = new ViewHolderLeave();

            holder.leaveId = (TextView)convertView.findViewById(R.id.leaveId);
            holder.leaveHeadId =(TextView)convertView.findViewById(R.id.leaveHeadId);
            holder.leaveHead =(TextView)convertView.findViewById(R.id.leaveHead);
            holder.leaveBalQty =(EditText)convertView.findViewById(R.id.bal_leave_qty);
            holder.leaveReqQty =(EditText)convertView.findViewById(R.id.requriedLeave);

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolderLeave) convertView.getTag();
        }

            holder.leaveId.setText(data.get(position).getLeaveId());
            holder.leaveHeadId.setText(data.get(position).getLeaveHeadId());
            holder.leaveHead.setText(data.get(position).getLeaveHead());
            holder.leaveBalQty.setText(data.get(position).getBalQty());
            holder.leaveReqQty.setText(data.get(position).getReqQty());

            holder.leaveHead.setTag(position);
            holder.leaveBalQty.setTag(position);
            holder.leaveReqQty.setTag(position);

        holder.leaveReqQty.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                Toast.makeText(view.getContext(), data.get(position).getBalQty(), Toast.LENGTH_LONG).show();

               // int a = Integer.parseInt(data.get(position).getBalQty());
               // int c = Integer.parseInt(data.get(position).getReqQty());

               // if(a<=c){
                    Toast.makeText(view.getContext(),"Please Enter Valid input.", Toast.LENGTH_LONG).show();
                }
            //
        });




        return convertView;




    }



}

class ViewHolderLeave {

    TextView leaveHead;
    TextView leaveHeadId;
    TextView leaveId;
    EditText leaveBalQty;
    EditText leaveReqQty;

}


