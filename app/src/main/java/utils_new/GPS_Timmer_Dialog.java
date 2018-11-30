package utils_new;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.emp_tracking.GPSTracker;
import com.cbo.cbomobilereporting.emp_tracking.MyCustomMethod;
import com.cbo.cbomobilereporting.ui_new.DcrmenuInGrid;

/**
 * Created by pc24 on 28/11/2017.
 */

public class GPS_Timmer_Dialog extends AlertDialog {
    private ProgressBar progBar;
    RelativeLayout progessBG;
    TextView progesstxt,progessmsg;
    Custom_Variables_And_Method customVariablesAndMethod;
    Context context;
    String Msg="Please Wait....";
    Handler h1;
    Integer response_code;
    Integer delay;

    public GPS_Timmer_Dialog(@NonNull Context context,String Msg) {
        super(context);
        this.context = context;
        this.Msg=Msg;
    }
    public GPS_Timmer_Dialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public GPS_Timmer_Dialog(@NonNull Context context, Handler hh,String Msg,Integer response_code) {
        super(context);
        this.context = context;
        h1=hh;
        this.response_code=response_code;
        this.Msg=Msg;
    }

    @Override
    public void show() {
        super.show();
        double t1 = Double.parseDouble(Custom_Variables_And_Method.GPS_STATE_CHANGED_TIME);
        double t2 = Double.parseDouble(customVariablesAndMethod.get_currentTimeStamp());
        if (t1==0.0) {
            Custom_Variables_And_Method.GPS_STATE_CHANGED_TIME = "" + t2;
            t1 = t2;
        }
        delay=Integer.parseInt(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"CALLWAITINGTIME","0"));
        Boolean IsGPS_GPRS_ON=customVariablesAndMethod.IsGPS_GRPS_ON(context);
        if (IsGPS_GPRS_ON && t2-t1 <= 2*delay*1000 && Custom_Variables_And_Method.GPS_STATE_CHANGED){
            Custom_Variables_And_Method.GPS_STATE_CHANGED=false;
            new ShowProgess().execute();
        }else if (IsGPS_GPRS_ON){
            threadMsg("");
            dismiss();
        }else{
            customVariablesAndMethod.msgBox(context,"Please Swicth ON your GPS");
            dismiss();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gps_timmer_dialog);
        setCancelable(false);
        getWindow().setBackgroundDrawable(null);
        progBar= (ProgressBar)  findViewById(R.id.progressBar);
        progessBG= (RelativeLayout)  findViewById(R.id.progessbg);
        progesstxt= (TextView) findViewById(R.id.progesstxt);
        progessmsg= (TextView)  findViewById(R.id.progessmsg);
        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
    }

    private void threadMsg(String Msg) {
        Message msgObj = h1.obtainMessage(response_code);
        Bundle b = new Bundle();
        b.putString("Error",Msg);
        msgObj.setData(b);
        h1.sendMessage(msgObj);
    }


    private class ShowProgess extends AsyncTask<String, Integer, String> {


        @Override
        protected String doInBackground(String... params) {
            for (int i=0;i<delay;i++){
                try {
                    publishProgress(i);
                    Thread.sleep(1000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            dismiss();
            if (h1 !=null){
                threadMsg("Done");
            }
        }

        @Override
        protected void onPreExecute() {

            progBar.setVisibility(View.VISIBLE);
            progessBG.setVisibility(View.VISIBLE);
            progBar.setMax(delay);
            progessmsg.setText(Msg);

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progBar.setProgress(values[0]);
            int x=delay-values[0];
            progesstxt.setText(""+x);

        }
    }
}
