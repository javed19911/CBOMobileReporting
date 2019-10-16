package bill.Cart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.cbo.cbomobilereporting.MyCustumApplication;
import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.ui_new.CustomActivity;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.Expense.mOthExpense;
import com.uenics.javed.CBOLibrary.CBOServices;
import com.uenics.javed.CBOLibrary.ResponseBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import bill.BillReport.aPayMode;
import bill.BillReport.mPay;
import bill.mBillOrder;
import cbomobilereporting.cbo.com.cboorder.Utils.AddToCartView;
import saleOrder.MyOrderAPIService;
import utils_new.AppAlert;
import utils_new.CustomDatePicker;

public class AddCustomer extends CustomActivity {

    mCustomer customer = new mCustomer();

    Button add,cancel;
    EditText mobile,name,gst;
    ImageView dob_img,doa_img,bill_date_img;
    TextView dob,doa,bill_amount,bill_date;
    mPay selectedPayMode;
    Spinner paymode;
    ArrayList<mPay> paymodes = new ArrayList<>();
    mBillOrder order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);

        add = findViewById(R.id.save);
        cancel = findViewById(R.id.cancel);
        mobile = findViewById(R.id.mobile);
        name = findViewById(R.id.name);

        paymode = findViewById(R.id.paymode);

        dob = findViewById(R.id.dob);
        dob_img = findViewById(R.id.dob_img);
        doa = findViewById(R.id.doa);
        doa_img = findViewById(R.id.doa_img);
        bill_date = findViewById(R.id.bill_date);
        bill_date_img = findViewById(R.id.bill_date_img);
        bill_amount = findViewById(R.id.bill_amount);
        gst = findViewById(R.id.gst);

        customer = (mCustomer) getIntent().getSerializableExtra("customer");
        order = (mBillOrder) getIntent().getSerializableExtra("order");
        paymodes = (ArrayList<mPay>) getIntent().getSerializableExtra("PayModes");



        aPayMode dataAdapter = new aPayMode(this, android.R.layout.simple_spinner_item, paymodes);
        paymode.setAdapter(dataAdapter);
        int index =0;
        for (mPay pay: paymodes){
            if (pay.getName().equalsIgnoreCase(order.getPayMode())){
                paymode.setSelection(index);
            }
            index++;
        }


        updateCustomerView(customer);
        bill_date.setText(order.getDocDate());
        bill_amount.setText(AddToCartView.toCurrency(String.format("%.2f",order.getTotAmt ())));

        paymode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long arg3) {
                // TODO Auto-generated method stub

                selectedPayMode = paymodes.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
        mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() == 10){
                    getCustomer();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        dob_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dob.getText().toString().isEmpty()){
                    dob.setText(CustomDatePicker.currentDate(CustomDatePicker.ShowFormat));
                }
                try {
                    new CustomDatePicker(context, null,null

                    ).Show(CustomDatePicker.getDate(dob.getText().toString(),  CustomDatePicker.ShowFormat)
                            , new CustomDatePicker.ICustomDatePicker() {
                                @Override
                                public void onDateSet(Date date) {
                                    dob.setText(CustomDatePicker.formatDate(date,CustomDatePicker.ShowFormat));
                                }
                            });
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });

        bill_date_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    new CustomDatePicker(context, null,null

                    ).Show(CustomDatePicker.getDate(bill_date.getText().toString(),  CustomDatePicker.CommitFormat)
                            , new CustomDatePicker.ICustomDatePicker() {
                                @Override
                                public void onDateSet(Date date) {
                                    bill_date.setText(CustomDatePicker.formatDate(date,CustomDatePicker.CommitFormat));
                                    order.setBillDate(CustomDatePicker.formatDate(date,CustomDatePicker.CommitFormat));
                                }
                            });
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });


        doa_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    if (doa.getText().toString().isEmpty()){
                        doa.setText(CustomDatePicker.currentDate(CustomDatePicker.ShowFormat));
                    }

                    new CustomDatePicker(context, null,null

                    ).Show(CustomDatePicker.getDate(doa.getText().toString(),  CustomDatePicker.ShowFormat)
                            , new CustomDatePicker.ICustomDatePicker() {
                                @Override
                                public void onDateSet(Date date) {
                                    doa.setText(CustomDatePicker.formatDate(date,CustomDatePicker.ShowFormat));
                                }
                            });
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (gst.getText().toString().length() == 15 || gst.getText().toString().length() == 0 || gst.getText().toString().equalsIgnoreCase("NA")) {
                        order.setPayMode(selectedPayMode.getName());
                        customer.setName(name.getText().toString())
                                .setMobile(mobile.getText().toString())
                                .setGST_NO(gst.getText().toString())
                                .setDOA(CustomDatePicker.formatDate(CustomDatePicker.getDate(doa.getText().toString(), CustomDatePicker.ShowFormat), CustomDatePicker.CommitFormat))
                                .setDOB(CustomDatePicker.formatDate(CustomDatePicker.getDate(dob.getText().toString(), CustomDatePicker.ShowFormat), CustomDatePicker.CommitFormat));

                        onSendResponse(customer);
                    }else{
                        AppAlert.getInstance().Alert(context, "Invalid GST!!!", "Please Enter Valid GST No.",
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        gst.requestFocus();
                                    }
                                });
                    }
                } catch (ParseException e) {
                    onSendResponse(customer);
                    e.printStackTrace();
                }


                //CustomerCommit(customer);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



    }

    private void CustomerCommit(mCustomer customer) {
        HashMap<String, String> request = new HashMap<>();
        request.put("sCompanyFolder", MyCustumApplication.getInstance().getUser().getCompanyCode());
        request.put("iPA_ID", MyCustumApplication.getInstance().getUser().getID());
        request.put("iID", ""+ customer.getId());
        request.put("MOBILE", customer.getMobile());
        request.put("CUSTOMER_NAME", customer.getMobile());
        request.put("DOB", customer.getDOB());
        request.put("DOA", customer.getDOA());

        ArrayList<Integer> tables = new ArrayList<>();
        tables.add(0);

        new MyOrderAPIService(context).execute(new ResponseBuilder("BILL_CUSTOMER_COMMIT_MOBILE", request)
                .setTables(tables)
                .setDescription("Please wait.....\n" +
                        "Processing your expense request....")
                .setResponse(new CBOServices.APIResponse() {
                    @Override
                    public void onComplete(Bundle bundle) throws Exception {
                        String table0 = bundle.getString("Tables0");
                        JSONArray jsonArray1 = new JSONArray(table0);
                        for (int i = 0; i < jsonArray1.length(); i++) {
                            JSONObject object = jsonArray1.getJSONObject(i);
                            customer.setId(object.getInt("ID"));
                        }

                        onSendResponse(customer);
                    }

                    @Override
                    public void onResponse(Bundle bundle) throws Exception {


                    }

                    @Override
                    public void onError(String s, String s1) {
                        AppAlert.getInstance().getAlert(context, s, s1);
                    }
                }));
    }

    private void getCustomer(){


        if (!customer.getMobile().equalsIgnoreCase(mobile.getText().toString())) {
            //Start of call to service

            HashMap<String, String> request = new HashMap<>();
            request.put("sCompanyFolder", MyCustumApplication.getInstance().getUser().getCompanyCode());
            request.put("iPA_ID", MyCustumApplication.getInstance().getUser().getID());
            request.put("sMobile", mobile.getText().toString());


            ArrayList<Integer> tables = new ArrayList<>();
            tables.add(0);

            new MyOrderAPIService(context).execute(new ResponseBuilder("BILL_CUSTOMER_MOBILE", request)
                    .setTables(tables)
                    .setDescription("Please wait.....\n" +
                            "Processing your expense request....")
                    .setResponse(new CBOServices.APIResponse() {
                        @Override
                        public void onComplete(Bundle bundle) throws Exception {
                            String table0 = bundle.getString("Tables0");
                            JSONArray jsonArray1 = new JSONArray(table0);
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject object = jsonArray1.getJSONObject(i);
                                customer.setMobile(object.getString("MOBILE"))
                                        .setName(object.getString("CUSTOMER_NAME"))
                                        .setId(object.getInt("ID"))
                                        .setDOB(object.getString("DOB"))
                                        .setDOA(object.getString("DOA"))
                                        .setCity(object.getString("CITY"))
                                        .setGST_NO(object.getString("GST_NO"));
                            }

                            if (!customer.getMobile().equalsIgnoreCase(mobile.getText().toString())){
                                customer.setMobile(mobile.getText().toString())
                                        .setName("")
                                        .setId(0)
                                        .setDOB("")
                                        .setDOA("")
                                        .setCity("")
                                        .setGST_NO("");
                            }

                            updateCustomerView(customer);

                        }

                        @Override
                        public void onResponse(Bundle bundle) throws Exception {


                        }

                        @Override
                        public void onError(String s, String s1) {
                            AppAlert.getInstance().getAlert(context, s, s1);
                        }
                    }));
        }
    }

    private void updateCustomerView(mCustomer customer){
        mobile.setText(customer.getMobile());
        name.setText(customer.getName());
        gst.setText(customer.getGST_NO());
        try {
            dob.setText(CustomDatePicker.formatDate(CustomDatePicker.getDate(customer.getDOB(),  CustomDatePicker.ShowFormatOld),CustomDatePicker.ShowFormat));
            doa.setText(CustomDatePicker.formatDate(CustomDatePicker.getDate(customer.getDOA(),  CustomDatePicker.ShowFormatOld),CustomDatePicker.ShowFormat));
        } catch (ParseException e) {
            e.printStackTrace();
        }



    }


   // @Override
    public void onSendResponse(mCustomer customer) {
        Intent intent = new Intent();
        intent.putExtra("customer",customer);
        intent.putExtra("order",order);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }
}
