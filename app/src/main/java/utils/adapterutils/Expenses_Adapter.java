package utils.adapterutils;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.root.ExpenseRoot;

import java.util.ArrayList;
import java.util.Map;

import utils.MyConnection;

public class Expenses_Adapter extends BaseAdapter {

  Context context;
    LayoutInflater layoutInflater;
    ArrayList<Map<String, String>> dataList= new ArrayList<Map<String, String>>();

    private Expense_interface expense_interface;

    public interface Expense_interface {
        public void Edit_Expense(String who, String hed, String amt, String rem, final String path, String hed_id);
        public void delete_Expense(String hed_id);
    }
    public Expenses_Adapter(Context context, ArrayList<Map<String, String>> data){
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dataList = data;
        expense_interface= (Expense_interface) context ;

  }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null){
             holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.exp_row,null);

            holder.exp_hed=(TextView) convertView.findViewById(R.id.tv_exp_id);
            holder.exp_amt=(TextView) convertView.findViewById(R.id.tv_amt_id);
            holder.exp_remak=(TextView) convertView.findViewById(R.id.tv_rem_id);
            holder.edit_exp=(ImageView) convertView.findViewById(R.id.edit_exp);
            holder.delete_exp=(ImageView) convertView.findViewById(R.id.delete_exp);
            holder.attachment=(ImageView) convertView.findViewById(R.id.attach);

            convertView.setTag(holder);

        }else {
            holder = (ViewHolder) convertView.getTag();
        }



        holder.exp_hed.setText(dataList.get(position).get("exp_head"));
        holder.exp_amt.setText(dataList.get(position).get("amount"));
        holder.exp_remak.setText(dataList.get(position).get("remark"));
        holder.edit_exp.setVisibility(View.VISIBLE);
        holder.delete_exp.setVisibility(View.VISIBLE);

        if (!dataList.get(position).get("FILE_NAME").equals("")){
            holder.attachment.setVisibility(View.VISIBLE);
        }else{
            holder.attachment.setVisibility(View.GONE);
        }

       holder.edit_exp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expense_interface.Edit_Expense("1",dataList.get(position).get("exp_head"),dataList.get(position).get("amount"),dataList.get(position).get("remark"),dataList.get(position).get("FILE_NAME"),dataList.get(position).get("exp_head_id"));
            }
        });

       holder.delete_exp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expense_interface.delete_Expense(dataList.get(position).get("ID"));
            }
        });

        return convertView;
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    public class ViewHolder{

        TextView exp_hed,exp_amt,exp_remak;
        ImageView edit_exp,delete_exp,attachment;

    }
}
