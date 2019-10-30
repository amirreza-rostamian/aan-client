package ir.amin.HaftTeen.vasni.adapter.Match

import android.view.View
import kotlinx.android.synthetic.main.row_league_game.*
import me.himanshusoni.chatmessageview.ui.MoreView.MoreViewHolder
import ir.amin.HaftTeen.ui.LaunchActivity.presentFragment
import ir.amin.HaftTeen.vasni.core.DataLoader
import ir.amin.HaftTeen.vasni.extention.getHumanReadableSize
import ir.amin.HaftTeen.vasni.extention.loadImage
import ir.amin.HaftTeen.vasni.model.teentaak.Vitrin.Game
import ir.amin.HaftTeen.vasni.teentaak.fragment.market.GameDetailFragment
import ir.amin.HaftTeen.R


class LeagueGameAdapter(containerView: View) : MoreViewHolder<Game>(containerView) {

    override fun bindData(data: Game, payloads: List<Any>) {
        imv_league_game.loadImage(containerView.context, data.thumbnail!!, pv_league_game_loading)
        tv_league_game_name.text = data.title
        if (data.volume != null) {
            tv_league_game_volume.text =
                    getHumanReadableSize(containerView.context, data.volume!!.toLong()) + " " + containerView.context.getString(
                            R.string.game_volume_unit
                    )
        }
        if (data.top_score!!.size != 0) {
            tv_league_game_score.text =
                    data.top_score!!.get(0).score + " " + containerView.context.getString(R.string.scores)
        } else {
            tv_league_game_score.text = " 0 " + containerView.context.getString(R.string.scores)
        }

        ll_league_game.setOnClickListener {
            DataLoader.instance.game = data
            presentFragment(GameDetailFragment(""))
        }

    }

}