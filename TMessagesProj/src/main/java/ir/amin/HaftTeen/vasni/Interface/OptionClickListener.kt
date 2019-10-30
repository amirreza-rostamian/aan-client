package ir.amin.HaftTeen.vasni.Interface

import android.view.View

interface OptionClickListener {
    fun onItemClick(view: View, position: Int)
    fun onMediaClick(view: View, position: Int)
}


interface RefreshData {
    fun refresh()
}