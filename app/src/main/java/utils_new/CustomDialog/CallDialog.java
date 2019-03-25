package utils_new.CustomDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.ui_new.transaction_activities.Doctor_registration_GPS;
import com.uenics.javed.CBOLibrary.Response;

import java.util.ArrayList;

import locationpkg.Const;
import utils.adapterutils.SpinAdapter;
import utils.adapterutils.SpinAdapter_new;
import utils.adapterutils.SpinnerModel;
import utils.model.DropDownModel;
import utils_new.AppAlert;
import utils_new.Custom_Variables_And_Method;
import utils_new.Report_Registration;
import utils_new.Service_Call_From_Multiple_Classes;

/**
 * Created by cboios on 10/03/19.
 */

public class CallDialog {
    AppCompatActivity context;
    private ArrayList<SpinnerModel> List;
    private ArrayList<SpinnerModel> ListCopy;
    private AlertDialog myalertDialog = null;
    int showRegistrtion=1;

    private OnItemClickListener Listener = null;

    public interface OnItemClickListener {
        void ItemSelected(SpinnerModel item);
        void onListRefressed();
    }

    public CallDialog(@NonNull AppCompatActivity context, ArrayList<SpinnerModel> List, OnItemClickListener Listener) {
        this.context = context;
        this.List = List;
        ListCopy = (ArrayList<SpinnerModel>) this.List.clone();
        this.Listener = Listener;
        showRegistrtion=Integer.parseInt(Custom_Variables_And_Method.getInstance().getDataFrom_FMCG_PREFRENCE(context,"IsBackDate","1"));


    }

    public void show() {



        AlertDialog.Builder myDialog = new AlertDialog.Builder(context);
        final EditText editText = new EditText(context);
        final ListView listview=new ListView(context);
        LinearLayout layout = new LinearLayout(context);
        editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.search, 0,  R.mipmap.ref3, 0);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(editText);
        layout.addView(listview);
        myDialog.setView(layout);
        SpinAdapter_new arrayAdapter=new SpinAdapter_new(context, R.layout.spin_row, ListCopy,showRegistrtion);
        listview.setAdapter(arrayAdapter);
        listview.setOnItemClickListener((parent, view, position, id) -> {

            SpinnerModel model = ListCopy.get(position);
            myalertDialog.dismiss();

            if (!model.getAPP_PENDING_YN().equalsIgnoreCase("0")){
//                drname.setText("---Select---");
//                dr_id="";
//                doc_name="";
                if (!Custom_Variables_And_Method.getInstance().IsGPS_GRPS_ON(context)) {
                    //customVariablesAndMethod.Connect_to_Internet_Msg(context);
                    AppAlert.getInstance().setNagativeTxt("Cancel").setPositiveTxt("Check").DecisionAlert(context,
                            "Approval Pending !!!", Custom_Variables_And_Method.getInstance().getDataFrom_FMCG_PREFRENCE(context,
                                    "DCRDRADDAREA_APP_MSG","Your Additional Area Approval is Pending... \nYou Additional Area must be approved first !!!\n" +
                                            "Please contact your Head-Office for APPROVAL"),
                            new AppAlert.OnClickListener() {
                                @Override
                                public void onPositiveClicked(View item, String result) {
                                    new Service_Call_From_Multiple_Classes().CheckIfCallsUnlocked(context,"ADDAREA");
                                }

                                @Override
                                public void onNegativeClicked(View item, String result) {

                                }
                            });

                } else {
                    new Service_Call_From_Multiple_Classes().CheckIfCallsUnlocked(context,"ADDAREA");
                }
            }else if (((TextView) view.findViewById(R.id.distance)).getText().toString().equals("Registration pending...")){
                if (!Custom_Variables_And_Method.getInstance().IsGPS_GRPS_ON(context)) {
                    Custom_Variables_And_Method.getInstance().Connect_to_Internet_Msg(context);
//                    drname.setText("---Select---");
//                    dr_id="";
//                    doc_name="";

                } else {
                    Intent intent = new Intent(context, Doctor_registration_GPS.class);
                    intent.putExtra("id",model.getId());
                    intent.putExtra("name",model.getName());
                    intent.putExtra("type","D");
                    context.startActivity(intent);
                    context.finish();
                }
            }else if(((TextView) view.findViewById(R.id.distance)).getText().toString().contains("Km Away")) {
                //getAlert(context,"Not In Range","You are "+((TextView) view.findViewById(R.id.distance)).getText().toString()+" from "+doc_name,true);

                FragmentManager fm = context.getSupportFragmentManager();
                Report_Registration alertdFragment = new Report_Registration();
                String km=((TextView) view.findViewById(R.id.distance)).getText().toString();
                alertdFragment.setAlertLocation(model.getLoc(),model.getLoc2(),model.getLoc3());
                alertdFragment.setAlertData("Not In Range","You are "+km+" from "+model.getName());
                alertdFragment.show(fm, "Alert Dialog Fragment");
                km=km.replace("Km Away","").trim();

//                dr_id_reg = dr_id;
//                dr_id_index = "";
//                dr_name_reg=doc_name;
//                if(model.getLoc2().equals("") && Float.parseFloat(km)< Float.parseFloat(Custom_Variables_And_Method.getInstance().getDataFrom_FMCG_PREFRENCE(context,"RE_REG_KM","5"))){
//                    dr_id_index = "2";
//                }else if(model.getLoc3().equals("") && Float.parseFloat(km)< Float.parseFloat(Custom_Variables_And_Method.getInstance().getDataFrom_FMCG_PREFRENCE(context,"RE_REG_KM","5"))){
//                    dr_id_index = "3";
//                }

//                drname.setText("---Select---");
//                dr_id="";
//                doc_name="";


            }else if( Integer.parseInt(model.getFREQ()) != 0 && Integer.parseInt(model.getFREQ()) <= Integer.parseInt(model.getNO_VISITED()) ) {
                Custom_Variables_And_Method.getInstance().getAlert(context,"Visit Freq. Exceeded",("For "+model.getName() +"@ Allowed Freq. : " + model.getFREQ() + "@ Visited       : "+ model.getNO_VISITED()).split("@"));
//                drname.setText("---Select---");
//                dr_id="";
//                doc_name="";

            }else{
                if (Listener != null){
                    Listener.ItemSelected(ListCopy.get(position));
                }
            }




        });

        editText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s,
                                          int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int textlength = editText.getText().length();
                ListCopy.clear();
                for (int i = 0; i < List.size(); i++) {
                    if (textlength <= List.get(i).getName().length()) {

                        if (List.get(i).getName().toLowerCase().contains(editText.getText().toString().toLowerCase().trim())) {
                            ListCopy.add(List.get(i));
                        }
                    }
                }
                try {
                    listview.setAdapter(new SpinAdapter_new(context, R.layout.spin_row, ListCopy,showRegistrtion));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (editText.getRight() - editText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        myalertDialog.dismiss();
                        if(!Custom_Variables_And_Method.getInstance().checkIfCallLocationValid(context,true,false)) {
                            Custom_Variables_And_Method.getInstance().msgBox(context,"Verifing Your Location");
                            LocalBroadcastManager.getInstance(context).registerReceiver(mLocationUpdated,
                                    new IntentFilter(Const.INTENT_FILTER_LOCATION_UPDATE_AVAILABLE));

                        }else{
                            //new Service_Call_From_Multiple_Classes().DownloadAll(context, mHandler, MESSAGE_INTERNET_DCRCOMMIT_DOWNLOADALL);
                            new Service_Call_From_Multiple_Classes().DownloadAll(context, new Response() {
                                @Override
                                public void onSuccess(Bundle bundle) {
                                    if (Listener != null){
                                        Listener.onListRefressed();
                                    }
                                }

                                @Override
                                public void onError(String s, String s1) {
                                    AppAlert.getInstance().getAlert(context,s,s1);
                                }
                            });
                        }

                        Vibrator vbr = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                        vbr.vibrate(100);
                        return true;
                    }
                }
                return false;
            }
        });
        myalertDialog=myDialog.show();

    }


    private BroadcastReceiver mLocationUpdated = new BroadcastReceiver() {
        @Override
        public void onReceive(Context contex, Intent intent) {
            Location location = intent.getParcelableExtra(Const.LBM_EVENT_LOCATION_UPDATE);

                new Service_Call_From_Multiple_Classes().DownloadAll(context, new Response() {
                    @Override
                    public void onSuccess(Bundle bundle) {
                        if (Listener != null){
                            Listener.onListRefressed();
                        }
                    }

                    @Override
                    public void onError(String s, String s1) {
                        AppAlert.getInstance().getAlert(context,s,s1);
                    }
                });

            LocalBroadcastManager.getInstance(context).unregisterReceiver(mLocationUpdated);

        }
    };

}
