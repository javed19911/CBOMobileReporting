package async;

import android.content.Context;
import android.os.AsyncTask;

import androidx.appcompat.app.AppCompatActivity;

import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;

import services.ServiceHandler;
import services.TaskListener;

/**
 * Created by Kuldeep.Dwivedi on 2/1/2015.
 */
public class CommitRootTask extends AsyncTask<String,String,String>{
    private ServiceHandler mServiceHandler;
    public Context mContext;
    private TaskListener<String> mListener;
    CBO_DB_Helper cbohelp;

    public CommitRootTask(AppCompatActivity context){
        mContext = context;
    }
    /*(String sCompanyFolder,String iPA_ID,String sDCR_DATE,String sSTATION,
    String iTOTAL_DR,String iIN_TIME,String iOUT_TIME,String sM_E1,String sM_E2,String sM_E3,String iIN_TIME1,String iIN_TIME2
    ,String iIN_TIME3,String iOUT_TIME1,String iOUT_TIME2,String iOUT_TIME3,String iWORK_WITH1,String iWORK_WITH2,String iWORK_WITH3
    ,String sDA_TYPE,String iDISTANCE_ID,String sREMARK,String sLOC1,String iRETID){*/

    @Override
    protected String doInBackground(String... params) {
        String response=null;
            cbohelp=new CBO_DB_Helper(mContext);
        /*response=mServiceHandler.getResponse_DCR_COMMIT_ROUTE(cbohelp.getCompanyCode(),params[0],params[1],params[2],"1","99","0.0","","","","0.0","0.0","0.0","0.0","0.0","0.0",
                params[3],params[4],params[5],params[6],params[7],"",params[8],"");*/
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
