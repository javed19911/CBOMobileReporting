package com.cbo.cbomobilereporting;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.cbo.cbomobilereporting.ui.LoginFake;


/**
 * Created by whit3hawks on 11/16/16.
 */

public class FingerprintHandler {

    CancellationSignal cancellationSignal;

    private Context context;


    // Constructor
    @RequiresApi(api = Build.VERSION_CODES.M)
    public FingerprintHandler(Context mContext) {
        context = mContext;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject) {
        cancellationSignal = new CancellationSignal();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.authenticate(cryptoObject, cancellationSignal, 0, new FingerprintManager.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                update("Fingerprint Authentication error\n" + errString, false);
            }

            @Override
            public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
                update("Fingerprint Authentication help\n" + helpString, false);
            }

            @Override
            public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
                update("Fingerprint Authentication failed.", true);
            }

            @Override
            public void onAuthenticationFailed() {
                update("Fingerprint Authentication succeeded.", false);
            }
        }, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void stopAuth(){
        if(cancellationSignal != null && !cancellationSignal.isCanceled()){
            cancellationSignal.cancel();
        }
    }


    public void update(String e, Boolean success){
        if(success) {
            //AppAlert.getInstance().getAlert(context, "Success", "FingerPrint Validated");
            ((LoginFake)context).pin.setText(((LoginFake)context).MyPin);
            ((LoginFake)context).LoginFake(false);
        }
        /*TextView textView = (TextView) ((Activity)context).findViewById(R.id.errorText);
        textView.setText(e);
        if(success){
            textView.setTextColor(ContextCompat.getColor(context,R.color.colorPrimaryDark));
        }*/
    }
}
