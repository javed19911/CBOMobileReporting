package fcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.util.Log;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.ui_new.CustomActivity;
import com.cbo.cbomobilereporting.ui_new.ViewPager_2016;
import com.cbo.cbomobilereporting.ui_new.approval_activities.Remainder.FloatingRemainderApproval;
import com.cbo.cbomobilereporting.ui_new.approval_activities.Remainder.RemainderActivity;
import com.cbo.cbomobilereporting.ui_new.for_all_activities.CustomWebView;
import com.cbo.cbomobilereporting.ui_new.mail_activities.Inbox_Mail;
import com.cbo.cbomobilereporting.ui_new.report_activities.DOB_DOA;
import com.cbo.cbomobilereporting.ui_new.report_activities.Msg_ho;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import me.leolin.shortcutbadger.ShortcutBadger;
import utils_new.Custom_Variables_And_Method;


/**
 * Created by pc24 on 09/12/2016.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    Custom_Variables_And_Method customVariablesAndMethod;
    String msgtyp,title,msg,url,logo;
    Bitmap largeIcon;
    Boolean flag_logo=false,flag_info=false,insert=true;
    CBO_DB_Helper cboDbHelper;
    Context context;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Displaying data in log
        //It is optional
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        context=this;
        cboDbHelper=new CBO_DB_Helper(this);
        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
        //Log.d(TAG, "Notification Message Body: " + remoteMessage.getData().get("body"));
        //sendNotification(remoteMessage.getNotification().getBody());
        sendNotification(remoteMessage.getData().get("body"));

    }


    //This method is generating a notification and displaying the notification
    private void sendNotification(String message) {
        Intent intent = new Intent(this, ViewPager_2016.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        int requestCode = 0;
        msgtyp = "Message from server";
        title = "Hello";
        msg = "Hello";
        url="";
        logo="";
        largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.cbo);
        try {
            JSONArray jsonArray = new JSONArray(message);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            msgtyp= jsonObject.getString("msgtyp");
            if (msgtyp.equals("MAIL")) {
                intent = new Intent(this, Inbox_Mail.class);
                JSONObject jsonObject1 = jsonArray.getJSONObject(1);
                title = jsonObject1.getString("tilte");
                JSONObject jsonObject2 = jsonArray.getJSONObject(2);
                msg = jsonObject2.getString("msg");
                JSONObject jsonObject3 = jsonArray.getJSONObject(3);
                url = jsonObject3.getString("url");
                JSONObject jsonObject4 = jsonArray.getJSONObject(4);
                logo = jsonObject4.getString("logo");
                if (logo==null || logo.equals("") ){
                    flag_logo=false;
                }else{
                    flag_logo=true;
                }
                if (url==null || url.equals("") ){
                    flag_info=false;

                }else {
                    flag_info=true;
                }
                insert=false;
                new generatePictureStyleNotification(this,title,msg,url,intent).execute();

            }
            else if (msgtyp.equals("DOB_DOA")) {
                intent = new Intent(this, DOB_DOA.class);
                JSONObject jsonObject1 = jsonArray.getJSONObject(1);
                title = jsonObject1.getString("tilte");
                JSONObject jsonObject2 = jsonArray.getJSONObject(2);
                msg = jsonObject2.getString("msg");
                JSONObject jsonObject3 = jsonArray.getJSONObject(3);
                url = jsonObject3.getString("url");
                JSONObject jsonObject4 = jsonArray.getJSONObject(4);
                logo = jsonObject4.getString("logo");
                if (logo==null || logo.equals("") ){
                    flag_logo=false;
                }else{
                    flag_logo=true;
                }
                if (url==null || url.equals("") ){
                    flag_info=false;
                    Log.v("javed","false");
                }else {
                    flag_info=true;
                    Log.v("javed","true");
                }
                new generatePictureStyleNotification(this,title,msg,url,intent).execute();

            }

            else if (msgtyp.contains("WS_")) {
                String tag=msgtyp.substring(3);
                new generateInboxStyleNotification(this,tag).execute();
            }

            else if (msgtyp.equals("MSG")) {
                intent = new Intent(this, com.cbo.cbomobilereporting.ui_new.mail_activities.Notification.class);
                JSONObject jsonObject1 = jsonArray.getJSONObject(1);
                title = jsonObject1.getString("tilte");
                JSONObject jsonObject2 = jsonArray.getJSONObject(2);
                msg = jsonObject2.getString("msg");
                JSONObject jsonObject3 = jsonArray.getJSONObject(3);
                url = jsonObject3.getString("url");
                if (url!=null && !url.equals("") ){
                    flag_info=false;
                    intent = new Intent(this, Msg_ho.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("msg", "1");
                    intent.putExtra("msg_ho", url);
                }
                // new generateInboxStyleNotification(this,tag).execute();
                big_table_Style(requestCode,intent);
            }
            else if (msgtyp.equals("SERVICE_URL")) {
                intent = new Intent(this, com.cbo.cbomobilereporting.ui_new.mail_activities.Notification.class);
                JSONObject jsonObject1 = jsonArray.getJSONObject(1);
                title = jsonObject1.getString("tilte");
                title="WEB SERVICE UPDATED";
                JSONObject jsonObject2 = jsonArray.getJSONObject(2);
                msg = jsonObject2.getString("msg");
                if(msg!=null && !msg.equals("") && !msg.equals("Hello")) {
                    customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"WEBSERVICE_URL", msg);
                }
                msg="New webservices configured successfully";
                //editor.putString("WEBSERVICE_URL", c.getString("WEBSERVICE_URL"));
                // new generateInboxStyleNotification(this,tag).execute();
                big_table_Style(requestCode,intent);
                //insert=false;
            } else if (msgtyp.equals("SET_PREFRENCE")) {
                intent = new Intent(this, com.cbo.cbomobilereporting.ui_new.mail_activities.Notification.class);
                JSONObject jsonObject1 = jsonArray.getJSONObject(1);
                title = jsonObject1.getString("tilte");
                JSONObject jsonObject2 = jsonArray.getJSONObject(2);
                msg = jsonObject2.getString("msg");
                if(msg!=null  && !msg.equals("Hello")) {
                    customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,title, msg);
                }
                msg="Setting updated successfully";
                //editor.putString("WEBSERVICE_URL", c.getString("WEBSERVICE_URL"));
                // new generateInboxStyleNotification(this,tag).execute();
                big_table_Style(requestCode,intent);
                //insert=false;
            }else if (msgtyp.equals("FLASH_MESSAGE")) {
                intent = new Intent(this, com.cbo.cbomobilereporting.ui_new.mail_activities.Notification.class);
                JSONObject jsonObject1 = jsonArray.getJSONObject(1);
                title = jsonObject1.getString("tilte");
                title="FLASH MESSAGE";
                JSONObject jsonObject2 = jsonArray.getJSONObject(2);
                msg = jsonObject2.getString("msg");
                if(msg!=null && !msg.equals("") && !msg.equals("Hello")) {
                    customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"mark", msg);
                }
                big_table_Style(requestCode,intent);
                //insert=false;
            }else if (msgtyp.equals("CALL")) {
                intent = new Intent(this, com.cbo.cbomobilereporting.ui_new.mail_activities.Notification.class);
                JSONObject jsonObject1 = jsonArray.getJSONObject(1);
                title = jsonObject1.getString("tilte");
                JSONObject jsonObject2 = jsonArray.getJSONObject(2);
                msg = jsonObject2.getString("msg");
                JSONObject jsonObject3 = jsonArray.getJSONObject(3);
                url = jsonObject3.getString("url");
                JSONObject jsonObject4 = jsonArray.getJSONObject(4);
                logo = jsonObject4.getString("logo");
                if(msg!=null && !msg.equals("") && !msg.equals("Hello")) {
                    //cboDbHelper.updateLatLongOnCall(lat_long,dr_id,doc_type);

                    cboDbHelper.updateLatLongOnCall(url, title, msg);
                    title = "Updated...";
                    msg = "DCR Successfully Updated...";
                    big_table_Style(requestCode,intent);
                }
                //insert=false;
            }else if (msgtyp.equals("REG")) {
                intent = new Intent(this, com.cbo.cbomobilereporting.ui_new.mail_activities.Notification.class);
                JSONObject jsonObject1 = jsonArray.getJSONObject(1);
                title = jsonObject1.getString("tilte");
                JSONObject jsonObject2 = jsonArray.getJSONObject(2);
                msg = jsonObject2.getString("msg");
                JSONObject jsonObject3 = jsonArray.getJSONObject(3);
                url = jsonObject3.getString("url");
                JSONObject jsonObject4 = jsonArray.getJSONObject(4);
                logo = jsonObject4.getString("logo");
                if(msg!=null && !msg.equals("") && !msg.equals("Hello")) {
                    //cboDbHelper.updateLatLong(lat_long,dr_id,doc_type);

                    cboDbHelper.updateLatLong(url, title, msg,logo);
                    if(msg.equals("D")) {
                        title = "Doctor Registration";
                    }else if(msg.equals("S")) {
                        title = "Stockist Registration";
                    }else {
                        title = "Chemist Registration";
                    }
                    if(url.equals("")) {
                        msg = "Registration Successfully Reset";
                    }else {
                        msg = "Registration Successfully Done";
                    }
                }
                big_table_Style(requestCode,intent);
                //insert=false;
            }else if (msgtyp.equals("CALL_LOCK")) {
                intent = new Intent(this, com.cbo.cbomobilereporting.ui_new.mail_activities.Notification.class);
                JSONObject jsonObject1 = jsonArray.getJSONObject(1);
                title = jsonObject1.getString("tilte");
                JSONObject jsonObject2 = jsonArray.getJSONObject(2);
                msg = jsonObject2.getString("msg");
                if (msg != null  && !msg.equals("Hello") && msg.contains("unlocked")) {
                    //cboDbHelper.updateLatLong(lat_long,dr_id,doc_type);
                    customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"CALL_UNLOCK_STATUS","[CALL_UNLOCK]");
                }
                big_table_Style(requestCode, intent);
                //insert=false;
            }else if (msgtyp.equals("APPROVAL_URL")) {

                JSONObject jsonObject1 = jsonArray.getJSONObject(1);
                title = jsonObject1.getString("tilte");
                JSONObject jsonObject2 = jsonArray.getJSONObject(2);
                msg = jsonObject2.getString("msg");
                JSONObject jsonObject3 = jsonArray.getJSONObject(3);
                url = jsonObject3.getString("url");

                if(url!=null && !url.equals("") ) {
                    intent = new Intent(context, CustomWebView.class);
                    intent.putExtra("A_TP", url);
                    intent.putExtra("Title", title);
                }

                big_table_Style(requestCode,intent);
                //insert=false;
            }else if (msgtyp.equals("REM_APPROVAL")) {
                intent = new Intent(context, FloatingRemainderApproval.class);
                intent.putExtra("isAlertDialog", CustomActivity.activityType.DIALOG.getValue());
                JSONObject jsonObject1 = jsonArray.getJSONObject(1);
                title = jsonObject1.getString("tilte");
                intent.putExtra("Title", title);
                JSONObject jsonObject2 = jsonArray.getJSONObject(2);
                msg = "";
                intent.putExtra("response", jsonObject2.getString("msg"));
                startService(intent);
                //insert=false;
            }
            //if (msgtyp.equals("WISHES")) {
            else{
                intent = new Intent(this, com.cbo.cbomobilereporting.ui_new.mail_activities.Notification.class);
                JSONObject jsonObject1 = jsonArray.getJSONObject(1);
                title = jsonObject1.getString("tilte");
                JSONObject jsonObject2 = jsonArray.getJSONObject(2);
                msg = jsonObject2.getString("msg");
                JSONObject jsonObject3 = jsonArray.getJSONObject(3);
                url = jsonObject3.getString("url");
                JSONObject jsonObject4 = jsonArray.getJSONObject(4);
                logo = jsonObject4.getString("logo");
                if (logo==null || logo.equals("") ){
                    flag_logo=false;
                }else{
                    flag_logo=true;
                }
                if (url==null || url.equals("") ){
                    flag_info=false;
                    Log.v("javed","false");
                }else {
                    flag_info=true;
                    Log.v("javed","true");
                }

                new generatePictureStyleNotification(this,title,msg,url,intent).execute();


            }


            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -7);
            cboDbHelper.delete_weakOld_Notification(cal);
            if (insert) {
                Date date = new Date();
                String date1 = customVariablesAndMethod.convetDateddMMyyyy(date);
                String time = customVariablesAndMethod.currentTime(context);
                cboDbHelper.insert_Notification(title, msg, logo, url, "0", date1, time);
            }


            int badgeCount = cboDbHelper.getNotification_count(); //+1;
            ShortcutBadger.applyCount(this, badgeCount); //for 1.1.4+


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void big_table_Style(int requestCode,Intent intent){
        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);

        String dis;

        //Assign inbox style notification
        NotificationCompat.InboxStyle inboxStyle =
                new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle(title);
        String msg2=msg;
        msg2=msg2.replace("@","#_");
        msg2=msg2.replace("^","@");
        String[] msglist=msg2.split("@");

        dis =msglist[0];

        int x=msglist.length;
        if(x>=6){
            x=6;
        }
        int y=msglist.length-x;
        if(y>0){
            inboxStyle.setSummaryText("+"+y+" more");
        }
        for (int i=0;i<x;i++){
            String msg1=msglist[i];
            msg1=msg1.replace("#_","@");
            inboxStyle.addLine(msg1);
        }



        String CHANNEL_ID = "my_channel_01";// The id of the channel.
        CharSequence name = "CBO Notification"; //getString(R.string.channel_name);// The user-visible name of the channel.
        int importance = NotificationManager.IMPORTANCE_HIGH;


        //build notification
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this,CHANNEL_ID)
                        .setSmallIcon(R.drawable.cbo_noti)
                        .setContentTitle(title)
                        .setContentText(dis)
                        .setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setStyle(inboxStyle)
                        .setChannelId(CHANNEL_ID)
                        .setContentIntent(pendingIntent);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int color = 0x125688;
            mBuilder.setColor(color);
            mBuilder.setSmallIcon(R.drawable.cbo_noti);
        }

        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Sets an ID for the notification, so it can be updated.

            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationManager.createNotificationChannel(mChannel);
        }
        notificationManager.notify(m, mBuilder.build()); //m = ID of notification
    }

    private void P_intent(int requestCode,Intent intent){
        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        String CHANNEL_ID = "my_channel_01";// The id of the channel.
        CharSequence name =  "CBO Notification"; //getString(R.string.channel_name);// The user-visible name of the channel.
        int importance = NotificationManager.IMPORTANCE_HIGH;

        NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setSmallIcon(R.drawable.cbo)
                .setContentTitle(title)
                .setContentText(msg)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setChannelId(CHANNEL_ID)
                .setContentIntent(pendingIntent);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int color = 0x125688;
            noBuilder.setColor(color);
            noBuilder.setSmallIcon(R.drawable.cbo_noti);
        }
        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Sets an ID for the notification, so it can be updated.


            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        notificationManager.notify(m, noBuilder.build()); //m = ID of notification
    }


    public class generatePictureStyleNotification extends AsyncTask<String, Void, Bitmap> {

        private Context mContext;
        private String title, message, imageUrl;
        int requestCode =0;
        Intent intent;

        public generatePictureStyleNotification(Context context, String title, String message, String imageUrl,Intent intent) {
            super();
            this.mContext = context;
            this.title = title;
            this.message = message;
            this.imageUrl = imageUrl;
            this.intent=intent;
            /*DisplayMetrics displaymetrics = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            height = (displaymetrics.heightPixels-displaymetrics.heightPixels%5)/5;
            width = displaymetrics.widthPixels;*/
        }

        @Override
        protected Bitmap doInBackground(String... params) {

            InputStream in;
            Bitmap myBitmap=null;
            try {
                if(flag_info) {
                    URL url = new URL(this.imageUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    in = connection.getInputStream();
                    myBitmap = BitmapFactory.decodeStream(in);
                    connection.disconnect();
                    Log.v("javed","true2");
                    //Bitmap resized = Bitmap.createScaledBitmap(myBitmap, width, height, true);
                }
                if(flag_logo){
                    URL url = new URL(logo);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    in = connection.getInputStream();
                    largeIcon = BitmapFactory.decodeStream(in);
                    connection.disconnect();
                }
                if(flag_info){
                    return myBitmap;
                }
                return myBitmap;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            if(!flag_info){
                big_table_Style(requestCode,intent);
            }else {
                PendingIntent pendingIntent = PendingIntent.getActivity(mContext, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);
                Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                String CHANNEL_ID = "my_channel_01";// The id of the channel.
                CharSequence name =  "CBO Notification"; //getString(R.string.channel_name);// The user-visible name of the channel.
                int importance = NotificationManager.IMPORTANCE_HIGH;


                NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(mContext,CHANNEL_ID)
                        .setContentTitle(title)
                        .setContentText(msg)
                        .setSmallIcon(R.drawable.cbo_noti)
                        .setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setLargeIcon(largeIcon)
                        .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(result))
                        .setChannelId(CHANNEL_ID)
                        .setContentIntent(pendingIntent);

                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    int color = 0x125688;
                    noBuilder.setColor(color);
                    noBuilder.setSmallIcon(R.drawable.cbo_noti);
                }

                Random random = new Random();
                int m = random.nextInt(9999 - 1000) + 1000;
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    // Sets an ID for the notification, so it can be updated.
                    NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                    notificationManager.createNotificationChannel(mChannel);
                }
                notificationManager.notify(m, noBuilder.build()); //m = ID of notification

            }
        }
    }


    public class generateInboxStyleNotification extends AsyncTask<String, Void, Bitmap> {

        private Context mContext;
        private String  message, imageUrl,dis,tag,paid,companyFolder;
        int requestCode =0;
        Intent intent;

        public generateInboxStyleNotification(Context context,String tag) {
            super();
            this.mContext = context;
            this.tag=tag;
            this.paid=cboDbHelper.getPaid();
            this.companyFolder=cboDbHelper.getCompanyCode();
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            intent = new Intent(getApplicationContext(), com.cbo.cbomobilereporting.ui_new.mail_activities.Notification.class);
            JSONArray jsonArray = null;
            try {
                //message=new ServiceHandler(mContext).getReponseGcmCommon(companyFolder,paid,tag);
                message="[{\"tilte\":\"You have a mail\"},{\"msg\":[{\"item\":\"<TD><STRONG>NAME</STRONG></TD><TD><STRONG>DR</STRONG></TD>" +
                        "    <TD><STRONG>CHEMIST</STRONG></TD>\"},{\"item\":\"message 2\"},{\"item\":\"message 1\"},{\"item\":\"message 2\"},{\"item\":\"message 3\"},{\"item\":\"message 1\"},{\"item\":\"message 2\"},{\"item\":\"message 3\"},{\"item\":\"message 3\"}]}]";
                jsonArray = new JSONArray(message);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                title = jsonObject.getString("tilte");
                JSONObject jsonObject1 = jsonArray.getJSONObject(1);
                msg = jsonObject1.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);



            //Assign inbox style notification
            NotificationCompat.InboxStyle inboxStyle =
                    new NotificationCompat.InboxStyle();

            inboxStyle.setBigContentTitle(title);
            try {
                JSONArray jsonArray1 = new JSONArray(msg);
                JSONObject jsonObject9 = jsonArray1.getJSONObject(jsonArray1.length()-1);
                dis =jsonObject9.getString("item");

                int x=jsonArray1.length();
                if(x>=6){
                    x=6;
                }
                int y=jsonArray1.length()-x;
                if(y>0){
                    inboxStyle.setSummaryText("+"+y+" more");
                }
                for (int i=0;i<x-1;i++){
                    JSONObject jsonObject = jsonArray1.getJSONObject(i);
                    inboxStyle.addLine(Html.fromHtml(jsonObject.getString("item")));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            String CHANNEL_ID = "my_channel_01";// The id of the channel.
            CharSequence name =  "CBO Notification"; //getString(R.string.channel_name);// The user-visible name of the channel.
            int importance = NotificationManager.IMPORTANCE_HIGH;

            //build notification
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(mContext,CHANNEL_ID)
                            .setSmallIcon(R.drawable.cbo_noti)
                            .setContentTitle(title)
                            .setContentText(dis)
                            .setAutoCancel(true)
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setStyle(inboxStyle)
                            .setChannelId(CHANNEL_ID)
                            .setContentIntent(pendingIntent);

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                int color = 0x125688;
                mBuilder.setColor(color);
                mBuilder.setSmallIcon(R.drawable.cbo_noti);
            }

            Random random = new Random();
            int m = random.nextInt(9999 - 1000) + 1000;
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Sets an ID for the notification, so it can be updated.
                NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                notificationManager.createNotificationChannel(mChannel);
            }
            notificationManager.notify(m, mBuilder.build()); //m = ID of notification

        }
    }

}
