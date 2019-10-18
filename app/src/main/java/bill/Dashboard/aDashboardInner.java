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

public class aDashboardInner extends RecyclerView.Adapter<aDashboardInner.MyViewHolder> {


    private Context context;
    private ArrayList<String> CaptionList = new ArrayList<>();
    private ArrayList<String> ValueList = new ArrayList<>();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView caption,value;
        View rowView;


        public MyViewHolder(View view) {
            super(view);
            this.rowView=view;
            caption= view.findViewById(R.id.caption);
            value = view.findViewById(R.id.value);

        }
    }


    public aDashboardInner(Context context){
        this.context = context;
    }

    public void updateList(ArrayList<String> CaptionList,ArrayList<String> ValueList){
        this.ValueList = ValueList;
        this.CaptionList = CaptionList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dashboard_inner_row_view, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.caption.setText(CaptionList.get(position));
        holder.value.setText(ValueList.get(position));

    }



    @Override
    public int getItemCount() {
        return CaptionList.size();
    }
}
