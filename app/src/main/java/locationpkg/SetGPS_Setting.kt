package locationpkg

import androidx.appcompat.app.AppCompatActivity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import com.cbo.cbomobilereporting.R
import com.cbo.cbomobilereporting.MyCustumApplication
import com.uenics.javed.CBOLibrary.Response
import utils_new.AppAlert
import utils_new.Custom_Variables_And_Method
import utils_new.Service_Call_From_Multiple_Classes


class SetGPS_Setting : AppCompatActivity() {

    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_gps__setting)
        this.setFinishOnTouchOutside(false);

        context = this;

        if (intent.getStringExtra("code").equals("DCRDOWNLOADALL")){
            DownloadAll();
        }else{
            val pI = intent.getParcelableExtra<Parcelable>("resolution") as PendingIntent
            startIntentSenderForResult(pI.getIntentSender(), 1, null, 0, 0, 0)
        }

    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val intent = Intent()
        when (requestCode) {
            1 -> when (resultCode) {
                AppCompatActivity.RESULT_OK -> {
                    val intent = Intent(Const.INTENT_FILTER_LOCATION_SETTING)
                    androidx.localbroadcastmanager.content.LocalBroadcastManager.getInstance(MyCustumApplication.getInstance()).sendBroadcast(intent)
                }
                AppCompatActivity.RESULT_CANCELED -> {
                }
                else -> {
                }

            }
        }
        finish();
    }

    private fun DownloadAll(){

         Service_Call_From_Multiple_Classes().DownloadAll(
                 context, object :Response {
                     override fun onSuccess( bundle : Bundle) {
                         Custom_Variables_And_Method.getInstance().msgBox(context, "Data Downloded Sucessfully...");
                         finish();
                     }

                     override fun onError( s : String,  s1 : String) {

                         AppAlert.getInstance().Alert(context,s,s1,object : View.OnClickListener{
                             override fun onClick(v: View?) {
                                 finish();
                             }

                         });
                     }
                 }
         );
    }

}
