package utils.adapterutils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;

import java.util.ArrayList;
import java.util.Map;

public class TP_Adapter extends BaseAdapter {

  Context context;
    LayoutInflater layoutInflater;
    ArrayList<Map<String, String>> dataList= new ArrayList<Map<String, String>>();

    public TP_Adapter(Context context, ArrayList<Map<String, String>> data){
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dataList = data;

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
            convertView = layoutInflater.inflate(R.layout.tp_reports_row,null);

            holder.date=(TextView) convertView.findViewById(R.id.tv_tp_date);
            holder.work_with=(TextView) convertView.findViewById(R.id.tv_tp_workwith);
            holder.area=(TextView) convertView.findViewById(R.id.tv_tp_area);
            holder.remark=(TextView) convertView.findViewById(R.id.tv_tp_remark);
            holder.doctor=(TextView) convertView.findViewById(R.id.tv_tp_doctor);
            holder.DA_layout = (TableLayout) convertView.findViewById(R.id.DA_layout);

            convertView.setTag(holder);

        }else {
            holder = (ViewHolder) convertView.getTag();
        }



        holder.date.setText(dataList.get(position).get("date"));
        holder.work_with.setText(dataList.get(position).get("work_with"));
        holder.area.setText(dataList.get(position).get("station"));
        holder.remark.setText(dataList.get(position).get("station_remark"));
        holder.doctor.setText(dataList.get(position).get("doctor"));


        if (dataList.get(position).get("doctor").split("\n").length>0){
            init(holder.DA_layout,dataList.get(position).get("doctor").split("\n"),dataList.get(position).get("potential").split("\n"),dataList.get(position).get("class").split("\n"),dataList.get(position).get("area").split("\n"));
        }

      /* holder.edit_exp.setOnClickListener(new View.OnClickListener() {
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
*/
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

        TextView date,work_with,area;
        TextView remark,doctor;
        TableLayout DA_layout;

    }

    private void init(TableLayout stk1, String[] doctor, String[] potential, String[] dr_class, String[] dr_area) {
        TableLayout stk=stk1;
        //ArrayList<String> sample_name= childText.get("sample_name").get(childPosition).toString();
        TableRow tbrow0 = new TableRow(context);
        tbrow0.setBackgroundColor(0xff125688);
        TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 1f);
        TextView tv0 = new TextView(context);
        tv0.setText("Doctor");
        tv0.setPadding(5, 5, 5, 0);
        tv0.setTextSize(12);
        tv0.setTextColor(Color.WHITE);
        tv0.setTypeface(null, Typeface.BOLD);
        tv0.setLayoutParams(params);
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(context);
        tv1.setText(" Potential ");
        tv1.setPadding(5, 5, 5, 0);
        tv1.setTextSize(12);
        tv1.setTextColor(Color.WHITE);
        tv1.setTypeface(null, Typeface.BOLD);
        tbrow0.addView(tv1);
        TextView tv2 = new TextView(context);
        tv2.setPadding(5, 5, 5, 0);
        tv2.setTextSize(12);
        tv2.setText(" Class ");
        tv2.setTextColor(Color.WHITE);
        tv2.setTypeface(null, Typeface.BOLD);
        tbrow0.addView(tv2);
        TextView tv3 = new TextView(context);
        tv3.setPadding(5, 5, 5, 0);
        tv3.setText(" Area ");
        tv3.setTextSize(12);
        tv3.setTextColor(Color.WHITE);
        tv3.setTypeface(null, Typeface.BOLD);
        tbrow0.addView(tv3);
        stk.removeAllViews();
        stk.addView(tbrow0);
        for (int i = 0; i < doctor.length; i++) {
            TableRow tbrow = new TableRow(context);
            TextView t1v = new TextView(context);
            t1v.setText(doctor[i]);
            t1v.setPadding(5, 5, 5, 0);
            t1v.setTextSize(10);
            t1v.setTextColor(Color.BLACK);
            t1v.setLayoutParams(params);
            tbrow.addView(t1v);
            TextView t2v = new TextView(context);
            t2v.setText(potential[i]);
            /*t2v.setText("0");*/
            t2v.setPadding(5, 5, 5, 0);
            t2v.setTextSize(10);
            t2v.setTextColor(Color.BLACK);
            t2v.setGravity(Gravity.CENTER);
            tbrow.addView(t2v);
            TextView t3v = new TextView(context);
            t3v.setText(dr_class[i]);
            /*t3v.setText("0");*/
            t3v.setPadding(5, 5, 5, 0);
            t3v.setTextSize(10);
            t3v.setTextColor(Color.BLACK);
            t3v.setGravity(Gravity.CENTER);
            tbrow.addView(t3v);
            TextView t4v = new TextView(context);
            t4v.setText(dr_area[i]);
            t4v.setMaxWidth(150);
            t4v.setLines(1);
            /*t3v.setText("0");*/
            t4v.setPadding(5, 5, 5, 0);
            t4v.setTextSize(10);
            t4v.setTextColor(Color.BLACK);
            t4v.setGravity(Gravity.CENTER);
            tbrow.addView(t4v);
            stk.addView(tbrow);
        }
        if(doctor.length==1 && doctor[0].equals("")){
            stk.removeAllViews();
        }

    }
}
