package ir.amin.HaftTeen.vasni.adapter.Vitrin

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import kotlinx.android.synthetic.main.row_cat.*
import kotlinx.android.synthetic.main.row_media_video.*
import me.himanshusoni.chatmessageview.ui.MoreView.MoreAdapter
import me.himanshusoni.chatmessageview.ui.MoreView.MoreViewHolder
import me.himanshusoni.chatmessageview.ui.MoreView.link.RegisterItem
import ir.amin.HaftTeen.ui.LaunchActivity.presentFragment
import ir.amin.HaftTeen.vasni.core.DataLoader
import ir.amin.HaftTeen.vasni.extention.loadImage
import ir.amin.HaftTeen.vasni.model.teentaak.Vitrin.Video
import ir.amin.HaftTeen.vasni.model.teentaak.Vitrin.VideoInfo
import ir.amin.HaftTeen.vasni.teentaak.fragment.market.MoreVideoEpisodeFragment
import ir.amin.HaftTeen.vasni.teentaak.fragment.market.VideoDetailFragment
import ir.amin.HaftTeen.R

class VideoEpisodeAdapter(containerView: View) : MoreViewHolder<VideoInfo>(containerView) {

    private val adapter: MoreAdapter by lazy {
        MoreAdapter().apply {
            register(RegisterItem(R.layout.row_media_video, MediaVideoHolder::class.java))
            attachTo(category_list)
        }
    }

    override fun bindData(data: VideoInfo, payloads: List<Any>) {

        category_title.setText(R.string.episodes_view)
        val layoutManager = LinearLayoutManager(itemView.context)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        layoutManager.reverseLayout = true
        category_list.setLayoutManager(layoutManager)
        category_list.setHasFixedSize(true)
        category_list.setNestedScrollingEnabled(false)
        adapter.removeAllData()
        adapter.loadData(data.episodes!!)
        category_more.setOnClickListener {
            presentFragment(MoreVideoEpisodeFragment(data.items!!.title!!, data.items!!.id!!))
        }

    }

    class MediaVideoHolder(containerView: View) : MoreViewHolder<Video>(containerView) {

        override fun bindData(data: Video, payloads: List<Any>) {
            imv_media_video.loadImage(containerView.context, data.thumbnail!!, pv_media_video_loading)
            tv_media_video_name.setText(data.title)

            if (data.price == 0)
                tv_media_video_price.text = containerView.context.getString(R.string.free)
            else
                tv_media_video_price.text =
                        data.price.toString() + " " + containerView.context.getString(R.string.currency)

            ll_media_video.setOnClickListener {
                DataLoader.instance.video = data
                presentFragment(VideoDetailFragment(""))
            }

        }
    }


}
