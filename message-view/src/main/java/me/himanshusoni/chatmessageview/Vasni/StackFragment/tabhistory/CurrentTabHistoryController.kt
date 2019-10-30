package me.himanshusoni.chatmessageview.Vasni.StackFragment.tabhistory

import android.os.Bundle
import me.himanshusoni.chatmessageview.Vasni.StackFragment.FragNavPopController
import me.himanshusoni.chatmessageview.Vasni.StackFragment.FragNavTransactionOptions


class CurrentTabHistoryController(fragNavPopController: FragNavPopController) :
        BaseFragNavTabHistoryController(fragNavPopController) {

    @Throws(UnsupportedOperationException::class)
    override fun popFragments(
            popDepth: Int,
            transactionOptions: FragNavTransactionOptions?
    ): Boolean {
        return fragNavPopController.tryPopFragments(popDepth, transactionOptions) > 0
    }

    override fun switchTab(index: Int) {}

    override fun onSaveInstanceState(outState: Bundle) {}

    override fun restoreFromBundle(savedInstanceState: Bundle?) {}
}
