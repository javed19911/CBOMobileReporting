package bill.Cart;

import android.app.AlertDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.cbo.cbomobilereporting.R;

import java.util.ArrayList;

import bill.NewOrder.mBillBatch;

/**
 * Created by pc24 on 28/11/2017.
 */

public class Batch_Dialog {

    Context context;
    private ArrayList<mBillBatch> List;
    private ArrayList<mBillBatch> ListCopy;
    private AlertDialog myalertDialog = null;

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

    public void show() {



        AlertDialog.Builder myDialog = new AlertDialog.Builder(context);
        final EditText editText = new EditText(context);
        final ListView listview=new ListView(context);
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(editText);
        layout.addView(listview);
        myDialog.setView(layout);
        aBillBatch arrayAdapter=new aBillBatch(context, R.layout.spin_row,ListCopy);
        listview.setAdapter(arrayAdapter);
        listview.setOnItemClickListener((parent, view, position, id) -> {

            myalertDialog.dismiss();
            if (Listener != null){
                Listener.ItemSelected(ListCopy.get(position));
            }

        });

        editText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s,
                                          int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int textlength = editText.getText().length();
                ListCopy.clear();
                for (int i = 0; i < List.size(); i++) {
                    if (textlength <= List.get(i).getBATCH_NO().length()  ) {

                        if (List.get(i).getBATCH_NO().toLowerCase().contains(editText.getText().toString().toLowerCase().trim())) {
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
        myalertDialog=myDialog.show();

    }



}
