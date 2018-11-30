package utils.adapterutils;

import java.util.List;






import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;

public class RootAdapter extends BaseAdapter {
	 List<RootModel>list;
	 Context context;
	LayoutInflater mInflater;
	
	public RootAdapter(Context context){
		//super(context, R.layout.dcr_root_row, list);
		this.context = context;       
		   //this.list = list;  
		   this.mInflater=LayoutInflater.from(context);
	}
	public void setList(List<RootModel> Book) {  
        this.list = Book;  
    }  
	
	static class ViewHolder
    {        
    	protected TextView text; 
    	protected TextView id;
    	protected RadioButton checkbox;        
		
    }
	
	
	
	 @Override   
	    public View getView(int position, View convertView, ViewGroup parent)
	    {        
	    	View view = null;   
	    	if (convertView == null)
	    	{            
	    		//LayoutInflater inflator = context.getLayoutInflater(); 

	    		convertView = mInflater.inflate(R.layout.dcr_root_row, null); 

	    		final ViewHolder viewHolder = new ViewHolder(); 
	    		viewHolder.text = (TextView) view.findViewById(R.id.dcr_rootrow_name); 
	    		viewHolder.id=(TextView) view.findViewById(R.id.dcr_rootrow_id);
	    		
	    		
	    		
	    		viewHolder.checkbox = (RadioButton) view.findViewById(R.id.rootselector);  
	    		//viewHolder.checkbox.setClickable(false);
	    		/*viewHolder.checkbox.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						RootModel model=(RootModel)viewHolder.checkbox.getTag();
						model.setSelected(true);
					}
				});*/
	    		/*viewHolder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
	                public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {   
	                	PobModel element = (PobModel) viewHolder.checkbox.getTag(); 
	                	
	                	element.setSelected(buttonView.isChecked()); 
	                	
	                	
	                	
	                	}         
	                }); */
	    		/*viewHolder.checkbox.setTag(list.get(position));      
	    		viewHolder.scores.setTag(list.get(position));*/
	    		view.setTag(viewHolder);   
	    	}
	    	else
	    	{
	    		
	    		view = convertView; 
	    		//((ViewHolder) view.getTag()).checkbox.setTag(list.get(position));  
	    		   
	    		}   
	    	
	    		ViewHolder holder = (ViewHolder) view.getTag();   
	    		RootModel model=(RootModel)getItem(position);
	    		holder.checkbox.setSelected(model.isSelected);
	    		holder.text.setText(list.get(position).getRootName());  
	    		holder.id.setText(list.get(position).getRootId());
	    		     
	    		return view;

	     }



	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}



	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}



	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	} 
	    



}




