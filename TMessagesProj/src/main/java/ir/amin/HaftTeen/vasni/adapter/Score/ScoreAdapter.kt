package ir.amin.HaftTeen.vasni.adapter.Score

import android.view.View
import kotlinx.android.synthetic.main.row_score.*
import me.himanshusoni.chatmessageview.ui.MoreView.MoreViewHolder
import ir.amin.HaftTeen.vasni.model.teentaak.Score
import ir.amin.HaftTeen.R

class ScoreAdapter(containerView: View) : MoreViewHolder<Score>(containerView) {

    override fun bindData(data: Score, payloads: List<Any>) {

        tv_program_name_score.text = data.program_name
        tv_user_point_score.text = data.user_point + " " + containerView.context.getString(R.string.scores)
        addOnClickListener(ll_all_score)

    }

}