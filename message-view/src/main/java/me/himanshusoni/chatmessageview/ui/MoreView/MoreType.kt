package me.himanshusoni.chatmessageview.ui.MoreView

import me.himanshusoni.chatmessageview.ui.MoreView.link.RegisterItem
import me.himanshusoni.chatmessageview.ui.MoreView.link.SoleLinkManager


/**
 * [MoreType] uses in Application to init something
 * Created by wanbo on 2017/7/12.
 */
object MoreType {

    /** [soleRegister] register global RegisterItem [items] RegisterItem Array */
    fun soleRegister(vararg items: RegisterItem) {
        SoleLinkManager.globalRegister(*items)
    }

}