package locationpkg

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.content.LocalBroadcastManager
import com.cbo.cbomobilereporting.R
import utils.clearAppData.MyCustumApplication


class SetGPS_Setting : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_gps__setting)
        this.setFinishOnTouchOutside(false);

        val pI = intent.getParcelableExtra<Parcelable>("resolution") as PendingIntent
        startIntentSenderForResult(pI.getIntentSender(), 1, null, 0, 0, 0)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val intent = Intent()
        when (requestCode) {
            1 -> when (resultCode) {
                Activity.RESULT_OK -> {
                    val intent = Intent(Const.INTENT_FILTER_LOCATION_SETTING)
                    LocalBroadcastManager.getInstance(MyCustumApplication.getInstance()).sendBroadcast(intent)
                }
                Activity.RESULT_CANCELED -> {
                }
                else -> {
                }

            }
        }
        finish();
    }

}
