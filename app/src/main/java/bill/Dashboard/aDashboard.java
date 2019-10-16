package bill.Dashboard;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cbo.cbomobilereporting.R;

import java.util.ArrayList;

import bill.Outlet.Outlet;

public class aDashboard extends RecyclerView.Adapter<aDashboard.MyViewHolder> {


    private Context context;
    private ArrayList<mDashboard> list;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView OthSale,TotSale,CashSale,bills;
        LinearLayout conatiner,detail_layout,main_layout;
        View rowView;


        public MyViewHolder(View view) {
            super(view);
            this.rowView=view;
            //rcpt_no=(TextView)view.findViewById(R.id.rcpt_no);
            name= view.findViewById(R.id.name);
            CashSale=(TextView) view.findViewById(R.id.CashSale);
            TotSale=(TextView) view.findViewById(R.id.TotSale);

            OthSale=(TextView) view.findViewById(R.id.OthSale);
            conatiner=(LinearLayout) view.findViewById(R.id.container);
            bills = view.findViewById(R.id.bill_count);

        }
    }


    public aDashboard(Context context, ArrayList<mDashboard> list){
        this.context = context;
        this.list =  list;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dashboard_row_view, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        mDashboard dashboard = list.get(position);
        holder.name.setText(dashboard.getDOC_CAPTION());
        holder.TotSale.setText(dashboard.getTOTAL_SALE());
        holder.OthSale.setText(dashboard.getOTHER_SALE());
        holder.CashSale.setText(dashboard.getCASH_SALE());
        holder.bills.setText(dashboard.getNO_BILL());


        holder.rowView.setBackgroundColor(Color.parseColor(dashboard.getBG_COLOR()));


        holder.rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, Outlet.class);
                    intent.putExtra("dashboard",dashboard);
                    context.startActivity(intent);
                }
            });



    }



    @Override
    public int getItemCount() {
        return list.size();
    }
}
