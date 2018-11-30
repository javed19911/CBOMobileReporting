package utils.font_package;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Shivam on 2/23/2016.
 */
public class Text_view_Bold_large extends android.support.v7.widget.AppCompatTextView {
    public Text_view_Bold_large(Context context, AttributeSet set) {
        super(context,set);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "font/proximaNova_Bold.ttf"));

        this.setTextSize(21.0f);
        this.setTextColor(Color.WHITE);
    }

}
