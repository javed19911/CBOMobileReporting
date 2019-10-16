package utils.adapterutils;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.cbo.cbomobilereporting.R;

import java.util.Arrays;
import java.util.List;

import utils_new.Custom_Variables_And_Method;


public class RCPA_Adapter extends ArrayAdapter<GiftModel> {

    private final List<GiftModel> list;
    private final AppCompatActivity context;
    Custom_Variables_And_Method customVariablesAndMethod;
    private String[] product,qty,remark;

    public RCPA_Adapter(AppCompatActivity context, List<GiftModel> list) {
        super(context, R.layout.stk_sample_row, list);
        this.context = context;
        this.list = list;
        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
    }

    static class ViewHolder {
        protected TextView text;
        protected TextView rate;
        protected TextView id;
        protected CheckBox checkbox;
        protected EditText scores;
        protected EditText sample;
        protected LinearLayout prescription;
        protected Button add;
        protected TableLayout details;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = null;

        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();

            view = inflator.inflate(R.layout.rcpa_row, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.text = (TextView) view.findViewById(R.id.label);
            viewHolder.checkbox = (CheckBox) view.findViewById(R.id.check);
            viewHolder.prescription = (LinearLayout) view.findViewById(R.id.prescription);
            viewHolder.add = (Button) view.findViewById(R.id.add);
            viewHolder.details= (TableLayout) view.findViewById(R.id.details);

            viewHolder.checkbox.setTag(list.get(position));




            view.setTag(viewHolder);
        } else {
            view = convertView;
            ((ViewHolder) view.getTag()).checkbox.setTag(list.get(position));
        }

        final ViewHolder holder = (ViewHolder) view.getTag();

        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                GiftModel element = (GiftModel) holder.checkbox.getTag();
                element.setSelected(buttonView.isChecked());
                if (isChecked){
                    holder.prescription.setVisibility(View.VISIBLE);
                }else {
                    holder.prescription.setVisibility(View.GONE);
                }
            }
        });

        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (list.get(position).getSample().split("\\|").length>=5){
                    customVariablesAndMethod.getAlert(context,"Limit exceeded","You can't add more than 5 products...");
                }else {
                    Add_prescription(position);
                }
            }
        });


        product=list.get(position).getSample().split("\\|");
        qty=list.get(position).getScore().split("\\|");
        remark=list.get(position).getRate().split("\\|");

        display_data_in_table(holder.details,Arrays.asList(product), Arrays.asList(qty), Arrays.asList(remark),"Product","Qty.");


        holder.text.setText(list.get(position).getName());
        holder.checkbox.setChecked(list.get(position).isSelected());
        if (list.get(position).isHighlighted()){
            holder.text.setTextColor(0xffFF8333);
        }else {
            holder.text.setTextColor(0xff000000);
        }
        return view;

    }



    public void Add_prescription(final int position){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setPositiveButton("ADD", null)
                .setCancelable(false)
                .setNegativeButton("Cancel", null);
        final AlertDialog dialog = builder.create();
        LayoutInflater inflater = context.getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.rcpa_pop_up, null);
         final EditText his_label,his_qty,his_remark;
        his_label= (EditText) dialogLayout.findViewById(R.id.his_label);
        his_qty= (EditText) dialogLayout.findViewById(R.id.his_qty);
        his_remark= (EditText) dialogLayout.findViewById(R.id.his_remark);


        dialog.setView(dialogLayout);
        dialog.setTitle("Add Prescription");


        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String label = his_label.getText().toString();
                String qty = his_qty.getText().toString();
                String remark = his_remark.getText().toString();


                //mycon.msgBox(dist_id3);
                if (label.equals("")) {
                    customVariablesAndMethod.msgBox(context,"Please Enter the Sample name...");
                } else if (qty.equals("")) {
                    customVariablesAndMethod.msgBox(context,"Please Enter the Qty....");
                } else if (remark.equals("")) {
                    customVariablesAndMethod.msgBox(context,"Please Enter the Remark....");
                } else {
                    if ( list.get(position).getSample().equals("")){
                        list.get(position).setSample(label);
                        list.get(position).setScore(qty);
                        list.get(position).setRate(remark);
                    }else {
                        list.get(position).setSample(list.get(position).getSample()+"|"+label);
                        list.get(position).setScore(list.get(position).getScore()+"|"+qty);
                        list.get(position).setRate(list.get(position).getRate()+"|"+remark);
                    }
                    notifyDataSetChanged();
                    dialog.dismiss();


                }
            }
        });
    }


    public void display_data_in_table(TableLayout stk, List<String> product, List<String> qty, List<String> remark, String Left_Head, String Right_Head){
        TableRow tbrow0 = new TableRow(context);
        tbrow0.setBackgroundColor(0xff125688);
        // tbrow0.setWeightSum(1);
        TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, .5f);
        TableRow.LayoutParams params2 = new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 1f);
        TextView tv0 = new TextView(context);
        tv0.setText(Left_Head);
        tv0.setPadding(5, 5, 5, 0);
        tv0.setTextColor(Color.WHITE);
        tv0.setTypeface(null, Typeface.BOLD);
        tv0.setLayoutParams(params);
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(context);
        tv1.setText(Right_Head);
        tv1.setPadding(5, 5, 5, 0);
        tv1.setTextColor(Color.WHITE);
        tv1.setTypeface(null, Typeface.BOLD);
        tv1.setGravity(Gravity.RIGHT);
        //tv1.setLayoutParams(params);
        tbrow0.addView(tv1);
        stk.removeAllViews();
        stk.addView(tbrow0);

        TableLayout.LayoutParams params1 = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
       // params1.setMargins(130, 0, 130, 0);

        for (int i = 0; i < product.size(); i++) {
            TableRow tbrow = new TableRow(context);
            tbrow.setLayoutParams(params1);
            //tbrow.setPadding(20,0,20,0);



            tbrow.setWeightSum(1);
            TextView t1v = new TextView(context);
            t1v.setText(product.get(i));
            t1v.setPadding(5, 5, 5, 0);
            t1v.setTextColor(Color.BLACK);
            t1v.setTypeface(null, Typeface.BOLD);
            t1v.setLayoutParams(params);
            tbrow.addView(t1v);

            TextView t4v = new TextView(context);
            t4v.setText(":");
            t4v.setTextColor(Color.BLACK);
            tbrow.addView(t4v);

            TextView t2v = new TextView(context);
            t2v.setText(qty.get(i));
            t2v.setPadding(5, 5, 5, 0);
            t2v.setTextColor(Color.BLACK);
            t2v.setGravity(Gravity.RIGHT);
            t2v.setTypeface(null, Typeface.BOLD);
            t2v.setLayoutParams(params);
            tbrow.addView(t2v);
            stk.addView(tbrow);

            TableRow tbrow2 = new TableRow(context);
            TextView t7v = new TextView(context);
            t7v.setText(remark.get(i));
            t7v.setPadding(5, 5, 5, 0);
            t7v.setTextColor(Color.BLACK);
            t7v.setLayoutParams(params2);
            tbrow2.addView(t7v);
            stk.addView(tbrow2);


            TableRow tbrow1 = new TableRow(context);
            tbrow1.setBackgroundColor(0xf0125688);
            TextView t3v = new TextView(context);
            t3v.setHeight(2);
            tbrow1.setLayoutParams(params1);
            tbrow1.addView(t3v);
            stk.addView(tbrow1);
        }
        if(product.size()==1 && product.get(0).equals("")){
            stk.removeAllViews();
        }
    }



}