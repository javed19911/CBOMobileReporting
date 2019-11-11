package utils_new;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.cbo.cbomobilereporting.MyCustumApplication;
import com.cbo.cbomobilereporting.databaseHelper.Call.Local.DayPlanAlertDB;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.content.Context.POWER_SERVICE;
import static android.content.Context.VIBRATOR_SERVICE;

/**
 * Created by cboios on 07/08/18.
 */

public class DayPlanTextToSpeech extends BroadcastReceiver  {


    final static int RQS_2 = 2;
    private TextToSpeech tts;
    Custom_Variables_And_Method customVariablesAndMethod = Custom_Variables_And_Method.getInstance();
    DayPlanAlertDB dayPlanAlertDB=null;

    Intent intent = new Intent( MyCustumApplication.getInstance(), DayPlanTextToSpeech.class);
    PendingIntent pendingIntent = PendingIntent.getBroadcast(
            MyCustumApplication.getInstance(), RQS_2, intent, 0);
    AlarmManager alarmManager = (AlarmManager)  MyCustumApplication.getInstance().getSystemService(Context.ALARM_SERVICE);


    private Map<String, String> getNextAlertDateDetail(Context context){
        Date NextAlertDate = null;
        Map<String, String> NextAlertDateDetail = null ;
        Date Today = new Date();
        dayPlanAlertDB = new DayPlanAlertDB(context);
        ArrayList<Map<String, String>> dayplanAlert = dayPlanAlertDB.get();
        for (int i = 0; i< dayplanAlert.size()-1;i++){
            try {
                Date alertDate = CustomDatePicker.getDate(
                        dayplanAlert.get(i).get("DCR_DATE")+" " + dayplanAlert.get(i).get("DAYPLAN_REMINDER_FTIME") ,
                        CustomDatePicker.CommitFormat +" HH.mm");
                if (NextAlertDate == null && Today.before(alertDate)){
                    NextAlertDateDetail = dayplanAlert.get(i);
                    NextAlertDate = alertDate;
                }else if(NextAlertDate != null && NextAlertDate.after(alertDate) && Today.before(alertDate)){
                    NextAlertDate = alertDate;
                    NextAlertDateDetail = dayplanAlert.get(i);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (NextAlertDateDetail != null){
            if (NextAlertDateDetail.get("DAYPLAN_REMINDER_FTIME").equalsIgnoreCase("0") ||
                    NextAlertDateDetail.get("DAYPLAN_REMINDER_TTIME").equalsIgnoreCase("0") ||
                    NextAlertDateDetail.get("DAYPLAN_REMINDER_VOICE").isEmpty()){
                return null;
            }
        }

        return NextAlertDateDetail;
    }


    public void setTextToSpeech(Context context,String time,String voice_msg,Date EndTime) {

        if (Double.parseDouble(MyCustumApplication.getInstance().getDataFrom_FMCG_PREFRENCE("DAYPLAN_REMINDER_FTIME","0.0")) == 0)
            return;

        Calendar calNow = Calendar.getInstance();
        Calendar calSet =  Calendar.getInstance();


        if (time.equals("")){
            Map<String, String> NextAlertDateDetail = getNextAlertDateDetail(context);
            if (NextAlertDateDetail == null){
                return;
            }
            Date alertDate = null;
            try {
                alertDate = CustomDatePicker.getDate(NextAlertDateDetail.get("DCR_DATE")+" " + NextAlertDateDetail.get("DAYPLAN_REMINDER_FTIME") ,
                        CustomDatePicker.CommitFormat +" HH.mm");
                EndTime = CustomDatePicker.getDate(NextAlertDateDetail.get("DCR_DATE")+" " + NextAlertDateDetail.get("DAYPLAN_REMINDER_TTIME") ,
                        CustomDatePicker.CommitFormat +" HH.mm");

            } catch (ParseException e) {
                e.printStackTrace();
                return;
            }
            voice_msg = NextAlertDateDetail.get("DAYPLAN_REMINDER_VOICE");
            time = NextAlertDateDetail.get("DAYPLAN_REMINDER_INTERVAL");
            calSet.setTime(alertDate);

            /*int hour = alert_time.intValue();
            int miniute =  ((Double)((alert_time - hour)*100)).intValue() ;
            calSet.set(Calendar.HOUR_OF_DAY, hour);*/
            //calSet.set(Calendar.MINUTE, calNow.get(Calendar.MINUTE) +1);
        }else {
            calSet.set(Calendar.HOUR_OF_DAY, calNow.get(Calendar.HOUR_OF_DAY)); // calNow.getTime().getHours()
            calSet.set(Calendar.MINUTE, calNow.get(Calendar.MINUTE) + Integer.parseInt(time));//Integer.parseInt (NextAlertDateDetail.get("DAYPLAN_REMINDER_INTERVAL"))); //calNow.getTime().getMinutes()
        }

        calSet.set(Calendar.SECOND, 0);
        calSet.set(Calendar.MILLISECOND, 0);

        if (calSet.compareTo(calNow) <= 0) {
            // Today Set time passed, count to tomorrow
            //calSet.add(Calendar.DATE, 1);
            return;
        }

        setAlarm(calSet, time,voice_msg,EndTime);

    }

    public void stopTextToSpeech() {
        alarmManager.cancel(pendingIntent);
    }

    private void setAlarm(Calendar targetCal,String repeatIn,String voice_msg,Date EndTime) {
        intent = new Intent( MyCustumApplication.getInstance(), DayPlanTextToSpeech.class);
        intent.putExtra("repeatIn",repeatIn);
        intent.putExtra("voice_msg",voice_msg);
        intent.putExtra("EndTime",EndTime);
        pendingIntent = PendingIntent.getBroadcast(
                MyCustumApplication.getInstance(), RQS_2, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(),
                pendingIntent);

    }


    @Override
    public void onReceive(Context context, Intent intent) {
        if (!MyCustumApplication.getInstance().getDataFrom_FMCG_PREFRENCE("dcr_date_real","").equals(""))
            return;
        Date EndTime = null;
        if (intent.getSerializableExtra("EndTime") != null){
            EndTime = (Date) intent.getSerializableExtra("EndTime");
        }else{
            return;
        }
        if (EndTime.before(new Date())){
            return;
        }

        String text ="Your Day Plan is Pending. Please plan your DCR.";
        if (intent.getStringExtra("voice_msg") != null){
            text = intent.getStringExtra("voice_msg");
        }
        String finalText = text;
        Date finalEndTime = EndTime;
        tts = new TextToSpeech(MyCustumApplication.getInstance(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {

                    int result = tts.setLanguage(Locale.US);

                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "This Language is not supported");
                    } else {

                        if(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,
                                "ShowSystemAlert","Y").equals("Y")) {
                            customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,
                                    "ShowSystemAlert","N");
                            AppAlert.getInstance().SystemAlert("Alert !!!!", finalText, null);
                        }
                        PowerManager.WakeLock screenLock = ((PowerManager) MyCustumApplication.getInstance().getSystemService(POWER_SERVICE)).newWakeLock(
                                PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
                        screenLock.acquire(15);

                        long[] pattern = { 0, 1000, 500, 1000, 500, 1000, 500, 1000};
                        assert ((Vibrator) MyCustumApplication.getInstance().getSystemService(VIBRATOR_SERVICE)) != null;
                        ((Vibrator)MyCustumApplication.getInstance().getSystemService(VIBRATOR_SERVICE)).vibrate(pattern,-1);

                        //tts.setPitch(0.8f);
                        tts.setSpeechRate(0.7f);
                        tts.speak(finalText, TextToSpeech.QUEUE_FLUSH, null);
                        String repeatIn ="10";
                        if (intent.getStringExtra("repeatIn") != null){
                            repeatIn = intent.getStringExtra("repeatIn");
                        }
                        setTextToSpeech(context,repeatIn, finalText, finalEndTime);
                    }

                } else {
                    Log.e("TTS", "Initilization Failed!");
                }
            }
        });

    }


}
