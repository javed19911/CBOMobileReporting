package com.cbo.cbomobilereporting.ui_new.approval_activities.BackgroundNotification;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;
import com.uenics.javed.CBOLibrary.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import locationpkg.SetGPS_Setting;
import utils_new.AppAlert;
import utils_new.Custom_Variables_And_Method;
import utils_new.Service_Call_From_Multiple_Classes;

public class FloatingAlertService extends Service implements View.OnTouchListener {
    private WindowManager mWindowManager;
    private View mFloatingView;
    FloatingAlertService context;
    Custom_Variables_And_Method customVariablesAndMethod;
    TextView headerText;

    String title,message,code,url;
    String[] table_list;

    View expandedView;
    View collapsedView;

    private int initialX;
    private int initialY;
    private float initialTouchX;
    private float initialTouchY;

    WindowManager.LayoutParams params;

    public FloatingAlertService() {
        super();
    }


    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public int onStartCommand(Intent intent,int flag,int startId) {
        super.onStartCommand(intent,flag, startId);

        title = intent.getStringExtra("Title");
        message = intent.getStringExtra("message");
        code = intent.getStringExtra("code");
        //table_list = intent.get("table_list");
        url = intent.getStringExtra("url");

        String msg2=message;
        msg2=msg2.replace("@","#_");
        msg2=msg2.replace("^","@");
        String[] msglist=msg2.split("@");

        String msg3="";
        for (String aMsglist : msglist) {
            String msg1 = aMsglist;
            msg1 = msg1.replace("#_", "@");
            msg3=msg3.concat(msg1 + "\n");
        }
        if (msglist.length>1) {
            table_list = msglist;
            message = null;
        }else{
            table_list = null;
        }
        updateUI();
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        context=FloatingAlertService.this;
        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
        //Inflate the floating view layout we created
        //mFloatingView = LayoutInflater.from(this).inflate(R.layout.activity_remainder_floating, null);
        LayoutInflater li = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        mFloatingView = li.inflate(R.layout.alert_view, null);

        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }

        //Add the view to the window.
        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSPARENT);

        //Specify the view position
        params.gravity = Gravity.CENTER_VERTICAL;        //Initially view will be added to top-left corner
        params.x = 0;
        params.y = 100;

        //Add the view to the window
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mFloatingView, params);






    }

    private void updateUI(){
        final TextView Alert_title= (TextView) mFloatingView.findViewById(R.id.title);

        final TextView Alert_message= (TextView) mFloatingView.findViewById(R.id.message);
        final TableLayout Alert_message_list= (TableLayout) mFloatingView.findViewById(R.id.table_view);
        final Button Alert_Positive= (Button) mFloatingView.findViewById(R.id.positive);
        Alert_title.setText(title);

        final TextView pa_id_txt= (TextView) mFloatingView.findViewById(R.id.PA_ID);
        pa_id_txt.setText(""+ Custom_Variables_And_Method.PA_ID);

        Alert_message.setText(message);


        if (table_list==null ) {
            Alert_message.setText(message);
            Alert_message_list.setVisibility(View.GONE);
        }else{
            Alert_message.setVisibility(View.GONE);
            Alert_message_list.setVisibility(View.VISIBLE);
            TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 1f);
            TableRow.LayoutParams params1 = new TableRow.LayoutParams(0, 1, 1f);
            Alert_message_list.removeAllViews();
            for (int i = 0; i < table_list.length; i++) {
                TableRow tbrow = new TableRow(context);
                if ( !table_list[i].contains(":")) {
                    TextView t1v = new TextView(context);
                    t1v.setText(table_list[i].replace("\n",""));
                    t1v.setPadding(15, 10, 15, 10);
                    t1v.setBackgroundColor(0xff5477cf);
                    t1v.setTextColor(Color.WHITE);
                    t1v.setTextSize(16);
                    t1v.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                    t1v.setLayoutParams(params);
                    tbrow.addView(t1v);
                }else{
                    TextView t1v = new TextView(context);
                    t1v.setText(table_list[i]);
                    t1v.setPadding(15, 5, 15, 0);
                    t1v.setTextColor(Color.BLACK);
                    t1v.setLayoutParams(params);
                    t1v.setTypeface(Typeface.MONOSPACE, Typeface.NORMAL);
                    tbrow.addView(t1v);
                }
               /* TextView t2v = new TextView(context);
                t2v.setText(table_list[i]);
                t2v.setPadding(5, 5, 5, 0);
                t2v.setTextColor(Color.BLACK);
                t2v.setGravity(Gravity.CENTER);
                tbrow.addView(t2v);*/
                TableRow tbrow1 = new TableRow(context);
                TextView t3v = new TextView(context);
                t3v.setPadding(15, 1, 15, 0);
                t3v.setLayoutParams(params1);
                t3v.setBackgroundColor(0xff125688);
                tbrow1.addView(t3v);
                Alert_message_list.addView(tbrow);
                Alert_message_list.addView(tbrow1);
            }
        }



      /*  Alert_Positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (url!=null && !url.isEmpty()){
                   *//* Intent i = new Intent(context, CustomWebView.class);
                    i.putExtra("A_TP", url);
                    i.putExtra("Title", title);
                    context.startActivity(i);*//*
                    MyCustumApplication.getInstance().LoadURL(title,url);
                }
                dialog.dismiss();
            }
        });*/
        Alert_Positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SetGPS_Setting.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra ("code",code);
                startActivity(intent);
                stopSelf();
            }
        });
    }



    /**
     * Detect if the floating view is collapsed or expanded.
     *
     * @return true if the floating view is collapsed.
     */
    private boolean isViewCollapsed() {
        return mFloatingView == null || mFloatingView.findViewById(R.id.collapse_view).getVisibility() == View.VISIBLE;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatingView != null) mWindowManager.removeView(mFloatingView);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                //remember the initial position.
                initialX = params.x;
                initialY = params.y;

                //get the touch location
                initialTouchX = event.getRawX();
                initialTouchY = event.getRawY();
                return true;
            case MotionEvent.ACTION_UP:
                int Xdiff = (int) (event.getRawX() - initialTouchX);
                int Ydiff = (int) (event.getRawY() - initialTouchY);


                //The check for Xdiff <10 && YDiff< 10 because sometime elements moves a little while clicking.
                //So that is click event.
                if (Xdiff < 10 && Ydiff < 10) {
                    if (isViewCollapsed()) {
                        //When user clicks on the image view of the collapsed layout,
                        //visibility of the collapsed layout will be changed to "View.GONE"
                        //and expanded view will become visible.
                        collapsedView.setVisibility(View.GONE);
                        expandedView.setVisibility(View.VISIBLE);
                    }
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                //Calculate the X and Y coordinates of the view.
                params.x = initialX + (int) (event.getRawX() - initialTouchX);
                params.y = initialY + (int) (event.getRawY() - initialTouchY);


                //Update the layout with new X & Y coordinate
                mWindowManager.updateViewLayout(mFloatingView, params);
                return true;
        }
        return false;
    }
}