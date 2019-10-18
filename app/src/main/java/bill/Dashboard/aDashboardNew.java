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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cbo.cbomobilereporting.R;

import java.util.ArrayList;

import bill.Outlet.Outlet;

public class aDashboardNew extends RecyclerView.Adapter<aDashboardNew.MyViewHolder> {


    private Context context;
    private ArrayList<mDashboardNew> list;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        RecyclerView itemList;
        View rowView;
        aDashboardInner InnerAdaptor;


        public MyViewHolder(View view) {
            super(view);
            this.rowView=view;
            name= view.findViewById(R.id.name);
            itemList= view.findViewById(R.id.itemList);

            InnerAdaptor = new aDashboardInner(context);
            itemList.setLayoutManager(new LinearLayoutManager(context));
            itemList.setItemAnimator(new DefaultItemAnimator());
            itemList.setAdapter(InnerAdaptor);

            itemList.setOnClickListener(null);

            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!list.get(getAdapterPosition()).getDOC_TYPE().isEmpty()) {
                        Intent intent = new Intent(context, Outlet.class);
                        intent.putExtra("dashboard", list.get(getAdapterPosition()));
                        context.startActivity(intent);
                    }
                }
            });


        }
    }


    public aDashboardNew(Context context, ArrayList<mDashboardNew> list){
        this.context = context;
        this.list =  list;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dashboard_new_row_view, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        mDashboardNew dashboard = list.get(position);
        holder.name.setText(dashboard.getGROUP_NAME());
        ((aDashboardInner)holder.itemList.getAdapter()).updateList(dashboard.getCOL_NAME(),dashboard.getCOL_VALUE());


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
