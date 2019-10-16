package async;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;

import services.ServiceHandler;
import services.TaskListener;

/**
 * Created by Akshit on 3/20/2015.
 */
public class TpReportTask extends AsyncTask<String,String,String>
{

    private ServiceHandler serviceHandler;
    public Context mContext;
    private TaskListener<String> stringTaskListener;
    CBO_DB_Helper cboDbHelper;

    public TpReportTask(AppCompatActivity context){ mContext = context;}

    @Override
    protected String doInBackground(String... params) {
         String response = null;
        try {
            serviceHandler = new ServiceHandler(mContext);
            cboDbHelper = new CBO_DB_Helper(mContext);

            response = serviceHandler.getResponse_TP_VIEW(cboDbHelper.getCompanyCode(), params[0], params[1]);
           /* response = serviceHandler.getResponse_TP_VIEW("NOVO", "785", params[1]);*/

            Log.e("T.P. Report Response", "" + response);
        }catch (Exception e){
            return "ERROR apk "+e;
        }
        return response;
    }
    @Override
    protected void onPreExecute(){

        if(stringTaskListener != null){
            stringTaskListener.onStarted();
        }

    }
    @Override
    protected void onPostExecute(String result){
       if (stringTaskListener!= null){
           stringTaskListener.onFinished(result);
       }

    }
    public void setListner(TaskListener<String> listner){
        stringTaskListener =listner;
    }

}
