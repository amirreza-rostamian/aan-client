package ir.amin.HaftTeen.vasni.utils;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;


public class RtlGrid extends GridLayoutManager {


    public RtlGrid(Context context, int spanCount) {
        super(context, spanCount);
    }

    public RtlGrid(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    @Override
    protected boolean isLayoutRTL() {
        return super.isLayoutRTL();
    }
}
