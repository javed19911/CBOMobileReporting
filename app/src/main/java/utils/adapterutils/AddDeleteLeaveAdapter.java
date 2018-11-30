package utils.adapterutils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.ui.Leave_Request;
import com.cbo.cbomobilereporting.ui.LoginFake;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import services.ServiceHandler;
import utils_new.Custom_Variables_And_Method;


public class AddDeleteLeaveAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    Custom_Variables_And_Method customVariablesAndMethod;
    String leaveIdFinal;
    //public ViewClickListener mViewClickListener;
    ArrayList<AddDelLeaveModel> Rptdata = new ArrayList<AddDelLeaveModel>();

    public AddDeleteLeaveAdapter(Context mContext, ArrayList<AddDelLeaveModel> data) {
        this.context = mContext;
        layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.Rptdata = data;
        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return Rptdata.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolderAddDel holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.add_del_leave_new, null);
            holder = new ViewHolderAddDel();
            holder.leaveid = (TextView) convertView.findViewById(R.id.leave_id1);
            holder.approval = (TextView) convertView.findViewById(R.id.leave_status1);
            holder.purpose = (TextView) convertView.findViewById(R.id.purpose);
            holder.todate = (TextView) convertView.findViewById(R.id.todate);
            holder.fdate = (TextView) convertView.findViewById(R.id.fromdate);
            holder.days = (TextView) convertView.findViewById(R.id.days);
            holder.docDate = (TextView) convertView.findViewById(R.id.doc_date);
            holder.docNo = (TextView) convertView.findViewById(R.id.doc_no);
            holder.edit = (Button) convertView.findViewById(R.id.leaveEdit);
            holder.delete = (Button) convertView.findViewById(R.id.leaveDelete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolderAddDel) convertView.getTag();
        }
        holder.leaveid.setText(Rptdata.get(position).getId());


        holder.approval.setText(Rptdata.get(position).getApproval());
        holder.purpose.setText(Rptdata.get(position).getPurpose());
        holder.todate.setText(Rptdata.get(position).gettDate());
        holder.fdate.setText(Rptdata.get(position).getfDate());
        holder.days.setText(Rptdata.get(position).getDays());
        holder.docDate.setText(Rptdata.get(position).getDocDate());
        holder.docNo.setText(Rptdata.get(position).getDocNo());

        holder.delete.setTag(position);

        if (Rptdata.get(position).getApproval().toString().isEmpty()) {
            holder.delete.setVisibility(View.VISIBLE);
            holder.edit.setVisibility(View.VISIBLE);
        } else {

            holder.delete.setVisibility(View.GONE);
            holder.edit.setVisibility(View.GONE);

        }



        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {


                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);

                builder1.setTitle("Alert Dialog");
                builder1.setMessage(" Do you Really want to delete..");
                builder1.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        leaveIdFinal = Rptdata.get(position).getId();
                        new DeleteLeave().execute();

                    }
                });
                builder1.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

                    }
                });
                builder1.show();


            }
        });


        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), Leave_Request.class);
                i.putExtra("leaveIdExtra", Rptdata.get(position).getId());

                v.getContext().startActivity(i);
            }
        });


        return convertView;
    }


    public class DeleteLeave extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;
        CBO_DB_Helper cboDbHelper;
        ServiceHandler serviceHandler;
        String deleteStatus;


        @Override
        protected String doInBackground(String... strings) {

            String result;
            serviceHandler = new ServiceHandler(context);
            cboDbHelper = new CBO_DB_Helper(context);
            try {
                result = serviceHandler.get_Response_DeleteAddLeave(cboDbHelper.getCompanyCode(), leaveIdFinal);
            }catch (Exception e){
                return "Error apk "+e;
            }


            return result;
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("CBO");
            progressDialog.setMessage("Processing....");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if ((s == null) || (s.contains("ERROR"))) {
                progressDialog.dismiss();
                customVariablesAndMethod.msgBox(context,"Sorry No Result Found");


            } else {
                try {


                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray rows = jsonObject.getJSONArray("Tables0");
                    JSONObject c = rows.getJSONObject(0);
                    deleteStatus = c.getString("DCRID");


                    if (deleteStatus.equals("SAVED")) {

                        customVariablesAndMethod.msgBox(context, "Leave Successfully Deleted..");

                        Intent intent = new Intent(context, LoginFake.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);

                    } else {
                        customVariablesAndMethod.msgBox(context,"Server Not responding" + "\n" + "Please try after Some time");
                    }
                    progressDialog.dismiss();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }


    }
}

class ViewHolderAddDel {
    TextView leaveid;
    TextView approval;
    TextView purpose;
    TextView todate;
    TextView fdate;
    TextView days;
    TextView docDate;
    TextView docNo;
    Button delete;
    Button edit;

}


