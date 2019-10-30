/*
 * This is the source code of aan for Android v. 3.x.x.
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Nikolai Kudashov, 2013-2017.
 */

package ir.amin.HaftTeen.ui.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.SystemClock;
import android.support.annotation.Keep;
import android.util.SparseIntArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import me.himanshusoni.chatmessageview.util.ViewUtil;
import ir.amin.HaftTeen.messenger.AndroidUtilities;
import ir.amin.HaftTeen.messenger.NotificationsController;
import ir.amin.HaftTeen.ui.ActionBar.Theme;
import ir.amin.HaftTeen.R;

public class ScrollSlidingTextTabStrip extends HorizontalScrollView {

    public boolean isNeedSetBadge = false;
    private LinearLayout tabsContainer;
    private ScrollSlidingTabStripDelegate delegate;

    private int tabCount;
    private int currentPosition;
    private int selectedTabId = -1;
    private Paint rectPaint;
    private int allTextWidth;

    private int indicatorX;
    private int indicatorWidth;

    private int prevLayoutWidth;

    private int animateIndicatorStartX;
    private int animateIndicatorStartWidth;
    private int animateIndicatorToX;
    private int animateIndicatorToWidth;
    private boolean animatingIndicator;
    private float animationIdicatorProgress;
    private ArrayList<String> badgeList = new ArrayList<>();
    private CubicBezierInterpolator interpolator = CubicBezierInterpolator.EASE_OUT_QUINT;

    private SparseIntArray positionToId = new SparseIntArray(5);
    private SparseIntArray idToPosition = new SparseIntArray(5);
    private SparseIntArray positionToWidth = new SparseIntArray(5);

    private boolean animationRunning;
    private long lastAnimationTime;
    private float animationTime;
    private int previousPosition;
    private Runnable animationRunnable = new Runnable() {
        @Override
        public void run() {
            if (!animatingIndicator) {
                return;
            }
            long newTime = SystemClock.elapsedRealtime();
            long dt = (newTime - lastAnimationTime);
            if (dt > 17) {
                dt = 17;
            }
            animationTime += dt / 200.0f;
            setAnimationIdicatorProgress(interpolator.getInterpolation(animationTime));
            if (animationTime > 1.0f) {
                animationTime = 1.0f;
            }
            if (animationTime < 1.0f) {
                AndroidUtilities.runOnUIThread(animationRunnable);
            } else {
                animatingIndicator = false;
                setEnabled(true);
                if (delegate != null) {
                    delegate.onPageScrolled(1.0f);
                }
            }
        }
    };

    public ScrollSlidingTextTabStrip(Context context) {
        super(context);

        setFillViewport(true);
        setWillNotDraw(false);

        setHorizontalScrollBarEnabled(false);
        tabsContainer = new LinearLayout(context);
        tabsContainer.setOrientation(LinearLayout.HORIZONTAL);
        tabsContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutHelper.MATCH_PARENT));
        addView(tabsContainer);

        rectPaint = new Paint();
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Style.FILL);
        rectPaint.setColor(Theme.getColor(Theme.key_actionBarDefaultTitle));
        badgeList.add("0");
        badgeList.add("0");

    }

    public void setDelegate(ScrollSlidingTabStripDelegate scrollSlidingTabStripDelegate) {
        delegate = scrollSlidingTabStripDelegate;
    }

    public boolean isAnimatingIndicator() {
        return animatingIndicator;
    }

    private void setAnimationProgressInernal(RelativeLayout newTab, RelativeLayout prevTab, float value) {
        int newColor = Theme.getColor(Theme.key_actionBarDefaultTitle);
        int prevColor = Theme.getColor(Theme.key_actionBarDefaultSubtitle);

        int r1 = Color.red(newColor);
        int g1 = Color.green(newColor);
        int b1 = Color.blue(newColor);
        int a1 = Color.alpha(newColor);
        int r2 = Color.red(prevColor);
        int g2 = Color.green(prevColor);
        int b2 = Color.blue(prevColor);
        int a2 = Color.alpha(prevColor);

        ((TextView) prevTab.getChildAt(0)).setTextColor(Color.argb((int) (a1 + (a2 - a1) * value), (int) (r1 + (r2 - r1) * value), (int) (g1 + (g2 - g1) * value), (int) (b1 + (b2 - b1) * value)));
        ((TextView) newTab.getChildAt(0)).setTextColor(Color.argb((int) (a2 + (a1 - a2) * value), (int) (r2 + (r1 - r2) * value), (int) (g2 + (g1 - g2) * value), (int) (b2 + (b1 - b2) * value)));

        indicatorX = (int) (animateIndicatorStartX + (animateIndicatorToX - animateIndicatorStartX) * value);
        indicatorWidth = (int) (animateIndicatorStartWidth + (animateIndicatorToWidth - animateIndicatorStartWidth) * value);
        invalidate();
    }

    public Paint getRectPaint() {
        return rectPaint;
    }

    public View getTabsContainer() {
        return tabsContainer;
    }

    public float getAnimationIdicatorProgress() {
        return animationIdicatorProgress;
    }

    @Keep
    public void setAnimationIdicatorProgress(float value) {
        animationIdicatorProgress = value;

        RelativeLayout newTab = (RelativeLayout) tabsContainer.getChildAt(currentPosition);
        RelativeLayout prevTab = (RelativeLayout) tabsContainer.getChildAt(previousPosition);
        setAnimationProgressInernal(newTab, prevTab, value);

        if (delegate != null) {
            delegate.onPageScrolled(value);
        }
    }

    public int getNextPageId(boolean forward) {
        return positionToId.get(currentPosition + (forward ? 1 : -1), -1);
    }

    public void removeTabs() {
        positionToId.clear();
        idToPosition.clear();
        positionToWidth.clear();
        tabsContainer.removeAllViews();
        allTextWidth = 0;
        tabCount = 0;
    }

    public int getTabsCount() {
        return tabCount;
    }

    public boolean hasTab(int id) {
        return idToPosition.get(id, -1) != -1;
    }

    public void addTextTab(final int id, CharSequence text) {
        int position = tabCount++;
        if (position == 0 && selectedTabId == -1) {
            selectedTabId = id;
        }
        positionToId.put(position, id);
        idToPosition.put(id, position);
        if (selectedTabId != -1 && selectedTabId == id) {
            currentPosition = position;
            prevLayoutWidth = 0;
        }
        TextView tab = new TextView(getContext());
        tab.setGravity(Gravity.CENTER);
        tab.setText(text);
        tab.setBackgroundDrawable(Theme.createSelectorDrawable(Theme.getColor(Theme.key_actionBarDefaultSelector), 2));
        tab.setTag(selectedTabId == id ? Theme.key_actionBarDefaultTitle : Theme.key_actionBarDefaultSubtitle);
        tab.setTextColor(Theme.getColor(currentPosition == position ? Theme.key_actionBarDefaultTitle : Theme.key_actionBarDefaultSubtitle));
        tab.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
        tab.setSingleLine(true);
        tab.setTypeface(ViewUtil.getFont(getContext()));
        tab.setPadding(AndroidUtilities.dp(8), 0, AndroidUtilities.dp(8), 0);
//        tab.setOnClickListener(v -> {
//
//        });
        int tabWidth = (int) Math.ceil(tab.getPaint().measureText(text, 0, text.length())) + AndroidUtilities.dp(16);
        allTextWidth += tabWidth;
        positionToWidth.put(position, tabWidth);
        RelativeLayout txtContainer = new RelativeLayout(getContext());
        txtContainer.setOnClickListener(v -> {
            int position1 = tabsContainer.indexOfChild(v);
            if (position1 < 0 || position1 == currentPosition) {
                return;
            }
            boolean scrollingForward = currentPosition < position1;
            previousPosition = currentPosition;
            currentPosition = position1;
            selectedTabId = id;

            if (animatingIndicator) {
                AndroidUtilities.cancelRunOnUIThread(animationRunnable);
                animatingIndicator = false;
            }

            animationTime = 0;
            animatingIndicator = true;
            animateIndicatorStartX = indicatorX;
            animateIndicatorStartWidth = indicatorWidth;

            animateIndicatorToX = v.getLeft();
            animateIndicatorToWidth = v.getMeasuredWidth();
            setEnabled(false);

            AndroidUtilities.runOnUIThread(animationRunnable, 16);

            if (delegate != null) {
                delegate.onPageSelected(id, scrollingForward);
            }
            scrollToChild(position1);
        });
        txtContainer.addView(tab, LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT, RelativeLayout.CENTER_IN_PARENT));
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        txtContainer.addView(badge_layout, LayoutHelper.createRelative(0, LayoutHelper.MATCH_PARENT, RelativeLayout.ALIGN_PARENT_RIGHT, 0, 20, 0, 0));
        LinearLayout.LayoutParams lp_linear = LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT);
        tabsContainer.addView(txtContainer, lp_linear);
        if (isNeedSetBadge) {
            FrameLayout badge_layout = (FrameLayout) inflater.inflate(R.layout.item_tab_badge, null);
            badge_layout.setTag("badge");
            RelativeLayout.LayoutParams lp = LayoutHelper.createRelative(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 0, 0, 30, 0, RelativeLayout.ALIGN_PARENT_END);
            lp.addRule(RelativeLayout.CENTER_IN_PARENT);
            txtContainer.addView(badge_layout, lp);
        }
    }

    public void checkUnreadCountAndShow(int currentAccount) {
//        int counter = NotificationsController.getInstance(accountNumber).getTotalUnreadCount();
        int unreadCount = NotificationsController.getInstance(currentAccount).getTotalUnreadCount();
        badgeList.set(1, String.format("%d", unreadCount));
        requestLayout();
        invalidate();
    }

    public int getCurrentTabId() {
        return selectedTabId;
    }

    public int getFirstTabId() {
        return positionToId.get(0, 0);
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        boolean result = super.drawChild(canvas, child, drawingTime);
        if (child == tabsContainer) {
            final int height = getMeasuredHeight();
            canvas.drawRect(indicatorX, height - AndroidUtilities.dp(2), indicatorX + indicatorWidth, height, rectPaint);
        }
        return result;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int count = tabsContainer.getChildCount();
        for (int a = 0; a < count; a++) {
            View child = tabsContainer.getChildAt(a);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) child.getLayoutParams();
            if (allTextWidth > width) {
                layoutParams.weight = 0;
                layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            } else {
                if (isNeedSetBadge) {
                    layoutParams.weight = 0.5f;
                } else {
                    layoutParams.weight = 1.0f / allTextWidth * positionToWidth.get(a);
                }
                layoutParams.width = 0;
            }

            if (isNeedSetBadge) {
                int unread_count = Integer.parseInt(badgeList.get(a));
                if (unread_count == 0) {
                    for (int i = 0; i < ((ViewGroup) child).getChildCount(); i++) {
                        View double_child = ((ViewGroup) child).getChildAt(i);
                        if (double_child.getTag().equals("badge")) {
                            TextView txtBadge = (TextView) ((FrameLayout) double_child).findViewById(R.id.count);
                            double_child.setVisibility(View.GONE);
                        }
                    }
                } else {
                    for (int i = 0; i < ((ViewGroup) child).getChildCount(); i++) {
                        View double_child = ((ViewGroup) child).getChildAt(i);
                        if (double_child.getTag().equals("badge")) {
                            TextView txtBadge = (TextView) ((FrameLayout) double_child).findViewById(R.id.count);
                            double_child.setVisibility(View.VISIBLE);
                            txtBadge.setText(badgeList.get(a));
                        }
                    }
                }
            }
        }
        if (allTextWidth > width) {
            tabsContainer.setWeightSum(0.0f);
        } else {
            tabsContainer.setWeightSum(1.0f);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void scrollToChild(int position) {
        if (tabCount == 0) {
            return;
        }
        View child = tabsContainer.getChildAt(position);
        if (child == null) {
            return;
        }
        int currentScrollX = getScrollX();
        int left = child.getLeft();
        int width = child.getMeasuredWidth();
        if (left < currentScrollX) {
            smoothScrollTo(left, 0);
        } else if (left + width > currentScrollX + getWidth()) {
            smoothScrollTo(left + width, 0);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if (prevLayoutWidth != r - l) {
            prevLayoutWidth = r - l;
            if (animatingIndicator) {
                AndroidUtilities.cancelRunOnUIThread(animationRunnable);
                animatingIndicator = false;
                setEnabled(true);
                if (delegate != null) {
                    delegate.onPageScrolled(1.0f);
                }
            }
            View child = tabsContainer.getChildAt(currentPosition);
            if (child != null) {
                indicatorX = child.getLeft();
                indicatorWidth = child.getMeasuredWidth();
            }
        }
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        int count = tabsContainer.getChildCount();
        for (int a = 0; a < count; a++) {
            View child = tabsContainer.getChildAt(a);
            child.setEnabled(enabled);
        }
    }

    public void selectTabWithId(int id, float progress) {
        int position = idToPosition.get(id, -1);
        if (position < 0) {
            return;
        }
        if (progress < 0) {
            progress = 0;
        } else if (progress > 1.0f) {
            progress = 1.0f;
        }
        RelativeLayout child = (RelativeLayout) tabsContainer.getChildAt(currentPosition);
        RelativeLayout nextChild = (RelativeLayout) tabsContainer.getChildAt(position);
        if (child != null && nextChild != null) {
            animateIndicatorStartX = child.getLeft();
            animateIndicatorStartWidth = child.getMeasuredWidth();
            animateIndicatorToX = nextChild.getLeft();
            animateIndicatorToWidth = nextChild.getMeasuredWidth();
            setAnimationProgressInernal(nextChild, child, progress);
        }
        if (progress >= 1.0f) {
            currentPosition = position;
            selectedTabId = id;
        }
    }

    public void onPageScrolled(int position, int first) {
        if (currentPosition == position) {
            return;
        }
        currentPosition = position;
        if (position >= tabsContainer.getChildCount()) {
            return;
        }
        for (int a = 0; a < tabsContainer.getChildCount(); a++) {
            tabsContainer.getChildAt(a).setSelected(a == position);
        }
        if (first == position && position > 1) {
            scrollToChild(position - 1);
        } else {
            scrollToChild(position);
        }
        invalidate();
    }

    public interface ScrollSlidingTabStripDelegate {
        void onPageSelected(int page, boolean forward);

        void onPageScrolled(float progress);
    }
}
