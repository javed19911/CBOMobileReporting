package utils.adapterutils;

import java.util.List;





import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;


public class DcrRootAdapter extends ArrayAdapter<RootModel>{
	
	public List<RootModel>list;
	public Context context;
	int selectedPosition=-1;
	RootModel model;
	public String name="";
	public String id;
	private Boolean allowMultipleRoute = false;
	public RootModel rootModel=null;
	
	public DcrRootAdapter(Context context,List<RootModel> list2,Boolean allowMultipleRoute){
		super(context, R.layout.dcr_root_row,list2);
		this.context=context;
		this.list=list2;
		//this.allowMultipleRoute = allowMultipleRoute;
		
	}
	
	
	static class ViewHolder{
		protected TextView tvname;
		protected TextView tvId;
		protected RadioButton rdbtn;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent){
		View v=convertView;
		
		if (v == null) {
            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.dcr_root_row, null);
            ViewHolder holder=new ViewHolder();
        }
		final ViewHolder holder=new ViewHolder();
		model=(RootModel)getItem(position);
		 holder.tvname = (TextView)v.findViewById(R.id.dcr_rootrow_name);
		 holder.tvId=(TextView)v.findViewById(R.id.dcr_rootrow_id);
		 holder.tvname.setText(model.getRootName());
		 holder.tvId.setText(model.getRootId());
		 holder.rdbtn = (RadioButton)v.findViewById(R.id.rootselector);
		if (!allowMultipleRoute ) {
			//holder.rdbtn.setChecked(position == selectedPosition);
			if (rootModel != null) {
				holder.rdbtn.setChecked(rootModel.rootId.equalsIgnoreCase(model.getRootId()));
			}
		}else{
			holder.rdbtn.setChecked(model.isSelected);
		}

		 holder.rdbtn.setTag(position);

		/*holder.rdbtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				int position = (int) buttonView.getTag();
				if (!allowMultipleRoute){
					selectedPosition=position;
					notifyDataSetChanged();
				}else {
					getItem(position).setSelected(isChecked);
				}
			}
		});*/

		 holder.rdbtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
				 int position = (int) view.getTag();
            	 //holder.rdbtn.setEnabled(false);
                // selectedPosition = (Integer)view.getTag();
            	 //selectedPosition=position;
            	 rootModel = list.get(position);
            	 notifyDataSetChanged();
            	 name=list.get(position).getRootName();
            	 id=list.get(position).getRootId();
            	 //Toast.makeText(view.getContext(), name, Toast.LENGTH_LONG).show();
            	 //Toast.makeText(view.getContext(), id, Toast.LENGTH_LONG).show();
             }
         });
		return v;
	}
	
	public void setPosition(int position){
		selectedPosition=position;
		notifyDataSetChanged();
				
	}
	public int getPosition()
	{
		return selectedPosition;
	}

}
