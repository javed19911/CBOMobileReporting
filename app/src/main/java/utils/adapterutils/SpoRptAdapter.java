package utils.adapterutils;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.ui.SpoDistributorsWise;
import com.cbo.cbomobilereporting.ui.SpoHeadquarterWise;
import com.cbo.cbomobilereporting.ui.SpoProductWiseStock;

import java.util.ArrayList;

import services.ServiceHandler;
import utils.MyConnection;
import utils_new.Custom_Variables_And_Method;

/**
 * Created by RAM on 9/4/15.
 */
public class SpoRptAdapter extends BaseAdapter {


    Context context;
    Custom_Variables_And_Method customVariablesAndMethod;
    ServiceHandler myServices;
    String spoId;
    CBO_DB_Helper myDb;
     public static int clickCount =0;



    LayoutInflater layoutInflater;
    ArrayList<SpoModel> dataList = new ArrayList<SpoModel>();

    public SpoRptAdapter(Context context, ArrayList<SpoModel> arrayList){

        this.context = context;
        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
        myServices = new ServiceHandler(context);
        this.layoutInflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.dataList =arrayList;
        myDb = new CBO_DB_Helper(context);


    }



    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolderSpo holderSpo;

        if (convertView == null){

         convertView = layoutInflater.inflate(R.layout.spo_detail_view,null);
            holderSpo = new ViewHolderSpo();
            holderSpo.id = (TextView) convertView.findViewById(R.id.spo_cnf_id);
            holderSpo.consignee = (Button) convertView.findViewById(R.id.spo_cnf_consignee);
            holderSpo.salAmt = (TextView) convertView.findViewById(R.id.spo_cnf_sales_amt);
            holderSpo.salReturn =(TextView) convertView.findViewById(R.id.spo_cnf_sales_return);
            holderSpo.breakage_Exp =(TextView) convertView.findViewById(R.id.spo_cnf_breakage_exp);
            holderSpo.creaditNt_Other = (TextView) convertView.findViewById(R.id.spo_cnf_creadit_note_other);
            holderSpo.netSales = (TextView) convertView.findViewById(R.id.spo_cnf_net_sales);
            holderSpo.secSales = (TextView) convertView.findViewById(R.id.spo_cnf_Secondary_sales);
            holderSpo.recipt =(TextView) convertView.findViewById(R.id.spo_cnf_receipt);
            holderSpo.outStnding =(TextView) convertView.findViewById(R.id.spo_cnf_out_standing);
            holderSpo.stkAmt =(Button) convertView.findViewById(R.id.spo_cnf_stock_amt);
            convertView.setTag(holderSpo);
        }else {

            holderSpo =(ViewHolderSpo) convertView.getTag();

        }
        holderSpo.id.setText(dataList.get(position).getId());
        holderSpo.consignee.setText(dataList.get(position).getConsignee());
        holderSpo.salAmt.setText(dataList.get(position).getSalAmt());
        holderSpo.salReturn.setText(dataList.get(position).getSaleReturn());
        holderSpo.breakage_Exp.setText(dataList.get(position).getBreageExpiry());
        holderSpo.creaditNt_Other.setText(dataList.get(position).getCreditNotOrther());
        holderSpo.netSales.setText(dataList.get(position).getNetSales());
        holderSpo.secSales.setText(dataList.get(position).getSecSales());
        holderSpo.recipt.setText(dataList.get(position).getRecipt());
        holderSpo.outStnding.setText(dataList.get(position).getOutStanding());
        holderSpo.stkAmt.setText(dataList.get(position).getStockAmt());

        holderSpo.consignee.getTag(position);
        holderSpo.stkAmt.getTag(position);

        if (clickCount ==2){


            holderSpo.consignee.setClickable(false);
        }
        holderSpo.stkAmt.setVisibility(View.VISIBLE);
        if (clickCount ==3 ){
            holderSpo.stkAmt.setVisibility(View.GONE);
        }else {
            holderSpo.stkAmt.setVisibility(View.VISIBLE);
        }



        holderSpo.consignee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            String spoIdFromList = dataList.get(position).getId();

                if(clickCount<3){
                    clickCount = clickCount+1;
                }


                if (clickCount == 1){
                if (!spoIdFromList.equals("0")) {
                Intent spoHeadquarterWise = new Intent(context, SpoHeadquarterWise.class);
                    spoHeadquarterWise.putExtra("spoId", spoIdFromList);

                    v.getContext().startActivity(spoHeadquarterWise);
                }
                else {
                    customVariablesAndMethod.msgBox(context,dataList.get(position).getConsignee().toString());
                }

                //spoId = dataList.get(position).getId().toString();
           /*  String  resultHeadquator = myServices.getResponse_SpoCNFGrid(myDb.getCompanyCode(),""+MyConnection.PA_ID, LayoutZoomer.extraFrom,
                     LayoutZoomer.extraTo,"h",spoId,"0");
*/


            }
            else{
                    if (!spoIdFromList.equals("0")) {
                        Intent spoDistributorsWise = new Intent(context, SpoDistributorsWise.class);
                        spoDistributorsWise.putExtra("spoId", spoIdFromList);

                        v.getContext().startActivity(spoDistributorsWise);
                    }
                    else {
                        customVariablesAndMethod.msgBox(context,dataList.get(position).getConsignee().toString());
                    }
                  //clickCount = clickCount--;
                    //holderSpo.consignee.setClickable(false);

                }}
        });
        holderSpo.stkAmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String compName =dataList.get(position).getConsignee().toString();
                //mycon.msgBox(dataList.get(position).getStockAmt().toString());
                  Intent spoProductWiseStock = new Intent(context, SpoProductWiseStock.class);
                spoProductWiseStock.putExtra("company_name",compName);
                v.getContext().startActivity(spoProductWiseStock);

            }
        });





        return convertView;
    }

    class ViewHolderSpo{

        TextView id;
        Button consignee;
        TextView salAmt;
        TextView salReturn;
        TextView breakage_Exp;
        TextView creaditNt_Other;
        TextView netSales;
        TextView secSales;
        TextView recipt;
        TextView outStnding;
        Button stkAmt;
    }
}
