package bill.BillReport;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.cbo.cbomobilereporting.MyCustumApplication;
import com.uenics.javed.CBOLibrary.CBOServices;
import com.uenics.javed.CBOLibrary.ResponseBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import bill.Cart.mCustomer;
import bill.NewOrder.mBillItem;
import bill.Outlet.mOutlet;
import bill.mBillOrder;
import cbomobilereporting.cbo.com.cboorder.Enum.eDeal;
import cbomobilereporting.cbo.com.cboorder.Enum.eDiscount;
import cbomobilereporting.cbo.com.cboorder.Enum.eTax;
import cbomobilereporting.cbo.com.cboorder.Model.mDeal;
import cbomobilereporting.cbo.com.cboorder.Model.mDiscount;
import cbomobilereporting.cbo.com.cboorder.Model.mItem;
import cbomobilereporting.cbo.com.cboorder.Model.mTax;
import saleOrder.MyOrderAPIService;
import saleOrder.ViewModel.CBOViewModel;
import services.MyAPIService;
import utils_new.AppAlert;
import utils_new.CustomDatePicker;

public class vmBill extends CBOViewModel<IBill> {
   private  ArrayList<mBill> billArrayList= new ArrayList<>();
   private Double billAmt=0.0;

   private mOutlet outlet;

    @Override
    public void onUpdateView(AppCompatActivity context, IBill view) {
        if (view != null) {
            view.getReferencesById();
            view.getActivityTitle();
            view.setActvityTitle(view.getActivityTitle());
            view.updateTotBillAmt(billAmt);

        }

    }


    public mOutlet getOutlet() {
        return outlet;
    }

    public void setOutlet(mOutlet outlet) {
        this.outlet = outlet;
    }

    public void getBills(Context context, mCompany company, Date FDate, Date  TDate){

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder", MyCustumApplication.getInstance().getUser().getCompanyCode());
        request.put("iPA_ID", MyCustumApplication.getInstance().getUser().getID());
        request.put("iCOMPANY_ID",company.getId());
        request.put("FDATE",CustomDatePicker.formatDate(FDate,CustomDatePicker.CommitFormat));
        request.put("TDATE",CustomDatePicker.formatDate(TDate,CustomDatePicker.CommitFormat) );

        getBills(context,request);



    }

    public void getBills(Context context, mOutlet outlet){

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder", MyCustumApplication.getInstance().getUser().getCompanyCode());
        request.put("iPA_ID", MyCustumApplication.getInstance().getUser().getID());
        request.put("iCOMPANY_ID",outlet.getCOMPANY_ID());
        request.put("FDATE",outlet.getFDATE());
        request.put("TDATE",outlet.getTDATE() );

        getBills(context,request);



    }

    public void getBills(Context context, HashMap<String,String> request){



        ArrayList<Integer> tables=new ArrayList<>();
        tables.add(0);

        new MyOrderAPIService(context).execute(new ResponseBuilder("BILL_GRID_MOBILE",request)
                .setTables(tables)
//                    .setShowProgess(items.size() == 0 )
                .setResponse(new CBOServices.APIResponse() {
                    @Override
                    public void onComplete(Bundle bundle) throws Exception {
                        if (view != null) {
                            view.onBillListlistchange(billArrayList);
                            view.updateTotBillAmt(billAmt);
                        }
                    }

                    @Override
                    public void onResponse(Bundle bundle) throws Exception {

                        parser2(bundle);
                    }

                    @Override
                    public void onError(String s, String s1) {
                        AppAlert.getInstance().getAlert(context,s,s1);
                    }
                }));

    }



    private void parser2(Bundle result) throws Exception {
        {
            String table0 = result.getString("Tables0");
            JSONArray row = new JSONArray(table0);
            billAmt = 0.0;
            billArrayList.clear();
            for (int i = 0; i < row.length(); i++) {
                JSONObject c = row.getJSONObject(i);
                mBill bill=new mBill();
                bill.setCOMPANY_NAME(c.getString("COMPANY_NAME"));
                bill.setPOSTING_ID(c.getString("POSTING_ID"));
                bill.setBILL_PRINT(c.getString("BILL_PRINT"));
                bill.setNET_AMT(c.getDouble("NET_AMT"));
                bill.setDOC_DATE(CustomDatePicker.getDate( c.getString("DOC_DATE"),CustomDatePicker.ShowFormatOld));
                bill.setPartyId(c.getString("COMPANY_ID"));
                bill.setPayMode(c.getString("PAYMENT_MODE"));
                bill.setParty_mobile(c.getString("MOBILE_NO"));
                bill.setDOC_NO(c.getString("DOC_NO"));

                bill.setPartyName(c.getString("PATIENT_NAME"));
                bill.setTaxAmt(c.getString("TAX_AMT"));
                bill.setAmt(c.getString("TOT_AMT"));

                bill.setDOA(c.getString("DOA"));
                bill.setDOB(c.getString("DOB"));

                bill.setGST_NO(c.getString("GST_NO"));

                bill.setEdit(c.getInt("EDITYN")== 1);
                bill.setDelete(c.getInt("DELETEYN") == 1);


                billAmt += bill.getNET_AMT();
                billArrayList.add(bill);


            }


        }


    }


    public void deleteBill(Context context, mBill bill){
        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder", MyCustumApplication.getInstance().getUser().getCompanyCode());
        request.put("iPA_ID", MyCustumApplication.getInstance().getUser().getID());
        request.put("iPOSTING_ID",bill.getPOSTING_ID());

        ArrayList<Integer> tables=new ArrayList<>();
        tables.add(0);

        new MyOrderAPIService(context).execute(new ResponseBuilder("BILL_DELETE_MOBILE",request)
                .setTables(tables)
                .setResponse(new CBOServices.APIResponse() {
                    @Override
                    public void onComplete(Bundle bundle) throws Exception {
                        String table0 = bundle.getString("Tables0");
                        JSONArray row = new JSONArray(table0);
                        ((BillActivity) context).getBills();
                    }

                    @Override
                    public void onResponse(Bundle bundle) throws Exception {



                    }

                    @Override
                    public void onError(String s, String s1) {
                        AppAlert.getInstance().getAlert(context,s,s1);
                    }
                }));

    }


    public void getBillDet(Context context, mBill bill,String status){

        mBillOrder order = new mBillOrder()
                .setDocDate(CustomDatePicker.formatDate( bill.getDOC_DATE(),CustomDatePicker.ShowFormatOld))
                .setPartyId(bill.getPartyId())
                .setPartyName(bill.getCOMPANY_NAME())
                .setPayMode(bill.getPayMode())
                .setDocId(bill.getPOSTING_ID())
                .setDocNo(bill.getDOC_NO())
                .setBillNo(bill.getBILL_PRINT())
                .setStatus(status);
        order.getItems().clear();

        mCustomer company = new mCustomer()
                .setMobile(bill.getParty_mobile())
                .setName(bill.getPartyName())
                .setDOA(bill.getDOA())
                .setDOB(bill.getDOB())
                .setGST_NO(bill.getGST_NO());


        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder", MyCustumApplication.getInstance().getUser().getCompanyCode());
        request.put("iPA_ID", MyCustumApplication.getInstance().getUser().getID());
        request.put("iPOSTING_ID",bill.getPOSTING_ID());

        ArrayList<Integer> tables=new ArrayList<>();
        tables.add(0);

        new MyOrderAPIService(context).execute(new ResponseBuilder("BILL_POPULATE_MOBILE",request)
                .setTables(tables)
                .setResponse(new CBOServices.APIResponse() {
                    @Override
                    public void onComplete(Bundle bundle) throws Exception {
                        if (view != null) {
                            view.showBillDetail(order,company);
                        }
                    }

                    @Override
                    public void onResponse(Bundle bundle) throws Exception {

                        String table0 = bundle.getString("Tables0");
                        JSONArray row = new JSONArray(table0);


                        for (int i = 0; i < row.length(); i++) {
                            JSONObject jsonObject2 = row.getJSONObject(i);
                            ArrayList<mDiscount> discounts = new ArrayList<>();
                            discounts.add(new mDiscount().setType(eDiscount.QI).setPercent(jsonObject2.getDouble("DISC_PERCENT")));
                            discounts.add(new mDiscount().setType(eDiscount.VI).setPercent(jsonObject2.getDouble("DISC_PERCENT1")));
                            discounts.add(new mDiscount().setType(eDiscount.P).setPercent(jsonObject2.getDouble("DISC_PERCENT2")));
                            mBillItem item = new mBillItem()
                                    .setBATCH_NO(jsonObject2.getString("BATCH_NO"))
                                    .setBATCH_ID(jsonObject2.getString("BATCH_ID"))
                                    .setId(jsonObject2.getString("ITEM_ID"))
                                    .setName(jsonObject2.getString("ITEM_NAME"))
                                    .setSALE_RATE(jsonObject2.getDouble("RATE"))
                                    .setMRP_RATE(jsonObject2.getDouble("RATE"))
                                    .setQty(jsonObject2.getDouble("QTY"))
                                    .setPACK(jsonObject2.getString("PACK"))
                                    .setAmt(jsonObject2.getDouble("AMOUNT"))
                                    .setMiscDiscount(discounts);

                            mTax GST = new mTax(eTax.getTax(jsonObject2.getInt("GST_TYPE")));
                            GST.setSGST(jsonObject2.getDouble("TAX_PERCENT1"))
                                    .setCGST(jsonObject2.getDouble("TAX_PERCENT"));

                            item.setGST(GST).setQty(jsonObject2.getDouble("QTY"));

                            mDeal deal = new mDeal();
                            deal.setType(eDeal.get(jsonObject2.getString("DEAL_TYPE")))
                                    .setId(jsonObject2.getInt("DEAL_ID"))
                                    .setFreeQty(jsonObject2.getDouble("DEAL_QTY"))
                                    .setQty(jsonObject2.getDouble("DEAL_ON"));
                            item.setDeal(deal);


                            item.setFreeQty(jsonObject2.getDouble("FREE_QTY"));

                            order.getItems().add(item);

                        }
                    }

                    @Override
                    public void onError(String s, String s1) {
                        AppAlert.getInstance().getAlert(context,s,s1);
                    }
                }));

    }



}
