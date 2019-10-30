package me.himanshusoni.chatmessageview.ui.MoreView.extension

import me.himanshusoni.chatmessageview.ui.MoreView.MoreAdapter
import me.himanshusoni.chatmessageview.ui.MoreView.MoreViewHolder


/**
 * Created by wanbo on 2017/7/13.
 */
internal interface AnimExtension {

    fun renderWithAnimation(): MoreAdapter

    fun renderWithAnimation(animation: MoreAnimation): MoreAdapter

    fun addAnimation(holder: MoreViewHolder<Any>)

    fun duration(duration: Long): MoreAdapter

    fun firstShowAnim(firstShow: Boolean): MoreAdapter

    fun startAnimPosition(position: Int): MoreAdapter

}