package utils.adapterutils;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.cbo.cbomobilereporting.MyCustumApplication;
import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.ui_new.mail_activities.Notification;
import com.cbo.cbomobilereporting.ui_new.mail_activities.popup_noti.PopUpModel;

import java.util.ArrayList;
import java.util.HashMap;

import utils_new.Custom_Variables_And_Method;

/**
 * Created by Akshit on 11/28/2015.
 */
public class Notification_Adapter extends BaseAdapter {

  Context context;
    LayoutInflater layoutInflater;
    HashMap<String, ArrayList<String>> dataList;
    Custom_Variables_And_Method customVariablesAndMethod;

    ArrayList<PopUpModel> arrayList = new ArrayList<>();





    public Notification_Adapter(Context context, HashMap<String, ArrayList<String>> data){

      this.context = context;
      layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      dataList = data;
      customVariablesAndMethod=Custom_Variables_And_Method.getInstance();

  }

    @Override
    public int getCount() {
        return dataList.get("Title").size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null){
             holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.notification_item,null);

            holder.Title=(TextView) convertView.findViewById(R.id.not_title);
            holder.Describtion=(TextView) convertView.findViewById(R.id.not_des);
            holder.Time=(TextView) convertView.findViewById(R.id.not_time);
            holder.delete=(ImageView) convertView.findViewById(R.id.not_delete);
            holder.main= (LinearLayout) convertView.findViewById(R.id.not_main);
            holder.submain= (LinearLayout) convertView.findViewById(R.id.not_subMain);

            convertView.setTag(holder);

        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.Title.setText(dataList.get("Title").get(position));
        holder.Time.setText(dataList.get("Date").get(position)+"\n      "+dataList.get("Time").get(position));

        String msg2=dataList.get("Des").get(position);
        msg2=msg2.replace("@","#_");
        msg2=msg2.replace("^","@");
        String[] msglist=msg2.split("@");
        holder.Describtion.setText(msglist[0]);

        if(dataList.get("Status").get(position).equals("0")){
           holder.main.setBackgroundResource(R.drawable.notification_unread);
        }else{
            holder.main.setBackgroundResource(R.drawable.notification_read);
        }


        holder.submain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dataList.get("Info_url").get(position).equals("")) {

                    String msg2=dataList.get("Des").get(position);
                    msg2=msg2.replace("@","#_");
                    msg2=msg2.replace("^","@");
                    String[] msglist=msg2.split("@");

                    String msg3="";
                   /* for (String aMsglist : msglist) {
                        String msg1 = aMsglist;
                        msg1 = msg1.replace("#_", "@");
                        msg3=msg3.concat(msg1 + "\n");
                    }

*/
                    arrayList.clear();
                    for (String aMsglist : msglist) {
                        String msg1 = aMsglist;
                        msg1 = msg1.replace("#_", "@");
                        String[] msgC = msg1.split("!@");
                        String textMsg = msgC[0];
                        String[] th = textMsg.split(":");

                        String tName=th[0];
                        String tValue="";
                        if(th.length>1){
                            tValue=th[1];
                        }


                        String dcrCode = "";
                        String page = "";
                        if (msgC.length > 1) {

                            String[]   code = msgC[1].split(",");

                            if (code.length > 1) {
                                page = code[0];
                                dcrCode = code[1];
                            }else{
                                page = code[0];
                            }
                        }



                        arrayList.add(new PopUpModel( page, dcrCode,tName,tValue));
                        msg3 = msg3.concat(msg1 + "\n");
                    }

                    if (msglist.length > 1) {
//                        customVariablesAndMethod.getAlert(context, dataList.get("Title").get(position), msglist);
                        customVariablesAndMethod.getAlertArrayR(context, dataList.get("Title").get(position), arrayList);
                    } else {
                        customVariablesAndMethod.getAlert(context, dataList.get("Title").get(position), msg3);
                    }
                }else {
                   /* Intent intent1 = new Intent(context.getApplicationContext(), Msg_ho.class);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent1.putExtra("msg", "1");
                    intent1.putExtra("msg_ho", dataList.get("Info_url").get(position));*/

                    /*Intent intent1 = new Intent(context, CustomWebView.class);
                    intent1.putExtra("A_TP", dataList.get("Info_url").get(position));
                    intent1.putExtra("Title", dataList.get("Title").get(position));
                    //Log.d("javed", new CBO_DB_Helper(context1).getMenuUrl("REPORTS","MSG_HO"));
                    context.startActivity(intent1);*/
                    MyCustumApplication.getInstance().LoadURL(dataList.get("Title").get(position),dataList.get("Info_url").get(position));

                }
                dataList.get("Status").set(position, "1");
                ((Notification) context).updateNotification(position);
                new CBO_DB_Helper(context).updateNotificationStatus(dataList.get("ID").get(position), "1");
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,"delete clicked "+dataList.get("ID").get(position),Toast.LENGTH_LONG).show();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder
                        .setMessage("Do you really want to delete?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                new CBO_DB_Helper(context).notificationDeletebyID(dataList.get("ID").get(position));
                                ((Notification)context).deleteRow(position);
                                dialog.dismiss();
                                //((Activity)context).finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });


        return convertView;
    }



    public class ViewHolder{

        TextView Title,Describtion,Time;
        ImageView delete,msg;
        LinearLayout main,submain;

    }
}
