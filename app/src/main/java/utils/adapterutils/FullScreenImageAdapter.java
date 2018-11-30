package utils.adapterutils;



import java.util.ArrayList;



import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.ui.TouchImageView;


import utils.Utils;

public class FullScreenImageAdapter extends PagerAdapter {

	private Activity _activity;
	private Context c;
	private Utils utils;
	//final int stub_id = R.drawable.error;
	private ArrayList<String> _imagePaths; 
	private LayoutInflater inflater;
	String val="";
	String val2="";
	String id="";
	 String name="";
	//TextView tv;
	

	// constructor
	public FullScreenImageAdapter(Activity activity,
			ArrayList<String> imagePaths) {
		this._activity = activity;
		this._imagePaths = imagePaths;
		//utils=new Utils(this);
	}

	@Override
	public int getCount() {
		return this._imagePaths.size();
	}

	@Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }
	
	@Override
    public Object instantiateItem(ViewGroup container, int position) {
        TouchImageView imgDisplay;
        Button btnClose;
 
        inflater = (LayoutInflater) _activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View viewLayout = inflater.inflate(R.layout.layout_fullscreen_image2, container,
                false);
        
        //final View toastView=inflater.inflate(R.layout.mytoast, container,false);
       // final ImageView imageView = (ImageView)toastView.findViewById(R.id.image);
       // final TextView textView = (TextView)toastView.findViewById(R.id.text);
 
        imgDisplay = (TouchImageView) viewLayout.findViewById(R.id.imgDisplay);
        //btnClose = (Button) viewLayout.findViewById(R.id.btn);
        // tv=(TextView)viewLayout.findViewById(R.id.tv1);
        
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(_imagePaths.get(position), options);
        imgDisplay.setImageBitmap(bitmap);
        /*
        
        File path1 = new File(Environment.getExternalStorageDirectory(), "cbo/"+id+"_"+MyConnection.pub_doctor_spl_code +".jpg");
		 File path = new File(Environment.getExternalStorageDirectory(), "cbo/"+id+".jpg");
		
		 if(path1.exists()) 
		 {
			  Bitmap bitmap= BitmapFactory.decodeFile(path1.toString());
			// final Bitmap bitmapt=b.createScaledBitmap(b, 50, 50, true);
			 imgDisplay.setImageBitmap(bitmap);
			 MyConnection.doctor_image_name=path1.getName();
		    }
		 else if(path.exists()) 
		 {
			  Bitmap bitmap = BitmapFactory.decodeFile(path.toString());
			// Bitmap dd=b.createScaledBitmap(b, 50, 50, true);
			 imgDisplay.setImageBitmap(bitmap);	
			 MyConnection.doctor_image_name=path.getName();
		    }
		 else
	        {
	            //queuePhoto(url, imageView);
	            //imageView.setImageResource(stub_id);
	            imgDisplay.setImageResource(stub_id);
	           // save.setVisibility(View.GONE);
	            MyConnection.doctor_image_name="no_image";
	        }
       // final Bitmap bitmap = BitmapFactory.decodeFile(_imagePaths.get(position), options);
        final File newfile=new File(_imagePaths.get(position));
        
        //imgDisplay.setImageBitmap(bitmap);
        
       // tv.setText(newfile.getName());
       // name=tv.getText().toString();
       
        //name=""+imgDisplay.getId();
        // close button click event
        
         btnClose.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				
				
				//_activity.finish();
				//helper=new DataHelper(viewLayout.getContext());
				
				String myfile=newfile.getName().toString();
				String fileName[]=myfile.split(".jpg");
				String realFile=fileName[0];
				
				String DR_ID=MyConnection.DR_ID;
				
				//Toast.makeText(viewLayout.getContext(), DR_ID, Toast.LENGTH_LONG).show();
				//Toast.makeText(viewLayout.getContext(), realFile, Toast.LENGTH_SHORT).show();
				
				//Toast toast = new Toast(viewLayout.getContext());
				long in=1;
				//long in=helper.insertdata(realFile);
				//in=helper.insertPhoto("kk",realFile);
				
			
				//if(in>0)
				//{
				//	imageView.setImageBitmap(bitmap);
					//textView.setText(realFile+" Saved");
				//	Toast.makeText(viewLayout.getContext(), realFile+" Saved", Toast.LENGTH_SHORT).show();
					
					
					
					

					//toast.show();
					//Toast.makeText(viewLayout.getContext(), val2, Toast.LENGTH_LONG).show();
					//Toast.makeText(viewLayout.getContext(), realFile+" Saved", Toast.LENGTH_LONG).show();
			//	}
			//	else
			//	{
			//		Toast.makeText(viewLayout.getContext(), ""+in, Toast.LENGTH_LONG).show();
			//	}
				//ArrayList<String>myPic=helper.getName();
			//	if(myPic.contains(realFile))
				//{
				//	Toast.makeText(viewLayout.getContext(), realFile+" Saved", Toast.LENGTH_LONG).show();
				//}
				//if(myPic.isEmpty())
				//{
				//	Toast.makeText(viewLayout.getContext(), "Photo Not Saved...", Toast.LENGTH_LONG).show();
				//}
				
			}
		}); 
       
        
         /*     btnClose.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(buttonView.isChecked())
				{
					Toast.makeText(c, "kkkk", Toast.LENGTH_LONG).show();
				}
				
			}
		});
		*/

        ((ViewPager) container).addView(viewLayout);
 
        return viewLayout;
	}
	
	@Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);
 
    }
	/*
	public String fileName(){
		name=tv.getText().toString();
		return name;
	}
	*/

	}
