package utils.adapterutils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cbo.cbomobilereporting.R;

import java.util.List;


public class Dcr_Workwith_Adapter_update extends ArrayAdapter<Dcr_Workwith_Model>
{
	private final List<Dcr_Workwith_Model> list;   
	private List<String> selected_ids;
	private final AppCompatActivity context;
	
	
	public Dcr_Workwith_Adapter_update(AppCompatActivity context, List<Dcr_Workwith_Model>list, List<String> selected_ids)
	{ 
		super(context, R.layout.dcr_workwith_row,list);
		this.context = context;       
		this.list = list;  
		this.selected_ids = selected_ids;
	}
	
	static class ViewHolder
    {       
    	protected TextView text;  
    	protected TextView id;
    	protected CheckBox checkbox;            
    }
	
	
	@Override   
    public View getView(final int position, View convertView, ViewGroup parent)
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

    		
//****************************************************************************************************	    		
    	    Dcr_Workwith_Model temp = 	list.get(position);
    	    temp.getId();
    	   // Toast.makeText(view.getContext(), temp.getId(), Toast.LENGTH_LONG).show();
    		if (selected_ids.contains(temp.getId()))
    		{
    			viewHolder.checkbox.setChecked(true);
    			//viewHolder.checkbox.setEnabled(false);
    			temp.setSelected(true);
    			
    		}
    		
    		
    		
    		
    		viewHolder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {   
                	Dcr_Workwith_Model element = (Dcr_Workwith_Model) viewHolder.checkbox.getTag();  
                	element.setSelected(buttonView.isChecked());	                	
                	} 
               
                }); 
    		viewHolder.checkbox.setTag(list.get(position));      
    		view.setTag(viewHolder); 
    		
    	}
    	else
    	{
    		view = convertView; 
    		((ViewHolder) view.getTag()).checkbox.setTag(list.get(position));     
    		}   
    	
    		ViewHolder holder = (ViewHolder) view.getTag();     
    		holder.text.setText(list.get(position).getName()); 
    		holder.id.setText(list.get(position).getId());      
    		holder.checkbox.setChecked(list.get(position).isSelected());     
    		return view;

     } 
}