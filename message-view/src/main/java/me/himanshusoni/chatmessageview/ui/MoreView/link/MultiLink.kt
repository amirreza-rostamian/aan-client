package me.himanshusoni.chatmessageview.ui.MoreView.link

/**
 * [MultiLink] one2more case in model with ViewType
 * Created by wanbo on 2017/7/11.
 */
abstract class MultiLink<T : Any> {

    /** [link] return one ViewType by judge data */
    abstract fun link(data: T): RegisterItem

}