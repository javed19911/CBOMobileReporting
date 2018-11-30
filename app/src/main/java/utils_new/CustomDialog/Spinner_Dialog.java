package utils_new.CustomDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.ui_new.report_activities.TeamMonthDivision.Model.mUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import services.CboServices;
import utils.adapterutils.Dcr_Workwith_Adapter;
import utils.adapterutils.Dcr_Workwith_Model;
import utils.adapterutils.SpinAdapter;
import utils.adapterutils.SpinnerModel;
import utils.model.DropDownModel;
import utils_new.Custom_Variables_And_Method;

/**
 * Created by pc24 on 28/11/2017.
 */

public class Spinner_Dialog {

    Context context;
    private ArrayList<DropDownModel> List;
    private ArrayList<DropDownModel> ListCopy;
    private AlertDialog myalertDialog = null;

    private OnItemClickListener Listener = null;

    public interface OnItemClickListener {
         void ItemSelected(DropDownModel item);
    }

    public Spinner_Dialog(@NonNull Context context, Object List,OnItemClickListener Listener) {
        this.context = context;
        this.List = (ArrayList<DropDownModel>) List;
        ListCopy = (ArrayList<DropDownModel>) this.List.clone();
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
        SpinAdapter arrayAdapter=new SpinAdapter(context, R.layout.spin_row,ListCopy);
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
                    if (textlength <= List.get(i).getName().length()) {

                        if (List.get(i).getName().toLowerCase().contains(editText.getText().toString().toLowerCase().trim())) {
                            ListCopy.add(List.get(i));
                        }
                    }
                }
                try {
                    listview.setAdapter(new SpinAdapter(context, R.layout.spin_row, ListCopy));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        myalertDialog=myDialog.show();

    }



}
