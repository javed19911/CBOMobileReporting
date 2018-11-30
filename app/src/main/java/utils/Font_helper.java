package utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Typeface;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Akshit on 2/18/2016.
 */
public class Font_helper extends ContextWrapper {
    public Font_helper(Context base) {
        super(base);
    }


    public void tv_regular_font(TextView tv){
        Typeface typeface=Typeface.createFromAsset(getAssets(),"font/ProximaNova-Reg.ttf");
        tv.setTypeface(typeface);
    }
    public void tv_bold_font(TextView tv){
        Typeface typeface=Typeface.createFromAsset(getAssets(),"font/proximaNova_Bold.ttf");
        tv.setTypeface(typeface);
    }
    public void et_regular_font(EditText et){
        Typeface typeface=Typeface.createFromAsset(getAssets(),"font/ProximaNova-Reg.ttf");
        et.setTypeface(typeface);
    }
    public void et_bold_font(EditText et){
        Typeface typeface=Typeface.createFromAsset(getAssets(),"font/proximaNova_Bold.ttf");
        et.setTypeface(typeface);
    }
}
