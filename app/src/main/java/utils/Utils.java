package utils;





import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Environment;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

public class Utils {

	private Context _context;
	static String filePath="";

	// constructor
	public Utils(Context context) {
		this._context = context;
	}

	/*
	 * Reading file paths from SDCard
	 */
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
						filePaths.add(filePath);
					}
					
				}
			} else {
				// image directory is empty
				Toast.makeText(
						_context,
						AppConstant.PHOTO_ALBUM
								+ " is empty. Please load some images in it !",
						Toast.LENGTH_LONG).show();
			}

		} else {
			AlertDialog.Builder alert = new AlertDialog.Builder(_context);
			alert.setTitle("Error!");
			alert.setMessage(AppConstant.PHOTO_ALBUM
					+ " directory path is not valid! Please set the image directory name AppConstant.java class");
			alert.setPositiveButton("OK", null);
			alert.show();
		}

		return filePaths;
	}

	/*
	 * Check supported file extensions
	 * 
	 * @returns boolean
	 */
	private boolean IsSupportedFile(String filePath) {
		String ext = filePath.substring((filePath.lastIndexOf(".") + 1),
				filePath.length());

		if (AppConstant.FILE_EXTN
				.contains(ext.toLowerCase(Locale.getDefault())))
			return true;
		else
			return false;

	}

	/*
	 * getting screen width
	 */
	public int getScreenWidth() {
		int columnWidth;
		WindowManager wm = (WindowManager) _context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();

		final Point point = new Point();
		try {
			//display.getSize(point);
		} catch (NoSuchMethodError ignore) { // Older device
			point.x = display.getWidth();
			point.y = display.getHeight();
		}
		columnWidth = point.x;
		return columnWidth;
	}
	public String getFileName() throws IOException{
		getFilePaths();
		File directory = new File(
				Environment.getExternalStorageDirectory(),"cbo");
		if (directory.isDirectory()) {
			// getting list of file paths
			File[] listFiles = directory.listFiles();
		// loop through all files
			if (listFiles.length > 0) {
		for (int i = 0; i < listFiles.length; i++) {

			// get file path
			 filePath = listFiles[i].getCanonicalPath();//getAbsolutePath();
		}
			}
		}
		return filePath;
	}
	public String getFilePath(){
		//ArrayList<String> filePaths = new ArrayList<String>();

		File directory = new File(
				Environment.getExternalStorageDirectory(),"cbo");
						//+ File.separator + AppConstant.PHOTO_ALBUM);
		if (directory.isDirectory()) {
			// getting list of file paths
			File[] listFiles = directory.listFiles();

			// Check for count
			if (listFiles.length > 0) {

				// loop through all files
				for (int i = 0; i < listFiles.length; i++) {

					// get file path
					 //filePath = listFiles[i].getAbsolutePath();
					filePath=listFiles[i].getName();
				}
			}
		}

		return filePath;
	}
	}
