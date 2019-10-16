package utils_new.cboUtils;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.Enum.CallType;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.lead.LeadActivity;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.lead.SamplePOBBuilder;
import com.cbo.utils.MultiSelectView;

import utils.adapterutils.PobModel;

public class CBOGift extends MultiSelectView<PobModel,CBOGift.MyViewHolder> {

    public static final int GIFT_DIALOG= 10001;
    Fragment fragment;
    AppCompatActivity activity;
    CallType callType = CallType.NONE;
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
        initialize();
    }

    public CBOGift(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public CBOGift(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CBOGift(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize();
    }

    private void initialize(){
        setDivertReqd(false);
        setTitle("Gift");
    }

    public void setActivity(Fragment fragment,CallType callType){
        this.fragment = fragment;
        setCallType(callType);
    }
    public void setActivity(AppCompatActivity activity, CallType callType){
        this.activity = activity;
        setCallType(callType);
    }

    public CallType getCallType() {
        return callType;
    }

    public void setCallType(CallType callType) {
        this.callType = callType;
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
        if (activity != null || fragment != null) {
            SamplePOBBuilder samplePOBBuilder = new SamplePOBBuilder()
                    .setCallType(getCallType())
                    .setType(SamplePOBBuilder.ItemType.GIFT)
                    .setTitle("Select "+ getTitle()+"...")
                    .setItems(getDataList());
            Intent intent = new Intent(getContext(), LeadActivity.class);
            intent.putExtra("builder", samplePOBBuilder);
            if (activity != null) {
                (activity).startActivityForResult(intent, GIFT_DIALOG);
            }else{
                (fragment).startActivityForResult(intent, GIFT_DIALOG);
            }
        }

    }

    @Override
    public void onDivertListener() {

    }
}

