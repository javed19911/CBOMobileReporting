package utils.font_package;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by Shivam on 2/23/2016.
 */
public class Edit_text_Regular extends androidx.appcompat.widget.AppCompatEditText {
    public Edit_text_Regular(Context context, AttributeSet set) {
        super(context,set);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "font/ProximaNova-Reg.ttf"));
        this.setTextSize(17.0f);
        this.setTextColor(Color.BLACK);


    }

}
