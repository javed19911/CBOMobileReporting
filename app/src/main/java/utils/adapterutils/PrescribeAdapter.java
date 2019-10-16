
package utils.adapterutils;

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

import androidx.appcompat.app.AppCompatActivity;

import com.cbo.cbomobilereporting.R;

import java.util.List;


public class PrescribeAdapter extends ArrayAdapter<PobModel> {
    private final List<PobModel>list;
    private final AppCompatActivity context;

    public PrescribeAdapter(AppCompatActivity context, List<PobModel> list){
        super(context, R.layout.dr_pob_row, list);
        this.context = context;

        this.list = list;
    }
    static class ViewHolder
    {
        protected TextView text;
        protected TextView id;
        protected TextView rate;
        protected CheckBox checkbox;
        protected EditText scores;
        protected EditText pob_val;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view = null;
        if (convertView == null)
        {
            LayoutInflater inflator = context.getLayoutInflater();

            view = inflator.inflate(R.layout.dr_pob_row, null);

            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.text = (TextView) view.findViewById(R.id.name_pob);
            viewHolder.id=(TextView) view.findViewById(R.id.id_pob);
            viewHolder.rate=(TextView) view.findViewById(R.id.rate_pob);
            viewHolder.scores=(EditText) view.findViewById(R.id.qty_pob);
            viewHolder.pob_val=(EditText)view.findViewById(R.id.pob_pob);


            viewHolder.scores.addTextChangedListener(new TextWatcher()
            {
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    PobModel element=(PobModel)viewHolder.scores.getTag();
                    element.setScore(s.toString());
                }
                public void beforeTextChanged(CharSequence s, int start, int count,int after)
                {
                }
                public void afterTextChanged(Editable s)
                {
                }
            });
            viewHolder.pob_val.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // TODO Auto-generated method stub
                    PobModel element=(PobModel)viewHolder.scores.getTag();
                    element.setPob(s.toString());

                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void afterTextChanged(Editable s) {
                    // TODO Auto-generated method stub

                }
            });
            viewHolder.scores.setVisibility(view.GONE);
            viewHolder.pob_val.setHint("Qty.");
            viewHolder.checkbox = (CheckBox) view.findViewById(R.id.check_pob);
            viewHolder.checkbox.setVisibility(view.GONE);

            viewHolder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                    PobModel element = (PobModel) viewHolder.checkbox.getTag();

                    element.setSelected(buttonView.isChecked());



                }
            });
            viewHolder.checkbox.setTag(list.get(position));
            viewHolder.scores.setTag(list.get(position));
            view.setTag(viewHolder);
        }
        else
        {
            view = convertView;
            ((ViewHolder) view.getTag()).checkbox.setTag(list.get(position));
            ((ViewHolder) view.getTag()).scores.setTag(list.get(position));
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        holder.text.setText(list.get(position).getName());
        holder.id.setText(list.get(position).getId());
        holder.rate.setText(list.get(position).getRate());
        holder.scores.setText(list.get(position).getScore());
        holder.pob_val.setText(list.get(position).getPob());
        holder.checkbox.setChecked(list.get(position).isSelected());
        return view;

    }




}
