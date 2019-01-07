package utils;

import java.util.Arrays;
import java.util.List;

public class AppConstant {

// Number of columns of Grid View
	public static final int NUM_OF_COLUMNS = 3;
	public static String MAIN_URL="http://www.cboservices1.com/mobilerpt.asmx";
      //public static String MAIN_URL="http://www.cboinfotech.co.in/MobileRpt.asmx";
      //public static String MAIN_URL="http://www.akumentishealthcare.co.in/MobileRpt.asmx";
	  // Gridview image padding
	public static final int GRID_PADDING = 8; // in dp

	    // SD card image directory
    	//@SuppressLint("SdCardPath")
	public static final String PHOTO_ALBUM = "/SD card/cbo/";

	// supported file formats
	public static final List<String> FILE_EXTN = Arrays.asList("jpg", "jpeg",
			"png");
	}