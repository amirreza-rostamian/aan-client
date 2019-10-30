package me.himanshusoni.chatmessageview.ui

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.util.AttributeSet


class RtlGrid : GridLayoutManager {

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
            context,
            attrs,
            defStyleAttr,
            defStyleRes
    ) {
    }

    constructor(context: Context, spanCount: Int) : super(context, spanCount) {}

    constructor(context: Context, spanCount: Int, orientation: Int, reverseLayout: Boolean) : super(
            context,
            spanCount,
            orientation,
            reverseLayout
    ) {
    }

    override fun isLayoutRTL(): Boolean {
        return true
    }
}