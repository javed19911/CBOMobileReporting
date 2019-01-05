package utils.adapterutils;

import java.util.List;



import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;

import utils_new.Custom_Variables_And_Method;


public class LazyAdapter extends ArrayAdapter<DocSampleModel>{
	private final List<DocSampleModel> list;   
	private final Activity context;
	public ImageLoader imageLoader; 
	final int stub_id = R.drawable.no_image;
	String visual_pdf;
	Custom_Variables_And_Method customVariablesAndMethod;
	public LazyAdapter(Activity context, List<DocSampleModel> list){
		super(context, R.layout.row_doclist, list);
		this.context = context;
		this.list = list;

		imageLoader=new ImageLoader(context.getApplicationContext());
		customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
		visual_pdf=customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"VISUALAIDPDFYN");
		
	}
	static class ViewHolder
    {        
    	protected TextView text;  
    	protected TextView id;
    	TextView rowid;
    	ImageView myimg,promoted;

    }
	@Override   
    public View getView(int position, View convertView, ViewGroup parent)
    {        
    	View view = null;   
    	if (convertView == null)
    	{            
    		LayoutInflater inflator = context.getLayoutInflater(); 

    		view = inflator.inflate(R.layout.row_doclist, null); 

    		final ViewHolder viewHolder = new ViewHolder(); 
    		viewHolder.text = (TextView) view.findViewById(R.id.dcr_workwith_name); 
    		
    		viewHolder.id=(TextView) view.findViewById(R.id.dcr_workwith_id);
    		viewHolder.rowid=(TextView) view.findViewById(R.id.list_row_id);
    		viewHolder.myimg=(ImageView)view.findViewById(R.id.doc_sample_img);
			viewHolder.promoted=(ImageView)view.findViewById(R.id.promoted);
    		
    		view.setTag(viewHolder);   
    	}
    	else {
    		view = convertView;
		}
    	
    		ViewHolder holder = (ViewHolder) view.getTag();    
    		
    		holder.text.setText(list.get(position).getName()); 
    		holder.id.setText(list.get(position).getId());
    		holder.rowid.setText(list.get(position).getRowId());

			if (list.get(position).get_Checked()){
				holder.promoted.setVisibility(View.VISIBLE);
                view.setBackgroundResource(R.drawable.list_selector_selected);
			}else {
				holder.promoted.setVisibility(View.GONE);
                view.setBackgroundResource(R.drawable.list_selector_unselected);
			}

		if (list.get(position).isHighlighted()){
			holder.text.setTextColor(0xffFF8333);
		}else {
			if (list.get(position).get_Checked()) {
				holder.text.setTextColor(0xff7c7b7b);
			}else{
				holder.text.setTextColor(0xff000000);
			}
		}

    		//String stk_id=((TextView)view.findViewById(R.id.dcr_workwith_id)).getText().toString();
    		switch (list.get(position).get_file_ext().toLowerCase()){
				case ".pdf":
					holder.myimg.setImageResource(R.drawable.pdf_icon);
					break;
				case ".mp4":
				case ".3gp":
				case ".avi":
				case ".mov":
					holder.myimg.setImageResource(R.drawable.mp4_icon);
					break;
				case ".mp3":
					holder.myimg.setImageResource(R.drawable.music_icon);
					break;
				case ".html":
					holder.myimg.setImageResource(R.drawable.web_login_white);
					break;
				case ".jpg":
				case ".bmp":
				case ".gif":
				case ".png":
					holder.myimg.setImageResource(R.drawable.image_icon);
					break;
				default:
					holder.myimg.setImageResource(R.drawable.no_image);
			}
    		//imageLoader.DisplayImage(holder.myimg, stk_id,visual_pdf,list.get(position).getName());
    		return view;

     } 
    


	}
