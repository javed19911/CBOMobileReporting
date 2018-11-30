package utils.adapterutils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;

import java.util.ArrayList;
import java.util.Map;

import utils_new.Custom_Variables_And_Method;


public class Doctor_Visit_Adapter extends BaseAdapter {

  Context context;
    LayoutInflater layoutInflater;
    ArrayList<Map<String,String>> dataList= new ArrayList<Map<String,String>>();
    Custom_Variables_And_Method customVariablesAndMethod;
    String aT1;







    public Doctor_Visit_Adapter(Context context, ArrayList<Map<String,String>> data){

      this.context = context;
      layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      dataList = data;
      customVariablesAndMethod=Custom_Variables_And_Method.getInstance();

  }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null){
             holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.dr_visit_row,null);

            holder.DR_NAME=(TextView) convertView.findViewById(R.id.tv_dr_data);
            holder.SN=(TextView) convertView.findViewById(R.id.sn);
            holder.FREQ=(TextView) convertView.findViewById(R.id.tv_visit_fre_data);
            holder.CALL_DATE=(TextView) convertView.findViewById(R.id.tv_dr_visited_date);
            holder.REMARK=(TextView) convertView.findViewById(R.id.remark);
            holder.detail=(TableLayout) convertView.findViewById(R.id.details);
            convertView.setTag(holder);

        }else {
             holder = (ViewHolder) convertView.getTag();
        }

        ArrayList<String> tag=new ArrayList<>();
        ArrayList<String> value=new ArrayList<>();
       /* holder.DR_NAME.setText(dataList.get(position).get("DR_NAME"));
        holder.FREQ.setText(dataList.get(position).get("FREQ"));
        holder.CALL_DATE.setText(dataList.get(position).get("CALL_DATE"));*/
       /* if (dataList.get(position).get("REMARK").equals("")){
            holder.REMARK.setVisibility(View.GONE);
        }else {
            holder.REMARK.setVisibility(View.VISIBLE);
            holder.REMARK.setText("Remark : "+dataList.get(position).get("REMARK"));
        }*/

       /* if (!dataList.get(position).get("DR_CODE").equals("")){
        tag.add("Dr. Code");
        value.add(dataList.get(position).get("DR_CODE"));
        }*/
        /*if (!dataList.get(position).get("AREA").equals("")){
            tag.add("Area");
            if (dataList.get(position).get("AREA").length()>35){
                value.add(dataList.get(position).get("AREA").substring(0,35));
            }else {
                value.add(dataList.get(position).get("AREA"));
            }
        }*/
       /* if (!dataList.get(position).get("CLASS").equals("")){
            tag.add("Class");
            value.add(dataList.get(position).get("CLASS"));
        }*/
        //if (!dataList.get(position).get("FREQ").equals("")){
            tag.add("Visit Frequency");
            value.add(dataList.get(position).get("FREQ"));
        //}
        if (!dataList.get(position).get("NO_CALL").equals("")){
            tag.add("No of Calls");
            value.add(dataList.get(position).get("NO_CALL"));
        }
       // if (!dataList.get(position).get("CALL_DATE").equals("")){
            tag.add("Visited Dates");
            value.add(dataList.get(position).get("CALL_DATE"));
        //}
        if (!dataList.get(position).get("DR_SALE").equals("")){
            tag.add("Dr. Sales");
            value.add(dataList.get(position).get("DR_SALE"));
        }
        if (!dataList.get(position).get("MISSEDCALL").equals("")){
            tag.add("Missed Calls");
            value.add(dataList.get(position).get("MISSEDCALL"));
        }
        if (!dataList.get(position).get("LASTCALL").equals("")){
            tag.add("Last Call");
            value.add(dataList.get(position).get("LASTCALL"));
        }

        if (!dataList.get(position).get("DR_CAMP").equals("")){
            tag.add("Dr. Camp");
            value.add(dataList.get(position).get("DR_CAMP"));
        }
        /*if (!dataList.get(position).get("HOSPITAL_NAME").equals("")){
            tag.add("Hospital Name");
            value.add(dataList.get(position).get("HOSPITAL_NAME"));
        }*/
       /* if (!dataList.get(position).get("SPRODUCT").equals("")){
           // tag.add("Last Call");
            //value.add(dataList.get(position).get("SPRODUCT"));
        }*/

        int sn=position+1;
        holder.SN.setText(sn+". ");

        display_data_in_table(holder.detail,tag,value,sn+". "+dataList.get(position).get("DR_NAME")+"("+dataList.get(position).get("DR_CODE")+")","Class : "+dataList.get(position).get("CLASS"));



        return convertView;
    }

    private void display_data_in_table(TableLayout stk, ArrayList<String> tag, ArrayList<String> value, String Left_Head, String Right_Head){
        TableRow tbrow0 = new TableRow(context);
        tbrow0.setBackgroundColor(0xff125688);
       // tbrow0.setWeightSum(1);
        TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, .5f);
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
        params1.setMargins(130, 0, 130, 0);

        for (int i = 0; i < tag.size(); i++) {
            TableRow tbrow = new TableRow(context);
            tbrow.setLayoutParams(params1);
            //tbrow.setPadding(20,0,20,0);

            TextView t6v = new TextView(context);
            t6v.setBackgroundColor(0xf0125688);
            t6v.setWidth(2);
            tbrow.addView(t6v);


            tbrow.setWeightSum(1);
            TextView t1v = new TextView(context);
            t1v.setText(tag.get(i));
            t1v.setPadding(5, 5, 5, 0);
            t1v.setTextColor(Color.BLACK);
            t1v.setLayoutParams(params);
            tbrow.addView(t1v);

            TextView t4v = new TextView(context);
            t4v.setText(":");
            t4v.setTextColor(Color.BLACK);
            tbrow.addView(t4v);

            TextView t2v = new TextView(context);
            t2v.setText(value.get(i));
            t2v.setPadding(5, 5, 5, 0);
            t2v.setTextColor(Color.BLACK);
            t2v.setGravity(Gravity.RIGHT);
            t2v.setTypeface(null, Typeface.BOLD);
            t2v.setLayoutParams(params);
            tbrow.addView(t2v);
            stk.addView(tbrow);

            TextView t5v = new TextView(context);
            t5v.setBackgroundColor(0xf0125688);
            t5v.setWidth(2);
            tbrow.addView(t5v);

            TableRow tbrow1 = new TableRow(context);
            tbrow1.setBackgroundColor(0xf0125688);
            TextView t3v = new TextView(context);
            t3v.setHeight(2);
            tbrow1.setLayoutParams(params1);
            tbrow1.addView(t3v);
            stk.addView(tbrow1);
        }
        if(tag.size()==1 && tag.get(0).equals("0")){
            stk.removeAllViews();
        }
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

        TextView DR_NAME,FREQ,CALL_DATE,SN,REMARK;
        TableLayout detail;


    }
}
