package utils_new.cboUtils;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.Expense.mDistance;
import com.cbo.utils.MultiSelectView;

public class CBOStation extends MultiSelectView<mDistance, CBOStation.MyViewHolder> {

    private AlertDialog myalertDialog = null;


    public CBOStation(Context context) {
        super(context);
        initialize();
    }

    public CBOStation(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public CBOStation(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CBOStation(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize();
    }

    private void initialize(){
        setDivertReqd(false);
        setHeaderReqd(false);
        setTitle("Add Working Station");
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        mDistance item = getDataList().get(position);
        holder.exp_hed.setText(item.getName());

        holder.edit_exp.setVisibility(View.VISIBLE);
        holder.delete_exp.setVisibility(View.VISIBLE);

    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.exp_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindHeader(MyViewHolder myViewHolder) {

    }

    @Override
    public void onClickListener() {
        addStation(new mDistance(),-1);
    }

    @Override
    public void onDivertListener() {

    }

    private void addStation(mDistance distance,int position ){
        AlertDialog.Builder myDialog = new AlertDialog.Builder(getContext());
        final EditText editText = new EditText(getContext());
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(editText);
        if (position!= -1){
            editText.setText(distance.getName());
        }

        myDialog.setTitle(getTitle());
        myDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                myalertDialog.dismiss();
            }
        });

        myDialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                myalertDialog.dismiss();
                if (position != -1){
                    getDataList().remove(position);
                    getAdapter().notifyItemRemoved(position);
                }
                getDataList().add(distance.setName(editText.getText().toString()));
            }
        });

        myDialog.setView(layout);

        myalertDialog = myDialog.show();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView exp_hed,exp_amt,exp_remak;
        ImageView edit_exp,delete_exp,attachment;

        public MyViewHolder(View view) {
            super(view);
            exp_hed=(TextView) view.findViewById(R.id.tv_exp_id);
            exp_amt=(TextView) view.findViewById(R.id.tv_amt_id);
            exp_remak=(TextView) view.findViewById(R.id.tv_rem_id);
            edit_exp=(ImageView) view.findViewById(R.id.edit_exp);
            delete_exp=(ImageView) view.findViewById(R.id.delete_exp);
            attachment=(ImageView) view.findViewById(R.id.attach);

            exp_amt.setVisibility(GONE);
            exp_remak.setVisibility(GONE);
            attachment.setVisibility(GONE);

            edit_exp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addStation(getDataList().get(getAdapterPosition()),getAdapterPosition());
                }
            });

            delete_exp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getDataList().remove(getAdapterPosition());
                    getAdapter().notifyItemRemoved(getAdapterPosition());

                }
            });
        }
    }
}
