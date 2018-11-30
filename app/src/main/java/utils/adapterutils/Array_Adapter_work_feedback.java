package utils.adapterutils;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;

import java.util.ArrayList;

public class Array_Adapter_work_feedback
 extends BaseAdapter{
    Context context;
    Spinner dropdown_item;
    String[] abc;
    ArrayList<SpinnerModel> spinnerModelArrayList;
    SpinnerModel[] TitleName ;
    String [] dropData = {"1","2","3","4","5","6","7","8","9","10"};
    ArrayList<SpinnerModel>array_sort;

    AlertDialog myAlertDialog =null;
    View mConView;

    String drop_down;
    public static String spin_select_id;
    ArrayList<Appraisal_Model> model_arry_lst = new ArrayList<Appraisal_Model>();
    LayoutInflater layoutInflater;



    public Array_Adapter_work_feedback(Context context1, ArrayList<Appraisal_Model> model_clas) {
        this.context = context1;
        this.model_arry_lst = model_clas;
        this.layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return model_arry_lst.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder;
        if (convertView == null) {
            mConView =convertView;
            holder = new Holder();
            convertView = layoutInflater.inflate(R.layout.work_feedback_of_managers_row, null);
            holder.sr_no = (TextView) convertView.findViewById(R.id.sr_no);
            holder.cus_id = (TextView) convertView.findViewById(R.id.cust_id);
            holder.cust_name = (TextView) convertView.findViewById(R.id.fields_name);


            //holder.spin_id = (TextView) convertView.findViewById(R.id.spin_id);
            //holder.spin =(Spinner) convertView.findViewById(R.id.drop_down_item);
            holder.spin_button = (Button) convertView.findViewById(R.id.drop_button);
            holder.spinner_img = (ImageView) convertView.findViewById(R.id.spinner_img);

            ImageView spinner_img = (ImageView) convertView.findViewById(R.id.spinner_img);
            spinner_img.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    show_spinner(holder);
                }
            });

            holder.spin_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    show_spinner(holder);
                } });



             convertView.setTag(holder);

            holder.spin_button.setTag(model_arry_lst.get(position));
        } else {
            holder = (Holder) convertView.getTag();
            ((Holder) convertView.getTag()).spin_button.setTag(model_arry_lst.get(position));

        }
        holder.sr_no.setText(""+  model_arry_lst.get(position).getSno());
        holder.cust_name.setText(model_arry_lst.get(position).getCus_name());
        holder.cus_id.setText(model_arry_lst.get(position).getCus_id());
        // holder.spin_id.setText(model_arry_lst.get(position).getSpin_id());
        holder.spin_button.setText(model_arry_lst.get(position).getSpin_id());
        // spin_select_id =model_arry_lst.get(position).getSpin_id();













      return convertView;
    }

   private void show_spinner(final Holder holder){
       //   Appraisal_Model element=(Appraisal_Model)holder.spin_id.getTag();

       final AlertDialog.Builder myDialog = new AlertDialog.Builder(context);
       final EditText editText = new EditText(context);
       final ListView listview = new ListView(context);
       editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.search, 0, 0, 0);

       spinnerModelArrayList = new ArrayList<SpinnerModel>();
       for (int i = 0; i <= 10; i++) {
           spinnerModelArrayList.add(new SpinnerModel("" + i, "" + i));

       }

       LinearLayout layout = new LinearLayout(context);
       layout.setOrientation(LinearLayout.VERTICAL);

       int width=60;
       int height=60;
       LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(width,height);
       layout.setLayoutParams(parms);
       //layout.addView(editText);
       layout.addView(listview);
       myDialog.setView(layout);
       SpinAdapter arrayAdapter = new SpinAdapter(context, R.layout.spin_row, spinnerModelArrayList);
       listview.setAdapter(arrayAdapter);
       listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {

               myAlertDialog.dismiss();


               String dr_id = ((TextView) view.findViewById(R.id.spin_id)).getText().toString();

               Appraisal_Model appraisal_model = (Appraisal_Model) holder.spin_button.getTag();
               appraisal_model.setSpin_id(dr_id);

               Appraisal_Model element = new Appraisal_Model();


               element.setSpin_id(dr_id);
               //  ((Holder) mConView.getTag()).spin_button.setTag(model_arry_lst.get(position));
               holder.spin_button.setText(dr_id);
               //doc_name = ((TextView) view.findViewById(R.id.spin_name)).getText().toString();
               // drname.setText(doc_name);
           }
       });
       myAlertDialog= myDialog.show();
   }

    class Holder {
        TextView sr_no, cust_name,cus_id,spin_id;
        Button spin_button;
        ImageView spinner_img;

    }


}