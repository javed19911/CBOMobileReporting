package saleOrder.Adaptor;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;

import java.util.ArrayList;

import cbomobilereporting.cbo.com.cboorder.Utils.AddToCartView;
import cbomobilereporting.cbo.com.cboorder.interfaces.RecycleViewOnItemClickListener;
import saleOrder.Model.mOverDue;

public class OverDueAdapter extends RecyclerView.Adapter<OverDueAdapter.MyViewHolder>{

    private Context mContext;
    private ArrayList<mOverDue> overDues;
    RecycleViewOnItemClickListener recycleViewOnItemClickListener=null;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView docNo, docDate, dueDate, balAmt;

        public MyViewHolder(View view) {
            super(view);
            docNo = (TextView) view.findViewById(R.id.order_no);
            docDate = (TextView) view.findViewById(R.id.docDate);
            dueDate = view.findViewById(R.id.dueDate);
            balAmt = view.findViewById(R.id.amt);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recycleViewOnItemClickListener != null) {
                        recycleViewOnItemClickListener.onClick(view, getAdapterPosition(), false);
                    }
                }
            });
        }

    }


    public OverDueAdapter(Context mContext, ArrayList<mOverDue> overDues) {
        this.mContext = mContext;
        this.overDues = overDues;
    }


    public void update(ArrayList<mOverDue> overDues) {
        this.overDues = overDues;
        notifyDataSetChanged();
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.over_due_card, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        mOverDue item = overDues.get(position);
        holder.docDate.setText(item.getDOC_DATE());
        holder.docNo.setText(item.getDOC_NO());
        holder.dueDate.setText(item.getDUE_DATE());
        holder.balAmt.setText(AddToCartView.toCurrency(item.getBAL_AMT()));

    }

    public void setOnClickListner(RecycleViewOnItemClickListener recycleViewOnItemClickListener) {
        this.recycleViewOnItemClickListener = recycleViewOnItemClickListener;
    }


    @Override
    public int getItemCount() {
        return overDues.size();
    }
}
