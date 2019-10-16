package async;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.ui.CBOReportView;

import services.ServiceHandler;
import services.TaskListener;

/**
 * Created by Akshit on 3/27/2015.
 */
public class Che_RptTask extends AsyncTask<String , String, String> {

    private ServiceHandler serviceHandler;
    private TaskListener<String> stringTaskListener;
    public Context context;
    public CBO_DB_Helper db_helper;

    public Che_RptTask(AppCompatActivity context){
        this.context = context;
    }


    @Override
    protected void onPreExecute() {
       if(stringTaskListener != null){
        stringTaskListener.onStarted();
    }
    }

    @Override
    protected void onPostExecute(String result) {

        if(stringTaskListener != null){
        stringTaskListener.onFinished(result);
    }}

    @Override
    protected String doInBackground(String... params) {

        String response = null;
        try {
            serviceHandler = new ServiceHandler(context);
            db_helper = new CBO_DB_Helper(context);

            response = serviceHandler.getResponse_CHEMIST_VIEW(db_helper.getCompanyCode(), "" + CBOReportView.lastPaId, params[0]);
            Log.e("Response of ChemistData", "" + response);
        }catch (Exception e){
            return "ERROR apk "+e;
        }

        return response;
    }


    public void setListener(TaskListener<String> listener){

        stringTaskListener =listener;

    }
}
