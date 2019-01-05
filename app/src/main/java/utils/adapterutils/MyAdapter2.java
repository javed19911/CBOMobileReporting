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
import android.widget.EditText;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;

import utils_new.AppAlert;
import utils_new.Custom_Variables_And_Method;
import utils_new.InputFilterMinMax;


public class MyAdapter2 extends ArrayAdapter<GiftModel> {

	private final List<GiftModel> list;   
	private final Activity context;
	Custom_Variables_And_Method customVariablesAndMethod;
	private Boolean clicked= true;
	String DCRGIFT_QTY_VALIDATE ="";
	
    public MyAdapter2(Activity context, List<GiftModel> list) {
	   super(context, R.layout.dr_gift_row, list);
	   this.context = context;       
	   this.list = list;
		customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
		DCRGIFT_QTY_VALIDATE = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"DCRGIFT_QTY_VALIDATE","");
	}
    
    static class ViewHolder
    {        
    	protected TextView text; 
    	protected TextView rate; 
    	protected TextView id;
    	protected CheckBox checkbox;        
		protected EditText scores;    
    }
    
    @Override   
    public View getView(int position, View convertView, ViewGroup parent)
    {        
    	View view = null;   
    	if (convertView == null)
    	{            
    		LayoutInflater inflator = context.getLayoutInflater(); 

    		view = inflator.inflate(R.layout.dr_gift_row, null); 

    		final ViewHolder viewHolder = new ViewHolder(); 
    		viewHolder.text = (TextView) view.findViewById(R.id.gift_label); 
    		viewHolder.rate = (TextView) view.findViewById(R.id.gift_label_rate);
    		viewHolder.id=(TextView) view.findViewById(R.id.gift_label2);
    		viewHolder.scores=(EditText) view.findViewById(R.id.gift_txtAddress);
			viewHolder.scores.setFilters(new InputFilter[]{ new InputFilterMinMax("0",customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"SAMPLEMAXQTY","10000"))});
    		viewHolder.scores.addTextChangedListener(new TextWatcher()
    		{  
    			public void onTextChanged(CharSequence s, int start, int before, int count) {
					if (!clicked)
						return;
    				GiftModel element=(GiftModel)viewHolder.scores.getTag();
					if(element.getBalance() >=  Integer.parseInt(s.toString().isEmpty()? "0":s.toString())  || !DCRGIFT_QTY_VALIDATE.contains("G")) {
						element.setScore(s.toString());
					}else{
						AppAlert.getInstance().Alert(context, "Out Of Stock!!!",
								"Only " + element.getBalance() + " is available in the Stock",
								new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										clicked = false;
										viewHolder.scores.setText(""+element.getBalance());
										element.setScore(""+element.getBalance());
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
    		/*viewHolder.checkbox = (CheckBox) view.findViewById(R.id.check);  
    		viewHolder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {   
                	GiftModel element = (GiftModel) viewHolder.checkbox.getTag(); 
                	
                	element.setSelected(buttonView.isChecked()); 
                	
                	
                	
                	}         
                }); 
    		viewHolder.checkbox.setTag(list.get(position));      */
    		viewHolder.scores.setTag(list.get(position));
    		view.setTag(viewHolder);   
    	}
    	else
    	{
    		view = convertView; 
    		//((ViewHolder) view.getTag()).checkbox.setTag(list.get(position));  
    		((ViewHolder) view.getTag()).scores.setTag(list.get(position));   
    		}

		clicked = false;
    		ViewHolder holder = (ViewHolder) view.getTag();     
    		holder.text.setText(list.get(position).getName()); 
    		holder.rate.setText(list.get(position).getRate());
    		holder.id.setText(list.get(position).getId());
    		holder.scores.setText(list.get(position).getScore());
		clicked = true;
    		//holder.checkbox.setChecked(list.get(position).isSelected());     
    		return view;

     } 
    

	}