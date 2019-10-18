package bill.stockEntry;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.ui_new.CustomActivity;

import java.util.ArrayList;

import bill.BillReport.mPay;
import bill.Cart.mCustomer;
import bill.mBillOrder;


public class AddRemark extends CustomActivity {

    mBillOrder order;
    Button add, cancel;
    EditText remark;
    ArrayList<mPay> paymodes = new ArrayList<>();
    private mCustomer customer = new mCustomer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_remark);

        add = findViewById(R.id.save);
        cancel = findViewById(R.id.cancel);
        remark = findViewById(R.id.remark);

        order = (mBillOrder) getIntent().getSerializableExtra("order");
        customer = (mCustomer) getIntent().getSerializableExtra("customer");
        paymodes = (ArrayList<mPay>) getIntent().getSerializableExtra("PayModes");
        remark.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (s.toString().length() == 10){
//                    getCustomer();
//                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (remark.getText().toString().length() != 0) {
                        order.setRemark(remark.getText().toString());
                        onSendResponse(order);
                    } else {
                        remark.setError("Please Enter Remark");
                    }

                } catch (Exception e) {
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

        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
        remark.requestFocus();


    }


    public void onSendResponse(mBillOrder order) {
        Intent intent = new Intent();
        intent.putExtra("customer", customer);
        intent.putExtra("order", order);
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
