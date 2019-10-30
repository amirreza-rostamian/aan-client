package ir.amin.HaftTeen.vasni.adapter.Vitrin

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import kotlinx.android.synthetic.main.row_cat.*
import kotlinx.android.synthetic.main.row_media_banner.*
import kotlinx.android.synthetic.main.row_media_book.*
import me.himanshusoni.chatmessageview.Vasni.VasniSchema
import me.himanshusoni.chatmessageview.ui.BannerView
import me.himanshusoni.chatmessageview.ui.MoreView.MoreAdapter
import me.himanshusoni.chatmessageview.ui.MoreView.MoreViewHolder
import me.himanshusoni.chatmessageview.ui.MoreView.link.RegisterItem
import ir.amin.HaftTeen.ui.LaunchActivity.presentFragment
import ir.amin.HaftTeen.vasni.core.DataLoader
import ir.amin.HaftTeen.vasni.extention.loadImage
import ir.amin.HaftTeen.vasni.model.teentaak.Vitrin.Banners
import ir.amin.HaftTeen.vasni.model.teentaak.Vitrin.Book
import ir.amin.HaftTeen.vasni.model.teentaak.Vitrin.Tag
import ir.amin.HaftTeen.vasni.teentaak.fragment.market.BookDetailFragment
import ir.amin.HaftTeen.vasni.teentaak.fragment.market.BookPageFragment
import ir.amin.HaftTeen.vasni.teentaak.fragment.market.MoreBookFragment
import ir.amin.HaftTeen.R


class BookMediaAdapter(containerView: View) : MoreViewHolder<Tag>(containerView) {

    private val adapter: MoreAdapter by lazy {
        MoreAdapter().apply {
            register(RegisterItem(R.layout.row_media_book, MediaBookHolder::class.java))
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
                var bookBanner: ArrayList<Book> = data.book!!
                var bannerUrl: ArrayList<String> = ArrayList()
                for (i in 0 until bookBanner.size) {
                    bannerUrl.add(bookBanner.get(i).banner!!)
                }
                category_banner_row.setImagesUrl(bannerUrl)
                category_banner_row.setOnItemClickListener(object : BannerView.OnItemClickListener {
                    override fun onItemClick(position: Int) {
                        VasniSchema.instance.vitrin_is_banner = "0"
                        DataLoader.instance.book = data.book!!.get(position)
                        presentFragment(BookDetailFragment(""))
                    }
                })

            } else if (data.display == VasniSchema.instance.horizentalView) {
                category_title.text = data.title
                val layoutManager = LinearLayoutManager(itemView.context)
                layoutManager.orientation = LinearLayoutManager.HORIZONTAL
                layoutManager.reverseLayout = true
                category_list.layoutManager = layoutManager
                category_list.setHasFixedSize(true)
                category_list.isNestedScrollingEnabled = false
                adapter.removeAllData()
                adapter.loadData(data.book!!)
                category_more.setOnClickListener {
                    VasniSchema.instance.vitrin_is_banner = "0"
                    DataLoader.instance.vitrinTag = data
                    presentFragment(MoreBookFragment(DataLoader.instance.vitrinTag.title!!))
                }
            } else {
                category_title.text = data.title
                val layoutManager = LinearLayoutManager(itemView.context)
                layoutManager.orientation = LinearLayoutManager.HORIZONTAL
                layoutManager.reverseLayout = true
                category_list.layoutManager = layoutManager
                category_list.setHasFixedSize(true)
                category_list.isNestedScrollingEnabled = false
                adapter.removeAllData()
                adapter.loadData(data.book!!)
                category_more.setOnClickListener {
                    VasniSchema.instance.vitrin_is_banner = "0"
                    DataLoader.instance.vitrinTag = data
                    presentFragment(MoreBookFragment(DataLoader.instance.vitrinTag.title!!))
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
                                DataLoader.instance.book.id = banners.get(position).source_id
                                presentFragment(BookDetailFragment(""))
                            } else if (banners.get(position).source_type == "1") {
                                DataLoader.instance.vitrinTag.id = banners.get(position).source_id
                                DataLoader.instance.vitrinTag.title = data.title
                                presentFragment(MoreBookFragment(DataLoader.instance.vitrinTag.title!!))
                            } else if (banners.get(position).source_type == "3") {
                                presentFragment(BookPageFragment(DataLoader.instance.vitrinTag.title!!, banners.get(position).source_id!!))
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

    class MediaBookHolder(containerView: View) : MoreViewHolder<Book>(containerView) {
        override fun bindData(data: Book, payloads: List<Any>) {
            imv_media_book.loadImage(containerView.context, data.thumbnail!!, pv_loading_pic_book)
            tv_media_book_name.text = data.title
            if (data.price == 0)
                tv_media_book_price.text = containerView.context.getString(R.string.free)
            else
                tv_media_book_price.text = data.price.toString() + " " +
                        containerView.context.getString(R.string.currency)

            ll_media_book.setOnClickListener {
                DataLoader.instance.book = data
                presentFragment(BookDetailFragment(""))
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
                        DataLoader.instance.book.id = data.source_id
                        presentFragment(BookDetailFragment(""))
                    } else if (data.source_type == "1") {
                        DataLoader.instance.vitrinTag.id = data.source_id
                        presentFragment(MoreBookFragment(DataLoader.instance.vitrinTag.title!!))
                    } else if (data.source_type == "3") {
                        presentFragment(BookPageFragment(DataLoader.instance.vitrinTag.title!!, data.source_id!!))
                    }
                }
            }

        }
    }


}
