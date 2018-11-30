package utils_new;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.Log;

import com.cbo.cbomobilereporting.emp_tracking.MyCustomMethod;

import org.jsoup.Jsoup;

import utils.MyConnection;

/**
 * Created by pc24 on 26/12/2016.
 */

public class GetVersionCode extends AsyncTask<Void, String, String> {
    Context context;

    public GetVersionCode(Context context){
        this.context=context;
    }

    @Override
    protected String doInBackground(Void... voids) {

        String newVersion = null;
        try {
            newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + "com.cbo.cbomobilereporting" + "&hl=it")
                    .timeout(30000)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .get()
                    .select("div[itemprop=softwareVersion]")
                    .first()
                    .ownText();
            return newVersion;
        } catch (Exception e) {
            return newVersion;
        }
    }

    @Override
    protected void onPostExecute(String onlineVersion) {
        super.onPostExecute(onlineVersion);
        PackageInfo pInfo = null;
        try {
            pInfo =context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        }
        catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        //get the app version Name for display
        String currentVersion = pInfo.versionName;
        if (onlineVersion != null && !onlineVersion.isEmpty()) {
            if (Float.valueOf(currentVersion) < Float.valueOf(onlineVersion)) {
                //show dialog
                new MyCustomMethod(context).sendMessage("2");
            }
        }
       // Log.d("update", "Current version " + currentVersion + "playstore version " + onlineVersion);
    }
}
