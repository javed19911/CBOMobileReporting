package bill.ItemFilter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cbo.cbomobilereporting.R;

import java.util.ArrayList;
import java.util.Random;

import bill.NewOrder.mBillItem;
import cbomobilereporting.cbo.com.cboorder.Utils.AddToCartView;
import saleOrder.Enum.eItem;

public class aCompanyItemFilter extends RecyclerView.Adapter<aCompanyItemFilter.MyViewHolder> {

    private Context mContext;
    private ArrayList<mBillItem> items;
    private ArrayList<mBillItem> itemscopy;
    private IitemFilter listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, headQtr,character;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            headQtr = (TextView) view.findViewById(R.id.headqtr);
            character = view.findViewById(R.id.character);
        }
    }


    public void setListener(IitemFilter listener){
        this.listener = listener;
    }



    public aCompanyItemFilter(Context mContext, ArrayList<mBillItem> items) {
        this.mContext = mContext;
        this.items = items;
        itemscopy = (ArrayList<mBillItem>) items.clone();
    }


    public ArrayList<mBillItem> getItems(){
        return items;
    }

    public void update(ArrayList<mBillItem> items) {
        this.items = items;
        itemscopy = (ArrayList<mBillItem>) items.clone();
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.company_medicine_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        mBillItem item = itemscopy.get(position);
        holder.name.setText(item.getName());
        holder.headQtr.setVisibility(View.GONE);

        final Drawable drawable = holder.character.getBackground();
        Random rnd = new Random();
        final int[] color = {Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))};
        drawable.setColorFilter(color[0], PorterDuff.Mode.SRC_IN);
        holder.itemView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (listener!= null){
                            listener.OnItemSelected(item);
                        }
                    }
                });
    }



    @Override
    public int getItemCount() {
        return itemscopy.size();
    }

    public void filter(String Qry){
        int textlength = Qry.length();
        itemscopy.clear();
        for (int i = 0; i < items.size(); i++) {
            if (textlength <= items.get(i).getName().length()) {

                if (items.get(i).getName().toLowerCase().contains(Qry.toLowerCase().trim())) {
                    itemscopy.add(items.get(i));
                }
            }
        }

        notifyDataSetChanged();

    }



    interface IitemFilter{
        void OnItemSelected(mBillItem item);
    }
}
