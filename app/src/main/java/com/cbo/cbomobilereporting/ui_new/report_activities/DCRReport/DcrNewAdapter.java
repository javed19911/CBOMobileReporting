package com.cbo.cbomobilereporting.ui_new.report_activities.DCRReport;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.ui.TotalChemistRpt;
import com.cbo.cbomobilereporting.ui.TotalDrRpt;
import com.cbo.cbomobilereporting.ui.TotalNonListedRpt;
import com.cbo.cbomobilereporting.ui.TotalRxRpt;
import com.cbo.cbomobilereporting.ui.TotalStockistRpt;

import java.util.ArrayList;
import utils_new.Custom_Variables_And_Method;

public class DcrNewAdapter extends RecyclerView.Adapter<DcrNewAdapter.MyviewHolder>{

    Context context;
    ArrayList<mDCR_Report> Rptdata=new ArrayList<mDCR_Report>();
    Custom_Variables_And_Method customVariablesAndMethod;
    CBO_DB_Helper cbohelp;
    String fmcgYn;
    String Report_PAID;
    public DcrNewAdapter(Context mcontext, ArrayList<mDCR_Report> data,String Report_PAID) {
        this.context = mcontext;
        this.Rptdata = data;
        customVariablesAndMethod= Custom_Variables_And_Method.getInstance(context);
        cbohelp = customVariablesAndMethod.get_cbo_db_instance();
        fmcgYn = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"fmcg_value");
        this.Report_PAID =  Report_PAID;
    }



    @Override
    public MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cborpt_row, parent, false);

        return new MyviewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyviewHolder holder, int position) {

        mDCR_Report rptmodel = Rptdata.get(position);


        holder.date.setText(rptmodel.getDate());
        holder.remark.setText(rptmodel.getRemark());
        holder.workwith.setText(rptmodel.getWith());
        holder.ttldr.setText(rptmodel.getTtldr());
        holder.TTlchm.setText(rptmodel.getTtlchm());
        holder.TTlstk.setText(rptmodel.getTtlstk());
        holder.TtlExp.setText(rptmodel.getTtlexp());
        holder.TtlMissed_call.setText(rptmodel.getTtlMissedCall());
        holder.TtlDoctor.setText(rptmodel.getTtlNonDoctor());
        holder.TttlDeRiminder.setText(rptmodel.getTtlDrRiminder());
        holder.TttlTenivia.setText(rptmodel.getTtlTenivia());
        holder.Dairy.setText(rptmodel.getDairyCount());
        holder.Polutary.setText(rptmodel.getPolutaryCount());

//        if (fmcgYn.equalsIgnoreCase("Y")){
//            holder.lLayoutDr.setVisibility(View.GONE);
//            // holder.totalDr_text.setText("Total Retailer :");
//
//        }

        if (Rptdata.get(position).isBlinkRemark()){
            Animation anim = new AlphaAnimation(0.0f, 1.0f);
            anim.setDuration(500);
            anim.setStartOffset(20);
            anim.setRepeatMode(Animation.REVERSE);
            anim.setRepeatCount(Animation.INFINITE);
            holder.remark.startAnimation(anim);
            holder.remark.setText("Final Submit Pending");
            holder.remark.setTextColor(Color.parseColor("#ff0000"));
        }else{
            holder.remark.setText(Rptdata.get(position).getRemark());
            holder.remark.clearAnimation();
            holder.remark.setTextColor(Color.parseColor("#000000"));
        }


        if(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"CUSTOMER_NOT_REQUIRED","Y").equals("N")) {
            holder.totalChem_text.setText("Total " + cbohelp.getMenu("DCR", "D_CUST_CALL").get("D_CUST_CALL").split(" ")[0] + " :");
        }
        if(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"CHEMIST_NOT_REQUIRED","Y").equals("N")) {
            holder.totalChem_text.setText("Total " + cbohelp.getMenu("DCR", "D_CHEMCALL").get("D_CHEMCALL").split(" ")[0] + " :");
        }


        holder.totalTenivia_text.setText(rptmodel.getRxCaps());


        if(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"Tenivia_NOT_REQUIRED","Y").equals("N")) {
            holder.totalTenivia_text.setText(cbohelp.getMenu("DCR", "D_DR_RX").get("D_DR_RX"));
        }

        if(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"Rx_NOT_REQUIRED","Y").equals("N")) {
            holder.totalTenivia_text.setText(cbohelp.getMenu("DCR", "D_RX_GEN").get("D_RX_GEN"));
        }

        if(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"Rx_NA_NOT_REQUIRED","Y").equals("N")) {
            holder.totalTenivia_text.setText(cbohelp.getMenu("DCR", "D_RX_GEN_NA").get("D_RX_GEN_NA"));
        }

        if ((Rptdata.get(position).getTtldr()).equals("0") &&
                customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"Doctor_NOT_REQUIRED").equals("Y")){
            holder.lLayoutDr.setVisibility(View.GONE);
        }else{
            holder.lLayoutDr.setVisibility(View.VISIBLE);
        }

        if ((Rptdata.get(position).getTtlstk()).equals("0") &&
                customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"STOCKIST_NOT_REQUIRED").equals("Y")){
            holder.lLayoutSTK.setVisibility(View.GONE);
        }else{
            holder.lLayoutSTK.setVisibility(View.VISIBLE);
        }

        if ((Rptdata.get(position).getTtlchm()).equals("0") &&
                (customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"CHEMIST_NOT_REQUIRED").equals("Y") &&
                        customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"CUSTOMER_NOT_REQUIRED").equals("Y"))){
            holder.chem.setVisibility(View.GONE);
        }else{
            holder.chem.setVisibility(View.VISIBLE);
        }



        if ((Rptdata.get(position).getTtlMissedCall()).equals("0")){
            holder.lLayoutMissed_call.setVisibility(View.GONE);
        }else{
            holder.lLayoutMissed_call.setVisibility(View.VISIBLE);
        }

        if ((Rptdata.get(position).getTtlDrRiminder()).equals("0")){
            holder.lLayoutDeRiminder.setVisibility(View.GONE);
        }else{
            holder.lLayoutDeRiminder.setVisibility(View.VISIBLE);
        }

        if ((Rptdata.get(position).getTtlNonDoctor()).equals("0")){
            holder.lLayoutDoctor.setVisibility(View.GONE);
        }else{
            holder.lLayoutDoctor.setVisibility(View.VISIBLE);
        }


        if ((Rptdata.get(position).getDairyCount()).equals("0")){
            holder.Dairy_layout.setVisibility(View.GONE);
        }else{
            holder.Dairy_layout.setVisibility(View.VISIBLE);
        }


        if ((Rptdata.get(position).getPolutaryCount()).equals("0")){
            holder.poltary_layout.setVisibility(View.GONE);
        }else{
            holder.poltary_layout.setVisibility(View.VISIBLE);
        }


        if (Rptdata.get(position).getTtlTenivia().equals("0")){
            holder.lLayoutTenivia.setVisibility(View.GONE);
        }/*else if (Integer.valueOf(Rptdata.get(position).getTtlTenivia())>1){
            holder.lLayoutTenivia.setVisibility(View.VISIBLE);
            holder.TttlTenivia.setBackgroundResource(R.drawable.button_text_view_selector_green);
        }*/else{
            holder.lLayoutTenivia.setVisibility(View.VISIBLE);
            //holder.TttlTenivia.setBackgroundResource(R.drawable.button_text_view_selector);
            holder.TttlTenivia.setBackgroundResource(R.drawable.button_text_view_selector_green);

        }
        holder.ttldr.setTag(position);
        holder.TTlchm.setTag(position);
        holder.TTlstk.setTag(position);
        holder.TtlExp.setTag(position);
        holder.TtlMissed_call.setTag(position);
        holder.TttlDeRiminder.setTag(position);
        holder.TttlTenivia.setTag(position);

        holder.ttldr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(v.getContext(), Rptdata.get(position).getDate(), Toast.LENGTH_LONG).show();
                if (!holder.ttldr.getText().toString().equals("0")) {
                    Intent intent = new Intent(v.getContext(), TotalDrRpt.class);
                    intent.putExtra("PAID", Report_PAID);
                    intent.putExtra("date", Rptdata.get(position).getDate());
                    intent.putExtra("call_type","D");
                    intent.putExtra("Title",cbohelp.getMenu("DCR", "D_DRCALL").get("D_DRCALL"));
                    v.getContext().startActivity(intent);
                }else{
                    customVariablesAndMethod.msgBox(context,"No Doctor in the list");
                }
            }
        });

        holder.Dairy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(v.getContext(), Rptdata.get(position).getDate(), Toast.LENGTH_LONG).show();
                if (!holder.Dairy.getText().toString().equals("0")) {
                    Intent intent = new Intent(v.getContext(), TotalDrRpt.class);
                    intent.putExtra("PAID", Report_PAID);
                    intent.putExtra("date", Rptdata.get(position).getDate());
                    intent.putExtra("call_type","DA");
                    intent.putExtra("Title",cbohelp.getMenu("DCR", "D_DAIRY").get("D_DAIRY"));
                    v.getContext().startActivity(intent);
                }else{
                    customVariablesAndMethod.msgBox(context,"No Dairy in the list");
                }
            }
        });
        holder.Polutary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(v.getContext(), Rptdata.get(position).getDate(), Toast.LENGTH_LONG).show();
                if (!holder.Polutary.getText().toString().equals("0")) {
                    Intent intent = new Intent(v.getContext(), TotalDrRpt.class);
                    intent.putExtra("PAID", Report_PAID);
                    intent.putExtra("date", Rptdata.get(position).getDate());
                    intent.putExtra("call_type","P");
                    intent.putExtra("Title",cbohelp.getMenu("DCR", "D_POULTRY").get("D_POULTRY"));
                    v.getContext().startActivity(intent);
                }else{
                    customVariablesAndMethod.msgBox(context,"No Polutary in the list");
                }
            }
        });

        holder.TTlchm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!holder.TTlchm.getText().toString().equals("0")) {
                    Intent ttlche=new Intent(v.getContext(),TotalChemistRpt.class);
                    ttlche.putExtra("PAID", Report_PAID);
                    ttlche.putExtra("date",Rptdata.get(position).getDate());
                    ttlche.putExtra("Title", holder.totalChem_text.getText().toString());
                    v.getContext().startActivity(ttlche);
                }else{
                    customVariablesAndMethod.msgBox(context,"No Chemist in the list");
                }
            }
        });
        holder.TTlstk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!holder.TTlstk.getText().toString().equals("0")) {
                    Intent ttlStk = new Intent(v.getContext(), TotalStockistRpt.class);
                    ttlStk.putExtra("PAID", Report_PAID);
                    ttlStk.putExtra("date", Rptdata.get(position).getDate());
                    v.getContext().startActivity(ttlStk);
                }else{
                    customVariablesAndMethod.msgBox(context,"No Stokist in the list");
                }

            }
        });

        holder.TtlDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!holder.TtlDoctor.getText().toString().equals("0")) {
                    Intent ttlStk = new Intent(v.getContext(), TotalNonListedRpt.class);
                    ttlStk.putExtra("PAID", Report_PAID);
                    ttlStk.putExtra("date", Rptdata.get(position).getDate());
                    v.getContext().startActivity(ttlStk);
                }else{
                    customVariablesAndMethod.msgBox(context,"No Non Listed in the list");
                }

            }
        });

        holder.TtlExp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.TtlMissed_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ttlRetailer = new Intent(v.getContext(), TotalDrRpt.class);
                ttlRetailer.putExtra("PAID", Report_PAID);
                ttlRetailer.putExtra("date",Rptdata.get(position).getDate());
                ttlRetailer.putExtra("call_type","M");
                ttlRetailer.putExtra("Title","Missed " + cbohelp.getMenu("DCR", "D_DRCALL").get("D_DRCALL"));
                v.getContext().startActivity(ttlRetailer);
            }
        });


        holder.TttlDeRiminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ttlRetailer = new Intent(v.getContext(), TotalDrRpt.class);
                ttlRetailer.putExtra("PAID", Report_PAID);
                ttlRetailer.putExtra("date",Rptdata.get(position).getDate());
                ttlRetailer.putExtra("call_type","R");
                ttlRetailer.putExtra("Title",cbohelp.getMenu("DCR", "D_RCCALL").get("D_RCCALL"));
                v.getContext().startActivity(ttlRetailer);
            }
        });

        holder.TttlTenivia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ttlRetailer = new Intent(v.getContext(), TotalRxRpt.class);
                ttlRetailer.putExtra("PAID", Report_PAID);
                ttlRetailer.putExtra("date",Rptdata.get(position).getDate());
                ttlRetailer.putExtra("call_type","D");
                ttlRetailer.putExtra("Title", holder.totalTenivia_text.getText().toString());
                v.getContext().startActivity(ttlRetailer);
            }
        });



        return;
    }

    @Override
    public int getItemCount() {
        return  Rptdata.size();
    }


    public class MyviewHolder extends RecyclerView. ViewHolder{

        TextView date;
        TextView remark;
        TextView workwith;
        Button ttldr;
        Button TTlchm;
        Button TTlstk;
        Button TtlExp;
        Button Dairy;
        Button Polutary;
        Button TtlMissed_call,TtlDoctor;
        Button TttlDeRiminder,TttlTenivia;
        LinearLayout lLayoutMissed_call;
        LinearLayout lLayoutDeRiminder,chem,lLayoutDoctor,lLayoutSTK,lLayoutTenivia;
        LinearLayout lLayoutDr,Dairy_layout,poltary_layout;
        TextView totalChem_text,totalTenivia_text;

        public MyviewHolder(View convertView) {
            super(convertView);

            date=(TextView)convertView.findViewById(R.id.filldate);
            remark=(TextView)convertView.findViewById(R.id.ttl_remark);
            workwith=(TextView)convertView.findViewById(R.id.worked_station);
            ttldr=(Button)convertView.findViewById(R.id.totaaldr);
            TTlchm=(Button)convertView.findViewById(R.id.ttl_chmist);
            TTlstk=(Button)convertView.findViewById(R.id.ttl_stckst);
            TtlExp=(Button)convertView.findViewById(R.id.ttl_exp);
            TtlDoctor =(Button) convertView.findViewById(R.id.ttl_doctor);
            TtlMissed_call =(Button) convertView.findViewById(R.id.ttl_missed_call);
            TttlDeRiminder =(Button) convertView.findViewById(R.id.ttl_distributor);
            TttlTenivia =(Button) convertView.findViewById(R.id.ttl_tenivia_call);
            Dairy =(Button) convertView.findViewById(R.id.ttl_dairy);
            Polutary =(Button) convertView.findViewById(R.id.ttl_poltary);

            lLayoutMissed_call =(LinearLayout) convertView.findViewById(R.id.missed_call_layout);
            lLayoutDeRiminder =(LinearLayout) convertView.findViewById(R.id.nolisted_Stk_layout);
            lLayoutDoctor=(LinearLayout) convertView.findViewById(R.id.nonlisted_doctor_layout);
            lLayoutDr=(LinearLayout) convertView.findViewById(R.id.cborpt_dr_layout);
            lLayoutSTK=(LinearLayout) convertView.findViewById(R.id.mob_loc);
            lLayoutTenivia=(LinearLayout) convertView.findViewById(R.id.Tenivia_layout);


            poltary_layout=(LinearLayout) convertView.findViewById(R.id.poltary_layout);
            Dairy_layout=(LinearLayout) convertView.findViewById(R.id.Dairy_layout);

            totalChem_text =(TextView) convertView.findViewById(R.id.totalChem_text);
            totalTenivia_text =(TextView) convertView.findViewById(R.id.tenivia_name);

            chem =(LinearLayout) convertView.findViewById(R.id.chem);
        }
    }
}
