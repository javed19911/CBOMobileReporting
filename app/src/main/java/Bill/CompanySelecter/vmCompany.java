package Bill.CompanySelecter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.cbo.cbomobilereporting.MyCustumApplication;
import com.uenics.javed.CBOLibrary.CBOServices;
import com.uenics.javed.CBOLibrary.ResponseBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import Bill.BillReport.mCompany;
import saleOrder.ViewModel.CBOViewModel;
import services.MyAPIService;
import utils_new.AppAlert;

public class vmCompany extends CBOViewModel<ICompany> {
    private Context context;
private mCompany company=new mCompany();
private  ArrayList <mCompany> Partylist= new ArrayList<>();
private String ComponeyId="";
private Date FDATE;
private Date TDATE;

    ICompany iCompany=null;
    public  void setListner(ICompany iTeam){
        this.iCompany=iTeam;

    }

    @Override
    public void onUpdateView(Activity context, ICompany view) {
        if(view!=null){
            view.getReferencesById();
            view.setFDATE();
            view.setTDATE();
        }

        getCompanyDDl(context);
    }


    public String getComponeyId() {
        return ComponeyId;
    }

    public Date getFDATE() {
        return FDATE;
    }

    public Date getTDATE() {
        return TDATE;
    }
public ArrayList getPartylist(){
        return Partylist;
}
    public void setComponeyId(String componeyId) {
        ComponeyId = componeyId;
    }

    public Date setFDATE(Date FDATE) {
        this.FDATE = FDATE;
        return  FDATE;
    }

    public void setTDATE(Date TDATE) {
        this.TDATE = TDATE;
    }

    public  mCompany getCompany(){
        return  company;
    }

    private void getCompanyDDl(Context context) {
        HashMap<String, String> request = new HashMap<>();

        request.put("sCompanyFolder", MyCustumApplication.getInstance().getUser().getCompanyCode());
        request.put("iPA_ID", MyCustumApplication.getInstance().getUser().getID());
        ArrayList<Integer> tables = new ArrayList<>();
        tables.add(0);


        new MyAPIService(context)
                .execute(
                        new ResponseBuilder("BILLDDL_MOBILE", request)

                                .setTables(tables)
                                .setResponse(
                                        new CBOServices.APIResponse() {
                                            @Override
                                            public void onComplete(Bundle bundle) {
                                                if (view != null) {
                                                    view.onPartyListUpdated(Partylist);

                                                }

                                            }

                                            @Override
                                            public void onResponse(Bundle bundle) throws Exception {
                                                parser1(bundle);
                                                //"COMPANY_ID":"23","COMPANY_NAME":"ABN Medical Specialties"
                                            }

                                            @Override
                                            public void onError(String s, String s1) {
                                                AppAlert.getInstance().getAlert(context,s,s1);                                          }
                                        }));


    }

    private void parser1(Bundle bundle) throws JSONException {
        String table0 = bundle.getString("Tables0");
        JSONArray jsonArray = null;
        jsonArray = new JSONArray(table0);
        Partylist.clear();
        //  Partylist.add(new DropDownModel("--Select--", "0"));
        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject c = jsonArray.getJSONObject(i);
            Partylist.add(new mCompany(c.getString("COMPANY_NAME"), c.getString("COMPANY_ID")));
            if(i==jsonArray.length()-1){

              //view.onPartyListUpdated();
            }
        }




    }

}
