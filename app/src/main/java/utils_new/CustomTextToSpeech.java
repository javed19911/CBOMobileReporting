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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import com.cbo.cbomobilereporting.MyCustumApplication;
import com.cbo.cbomobilereporting.databaseHelper.Call.Local.DayPlanAlertDB;

import static android.content.Context.POWER_SERVICE;
import static android.content.Context.VIBRATOR_SERVICE;

/**
 * Created by cboios on 07/08/18.
 */

public class CustomTextToSpeech extends BroadcastReceiver  {


    final static int RQS_1 = 1;
    private TextToSpeech tts;
    Custom_Variables_And_Method customVariablesAndMethod = Custom_Variables_And_Method.getInstance();

    Intent intent = new Intent( MyCustumApplication.getInstance(), CustomTextToSpeech.class);
    PendingIntent pendingIntent = PendingIntent.getBroadcast(
            MyCustumApplication.getInstance(), RQS_1, intent, 0);
    AlarmManager alarmManager = (AlarmManager)  MyCustumApplication.getInstance().getSystemService(Context.ALARM_SERVICE);


    public void setTextToSpeech(String time) {
       Double alert_time =  Double.parseDouble(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(MyCustumApplication.getInstance(),"DCR_SUBMIT_TIME","0.0"));
       if (alert_time == 0)
           return;


        Calendar calNow = Calendar.getInstance();
        Calendar calSet = (Calendar) calNow.clone();
        int hour = alert_time.intValue();
        int miniute =  ((Double)((alert_time - hour)*100)).intValue() ;

        if (time.equals("")){
            calSet.set(Calendar.HOUR_OF_DAY, hour);
            calSet.set(Calendar.MINUTE, miniute);
        }else {
            calSet.set(Calendar.HOUR_OF_DAY, calNow.get(Calendar.HOUR_OF_DAY)); // calNow.getTime().getHours()
            calSet.set(Calendar.MINUTE, calNow.get(Calendar.MINUTE) + 10); //calNow.getTime().getMinutes()
        }

        calSet.set(Calendar.SECOND, 0);
        calSet.set(Calendar.MILLISECOND, 0);

        if (calSet.compareTo(calNow) <= 0) {
            // Today Set time passed, count to tomorrow
            calSet.add(Calendar.DATE, 1);
        }

        setAlarm(calSet);

    }

    public void stopTextToSpeech() {
        alarmManager.cancel(pendingIntent);
    }

    private void setAlarm(Calendar targetCal) {

        alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(),
                pendingIntent);

    }


    @Override
    public void onReceive(Context context, Intent intent) {
        if (customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(MyCustumApplication.getInstance(),"Final_submit","Y").equals("Y"))
            return;

        String text = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(MyCustumApplication.getInstance(),"DCR_SUBMIT_SPEACH","Its too late now, Please final submit your DCR...") ;//"hello javed!!, how are you?";
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
                            AppAlert.getInstance().SystemAlert("Alert !!!!", text, null);
                        }
                        PowerManager.WakeLock screenLock = ((PowerManager) MyCustumApplication.getInstance().getSystemService(POWER_SERVICE)).newWakeLock(
                                PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
                        screenLock.acquire(15);

                        long[] pattern = { 0, 1000, 500, 1000, 500, 1000, 500, 1000};
                        assert ((Vibrator) MyCustumApplication.getInstance().getSystemService(VIBRATOR_SERVICE)) != null;
                        ((Vibrator)MyCustumApplication.getInstance().getSystemService(VIBRATOR_SERVICE)).vibrate(pattern,-1);

                        //tts.setPitch(0.8f);
                        tts.setSpeechRate(0.7f);
                        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                        setTextToSpeech("1");
                    }

                } else {
                    Log.e("TTS", "Initilization Failed!");
                }
            }
        });

    }


}
