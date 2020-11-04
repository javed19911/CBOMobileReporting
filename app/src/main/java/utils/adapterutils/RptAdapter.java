package utils.adapterutils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
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

/**
 * Created by Kuldeep.Dwivedi on 3/22/2015.
 */
public class RptAdapter extends BaseAdapter{
    Context context;
    LayoutInflater layoutInflater;
    CBO_DB_Helper cbohelp;
    //public ViewClickListener mViewClickListener;
    ArrayList<RptModel> Rptdata=new ArrayList<RptModel>();
    Custom_Variables_And_Method customVariablesAndMethod;
    String fmcgYn;

    public RptAdapter(Context mContext,ArrayList<RptModel>data){
        this.context=mContext;
        layoutInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.Rptdata=data;
       customVariablesAndMethod=Custom_Variables_And_Method.getInstance(context);
        cbohelp = customVariablesAndMethod.get_cbo_db_instance();
        fmcgYn = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"fmcg_value");

    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return Rptdata.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.cborpt_row, null);
            holder = new ViewHolder();
            holder.date=(TextView)convertView.findViewById(R.id.filldate);
            holder.remark=(TextView)convertView.findViewById(R.id.ttl_remark);
            holder.workwith=(TextView)convertView.findViewById(R.id.worked_station);
            holder.ttldr=(Button)convertView.findViewById(R.id.totaaldr);
            holder.TTlchm=(Button)convertView.findViewById(R.id.ttl_chmist);
            holder.TTlstk=(Button)convertView.findViewById(R.id.ttl_stckst);
            holder.TtlExp=(Button)convertView.findViewById(R.id.ttl_exp);
            holder.TtlDoctor =(Button) convertView.findViewById(R.id.ttl_doctor);
            holder.TtlMissed_call =(Button) convertView.findViewById(R.id.ttl_missed_call);
            holder.TttlDeRiminder =(Button) convertView.findViewById(R.id.ttl_distributor);
            holder.TttlTenivia =(Button) convertView.findViewById(R.id.ttl_tenivia_call);
            holder.Dairy =(Button) convertView.findViewById(R.id.ttl_dairy);
            holder.Polutary =(Button) convertView.findViewById(R.id.ttl_poltary);

            holder.lLayoutMissed_call =(LinearLayout) convertView.findViewById(R.id.missed_call_layout);
            holder.lLayoutDeRiminder =(LinearLayout) convertView.findViewById(R.id.nolisted_Stk_layout);
            holder.lLayoutDoctor=(LinearLayout) convertView.findViewById(R.id.nonlisted_doctor_layout);
            holder.lLayoutDr=(LinearLayout) convertView.findViewById(R.id.cborpt_dr_layout);
            holder.lLayoutSTK=(LinearLayout) convertView.findViewById(R.id.mob_loc);
            holder.lLayoutTenivia=(LinearLayout) convertView.findViewById(R.id.Tenivia_layout);


            holder.poltary_layout=(LinearLayout) convertView.findViewById(R.id.poltary_layout);
            holder.Dairy_layout=(LinearLayout) convertView.findViewById(R.id.Dairy_layout);

            holder.totalChem_text =(TextView) convertView.findViewById(R.id.totalChem_text);
            holder.totalTenivia_text =(TextView) convertView.findViewById(R.id.tenivia_name);

            holder.chem =(LinearLayout) convertView.findViewById(R.id.chem);

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.date.setText(Rptdata.get(position).getDate());
        holder.remark.setText(Rptdata.get(position).getRemark());
        holder.workwith.setText(Rptdata.get(position).getWith());
        holder.ttldr.setText(Rptdata.get(position).getTtldr());
        holder.TTlchm.setText(Rptdata.get(position).getTtlchm());
        holder.TTlstk.setText(Rptdata.get(position).getTtlstk());
        holder.TtlExp.setText(Rptdata.get(position).getTtlexp());
        holder.TtlMissed_call.setText(Rptdata.get(position).getTtlMissedCall());
        holder.TtlDoctor.setText(Rptdata.get(position).getTtlNonDoctor());
        holder.TttlDeRiminder.setText(Rptdata.get(position).getTtlDrRiminder());
        holder.TttlTenivia.setText(Rptdata.get(position).getTtlTenivia());
        holder.Dairy.setText(Rptdata.get(position).getDairyCount());
        holder.Polutary.setText(Rptdata.get(position).getPolutaryCount());


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
            holder.remark.setTextColor(Color.parseColor("#000"));
        }

        if (fmcgYn.equalsIgnoreCase("Y")){
            holder.lLayoutDr.setVisibility(View.GONE);
           // holder.totalDr_text.setText("Total Retailer :");

        }
        if(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"CHEMIST_NOT_REQUIRED","Y").equals("N")) {
            holder.totalChem_text.setText("Total " + cbohelp.getMenu("DCR", "D_CHEMCALL").get("D_CHEMCALL").split(" ")[0] + " :");
        }

        if(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"Tenivia_NOT_REQUIRED","Y").equals("N")) {
            holder.totalTenivia_text.setText(cbohelp.getMenu("DCR", "D_DR_RX").get("D_DR_RX"));
        }

        if(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"Rx_NOT_REQUIRED","Y").equals("N")) {
            holder.totalTenivia_text.setText(cbohelp.getMenu("DCR", "D_RX_GEN").get("D_RX_GEN"));
        }

        if(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"Rx_NA_NOT_REQUIRED","Y").equals("N")) {
            holder.totalTenivia_text.setText(cbohelp.getMenu("DCR", "D_RX_GEN_NA").get("D_RX_GEN_NA"));
        }

        if ((Rptdata.get(position).getTtldr()).equals("")){
            holder.lLayoutDr.setVisibility(View.GONE);
        }else{
            holder.lLayoutDr.setVisibility(View.VISIBLE);
        }

        if ((Rptdata.get(position).getTtlstk()).equals("")){
            holder.lLayoutSTK.setVisibility(View.GONE);
        }else{
            holder.lLayoutSTK.setVisibility(View.VISIBLE);
        }

        if ((Rptdata.get(position).getTtlchm()).equals("")){
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
                    ttlche.putExtra("date",Rptdata.get(position).getDate());
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
                ttlRetailer.putExtra("date",Rptdata.get(position).getDate());
                ttlRetailer.putExtra("call_type","D");
                ttlRetailer.putExtra("Title", holder.totalTenivia_text.getText().toString());
                v.getContext().startActivity(ttlRetailer);
            }
        });

        if(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"CHEMIST_NOT_REQUIRED").equals("Y")){
            holder.chem.setVisibility(View.GONE);
        }

        return convertView;
    }


}

class ViewHolder {
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


}