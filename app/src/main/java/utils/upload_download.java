package utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;

import org.json.JSONArray;
import org.json.JSONObject;

import services.ServiceHandler;
import services.Up_Dwn_interface;
import utils_new.Custom_Variables_And_Method;

/**
 * Created by pc24 on 08/11/2016.
 */
public class upload_download extends AsyncTask<Void, Integer, Long> {
    ProgressDialog pd;
    private int progress;
    long val = 0;
    Context context;
    CBO_DB_Helper cbohelper;
    ServiceHandler serviceHandler;
    Custom_Variables_And_Method customVariablesAndMethod;
    int count = 0;
    int chem_count = 0;
    Up_Dwn_interface up_dwn_interface;

    public upload_download(Context context){
        this.context=context;
        up_dwn_interface=(Up_Dwn_interface ) context;
        this.execute();
        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
    }
    public upload_download(Context context,Up_Dwn_interface listener){
        this.context=context;
        up_dwn_interface = listener;
        this.execute();

        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
    }
    @Override
    protected Long doInBackground(Void... params) {

        cbohelper = new CBO_DB_Helper(context);

        cbohelper.delete_phitem();
        //cbohelper.delete_phdoctoritem();
        //cbohelper.delete_phdoctor();
        cbohelper.delete_phallmst();
        cbohelper.delete_phparty();
        cbohelper.delete_phrelation();
        cbohelper.delete_phitemspl();
        cbohelper.deleteFTPTABLE();
        //cbohelper.deleteApprisalValues();
        //cbohelper.deleteChemist();
        //getcount();

        try {
            String response;
            serviceHandler = new ServiceHandler(context);

            response = serviceHandler.getResultFrom_GetItemListForLocal(cbohelper.getCompanyCode(), "" + Custom_Variables_And_Method.PA_ID);
            if (!response.contains("[ERROR]")) {
                Log.v("Result1", "Invalid" + response);

                JSONObject comleteRsponse = new JSONObject(response);
                JSONArray completeTable = comleteRsponse.getJSONArray("Tables");
                JSONObject zero = completeTable.getJSONObject(0);
                JSONObject one = completeTable.getJSONObject(1);
                JSONObject two = completeTable.getJSONObject(2);
                JSONObject three = completeTable.getJSONObject(3);
                JSONObject four = completeTable.getJSONObject(4);
                JSONObject five = completeTable.getJSONObject(5);
                JSONObject six = completeTable.getJSONObject(6);
                JSONObject seven = completeTable.getJSONObject(7);
                JSONObject eight = completeTable.getJSONObject(8);
                JSONObject nine = completeTable.getJSONObject(9);
                JSONObject ten = completeTable.getJSONObject(11);

                JSONArray jsonArray0 = zero.getJSONArray("Tables0");
                JSONArray jsonArray1 = one.getJSONArray("Tables1");
                JSONArray jsonArray2 = two.getJSONArray("Tables2");
                JSONArray jsonArray3 = three.getJSONArray("Tables3");
                JSONArray jsonArray4 = four.getJSONArray("Tables4");
                JSONArray jsonArray5 = five.getJSONArray("Tables5");
                JSONArray jsonArray6 = six.getJSONArray("Tables6");
                JSONArray jsonArray7 = seven.getJSONArray("Tables7");
                JSONArray jsonArray8 = eight.getJSONArray("Tables8");
                JSONArray jsonArray9 = nine.getJSONArray("Tables9");
                JSONArray jsonArray10 = ten.getJSONArray("Tables11");

                for (int a = 0; a < jsonArray0.length(); a++) {
                    JSONObject jasonObj1 = jsonArray0.getJSONObject(a);
                    val = cbohelper.insertProducts(jasonObj1.getString("ITEM_ID"), jasonObj1.getString("ITEM_NAME"), Double.parseDouble(jasonObj1.getString("STK_RATE")), jasonObj1.getString("GIFT_TYPE"),jasonObj1.getString("SHOW_ON_TOP"),jasonObj1.getString("SHOW_YN"));
                    Log.e("%%%%%%%%%%%%%%%", "item insert");

                }
                /*for (int b = 0; b<jsonArray2.length();b++){
                    JSONObject jasonObj2 = jsonArray2.getJSONObject(b);
                    val=cbohelper.insertDoctorData(jasonObj2.getString("DR_ID"), jasonObj2.getString("ITEM_ID"),jasonObj2.getString("item_name"));
                    Log.e("%%%%%%%%%%%%%%%", "doctor insert");

                }*/
                for (int c = 0; c < jsonArray3.length(); c++) {

                    JSONObject jsonObject3 = jsonArray3.getJSONObject(c);
                    val = cbohelper.insert_phallmst(jsonObject3.getInt("ID"), jsonObject3.getString("TABLE_NAME"), jsonObject3.getString("FIELD_NAME"), jsonObject3.getString("REMARK"));
                    Log.e("%%%%%%%%%%%%%%%", "allmst_insert");
                }

                for (int d = 0; d < jsonArray4.length(); d++) {

                    JSONObject jsonObject4 = jsonArray4.getJSONObject(d);
                    val = cbohelper.insert_phparty(jsonObject4.getInt("PA_ID"), jsonObject4.getString("PA_NAME"),
                            jsonObject4.getInt("DESIG_ID"), jsonObject4.getString("CATEGORY"),
                            jsonObject4.getInt("HQ_ID"), jsonObject4.getString("PA_LAT_LONG"),
                            jsonObject4.getString("PA_LAT_LONG2"), jsonObject4.getString("PA_LAT_LONG3"),
                            jsonObject4.getString("SHOWYN"));
                    Log.e("%%%%%%%%%%%%%%%", "party_insert");

                }
                for (int e = 0; e < jsonArray5.length(); e++) {

                    JSONObject jsonObject5 = jsonArray5.getJSONObject(e);
                    val = cbohelper.insert_phrelation(jsonObject5.getInt("PA_ID"), jsonObject5.getInt("UNDER_ID"), jsonObject5.getInt("RANK"));
                    Log.e("%%%%%%%%%%%%%%%", "relation_insert");

                }

                for (int f = 0; f < jsonArray6.length(); f++) {

                    JSONObject jsonObject6 = jsonArray6.getJSONObject(f);
                    val = cbohelper.insert_phitempl(jsonObject6.getString("ITEM_ID"), jsonObject6.getString("DR_SPL_ID"), jsonObject6.getInt("SRNO"));
                    Log.e("%%%%%%%%%%%%%%%", "" + jsonObject6.getInt("SRNO"));


                }
                for (int f = 0; f < jsonArray7.length(); f++) {

                    JSONObject jsonObject7 = jsonArray7.getJSONObject(f);

                    val = cbohelper.insert_FtpData(jsonObject7.getString("WEB_IP"), jsonObject7.getString("WEB_USER"), jsonObject7.getString("WEB_PWD"), jsonObject7.getString("WEB_PORT"), jsonObject7.getString("WEB_ROOT_PATH"));
                    Log.e("%%%%%%%%%%%%%%%", "ftp_insert");
                }
                for (int g = 0; g < jsonArray9.length(); g++) {
                    JSONObject jsonObject9 = jsonArray9.getJSONObject(g);
                    count = jsonObject9.getInt("NO_DR");
                    chem_count = jsonObject9.getInt("NO_CHEM");
                }
                JSONObject jsonObjectLoginUrl = jsonArray10.getJSONObject(0);
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"Login_Url", jsonObjectLoginUrl.getString("LOGIN_URL"));
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"DR_ADDNEW_URL", jsonObjectLoginUrl.getString("DR_ADDNEW_URL"));
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"CHEM_ADDNEW_URL", jsonObjectLoginUrl.getString("CHEM_ADDNEW_URL"));
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"DRSALE_ADDNEW_URL", jsonObjectLoginUrl.getString("DRSALE_ADDNEW_URL"));
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"TP_ADDNEW_URL", jsonObjectLoginUrl.getString("TP_ADDNEW_URL"));
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"CHALLAN_ACK_URL", jsonObjectLoginUrl.getString("CHALLAN_ACK_URL"));
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"SECONDARY_SALE_URL", jsonObjectLoginUrl.getString("SECONDARY_SALE_URL"));
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"TP_APPROVE_URL", jsonObjectLoginUrl.getString("TP_APPROVE_URL"));

                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"PERSONAL_INFORMATION_URL", jsonObjectLoginUrl.getString("PERSONAL_INFORMATION_URL"));
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"CHANGE_PASSWORD_URL", jsonObjectLoginUrl.getString("CHANGE_PASSWORD_URL"));
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"CIRCULAR_URL", jsonObjectLoginUrl.getString("CIRCULAR_URL"));
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"DECLARATION_OF_SAVING_URL", jsonObjectLoginUrl.getString("DECLARATION_OF_SAVING_URL"));
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"SALARY_SLIP_URL", jsonObjectLoginUrl.getString("SALARY_SLIP_URL"));
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"FORM16_URL", jsonObjectLoginUrl.getString("FORM16_URL"));
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"ROUTE_MASTER_URL", jsonObjectLoginUrl.getString("ROUTE_MASTER_URL"));
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"HOLIDAY_URL", jsonObjectLoginUrl.getString("HOLIDAY_URL"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

       // cbohelper.close();

        return val;

    }

    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        super.onPreExecute();
        pd = new ProgressDialog(context);
        //pd.setTitle("CBO");
        pd.setMessage("Downloading Miscellaneous data.." + "\n" + "please wait");
        //pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setCancelable(false);

        pd.setCanceledOnTouchOutside(false);
        pd.show();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {

			/*--- show download progress on main UI thread---*/

        pd.setProgress((values[0]) * 100 / count);

        //pd.setText((values[0]*100)/count +" % downloaded");
        pd.setMessage((values[0] * 100) / count + " % downloaded");

        super.onProgressUpdate(values);
        Log.e("mycount", "" + count);

    }

    @Override
    protected void onPostExecute(Long result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);

        //mycon.msgBox(""+result);
        //mycon.msgBox(""+chem_count);

        if (result > 0) {
            //new Doback_phDoctorMaster().execute();
            customVariablesAndMethod.msgBox(context,"Data Downloaded Successfully...");
            //up_dwn_interface=(Up_Dwn_interface ) context;
           up_dwn_interface.onDownloadComplete();
        }
        pd.dismiss();


    }
}
