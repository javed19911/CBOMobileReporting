package bill.openingStock;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cbo.cbomobilereporting.R;

import java.util.ArrayList;

import bill.BillReport.aBill;
import bill.BillReport.mBill;
import utils_new.CustomDatePicker;

public class aOpening extends RecyclerView.Adapter<aOpening.MyViewHolder> {

    Context context;
    LayoutInflater layoutInflater;
    ArrayList<mOpening> billlsit = new ArrayList<>();
    ArrayList<mOpening> billlsitfilter = new ArrayList<mOpening>();
    private aOpening.Opening_interface bill_interface;


    public aOpening(Context context, ArrayList<mOpening> billArrayListt) {
        this.context = context;
        this.billlsit = billArrayListt;
        this.billlsitfilter = (ArrayList<mOpening>) billlsit.clone();
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.bill_interface = (aOpening.Opening_interface) context;

    }

    @NonNull
    @Override
    public aOpening.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.opening_row_view, parent, false);

        return new aOpening.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull aOpening.MyViewHolder holder, int position) {


        mOpening mOpeninglist = billlsitfilter.get(position);
        holder.doc_date.setText(CustomDatePicker.formatDate(mOpeninglist.getDOC_DATE(), CustomDatePicker.ShowFormatOld));
        //     holder.rec_name.setText(mOpeninglist.getParty().getName());
        holder.doc_no.setText(mOpeninglist.getDOC_NO());
        holder.enter_by.setText( mOpeninglist.getENTRY_BY());
        holder.no_item.setText(mOpeninglist.getNO_ITEM());//DATE

        holder.customer_name.setText(mOpeninglist.getCOMPANY_NAME());

        holder.delete.setVisibility(mOpeninglist.getDelete() ? View.VISIBLE : View.GONE);
        holder.edit.setVisibility(mOpeninglist.getEdit() ? View.VISIBLE : View.GONE);

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bill_interface.Edit_Bill(mOpeninglist);
            }
        });

        holder.main_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.expand.getText().toString().equalsIgnoreCase("+")) {
                    holder.expand.setText("-");
                    holder.detail_layout.setVisibility(View.VISIBLE);
                } else {
                    holder.expand.setText("+");
                    holder.detail_layout.setVisibility(View.GONE);
                }
            }
        });


        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bill_interface.Delete_Bill(mOpeninglist);
            }
        });

        holder.billView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bill_interface.OnClick_Bill(mOpeninglist);
            }
        });
           /* holder.rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bill_interface.OnClick_Bill(mOpeninglist);
                }
            });*/
    }

    @Override
    public int getItemCount() {
        return billlsitfilter.size();
    }

    public void filter(String Query) {
        billlsitfilter.clear();
        if (Query.trim().equals("")) {
            billlsitfilter = (ArrayList<mOpening>) billlsit.clone();
            notifyDataSetChanged();
            return;
        }

        for (mOpening item : billlsit) {
            if (item.getDOC_NO().toLowerCase().contains(Query.toLowerCase())) {
                billlsitfilter.add(item);
            }
        }
        notifyDataSetChanged();
    }


    public interface Opening_interface {
        void Edit_Bill(mOpening mopenings);

        void Delete_Bill(mOpening mopenings);

        void OnClick_Bill(mOpening mopenings);

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView doc_date, doc_no, customer_name, enter_by, no_item, mobile, gst, amt, net_amt, payMode;
        ImageView edit, delete, billView;
        LinearLayout conatiner, detail_layout, main_layout;
        TextView expand;
        View rowView;


        public MyViewHolder(View view) {
            super(view);
            this.rowView = view;
            //rcpt_no=(TextView)view.findViewById(R.id.rcpt_no);
            doc_no = (TextView) view.findViewById(R.id.doc_no);
            enter_by = (TextView) view.findViewById(R.id.enter_by);
            doc_date = (TextView) view.findViewById(R.id.doc_date);

            no_item = (TextView) view.findViewById(R.id.no_item);
            expand = view.findViewById(R.id.expand);

            customer_name = (TextView) view.findViewById(R.id.cust_name);
            mobile = (TextView) view.findViewById(R.id.mobile);
            gst = (TextView) view.findViewById(R.id.gst);
            amt = (TextView) view.findViewById(R.id.amount);
            net_amt = (TextView) view.findViewById(R.id.netAmt);
            payMode = (TextView) view.findViewById(R.id.payMode);

            delete = (ImageView) view.findViewById(R.id.delete);
            edit = (ImageView) view.findViewById(R.id.edit);
            billView = (ImageView) view.findViewById(R.id.view);

            conatiner = (LinearLayout) view.findViewById(R.id.container);
            detail_layout = view.findViewById(R.id.detail_layout);
            main_layout = view.findViewById(R.id.main_layout);


        }
    }

}
