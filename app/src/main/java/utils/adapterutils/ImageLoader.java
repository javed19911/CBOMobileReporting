package utils.adapterutils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.WeakHashMap;



import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.Toast;


import com.cbo.cbomobilereporting.R;

import utils.AppConstant;
import utils.MyConnection;
import utils_new.Custom_Variables_And_Method;

public class ImageLoader {
	Context context;
	final int stub_id = R.drawable.no_image;
	private Map<ImageView, String> imageViews=Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
	private ArrayList<String> _filePaths = new ArrayList<String>();
	static String filePath="";
	
	
	 public ImageLoader(Context context){
		 this.context=context;
	 
	 }
	 
	 @SuppressWarnings("static-access")
	public void DisplayImage( ImageView imageView,String id,String visual_pdf,String item_name){
		 
	
		 File path1 = new File(Environment.getExternalStorageDirectory(), "cbo/"+id+"_"+ Custom_Variables_And_Method.pub_doctor_spl_code +".jpg");
		 File path = new File(Environment.getExternalStorageDirectory(), "cbo/"+id+".jpg");
		if(item_name.equals("CATALOG")){
			imageView.setImageResource(R.drawable.web_login_white);
		}else if(path1.exists())
		 {
			 Bitmap b = BitmapFactory.decodeFile(path1.toString());
			 Bitmap dd=b.createScaledBitmap(b, 50, 50, true);
			 imageView.setImageBitmap(b);
			 Custom_Variables_And_Method.doctor_image_name=path1.getName();
		    }
		 else if(path.exists()) 
		 {
			 Bitmap b = BitmapFactory.decodeFile(path.toString());
			 Bitmap dd=b.createScaledBitmap(b, 50, 50, true);
			 imageView.setImageBitmap(dd);
			 Custom_Variables_And_Method.doctor_image_name=path.getName();
		    }
		 else if(visual_pdf.equals("Y"))
		 {
			 imageView.setImageResource(R.drawable.pdf_icon);
		 }
		 else
	        {
	            //queuePhoto(url, imageView);
	            imageView.setImageResource(stub_id);
	           // save.setVisibility(View.GONE);
				Custom_Variables_And_Method.doctor_image_name="no_image";
	        }
		 
	 }
	 
	 public ArrayList<String>getFilePath(String id){
		 ArrayList<String>mypath=new ArrayList<String>();
		 File path = new File(Environment.getExternalStorageDirectory(), "cbo/"+id+".jpg");
		 if(path.exists())
		 {
			 File[] listFiles = path.listFiles();
			 if (listFiles.length > 0) {
				 for (int i = 0; i < listFiles.length; i++) {
					 filePath = listFiles[i].getAbsolutePath();
					 if (IsSupportedFile(filePath)) {
							// Add image path to array list
						 mypath.add(filePath);
						}
				 }
			 }
			 return mypath;
		 }
		 
		 else
		 {
			 mypath.add(""+stub_id);
			 return mypath;
		 }
	 }
	 public static Bitmap decodeFile(String filePath, int WIDTH, int HIGHT) {
		 
			try {

				File f = new File(filePath);

				BitmapFactory.Options o = new BitmapFactory.Options();
				o.inJustDecodeBounds = true;
				BitmapFactory.decodeStream(new FileInputStream(f), null, o);

				final int REQUIRED_WIDTH = WIDTH;
				final int REQUIRED_HIGHT = HIGHT;
				int scale = 1;
				while (o.outWidth / scale / 2 >= REQUIRED_WIDTH
						&& o.outHeight / scale / 2 >= REQUIRED_HIGHT)
					scale *= 2;

				BitmapFactory.Options o2 = new BitmapFactory.Options();
				o2.inSampleSize = scale;
				return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			return null;
		}
	 public ArrayList<String> getFilePaths() {
			ArrayList<String> filePaths = new ArrayList<String>();

			File directory = new File(
					Environment.getExternalStorageDirectory(),"cbo");
							//+ File.separator + AppConstant.PHOTO_ALBUM);

			// check for directory
			if (directory.isDirectory()) {
				// getting list of file paths
				File[] listFiles = directory.listFiles();

				// Check for count
				if (listFiles.length > 0) {

					// loop through all files
					for (int i = 0; i < listFiles.length; i++) {

						// get file path
						 filePath = listFiles[i].getAbsolutePath();

						// check for supported file extension
						if (IsSupportedFile(filePath)) {
							// Add image path to array list
							_filePaths.add(filePath);
						}
						
					}
				} else {
					// image directory is empty
					Toast.makeText(
							context,
							AppConstant.PHOTO_ALBUM
									+ " is empty. Please load some images in it !",
							Toast.LENGTH_LONG).show();
				}

			} else {
				AlertDialog.Builder alert = new AlertDialog.Builder(context);
				alert.setTitle("Error!");
				alert.setMessage(AppConstant.PHOTO_ALBUM
						+ " directory path is not valid! Please set the image directory name AppConstant.java class");
				alert.setPositiveButton("OK", null);
				alert.show();
			}

			return filePaths;
		}
	 private boolean IsSupportedFile(String filePath) {
			String ext = filePath.substring((filePath.lastIndexOf(".") + 1),
					filePath.length());

			if (AppConstant.FILE_EXTN
					.contains(ext.toLowerCase(Locale.getDefault())))
				return true;
			else
				return false;

		}

}
