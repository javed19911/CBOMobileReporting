package async;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import services.ServiceHandler;
import services.TaskListener;
import utils.adapterutils.RootModel;

/**
 * Created by kuldeep.Dwivedi on 2/1/2015.
 */
public class CBORootTask extends AsyncTask<String,String,String>{
    private ServiceHandler mServiceHandler;
    public Context mContext;
    private TaskListener<String> mListener;

    CBO_DB_Helper cbohelp;

    public CBORootTask(Activity context){
        mContext = context;
    }


    @Override
    protected String doInBackground(String... params) {
        String response=null;
        try {
            mServiceHandler = new ServiceHandler(mContext);
            cbohelp = new CBO_DB_Helper(mContext);
            response = mServiceHandler.getResponse_DCRAREADDL_ROUTE(cbohelp.getCompanyCode(), params[0], params[1], params[2], params[3], "0", params[4], params[5], params[6]);
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

    public ArrayList<RootModel> setDataToRootList(String result,ArrayList<RootModel>list){
        try{
            list=new ArrayList<RootModel>();
            JSONObject jsonObject = new JSONObject(result);
            JSONArray rows = jsonObject.getJSONArray("Tables0");
            for (int i = 0; i < rows.length(); i++) {
                JSONObject c = rows.getJSONObject(i);
                list.add(new RootModel(c.getString("ROUTE_NAME"),c.getString("DISTANCE_ID")));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }
}
