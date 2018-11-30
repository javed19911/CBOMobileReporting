package async;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;

import services.ServiceHandler;
import services.TaskListener;
import utils.MyConnection;
import utils_new.Custom_Variables_And_Method;

/**
 * Created by Akshit on 4/16/2015.
 */
public class LeaveRequestGridTask extends AsyncTask<String,String,String> {


    private ServiceHandler serviceHandler;
    public Context context;
    private TaskListener<String> mTaskListener;
    private CBO_DB_Helper cbohelp;


    public LeaveRequestGridTask(Context context){

        this.context = context;
    }



    @Override
    protected String doInBackground(String... strings) {
        serviceHandler = new ServiceHandler(context);
        cbohelp = new CBO_DB_Helper(context);
       String response = null;
        try {
            response = serviceHandler.getResponse_LeaveRequestGrid(cbohelp.getCompanyCode(), "" + 0, "" + Custom_Variables_And_Method.PA_ID);
        }catch (Exception e){
            return "ERROR apk "+e;
        }
       // Log.e("LeaveRequstGridTask.....",response);



        return response;



    }

    @Override
    protected void onPreExecute() {
        if (mTaskListener != null){

       mTaskListener.onStarted();
    }}

    @Override
    protected void onPostExecute(String result) {

        if(mTaskListener != null){
         mTaskListener.onFinished(result);
        }


    }

    public void setListner(TaskListener<String> listner){
        mTaskListener = listner;
    }

}
