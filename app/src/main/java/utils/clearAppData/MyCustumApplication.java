package utils.clearAppData;

import java.io.File;
import java.util.List;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import utils.ExceptionHandler;
import utils_new.Custom_Variables_And_Method;


public class MyCustumApplication extends MultiDexApplication {
    private static MyCustumApplication instance;


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        //new ExceptionHandler(this);
    }

    public static MyCustumApplication getInstance(){
        return instance;
    }



    public void clearApplicationData() {
        File cache = getCacheDir();
        File appDir = new File(cache.getParent());
        if(appDir.exists()){
            String[] children = appDir.list();
            for(String s : children){
                if(!s.equals("lib")){
                    deleteDir(new File(appDir, s));
                    Log.i("TAG", "File /data/data/APP_PACKAGE/" + s +" DELETED");
                }
            }
        }
    }

    public static boolean deleteData(File data){

         if (data != null && data.isDirectory()){
             String[] dataChild = data.list();

         for (int i= 0; i<dataChild.length;i++){
             boolean success = deleteDir(new File(data,dataChild[i]));
             if (!success){
                 return false;
             }
         }


         }

        return data.delete();
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }


        return dir.delete();
    }

    public static String getServeiceURL() {
        String URL= "http://www.cboservices.com/mobilerpt.asmx";
        URL= Custom_Variables_And_Method.getInstance().getDataFrom_FMCG_PREFRENCE(getInstance(),"WEBSERVICE_URL",URL);
        if(URL.equals("")){
            URL= "http://www.cboservices.com/mobilerpt.asmx";
        }
        return URL;
    }

    public void ShowAutoStart(){
        try {

            String manufacturer = android.os.Build.MANUFACTURER;
            if ("xiaomi".equalsIgnoreCase(manufacturer)) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
            }else if ("Letv".equalsIgnoreCase(manufacturer)) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity"));
                startActivity(intent);
            }else if ("Honor".equalsIgnoreCase(manufacturer)) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity"));
                startActivity(intent);
            } else if ("oppo".equalsIgnoreCase(manufacturer)) {
               // intent.setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity"));
                try {
                    Intent intent = new Intent();
                    intent.setClassName("com.coloros.safecenter",
                            "com.coloros.safecenter.permission.startup.StartupAppListActivity");
                    startActivity(intent);
                } catch (Exception e) {
                    try {
                        Intent intent = new Intent();
                        intent.setClassName("com.oppo.safe",
                                "com.oppo.safe.permission.startup.StartupAppListActivity");
                        startActivity(intent);

                    } catch (Exception ex) {
                        try {
                            Intent intent = new Intent();
                            intent.setClassName("com.coloros.safecenter",
                                    "com.coloros.safecenter.startupapp.StartupAppListActivity");
                            startActivity(intent);
                        } catch (Exception exx) {

                        }
                    }
                }
            } else if ("vivo".equalsIgnoreCase(manufacturer)) {
                //intent.setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"));
                try {
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName("com.iqoo.secure",
                            "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity"));
                    startActivity(intent);
                } catch (Exception e) {
                    try {
                        Intent intent = new Intent();
                        intent.setComponent(new ComponentName("com.vivo.permissionmanager",
                                "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"));
                        startActivity(intent);
                    } catch (Exception ex) {
                        try {
                            Intent intent = new Intent();
                            intent.setClassName("com.iqoo.secure",
                                    "com.iqoo.secure.ui.phoneoptimize.BgStartUpManager");
                            startActivity(intent);
                        } catch (Exception exx) {
                            ex.printStackTrace();
                        }
                    }
                }
            }

//            List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
//            if  (list.size() > 0) {
//                startActivity(intent);
//            }
        } catch (Exception e) {
            //logException(e);
        }
    }
}
