package utils.font_package;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by Shivam on 2/23/2016.
 */
public class Button_Bold_10_black extends androidx.appcompat.widget.AppCompatButton {
    public Button_Bold_10_black(Context context, AttributeSet set) {
        super(context,set);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "font/proximaNova_Bold.ttf"));
        this.setTextSize(10.0f);
        this.setTextColor(Color.BLACK);

    }

}
