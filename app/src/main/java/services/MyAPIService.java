package services;

import android.content.Context;

import com.uenics.javed.CBOLibrary.CBOServices;
import com.uenics.javed.CBOLibrary.SendMail;

import java.util.ArrayList;

import utils.clearAppData.MyCustumApplication;
import utils_new.Custom_Variables_And_Method;

/**
 * Created by cboios on 24/11/18.
 */

public class MyAPIService extends CBOServices {


    private static String URL= "http://www.cboservices.com/mobilerpt.asmx";

    public MyAPIService(Context context) {
        super(context, MyCustumApplication.getServeiceURL());
        setAternateURL(Custom_Variables_And_Method.getInstance().getDataFrom_FMCG_PREFRENCE(context,"WEBSERVICE_URL_ALTERNATE",""));

        ArrayList<String> toEmailList = new ArrayList<>();
        //String to1=Custom_Variables_And_Method.getInstance().getDataFrom_FMCG_PREFRENCE(context,"ERROR_EMAIL","");    //"javed.hussain19911@gmail.com";

        toEmailList.add("mobilereporting@cboinfotech.com");
        /*if (!to1.trim().isEmpty()) {
            toEmailList.add(to1);
        }*/
        configureErrorMail(new SendMail.MailBuilder("mobilereporting@cboinfotech.com", "mreporting")
                .setSendTo(toEmailList)
                .setSubject(Custom_Variables_And_Method.COMPANY_CODE)
                .setBody("Company Code :" +Custom_Variables_And_Method.COMPANY_CODE+
                        "\n  DCR ID :"+Custom_Variables_And_Method.DCR_ID+
                        "\n PA ID : "+Custom_Variables_And_Method.PA_ID+
                        "\n App version : "+Custom_Variables_And_Method.VERSION)
                .setShowProgess(false));
    }



}