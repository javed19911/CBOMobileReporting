package async;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;

import services.ServiceHandler;
import services.TaskListener;

/**
 * Created by Akshit on 3/10/2015.
 */
public class DrvisitedListTask extends AsyncTask<String,String,String> {


    private ServiceHandler mServiceHandler;
    public Context mContext;
    private TaskListener<String> mListener;
    CBO_DB_Helper cbohelp;

    public DrvisitedListTask(AppCompatActivity context){
        mContext = context;
    }

    @Override
    protected String doInBackground(String... params) {
        String response=null;
        try {
            mServiceHandler = new ServiceHandler(mContext);
            cbohelp = new CBO_DB_Helper(mContext);
            response = mServiceHandler.DOCTOR_VISIT(cbohelp.getCompanyCode(), params[0], params[1], params[2]);
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

    public void setListener(TaskListener<String> listener) {
        mListener = listener;
    }
}
