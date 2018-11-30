package receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.ui_new.mail_activities.Inbox_Mail;
import com.cbo.cbomobilereporting.ui_new.report_activities.DOB_DOA;
import com.cbo.cbomobilereporting.ui_new.report_activities.Msg_ho;

import utils_new.Update_Avilable;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by pc24 on 22/09/2016.
 */
public class DOB_DOA_broadcastreciever  extends BroadcastReceiver {
    Context context1;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("sender", "recieving message");
        this.context1=context;
        Intent intent1;
        switch ( intent.getStringExtra("from")){
            case "0":
                //setDOB_Remainder(context);
                intent1=new Intent(context.getApplicationContext(),DOB_DOA.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent1.putExtra("msg","1");
                context.startActivity(intent1);
                break;
            case "1":
                intent1=new Intent(context1.getApplicationContext(),Msg_ho.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent1.putExtra("msg","1");
                intent1.putExtra("msg_ho", new CBO_DB_Helper(context1).getMenuUrl("REPORTS","MSG_HO"));
                context1.startActivity(intent1);
                break;
            case "2":
                intent1=new Intent(context1.getApplicationContext(), Update_Avilable.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context1.startActivity(intent1);
                //new MyConnection(context).getAppUpdateAlert(context, "Update Available", "Please Update your APP to avail the latest Features");
                break;
            case "3":
                intent1=new Intent(context1.getApplicationContext(),Msg_ho.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent1.putExtra("msg","1");
                intent1.putExtra("msg_ho", intent.getStringExtra("url"));
                context1.startActivity(intent1);
                break;
            case "4":
                intent1=new Intent(context1.getApplicationContext(),Inbox_Mail.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent1.putExtra("msg","1");
                context1.startActivity(intent1);
                break;
            case "5":
                intent1=new Intent(context1.getApplicationContext(),Msg_ho.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent1.putExtra("msg","2");
                intent1.putExtra("msg_ho", intent.getStringExtra("url"));
                context1.startActivity(intent1);
                break;
        }

    }

  /*  private void setDOB_Remainder(Context context) {

        int i = (60*60* 1000)/60;
        Intent intent = new Intent(context, MyBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context.getApplicationContext(), 234324243, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime()+ (i),
                (i), pendingIntent);
        //Toast.makeText(context, "Alarm set in " + i + " seconds",Toast.LENGTH_LONG).show();
    }*/
}
