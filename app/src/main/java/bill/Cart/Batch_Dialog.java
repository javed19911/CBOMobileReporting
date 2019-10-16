package bill.Cart;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cbo.cbomobilereporting.R;

import java.util.ArrayList;

import bill.NewOrder.mBillBatch;


public class Batch_Dialog {

    Context context;
    private ArrayList<mBillBatch> List;
    private ArrayList<mBillBatch> ListCopy;
    private Dialog  dialog = null;

    private OnItemClickListener Listener = null;

    public interface OnItemClickListener {
         void ItemSelected(mBillBatch item);
    }

    public Batch_Dialog(@NonNull Context context, ArrayList<mBillBatch> List, OnItemClickListener Listener) {
        this.context = context;
        this.List =  List;
        ListCopy = (ArrayList<mBillBatch>) this.List.clone();
        this.Listener = Listener;
    }

    public void show(String title) {





        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.batch_dialog, null, false);

        ((AppCompatActivity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.setContentView(view);
        final Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.setBackgroundDrawableResource(R.color.White_new);
        window.setGravity(Gravity.CENTER);

        TextView textView =(TextView) view.findViewById(R.id.hadder_text_1);
        textView.setText(title );

        TextView filter =(TextView) view.findViewById(R.id.filterTxt);
        ImageView clearFilter = view.findViewById(R.id.clearQry);
        ListView listview=(ListView) view.findViewById(R.id.list);

        aBillBatch arrayAdapter=new aBillBatch(context, R.layout.bill_row,ListCopy);
        listview.setAdapter(arrayAdapter);
        listview.setOnItemClickListener((parent, view1, position, id) -> {

            dialog.dismiss();
            if (Listener != null){
                Listener.ItemSelected(ListCopy.get(position));
            }

        });


        clearFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter.setText("");
            }
        });
        filter.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s,
                                          int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int textlength = filter.getText().length();
                ListCopy.clear();
                for (int i = 0; i < List.size(); i++) {
                    if (textlength <= List.get(i).getBATCH_NO().length()  ) {

                        if (List.get(i).getBATCH_NO().toLowerCase().contains(filter.getText().toString().toLowerCase().trim())) {
                            ListCopy.add(List.get(i));
                        }
                    }
                }
                try {
                    listview.setAdapter(new aBillBatch(context, R.layout.spin_row, ListCopy));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        //dialog.setCancelable(false);
        dialog.show();

    }



}
