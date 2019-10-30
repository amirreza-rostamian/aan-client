/*
 * This is the source code of aan for Android v. 3.x.x.
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Nikolai Kudashov, 2013-2017.
 */

package ir.amin.HaftTeen.ui.Cells;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

import me.himanshusoni.chatmessageview.util.ViewUtil;
import ir.amin.HaftTeen.messenger.AndroidUtilities;
import ir.amin.HaftTeen.messenger.LocaleController;
import ir.amin.HaftTeen.ui.ActionBar.Theme;
import ir.amin.HaftTeen.ui.Components.LayoutHelper;

public class HeaderCell extends FrameLayout {

    private TextView textView;

    public HeaderCell(Context context) {
        super(context);

        textView = new TextView(getContext());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
        textView.setTypeface(ViewUtil.getFont(context));
        textView.setGravity((LocaleController.isRTL ? Gravity.RIGHT : Gravity.LEFT) | Gravity.CENTER_VERTICAL);
        addView(textView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT, (LocaleController.isRTL ? Gravity.RIGHT : Gravity.LEFT) | Gravity.TOP, 17, 15, 17, 0));
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        textView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlueHeader));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(38), MeasureSpec.EXACTLY));
    }

    public void setText(String text) {
        textView.setText(text);
    }
}