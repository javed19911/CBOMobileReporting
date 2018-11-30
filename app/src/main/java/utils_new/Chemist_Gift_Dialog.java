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

public class Chemist_Gift_Dialog  implements Up_Dwn_interface {

    ListView mylist;
    Button save;
    Custom_Variables_And_Method customVariablesAndMethod;
    Context context;
    int PA_ID=0;
    ResultSet rs;
    String val1="";
    String val2="";
    ArrayAdapter<GiftModel> adapter;
    CBO_DB_Helper cbohelp;
    List<GiftModel> list=new ArrayList<GiftModel>();
    ArrayList<String>item_qty,item_id,item_name;
    StringBuilder sb1,sb2;
    String gift_name="",gift_qty="";
    String gift_name_previous="",gift_qty_previous="";



    Handler h1;
    Integer response_code;
    Bundle Msg;

    Dialog dialog;
    public ProgressDialog progress1;
    private  static final int MESSAGE_INTERNET=1;


    public Chemist_Gift_Dialog(@NonNull Context context, Handler hh, Bundle Msg, Integer response_code) {

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
        View view = inflater.inflate(R.layout.chemist_gift, null, false);

        ((Activity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.setContentView(view);
        final Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawableResource(R.color.White_new);
        window.setGravity(Gravity.CENTER);


       /* TextView hader_text = (TextView) view.findViewById(R.id.hadder_text_1);
        hader_text.setText("Sample/POB");*/
        // hader_text.setText( Msg.getString("header"));


        mylist=(ListView) view.findViewById(R.id.chm_gift_list);
        save=(Button) view.findViewById(R.id.chm_gift_save);

        customVariablesAndMethod=Custom_Variables_And_Method.getInstance(context);

        PA_ID= Custom_Variables_And_Method.PA_ID;
        item_qty=new ArrayList<String>();
        item_id=new ArrayList<String>();
        item_name=new ArrayList<String>();
        sb1=new StringBuilder();
        sb2=new StringBuilder();
        cbohelp=customVariablesAndMethod.get_cbo_db_instance();


        if (Msg != null) {
            gift_name = Msg.getString("gift_name");
            gift_qty = Msg.getString("gift_qty");

            gift_name_previous = Msg.getString("gift_name_previous");
            gift_qty_previous = Msg.getString("gift_qty_previous");
        }

        getModelLocal();

        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                StringBuilder sb3=new StringBuilder();

                item_qty.clear();
                item_id.clear();
                item_name.clear();

                for(int i=0;i<list.size();i++){
                    if(!list.get(i).getScore().equals("") && !list.get(i).getScore().equals("")){
                        item_id.add(list.get(i).getId());
                        item_qty.add(list.get(i).getScore());
                        item_name.add(list.get(i).getName());
                    }

                }


                for(int i=0;i<item_id.size();i++){

                    sb3.append(item_id.get(i)).append(",");
                    sb2.append(item_qty.get(i)).append(",");
                    sb1.append(item_name.get(i)).append(",");
                }
                Bundle i = new Bundle();
                i.putString("giftid", sb3.toString());
                i.putString("giftqan", sb2.toString());
                i.putString("giftname", sb1.toString());




                threadMsg(i);
                dialog.dismiss();


            }
        });


        if (list.size()>0) {
            adapter = new MyAdapter2((Activity) context, list);
            mylist.setAdapter(adapter);

            String[] sample_name1= gift_name.split(",");
            String[] sample_qty1= gift_qty.split(",");

            for (int i=0;i<sample_name1.length;i++){
                for (int j=0;j<list.size();j++) {
                    if (sample_name1[i].equals(list.get(j).getName())) {
                        list.get(j).setScore(sample_qty1[i]);
                    }
                }
            }


            String[] sample_name1_previous= gift_name_previous.split(",");
            String[] sample_qty1_previous= gift_qty_previous.split(",");

            for (int i=0;i<sample_name1_previous.length;i++){
                for (int j=0;j<list.size();j++) {
                    if (sample_name1_previous[i].equals(list.get(j).getName())) {
                        list.get(j).setBalance( list.get(j).getBalance() + Integer.parseInt(sample_qty1_previous[i]));
                    }
                }
            }
            dialog.show();

        }else{
            AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
            builder1.setTitle("CBO");
            builder1.setIcon(R.drawable.alert1);
            builder1.setMessage(" No Data In List.." + "\n" + "Please Download Data.....");
            builder1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog1, int which) {
                    NetworkUtil networkUtil = new NetworkUtil(context);
                    if (!networkUtil.internetConneted(context)) {
                        customVariablesAndMethod.Connect_to_Internet_Msg(context);
                    } else {

                        new upload_download(context,Chemist_Gift_Dialog.this);

                    }

                }
            });
            builder1.show();
        }






    }


    private List<GiftModel> getModelLocal() {
        list.clear();

        String ItemIdNotIn="0";
        //cbohelp.giftDelete();

        Cursor c1=cbohelp.getAllGifts(ItemIdNotIn);
        if(c1.moveToFirst()){
            do{

                list.add(new GiftModel(c1.getString(c1.getColumnIndex("item_name")), c1.getString(c1.getColumnIndex("item_id")), "",
                        c1.getInt(c1.getColumnIndex("STOCK_QTY")), c1.getInt(c1.getColumnIndex("BALANCE"))));

            }while(c1.moveToNext());
        }
        return list;
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
        getModelLocal();
        adapter=new MyAdapter2((Activity) context,list);
        mylist.setAdapter(adapter);
        dialog.show();
    }

}
