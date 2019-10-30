package me.himanshusoni.chatmessageview.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import me.himanshusoni.chatmessageview.util.AppSchema;


public class MTextView extends AppCompatTextView {
    public static Typeface FONT_NAME;


    public MTextView(Context context) {
        super(context);
        isInEditMode();
        if (FONT_NAME == null)
            FONT_NAME = Typeface.createFromAsset(context.getAssets(), AppSchema.FONT_PATH);
        this.setTypeface(FONT_NAME);
    }

    public MTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        isInEditMode();
        try {
            if (FONT_NAME == null)
                FONT_NAME = Typeface.createFromAsset(context.getAssets(), AppSchema.FONT_PATH);
            this.setTypeface(FONT_NAME);
        } catch (Exception ex) {

        }
    }

    public MTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        isInEditMode();
        if (FONT_NAME == null)
            FONT_NAME = Typeface.createFromAsset(context.getAssets(), AppSchema.FONT_PATH);
        this.setTypeface(FONT_NAME);
    }

}
