package ir.amin.HaftTeen.vasni.adapter.Score

import android.view.View
import kotlinx.android.synthetic.main.row_program_score.*
import me.himanshusoni.chatmessageview.ui.MoreView.MoreViewHolder
import ir.amin.HaftTeen.vasni.model.teentaak.ProgramScore
import ir.amin.HaftTeen.R


class ProgramScoreAdapter(containerView: View) : MoreViewHolder<ProgramScore>(containerView) {

    override fun bindData(data: ProgramScore, payloads: List<Any>) {

        tv_program_score_name.text = data.source_type_name
        tv_program_score_score.text = data.score + " " + containerView.context.getString(R.string.scores)
        tv_program_score_date.text = data.created

    }

}