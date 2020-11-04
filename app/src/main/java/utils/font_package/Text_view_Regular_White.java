package utils.font_package;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by Shivam on 2/23/2016.
 */
public class Text_view_Regular_White extends androidx.appcompat.widget.AppCompatTextView {
    public Text_view_Regular_White(Context context, AttributeSet set) {
        super(context,set);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "font/ProximaNova-Reg.ttf"));
        this.setTextSize(15.0f);
        this.setTextColor(Color.WHITE);
    }

}
