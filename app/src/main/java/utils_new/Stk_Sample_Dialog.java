package utils_new;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.uenics.javed.CBOLibrary.Response;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import cbomobilereporting.cbo.com.cboorder.Utils.AddToCartView;
import interfaces.Ipob;
import services.Up_Dwn_interface;
import utils.adapterutils.GiftModel;
import utils.adapterutils.MyAdapter;
import utils.model.DropDownModel;
import utils.networkUtil.NetworkUtil;
import utils_new.CustomDialog.Spinner_Dialog;

;

public class Stk_Sample_Dialog implements Up_Dwn_interface, Ipob {

    Handler h1;
    Integer response_code;
    Bundle Msg;


    ListView mylist;
    LinearLayout save;
    TextView itemincart;
    Custom_Variables_And_Method customVariablesAndMethod;
    Context context;
    int PA_ID = 0;
    ResultSet rs;
    CBO_DB_Helper cbohelp;
    ArrayAdapter<GiftModel> adapter;
    List<GiftModel> main_item_list = new ArrayList<GiftModel>();
    List<GiftModel> display_item_list = new ArrayList<GiftModel>();
    ArrayList<String> data1, data2, data3,data5,item_list;
    StringBuilder sb2, sb3, sb4,sb5,item_list_string;
    double mainval = 0.0;
    EditText search;
    String ID="0";

    String sample_name="",sample_pob="",sample_sample="";
    String sample_name_previous="",sample_pob_previous="",sample_sample_previous="";
    ImageView speciality_filter;

    Dialog dialog;
    public ProgressDialog progress1;
    private  static final int MESSAGE_INTERNET=1;
    ArrayList<DropDownModel> Specialitis;


    public Stk_Sample_Dialog(@NonNull Context context, Handler hh, Bundle Msg, Integer response_code) {

        this.context = context;
        h1=hh;
        this.response_code=response_code;
        this.Msg=Msg;
    }


    public void Show() {

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.stk_sample, null, false);

        ((AppCompatActivity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.setContentView(view);
        final Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawableResource(R.color.White_new);
        window.setGravity(Gravity.CENTER);



        TextView hader_text =(TextView) view.findViewById(R.id.hadder_text_1);
        hader_text.setText("Stockist Sample");


        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();

        search= (EditText) view.findViewById(R.id.search);
        mylist = (ListView) view.findViewById(R.id.stk_sample_list);
        mylist.setItemsCanFocus(true);
        mylist.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        save = view.findViewById(R.id.stk_sample_save);
        itemincart = view.findViewById(R.id.itemincart);
        speciality_filter = view.findViewById(R.id.filter);
        PA_ID = Custom_Variables_And_Method.PA_ID;
        data1 = new ArrayList<String>();
        data2 = new ArrayList<String>();
        data3 = new ArrayList<String>();
        data5 = new ArrayList<String>();
        item_list = new ArrayList<String>();
        cbohelp = new CBO_DB_Helper(context);
        sb2 = new StringBuilder();
        sb3 = new StringBuilder();
        sb4 = new StringBuilder();
        sb5 = new StringBuilder();
        item_list_string = new StringBuilder();

        if (Msg != null) {
            ID = Msg.getString("ID");
            sample_name = Msg.getString("sample_name");
            sample_pob = Msg.getString("sample_pob");
            sample_sample = Msg.getString("sample_sample");

            sample_name_previous = Msg.getString("sample_name_previous");
            sample_pob_previous = Msg.getString("sample_pob_previous");
            sample_sample_previous = Msg.getString("sample_sample_previous");
        }

        getModelLocal();

        if (main_item_list.size()>0) {
            adapter = new MyAdapter((AppCompatActivity) context, display_item_list,this);



            String[] sample_name1= sample_name.split(",");
            String[] sample_qty1= sample_sample.split(",");
            String[] sample_pob1= sample_pob.split(",");

            for (int i=0;i<sample_name1.length;i++){
                for (int j=0;j<main_item_list.size();j++) {
                    if (sample_name1[i].equals(main_item_list.get(j).getName())) {
                        main_item_list.get(j).setScore(sample_pob1[i]);
                        main_item_list.get(j).setSample(sample_qty1[i]);
                        //display_item_list.get(j).setBalance( display_item_list.get(j).getBalance() + Integer.parseInt(sample_qty1[i]));
                        break;
                    }
                }
            }


            String[] sample_name1_previous= sample_name_previous.split(",");
            String[] sample_qty1_previous= sample_sample_previous.split(",");

            for (int i=0;i<sample_name1_previous.length;i++){
                for (int j=0;j<main_item_list.size();j++) {
                    if (sample_name1_previous[i].equals(main_item_list.get(j).getName())) {
                        main_item_list.get(j).setBalance( main_item_list.get(j).getBalance() + Integer.parseInt(sample_qty1_previous[i]));
                    }
                }
            }
            mylist.setAdapter(adapter);
            onItemSelectedListChanged();

        }else{
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

                        //new upload_download(context);
                        new Service_Call_From_Multiple_Classes().getListForLocal(context, new Response() {
                            @Override
                            public void onSuccess(Bundle bundle) {
                                onDownloadComplete();

                            }

                            @Override
                            public void onError(String message, String description) {
                                AppAlert.getInstance().getAlert(context,message,description);
                            }
                        });
                    }

                }
            });
            builder1.show();
        }


        if (customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"DR_DIVISION_FILTER_YN","N").equals("N")){

            speciality_filter.setVisibility(View.GONE);

        }

        speciality_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Specialitis == null){
                    Specialitis = cbohelp.get_Specialitis();
                }
                new Spinner_Dialog(context, Specialitis, new Spinner_Dialog.OnItemClickListener() {
                    @Override
                    public void ItemSelected(DropDownModel item) {
                        filer_item_speciality(item.getId());
                    }
                }).show();
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                for (int l = 0; l < display_item_list.size(); l++) {
                    if (!search.getText().toString().equals("") && search.getText().length() <= display_item_list.get(l).getName().length()) {
                        if (display_item_list.get(l).getName().toLowerCase().contains(search.getText().toString().toLowerCase().trim())) {
                            //mylist.smoothScrollToPosition(l);
                            mylist.smoothScrollToPositionFromTop(l,l,500);
                            for (int j = l; j < display_item_list.size(); j++) {
                                if (search.getText().length() <= display_item_list.get(j).getName().length()) {
                                    if (display_item_list.get(j).getName().toLowerCase().contains(search.getText().toString().toLowerCase().trim())) {
                                        display_item_list.get(j).setHighlight(true);
                                    }else{
                                        display_item_list.get(j).setHighlight(false);
                                    }
                                }else{
                                    display_item_list.get(j).setHighlight(false);
                                }
                            }
                            break;
                        }else{
                            display_item_list.get(l).setHighlight(false);
                        }
                    }else{
                        display_item_list.get(l).setHighlight(false);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                String total_pob="";
                boolean count=false,check;

                for (int i = 0; i < main_item_list.size(); i++) {
                    check = main_item_list.get(i).isSelected();
                    if (check ) {
                        count=true;
                        break;
                    }
                }

                if (count) {
                    item_list.clear();
                    data1.clear();
                    data2.clear();
                    data3.clear();
                    data5.clear();
                    for (int i = 0; i < main_item_list.size(); i++) {
                        check = main_item_list.get(i).isSelected();

                        if (check && !main_item_list.get(i).getScore().equals("")) {
                            total_pob = main_item_list.get(i).getScore();
                            break;
                        }
                    }
                    try {

                        for (int i = 0; i < main_item_list.size(); i++) {
                            check = main_item_list.get(i).isSelected();
                            if (check) {
                                data1.add(main_item_list.get(i).getId());
                                item_list.add(main_item_list.get(i).getName());
                                //pob
                                if (main_item_list.get(i).getScore() == null || main_item_list.get(i).getScore().equals("")) {
                                    data2.add("0");
                                } else {
                                    data2.add(main_item_list.get(i).getScore());
                                }

                                data3.add(main_item_list.get(i).getRate());
                                //sample
                                if (main_item_list.get(i).getSample() == null || main_item_list.get(i).getSample().equals("")) {
                                    data5.add("0");
                                } else {
                                    data5.add(main_item_list.get(i).getSample());
                                }
                            } else {
                                item_list.remove(check);
                                data1.remove(check);
                                data2.remove(check);
                                data3.remove(check);
                                data5.remove(check);
                            }

                        }


                        for (int i = 0; i < data1.size(); i++) {

                            sb3.append(data1.get(i)).append(",");
                            item_list_string.append(item_list.get(i)).append(",");
                            sb2.append(data2.get(i)).append(",");
                            sb4.append(data3.get(i)).append(",");
                            sb5.append(data5.get(i)).append(",");
                        }

                        String rateval = (String) sb4.toString();
                        if (data3.isEmpty() || data1.isEmpty()) {
                            mainval = 0.0;
                            rateval += "" + 0.0 + ",";
                        }
                        String rateval1[] = rateval.split(",");
                        Double[] intarray = new Double[rateval1.length];
                        for (int i = 0; i < rateval1.length; i++) {
                            intarray[i] = Double.parseDouble(rateval1[i]);
                        }

                        String pobval = sb2.toString();
                        if (data3.isEmpty() || data1.isEmpty()) {
                            mainval = 0.0;
                            pobval += "" + 0.0 + ",";
                        }
                        String pobval1[] = pobval.split(",");
                        Double pobarray[] = new Double[pobval1.length];
                        for (int i = 0; i < pobval1.length; i++) {
                            pobarray[i] = Double.parseDouble(pobval1[i]);
                        }
                        for (int i = 0; i < intarray.length; i++) {
                            if (data3.isEmpty() || data1.isEmpty()) {

                                pobval += "" + 0.0 + ",";
                                rateval += "" + 0.0 + ",";
                                pobarray[i] = 0.0;
                                intarray[i] = 0.0;
                                mainval += 0.0;
                            }

                            try {
                                mainval += (pobarray[i] * intarray[i]);
                            } catch (ArrayIndexOutOfBoundsException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        customVariablesAndMethod.msgBox(context, "Wrong Input Please Try Again..");
                    }
                }
                if (customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"SAMPLE_POB_MANDATORY").equals("Y") && total_pob.equals("")) {
                    customVariablesAndMethod.msgBox(context,"POB can't be blank");
                    sb2 = new StringBuilder();
                    sb3 = new StringBuilder();
                    sb4 = new StringBuilder();
                    sb5 = new StringBuilder();
                    item_list_string = new StringBuilder();
                }else {

                    Bundle i = new Bundle();
                    i.putString("val", sb3.toString());
                    i.putString("val2", sb2.toString());
                    i.putString("sampleQty", sb5.toString());
                    i.putDouble("resultpob", mainval);
                    i.putString("resultRate", sb4.toString());
                    i.putString("resultList", item_list_string.toString());

                    threadMsg(i);
                    dialog.dismiss();
                }

                }
        });

        dialog.show();
    }


    private List<GiftModel> getModelLocal() {
        main_item_list.clear();
        display_item_list.clear();
       // String ItemIdNotIn = "0";
        Cursor c = cbohelp.getAllProducts(ID,"Stk");
        if (c.moveToFirst()) {
            do {
                main_item_list.add(new GiftModel(c.getString(c.getColumnIndex("item_name")), c.getString(c.getColumnIndex("item_id")), c.getString(c.getColumnIndex("stk_rate")),
                        c.getInt(c.getColumnIndex("STOCK_QTY")), c.getInt(c.getColumnIndex("BALANCE")),c.getInt(c.getColumnIndex("SPL_ID"))));
            } while (c.moveToNext());
        }

        display_item_list.addAll(main_item_list);
        return main_item_list;
    }

    private void filer_item_speciality(String SPL_ID){
        display_item_list.clear();
        if (SPL_ID.equalsIgnoreCase("0")){
            display_item_list.addAll(main_item_list);
        }else {
            for (GiftModel item : main_item_list) {
                if (item.getSPL_ID() == Integer.parseInt(SPL_ID)) {
                    display_item_list.add(item);
                }

            }
        }
        adapter = new MyAdapter((AppCompatActivity) context, display_item_list,this::onItemSelectedListChanged);
        mylist.setAdapter(adapter);
        onItemSelectedListChanged();
    }
    @Override
    public void onDownloadComplete() {
        getModelLocal();
        adapter = new MyAdapter((AppCompatActivity) context, display_item_list,this::onItemSelectedListChanged);
        mylist.setAdapter(adapter);
        onItemSelectedListChanged();
    }

    private void threadMsg(Bundle Msg) {
        Message msgObj = h1.obtainMessage(response_code);
       /* Bundle b = new Bundle();
        b.putString("Error",Msg);*/
        msgObj.setData(Msg);
        h1.sendMessage(msgObj);
    }

    @Override
    public void onItemSelectedListChanged(){
        Double total_pob=0D;
        int items = 0;
        for (int i = 0; i < display_item_list.size(); i++) {

            if (display_item_list.get(i).isSelected()) {
                items ++;
                total_pob += Double.parseDouble( display_item_list.get(i).getScore()) * Double.parseDouble( display_item_list.get(i).getRate());

            }
        }
        if (total_pob>0){
            itemincart.setText( "POB : " + AddToCartView.toCurrency(String.format("%.2f", (total_pob))) + " (" + items + " items )" );
        }else{
            itemincart.setText(items + " item");
        }
    }
}
