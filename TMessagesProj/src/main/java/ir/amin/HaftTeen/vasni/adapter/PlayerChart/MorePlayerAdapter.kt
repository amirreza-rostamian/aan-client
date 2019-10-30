package  ir.amin.HaftTeen.vasni.adapter.PlayerChart

import android.view.View
import kotlinx.android.synthetic.main.row_more_player.*
import me.himanshusoni.chatmessageview.ui.MoreView.MoreViewHolder
import ir.amin.HaftTeen.vasni.extention.loadImage
import ir.amin.HaftTeen.vasni.model.teentaak.Player
import ir.amin.HaftTeen.BuildConfig
import ir.amin.HaftTeen.R
import java.util.*


class MorePlayerAdapter(containerView: View) : MoreViewHolder<Player>(containerView) {
    override fun bindData(data: Player, payloads: List<Any>) {
        var androidColors: IntArray = containerView.context.getResources().getIntArray(R.array.androidcolors)
        var randomAndroidColor: Int = androidColors[Random().nextInt(androidColors.size)]
//        cv_more_player_score.setBackgroundColor(randomAndroidColor)
        tv_more_player_name.setText(data.name)
        if (data.province != null)
            tv_more_player_province.setText(containerView.context.getString(R.string.province) + " " + data.province)
        if (data.grade != "0" || data.grade != null)
            tv_more_player_grade.setText(containerView.context.getString(R.string.class_name) + " " + data.grade)
        if (data.pic == null || data.pic == "" || data.pic.equals(BuildConfig.SERVER_URL + "/"))
            imv_more_player_icon.setImageResource(R.drawable.amir)
        else
            imv_more_player_icon.loadImage(containerView.context, data.pic, pv_more_player_content)
        tv_more_player_score.setText(data.sum + " " + containerView.context.getString(R.string.scores))
    }
}