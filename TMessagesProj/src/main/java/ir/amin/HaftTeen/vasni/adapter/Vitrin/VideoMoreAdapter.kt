package ir.amin.HaftTeen.vasni.adapter.Vitrin

import android.view.View
import kotlinx.android.synthetic.main.row_media_video_more.*
import me.himanshusoni.chatmessageview.ui.MoreView.MoreViewHolder
import ir.amin.HaftTeen.ui.LaunchActivity.presentFragment
import ir.amin.HaftTeen.vasni.core.DataLoader
import ir.amin.HaftTeen.vasni.extention.loadImage
import ir.amin.HaftTeen.vasni.model.teentaak.Vitrin.Video
import ir.amin.HaftTeen.vasni.teentaak.fragment.market.VideoDetailFragment
import ir.amin.HaftTeen.R


class VideoMoreAdapter(containerView: View) : MoreViewHolder<Video>(containerView) {

    override fun bindData(data: Video, payloads: List<Any>) {

        imv_media_video_more.loadImage(containerView.context, data.thumbnail!!, pv_loading_pic_video_more)
        tv_media_video_more_name.text = data.title
        if (data.price == 0)
            tv_media_video_more_price.text = containerView.context.getString(R.string.free)
        else
            tv_media_video_more_price.text =
                    data.price.toString() + " " + containerView.context.getString(R.string.currency)

        ll_video_more.setOnClickListener {
            DataLoader.instance.video = data
            presentFragment(VideoDetailFragment(""))
        }

    }

}