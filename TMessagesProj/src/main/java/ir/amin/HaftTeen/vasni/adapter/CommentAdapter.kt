package ir.amin.HaftTeen.vasni.adapter

import android.view.View
import kotlinx.android.synthetic.main.row_comment.*
import me.himanshusoni.chatmessageview.ui.MoreView.MoreViewHolder
import ir.amin.HaftTeen.vasni.model.teentaak.Comment


class CommentAdapter(containerView: View) : MoreViewHolder<Comment>(containerView) {
    override fun bindData(data: Comment, payloads: List<Any>) {
        try {
            tv_message_comment.text = data.comment
            tv_comment_user_name.text = data.user_name
            tv_date_comment.text = data.created_at
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}