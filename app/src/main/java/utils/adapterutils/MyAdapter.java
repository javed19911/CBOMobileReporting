package utils.adapterutils;

import java.util.List;


import android.app.Activity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;

import utils_new.AppAlert;
import utils_new.Custom_Variables_And_Method;
import utils_new.InputFilterMinMax;


public class MyAdapter extends ArrayAdapter<GiftModel> {

    private final List<GiftModel> list;
    private final Activity context;
    private Boolean clicked= true;
    Custom_Variables_And_Method customVariablesAndMethod;
    String DCRGIFT_QTY_VALIDATE ="";

    public MyAdapter(Activity context, List<GiftModel> list) {
        super(context, R.layout.stk_sample_row, list);
        this.context = context;
        this.list = list;
        customVariablesAndMethod =Custom_Variables_And_Method.getInstance();
        DCRGIFT_QTY_VALIDATE = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"DCRGIFT_QTY_VALIDATE","");
    }

    static class ViewHolder {
        protected TextView text;
        protected TextView rate;
        protected TextView id;
        protected CheckBox checkbox;
        protected EditText scores;
        protected EditText sample;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();

            view = inflator.inflate(R.layout.stk_sample_row, null);

            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.text = (TextView) view.findViewById(R.id.label);
            viewHolder.rate = (TextView) view.findViewById(R.id.label_rate);
            viewHolder.id = (TextView) view.findViewById(R.id.label2);
            viewHolder.scores = (EditText) view.findViewById(R.id.txtAddress);//pob
            viewHolder.sample = (EditText) view.findViewById(R.id.txtSample);//sample
            viewHolder.checkbox = (CheckBox) view.findViewById(R.id.check);

            viewHolder.sample.setFilters(new InputFilter[]{ new InputFilterMinMax("0",customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"SAMPLEMAXQTY","10000"))});
            viewHolder.scores.setFilters(new InputFilter[]{ new InputFilterMinMax("0", customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"POBMAXQTY","10000"))});

            viewHolder.scores.addTextChangedListener(new TextWatcher() {
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    GiftModel element = (GiftModel) viewHolder.scores.getTag();

                    element.setScore(s.toString());
                    //element.setSelected(viewHolder.checkbox.isChecked());
                    if(s.toString().equals("") && viewHolder.sample.getText().toString().equals("")  && !element.isSelected()) {
                        viewHolder.checkbox.setChecked(false);
                    }else {
                        viewHolder.checkbox.setChecked(true);
                    }
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void afterTextChanged(Editable s) {
                }
            });
            viewHolder.sample.addTextChangedListener(new TextWatcher() {
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    if (!clicked)
                        return;
                    GiftModel element = (GiftModel) viewHolder.sample.getTag();
                    if(element.getBalance() >=  Integer.parseInt(s.toString().isEmpty()? "0":s.toString())  || !DCRGIFT_QTY_VALIDATE.contains("S")) {
                        element.setSample(s.toString());
                        if (s.toString().equals("") && viewHolder.scores.getText().toString().equals("") && !element.isSelected()) {
                            viewHolder.checkbox.setChecked(false);
                        } else {
                            viewHolder.checkbox.setChecked(true);
                        }
                    }else{
                        AppAlert.getInstance().Alert(context, "Out Of Stock!!!",
                                "Only " + element.getBalance() + " is available in the Stock",
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        clicked = false;
                                        viewHolder.sample.setText(""+element.getBalance());
                                        element.setSample(""+element.getBalance());
                                        if (s.toString().equals("") && viewHolder.scores.getText().toString().equals("") && !element.isSelected()) {
                                            viewHolder.checkbox.setChecked(false);
                                        } else {
                                            viewHolder.checkbox.setChecked(true);
                                        }
                                        clicked = true;
                                    }
                                });
                    }
                    //element.setSelected(viewHolder.checkbox.isChecked());
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void afterTextChanged(Editable s) {
                }
            });

            viewHolder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        GiftModel element = (GiftModel) viewHolder.checkbox.getTag();
                        element.setSelected(buttonView.isChecked());
                }
            });

            viewHolder.checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    GiftModel element = (GiftModel) viewHolder.checkbox.getTag();
                    element.setSelected(viewHolder.checkbox.isChecked());
                   /* if (viewHolder.sample.getText().toString().equals("")) {
                        viewHolder.sample.setText("0");
                    }
                    if (viewHolder.scores.getText().toString().equals("")) {
                        viewHolder.scores.setText("0");
                    }*/
                }
            });

            viewHolder.checkbox.setTag(list.get(position));
            viewHolder.scores.setTag(list.get(position));
            viewHolder.sample.setTag(list.get(position));

            view.setTag(viewHolder);
        } else {
            view = convertView;
            ((ViewHolder) view.getTag()).checkbox.setTag(list.get(position));
            ((ViewHolder) view.getTag()).scores.setTag(list.get(position));
            ((ViewHolder) view.getTag()).sample.setTag(list.get(position));
        }

        clicked = false;

        ViewHolder holder = (ViewHolder) view.getTag();
        holder.text.setText(list.get(position).getName());
        holder.rate.setText(list.get(position).getRate());
        holder.id.setText(list.get(position).getId());
        holder.scores.setText(list.get(position).getScore());
        holder.sample.setText(list.get(position).getSample());
        holder.checkbox.setChecked(list.get(position).isSelected());

        clicked = true;

        if (list.get(position).isHighlighted()){
            holder.text.setTextColor(0xffFF8333);
        }else {
            holder.text.setTextColor(0xff000000);
        }
        return view;

    }


}