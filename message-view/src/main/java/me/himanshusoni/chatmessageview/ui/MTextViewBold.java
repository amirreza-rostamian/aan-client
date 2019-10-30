package me.himanshusoni.chatmessageview.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import me.himanshusoni.chatmessageview.util.AppSchema;


public class MTextViewBold extends TextView {
    public static Typeface FONT_NAME;


    public MTextViewBold(Context context) {
        super(context);
        isInEditMode();
        if (FONT_NAME == null)
            FONT_NAME = Typeface.createFromAsset(context.getAssets(), AppSchema.FONT_PATH_BOLD);
        this.setTypeface(FONT_NAME);
    }

    public MTextViewBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        isInEditMode();
        try {
            if (FONT_NAME == null)
                FONT_NAME = Typeface.createFromAsset(context.getAssets(), AppSchema.FONT_PATH_BOLD);
            this.setTypeface(FONT_NAME);
        } catch (Exception ex) {

        }
    }

    public MTextViewBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        isInEditMode();
        if (FONT_NAME == null)
            FONT_NAME = Typeface.createFromAsset(context.getAssets(), AppSchema.FONT_PATH_BOLD);
        this.setTypeface(FONT_NAME);
    }
}
