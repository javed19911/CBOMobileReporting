package async;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.ui.CBOReportView;

import services.ServiceHandler;
import services.TaskListener;

/**
 * Created by Akshit on 3/26/2015.
 */
public class NlcDistributorTask extends AsyncTask<String,String,String > {
    private ServiceHandler serviceHandler;
    public Context context;
    private TaskListener<String> mListener;
    CBO_DB_Helper cboDbHelper;

    public NlcDistributorTask(Activity context){
        this.context =context;

    }



    @Override
    protected void onPreExecute() {
        if (mListener != null) {
            mListener.onStarted();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if (mListener != null) {
            mListener.onFinished(result);
        }
    }

    @Override
    protected String doInBackground(String... params) {

       String response = null;
        try {
            serviceHandler = new ServiceHandler(context);
            cboDbHelper = new CBO_DB_Helper(context);

            response = serviceHandler.getResponse_NLCDISTRIBUTOR_VIEW(cboDbHelper.getCompanyCode(), "" + CBOReportView.lastPaId, params[0]);

            Log.e("Response: ", "> " + response);
        }catch (Exception e){
            return "ERROR apk "+e;
        }

        return response;
    }



    public void setListner(TaskListener<String> listner){
        mListener =listner;
    }
}
