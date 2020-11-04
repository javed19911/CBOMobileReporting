
package utils.adapterutils;

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
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;

import java.util.List;

import interfaces.Ipob;
import utils_new.AppAlert;
import utils_new.Custom_Variables_And_Method;
import utils_new.InputFilterMinMax;


public class PobAdapter extends ArrayAdapter<PobModel> {
	private final List<PobModel>list;
	Ipob listener;
	private final Activity context;
	private Boolean showNOC,clicked= true;
	private Boolean showRxQty = false;
	CBO_DB_Helper cbo_db_helper;
    Custom_Variables_And_Method customVariablesAndMethod;
    String DCRGIFT_QTY_VALIDATE ="";
	
	public PobAdapter(Activity context, List<PobModel> list,Boolean showNOC,Boolean showRxQty,Ipob listener){
		super(context, R.layout.dr_pob_row, list);
		this.context = context;
		this.listener = listener;
        this.list = list;
		this.showNOC=showNOC;
		this.showRxQty = showRxQty;
		cbo_db_helper = new CBO_DB_Helper(context);
        customVariablesAndMethod= Custom_Variables_And_Method.getInstance();
		DCRGIFT_QTY_VALIDATE = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"DCRGIFT_QTY_VALIDATE","");
	}



    static class ViewHolder
    {        
    	protected TextView text; 
    	protected TextView id;
    	protected TextView rate;
    	protected CheckBox checkbox;
		protected CheckBox checkbox_prescribe;
		protected EditText scores;    
		protected EditText pob_val;
		protected EditText noc;
		protected EditText RxQty;
    }
	 @Override   
	    public View getView(int position, View convertView, ViewGroup parent)
	    {        
	    	View view = null;   
	    	if (convertView == null) {
	    		LayoutInflater inflator = context.getLayoutInflater(); 
                view = inflator.inflate(R.layout.dr_pob_row, null);

	    		final ViewHolder viewHolder = new ViewHolder(); 
	    		viewHolder.text = (TextView) view.findViewById(R.id.name_pob); 
	    		viewHolder.id=(TextView) view.findViewById(R.id.id_pob);
	    		viewHolder.rate=(TextView) view.findViewById(R.id.rate_pob);
	    		viewHolder.scores=(EditText) view.findViewById(R.id.qty_pob); 
	    		viewHolder.pob_val=(EditText)view.findViewById(R.id.pob_pob);
				viewHolder.noc=(EditText)view.findViewById(R.id.noc_pob);
				viewHolder.RxQty= view.findViewById(R.id.rx_qty);

				//&& MyCustumApplication.getInstance().getDataFrom_FMCG_PREFRENCE("RXQTYYN","N").equalsIgnoreCase("Y")
				viewHolder.noc.setFilters(new InputFilter[]{ new InputFilterMinMax("0", customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"POBMAXQTY","10000"))});
				viewHolder.RxQty.setFilters(new InputFilter[]{ new InputFilterMinMax("0", customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"POBMAXQTY","10000"))});
				viewHolder.scores.setFilters(new InputFilter[]{ new InputFilterMinMax("0",customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"SAMPLEMAXQTY","10000"))});
				viewHolder.pob_val.setFilters(new InputFilter[]{ new InputFilterMinMax("0", customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"POBMAXQTY","10000"))});

	    		viewHolder.scores.addTextChangedListener(new TextWatcher() {
	    			public void onTextChanged(CharSequence s, int start, int before, int count) {      
	    				PobModel element=(PobModel)viewHolder.scores.getTag();
	    				if (!clicked)
	    					return;
						if(element.getBalance() >=  Integer.parseInt(s.toString().isEmpty()? "0":s.toString()) || !DCRGIFT_QTY_VALIDATE.contains("S")) {
							element.setScore(s.toString());
							if(s.toString().equals("") && viewHolder.pob_val.getText().toString().equals("") && viewHolder.noc.getText().toString().equals("") && !element.isSelected() ) {
								viewHolder.checkbox.setChecked(false);
							}else {
								viewHolder.checkbox.setChecked(true);
							}
						}else{
							AppAlert.getInstance().Alert(context, "Out Of Stock!!!",
									"Only " + element.getBalance() + " is available in the Stock",
									new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											clicked = false;
											if (element.getBalance() >= 0) {
												viewHolder.scores.setText("" + element.getBalance());
												element.setScore("" + element.getBalance());
											}else{
												viewHolder.scores.setText("" );
												element.setScore("0");
											}
											if(s.toString().equals("") && viewHolder.pob_val.getText().toString().equals("") && viewHolder.noc.getText().toString().equals("") && !element.isSelected() ) {
												viewHolder.checkbox.setChecked(false);
											}else {
												viewHolder.checkbox.setChecked(true);
											}
											clicked = true;
										}
									});
						}
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
						if(s.toString().equals("") && viewHolder.scores.getText().toString().equals("") && viewHolder.noc.getText().toString().equals("") && !element.isSelected() ) {
							viewHolder.checkbox.setChecked(false);
						}else {
							if (listener != null && element.isSelected()) {
								listener.onItemSelectedListChanged();
							}
							viewHolder.checkbox.setChecked(true);
						}


						
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

				viewHolder.noc.addTextChangedListener(new TextWatcher() {

					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {
						// TODO Auto-generated method stub
						PobModel element=(PobModel)viewHolder.scores.getTag();
						element.setNOC(s.toString());
						if(s.toString().equals("") && viewHolder.scores.getText().toString().equals("") && viewHolder.pob_val.getText().toString().equals("") && !element.isSelected() ) {
							viewHolder.checkbox.setChecked(false);
						}else {
							viewHolder.checkbox.setChecked(true);
						}

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



	    		viewHolder.checkbox = (CheckBox) view.findViewById(R.id.check_pob);

				viewHolder.checkbox.setChecked(false);
	    		viewHolder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						PobModel element = (PobModel) viewHolder.checkbox.getTag();

						element.setSelected(buttonView.isChecked());
						if (listener != null) {
							listener.onItemSelectedListChanged();
						}

					}
				});

				viewHolder.checkbox.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						PobModel element = (PobModel) viewHolder.checkbox.getTag();
						element.setSelected(viewHolder.checkbox.isChecked());
						/*if (viewHolder.pob_val.getText().toString().equals("")) {
							viewHolder.pob_val.setText("0");
						}
						if (viewHolder.scores.getText().toString().equals("")) {
							viewHolder.scores.setText("0");
						}*/
					}
				});

				viewHolder.checkbox_prescribe=(CheckBox)view.findViewById(R.id.check_prescribe);


				if (showNOC){
					viewHolder.noc.setVisibility(View.GONE);
				}
				if (customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"DR_RX_ENTRY_YN","N").equals("N")){
					viewHolder.checkbox_prescribe.setVisibility(View.GONE);
				}

				if (showRxQty){
					viewHolder.RxQty.setVisibility(View.VISIBLE);
				}

				viewHolder.checkbox_prescribe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

						PobModel element = (PobModel) viewHolder.checkbox_prescribe.getTag();
						element.setSelected_Rx(buttonView.isChecked());

					}
				});


				viewHolder.RxQty.addTextChangedListener(new TextWatcher() {

					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {
						// TODO Auto-generated method stub
						PobModel element=(PobModel)viewHolder.checkbox_prescribe.getTag();
						element.setRx_Qty(s.toString());
						if(s.toString().equals("") && !element.isSelected_Rx() ) {
							viewHolder.checkbox_prescribe.setChecked(false);
						}else {
							viewHolder.checkbox_prescribe.setChecked(true);
						}

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
				    viewHolder.checkbox.setTag(list.get(position));
			    	viewHolder.checkbox_prescribe.setTag(list.get(position));
	    	    	viewHolder.scores.setTag(list.get(position));
	    	    	view.setTag(viewHolder);
	    	} else {
				view = convertView;
				((ViewHolder) view.getTag()).checkbox.setTag(list.get(position));
				((ViewHolder) view.getTag()).checkbox_prescribe.setTag(list.get(position));
				((ViewHolder) view.getTag()).scores.setTag(list.get(position));
			}

			clicked = false;
	    		ViewHolder holder = (ViewHolder) view.getTag();     
	    		holder.text.setText(list.get(position).getName());  
	    		holder.id.setText(list.get(position).getId());
	    		holder.rate.setText(list.get(position).getRate());
	    		holder.scores.setText(list.get(position).getScore()); 
	    		holder.pob_val.setText(list.get(position).getPob().equalsIgnoreCase("0")?"":list.get(position).getPob());
				holder.noc.setText(list.get(position).getNOC());
	    		holder.checkbox.setChecked(list.get(position).isSelected());
			    holder.checkbox_prescribe.setChecked(list.get(position).isSelected_Rx());
				holder.RxQty.setText(list.get(position).getRx_Qty());

			clicked = true;

			if (list.get(position).isHighlighted()){
				holder.text.setTextColor(0xffFF8333);
			}else if (list.get(position).isdr_item().equals("0")){
				holder.text.setTextColor(0xff008333);
			}else {
				holder.text.setTextColor(0xff000000);
			}
			return view;
        }
	    



}
