package fcm;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

import utils_new.Custom_Variables_And_Method;

/**
 * Created by pc24 on 09/12/2016.
 */

//Class extending FirebaseInstanceIdService
public class MyFirebaseInstanceIDService {
       // extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";
    Custom_Variables_And_Method customVariablesAndMethod;

    //@Override
    public void onTokenRefresh() {

        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
        //Getting registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        //Displaying token on logcat
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        sendRegistrationToServer(refreshedToken);

    }

    private void sendRegistrationToServer(String token) {
        //You can implement this method to store the token on your server
        //Not required for current project
        //customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(this,"GCMToken",token);


    }
}