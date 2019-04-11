package saleOrder.Adaptor;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;

import java.util.ArrayList;
import java.util.Random;

import cbomobilereporting.cbo.com.cboorder.Utils.AddToCartView;
import cbomobilereporting.cbo.com.cboorder.interfaces.RecycleViewOnItemClickListener;
import saleOrder.Model.mParty;


public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<mParty> parties;
    private ArrayList<mParty> partiesCopy;
    RecycleViewOnItemClickListener recycleViewOnItemClickListener = null;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, headQtr,character,amt;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            headQtr = (TextView) view.findViewById(R.id.headqtr);
            character = view.findViewById(R.id.character);
            amt = view.findViewById(R.id.amt);


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


    public ClientAdapter(Context mContext, ArrayList<mParty> parties) {
        this.mContext = mContext;
        this.parties = parties;
        partiesCopy = (ArrayList<mParty>) parties.clone();
    }


    public ArrayList<mParty> getParties(){
        return parties;
    }

    public void update(ArrayList<mParty> parties) {
        this.parties = parties;
        partiesCopy = (ArrayList<mParty>) parties.clone();
        notifyDataSetChanged();
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.client_card, parent, false);

        return new MyViewHolder(itemView);
    }

    public mParty getPartyAt(int position){
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
        mParty item = partiesCopy.get(position);
        holder.name.setText(item.getName());
        holder.headQtr.setText(item.getHeadQtr());
        holder.amt.setText(AddToCartView.toCurrency(item.getBalance()));

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
