package ir.amin.HaftTeen.vasni.adapter.Vitrin

import android.view.View
import kotlinx.android.synthetic.main.row_comment.*
import me.himanshusoni.chatmessageview.ui.MoreView.MoreViewHolder
import ir.amin.HaftTeen.vasni.model.teentaak.Vitrin.Comments


class GameCommentAdapter(containerView: View) : MoreViewHolder<Comments>(containerView) {
    override fun bindData(data: Comments, payloads: List<Any>) {
        try {
            tv_comment_user_name.text = data.fullName
            tv_message_comment.text = data.message
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}