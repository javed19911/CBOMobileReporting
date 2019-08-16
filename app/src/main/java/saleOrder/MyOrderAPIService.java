package saleOrder;

import android.content.Context;

import com.cbo.cbomobilereporting.MyCustumApplication;
import com.cbo.cbomobilereporting.databaseHelper.User.mUser;
import com.uenics.javed.CBOLibrary.CBOServices;
import com.uenics.javed.CBOLibrary.ResponseBuilder;
import com.uenics.javed.CBOLibrary.SendMail;

import java.util.ArrayList;

import utils_new.Custom_Variables_And_Method;

/**
 * Created by cboios on 24/11/18.
 */

public class MyOrderAPIService extends CBOServices {


    private static String URL= "http://www.cboservices.com/doctorrpt.asmx";

    public MyOrderAPIService(Context context) {
        super(context, URL);
        //setAternateURL(Custom_Variables_And_Method.getInstance().getDataFrom_FMCG_PREFRENCE(context,"WEBSERVICE_URL_ALTERNATE",""));

        ArrayList<String> toEmailList = new ArrayList<>();
        //String to1= Custom_Variables_And_Method.getInstance().getDataFrom_FMCG_PREFRENCE(context,"ERROR_EMAIL","");    //"javed.hussain19911@gmail.com";

        toEmailList.add("mobilereporting@cboinfotech.com");
       /* if (!to1.trim().isEmpty()) {
            toEmailList.add(to1);
        }*/
        mUser user = MyCustumApplication.getInstance().getUser();

        configureErrorMail(new SendMail.MailBuilder("mobilereporting@cboinfotech.com", "mreporting")
                .setSendTo(toEmailList)
                .setSubject(user.getCompanyCode())
                .setBody("Company Code :" +user.getCompanyCode()+
                        "\n  DCR ID :"+user.getDCRId()+
                        "\n PA ID : "+user.getID()+
                        "\n App version : "+user.getAppVersion())
                .setShowProgess(false));
    }

    @Override
    public void execute(ResponseBuilder responseBuilder) {
        responseBuilder.getRequest().put("ISSUPPORTUSER", MyCustumApplication.getInstance().getUser().getLoggedInAsSupport()?"Y":"N");
        responseBuilder.getRequest().put("LOCATION_LAT_LONG", MyCustumApplication.getInstance().getDataFrom_FMCG_PREFRENCE("shareLatLong",""));
        responseBuilder.getRequest().put("APP_VERSION", MyCustumApplication.getInstance().getUser().getAppVersion());
        responseBuilder.getRequest().put("IMEI", MyCustumApplication.getInstance().getUser().getIMEI());
        responseBuilder.getRequest().put("OS", MyCustumApplication.getInstance().getUser().getOS());
        super.execute(responseBuilder);
    }


}