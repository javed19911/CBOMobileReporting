package async;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;

import services.ServiceHandler;
import services.TaskListener;

/**
 * Created by Kuldeep.Dwivedi on 2/22/2015.
 */
public class RptShowTask extends AsyncTask<String,String,String> {
    public Context mContext;
    private TaskListener<String> mListener;
    CBO_DB_Helper cbohelp;

    public RptShowTask(AppCompatActivity context){
        mContext = context;
    }

    @Override
    protected String doInBackground(String... params) {
        String response=null;
        try {
            ServiceHandler mServiceHandler = new ServiceHandler(mContext);
            cbohelp = new CBO_DB_Helper(mContext);
            response = mServiceHandler.DCRLISTWITHEXPENSEGRID(cbohelp.getCompanyCode(), params[0], params[1], params[2], params[3], params[4]);
            Log.d("Response: ", "> " + response);
        }catch (Exception e){
            return "ERROR apk "+e;
        }
        return response;
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

    public void setListener(TaskListener<String> listener)
    {
        mListener = listener;
    }
}
