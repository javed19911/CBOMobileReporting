package com.cbo.cbomobilereporting.ui_new;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.view.animation.Animation;
import android.widget.ImageView;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.ui.LoginFake;
import com.cbo.cbomobilereporting.ui.LoginMain;

import interfaces.SampleInterface;
import me.leolin.shortcutbadger.ShortcutBadger;
import utils.Font_helper;
import utils_new.AppAlert;
import utils_new.Custom_Variables_And_Method;


public class SplashScreen_2016 extends CustomActivity  {

    Context context;
    CBO_DB_Helper cbohelp;
    Cursor cursorLoginDetail;
    Custom_Variables_And_Method customVariablesAndMethod;
    byte[] byteArray =null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_2016);

        context = SplashScreen_2016.this;

        cbohelp = new CBO_DB_Helper(context);
        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
        customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"MethodCallFinal", "N");
        customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"Tracking", "N");
        ShortcutBadger.applyCount(this, 0);

        cursorLoginDetail=   cbohelp.getLoginDetail();

        Bundle extras = getIntent().getExtras();

        if( null != extras && extras.getByteArray("picture") !=null  ) {

            byteArray=extras.getByteArray("picture");
            Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            ImageView image = (ImageView) findViewById(R.id.image_blur);
            image.setImageBitmap(bmp);
        }

        /*alert("From outside");
        Thread thread1=new Thread(){
            @Override
            public void run() {
                try{


                    sleep(50);

                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }  finally {

                    alert("From Inside");
                }

            }
        };*/


        Thread thread=new Thread(){
            @Override
            public void run() {
                try{


                    sleep(3000);

                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }  finally {


                  if ((cursorLoginDetail != null)&&(cursorLoginDetail.moveToFirst())){
                      try{
                         // if (!customVariablesAndMethod.isBackgroundServiceRunning(context)) {
                              //startService(new Intent(context, MyLoctionService.class));
                          //startLoctionService();
                          //}
                          Intent intent=new Intent(SplashScreen_2016.this, LoginFake.class);
                          intent.putExtra("picture", byteArray);
                          startActivity(intent);

                      }catch (Exception e){
                          customVariablesAndMethod.msgBox(context,""+e);
                      }
                  }else
                  {
                      try {
                          Intent intent=new Intent(SplashScreen_2016.this, LoginMain.class);
                          intent.putExtra("picture", byteArray);
                          startActivity(intent);

                      }catch (Exception e){

                          customVariablesAndMethod.msgBox(context,""+e);

                      }
}
                }

            }
        };
        if (!CheckIfResigned()) {
            thread.start();
        }
    }

    private void alert(String from){
        AppAlert.getInstance().getAlert(context,"test", from);
    }

    public boolean CheckIfResigned(){
       /* if(!customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"doryn","").equals("") ||
                !customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"dosyn","").equals("")){

        }*/
        return false;
    }


    @Override
    protected void onPause() {

        SplashScreen_2016.this.finish();
        super.onPause();
    }



}
