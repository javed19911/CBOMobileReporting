package services;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import utils.AppConstant;
import utils_new.Custom_Variables_And_Method;
import utils_new.SendMailTask;

public class ServiceHandler {
    public static SoapPrimitive result;
    private Context context;

    public static String URL= AppConstant.MAIN_URL;

    public static final String NAMESPACE = "http://tempuri.org/";
    Custom_Variables_And_Method customVariablesAndMethod;

    public ServiceHandler(Context context){
        this.context =context;
        customVariablesAndMethod = Custom_Variables_And_Method.getInstance();
        URL= customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"WEBSERVICE_URL",URL);
    }


    //////////////////////ComplaintGrid Start////////////////////
    public String getResponse_ComplaintGrid(String sCompanyFolder, String iPA_ID) {

        //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder", sCompanyFolder);
        request.put("iPA_ID", iPA_ID);

        //End of call to service

        return customMethodForAllServices(request,"ComplaintGrid");


    }
    //////////////////////ComplaintGrid End////////////////////

    ///////////////////////&&&&&&&&**********VISUALAID_DOWNLOAD Srat//////////////%%%%%%%%%%%%/////////
    public String getResponse_VISUALAID_DOWNLOAD(String sCompanyFolder, String iPA_ID) {
        //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder", sCompanyFolder);
        request.put("iPA_ID", iPA_ID);

        //End of call to service

        return customMethodForAllServices(request,"VISUALAID_DOWNLOAD");


    }
    ///////////////////////&&&&&&&&********** VISUALAID_DOWNLOAD End //////////////%%%%%%%%%%%%/////////

    /////////////////-------------DCRCALLDETMOBILEGRID Start-----------------/////////////////////

    public String getResponse_DCRCALLDETMOBILEGRID(String sCompanyFolder, String iPaId, String sFDATE, String sTDATE, String sAREA, String iTime, String sTEAMYN) {
     //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder", sCompanyFolder);
        request.put("iPaId", iPaId);
        request.put("sFDATE", sFDATE);
        request.put("sTDATE", sTDATE);
        request.put("sAREA", sAREA);
        request.put("iTime", iTime);
        request.put("sTEAMYN", sTEAMYN);

        //End of call to service

        return customMethodForAllServices(request,"DCRCALLDETMOBILEGRID");
    }

    /////////////Complaint_Docno Start /////////////////
    public String getResponse_Complaint_Docno(String sCompanyFolder) {
       //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder", sCompanyFolder);

        //End of call to service

        return customMethodForAllServices(request,"Complaint_Docno");
    }



    /////////////COMPLAINT_COMMIT Start /////////////////////////
    public String getResponse_COMPLAINT_COMMIT(String sCompanyFolder, String iPA_ID, String iRETAIL_PA_ID, String iDOC_NO
            , String iDOC_DATE, String iCHEMID, String sCOMPLAINT, String FileName) {
       //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder", sCompanyFolder);
        request.put("iPA_ID", iPA_ID);
        request.put("iRETAIL_PA_ID", iRETAIL_PA_ID);
        request.put("iDOC_NO", iDOC_NO);
        request.put("iDOC_DATE", iDOC_DATE);
        request.put("iCHEMID", iCHEMID);
        request.put("sCOMPLAINT", sCOMPLAINT);
        request.put("sFILE", FileName);

        //End of call to service

        return customMethodForAllServices(request,"COMPLAINT_COMMIT_1");
    }

    /////////////COMPLAINT_COMMIT End /////////////////////////



    /////////Delete Leave Request////////////

    public String get_Response_DeleteAddLeave(String sCompanyFolder, String LEAVE_ID) {
      //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder", sCompanyFolder);
        request.put("LEAVE_ID", LEAVE_ID);

        //End of call to service

        return customMethodForAllServices(request,"DeleteAddLeave");
    }


    ////////////////////////////////End//////////////////
    ///????????//////////////////ResetTask//////////????????///////////////

    public String getResponse_ResetTask(String sCompanyFolder, String DCRID) {

        //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder", sCompanyFolder);
        request.put("DCRID", DCRID);

        //End of call to service

        return customMethodForAllServices(request,"DcrReset");
    }
    ///????????//////////////////ResetTask End//////////????????///////////////

    //////////////////GetItemListForLocal Start /////////
    public String getResultFrom_GetItemListForLocal(String sCompanyFolder, String iPaId) {
        //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder", sCompanyFolder);
        request.put("iPaId", iPaId);

        //End of call to service

        return customMethodForAllServices(request,"GetItemListForLocal");

    }
//////////************/////////GetDoctorRoute Start /////////////////

    public String getResponseFrom_GetDoctorRoute(String sCompanyFolder, String iPaId, String DCRID) {
        //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder", sCompanyFolder);
        request.put("iPaId", iPaId);
        request.put("DCRID", DCRID);

        //End of call to service

        return customMethodForAllServices(request,"GetDoctorRoute_1");


    }

    //////////************/////////GetDoctorRoute End /////////////////



    public String get_DOB_DOA(String sCompanyFolder, String iPaId,String from,String to) {

        //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder", sCompanyFolder);
        request.put("iPA_ID", iPaId);
        request.put("sFdate", from);
        request.put("sTdate", to);

        //End of call to service

        return customMethodForAllServices(request,"DOB_DOA_List");
    }


    public String get_POPUP_MOBILE(String sCompanyFolder, String iPaId,String from,String to) {

        //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder", sCompanyFolder);
        request.put("iPA_ID", iPaId);
        request.put("sFdate", from);
        request.put("sTdate", to);

        //End of call to service

        return customMethodForAllServices(request,"POPUP_MOBILE");
    }

////////////////////////DOB/DOA End//////////////



    public String getResponse_MAILTODDL(String sCompanyFolder, String iPA_ID) {

        //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder", sCompanyFolder);
        request.put("iPA_ID", iPA_ID);

        //End of call to service

        return customMethodForAllServices(request,"MAILTODDL");
    }
    ///////////////////MAILTODDL End ///////////////


    ////////PopulateleaveDetailByPA_ID Start///////


    public String get_PopulateleaveDetailByPA_ID(String sCompanyFolder, String iPaId) {

        //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder", sCompanyFolder);
        request.put("iPaId", iPaId);

        //End of call to service

        return customMethodForAllServices(request,"PopulateleaveDetailByPA_ID_1");
    }

    ////////PopulateleaveDetailByPA_ID End///////


///////////////////"PopulateleaveDetail  for edit Leave//////////

    public String get_Response_PopulateleaveDetail(String sCompanyFolder, String LEAVE_ID, String PA_ID) {

        //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder", sCompanyFolder);
        request.put("LEAVE_ID", LEAVE_ID);
        request.put("PA_ID", PA_ID);

        //End of call to service

        return customMethodForAllServices(request,"PopulateleaveDetail");
    }


    public String getResponse_DCRAREADDL_ROUTE(String sCompanyFolder, String iPA_ID, String iMR_ID1, String iMR_ID2, String iMR_ID3, String iMR_ID4, String sWORKTYPE, String sDCR_DATE,String sAllYn) {

        //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder", sCompanyFolder);
        request.put("iPA_ID", iPA_ID);
        request.put("iMR_ID1", iMR_ID1);
        request.put("iMR_ID2", iMR_ID2);
        request.put("iMR_ID3", iMR_ID3);
        request.put("iMR_ID4", iMR_ID4);
        request.put("sWORKTYPE", sWORKTYPE);
        request.put("sDCR_DATE", sDCR_DATE);
        request.put("sAllYn", sAllYn);

        //End of call to service

        return customMethodForAllServices(request,"DCRAREADDL_ROUTE_2");
    }


    public String DOCTOR_VISIT(String sCompanyFolder, String iPaId, String sMONTH, String iDRID) {
        final String methodName = "";
        final String SOAP_ACTION = NAMESPACE + methodName;

        //System.setProperty("http.keepAlive", "false");

        SoapObject request = new SoapObject(NAMESPACE, methodName);
        request.addProperty("sCompanyFolder", sCompanyFolder);
        request.addProperty("iPaId", iPaId);
        request.addProperty("sMONTH", sMONTH);
        request.addProperty("iDRID", iDRID);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        try {

            androidHttpTransport.call(SOAP_ACTION, envelope);
            result = (SoapPrimitive) envelope.getResponse();
            Log.v("Result", "Invalid" + result.toString());
            return result.toString();

        } catch (Exception e) {
            Log.v("Invalid Result", "Invalid" + e.toString());
            return "ERROR "+"service  method "+methodName+ " "+e.toString();
        }

       /* request = null;
        envelope = null;
        androidHttpTransport = null;
        return result.toString();*/
    }


    public String getResponse_DOCTOR_VIEW(String sCompanyFolder, String iPaId, String sDCR_DATE,String call_type) {

        //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder", sCompanyFolder);
        request.put("iPaId", iPaId);
        request.put("sDCR_DATE", sDCR_DATE);
        request.put("sCALL_TYPE", call_type);

        //End of call to service

        return customMethodForAllServices(request,"DOCTOR_VIEW_1");
    }

        //////////////////////////NLCRETAILER_VIEW

    public String getResponse_NLCRETAILER_VIEW(String sCompanyFolder, String iPaId, String sDCR_DATE) {

        //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder", sCompanyFolder);
        request.put("iPaId", iPaId);
        request.put("sDCR_DATE", sDCR_DATE);

        //End of call to service

        return customMethodForAllServices(request,"NLCRETAILER_VIEW");
    }


    ///////////////////////

    //////////////////////////NLCDISTRIBUTOR_VIEW

    public String getResponse_NLCDISTRIBUTOR_VIEW(String sCompanyFolder, String iPaId, String sDCR_DATE) {

        //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder", sCompanyFolder);
        request.put("iPaId", iPaId);
        request.put("sDCR_DATE", sDCR_DATE);

        //End of call to service

        return customMethodForAllServices(request,"NLCDISTRIBUTOR_VIEW");
    }
//////////////////////

    public String getResponse_CHEMIST_VIEW(String sCompanyFolder, String iPaId, String sDCR_DATE) {

          //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder", sCompanyFolder);
        request.put("iPaId", iPaId);
        request.put("sDCR_DATE", sDCR_DATE);

        //End of call to service

        return customMethodForAllServices(request,"CHEMIST_VIEW");
    }

    public String getResponse_STOCKIST_VIEW(String sCompanyFolder, String iPaId, String sDCR_DATE) {

        //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder", sCompanyFolder);
        request.put("iPaId", iPaId);
        request.put("sDCR_DATE", sDCR_DATE);

        //End of call to service

        return customMethodForAllServices(request,"STOCKIST_VIEW");
    }

    public String getResponse_NLC_VIEW(String sCompanyFolder, String iPaId, String sDCR_DATE) {

        //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder", sCompanyFolder);
        request.put("iPaId", iPaId);
        request.put("sDcrDate", sDCR_DATE);

        //End of call to service

        return customMethodForAllServices(request,"NLCVIEW_MOBILE");
    }

    public String getResponse_TP_VIEW(String sCompanyFolder, String iPaId, String sMONTH) {

        //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder", sCompanyFolder);
        request.put("iPaId", iPaId);
        request.put("sMONTH", sMONTH);

        //End of call to service

        return customMethodForAllServices(request,"TP_VIEW");


    }
    //////////////////////////////





    public String getResponse_WORKWITH(String sCompanyFolder, String iPA_ID, String sDCR_DATE, String sAREA1,
                                       String sAREA2, String sAREA3, String sAREA4, String sAREA5, String sAREA6,
                                       String sDIVERTWWYN,String sWorking_Type) {
       //Start of call to service

               HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder", sCompanyFolder);
        request.put("iPA_ID", iPA_ID);
        request.put("sDCR_DATE", sDCR_DATE);
        request.put("sAREA1", sAREA1);
        request.put("sAREA2", sAREA2);
        request.put("sAREA3", sAREA3);
        request.put("sAREA4", sAREA4);
        request.put("sAREA5", sAREA5);
        request.put("sAREA6", sAREA6);
        request.put("iDIVERTWWYN", sDIVERTWWYN);
        request.put("sWorking_Type", sWorking_Type);


        //End of call to service

        return customMethodForAllServices(request,"WORKWITH_2");
    }

    public String getResponse_FMCGDDL(String sCompanyFolder,String PA_ID) {
        //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder", sCompanyFolder);
        request.put("iPaId", PA_ID);

        //End of call to service

        return customMethodForAllServices(request,"FMCGDDL_2");
    }

    public String getResponse_GETCHEMISTLIST_MOBILE(String sCompanyFolder, String sDcrId, String iPA_ID) {
        //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder", sCompanyFolder);
        request.put("sDcrId", sDcrId);
        request.put("iPA_ID", iPA_ID);

        //End of call to service

        return customMethodForAllServices(request,"GETCHEMISTLIST_MOBILE");
    }

    public String getResponse_LeaveRequestGrid(String sCompanyFolder, String iLEAVE_ID, String iPaId) {
        //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder", sCompanyFolder);
        request.put("iLEAVE_ID", iLEAVE_ID);
        request.put("iPaId", iPaId);

        //End of call to service

        return customMethodForAllServices(request,"LeaveRequestGrid");
    }

    public String getResponse_GETDOCTORLIST(String sCompanyFolder, String iPA_ID, String sDCR_ID) {
        //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder", sCompanyFolder);
        request.put("iPA_ID", iPA_ID);
        request.put("iDCRID", sDCR_ID);

        //End of call to service

        return customMethodForAllServices(request,"GETDOCTORLIST_1");
    }

    public String getResponse_LeaveAddCheckDuplicateInDCR(String sCompanyFolder, String iPaId, String sFdate, String sTdate) {
        //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder", sCompanyFolder);
        request.put("iPaId", iPaId);
        request.put("sFdate", sFdate);
        request.put("sTdate", sTdate);

        //End of call to service

        return customMethodForAllServices(request,"LeaveAddCheckDuplicateInDCR");
    }

    public String getResponse_LeaveDuplicate(String sCompanyFolder, String iPaId, String sFdate, String sTdate) {
        //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder", sCompanyFolder);
        request.put("iPaId", iPaId);
        request.put("sFdate", sFdate);
        request.put("sTdate", sTdate);

        //End of call to service

        return customMethodForAllServices(request,"LeaveDuplicate");
    }


    public String getResponse_CommitAddLeave_1(String sCompanyFolder, String PA_ID, String PURPOSE, String DOC_NO, String NO_DAY,
                                               String FDATE, String TDATE, String PHONE, String ID, String sDocDate, String sAFile, String sHALF_DATE, String sLeaveHeadId_String,
                                               String sID_String, String sBalQty_String, String sReqQty_String) {
       //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder", sCompanyFolder);
        request.put("PA_ID", PA_ID);
        request.put("PURPOSE", PURPOSE);
        request.put("DOC_NO", DOC_NO);
        request.put("NO_DAY", NO_DAY);
        request.put("FDATE", FDATE);
        request.put("TDATE", TDATE);
        request.put("PHONE", PHONE);
        request.put("ID", ID);
        request.put("sDocDate", sDocDate);
        request.put("sAFile", sAFile);

        request.put("sHALF_DATE", sHALF_DATE);
        request.put("sLeaveHeadId_String", sLeaveHeadId_String);
        request.put("sID_String", sID_String);
        request.put("sBalQty_String", sBalQty_String);
        request.put("sReqQty_String", sReqQty_String);

        //End of call to service

        return customMethodForAllServices(request,"CommitAddLeave_1");
    }


    public String TEAMALL(String sCompanyFolder, String iPaId, String iPRESENT) {
        //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder", sCompanyFolder);
        request.put("iPaId", iPaId);
        request.put("iPRESENT", iPRESENT);

        //End of call to service

        return customMethodForAllServices(request,"TEAMALL");
    }



    public String DCRLISTWITHEXPENSEGRID(String sCompanyFolder, String sPaId, String sMonth, String sVerify, String sLoginPaId, String sEntryDate) {
       //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder", sCompanyFolder);
        request.put("sPaId", sPaId);
        request.put("sMonth", sMonth);
        request.put("sVerify", sVerify);
        request.put("sLoginPaId", sLoginPaId);
        request.put("sEntryDate", sEntryDate);

        //End of call to service

        return customMethodForAllServices(request,"DCRLISTWITHEXPENSEGRID_1");


    }

//////////////////getDcr Start/////////


    public String getResponse_getDcr(String sCompanyFolder, String iDcrId) {
        //Start of call to service

       HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder",sCompanyFolder);
        request.put("iDcrId",iDcrId);

        //End of call to service

        return customMethodForAllServices(request,"GETDCR");
    }

    //////////////////getDcr End/////////
    //////////////////getDcr Start/////////

    public String getResponse_DCRUNLOGGED_MOBILE(String sCompanyFolder, String iPaId, String sDATE) {
        //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder", sCompanyFolder);
        request.put("iPaId", iPaId);
        request.put("sDATE", sDATE);

        //End of call to service

        return customMethodForAllServices(request,"DCRUNLOGGED_MOBILE");

    }

    //////////////////getDcr End/////////

//////////////////////////////////////


    public String getResponse_DCRLOGGED_MOBILE(String sCompanyFolder, String iPaId, String sDATE) {
        //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder", sCompanyFolder);
        request.put("iPaId", iPaId);
        request.put("sDATE", sDATE);

        //End of call to service

        return customMethodForAllServices(request,"DCRLOGGED_MOBILE");


    }




    public String getResponse_SpoCNFGrid(String sCompanyFolder, String iPA_ID, String sFDATE, String sTDATE, String sType, String iCompanyId, String iHqId) {

        //Start of call to service

        HashMap<String,String> request=new HashMap<>();


        request.put("sCompanyFolder", sCompanyFolder);
        request.put("iPA_ID", iPA_ID);
        request.put("sFDATE", sFDATE);
        request.put("sTDATE", sTDATE);
        request.put("sType", sType);
        request.put("iCompanyId", iCompanyId);
        request.put("iHqId", iHqId);

        //End of call to service

        return customMethodForAllServices(request,"SpoCNFGrid");

    }

    public String getResponse_SPOCNFViewGrid(String sCompanyFolder, String sCompanyName, String sFdate, String sTdate,
                                             String iLoginPaId, String iType) {
        //Start of call to service

        HashMap<String,String> request=new HashMap<>();

        request.put("sCompanyFolder", sCompanyFolder);
        request.put("sCompanyName", sCompanyName);
        request.put("sFdate", sFdate);
        request.put("sTdate", sTdate);
        request.put("iLoginPaId", iLoginPaId);
        request.put("iType", iType);

        //End of call to service

        return customMethodForAllServices(request,"SPOCNFViewGrid");


    }


    ///////////////////////////
    //////////////////Farmar Visit//////////////////
    public String getResponse_DcrFarmerCommit(String sCompanyFolder, String PA_ID, String DCR_ID,
                                              String MONTH, String OWN_NAMEMMC, String MOBILE, String FARMER_NAME,
                                              String MEETING_PALCE, String PRODUCT_DETAILS, String IHSTAFF_ATTENDMMC,
                                              String DIRECT_SALESFARMER, String ORDER_BOOKEDMMC, String ID, String REMARK,
                                              String FILE_NAME, String LATLONG, String LOC,String sCALL_TYPE,
                                              String iEXPENCE_AMT,String iTOTAL_SAMPLE_QTY,String iPOSSITIVE_SAMPLE_QTY,
                                              String iNEGATIVE_SAMPLE_QTY,String iITEM_ID,
                                              String iSQTY,String iPOB_QTY,String sATTACH_1,
                                              String sATTACH_2,String iDR_GROUP_ID) {

        //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder", sCompanyFolder);
        request.put("PA_ID", PA_ID);
        request.put("DCR_ID", DCR_ID);
        request.put("MONTH", MONTH);
        request.put("OWN_NAMEMMC", OWN_NAMEMMC);
        request.put("MOBILE", MOBILE);
        request.put("FARMER_NAME", FARMER_NAME);
        request.put("MEETING_PALCE", MEETING_PALCE);
        request.put("PRODUCT_DETAILS", PRODUCT_DETAILS);

        request.put("IHSTAFF_ATTENDMMC", IHSTAFF_ATTENDMMC);
        request.put("DIRECT_SALESFARMER", DIRECT_SALESFARMER);
        request.put("ORDER_BOOKEDMMC", ORDER_BOOKEDMMC);
        request.put("ID", ID);
        request.put("REMARK", REMARK);
        request.put("FILE_NAME", FILE_NAME);
        request.put("LATLONG", LATLONG);
        request.put("LOC", LOC);

        request.put("sCALL_TYPE", sCALL_TYPE);
        request.put("iEXPENCE_AMT", iEXPENCE_AMT);
        request.put("iTOTAL_SAMPLE_QTY", iTOTAL_SAMPLE_QTY);
        request.put("iPOSSITIVE_SAMPLE_QTY", iPOSSITIVE_SAMPLE_QTY);
        request.put("iNEGATIVE_SAMPLE_QTY", iNEGATIVE_SAMPLE_QTY);

        request.put("iITEM_ID", iITEM_ID);
        request.put("iSQTY", iSQTY);
        request.put("iPOB_QTY", iPOB_QTY);
        request.put("sATTACH_1", sATTACH_1);
        request.put("sATTACH_2", sATTACH_2);
        request.put("iDR_GROUP_ID", iDR_GROUP_ID);

        //End of call to service

        return customMethodForAllServices(request,"DcrFarmerCommit_3");

    }
    //////////////////////////////

    //////////////////////MobileImageCommit///////////
    public String getResponseMobileImageCommit(String sCompanyFolder, String ID, String PA_ID, String sDATE, String sFILENAME, String sLATLONG, String sLOC) {
       //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder", sCompanyFolder);
        request.put("ID", ID);
        request.put("PA_ID", PA_ID);
        request.put("sDATE", sDATE);
        request.put("sFILENAME", sFILENAME);

        request.put("sLATLONG", sLATLONG);
        request.put("sLOC", sLOC);

        //End of call to service

        return customMethodForAllServices(request,"MobileImageCommit");

    }
    ///////////======////////////////

    ///////////////=====================////////////////////

    //might be not in use

    public String getResponseMapDrCallMobile(String sCompanyFolder, String iPA_ID, String sDate, String iDivisionId) {

        final String METHOD_NAME = "MapDrCallMobile";
        final String SOAP_ACTION = NAMESPACE + METHOD_NAME;

        //System.setProperty("http.keepAlive", "false");

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("sCompanyFolder", sCompanyFolder);
        request.addProperty("iPA_ID", iPA_ID);
        request.addProperty("sDate", sDate);
        request.addProperty("iDivisionId", iDivisionId);

        SoapSerializationEnvelope soapSerializationEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        soapSerializationEnvelope.dotNet = true;
        soapSerializationEnvelope.setOutputSoapObject(request);
        HttpTransportSE httpTransportSE = new HttpTransportSE(URL);

        try {
            httpTransportSE.call(SOAP_ACTION, soapSerializationEnvelope);
            result = (SoapPrimitive) soapSerializationEnvelope.getResponse();
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR "+"service  method "+METHOD_NAME+ " "+e.toString();
        }
        /*return result.toString();*/
    }
    ////////*******************///////////////////////





    ///////////////DCRAPPRAISAL_COMMIT////////////////////////


    public String getResponseDCRAPPRAISAL_COMMIT(String sCompanyFolder, String PA_ID, String DCR_ID, String sAPPRAISAL_ID_STR,
                                                 String sGRADE_STR, String sOBSERVATION, String sACTION_TAKEN) {
        //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder", sCompanyFolder);
        request.put("PA_ID", PA_ID);
        request.put("DCR_ID", DCR_ID);
        request.put("sAPPRAISAL_ID_STR", sAPPRAISAL_ID_STR);
        request.put("sGRADE_STR", sGRADE_STR);
        request.put("sOBSERVATION", sOBSERVATION);
        request.put("sACTION_TAKEN", sACTION_TAKEN);

        //End of call to service

        return customMethodForAllServices(request,"DCRAppraisal_Commit_Mobile");
    }


    ///////////////////////////////========= Custom Method For all Web Services =========///////////////
    public String customMethodForAllServices(SoapObject soapObject, String methodName) {

        SoapSerializationEnvelope soapSerializationEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        final String SOAP_ACTION = NAMESPACE + methodName;

        //System.setProperty("http.keepAlive", "false");

        soapSerializationEnvelope.dotNet = true;
        soapSerializationEnvelope.setOutputSoapObject(soapObject);
        HttpTransportSE httpTransportSE = new HttpTransportSE(URL);


        try {
            httpTransportSE.call(SOAP_ACTION, soapSerializationEnvelope);
            result = (SoapPrimitive) soapSerializationEnvelope.getResponse();

        } catch (Exception e) {

            //return "ERROR "+"service  method "+METHOD_NAME+ " "+e.toString();
        }


        return result.toString();
    }

    ///////////////////////////////========= Custom Method For all AsyncTask Web Services =========///////////////

    private String customMethodForAllServices(final HashMap<String, String> data, final String methodName) {
        final String ACITON_NAME = NAMESPACE + methodName;
        //SoapPrimitive result=null;
        String reply="[ERROR]";


        //HTTP POST METHOD-------------------------------

        for (int service_try = 0; service_try < 2; service_try++) {
            try {
                java.net.URL url = new URL(URL+"/"+methodName);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("USER-AGENT","Mozilla/5.0");
                connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
                connection.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
                connection.setDoOutput(true);

                StringBuilder urlParameters = new StringBuilder();
                for (String key : data.keySet()) {
                    String values = data.get(key) == null? "" : data.get(key);
                    urlParameters.append(key).append("=").append(values.replace("&","")).append("&");
                }
                String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";
                String params = Uri.encode(urlParameters.toString(), ALLOWED_URI_CHARS);
                //String params= URLEncoder.encode(urlParameters.toString(), "UTF-8");
                if (params.length() > 1) {
                    params = params.substring(0, params.length() - 1);
                }
                int no_of_try = 0;
                try {
                    for ( no_of_try = 1; no_of_try <= 3; no_of_try++) {
                        service_try = 1;
                        DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
                        dStream.writeBytes(params); //Writes out the string to the underlying output stream as a sequence of bytes
                        dStream.flush(); // Flushes the data output stream.
                        dStream.close(); // Closing the output stream.

                        int responseCode = connection.getResponseCode(); // getting the response code
                               /* final StringBuilder output = new StringBuilder("Request URL " + url);
                                output.append(System.getProperty("line.separatoR") + "Request Parameters " + params);
                                output.append(System.getProperty("line.separator")  + "Response Code " + responseCode);*/
                        StringBuilder responseOutput = new StringBuilder();
                        if (responseCode == 200) {
                            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));


                            String line = "";

                            System.out.println("output===============" + br);
                            while ((line = br.readLine()) != null) {
                                responseOutput.append(line);
                            }
                            br.close();

                            String result =   responseOutput.toString();
                            result = result.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?><string xmlns=\"http://tempuri.org/\">", "").replace("</string>", "").replace("&amp;", "&").replace("&lt;", "<").replace("&gt;", ">").replace("&quote;", "\"");

                            return threadMsg(result, methodName);
                        } else {
                            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));


                            String line = "";

                            System.out.println("output===============" + br);
                            while ((line = br.readLine()) != null) {
                                responseOutput.append(line);
                            }
                            br.close();

                            //responseOutput.append(connection.getResponseMessage());
                            return threadMsg("[ERROR] " + methodName + " " + responseOutput.toString(), methodName);
                        }
                        // output.append(System.getProperty("line.separator") + "Response " + System.getProperty("");

                        //break;
                    }
                }catch(SocketTimeoutException e1){
                    if (no_of_try==3) {
                        return threadMsg("[ERROR] " + "service  method " + methodName + " " + e1.toString(),  methodName);
                    }else {
                        try {
                            if (customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "WEBSERVICE_URL_ALTERNATE", "").equals("")) {
                                URL= customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"WEBSERVICE_URL_ALTERNATE",URL);
                                url = new URL(URL + "/" + methodName);
                                connection = (HttpURLConnection) url.openConnection();
                                connection.setRequestMethod("POST");
                                connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                                connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
                                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                                connection.setDoOutput(true);
                            }

                        }
                        catch(ProtocolException e){
                            // PROTOCOL EXCEPTION
                            e.printStackTrace();
                            return threadMsg("[ERROR] " + "service  method " + methodName + " " + e.toString(), methodName);

                            //no_of_try = 4;

                        } catch(MalformedURLException e){
                            //URL CONVERT Exception
                            e.printStackTrace();
                            return threadMsg("[ERROR] " + "service  method " + methodName + " " + e.toString(), methodName);
                            //no_of_try = 4;
                        } catch(IOException e){
                            //OPEN EXCEPTION
                            e.printStackTrace();
                            return threadMsg("[ERROR] " + "service  method " + methodName + " " + e.toString(), methodName);
                            //no_of_try = 4;
                        }
                    }
                }
            } catch (ProtocolException e) {
                // PROTOCOL EXCEPTION
                if (service_try == 1) {
                    return threadMsg("[ERROR] " + "service  method " + methodName + " " + e.toString(), methodName);
                } else if (!customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "WEBSERVICE_URL_ALTERNATE", "").equals("")) {

                    URL = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "WEBSERVICE_URL_ALTERNATE", URL);
                }


                e.printStackTrace();
            } catch (MalformedURLException e) {
                //URL CONVERT Exception
                if (service_try == 1) {
                    return threadMsg("[ERROR] " + "service  method " + methodName + " " + e.toString(), methodName);
                } else if (!customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "WEBSERVICE_URL_ALTERNATE", "").equals("")) {

                    URL = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "WEBSERVICE_URL_ALTERNATE", URL);
                }
                e.printStackTrace();

            } catch (IOException e) {
                //OPEN EXCEPTION
                if (service_try == 1) {
                    return threadMsg("[ERROR] " + "service  method " + methodName + " " + e.toString(), methodName);
                } else if (!customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "WEBSERVICE_URL_ALTERNATE", "").equals("")) {

                    URL = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "WEBSERVICE_URL_ALTERNATE", URL);
                }
                e.printStackTrace();

            }
        }
        return reply;
    }



    private String threadMsg(final String result,final String methodName) {
        if(result!=null && !result.toUpperCase().contains("[ERROR]")) {
            //return "ERROR";
            return result;
        }else{
            Log.d("MYAPP", "error are: " + result);
            assert result != null;
            final String title;
            final String message;
            if(result.contains("service  method")) {
                if (result.contains("ECONNRESET") || result.contains("UnknownHostException")){
                    title="Server Error";
                    message=context.getResources().getString(R.string.try_later);
                }else {
                    title = "Internet Error";
                    message = context.getResources().getString(R.string.Internet_error);
                }
            }else{
                title="Server Error";
                message=context.getResources().getString(R.string.try_later);
            }
            Handler handler = new Handler(Looper.getMainLooper());

            handler.post(new Runnable() {

                @Override
                public void run() {
                    getAlert(context, title, message);
                    String subject="";
                    if (result.length()>22  && result.length()>=80){
                        subject=result.substring(22,80);
                    }else if (result.length()>22){
                        subject=result.substring(22);
                    }else{
                        subject=result;
                    }
                    List toEmailList = Arrays.asList("mobilereporting@cboinfotech.com".split("\\s*,\\s*"));
                    new SendMailTask().execute("mobilereporting@cboinfotech.com",
                            "mreporting",toEmailList , Custom_Variables_And_Method.COMPANY_CODE+": "+subject,context.getResources().getString(R.string.app_name)+"\n Company Code :"+Custom_Variables_And_Method.COMPANY_CODE+"\n DCR ID :"+Custom_Variables_And_Method.DCR_ID+"\n PA ID : "+Custom_Variables_And_Method.PA_ID+"\n App version : "+Custom_Variables_And_Method.VERSION+"\n methodName : "+methodName+"\n Error Alert :"+title+"\n"+ result);
                }
            });

            return "ERROR";
        }
    }


    public static void getAlert(Context context, String title, String messege) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogLayout = inflater.inflate(R.layout.alert_view, null);
        final TextView Alert_title= (TextView) dialogLayout.findViewById(R.id.title);
        final TextView Alert_message= (TextView) dialogLayout.findViewById(R.id.message);
        final Button Alert_Positive= (Button) dialogLayout.findViewById(R.id.positive);
        Alert_title.setText(title);
        Alert_message.setText(messege);

        final TextView pa_id_txt= (TextView) dialogLayout.findViewById(R.id.PA_ID);
        pa_id_txt.setText(""+Custom_Variables_And_Method.PA_ID);

        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);


        final AlertDialog dialog = builder1.create();

        dialog.setView(dialogLayout);
        Alert_Positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }

    /////////////////////////////&&&&&&&&& DCR_COMMITFINAL End &&&&&&&&&&&&////////////////

    public String getSoapResponse_DCRCommitFinal_New
            (String sCompanyFolder, String iDcrId, String iNoChemist, String iNoStockist, String sChemistRemark,
             String sStockistREmark, String iPob, String iPobQty, String iActualFareAmt,
             String sDatype, String iDistanceId, String sRemark, String sLoc, String iOuttime, String sRouteYn,
             String sDCRLATCOMMIT_ID, String sDCRLATCOMMIT_IN_TIME, String sDCRLATCOMMIT_LOC_LAT, String sDCRLATCOMMIT_LOC,
             String sDCRLATCOMMIT_KM, String sDCRITEM_DR_ID, String sDCRITEM_ITEMIDIN, String sDCRITEM_ITEM_ID_ARR, String
                     sDCRITEM_QTY_ARR, String sDCRITEM_ITEM_ID_GIFT_ARR, String sDCRITEM_QTY_GIFT_ARR, String sDCRITEM_POB_QTY, String
                     sDCRITEM_POB_VALUE, String sDCRITEM_VISUAL_ARR,String sDCRITEM_NOC_ARR, String sDCRDR_DR_ID, String sDCRDR_WW1, String sDCRDR_WW2, String
                     sDCRDR_WW3, String sDCRDR_LOC, String sDCRDR_IN_TIME, String sDCRDR_BATTERY_PERCENT, String sDCRDR_REMARK,
             String sDCRDR_KM,String sDCRDR_SRNO,String sDCRDR_FILE,String sDCRDR_CALLTYPE, String sDCRCHEM_CHEM_ID,
             String sDCRCHEM_POB_QTY, String sDCRCHEM_POB_AMT, String sDCRCHEM_ITEM_ID_ARR, String sDCRCHEM_QTY_ARR, String
                     sDCRCHEM_LOC, String sDCRCHEM_IN_TIME, String sDCRCHEM_SQTY_ARR, String sDCRCHEM_ITEM_ID_GIFT_ARR, String
                     sDCRCHEM_QTY_GIFT_ARR, String sDCRCHEM_BATTERY_PERCENT,
             String sDCRCHEM_KM,String sDCRCHEM_SRNO,String sDCRCHEM_REMARK,String sDCRCHEM_FILE, String sDCRSTK_STK_ID, String sDCRSTK_POB_QTY, String sDCRSTK_POB_AMT, String
                     sDCRSTK_ITEM_ID_ARR, String sDCRSTK_QTY_ARR, String sDCRSTK_LOC, String sDCRSTK_IN_TIME, String
                     sDCRSTK_SQTY_ARR, String sDCRSTK_ITEM_ID_GIFT_ARR, String sDCRSTK_QTY_GIFT_ARR, String sDCRSTK_BATTERY_PERCENT,
             String sDCRSTK_KM,String sDCRSTK_SRNO, String sDCRSTK_REMARK,String sDCRSTK_FILE,String sDCRRC_DR_ID, String
                     sDCRRC_LOC, String sDCRRC_IN_TIME, String sDCRRC_KM,String sDCRRC_SRNO,String sDCRRC_BATTERY_PERCENT,String sDCRRC_REMARK,String sDCRRC_FILE, String sDCRRX_DR_ARR,
             String sDCRRX_ITEMID_ARR, String iFinalKM,String sAPI_Pattern,String sBATTERY_PERCENT,String sGCMToken,
            String DCS_ID_ARR, String LAT_LONG_ARR,String DCS_TYPE_ARR,String DCS_ADD_ARR,String DCS_INDES_ARR) {



        final String methodName = "DCRCommitFinal_New_14";
        final String SOAP_ACTION = NAMESPACE + methodName;
        SoapObject request = new SoapObject(NAMESPACE, methodName);

        //System.setProperty("http.keepAlive", "false");


        request.addProperty("sCompanyFolder", sCompanyFolder);
        request.addProperty("iDcrId", iDcrId);
        request.addProperty("iNoChemist", iNoChemist);
        request.addProperty("iNoStockist", iNoStockist);
        request.addProperty("sChemistRemark", sChemistRemark);
        request.addProperty("sStockistREmark", sStockistREmark);
        request.addProperty("iPob", iPob);
        request.addProperty("iPobQty", iPobQty);
        request.addProperty("iActualFareAmt", iActualFareAmt);
        request.addProperty("sDatype", sDatype);
        request.addProperty("iDistanceId", iDistanceId);
        request.addProperty("sRemark", sRemark);
        request.addProperty("sLoc", sLoc);
        request.addProperty("iOuttime", iOuttime);

        request.addProperty("sRouteYn", sRouteYn);
        request.addProperty("sDCRLATCOMMIT_ID", sDCRLATCOMMIT_ID);
        request.addProperty("sDCRLATCOMMIT_IN_TIME", sDCRLATCOMMIT_IN_TIME);
        request.addProperty("sDCRLATCOMMIT_LOC_LAT", sDCRLATCOMMIT_LOC_LAT);
        request.addProperty("sDCRLATCOMMIT_LOC", sDCRLATCOMMIT_LOC);
        request.addProperty("sDCRLATCOMMIT_KM", sDCRLATCOMMIT_KM);
        request.addProperty("sDCRITEM_DR_ID", sDCRITEM_DR_ID);
        request.addProperty("sDCRITEM_ITEMIDIN", sDCRITEM_ITEMIDIN);


        request.addProperty("sDCRITEM_ITEM_ID_ARR", sDCRITEM_ITEM_ID_ARR);
        request.addProperty("sDCRITEM_QTY_ARR", sDCRITEM_QTY_ARR);
        request.addProperty("sDCRITEM_ITEM_ID_GIFT_ARR", sDCRITEM_ITEM_ID_GIFT_ARR);
        request.addProperty("sDCRITEM_QTY_GIFT_ARR", sDCRITEM_QTY_GIFT_ARR);

        request.addProperty("sDCRITEM_POB_QTY", sDCRITEM_POB_QTY);
        request.addProperty("sDCRITEM_POB_VALUE", sDCRITEM_POB_VALUE);
        request.addProperty("sDCRITEM_VISUAL_ARR", sDCRITEM_VISUAL_ARR);
        request.addProperty("sDCRITEM_NOC_ARR", sDCRITEM_NOC_ARR);

        request.addProperty("sDCRDR_DR_ID", sDCRDR_DR_ID);
        request.addProperty("sDCRDR_WW1", sDCRDR_WW1);
        request.addProperty("sDCRDR_WW2", sDCRDR_WW2);
        request.addProperty("sDCRDR_WW3", sDCRDR_WW3);
        request.addProperty("sDCRDR_LOC", sDCRDR_LOC);
        request.addProperty("sDCRDR_IN_TIME", sDCRDR_IN_TIME);
        request.addProperty("sDCRDR_BATTERY_PERCENT", sDCRDR_BATTERY_PERCENT);
        request.addProperty("sDCRDR_REMARK", sDCRDR_REMARK);
        request.addProperty("sDCRDR_KM", sDCRDR_KM);
        request.addProperty("sDCRDR_SRNO", sDCRDR_SRNO);
        request.addProperty("sDCRDR_FILE", sDCRDR_FILE);
        request.addProperty("sDCRDR_CALLTYPE", sDCRDR_CALLTYPE);

        request.addProperty("sDCRCHEM_CHEM_ID", sDCRCHEM_CHEM_ID);
        request.addProperty("sDCRCHEM_POB_QTY", sDCRCHEM_POB_QTY);
        request.addProperty("sDCRCHEM_POB_AMT", sDCRCHEM_POB_AMT);
        request.addProperty("sDCRCHEM_ITEM_ID_ARR", sDCRCHEM_ITEM_ID_ARR);
        request.addProperty("sDCRCHEM_QTY_ARR", sDCRCHEM_QTY_ARR);
        request.addProperty("sDCRCHEM_LOC", sDCRCHEM_LOC);
        request.addProperty("sDCRCHEM_IN_TIME", sDCRCHEM_IN_TIME);
        request.addProperty("sDCRCHEM_SQTY_ARR", sDCRCHEM_SQTY_ARR);
        request.addProperty("sDCRCHEM_ITEM_ID_GIFT_ARR", sDCRCHEM_ITEM_ID_GIFT_ARR);
        request.addProperty("sDCRCHEM_QTY_GIFT_ARR", sDCRCHEM_QTY_GIFT_ARR);
        request.addProperty("sDCRCHEM_BATTERY_PERCENT", sDCRCHEM_BATTERY_PERCENT);
        request.addProperty("sDCRCHEM_KM", sDCRCHEM_KM);
        request.addProperty("sDCRCHEM_SRNO", sDCRCHEM_SRNO);
        request.addProperty("sDCRCHEM_REMARK", sDCRCHEM_REMARK);
        request.addProperty("sDCRCHEM_FILE", sDCRCHEM_FILE);


        request.addProperty("sDCRSTK_STK_ID", sDCRSTK_STK_ID);
        request.addProperty("sDCRSTK_POB_QTY", sDCRSTK_POB_QTY);
        request.addProperty("sDCRSTK_POB_AMT", sDCRSTK_POB_AMT);
        request.addProperty("sDCRSTK_ITEM_ID_ARR", sDCRSTK_ITEM_ID_ARR);
        request.addProperty("sDCRSTK_QTY_ARR", sDCRSTK_QTY_ARR);
        request.addProperty("sDCRSTK_LOC", sDCRSTK_LOC);
        request.addProperty("sDCRSTK_IN_TIME", sDCRSTK_IN_TIME);
        request.addProperty("sDCRSTK_SQTY_ARR", sDCRSTK_SQTY_ARR);
        request.addProperty("sDCRSTK_ITEM_ID_GIFT_ARR", sDCRSTK_ITEM_ID_GIFT_ARR);
        request.addProperty("sDCRSTK_QTY_GIFT_ARR", sDCRSTK_QTY_GIFT_ARR);
        request.addProperty("sDCRSTK_BATTERY_PERCENT", sDCRSTK_BATTERY_PERCENT);
        request.addProperty("sDCRSTK_KM", sDCRSTK_KM);
        request.addProperty("sDCRSTK_SRNO", sDCRSTK_SRNO);
        request.addProperty("sDCRSTK_REMARK", sDCRSTK_REMARK);
        request.addProperty("sDCRSTK_FILE", sDCRSTK_FILE);

        request.addProperty("sDCRRC_DR_ID", sDCRRC_DR_ID);
        request.addProperty("sDCRRC_LOC", sDCRRC_LOC);
        request.addProperty("sDCRRC_IN_TIME", sDCRRC_IN_TIME);
        request.addProperty("sDCRRC_KM", sDCRRC_KM);
        request.addProperty("sDCRRC_SRNO", sDCRRC_SRNO);
        request.addProperty("sDCRRC_BATTERY_PERCENT", sDCRRC_BATTERY_PERCENT);
        request.addProperty("sDCRRC_REMARK", sDCRRC_REMARK);
        request.addProperty("sDCRRC_FILE", sDCRRC_FILE);

        request.addProperty("sDCRRX_DR_ARR", sDCRRX_DR_ARR);
        request.addProperty("sDCRRX_ITEMID_ARR", sDCRRX_ITEMID_ARR);

        request.addProperty("iFinalKM", iFinalKM);
        request.addProperty("iPaId",  Custom_Variables_And_Method.PA_ID);

        request.addProperty("sGCM_TOKEN", sGCMToken);
        request.addProperty("sAPI_PATTERN", sAPI_Pattern);
        request.addProperty("sBATTERY_PERCENT", sBATTERY_PERCENT);

        request.addProperty("REG_ID_ARR", DCS_ID_ARR);
        request.addProperty("REG_LAT_LONG_ARR", LAT_LONG_ARR);
        request.addProperty("REG_TYPE_ARR", DCS_TYPE_ARR);
        request.addProperty("REG_ADD_ARR", DCS_ADD_ARR);
        request.addProperty("REG_INDES_ARR", DCS_INDES_ARR);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        /*String URL="http://delcure.in/mobilerpt.asmx";*/

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL,40000);
        try {

            androidHttpTransport.call(SOAP_ACTION, envelope);

            result = (SoapPrimitive) envelope.getResponse();
            return result.toString();
        } catch (Exception e) {
            return "[ERROR] "+"service  method "+methodName+ " "+e.toString();
        }

        /*request = null;
        envelope = null;
        androidHttpTransport = null;*/

    }

}
