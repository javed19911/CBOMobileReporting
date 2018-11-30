
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
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;

import java.util.List;

import utils_new.Custom_Variables_And_Method;


public class DrPres_Adapter extends ArrayAdapter<DrPres_Model> {
    private final List<DrPres_Model> list;
    private final Activity context;
    CBO_DB_Helper cbo_db_helper;
    Custom_Variables_And_Method customVariablesAndMethod;
    int RX_MAX_QTY=0;

    public DrPres_Adapter(Activity context, List<DrPres_Model> list) {
        super(context, R.layout.dr_pres_row, list);
        this.context = context;
        this.list = list;
        cbo_db_helper = new CBO_DB_Helper(context);
        customVariablesAndMethod = Custom_Variables_And_Method.getInstance();
         RX_MAX_QTY= Integer.parseInt(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"RX_MAX_QTY","0"));
    }

    static class ViewHolder {
        protected TextView name;
        protected TextView id;
        protected EditText qty;
        protected TextView name_amt;
        protected EditText amt;
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(R.layout.dr_pres_row, null);

            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.name = (TextView) view.findViewById(R.id.dr_pre_item_name);
            viewHolder.id = (TextView) view.findViewById(R.id.dr_pre_item_id);
            viewHolder.qty = (EditText) view.findViewById(R.id.dr_pre_qty);
            viewHolder.amt = (EditText) view.findViewById(R.id.dr_pre_amt);
            viewHolder.name_amt = (TextView) view.findViewById(R.id.dr_pre_amt_caption);


            viewHolder.qty.addTextChangedListener(new TextWatcher() {
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    DrPres_Model element = (DrPres_Model) viewHolder.qty.getTag();

                    if(!s.toString().equals("")) {
                        if (RX_MAX_QTY != 0 && Integer.parseInt(s.toString()) > RX_MAX_QTY) {
                            element.setQty(""+ RX_MAX_QTY);
                            viewHolder.qty.setText(""+ RX_MAX_QTY);
                            viewHolder.qty.selectAll();
                            customVariablesAndMethod.getAlert(context,"Not Allowed!!!","Qty. cannot be more then "+ RX_MAX_QTY);
                        }else{
                            element.setQty(s.toString());

                        }
                    }else{
                        element.setQty(s.toString());
                    }

                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void afterTextChanged(Editable s) {
                }
            });
            viewHolder.amt.addTextChangedListener(new TextWatcher() {
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    DrPres_Model element = (DrPres_Model) viewHolder.qty.getTag();
                    element.setamt(s.toString());
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void afterTextChanged(Editable s) {
                }
            });


            viewHolder.qty.setTag(list.get(position));
            view.setTag(viewHolder);

        } else {
            view = convertView;
            ((ViewHolder) view.getTag()).qty.setTag(list.get(position));
        }
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.name.setText(list.get(position).getName());
        holder.id.setText(list.get(position).getId());
        holder.qty.setText(list.get(position).getQty());
        holder.amt.setText(list.get(position).getamt());
        holder.name_amt.setText(list.get(position).getName_amt());
        return view;
    }








}
