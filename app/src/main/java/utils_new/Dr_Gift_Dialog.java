package utils_new;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import services.Up_Dwn_interface;
import utils.adapterutils.GiftModel;
import utils.adapterutils.MyAdapter2;
import utils.networkUtil.NetworkUtil;
import utils.upload_download;

public class Dr_Gift_Dialog implements Up_Dwn_interface {

    Handler h1;
    Integer response_code;
    Bundle Msg;


    ListView mylist;
    Button save;
    int PA_ID=0;
    int cnt;
    boolean check;
    ResultSet rs;
    String val1="";
    String val2="";
    ArrayAdapter<GiftModel> adapter;
    List<GiftModel> list=new ArrayList<GiftModel>();
    CBO_DB_Helper cbohelp;
    ArrayList<String>data1,data2,data3;
    StringBuilder sb1,sb2;
    String gift_name="",gift_qty="";
    Custom_Variables_And_Method customVariablesAndMethod;
    Context context;

    Dialog dialog;
    public ProgressDialog progress1;
    private  static final int MESSAGE_INTERNET=1;


    public Dr_Gift_Dialog(@NonNull Context context, Handler hh, Bundle Msg, Integer response_code) {

        this.context = context;
        h1=hh;
        this.response_code=response_code;
        this.Msg=Msg;
    }

    public void Show() {
        //this.onNameChange = onNameChange;
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dr_gift, null, false);

        ((Activity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.setContentView(view);
        final Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawableResource(R.color.White_new);
        window.setGravity(Gravity.CENTER);


        TextView hader_text = (TextView) view.findViewById(R.id.hadder_text_1);
        hader_text.setText("Dr. Gift");
        // hader_text.setText( Msg.getString("header"));

        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();


        mylist=(ListView) view.findViewById(R.id.dr_gift_list);
        save=(Button) view.findViewById(R.id.dr_gift_save);
        PA_ID= Custom_Variables_And_Method.PA_ID;
        data1=new ArrayList<String>();
        data2=new ArrayList<String>();
        data3=new ArrayList<String>();
        sb1=new StringBuilder();
        sb2=new StringBuilder();
        cbohelp=new  CBO_DB_Helper(context);

        if (Msg != null) {
            gift_name = Msg.getString("gift_name");
            gift_qty = Msg.getString("gift_qty");
        }

        adapter=new MyAdapter2((Activity) context,getModel());


        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                StringBuilder sb3=new StringBuilder();
                String POB="0";
                String Rate="x";
                String Qty="";
                cbohelp.deletedata(Custom_Variables_And_Method.DR_ID,Rate);
                for( cnt=0;cnt<list.size();cnt++){

                    Qty=list.get(cnt).getScore();

                    if(!list.get(cnt).getScore().equals("") && !list.get(cnt).getScore().equals("")){
                        cbohelp.insertdata(Custom_Variables_And_Method.DR_ID, list.get(cnt).getId(), list.get(cnt).getName(), Qty, POB, Rate,"0","0");
                    }



                    sb3.append(list.get(cnt).getId()).append(",");
                    sb2.append(Qty).append(",");



                }
                // mycon.msgBox(sb3.toString());
                // mycon.msgBox(sb2.toString());

                Bundle i = new Bundle();
                i.putString("giftid", sb3.toString());
                i.putString("giftqan", sb2.toString());


                threadMsg(i);
                dialog.dismiss();


            }
        });


        if(adapter.getCount()!=0) {
            mylist.setAdapter(adapter);
            String[] sample_name1= gift_name.split(",");
            String[] sample_qty1= gift_qty.split(",");

            for (int i=0;i<sample_name1.length;i++){
                for (int j=0;j<list.size();j++) {
                    if (sample_name1[i].equals(list.get(j).getName())) {
                        list.get(j).setScore(sample_qty1[i]);
                        list.get(j).setBalance( list.get(j).getBalance() );//+ Integer.parseInt(sample_qty1[i]));
                    }
                }
            }

            dialog.show();

        } else {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
            builder1.setTitle("CBO");
            builder1.setIcon(R.drawable.alert1);
            builder1.setMessage(" No Data In List.." + "\n" + "Please Download Data.....");
            builder1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    NetworkUtil networkUtil = new NetworkUtil(context);
                    if (!networkUtil.internetConneted(context)) {
                        customVariablesAndMethod.Connect_to_Internet_Msg(context);
                    } else {

                        new upload_download(context,Dr_Gift_Dialog.this);
                    }

                }
            });
            builder1.show();
        }






    }


    private void threadMsg(Bundle Msg) {
        Message msgObj = h1.obtainMessage(response_code);
       /* Bundle b = new Bundle();
        b.putString("Error",Msg);*/
        msgObj.setData(Msg);
        h1.sendMessage(msgObj);
    }


    @Override
    public void onDownloadComplete() {
        adapter=new MyAdapter2((Activity) context,getModel());
        mylist.setAdapter(adapter);
        dialog.show();
    }

    private List<GiftModel> getModel() {
        list.clear();
        //int i=0;
        String ItemIdNotIn="0";
        cbohelp.giftDelete();

        Cursor c1=cbohelp.getAllGifts(ItemIdNotIn);
        if(c1.moveToFirst()){
            do{
                list.add(new GiftModel(c1.getString(c1.getColumnIndex("item_name")), c1.getString(c1.getColumnIndex("item_id")), "",
                        c1.getInt(c1.getColumnIndex("STOCK_QTY")), c1.getInt(c1.getColumnIndex("BALANCE"))));

            }while(c1.moveToNext());
        }
        return list;
    }


}
