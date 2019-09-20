package bill.CompanySelecter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cbo.cbomobilereporting.R;

import java.util.ArrayList;
import java.util.Random;

import bill.BillReport.mCompany;
import cbomobilereporting.cbo.com.cboorder.interfaces.RecycleViewOnItemClickListener;

public class aCompany extends RecyclerView.Adapter<aCompany.MyViewHolder> {

    private Context mContext;
    private ArrayList<mCompany> parties;
    private ArrayList<mCompany> partiesCopy;
    RecycleViewOnItemClickListener recycleViewOnItemClickListener = null;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, headQtr,character;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            headQtr = (TextView) view.findViewById(R.id.headqtr);
            character = view.findViewById(R.id.character);


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


    public aCompany(Context mContext, ArrayList<mCompany> parties) {
        this.mContext = mContext;
        this.parties = parties;
        partiesCopy = (ArrayList<mCompany>) parties.clone();
    }


    public ArrayList<mCompany> getParties(){
        return parties;
    }

    public void update(ArrayList<mCompany> parties) {
        this.parties = parties;
        partiesCopy = (ArrayList<mCompany>) parties.clone();
        notifyDataSetChanged();
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.company_card, parent, false);

        return new MyViewHolder(itemView);
    }

    public mCompany getPartyAt(int position){
        return partiesCopy.get(position);
    }

    public void filter(String Qry){
        int textlength = Qry.length();
        partiesCopy.clear();
        for (int i = 0; i < parties.size(); i++) {
            if (textlength <= parties.get(i).getName().length()) {

                if (parties.get(i).getName().toLowerCase().contains(Qry.toLowerCase().trim())) {
                    partiesCopy.add(parties.get(i));
                }
            }
        }

        notifyDataSetChanged();

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        mCompany item = partiesCopy.get(position);
        holder.name.setText(item.getName());
        holder.headQtr.setVisibility(View.GONE);

        final Drawable drawable = holder.character.getBackground();
        Random rnd = new Random();
        final int[] color = {Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))};
        drawable.setColorFilter(color[0], PorterDuff.Mode.SRC_IN);

    }

    public void setOnClickListner(RecycleViewOnItemClickListener recycleViewOnItemClickListener){
        this.recycleViewOnItemClickListener = recycleViewOnItemClickListener;
    }


    @Override
    public int getItemCount() {
        return partiesCopy.size();
    }
}

