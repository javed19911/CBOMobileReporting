package com.cbo.cbomobilereporting.ui_new.dcr_activities;

import android.content.Context;

import utils.adapterutils.SpinnerModel;

/**
 * Created by cboios on 10/01/19.
 */

public class DCRCallDialog {


    Context context;
    String Msg="Please Wait....",DocType="D";
    Integer response_code =0;
    IDCRCall listener = null;

    interface IDCRCall{
        void onSelected(SpinnerModel spinnerModel);
    }

    public DCRCallDialog(Context context,String DocType, String Msg, Integer response_code, IDCRCall listener) {

        this.context = context;
        this.DocType = DocType;
        this.listener=listener;
        this.response_code=response_code;
        this.Msg=Msg;
    }
    public void show() {



    }
}
