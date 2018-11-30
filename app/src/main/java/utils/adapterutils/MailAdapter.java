package utils.adapterutils;

import java.util.List;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;


public class MailAdapter extends ArrayAdapter<MailModel>{
	
	private final List<MailModel> list;   
	private final Activity context;
	
	public MailAdapter(Activity context, List<MailModel> list)
    {
	   super(context, R.layout.mail_row, list);
	   this.context = context;       
	   this.list = list;   
	    }
	static class ViewHolder
    {        
    	protected TextView text;  
    	protected TextView id;
    	protected CheckBox checkbox;            
    }
	 @Override   
	    public View getView(int position, View convertView, ViewGroup parent)
	    {        
	    	View view = null;   
	    	if (convertView == null)
	    	{            
	    		LayoutInflater inflator = context.getLayoutInflater(); 

	    		view = inflator.inflate(R.layout.mail_row, null); 

	    		final ViewHolder viewHolder = new ViewHolder(); 
	    		viewHolder.text = (TextView) view.findViewById(R.id.toppl_mail); 
	    		
	    		viewHolder.id=(TextView) view.findViewById(R.id.pplid_mail);
	    		      
	    		         
	    		viewHolder.checkbox = (CheckBox) view.findViewById(R.id.check_mailto);  
	    		viewHolder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
	                public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {   
	                	MailModel element = (MailModel) viewHolder.checkbox.getTag();  
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
