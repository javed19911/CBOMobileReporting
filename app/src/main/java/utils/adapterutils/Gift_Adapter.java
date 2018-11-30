package utils.adapterutils;

import android.app.Activity;
import android.text.Editable;
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

import java.util.List;


public class Gift_Adapter extends ArrayAdapter<GiftModel> {

    private final List<GiftModel> list;
    private final Activity context;

    public Gift_Adapter(Activity context, List<GiftModel> list) {
        super(context, R.layout.stk_sample_row, list);
        this.context = context;
        this.list = list;
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
            viewHolder.scores = (EditText) view.findViewById(R.id.txtAddress);
            viewHolder.sample = (EditText) view.findViewById(R.id.txtSample);
            viewHolder.scores.addTextChangedListener(new TextWatcher() {
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    GiftModel element = (GiftModel) viewHolder.scores.getTag();
                    element.setScore(s.toString());
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void afterTextChanged(Editable s) {
                }
            });
            viewHolder.sample.addTextChangedListener(new TextWatcher() {
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    GiftModel element = (GiftModel) viewHolder.sample.getTag();
                    element.setSample(s.toString());
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void afterTextChanged(Editable s) {
                }
            });
            viewHolder.checkbox = (CheckBox) view.findViewById(R.id.check);
            viewHolder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    GiftModel element = (GiftModel) viewHolder.checkbox.getTag();
                    element.setSelected(buttonView.isChecked());
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

        ViewHolder holder = (ViewHolder) view.getTag();
        holder.text.setText(list.get(position).getName());
        holder.rate.setText(list.get(position).getRate());
        holder.id.setText(list.get(position).getId());
        holder.scores.setText(list.get(position).getScore());
        holder.sample.setText(list.get(position).getSample());
        holder.checkbox.setChecked(list.get(position).isSelected());
        holder.scores.setVisibility(View.GONE);
        return view;

    }


}