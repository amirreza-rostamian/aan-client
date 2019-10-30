package ir.amin.HaftTeen.vasni.adapter.Vitrin

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import kotlinx.android.synthetic.main.row_cat.*
import kotlinx.android.synthetic.main.row_media_banner.*
import kotlinx.android.synthetic.main.row_media_video.*
import me.himanshusoni.chatmessageview.Vasni.VasniSchema
import me.himanshusoni.chatmessageview.ui.BannerView
import me.himanshusoni.chatmessageview.ui.MoreView.MoreAdapter
import me.himanshusoni.chatmessageview.ui.MoreView.MoreViewHolder
import me.himanshusoni.chatmessageview.ui.MoreView.link.RegisterItem
import ir.amin.HaftTeen.ui.LaunchActivity.presentFragment
import ir.amin.HaftTeen.vasni.core.DataLoader
import ir.amin.HaftTeen.vasni.extention.loadImage
import ir.amin.HaftTeen.vasni.model.teentaak.Vitrin.Banners
import ir.amin.HaftTeen.vasni.model.teentaak.Vitrin.Tag
import ir.amin.HaftTeen.vasni.model.teentaak.Vitrin.Video
import ir.amin.HaftTeen.vasni.teentaak.fragment.market.MoreVideoFragment
import ir.amin.HaftTeen.vasni.teentaak.fragment.market.VideoDetailFragment
import ir.amin.HaftTeen.vasni.teentaak.fragment.market.VideoPageFragment
import ir.amin.HaftTeen.R


class VideoMediaAdapter(containerView: View) : MoreViewHolder<Tag>(containerView) {

    private val adapter: MoreAdapter by lazy {
        MoreAdapter().apply {
            register(RegisterItem(R.layout.row_media_video, MediaVideoHolder::class.java))
            attachTo(category_list)
        }
    }

    private val bannerAdapter: MoreAdapter by lazy {
        MoreAdapter().apply {
            register(RegisterItem(R.layout.row_media_banner, MediaBannerHolder::class.java))
            attachTo(category_list)
        }
    }

    override fun bindData(data: Tag, payloads: List<Any>) {
        if (data.type == VasniSchema.instance.tagType) {
            if (data.display == VasniSchema.instance.bannerView) {
                VasniSchema.instance.show(true, category_banner_row)
                VasniSchema.instance.show(false, layout)
                VasniSchema.instance.show(true, rlayout)
                VasniSchema.instance.show(false, multi_banner)
                var videoBanner: ArrayList<Video> = data.video!!
                var bannerUrl: ArrayList<String> = ArrayList()
                for (i in 0 until videoBanner.size) {
                    bannerUrl.add(videoBanner.get(i).banner!!)
                }
                category_banner_row.setImagesUrl(bannerUrl)
                category_banner_row.setOnItemClickListener(object : BannerView.OnItemClickListener {
                    override fun onItemClick(position: Int) {
                        VasniSchema.instance.vitrin_is_banner = "0"
                        DataLoader.instance.video = data.video!!.get(position)
                        presentFragment(VideoDetailFragment(""))
                    }
                })

            } else if (data.display == VasniSchema.instance.horizentalView) {
                category_title.setText(data.title)
                val layoutManager = LinearLayoutManager(itemView.context)
                layoutManager.orientation = LinearLayoutManager.HORIZONTAL
                layoutManager.reverseLayout = true
                category_list.setLayoutManager(layoutManager)
                category_list.setHasFixedSize(true)
                category_list.setNestedScrollingEnabled(false)
                adapter.removeAllData()
                adapter.loadData(data.video!!)
                category_more.setOnClickListener {
                    VasniSchema.instance.vitrin_is_banner = "0"
                    DataLoader.instance.vitrinTag = data
                    presentFragment(MoreVideoFragment(DataLoader.instance.vitrinTag.title!!))
                }
            } else {
                category_title.setText(data.title)
                val layoutManager = LinearLayoutManager(itemView.context)
                layoutManager.orientation = LinearLayoutManager.HORIZONTAL
                layoutManager.reverseLayout = true
                category_list.setLayoutManager(layoutManager)
                category_list.setHasFixedSize(true)
                category_list.setNestedScrollingEnabled(false)
                adapter.removeAllData()
                adapter.loadData(data.video!!)
                category_more.setOnClickListener {
                    VasniSchema.instance.vitrin_is_banner = "0"
                    DataLoader.instance.vitrinTag = data
                    presentFragment(MoreVideoFragment(DataLoader.instance.vitrinTag.title!!))
                }
            }
        } else if (data.type == VasniSchema.instance.bannerType) {
            if (data.display == VasniSchema.instance.bannerView) {
                VasniSchema.instance.show(true, category_banner_row)
                VasniSchema.instance.show(false, rlayout)
                VasniSchema.instance.show(false, layout)
                VasniSchema.instance.show(false, multi_banner)
                var banners: ArrayList<Banners> = data.banners
                var bannerUrl: ArrayList<String> = ArrayList()
                for (i in 0 until banners.size) {
                    bannerUrl.add(banners.get(i).banner!!)
                }
                category_banner_row.setImagesUrl(bannerUrl)
                category_banner_row.setOnItemClickListener(object : BannerView.OnItemClickListener {
                    override fun onItemClick(position: Int) {
                        VasniSchema.instance.vitrin_is_banner = "1"
                        if (banners.get(position).source_id != null) {
                            if (banners.get(position).source_type == "2") {
                                DataLoader.instance.video.id = banners.get(position).source_id
                                presentFragment(VideoDetailFragment(""))
                            } else if (banners.get(position).source_type == "1") {
                                DataLoader.instance.vitrinTag.id = banners.get(position).source_id
                                DataLoader.instance.vitrinTag.title = data.title
                                presentFragment(MoreVideoFragment(DataLoader.instance.vitrinTag.title!!))
                            } else if (banners.get(position).source_type == "3") {
                                presentFragment(VideoPageFragment(DataLoader.instance.vitrinTag.title!!, banners.get(position).source_id!!))
                            }
                        }
                    }
                })
            } else if (data.display == VasniSchema.instance.multiBannerView) {
                VasniSchema.instance.vitrin_is_banner = "1"
                DataLoader.instance.vitrinTag.title = data.title
                VasniSchema.instance.show(true, multi_banner)
                VasniSchema.instance.show(false, category_banner_row)
                VasniSchema.instance.show(false, layout)
                VasniSchema.instance.show(true, rlayout)
                val layoutManager = LinearLayoutManager(itemView.context)
                layoutManager.orientation = LinearLayoutManager.HORIZONTAL
                layoutManager.reverseLayout = true
                category_list.layoutManager = layoutManager
                category_list.setHasFixedSize(true)
                category_list.isNestedScrollingEnabled = false
                bannerAdapter.removeAllData()
                bannerAdapter.loadData(data.banners!!)
            }
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

    class MediaBannerHolder(containerView: View) : MoreViewHolder<Banners>(containerView) {
        override fun bindData(data: Banners, payloads: List<Any>) {
            imv_media_banner.loadImage(containerView.context, data.banner!!, pv_loading_media_banner)
            containerView.context.getString(R.string.currency)
            ll_media_banner.setOnClickListener {
                VasniSchema.instance.vitrin_is_banner = "1"
                if (data.source_id != null) {
                    if (data.source_type == "2") {
                        DataLoader.instance.video.id = data.source_id
                        presentFragment(VideoDetailFragment(""))
                    } else if (data.source_type == "1") {
                        DataLoader.instance.vitrinTag.id = data.source_id
                        presentFragment(MoreVideoFragment(DataLoader.instance.vitrinTag.title!!))
                    } else if (data.source_type == "3") {
                        presentFragment(VideoPageFragment(DataLoader.instance.vitrinTag.title!!, data.source_id!!))
                    }
                }
            }

        }
    }

}
