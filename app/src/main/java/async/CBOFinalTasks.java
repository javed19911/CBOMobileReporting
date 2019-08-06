package async;

import android.content.Context;

import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.Expense.OthExpenseDB;

import utils_new.Custom_Variables_And_Method;

/**
 * Created by kuldeep.Dwivedi on 12/2/2014.
 */
public class CBOFinalTasks {
    private Context mContext;
    private CBO_DB_Helper cbohelp;
    private Custom_Variables_And_Method customVariablesAndMethod;

    public CBOFinalTasks(Context context) {
        this.mContext = context;
        cbohelp = new CBO_DB_Helper(context);
       customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
    }



    public void releseResources() {
        cbohelp = new CBO_DB_Helper(mContext);
        Custom_Variables_And_Method.CHEMIST_NOT_VISITED = "";
        Custom_Variables_And_Method.STOCKIST_NOT_VISITED = "";
        cbohelp.deleteDoctorRc();
        cbohelp.deleteDoctorItem();
        cbohelp.deleteDoctorItemPrescribe();
        cbohelp.deleteDoctor();
        cbohelp.deleteFinalDcr();
        cbohelp.deleteDCRDetails();
        cbohelp.deletedcrFromSqlite();
        cbohelp.deleteTempChemist();
        cbohelp.deleteChemistSample();
        cbohelp.deleteChemistRecordsTable();
        cbohelp.deleteStockistRecordsTable();
        cbohelp.deleteTempStockist();
        cbohelp.deleteAllRecord();
        cbohelp.deleteAllRecordFromOneMinute();
        cbohelp.deleteAllRecord10();
        cbohelp.deleteTempDr();
        cbohelp.deleteDoctormore();
        cbohelp.delete_phallmst();
        cbohelp.deleteDCRDetails();
        cbohelp.deleteRcpa_Table();
        cbohelp.deleteFarmar_Table();
        cbohelp.delete_Rx_Table();

        cbohelp.delete_Expense();
        new OthExpenseDB(mContext).delete();
        cbohelp.delete_Nonlisted_calls();
        cbohelp.deleteDcrAppraisal();
        cbohelp.delete_EXP_Head();
        cbohelp.delete_tenivia_traker();
        cbohelp.delete_Lat_Long_Reg();
        cbohelp.delete_phdairy_dcr(null);
        cbohelp.delete_Item_Stock();

        customVariablesAndMethod.deleteFmcg_ByKey(mContext,"DCR_ID");
        customVariablesAndMethod.deleteFmcg_ByKey(mContext,"DcrPlantime");
        customVariablesAndMethod.deleteFmcg_ByKey(mContext,"D_DR_RX_VISITED");
        customVariablesAndMethod.deleteFmcg_ByKey(mContext,"CHEMIST_NOT_VISITED");
        customVariablesAndMethod.deleteFmcg_ByKey(mContext,"STOCKIST_NOT_VISITED");
        customVariablesAndMethod.deleteFmcg_ByKey(mContext,"dcr_date_real");
        customVariablesAndMethod.deleteFmcg_ByKey(mContext,"Dcr_Planed_Date");
        customVariablesAndMethod.deleteFmcg_ByKey(mContext,"CUR_DATE");

//        customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(mContext,"DCR_ID","");
//        customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(mContext,"DcrPlantime","");
//        customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(mContext,"D_DR_RX_VISITED","N");
        customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(mContext,"CHEMIST_NOT_VISITED","N");
        customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(mContext,"STOCKIST_NOT_VISITED","N");
//        customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(mContext,"dcr_date_real","");
//        customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(mContext,"Dcr_Planed_Date","");
//        customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(mContext,"CUR_DATE","");

        customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(mContext,"myKm1", "0.0");
        customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(mContext,"OveAllKm","0.0");
        customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(mContext,"final_km","0.0");

        customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(mContext,"DA_TYPE","0");
        customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(mContext,"da_val","0");
        customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(mContext,"distance_val","0");
        customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(mContext,"Final_submit","Y");
        customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(mContext,"work_type_Selected","w");


        //cbohelp.close();


    }
}
