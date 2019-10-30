package ir.amin.HaftTeen.vasni.adapter.Match

import android.support.v4.content.ContextCompat
import android.view.View
import kotlinx.android.synthetic.main.row_tab_league.*
import me.himanshusoni.chatmessageview.ui.MoreView.MoreViewHolder
import ir.amin.HaftTeen.vasni.model.teentaak.League
import ir.amin.HaftTeen.R


class TabLeagueAdapter(containerView: View) : MoreViewHolder<League>(containerView) {

    override fun bindData(data: League, payloads: List<Any>) {
        tv_tab_league_title.setText(data.title)
        if (data.isSelected!!) {
            img_nav_tab_league.visibility = View.VISIBLE
            tv_tab_league_title.setTextColor(ContextCompat.getColor(containerView.context, R.color.colorGreen))
        } else {
            img_nav_tab_league.visibility = View.GONE
            tv_tab_league_title.setTextColor(ContextCompat.getColor(containerView.context, R.color.colorBlack))
        }

        addOnClickListener(tv_tab_league_title)
    }
}