package com.cbo.cbomobilereporting.ui_new;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.os.Environment;
import android.util.Base64;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.cbo.cbomobilereporting.MyCustumApplication;
import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.ui.LoginFake;
import com.cbo.cbomobilereporting.ui.LoginMain;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

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
        customVariablesAndMethod.deleteFmcg_ByKey(context, "logo");

        Bundle extras = getIntent().getExtras();

        ImageView image = (ImageView) findViewById(R.id.image_blur);

        File COMPANY_PIC= new File( Environment.getExternalStorageDirectory()+"/cbo/profile/company.png" );
        if( null != extras && extras.getByteArray("picture") !=null  ) {

            byteArray=extras.getByteArray("picture");
            Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            //ImageView image = (ImageView) findViewById(R.id.image_blur);
            image.setImageBitmap(bmp);
            image.setVisibility(View.GONE);
        } if  (customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"COMPANY_PICYN","").equalsIgnoreCase("Y")
                && COMPANY_PIC.exists()
                && !MyCustumApplication.getInstance().getUser().getID().equalsIgnoreCase("0")) {
            byte bytes[] = new byte[(int) COMPANY_PIC.length()];
            BufferedInputStream bis = null;
            DataInputStream dis = null;
            try {
                bis = new BufferedInputStream(new FileInputStream(COMPANY_PIC));
                dis = new DataInputStream(bis);
                dis.readFully(bytes);

                //byteArray = bytes;
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                //ImageView image = (ImageView) findViewById(R.id.image_blur);
                image.setImageBitmap(bmp);
            } catch (FileNotFoundException e) {
                byteArray = null;
                e.printStackTrace();
            } catch (IOException e) {
                byteArray = null;
                e.printStackTrace();
            }catch (Exception e){
                byteArray = null;
                e.printStackTrace();
            }

        }


        if (MyCustumApplication.getInstance().getUser().getID().equalsIgnoreCase("0")){
            image.setVisibility(View.GONE);
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
