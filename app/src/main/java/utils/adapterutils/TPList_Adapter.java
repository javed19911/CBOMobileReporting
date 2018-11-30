package utils.adapterutils;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import utils_new.Custom_Variables_And_Method;


public class TPList_Adapter extends ArrayAdapter<SpinnerModel> {
		private final ArrayList<SpinnerModel> list;
		private final Activity context;
		Custom_Variables_And_Method customVariablesAndMethod;



		public TPList_Adapter(Activity context, ArrayList<SpinnerModel> list)
		{
			super(context, R.layout.dcr_workwith_row,list);
			this.context = context;
			this.list = list;
		}

		
		static class ViewHolder
	    {        
	    	protected TextView text;  
	    	protected TextView id;
		}
		
		@NonNull
        @Override
	    public View getView(final int position, final View convertView, ViewGroup parent)
	    {        
	    	View view = null;   
	    	if (convertView == null)
	    	{            
	    		LayoutInflater inflator = context.getLayoutInflater();
	    		view = inflator.inflate(R.layout.tp_list_row, null);
	    		final ViewHolder viewHolder = new ViewHolder(); 
	    		viewHolder.text = (TextView) view.findViewById(R.id.name);
	    		view.setTag(viewHolder);   
	    	} else {
	    		view = convertView;
			}

			ViewHolder holder = (ViewHolder) view.getTag();
			holder.text.setText(list.get(position).getName());


			return view;

	     } 
	}
