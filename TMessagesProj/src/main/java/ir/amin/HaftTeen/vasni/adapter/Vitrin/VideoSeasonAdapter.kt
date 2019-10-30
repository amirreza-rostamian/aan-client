package ir.amin.HaftTeen.vasni.adapter.Vitrin

import android.view.View
import kotlinx.android.synthetic.main.row_season_video.*
import me.himanshusoni.chatmessageview.ui.MoreView.MoreViewHolder
import ir.amin.HaftTeen.vasni.extention.loadImage
import ir.amin.HaftTeen.vasni.model.teentaak.Vitrin.Video


class VideoSeasonAdapter(containerView: View) : MoreViewHolder<Video>(containerView) {

    override fun bindData(data: Video, payloads: List<Any>) {
        imv_season_video.loadImage(containerView.context, data.thumbnail!!, pv_loading_season_pic)
        tv_season_video_name.text = data.name
        addOnClickListener(ll_season_video)
    }

}