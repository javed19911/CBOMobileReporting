package com.cbo.cbomobilereporting.ui_new.approval_activities.Remainder;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import com.cbo.cbomobilereporting.MyCustumApplication;
import utils_new.AppAlert;


public class FloatingRemainderApproval extends Service implements IApprovalRemainder,View.OnTouchListener {
    private WindowManager mWindowManager;
    private View mFloatingView;
    FloatingRemainderApproval context;

    VMApprovalRemainder vmApprovalRemainder;
    RecyclerView listView;
    ApprovalRemainderAdaptor approvalRemainderAdaptor;
    TextView headerText;

    View expandedView;
    View collapsedView;

    private int initialX;
    private int initialY;
    private float initialTouchX;
    private float initialTouchY;

    WindowManager.LayoutParams params;

    public FloatingRemainderApproval() {
        super();
    }


    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    /*@Override
    protected void onHandleIntent(@Nullable Intent intent) {
        updateUI(intent);
    }*/

    @Override
    public int onStartCommand(Intent intent,int flag,int startId) {
        super.onStartCommand(intent,flag, startId);

        updateUI(intent);

        return START_STICKY;
    }


    private void updateUI(Intent intent){
        try {
            headerText.setText(intent.getStringExtra("Title"));
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("response"));
            JSONArray jsonArray = jsonObject.getJSONArray("Tables");
            Bundle b = new Bundle();

            ArrayList<Integer> tables = new ArrayList<>();
            tables.add(0);
            tables.add(1);
            for(int i:tables){
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                JSONArray jsonArray1 = jsonObject1.getJSONArray("Tables"+i);
                // Log.d("MYAPP", "objects are: " + jsonArray1.toString());

                b.putString("Tables"+i,jsonArray1.toString());

            }

            vmApprovalRemainder.parser_ApprovalRemainder(b);
        } catch (Exception e) {
            headerText.setText("Approval Remainder");
            vmApprovalRemainder.getApprovalRemainders(context);
        }

    }


    /*@Override
    protected void onHandleIntent(@Nullable Intent intent) {
        //company_name=intent.getStringExtra("Name");
    }
*/
    @Override
    public void onCreate() {
        super.onCreate();

        context=FloatingRemainderApproval.this;
        //Inflate the floating view layout we created
        //mFloatingView = LayoutInflater.from(this).inflate(R.layout.activity_remainder_floating, null);
        LayoutInflater li = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        mFloatingView = li.inflate(R.layout.activity_remainder_floating, null);

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
        params.gravity = Gravity.TOP | Gravity.LEFT;        //Initially view will be added to top-left corner
        params.x = 0;
        params.y = 100;

        //Add the view to the window
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mFloatingView, params);

        listView = mFloatingView.findViewById(R.id.listView_rem_approval) ;
        headerText = mFloatingView.findViewById(R.id.hadder_text);

        //The root element of the collapsed view layout
        collapsedView = mFloatingView.findViewById(R.id.collapse_view);
        //The root element of the expanded view layout
        expandedView = mFloatingView.findViewById(R.id.main);

        //Set the close button
        Button backBtn = mFloatingView.findViewById(R.id.back);
        //backBtn.setVisibility(View.GONE);
        Button closeButton =  mFloatingView.findViewById(R.id.cancel);
        //closeButton.setVisibility(View.VISIBLE);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //close the service and remove the from from the window
                stopSelf();
            }
        });

        Button later =  mFloatingView.findViewById(R.id.later);
        later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collapsedView.setVisibility(View.VISIBLE);
                expandedView.setVisibility(View.GONE);
            }
        });
        collapsedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collapsedView.setVisibility(View.GONE);
                expandedView.setVisibility(View.VISIBLE);
            }
        });
        vmApprovalRemainder = new VMApprovalRemainder();
        vmApprovalRemainder.setListener(this);

        //Drag and move floating view using user's touch action.

        mFloatingView.findViewById(R.id.root_container).setOnTouchListener(this);
        collapsedView.setOnTouchListener(this);
       /* mFloatingView.findViewById(R.id.root_container).setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;


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
        });*/
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
    public void onListUpdated(ArrayList<mApprovalRemainder> mApprovalRemainders) {
        approvalRemainderAdaptor =
                new ApprovalRemainderAdaptor(context, mApprovalRemainders, (view, position, isLongClick) -> {
                    collapsedView.setVisibility(View.VISIBLE);
                    expandedView.setVisibility(View.GONE);
                    /*Intent i = new Intent(context, CustomWebView.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra("A_TP", mApprovalRemainders.get(position).getADD_URL());
                    i.putExtra("Title",  mApprovalRemainders.get(position).getPARICULARS());
                    context.startActivity(i);*/
                    MyCustumApplication.getInstance().LoadURL(mApprovalRemainders.get(position).getPARICULARS(),mApprovalRemainders.get(position).getADD_URL());
                });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        listView.setLayoutManager(mLayoutManager);
        listView.setItemAnimator(new DefaultItemAnimator());
        listView.setAdapter(approvalRemainderAdaptor);
        ViewCompat.setNestedScrollingEnabled(
                listView, false);
    }

    @Override
    public void onError(String Title, String error) {
        AppAlert.getInstance().getAlert(context,Title,error);
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