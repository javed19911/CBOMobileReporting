package utils_new.cboUtils;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.Expense.eExpense;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.Expense.mOthExpense;
import com.cbo.utils.MultiSelectDetailView;

import java.util.ArrayList;

import cbomobilereporting.cbo.com.cboorder.Utils.AddToCartView;

public class CBOOtherExpense extends MultiSelectDetailView<mOthExpense, CBOOtherExpense.MyViewHolder> {


    Fragment fragment;
    AppCompatActivity activity;
    eExpense exp_type = eExpense.None;

    private Double DefaultAmount = 0d;
    private Double ManualAount = 0d;
    private String OtherDetail ="";
    private Boolean ManualMandetory = false;

    private Expense_interface expense_interface;

    public interface Expense_interface {
        public void AddExpenseClicked(eExpense expense_type);
        public void Edit_ExpenseClicked(mOthExpense othExpense,eExpense exp_type);
        public void delete_ExpenseClicked(mOthExpense othExpense,eExpense exp_type);
    }

    public CBOOtherExpense(Context context) {
        super(context);
        initialize();
    }

    public CBOOtherExpense(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public CBOOtherExpense(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CBOOtherExpense(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize();
    }

    public void initialize(){
        setHeaderReqd(false);
        setTitle("Other Expense");
    }

    public void setContext(Fragment fragment, eExpense exp_type){
        this.fragment = fragment;
        setExp_type(exp_type);
    }
    public void setContext(AppCompatActivity activity, eExpense exp_type){
        this.activity = activity;
        setExp_type(exp_type);
    }

    public eExpense getExp_type() {
        return exp_type;
    }

    public void setExp_type(eExpense exp_type) {
        this.exp_type = exp_type;
        setTitle();
    }

    public void setTitle(){
        String title = "";
        String subTitle ="";
        switch (getExp_type()){
            case TA:
                title = "TA";
                break;
            case DA:
                title = "DA";
                break;
                default:
                    title = "Other Expenses";
        }
        if ((!getOtherDetail().isEmpty() && !getOtherDetail().contains("*"))
                || (!getOtherDetail().isEmpty() && ManualAount == 0 && DefaultAmount !=0 && !(IsAddBtnReqd() && IsManualMandetory()))) {
            subTitle =  " ( " + getOtherDetail() + " ) ";
        }

        setTitle(title);
        setSubTitle(subTitle);
    }

    public void setListener(Expense_interface listener){
        expense_interface = listener;
    }

    public String getOtherDetail() {
        return OtherDetail;
    }

    public void setOtherDetail(String otherDetail) {
        OtherDetail = otherDetail;
        setTitle();
    }

    public Double getDefaultAmount() {
        return DefaultAmount;
    }

    public void setDefaultAmount(Double defaultAmount) {
        DefaultAmount = defaultAmount;
        updateDataList(getDataList());
    }

    public Boolean IsManualMandetory() {
        return ManualMandetory;
    }

    public void setManualMandetory(Boolean manualMendetory) {
        ManualMandetory = manualMendetory;
    }

    public Double getAmount(){
        return ManualAount > 0 || (IsAddBtnReqd() && IsManualMandetory())? ManualAount : DefaultAmount;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        mOthExpense item = getDataList().get(position);
        holder.exp_hed.setText(item.getExpHead().getName());
        holder.exp_amt.setText(""+item.getAmount());
        if (item.getKm() >0 && item.getAmount() > 0){
            String kmDescription = item.getKm() + "Kms * " +  AddToCartView.toCurrency(String.format("%.2f",item.getAmount()/ item.getKm())) + "/km";
            holder.exp_remak.setText(kmDescription + "\n"+item.getRemark());
        }else{
            holder.exp_remak.setText(item.getRemark());
        }

        holder.edit_exp.setVisibility(item.IsEditable()? View.VISIBLE : GONE);
        holder.delete_exp.setVisibility(item.IsEditable()? View.VISIBLE : GONE);

        if (!item.getAttachment().equals("")){
            holder.attachment.setVisibility(View.VISIBLE);
        }else{
            holder.attachment.setVisibility(View.GONE);
        }
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
        if (expense_interface != null){
            expense_interface.AddExpenseClicked(getExp_type());
        }
    }


    @Override
    public MultiSelectDetailView updateDataList(ArrayList<mOthExpense> datalist) {
        super.updateDataList(datalist);
        Double total = 0d;
        for (mOthExpense othExpense : datalist){
            total += othExpense.getAmount();
        }
        ManualAount = total;
        setDetail(AddToCartView.toCurrency(String.format("%.2f", getAmount())));
        setTitle();
        return this;
    }

    @Override
    public MultiSelectDetailView setAddBtnReqd(Boolean required) {
        super.setAddBtnReqd(required);
        updateDataList(getDataList());
        return this;
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



            edit_exp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (expense_interface != null) {
                        expense_interface.Edit_ExpenseClicked(getDataList().get(getAdapterPosition()), getExp_type());
                    }
                }
            });

            delete_exp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (expense_interface != null) {
                        expense_interface.delete_ExpenseClicked(getDataList().get(getAdapterPosition()), getExp_type());
                    }
                }
            });
        }
    }
}
