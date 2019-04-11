package async;

import android.content.Context;
import android.util.Log;

import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import utils_new.Custom_Variables_And_Method;

/**
 * Created by Akshit on 1/4/2016.
 */
public class CBOFinalTask_New {



        private Context mContext;
        private CBO_DB_Helper cbohelp;
        Custom_Variables_And_Method customVariablesAndMethod;




        public CBOFinalTask_New(Context context){
            this.mContext=context;
            cbohelp=new CBO_DB_Helper(context);
            customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
        }


        public Map<String,String> dcr_doctorSave(String updated)
        {
            Map<String,String> map_DcrDr_Save = new HashMap<String,String>();
            StringBuilder sb_sDCRDR_DR_ID = new StringBuilder();
            StringBuilder sb_sDCRDR_WW1 = new StringBuilder();
            StringBuilder sb_sDCRDR_WW2 = new StringBuilder();
            StringBuilder sb_sDCRDR_WW3 = new StringBuilder();
            StringBuilder sb_sDCRDR_LOC = new StringBuilder();
            StringBuilder sb_sDCRDR_IN_TIME = new StringBuilder();
            StringBuilder sb_sDCRDR_BATTERY_PERCENT = new StringBuilder();
            StringBuilder sb_sDCRDR_Remark = new StringBuilder();
            StringBuilder sb_sDCRDR_KM = new StringBuilder();
            StringBuilder sb_sDCRDR_SRNO = new StringBuilder();
            StringBuilder sb_sDCRDR_FILE = new StringBuilder();
            StringBuilder sb_sDCRDR_CALLTYPE = new StringBuilder();
            StringBuilder sb_sDR_REF_LAT_LONG = new StringBuilder();

            cbohelp=new CBO_DB_Helper(mContext);
            //ArrayList<String> mydrList=cbohelp.getDoctor();
            ArrayList<String> mydrList=cbohelp.tempDrListForFinalSubmit(updated);
            String saprator ="^";
            String saprator_1 ="|";



            if(mydrList.size()>0) {
                for (int i = 0; i < mydrList.size(); i++) {
                    try {
                        String dr_latLong, dr_Address,dr_locExtra;
                        dr_latLong = cbohelp.getDrCall_latLong(mydrList.get(i));
                        dr_Address = cbohelp.getDrCall_Location(mydrList.get(i));
                        dr_locExtra=cbohelp.getDrCall_LocExtra(mydrList.get(i));
                        cbohelp.updateDr_LocExtra(dr_latLong +"@"+dr_locExtra+ "!^" + dr_Address, mydrList.get(i));
                        if (updated != null) {

                            try {

                                if ((dr_Address.equals(""))) {
                                    dr_Address = customVariablesAndMethod.getAddressByLatLong(mContext, dr_latLong);
                                    cbohelp.updateDrCall_Address(dr_Address, mydrList.get(i));
                                    cbohelp.updateDr_LocExtra(dr_latLong +"@"+dr_locExtra+ "!^" + dr_Address, mydrList.get(i));
                                } else {
                                    dr_Address = customVariablesAndMethod.getAddressByLatLong(mContext, dr_latLong);
                                    cbohelp.updateDrCall_Address(dr_Address, mydrList.get(i));
                                    cbohelp.updateDr_LocExtra(dr_latLong +"@"+dr_locExtra+ "!^" + dr_Address, mydrList.get(i));


                                }


                            } catch (Exception n) {
                                Log.e("Exception ", "............" + n);
                            }
                        }
                    if (i==0){
                        saprator ="";
                        saprator_1 ="";
                    }else {
                        saprator ="^";
                        saprator_1="|";
                    }
                        sb_sDCRDR_DR_ID.append(saprator).append(mydrList.get(i));
                        sb_sDCRDR_WW1.append(saprator).append(cbohelp.getDoctorww1FromSqlite(mydrList.get(i)));
                        sb_sDCRDR_WW2.append(saprator).append( cbohelp.getDoctorww2FromSqlite(mydrList.get(i)));
                        sb_sDCRDR_WW3.append(saprator).append(cbohelp.getDoctorww3FromSqlite(mydrList.get(i)));
                        sb_sDCRDR_LOC.append(saprator_1).append(cbohelp.getDoctorLocExtraFromSqlite(mydrList.get(i)));
                        sb_sDCRDR_IN_TIME.append(saprator).append(cbohelp.getDoctorTimeFromSqlite(mydrList.get(i)));
                        sb_sDCRDR_BATTERY_PERCENT.append(saprator).append(cbohelp.getBattryLevel_RC(mydrList.get(i)));
                        sb_sDCRDR_Remark.append(saprator).append(cbohelp.getDr_Remark(mydrList.get(i)));
                        sb_sDCRDR_KM.append(saprator).append(cbohelp.getKm_Doctor(mydrList.get(i)));
                        sb_sDCRDR_SRNO.append(saprator).append(cbohelp.getSRNO_Doctor(mydrList.get(i)));
                        sb_sDCRDR_FILE.append(saprator).append(cbohelp.getFILE_Doctor(mydrList.get(i)));
                        sb_sDCRDR_CALLTYPE.append(saprator).append(cbohelp.getCALL_TYPE_Doctor(mydrList.get(i)));
                        sb_sDR_REF_LAT_LONG.append(saprator).append(cbohelp.getRef_LatLong_Doctor(mydrList.get(i)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                map_DcrDr_Save.put("sb_sDCRDR_DR_ID",""+sb_sDCRDR_DR_ID);
                map_DcrDr_Save.put("sb_sDCRDR_WW1",""+sb_sDCRDR_WW1);
                map_DcrDr_Save.put("sb_sDCRDR_WW2",""+sb_sDCRDR_WW2);
                map_DcrDr_Save.put("sb_sDCRDR_WW3",""+sb_sDCRDR_WW3);
                map_DcrDr_Save.put("sb_sDCRDR_LOC",""+sb_sDCRDR_LOC);
                map_DcrDr_Save.put("sb_sDCRDR_IN_TIME",""+sb_sDCRDR_IN_TIME);
                map_DcrDr_Save.put("sb_sDCRDR_BATTERY_PERCENT",""+sb_sDCRDR_BATTERY_PERCENT);
                map_DcrDr_Save.put("sb_sDCRDR_Remark",""+sb_sDCRDR_Remark);
                map_DcrDr_Save.put("sb_sDCRDR_KM",""+sb_sDCRDR_KM);

                map_DcrDr_Save.put("sb_sDCRDR_SRNO",""+sb_sDCRDR_SRNO);
                map_DcrDr_Save.put("sb_sDCRDR_FILE",""+sb_sDCRDR_FILE);
                map_DcrDr_Save.put("sb_sDCRDR_CALLTYPE",""+sb_sDCRDR_CALLTYPE);
                map_DcrDr_Save.put("sb_sDR_REF_LAT_LONG",""+sb_sDR_REF_LAT_LONG);


                cbohelp.close();
            }
            return map_DcrDr_Save;
        }

        public Map<String,String> drItemSave(String updated){
            Map<String,String> commitItemMobileMap= new HashMap<String,String>();
           StringBuilder sb_sDCRITEM_DR_ID = new StringBuilder();
            StringBuilder sb_sDCRITEM_ITEMIDIN = new StringBuilder();
            StringBuilder sb_sDCRITEM_ITEM_ID_ARR= new StringBuilder();
            StringBuilder sb_sDCRITEM_QTY_ARR = new StringBuilder();
            StringBuilder sb_sDCRITEM_ITEM_ID_GIFT_ARR = new StringBuilder();
            StringBuilder sb_sDCRITEM_QTY_GIFT_ARR = new StringBuilder();

            StringBuilder sb_sDCRITEM_POB_QTY= new StringBuilder();
            StringBuilder sb_sDCRITEM_POB_VALUE = new StringBuilder();
            StringBuilder sb_sDCRITEM_VISUAL_ARR = new StringBuilder();
            StringBuilder sb_sDCRITEM_NOC_ARR = new StringBuilder();
            StringBuilder sb_DCRDR_RATE = new StringBuilder();

            String AllItemQty,AllPob,AllGiftId,AllGiftQty;
            ArrayList<String> doc_id;
            cbohelp=new CBO_DB_Helper(mContext);
            //doc_id=cbohelp.getdoctormoreLit(updated);
            doc_id=cbohelp.tempDrListForFinalSubmit(updated);
            //cbohelp.getDoctorListLocal(null,"1")
            if(doc_id.size()>0) {
                String visual_items = "0";
                String noc_value = "0";
                String rate = "";
                String Pob_Value="";
                String saperator="";
                for (int i = 0; i < doc_id.size(); i++)

                {
                    String main_id = doc_id.get(i);
                    String doc_itemid = cbohelp.getDocItem((doc_id.get(i))).get(0);
                    AllItemQty = cbohelp.getDocItem((doc_id.get(i))).get(1);
                    AllPob = cbohelp.getDocItem((doc_id.get(i))).get(2);
                    AllGiftId = cbohelp.getDocItem(doc_id.get(i)).get(0);
                    AllGiftQty = cbohelp.getDocItem(doc_id.get(i)).get(1);
                    visual_items = cbohelp.getDocItem((doc_id.get(i))).get(3);
                    noc_value = cbohelp.getDocItem((doc_id.get(i))).get(4);
                    rate = cbohelp.getDocItem((doc_id.get(i))).get(5);
                    if (i==0){
                        saperator ="";
                    }else {
                        saperator ="^";
                    }

                       sb_sDCRITEM_DR_ID.append(saperator).append(main_id);
                       sb_sDCRITEM_ITEMIDIN.append(saperator).append("0");
                      sb_sDCRITEM_ITEM_ID_ARR.append(saperator).append(doc_itemid);
                     sb_sDCRITEM_QTY_ARR.append(saperator).append(AllItemQty);
                     sb_sDCRITEM_ITEM_ID_GIFT_ARR.append(saperator).append(AllGiftId);
                      sb_sDCRITEM_QTY_GIFT_ARR.append(saperator).append(AllGiftQty);
                      sb_sDCRITEM_POB_QTY.append(saperator).append(AllPob);
                     sb_sDCRITEM_POB_VALUE.append(saperator).append("0");
                    sb_sDCRITEM_VISUAL_ARR.append(saperator).append(visual_items);
                    sb_sDCRITEM_NOC_ARR.append(saperator).append(noc_value);
                    sb_DCRDR_RATE.append(saperator).append(rate);
     }


                commitItemMobileMap.put("sb_sDCRITEM_DR_ID",""+sb_sDCRITEM_DR_ID);
                commitItemMobileMap.put("sb_sDCRITEM_ITEMIDIN",""+sb_sDCRITEM_ITEMIDIN);
                commitItemMobileMap.put("sb_sDCRITEM_ITEM_ID_ARR",""+sb_sDCRITEM_ITEM_ID_ARR);
                commitItemMobileMap.put("sb_sDCRITEM_QTY_ARR",""+sb_sDCRITEM_QTY_ARR);
                commitItemMobileMap.put("sb_sDCRITEM_ITEM_ID_GIFT_ARR",""+sb_sDCRITEM_ITEM_ID_GIFT_ARR);
                commitItemMobileMap.put("sb_sDCRITEM_QTY_GIFT_ARR",""+sb_sDCRITEM_QTY_GIFT_ARR);
                commitItemMobileMap.put("sb_sDCRITEM_POB_QTY",""+sb_sDCRITEM_POB_QTY);
                commitItemMobileMap.put("sb_sDCRITEM_POB_VALUE",""+sb_sDCRITEM_POB_VALUE);
                commitItemMobileMap.put("sb_sDCRITEM_VISUAL_ARR",""+sb_sDCRITEM_VISUAL_ARR);
                commitItemMobileMap.put("sb_sDCRITEM_NOC_ARR",""+sb_sDCRITEM_NOC_ARR);
                commitItemMobileMap.put("sb_DCRDR_RATE",""+sb_DCRDR_RATE);

                //cbohelp.deleteDoctor();
                cbohelp.close();
            }

            return commitItemMobileMap;

        }







    public Map<String,String> drRx_Save(String updated){
        Map<String,String> commitRxItemMobileMap= new HashMap<String,String>();
        StringBuilder sDCRRX_DR_ARR = new StringBuilder();
        StringBuilder sDCRRX_ITEMID_ARR = new StringBuilder();



        cbohelp=new CBO_DB_Helper(mContext);
        ArrayList<String> doc_id=cbohelp.getDr_Rx_id(updated);
        if(doc_id.size()>0) {

            String Pob_Value="";
            String saperator="";
            for (int i = 0; i < doc_id.size(); i++)

            {
                String main_id = doc_id.get(i);
                String doc_itemid = cbohelp.getDr_Rx_itemId((doc_id.get(i)));

                if (i==0){
                    saperator ="";
                }else {
                    saperator ="^";
                }

                sDCRRX_DR_ARR.append(saperator).append(main_id);
                sDCRRX_ITEMID_ARR.append(saperator).append(doc_itemid);
                 }


            commitRxItemMobileMap.put("sDCRRX_DR_ARR",""+sDCRRX_DR_ARR);
            commitRxItemMobileMap.put("sDCRRX_ITEMID_ARR",""+sDCRRX_ITEMID_ARR);

            cbohelp.close();
        }

        return commitRxItemMobileMap;

    }





        public Map<String,String> dcr_chemSave(String updated)
        {
            StringBuilder sb_sDCRCHEM_CHEM_ID = new StringBuilder();
            StringBuilder sb_sDCRCHEM_POB_QTY = new StringBuilder();
            StringBuilder sb_sDCRCHEM_POB_AMT = new StringBuilder();
            StringBuilder sb_sDCRCHEM_ITEM_ID_ARR = new StringBuilder();
            StringBuilder sb_sDCRCHEM_QTY_ARR = new StringBuilder();
            StringBuilder sb_sDCRCHEM_LOC = new StringBuilder();
            StringBuilder sb_sDCRCHEM_IN_TIME = new StringBuilder();
            StringBuilder sb_sDCRCHEM_SQTY_ARR = new StringBuilder();
            StringBuilder sb_sDCRCHEM_ITEM_ID_GIFT_ARR = new StringBuilder();
            StringBuilder sb_sDCRCHEM_QTY_GIFT_ARR = new StringBuilder();
            StringBuilder sb_sDCRCHEM_BATTERY_PERCENT = new StringBuilder();
            StringBuilder sb_sDCRCHEM_KM = new StringBuilder();
            StringBuilder sb_sDCRCHEM_SRNO = new StringBuilder();
            StringBuilder sb_sDCRCHEM_REMARK = new StringBuilder();
            StringBuilder sb_sDCRCHEM_FILE = new StringBuilder();
            StringBuilder sb_sCHEM_REF_LAT_LONG = new StringBuilder();
            StringBuilder sb_DCRCHEM_RATE = new StringBuilder();
            StringBuilder sCHEM_STATUS = new StringBuilder();
            StringBuilder sCOMPETITOR_REMARK = new StringBuilder();


            Map<String,String> map_Dcr_ChemistSave = new HashMap<String,String>();


            cbohelp=new CBO_DB_Helper(mContext);
            ArrayList<String>chem_id=null;
            chem_id=cbohelp.chemistListForFinalSubmit(updated);
            if(chem_id.size()>0) {
               String saprator="";
                String saprator_1="|";

                for (int i = 0; i < chem_id.size(); i++) {

                        try {
                            String chemLatLong, chemAddress, chem_locExtra;
                            chemLatLong = cbohelp.getChemistLatLong(chem_id.get(i));
                            chemAddress = cbohelp.getChemistAddress(chem_id.get(i));
                            chem_locExtra=cbohelp.getChemistLocExtra(chem_id.get(i));
                            cbohelp.upDateChemistLocExtra(chem_id.get(i), chemLatLong +"@"+chem_locExtra +"!^" + chemAddress);
                            if (updated != null) {

                                try {
                                    if ((chemAddress.equals(""))) {
                                        chemAddress = customVariablesAndMethod.getAddressByLatLong(mContext, chemLatLong);
                                        cbohelp.getUpdateChemistAddress(chem_id.get(i), chemAddress);
                                        cbohelp.upDateChemistLocExtra(chem_id.get(i), chemLatLong +"@"+chem_locExtra +"!^" + chemAddress);
                                    } else {
                                        chemAddress = customVariablesAndMethod.getAddressByLatLong(mContext, chemLatLong);
                                        cbohelp.getUpdateChemistAddress(chem_id.get(i), chemAddress);
                                        cbohelp.upDateChemistLocExtra(chem_id.get(i), chemLatLong +"@"+chem_locExtra + "!^" + chemAddress);

                                    }
                                } catch (Exception n) {
                                    Log.e("Exception ", "............" + n);
                                }
                            }

                        if (i==0){
                            saprator ="";
                            saprator_1 ="";

                        }
                        else {
                            saprator ="^";
                            saprator_1 ="|";
                        }

                        sb_sDCRCHEM_CHEM_ID.append(saprator).append(chem_id.get(i));
                        sb_sDCRCHEM_POB_QTY.append(saprator).append(cbohelp.chemAllItemSample(chem_id.get(i)));
                        sb_sDCRCHEM_POB_AMT.append(saprator).append(cbohelp.chemAllItemPob(chem_id.get(i)));

                        sb_sDCRCHEM_ITEM_ID_ARR.append(saprator).append(cbohelp.chemAllItem(chem_id.get(i)));
                        sb_sDCRCHEM_QTY_ARR.append(saprator).append(cbohelp.chemAllItemQty(chem_id.get(i)));
                        sb_sDCRCHEM_LOC.append(saprator_1).append( cbohelp.chemAllItemLocExtra(chem_id.get(i)));
                        sb_sDCRCHEM_IN_TIME.append(saprator).append(cbohelp.chemAllTime(chem_id.get(i)));
                        sb_sDCRCHEM_SQTY_ARR.append(saprator).append(cbohelp.chemAllItemSample(chem_id.get(i)));
                        sb_sDCRCHEM_ITEM_ID_GIFT_ARR.append(saprator).append( cbohelp.chemAllItemGiftid(chem_id.get(i)));
                        sb_sDCRCHEM_QTY_GIFT_ARR.append(saprator).append( cbohelp.chemAllItemGiftQty(chem_id.get(i)));
                        sb_sDCRCHEM_BATTERY_PERCENT.append(saprator).append( cbohelp.chemBatterLevel(chem_id.get(i)));
                        sb_sDCRCHEM_KM.append(saprator).append( cbohelp.getKm_Chemist(chem_id.get(i)));
                        sb_sDCRCHEM_SRNO.append(saprator).append( cbohelp.getSRNO_Chemist(chem_id.get(i)));
                        sb_sDCRCHEM_REMARK.append(saprator).append( cbohelp.getRemark_Chemist(chem_id.get(i)));
                        sb_sDCRCHEM_FILE.append(saprator).append( cbohelp.getFileChemist(chem_id.get(i)));
                        sb_sCHEM_REF_LAT_LONG.append(saprator).append( cbohelp.getRef_LotLong_Chemist(chem_id.get(i)));
                        sb_DCRCHEM_RATE.append(saprator).append( cbohelp.getRate_Chemist(chem_id.get(i)));
                        sCHEM_STATUS.append(saprator).append( cbohelp.getStatus_Chemist (chem_id.get(i)));
                        sCOMPETITOR_REMARK.append(saprator).append( cbohelp.getCompProduct_Chemist (chem_id.get(i)));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                map_Dcr_ChemistSave.put("sb_sDCRCHEM_CHEM_ID",""+sb_sDCRCHEM_CHEM_ID);
                map_Dcr_ChemistSave.put("sb_sDCRCHEM_POB_QTY",""+sb_sDCRCHEM_POB_QTY);
                map_Dcr_ChemistSave.put("sb_sDCRCHEM_POB_AMT",""+sb_sDCRCHEM_POB_AMT);
                map_Dcr_ChemistSave.put("sb_sDCRCHEM_ITEM_ID_ARR",""+sb_sDCRCHEM_ITEM_ID_ARR);
                map_Dcr_ChemistSave.put("sb_sDCRCHEM_QTY_ARR",""+sb_sDCRCHEM_QTY_ARR);
                map_Dcr_ChemistSave.put("sb_sDCRCHEM_LOC",""+sb_sDCRCHEM_LOC);
                map_Dcr_ChemistSave.put("sb_sDCRCHEM_IN_TIME",""+sb_sDCRCHEM_IN_TIME);
                map_Dcr_ChemistSave.put("sb_sDCRCHEM_SQTY_ARR",""+sb_sDCRCHEM_SQTY_ARR);
                map_Dcr_ChemistSave.put("sb_sDCRCHEM_ITEM_ID_GIFT_ARR",""+sb_sDCRCHEM_ITEM_ID_GIFT_ARR);
                map_Dcr_ChemistSave.put("sb_sDCRCHEM_QTY_GIFT_ARR",""+sb_sDCRCHEM_QTY_GIFT_ARR);
                map_Dcr_ChemistSave.put("sb_sDCRCHEM_BATTERY_PERCENT",""+sb_sDCRCHEM_BATTERY_PERCENT);
                map_Dcr_ChemistSave.put("sb_sDCRCHEM_KM",""+sb_sDCRCHEM_KM);

                map_Dcr_ChemistSave.put("sb_sDCRCHEM_SRNO",""+sb_sDCRCHEM_SRNO);
                map_Dcr_ChemistSave.put("sb_sDCRCHEM_REMARK",""+sb_sDCRCHEM_REMARK);
                map_Dcr_ChemistSave.put("sb_sDCRCHEM_FILE",""+sb_sDCRCHEM_FILE);
                map_Dcr_ChemistSave.put("sb_sCHEM_REF_LAT_LONG",""+sb_sCHEM_REF_LAT_LONG);
                map_Dcr_ChemistSave.put("sb_DCRCHEM_RATE",""+sb_DCRCHEM_RATE);
                map_Dcr_ChemistSave.put("sCHEM_STATUS",""+sCHEM_STATUS);
                map_Dcr_ChemistSave.put("sCOMPETITOR_REMARK",""+sCOMPETITOR_REMARK);


                cbohelp.close();
            }
            return  map_Dcr_ChemistSave;
        }

        public Map<String,String> dcr_stkSave(String updated)
        {

            Map<String,String> map_StkSave= new HashMap<String,String>();
            StringBuilder sb_sDCRSTK_STK_ID = new StringBuilder();
            StringBuilder sb_sDCRSTK_POB_QTY = new StringBuilder();
            StringBuilder sb_sDCRSTK_POB_AMT = new StringBuilder();
            StringBuilder sb_sDCRSTK_ITEM_ID_ARR = new StringBuilder();
            StringBuilder sb_sDCRSTK_QTY_ARR = new StringBuilder();
            StringBuilder sb_sDCRSTK_LOC = new StringBuilder();
            StringBuilder sb_sDCRSTK_IN_TIME = new StringBuilder();
            StringBuilder sb_sDCRSTK_SQTY_ARR = new StringBuilder();
            StringBuilder sb_sDCRSTK_ITEM_ID_GIFT_ARR = new StringBuilder();
            StringBuilder sb_sDCRSTK_QTY_GIFT_ARR = new StringBuilder();
            StringBuilder sb_sDCRSTK_BATTERY_PERCENT = new StringBuilder();
            StringBuilder sb_sDCRSTK_KM = new StringBuilder();
            StringBuilder sb_sDCRSTK_SRNO= new StringBuilder();
            StringBuilder sb_sDCRSTK_REMARK = new StringBuilder();
            StringBuilder sb_sDCRSTK_FILE= new StringBuilder();
            StringBuilder sb_sSTK_REF_LAT_LONG = new StringBuilder();
            StringBuilder sb_DCRSTK_RATE = new StringBuilder();


            ArrayList<String>stk_id=null;

            cbohelp=new CBO_DB_Helper(mContext);

            stk_id=cbohelp.stockistListForFinalSubmit(updated);
            String saprator="";
            String saprator_1="";
            if(stk_id.size()>0) {
                for (int i = 0; i < stk_id.size(); i++) {
                    String loc = cbohelp.stockistAllItemLatLong(stk_id.get(i));
                    String locExtra = cbohelp.stockistAllItemLocExtra(stk_id.get(i));
                    String address = "";
                    cbohelp.stockistupdateAllItemAddress(stk_id.get(i), loc +"@"+locExtra+ "!^" + address);

                    if (updated != null) {
                        try {

                                address =  customVariablesAndMethod.getAddressByLatLong(mContext, loc);
                                cbohelp.stockistupdateAllItemAddress(stk_id.get(i), loc +"@"+locExtra+ "!^" + address);

                        }catch (Exception e){
                            Log.v("javed stk",e.toString());
                        }


                    }

                    try {
                        if (i == 0) {
                            saprator = "";
                            saprator_1 ="";

                        } else {
                            saprator="^";
                            saprator_1="|";

                        }

                        sb_sDCRSTK_STK_ID.append(saprator).append(stk_id.get(i));
                        sb_sDCRSTK_POB_QTY.append(saprator).append(cbohelp.stockistAllItemSample(stk_id.get(i)));
                        sb_sDCRSTK_POB_AMT.append(saprator).append(cbohelp.stockistAllItemPOB(stk_id.get(i)));
                        sb_sDCRSTK_ITEM_ID_ARR.append(saprator).append(cbohelp.stockistAllItemId(stk_id.get(i)));
                        sb_sDCRSTK_QTY_ARR.append(saprator).append(cbohelp.stockistAllItemQty(stk_id.get(i)));

                        sb_sDCRSTK_LOC.append(saprator_1).append(cbohelp.stockistAllItemAddress(stk_id.get(i)));
                        sb_sDCRSTK_IN_TIME.append(saprator).append(cbohelp.stockistAllTime(stk_id.get(i)));
                        sb_sDCRSTK_SQTY_ARR.append(saprator).append(cbohelp.stockistAllItemSample(stk_id.get(i)));
                        sb_sDCRSTK_ITEM_ID_GIFT_ARR.append(saprator).append(cbohelp.stockistAllItemGiftid(stk_id.get(i)));
                        sb_sDCRSTK_QTY_GIFT_ARR.append(saprator).append(cbohelp.stockistAllItemGiftQty(stk_id.get(i)));
                        sb_sDCRSTK_BATTERY_PERCENT.append(saprator).append(cbohelp.stockist_Battery(stk_id.get(i)));
                        sb_sDCRSTK_KM.append(saprator).append(cbohelp.getKm_Stockist(stk_id.get(i)));
                        sb_sDCRSTK_SRNO.append(saprator).append(cbohelp.getSRNO_Stockist(stk_id.get(i)));
                        sb_sDCRSTK_REMARK.append(saprator).append(cbohelp.getRemark_Stockist(stk_id.get(i)));
                        sb_sDCRSTK_FILE.append(saprator).append(cbohelp.getFile_Stockist(stk_id.get(i)));
                        sb_sSTK_REF_LAT_LONG.append(saprator).append(cbohelp.getRefLatLong_Stockist(stk_id.get(i)));
                        sb_DCRSTK_RATE.append(saprator).append(cbohelp.getRate_Stockist(stk_id.get(i)));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                map_StkSave.put("sb_sDCRSTK_STK_ID",""+sb_sDCRSTK_STK_ID);
                map_StkSave.put("sb_sDCRSTK_POB_QTY",""+sb_sDCRSTK_POB_QTY);
                map_StkSave.put("sb_sDCRSTK_POB_AMT",""+sb_sDCRSTK_POB_AMT);
                map_StkSave.put("sb_sDCRSTK_ITEM_ID_ARR",""+sb_sDCRSTK_ITEM_ID_ARR);
                map_StkSave.put("sb_sDCRSTK_QTY_ARR",""+sb_sDCRSTK_QTY_ARR);
                map_StkSave.put("sb_sDCRSTK_LOC",""+sb_sDCRSTK_LOC);
                map_StkSave.put("sb_sDCRSTK_IN_TIME",""+sb_sDCRSTK_IN_TIME);
                map_StkSave.put("sb_sDCRSTK_SQTY_ARR",""+sb_sDCRSTK_SQTY_ARR);
                map_StkSave.put("sb_sDCRSTK_ITEM_ID_GIFT_ARR",""+sb_sDCRSTK_ITEM_ID_GIFT_ARR);
                map_StkSave.put("sb_sDCRSTK_QTY_GIFT_ARR",""+sb_sDCRSTK_QTY_GIFT_ARR);
                map_StkSave.put("sb_sDCRSTK_BATTERY_PERCENT",""+sb_sDCRSTK_BATTERY_PERCENT);
                map_StkSave.put("sb_sDCRSTK_KM",""+sb_sDCRSTK_KM);

                map_StkSave.put("sb_sDCRSTK_SRNO",""+sb_sDCRSTK_SRNO);
                map_StkSave.put("sb_sDCRSTK_REMARK",""+sb_sDCRSTK_REMARK);
                map_StkSave.put("sb_sDCRSTK_FILE",""+sb_sDCRSTK_FILE);
                map_StkSave.put("sb_sSTK_REF_LAT_LONG",""+sb_sSTK_REF_LAT_LONG);
                map_StkSave.put("sb_DCRSTK_RATE",""+sb_DCRSTK_RATE);


                cbohelp.close();
            }
            return map_StkSave;
        }


        public Map<String,String> dcr_DrReminder(String updated)
        {
            Map<String,String> map_DrRemider = new HashMap<String,String>();
            StringBuilder sb_sDCRRC_DR_ID = new StringBuilder();
            StringBuilder sb_sDCRRC_LOC = new StringBuilder();
            StringBuilder sb_sDCRRC_IN_TIME = new StringBuilder();
            StringBuilder sb_sDCRRC_KM = new StringBuilder();
            StringBuilder sb_sDCRRC_SRNO = new StringBuilder();
            StringBuilder sb_sDCRRC_BATTERY_PERCENT = new StringBuilder();
            StringBuilder sb_sDCRRC_REMARK = new StringBuilder();
            StringBuilder sb_sDCRRC_FILE = new StringBuilder();
            StringBuilder sb_sRC_REF_LAT_LONG = new StringBuilder();

            cbohelp=new CBO_DB_Helper(mContext);
            ArrayList<String>dr_remId=new ArrayList<String>();
            dr_remId=cbohelp.getDrRc(updated);
            if(dr_remId.size()>0) {
                String saperator ="";
                String saperator_1 ="";
                for (int i = 0; i < dr_remId.size(); i++) {
                    try {
                        String loc = cbohelp.getLatLong_RC(dr_remId.get(i));
                        String locExtra = cbohelp.getLocExtra_RC(dr_remId.get(i));
                        String address =  "";
                        cbohelp.Dr_RCupdateAllItemAddress(dr_remId.get(i), loc +"@"+locExtra+ "!^" + address);

                        if (updated != null) {
                            try {

                                address =  customVariablesAndMethod.getAddressByLatLong(mContext, loc);
                                cbohelp.Dr_RCupdateAllItemAddress(dr_remId.get(i), loc +"@"+locExtra+ "!^" + address);

                            }catch (Exception e){
                                Log.v("javed stk",e.toString());
                            }


                        }

                        if (i==0) {
                            saperator ="";
                            saperator_1 ="";

                        } else {
                           saperator = "^";
                            saperator_1="|";
                        }


                        sb_sDCRRC_DR_ID.append(saperator).append(dr_remId.get(i));
                        sb_sDCRRC_LOC.append(saperator_1).append( cbohelp.getAddress_RC(dr_remId.get(i)));
                        sb_sDCRRC_IN_TIME.append(saperator).append(cbohelp.getTime_RC(dr_remId.get(i)));
                        sb_sDCRRC_KM.append(saperator).append(cbohelp.getKm_Rc(dr_remId.get(i)));
                        sb_sDCRRC_SRNO.append(saperator).append(cbohelp.getSRNO_Rc(dr_remId.get(i)));
                        sb_sDCRRC_BATTERY_PERCENT.append(saperator).append(cbohelp.Rc_Battery(dr_remId.get(i)));
                        sb_sDCRRC_REMARK.append(saperator).append(cbohelp.Rc_remark(dr_remId.get(i)));
                        sb_sDCRRC_FILE.append(saperator).append(cbohelp.Rc_file(dr_remId.get(i)));
                        sb_sRC_REF_LAT_LONG.append(saperator).append(cbohelp.Rc_RefLatLong(dr_remId.get(i)));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                map_DrRemider.put("sb_sDCRRC_DR_ID",""+sb_sDCRRC_DR_ID);
                map_DrRemider.put("sb_sDCRRC_LOC",""+sb_sDCRRC_LOC);
                map_DrRemider.put("sb_sDCRRC_IN_TIME",""+sb_sDCRRC_IN_TIME);
                map_DrRemider.put("sb_sDCRRC_KM",""+sb_sDCRRC_KM);

                map_DrRemider.put("sb_sDCRRC_SRNO",""+sb_sDCRRC_SRNO);
                map_DrRemider.put("sb_sDCRRC_BATTERY_PERCENT",""+sb_sDCRRC_BATTERY_PERCENT);
                map_DrRemider.put("sb_sDCRRC_REMARK",""+sb_sDCRRC_REMARK);
                map_DrRemider.put("sb_sDCRRC_FILE",""+sb_sDCRRC_FILE);
                map_DrRemider.put("sb_sRC_REF_LAT_LONG",""+sb_sRC_REF_LAT_LONG);

                cbohelp.close();
            }
            return map_DrRemider;
        }

    public Map<String,String> get_Lat_Long_Reg(String updated)
    {
        Map<String,String> map_Lat_Long_Reg = new HashMap<String,String>();
        StringBuilder sb_DCS_ID_ARR = new StringBuilder();
        StringBuilder sb_LAT_LONG_ARR = new StringBuilder();
        StringBuilder sb_DCS_TYPE_ARR = new StringBuilder();
        StringBuilder sb_DCS_ADD_ARR = new StringBuilder();
        StringBuilder sb_DCS_INDES_ARR = new StringBuilder();;


        cbohelp=new CBO_DB_Helper(mContext);

        ArrayList<HashMap<String,String>>  Lat_Long_Reg=cbohelp.get_Lat_Long_Reg(updated);
        if(Lat_Long_Reg.size()>0) {
            String saperator ="";
            String saperator_1 ="";
            for (int i = 0; i < Lat_Long_Reg.size(); i++) {
                try {

                    if (i==0) {
                        saperator ="";
                        saperator_1 ="";

                    } else {
                        saperator = "^";
                        saperator_1="|";
                    }


                    sb_DCS_ID_ARR.append(saperator).append(Lat_Long_Reg.get(i).get("DCS_ID"));
                    sb_LAT_LONG_ARR.append(saperator_1).append( Lat_Long_Reg.get(i).get("LAT_LONG"));
                    sb_DCS_TYPE_ARR.append(saperator).append(Lat_Long_Reg.get(i).get("DCS_TYPE"));
                    sb_DCS_ADD_ARR.append(saperator).append(Lat_Long_Reg.get(i).get("DCS_ADD"));
                    sb_DCS_INDES_ARR.append(saperator).append(Lat_Long_Reg.get(i).get("DCS_INDES"));


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            map_Lat_Long_Reg.put("DCS_ID_ARR",""+sb_DCS_ID_ARR);
            map_Lat_Long_Reg.put("LAT_LONG_ARR",""+sb_LAT_LONG_ARR);
            map_Lat_Long_Reg.put("DCS_TYPE_ARR",""+sb_DCS_TYPE_ARR);
            map_Lat_Long_Reg.put("DCS_ADD_ARR",""+sb_DCS_ADD_ARR);
            map_Lat_Long_Reg.put("DCS_INDES_ARR",""+sb_DCS_INDES_ARR);


            cbohelp.close();
        }
        return map_Lat_Long_Reg;
    }

    public Map<String,String> get_phdairy_dcr(String updated)
    {
        Map<String,String> map_phdairy_dcr = new HashMap<String,String>();

        StringBuilder sb_sDAIRY_ID = new StringBuilder();
        StringBuilder sb_sSTRDAIRY_CPID = new StringBuilder();
        StringBuilder sb_sDCRDAIRY_LOC = new StringBuilder();
        StringBuilder sb_sDCRDAIRY_IN_TIME = new StringBuilder();
        StringBuilder sb_sDCRDAIRY_BATTERY_PERCENT = new StringBuilder();
        StringBuilder sb_sDCRDAIRY_REMARK = new StringBuilder();
        StringBuilder sb_sDCRDAIRY_KM = new StringBuilder();
        StringBuilder sb_sDCRDAIRY_SRNO = new StringBuilder();
        StringBuilder sb_sDCRDAIRYITEM_DAIRY_ID = new StringBuilder();
        StringBuilder sb_sDCRDAIRYITEM_ITEM_ID_ARR = new StringBuilder();
        StringBuilder sb_sDCRDAIRYITEM_QTY_ARR = new StringBuilder();
        StringBuilder sb_sDCRDAIRYITEM_ITEM_ID_GIFT_ARR = new StringBuilder();
        StringBuilder sb_sDCRDAIRYITEM_QTY_GIFT_ARR = new StringBuilder();
        StringBuilder sb_sDCRDAIRYITEM_POB_QTY = new StringBuilder();
        StringBuilder sb_sDAIRY_FILE = new StringBuilder();
        StringBuilder sb_sDCRDAIRY_INTERSETEDYN = new StringBuilder();
        StringBuilder sb_sDAIRY_REF_LAT_LONG = new StringBuilder();

        cbohelp=new CBO_DB_Helper(mContext);

        ArrayList<HashMap<String,String>>  phdairy_dcr=cbohelp.get_phdairy_dcr(updated);
        if(phdairy_dcr.size()>0) {
            String saperator ="";
            String saperator_1 ="";
            for (int i = 0; i < phdairy_dcr.size(); i++) {
                try {

                    if (i==0) {
                        saperator ="";
                        saperator_1 ="";

                    } else {
                        saperator = "^";
                        saperator_1="|";
                    }

                    //loc +"@"+locExtra+ "!^" + address
                    sb_sDAIRY_ID.append(saperator).append(phdairy_dcr.get(i).get("DAIRY_ID"));
                    sb_sSTRDAIRY_CPID.append(saperator).append(phdairy_dcr.get(i).get("person_id"));
                    sb_sDCRDAIRY_LOC.append(saperator_1).append(phdairy_dcr.get(i).get("dr_latLong") +"@"+ phdairy_dcr.get(i).get("LOC_EXTRA") +"!^"+phdairy_dcr.get(i).get("dr_address") );
                    sb_sDCRDAIRY_IN_TIME.append(saperator).append(phdairy_dcr.get(i).get("visit_time"));
                    sb_sDCRDAIRY_BATTERY_PERCENT.append(saperator).append(phdairy_dcr.get(i).get("batteryLevel"));
                    sb_sDCRDAIRY_REMARK.append(saperator).append(phdairy_dcr.get(i).get("dr_remark"));
                    sb_sDCRDAIRY_KM.append(saperator).append(phdairy_dcr.get(i).get("dr_km"));
                    sb_sDCRDAIRY_SRNO.append(saperator).append(phdairy_dcr.get(i).get("srno"));
                    sb_sDCRDAIRYITEM_DAIRY_ID.append(saperator).append(phdairy_dcr.get(i).get("DAIRY_ID"));
                    sb_sDCRDAIRYITEM_ITEM_ID_ARR.append(saperator).append(phdairy_dcr.get(i).get("allitemid"));
                    sb_sDCRDAIRYITEM_QTY_ARR.append(saperator).append(phdairy_dcr.get(i).get("sample"));
                    sb_sDCRDAIRYITEM_ITEM_ID_GIFT_ARR.append(saperator).append(phdairy_dcr.get(i).get("allgiftid"));
                    sb_sDCRDAIRYITEM_QTY_GIFT_ARR.append(saperator).append(phdairy_dcr.get(i).get("allgiftqty"));
                    sb_sDCRDAIRYITEM_POB_QTY.append(saperator).append(phdairy_dcr.get(i).get("allitemqty"));
                    sb_sDAIRY_FILE.append(saperator).append(phdairy_dcr.get(i).get("file"));
                    sb_sDCRDAIRY_INTERSETEDYN.append(saperator).append(phdairy_dcr.get(i).get("IS_INTRESTED"));
                    sb_sDAIRY_REF_LAT_LONG.append(saperator).append(phdairy_dcr.get(i).get("Ref_latlong"));



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            map_phdairy_dcr.put("sDAIRY_ID",""+sb_sDAIRY_ID);
            map_phdairy_dcr.put("sSTRDAIRY_CPID",""+sb_sSTRDAIRY_CPID);
            map_phdairy_dcr.put("sDCRDAIRY_LOC",""+sb_sDCRDAIRY_LOC);
            map_phdairy_dcr.put("sDCRDAIRY_IN_TIME",""+sb_sDCRDAIRY_IN_TIME);
            map_phdairy_dcr.put("sDCRDAIRY_BATTERY_PERCENT",""+sb_sDCRDAIRY_BATTERY_PERCENT);
            map_phdairy_dcr.put("sDCRDAIRY_REMARK",""+sb_sDCRDAIRY_REMARK);
            map_phdairy_dcr.put("sDCRDAIRY_KM",""+sb_sDCRDAIRY_KM);
            map_phdairy_dcr.put("sDCRDAIRY_SRNO",""+sb_sDCRDAIRY_SRNO);
            map_phdairy_dcr.put("sDCRDAIRYITEM_DAIRY_ID",""+sb_sDCRDAIRYITEM_DAIRY_ID);
            map_phdairy_dcr.put("sDCRDAIRYITEM_ITEM_ID_ARR",""+sb_sDCRDAIRYITEM_ITEM_ID_ARR);
            map_phdairy_dcr.put("sDCRDAIRYITEM_ITEM_ID_GIFT_ARR",""+sb_sDCRDAIRYITEM_ITEM_ID_GIFT_ARR);
            map_phdairy_dcr.put("sDCRDAIRYITEM_QTY_GIFT_ARR",""+sb_sDCRDAIRYITEM_QTY_GIFT_ARR);
            map_phdairy_dcr.put("sDCRDAIRYITEM_POB_QTY",""+sb_sDCRDAIRYITEM_POB_QTY);
            map_phdairy_dcr.put("sDAIRY_FILE",""+sb_sDAIRY_FILE);
            map_phdairy_dcr.put("sDCRDAIRYITEM_QTY_ARR",""+sb_sDCRDAIRYITEM_QTY_ARR);
            map_phdairy_dcr.put("sDCRDAIRY_INTERSETEDYN",""+sb_sDCRDAIRY_INTERSETEDYN);
            map_phdairy_dcr.put("sDAIRY_REF_LAT_LONG",""+sb_sDAIRY_REF_LAT_LONG);


            cbohelp.close();
        }
        return map_phdairy_dcr;
    }


}


