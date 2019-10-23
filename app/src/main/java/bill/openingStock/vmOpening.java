package bill.openingStock;

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

import bill.BillReport.BillActivity;
import bill.BillReport.IBill;
import bill.BillReport.mBill;
import bill.BillReport.mCompany;
import bill.Cart.mCustomer;
import bill.NewOrder.mBillItem;
import bill.Outlet.mOutlet;
import bill.mBillOrder;
import cbomobilereporting.cbo.com.cboorder.Enum.eDeal;
import cbomobilereporting.cbo.com.cboorder.Enum.eDiscount;
import cbomobilereporting.cbo.com.cboorder.Enum.eTax;
import cbomobilereporting.cbo.com.cboorder.Model.mDeal;
import cbomobilereporting.cbo.com.cboorder.Model.mDiscount;
import cbomobilereporting.cbo.com.cboorder.Model.mTax;
import saleOrder.MyOrderAPIService;
import saleOrder.ViewModel.CBOViewModel;
import utils_new.AppAlert;
import utils_new.CustomDatePicker;

public class vmOpening extends CBOViewModel<IOpening> {
    private ArrayList<mOpening> billArrayList= new ArrayList<>();
    private Double billAmt=0.0;
    private mPage page;

    public mPage getPage() {
        return page;
    }

    public void setPage(mPage page) {
        this.page = page;
    }

    @Override
    public void onUpdateView(AppCompatActivity context, IOpening view) {
        if (view != null) {
            view.getReferencesById();
            view.getActivityTitle();
            view.setActvityTitle(view.getActivityTitle());
            view.updateTotBillAmt(billAmt);


        }

    }


    public void getBills(Context context, mCompany company, Date FDate, Date TDate) {

        HashMap<String, String> request = new HashMap<>();
        request.put("sCompanyFolder", MyCustumApplication.getInstance().getUser().getCompanyCode());
        request.put("LOGIN_PA_ID", MyCustumApplication.getInstance().getUser().getID());
        request.put("iCOMPANY_ID", company.getId());
        request.put("FDATE", CustomDatePicker.formatDate(FDate, CustomDatePicker.CommitFormat));
        request.put("TDATE", CustomDatePicker.formatDate(TDate, CustomDatePicker.CommitFormat));

        getBills(context, request);


    }



    public void getBills(Context context, HashMap<String, String> request) {


        ArrayList<Integer> tables = new ArrayList<>();
        tables.add(0);

        new MyOrderAPIService(context).execute(new ResponseBuilder(page.getOnLoadApi(), request)
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
                        AppAlert.getInstance().getAlert(context, s, s1);
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
                mOpening bill = new mOpening();
                bill.setPOSTING_ID(c.getString("POSTING_ID"));
                bill.setNO_ITEM(c.getString("NO_ITEM"));
                bill.setENTRY_BY(c.getString("ENTRY_BY"));
                bill.setDOC_DATE_ORDER(c.getString("DOC_DATE_ORDER"));
                bill.setDOC_DATE(CustomDatePicker.getDate(c.getString("DOC_DATE"), CustomDatePicker.ShowFormatOld));
                bill.setDOC_NO(c.getString("DOC_NO"));
                bill.setENTRY_BY_ID(c.getString("ENTRY_BY_ID"));
                bill.setCOMPANY_NAME(c.getString("COMPANY_NAME"));
                bill.setCOMPANY_ID(c.getString("COMPANY_ID"));

                bill.setEdit(c.getInt("EDITYN") == 1);
                bill.setDelete(c.getInt("DELETEYN") == 1);

                billArrayList.add(bill);


            }


        }


    }


    public void deleteBill(Context context, mOpening bill) {
        HashMap<String, String> request = new HashMap<>();
        request.put("sCompanyFolder", MyCustumApplication.getInstance().getUser().getCompanyCode());
        request.put("iPA_ID", MyCustumApplication.getInstance().getUser().getID());
        request.put("iPOSTING_ID", bill.getPOSTING_ID());

        ArrayList<Integer> tables = new ArrayList<>();
        tables.add(0);

        new MyOrderAPIService(context).execute(new ResponseBuilder(getPage().getOnDeleteApi(), request)
                .setTables(tables)
                .setResponse(new CBOServices.APIResponse() {
                    @Override
                    public void onComplete(Bundle bundle) throws Exception {
                        String table0 = bundle.getString("Tables0");
                        JSONArray row = new JSONArray(table0);
                        ((OpeningStockActivity) context).getBills();
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


    public void getBillDet(Context context, mOpening bill, String status) {

        mBillOrder order = new mBillOrder()
                .setDocDate(CustomDatePicker.formatDate(bill.getDOC_DATE(), CustomDatePicker.ShowFormatOld))
                .setPartyId(bill.getCOMPANY_ID())
                .setPartyName(bill.getCOMPANY_NAME())
                .setDocId(bill.getPOSTING_ID())
                .setDocNo(bill.getDOC_NO())
                .setBillNo(bill.getDOC_NO())
                .setStatus(status);
        order.getItems().clear();

        mCustomer company = new mCustomer();


        HashMap<String, String> request = new HashMap<>();
        request.put("sCompanyFolder", MyCustumApplication.getInstance().getUser().getCompanyCode());
        request.put("iPA_ID", MyCustumApplication.getInstance().getUser().getID());
        request.put("iPOSTING_ID", bill.getPOSTING_ID());

        ArrayList<Integer> tables = new ArrayList<>();
        tables.add(0);

        new MyOrderAPIService(context).execute(new ResponseBuilder(page.getOnDetailApi(), request)
                .setTables(tables)
                .setResponse(new CBOServices.APIResponse() {
                    @Override
                    public void onComplete(Bundle bundle) throws Exception {
                        if (view != null) {
                            view.showBillDetail(order, company);
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



                            if (OpeningStockActivity.DOC_TYPE.valueOf( page.getCode()) == OpeningStockActivity.DOC_TYPE.PHYSICAL_STOCK){
                                item.setStock(jsonObject2.getDouble("STOCK_AVIL"))
                                        .setQty(jsonObject2.getDouble("STOCK_PHY"));
                            }

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
                        AppAlert.getInstance().getAlert(context, s, s1);
                    }
                }));

    }


}
