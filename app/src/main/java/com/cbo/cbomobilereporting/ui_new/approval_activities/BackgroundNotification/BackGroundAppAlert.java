package com.cbo.cbomobilereporting.ui_new.approval_activities.BackgroundNotification;

import android.content.Context;
import android.content.Intent;

public class BackGroundAppAlert {


   /* private String positiveTxt = "OK";
    private String nagativeTxt = "Cancel";*/
    private static BackGroundAppAlert ourInstance = null;
    private String code = "";

    private BackGroundAppAlert() {
        super();
    }

    public static BackGroundAppAlert getInstance() {
        if (ourInstance == null) {
            ourInstance = new BackGroundAppAlert();
        }
        return ourInstance;
    }

    public String getCode() {
        return code;
    }

    public BackGroundAppAlert setCode(String code) {
        this.code = code;
        return this;
    }

   /* public String getPositiveTxt() {
        return positiveTxt;
    }

    public BackGroundAppAlert setPositiveTxt(String positiveTxt) {
        this.positiveTxt = positiveTxt;
        return this;
    }

    public String getNagativeTxt() {
        return nagativeTxt;
    }

    public BackGroundAppAlert setNagativeTxt(String nagativeTxt) {
        this.nagativeTxt = nagativeTxt;
        return this;
    }
*/
    public void getAlert(Context context, String title, String message) {
        getAlert(context,title,message,null,null,false);
    }
    public void getAlert(Context context, String title, String message, Boolean resultVisible) {
        getAlert(context,title,message,null,null,resultVisible);
    }
    public void getAlert(Context context, String title, String message, String url) {
        getAlert(context,title,message,null,url,false);
    }
    public void getAlert(Context context, String title, String[] table_list) {
        getAlert(context,title,null,table_list,null,false);
    }
    public void getAlert(Context context, final String title,  final String message, String[] table_list, final String url, Boolean reportVisible) {


        Intent     intent = new Intent(context, FloatingAlertService.class);
        intent.putExtra ("Title",title);
        intent.putExtra ("message",message);
        intent.putExtra ("code",code);
        intent.putExtra ("table_list",table_list);
        intent.putExtra ("url",url);

        code = "";
       context. startService(intent);

    }


}

