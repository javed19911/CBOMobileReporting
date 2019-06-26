package utils_new.cboUtils;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.ui_new.Model.mSPO;
import com.cbo.utils.MultiSelectView;

import cbomobilereporting.cbo.com.cboorder.interfaces.RecycleViewOnItemClickListener;

public class CBOImageView extends MultiSelectView<String,CBOImageView.MyViewHolder> {

    RecycleViewOnItemClickListener recycleViewOnItemClickListener = null;

    @Override
    public Boolean IsHeaderReqd() {
        return false;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView pescription;
        public TextView delete;
        public MyViewHolder(View view) {
            super(view);

            //city = (TextView) view.findViewById(R.id.city);
            pescription = view.findViewById(R.id.pescription);
            delete = view.findViewById(R.id.delete);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getDataList().remove(getAdapterPosition());
                    getAdapter().notifyItemRemoved(getAdapterPosition());
                }
            });


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getId() == R.id.delete){
                        getDataList().remove(getAdapterPosition());
                        getAdapter().notifyItemRemoved(getAdapterPosition());
                    }
                    else if (recycleViewOnItemClickListener != null) {
                        recycleViewOnItemClickListener.onClick(v,getAdapterPosition(),false);
                    }
                }
            });
        }
    }


    public CBOImageView(Context context) {
        super(context);
    }

    public CBOImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CBOImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CBOImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int position) {
        String path="";
            path=getDataList().get(position);
            Glide.with(getContext())
                    .load(path)
                    .error(R.drawable.no_image)
                    .into(viewHolder.pescription);

    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pescription_card, parent, false);

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