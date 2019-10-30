package ir.amin.HaftTeen.vasni.adapter.Match

import android.view.View
import kotlinx.android.synthetic.main.row_league_user.*
import me.himanshusoni.chatmessageview.ui.MoreView.MoreViewHolder
import ir.amin.HaftTeen.vasni.extention.loadImage
import ir.amin.HaftTeen.vasni.model.teentaak.LeagueUser
import ir.amin.HaftTeen.BuildConfig
import ir.amin.HaftTeen.R
import java.util.*
import kotlin.collections.ArrayList


class LeagueUserAdapter(containerView: View) : MoreViewHolder<LeagueUser>(containerView) {
    override fun bindData(data: LeagueUser, payloads: List<Any>) {
        val list = ArrayList<Int>()
        list.add(R.drawable.orange_card)
        list.add(R.drawable.red_card)
        list.add(R.drawable.purple_card)
        list.add(R.drawable.pink_card)
        list.add(R.drawable.blue_card)
        val position = Random().nextInt(list.size)
//        cv_league_user.setBackgroundResource(list.get(position))
        list.remove(position)

        tv_league_user_name.text = data.user_name
        if (data.user_pic == null || data.user_pic == "" || data.user_pic.equals(BuildConfig.SERVER_URL + "/"))
            imv_league_user_pic.setImageResource(R.drawable.amir)
        else
            imv_league_user_pic.loadImage(containerView.context, data.user_pic!!, pv_league_user_pic)

        tv_league_user_score.text = data.score + " " + containerView.context.getString(R.string.scores)

    }

}