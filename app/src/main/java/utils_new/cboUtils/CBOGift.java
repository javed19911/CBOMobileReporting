package utils_new.cboUtils;

import android.content.Context;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.ui_new.Model.mSPO;
import com.cbo.utils.MultiSelectView;

public class CBOGift extends MultiSelectView<mSPO,CBOGift.MyViewHolder> {

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, headQtr,character,amt;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
//            headQtr = (TextView) view.findViewById(R.id.headqtr);
//            character = view.findViewById(R.id.character);
//            amt = view.findViewById(R.id.amt);
//
//
//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (recycleViewOnItemClickListener != null) {
//                        recycleViewOnItemClickListener.onClick(view, getAdapterPosition(), false);
//                    }
//                }
//            });
        }
    }

    public CBOGift(Context context) {
        super(context);
    }

    public CBOGift(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CBOGift(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CBOGift(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int position) {
        /*mParty item = partiesCopy.get(position);
        holder.name.setText(item.getName());
        holder.headQtr.setText(item.getHeadQtr());
        holder.amt.setText(AddToCartView.toCurrency(item.getBalance()));

        final Drawable drawable = holder.character.getBackground();
        Random rnd = new Random();
        final int[] color = {Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))};
        drawable.setColorFilter(color[0], PorterDuff.Mode.SRC_IN);*/
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dr_gift_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindHeader(MyViewHolder viewHolder) {

    }

    @Override
    public void onClickListener() {

    }

    @Override
    public void onDivertListener() {

    }
}

