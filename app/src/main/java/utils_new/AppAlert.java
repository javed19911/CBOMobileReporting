package utils_new;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.cbo.cbomobilereporting.MyCustumApplication;
import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.ui_new.SystemAlertActivity;


/**
 * Created by cboios on 28/05/18.
 */

public class AppAlert {


    private String positiveTxt = "OK";
    private String nagativeTxt = "Cancel";
    private static AppAlert ourInstance = null;

    private AppAlert() {
        super();
    }

    public static AppAlert getInstance() {
        if (ourInstance == null) {
            ourInstance = new AppAlert();
        }
        return ourInstance;
    }

    public String getPositiveTxt() {
        return positiveTxt;
    }

    public AppAlert setPositiveTxt(String positiveTxt) {
        this.positiveTxt = positiveTxt;
        return this;
    }

    public String getNagativeTxt() {
        return nagativeTxt;
    }

    public AppAlert setNagativeTxt(String nagativeTxt) {
        this.nagativeTxt = nagativeTxt;
        return this;
    }

    public void getAlert(Context context, String title, String message) {
        getAlert(context,title,message,null,null,false);
    }
    public void getAlert(Context context, String title, String message, Boolean resultVisible) {
        getAlert(context,title,message,null,null,resultVisible);
    }
    public void getAlert(Context context, String title, String message, String url) {
        getAlert(context,title,message,null,url,false);
    }
    public void getAlert(Context context, String title, String[] table_list) {
        getAlert(context,title,null,table_list,null,false);
    }
    public void getAlert(Context context, final String title, final String message, String[] table_list, final String url, Boolean reportVisible) {

        //Context context = MyCustumApplication.getInstance();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogLayout = inflater.inflate(R.layout.alert_view, null);
        final TextView Alert_title= (TextView) dialogLayout.findViewById(R.id.title);

        final TextView Alert_message= (TextView) dialogLayout.findViewById(R.id.message);
        final TableLayout Alert_message_list= (TableLayout) dialogLayout.findViewById(R.id.table_view);
        final Button Alert_Positive= (Button) dialogLayout.findViewById(R.id.positive);
        Alert_title.setText(title);

        final TextView pa_id_txt= (TextView) dialogLayout.findViewById(R.id.PA_ID);
        pa_id_txt.setText(""+ Custom_Variables_And_Method.PA_ID);

        final TextView report= (TextView) dialogLayout.findViewById(R.id.report);
        if (reportVisible) {
            report.setVisibility(View.VISIBLE);
        }

        if (table_list==null ) {
            Alert_message.setText(message);
            Alert_message_list.setVisibility(View.GONE);
        }else{
            Alert_message.setVisibility(View.GONE);
            TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 1f);
            TableRow.LayoutParams params1 = new TableRow.LayoutParams(0, 1, 1f);
            Alert_message_list.removeAllViews();
            for (int i = 0; i < table_list.length; i++) {
                TableRow tbrow = new TableRow(context);
                if ( !table_list[i].contains(":")) {
                    TextView t1v = new TextView(context);
                    t1v.setText(table_list[i].replace("\n",""));
                    t1v.setPadding(15, 10, 15, 10);
                    t1v.setBackgroundColor(0xff5477cf);
                    t1v.setTextColor(Color.WHITE);
                    t1v.setTextSize(16);
                    t1v.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                    t1v.setLayoutParams(params);
                    tbrow.addView(t1v);
                }else{
                    TextView t1v = new TextView(context);
                    t1v.setText(table_list[i]);
                    t1v.setPadding(15, 5, 15, 0);
                    t1v.setTextColor(Color.BLACK);
                    t1v.setLayoutParams(params);
                    t1v.setTypeface(Typeface.MONOSPACE, Typeface.NORMAL);
                    tbrow.addView(t1v);
                }
               /* TextView t2v = new TextView(context);
                t2v.setText(table_list[i]);
                t2v.setPadding(5, 5, 5, 0);
                t2v.setTextColor(Color.BLACK);
                t2v.setGravity(Gravity.CENTER);
                tbrow.addView(t2v);*/
                TableRow tbrow1 = new TableRow(context);
                TextView t3v = new TextView(context);
                t3v.setPadding(15, 1, 15, 0);
                t3v.setLayoutParams(params1);
                t3v.setBackgroundColor(0xff125688);
                tbrow1.addView(t3v);
                Alert_message_list.addView(tbrow);
                Alert_message_list.addView(tbrow1);
            }
        }


        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);


        final AlertDialog dialog = builder1.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.setView(dialogLayout);
        Alert_Positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (url!=null && !url.isEmpty()){
                   /* Intent i = new Intent(context, CustomWebView.class);
                    i.putExtra("A_TP", url);
                    i.putExtra("Title", title);
                    context.startActivity(i);*/
                    MyCustumApplication.getInstance().LoadURL(title,url);
                }
                dialog.dismiss();
            }
        });

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Location currentBestLocation=getObject(context,"currentBestLocation",Location.class);
                List toEmailList = Arrays.asList("mobilereporting@cboinfotech.com".split("\\s*,\\s*"));
                *//*new SendMailTask().execute("mobilereporting@cboinfotech.com",
                        "mreporting",toEmailList , Custom_Variables_And_Method.COMPANY_CODE+": Out of Range Error report",context.getResources().getString(R.string.app_name)+"\n Company Code :"+Custom_Variables_And_Method.COMPANY_CODE+"\n DCR ID :"+Custom_Variables_And_Method.DCR_ID+"\n PA ID : "+Custom_Variables_And_Method.PA_ID+"\n App version : "+Custom_Variables_And_Method.VERSION+"\n message : "+massege+"\n Error Alert :"+title+"\n"+
                "\nLocation-timestamp : "+currentBestLocation.getTime()+"\nLocation-Lat : "+currentBestLocation.getLatitude()+
                        "\nLocation-long : "+currentBestLocation.getLongitude()+"\n time : " +currentTime(context)+"\nlatlong : "+ getDataFrom_FMCG_PREFRENCE(context,"shareLatLong",Custom_Variables_And_Method.GLOBAL_LATLON));
*//*

                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(ExpenseRoot.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //takePictureButton.setEnabled(false);
                    ActivityCompat.requestPermissions(context, new String[] { Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE }, ExpenseRoot.this.REQUEST_CAMERA);
                    Toast.makeText(context, "Please allow the permission", Toast.LENGTH_LONG).show();

                }else {

                    capture_Image();
                }*/
                //new SendAttachment((Activity) context).execute("HELLO JAVED",);
                dialog.dismiss();
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }

    public void inputAlert(Context context, String title, String message, String hint, OnClickListener listener){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogLayout = inflater.inflate(R.layout.input_alert_view, null);
        final TextView Alert_title= (TextView) dialogLayout.findViewById(R.id.title);

        final TextView Alert_message= (TextView) dialogLayout.findViewById(R.id.message);
        final TextView Alert_inputTxt= (TextView) dialogLayout.findViewById(R.id.inputTxt);
        final Button Alert_Positive= (Button) dialogLayout.findViewById(R.id.positive);
        Alert_title.setText(title);

        Alert_message.setText(hint);
        Alert_inputTxt.setHint(hint);
        final Button Alert_negative= dialogLayout.findViewById(R.id.negative);
        //Alert_message.setText(massege);
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);


        final AlertDialog dialog = builder1.create();

        dialog.setView(dialogLayout);
        Alert_Positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null ){
                    listener.onPositiveClicked(view,Alert_inputTxt.getText().toString());
                }
                dialog.dismiss();
            }
        });

        Alert_negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null ) {
                    listener.onNegativeClicked(view, Alert_inputTxt.getText().toString());
                }
                dialog.dismiss();
            }
        });
        //dialog.setCancelable(false);
        dialog.show();
    }


    public void Alert(Context context, String title, String message, View.OnClickListener listener){

        //Context context = MyCustumApplication.getInstance();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogLayout = inflater.inflate(R.layout.alert_view, null);
        final TextView Alert_title= (TextView) dialogLayout.findViewById(R.id.title);

        final TextView Alert_message= (TextView) dialogLayout.findViewById(R.id.message);
        final TableLayout Alert_message_list= (TableLayout) dialogLayout.findViewById(R.id.table_view);
        final Button Alert_Positive= (Button) dialogLayout.findViewById(R.id.positive);
        Alert_title.setText(title);

        final TextView pa_id_txt= (TextView) dialogLayout.findViewById(R.id.PA_ID);
        pa_id_txt.setText(""+ Custom_Variables_And_Method.PA_ID);

        Alert_message.setText(message);
        Alert_message_list.setVisibility(View.GONE);
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);


        final AlertDialog dialog = builder1.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.setView(dialogLayout);
        Alert_Positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null ){
                    listener.onClick(view);
                }
                dialog.dismiss();
            }
        });

        dialog.setCancelable(false);
        dialog.show();

     /*   LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        BottomSheetDialog dialog = new BottomSheetDialog(context);
        final View dialogLayout = inflater.inflate(R.layout.alert_view, null);

        final TextView Alert_title= (TextView) dialogLayout.findViewById(R.id.title);

        final TextView Alert_message= (TextView) dialogLayout.findViewById(R.id.message);
        final TableLayout Alert_message_list= (TableLayout) dialogLayout.findViewById(R.id.table_view);
        final Button Alert_Positive= (Button) dialogLayout.findViewById(R.id.positive);
        Alert_title.setText(title);

        final TextView pa_id_txt= (TextView) dialogLayout.findViewById(R.id.PA_ID);
        pa_id_txt.setText(""+ Custom_Variables_And_Method.PA_ID);

        Alert_message.setText(massege);
        Alert_message_list.setVisibility(View.GONE);
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);


        Alert_Positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null ){
                    listener.onClick(view);
                }
                dialog.dismiss();
            }
        });

        dialog.setContentView(dialogLayout);
        dialog.show();*/

    }

    public void SystemAlert( String title, String message, View.OnClickListener listener){

        Context context = MyCustumApplication.getInstance();

        Intent intent = new Intent(context, SystemAlertActivity.class);
        intent.putExtra("title",title);
        intent.putExtra("message",message);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void DecisionAlert(Context context, String title, String message, OnClickListener listener){

        //Context context = MyCustumApplication.getInstance();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogLayout = inflater.inflate(R.layout.alert_view, null);
        final TextView Alert_title= (TextView) dialogLayout.findViewById(R.id.title);

        final TextView Alert_message= (TextView) dialogLayout.findViewById(R.id.message);
        final TableLayout Alert_message_list= (TableLayout) dialogLayout.findViewById(R.id.table_view);
        final Button Alert_Positive= (Button) dialogLayout.findViewById(R.id.positive);
        final Button Alert_negative= dialogLayout.findViewById(R.id.negative);
        Alert_title.setText(title);
        Alert_Positive.setText(getPositiveTxt());
        Alert_negative.setText(getNagativeTxt());

        setNagativeTxt("Cancel");
        setPositiveTxt("OK");

        final TextView pa_id_txt= (TextView) dialogLayout.findViewById(R.id.PA_ID);
        pa_id_txt.setText(""+ Custom_Variables_And_Method.PA_ID);

        Alert_message.setText(message);
        Alert_message_list.setVisibility(View.GONE);
        Alert_negative.setVisibility(View.VISIBLE);
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);


        final AlertDialog dialog = builder1.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.setView(dialogLayout);
        Alert_Positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null ){
                    listener.onPositiveClicked(view,"");
                }
                dialog.dismiss();
            }
        });

        Alert_negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null ) {
                    listener.onNegativeClicked(view, "");
                }
                dialog.dismiss();
            }
        });
        dialog.setCancelable(false);
        dialog.show();

    }

    public interface OnClickListener{
        public void onPositiveClicked(View item, String result);
        public void onNegativeClicked(View item, String result);
    }
}
