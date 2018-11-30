package utils.font_package;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;



/**
 * Created by Shivam on 2/23/2016.
 */
public class Button_Regular_white extends android.support.v7.widget.AppCompatButton {


    public Button_Regular_white(Context context, AttributeSet set) {
        super(context,set);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "font/ProximaNova-Reg.ttf"));
        this.setTextSize(17.0f);
        this.setTextColor(Color.WHITE);

    }

}
