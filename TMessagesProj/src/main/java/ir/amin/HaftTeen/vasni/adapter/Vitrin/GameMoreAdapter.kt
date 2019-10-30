package ir.amin.HaftTeen.vasni.adapter.Vitrin

import android.view.View
import kotlinx.android.synthetic.main.row_media_game_more.*
import me.himanshusoni.chatmessageview.ui.MoreView.MoreViewHolder
import ir.amin.HaftTeen.ui.LaunchActivity.presentFragment
import ir.amin.HaftTeen.vasni.core.DataLoader
import ir.amin.HaftTeen.vasni.extention.loadImage
import ir.amin.HaftTeen.vasni.model.teentaak.Vitrin.Game
import ir.amin.HaftTeen.vasni.teentaak.fragment.market.GameDetailFragment
import ir.amin.HaftTeen.R

class GameMoreAdapter(containerView: View) : MoreViewHolder<Game>(containerView) {

    override fun bindData(data: Game, payloads: List<Any>) {

        imv_media_game_more.loadImage(containerView.context, data.thumbnail!!, pv_loading_pic_game_more)
        tv_media_game_more_name.text = data.title
        if (data.price == 0)
            tv_media_game_more_price.text = containerView.context.getString(R.string.free)
        else
            tv_media_game_more_price.text = data.price.toString() + " " + containerView.context.getString(R.string.currency)
        ll_game_more.setOnClickListener {
            DataLoader.instance.game = data
            presentFragment(GameDetailFragment(""))
        }

    }

}