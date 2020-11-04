package utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;

/**
 * Created by pc24 on 26/12/2016.
 */

public class Update_Avilable extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_available);

        androidx.appcompat.widget.Toolbar toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar_hadder);
        TextView hader_text = (TextView) findViewById(R.id.hadder_text_1);
        hader_text.setText("New Version Available");

        getAppUpdateAlert(this, "Update Available", "Please Update your APP to avail the latest Features");
    }


     void getAppUpdateAlert(final Context context, String title, String message) {

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogLayout = inflater.inflate(R.layout.update_available_alert_view, null);
        final TextView Alert_title= (TextView) dialogLayout.findViewById(R.id.title);
        final TextView Alert_message= (TextView) dialogLayout.findViewById(R.id.message);
        final Button Alert_Positive= (Button) dialogLayout.findViewById(R.id.positive);
        final Button Alert_Nagative= (Button) dialogLayout.findViewById(R.id.nagative);
        Alert_title.setText(title);
        Alert_message.setText(message);


        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);


        final AlertDialog dialog = builder1.create();

        dialog.setView(dialogLayout);
        Alert_Positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW,

                        Uri.parse("https://play.google.com/store/apps/details?id=com.cbo.cbomobilereporting&hl=en"));
                context.startActivity(i);
                finish();
            }
        });
        Alert_Nagative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                finish();
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }
}
