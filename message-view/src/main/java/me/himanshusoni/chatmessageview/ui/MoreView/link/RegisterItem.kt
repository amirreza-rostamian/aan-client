package me.himanshusoni.chatmessageview.ui.MoreView.link

import me.himanshusoni.chatmessageview.ui.MoreView.MoreViewHolder
import me.himanshusoni.chatmessageview.ui.MoreView.action.MoreClickListener


/** Created by wanbo <werbhelius@gmail.com> on 2017/9/14. */

data class RegisterItem(
        val layoutId: Int,
        val clazzViewHolder: Class<out MoreViewHolder<*>>,
        var clickListener: MoreClickListener? = null
)