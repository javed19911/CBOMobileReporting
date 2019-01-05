package services;

import android.content.Context;

import com.uenics.javed.CBOLibrary.CBOServices;

import utils.clearAppData.MyCustumApplication;
import utils_new.Custom_Variables_And_Method;

/**
 * Created by cboios on 24/11/18.
 */

public class MyAPIService extends CBOServices {


    Custom_Variables_And_Method customVariablesAndMethod;
    private static String URL= "http://www.cboservices.com/mobilerpt.asmx";

    public MyAPIService(Context context) {
        super(context, MyCustumApplication.getServeiceURL());
        setAternateURL(Custom_Variables_And_Method.getInstance().getDataFrom_FMCG_PREFRENCE(context,"WEBSERVICE_URL_ALTERNATE",""));
    }



}