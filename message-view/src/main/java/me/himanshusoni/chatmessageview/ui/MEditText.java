package me.himanshusoni.chatmessageview.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

import me.himanshusoni.chatmessageview.util.AppSchema;


public class MEditText extends EditText {

    Typeface regular, bold;

    public MEditText(Context context) {
        super(context);
    }

    public MEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        try {

            regular = Typeface.createFromAsset(context
                    .getAssets(), AppSchema.FONT_PATH);
            bold = Typeface.createFromAsset(context
                    .getAssets(), AppSchema.FONT_PATH);
        } catch (Exception e) {
            Log.d("", "Font Not Found.");
        }
        String textStyle = attrs.getAttributeValue(
                "http://schemas.android.com/apk/res/android", "textStyle");
        if (textStyle != null) {
            if (textStyle.equals("0x1")) { // bold
                this.setTypeface(bold);
                // this.setGravity(Gravity.RIGHT);
            } else {
                this.setTypeface(regular);
                // this.setGravity(Gravity.RIGHT);
            }
        } else {
            this.setTypeface(regular);
        }

    }


}
