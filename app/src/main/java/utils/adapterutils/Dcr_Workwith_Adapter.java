package utils.adapterutils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cbo.cbomobilereporting.R;

import utils_new.AppAlert;
import utils_new.Custom_Variables_And_Method;


public class Dcr_Workwith_Adapter extends ArrayAdapter<Dcr_Workwith_Model>
	{
		private final List<Dcr_Workwith_Model> list;   
		private final Activity context;
		String[] selected_list,independent_list;
		boolean show_independent=false,checked=true;
		Custom_Variables_And_Method customVariablesAndMethod;
		
		public Dcr_Workwith_Adapter(Activity context,ArrayList<Dcr_Workwith_Model> list,String[] selected_list,String[] independent_list)
		{
			super(context, R.layout.dcr_workwith_row,list);
			this.context = context;       
			this.list = list;
			this.selected_list=selected_list;
			this.independent_list=independent_list;

			if (!Custom_Variables_And_Method.pub_desig_id.equals(""+1)) {
				show_independent = true;
			}else{
                this.independent_list =new String[1];
                independent_list[0] = "cbo";
            }

			customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
		}

		public Dcr_Workwith_Adapter(Activity context,ArrayList<Dcr_Workwith_Model> list,String[] selected_list)
		{
			super(context, R.layout.dcr_workwith_row,list);
			this.context = context;
			this.list = list;
			this.selected_list=selected_list;
			this.independent_list =new String[1];
			independent_list[0] = "cbo";
			customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
		}

		
		static class ViewHolder
	    {        
	    	protected TextView text;  
	    	protected TextView id;
	    	protected CheckBox checkbox;
			protected CheckBox independent_checkbox;
		}
		
		@Override   
	    public View getView(final int position, final View convertView, ViewGroup parent)
	    {        
	    	View view = null;   
	    	if (convertView == null)
	    	{            
	    		LayoutInflater inflator = context.getLayoutInflater(); 

	    		view = inflator.inflate(R.layout.dcr_workwith_row, null); 

	    		final ViewHolder viewHolder = new ViewHolder(); 
	    		viewHolder.text = (TextView) view.findViewById(R.id.dcr_workwith_name); 
	    		
	    		viewHolder.id=(TextView) view.findViewById(R.id.dcr_workwith_id);
	    		      
	    		         
	    		viewHolder.checkbox = (CheckBox) view.findViewById(R.id.workwith_select);

	    		viewHolder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
	                public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {   
	                	Dcr_Workwith_Model element = (Dcr_Workwith_Model) viewHolder.checkbox.getTag();
						if (element.getResigYN().equals("0")) {
							element.setSelected(buttonView.isChecked());

							if (checked && element.isindependentSelected()) {
								checked = false;
								viewHolder.independent_checkbox.setChecked(!buttonView.isChecked());
							} else {
								checked = true;
							}
						}else if (checked ){

							String title ="Vacant";
							if (!element.getName().toLowerCase().contains("vacant")){
								title ="Resigned";
							}
							AppAlert.getInstance().Alert(context, title,
									element.getName() + " is \n" + title + "\nYou can only work as Independent..",
									new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											viewHolder.checkbox.setChecked(false);
											viewHolder.independent_checkbox.setChecked(true);
										}
									});
							//customVariablesAndMethod.getAlert(context,title, element.getName() +" is \n"+title+"\nYou can only work as Independent..");
						}
                    }
                });

				viewHolder.independent_checkbox = (CheckBox) view.findViewById(R.id.independent_select);
				viewHolder.independent_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
						Dcr_Workwith_Model element = (Dcr_Workwith_Model) viewHolder.independent_checkbox.getTag();

						if (element.getLeaveYN().equals("1")) {
							element.setindependentSelected(buttonView.isChecked());

							if (checked && element.isSelected()) {
								checked=false;
								viewHolder.checkbox.setChecked(!buttonView.isChecked());
							}else{
								checked=true;
							}
						}else if (checked ){

							String title ="Not on Leave...";

							AppAlert.getInstance().Alert(context, title,
									element.getName() +" is \n"+title+"\nYou cannot work as Independent..",
									new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											viewHolder.checkbox.setChecked(true);
											viewHolder.independent_checkbox.setChecked(false);
										}
									});

							//customVariablesAndMethod.getAlert(context,title, element.getName() +" is \n"+title+"\nYou cannot work as Independent..");
						}

					}
				});
				viewHolder.checkbox.setTag(list.get(position));
				viewHolder.independent_checkbox.setTag(list.get(position));
	    		view.setTag(viewHolder);   
	    	} else {
	    		view = convertView; 
	    		((ViewHolder) view.getTag()).checkbox.setTag(list.get(position));
				((ViewHolder) view.getTag()).independent_checkbox.setTag(list.get(position));
			}
			ViewHolder holder = (ViewHolder) view.getTag();

			if (Arrays.asList(selected_list).contains(list.get(position).getName()) && !Arrays.asList(independent_list).contains(list.get(position).getName())){
				Dcr_Workwith_Model element = (Dcr_Workwith_Model) holder.independent_checkbox.getTag();
				element.setSelected(true);
			}

			if (Arrays.asList(independent_list).contains(list.get(position).getName())){
				Dcr_Workwith_Model element = (Dcr_Workwith_Model) holder.independent_checkbox.getTag();
				element.setindependentSelected(true);
			}


			holder.text.setText(list.get(position).getName());
			holder.id.setText(list.get(position).getId());
			checked = false;
			holder.checkbox.setChecked(list.get(position).isSelected());
			if (show_independent) {
				holder.independent_checkbox.setChecked(list.get(position).isindependentSelected());
			}else{
				holder.independent_checkbox.setVisibility(View.GONE);
			}
			checked = true;
			return view;

	     } 
	}
