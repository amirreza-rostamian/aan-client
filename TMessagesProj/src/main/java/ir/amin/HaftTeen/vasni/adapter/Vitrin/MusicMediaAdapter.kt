package ir.amin.HaftTeen.vasni.adapter.Vitrin

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.row_cat.*
import kotlinx.android.synthetic.main.row_media_banner.*
import kotlinx.android.synthetic.main.row_media_music.*
import kotlinx.android.synthetic.main.view_wallet_dialog.view.*
import me.himanshusoni.chatmessageview.Vasni.VasniSchema
import me.himanshusoni.chatmessageview.ui.BannerView
import me.himanshusoni.chatmessageview.ui.BottomDialog
import me.himanshusoni.chatmessageview.ui.CenterDialog
import me.himanshusoni.chatmessageview.ui.MoreView.MoreAdapter
import me.himanshusoni.chatmessageview.ui.MoreView.MoreViewHolder
import me.himanshusoni.chatmessageview.ui.MoreView.link.RegisterItem
import org.greenrobot.eventbus.EventBus

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ir.amin.HaftTeen.ui.LaunchActivity.presentFragment
import ir.amin.HaftTeen.vasni.api.ApiService
import ir.amin.HaftTeen.vasni.core.DataLoader
import ir.amin.HaftTeen.vasni.extention.getData
import ir.amin.HaftTeen.vasni.extention.getError
import ir.amin.HaftTeen.vasni.extention.getSuccess
import ir.amin.HaftTeen.vasni.extention.loadImage
import ir.amin.HaftTeen.vasni.model.teentaak.Event
import ir.amin.HaftTeen.vasni.model.teentaak.Vitrin.Banners
import ir.amin.HaftTeen.vasni.model.teentaak.Vitrin.Music
import ir.amin.HaftTeen.vasni.model.teentaak.Vitrin.Tag
import ir.amin.HaftTeen.vasni.teentaak.fragment.market.MoreMusicFragment
import ir.amin.HaftTeen.vasni.teentaak.fragment.market.MusicPageFragment
import ir.amin.HaftTeen.vasni.teentaak.fragment.market.MusicPlayerFragment
import ir.amin.HaftTeen.R

class MusicMediaAdapter(containerView: View) : MoreViewHolder<Tag>(containerView) {

    private val adapter: MoreAdapter by lazy {
        MoreAdapter().apply {
            register(RegisterItem(R.layout.row_media_music, MediaMusicHolder::class.java))
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
                var musicBanner: ArrayList<Music> = data.music!!
                var bannerUrl: ArrayList<String> = ArrayList()
                for (i in 0 until musicBanner.size) {
                    bannerUrl.add(musicBanner.get(i).banner!!)
                }
                category_banner_row.setImagesUrl(bannerUrl)
                category_banner_row.setOnItemClickListener(object : BannerView.OnItemClickListener {
                    override fun onItemClick(position: Int) {
                        VasniSchema.instance.vitrin_is_banner = "0"
                        DataLoader.instance.music = data.music!!.get(position)
                        presentFragment(MusicPlayerFragment(""))
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
                adapter.loadData(data.music!!)
                category_more.setOnClickListener {
                    VasniSchema.instance.vitrin_is_banner = "0"
                    DataLoader.instance.vitrinTag = data
                    presentFragment(MoreMusicFragment(DataLoader.instance.vitrinTag.title!!))
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
                adapter.loadData(data.music!!)
                category_more.setOnClickListener {
                    VasniSchema.instance.vitrin_is_banner = "0"
                    DataLoader.instance.vitrinTag = data
                    presentFragment(MoreMusicFragment(DataLoader.instance.vitrinTag.title!!))
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
                                DataLoader.instance.music.id = banners.get(position).source_id
                                presentFragment(MusicPlayerFragment(""))
                            } else if (banners.get(position).source_type == "1") {
                                DataLoader.instance.vitrinTag.id = banners.get(position).source_id
                                DataLoader.instance.vitrinTag.title = data.title
                                presentFragment(MoreMusicFragment(DataLoader.instance.vitrinTag.title!!))
                            } else if (banners.get(position).source_type == "3") {
                                presentFragment(MusicPageFragment(DataLoader.instance.vitrinTag.title!!, banners.get(position).source_id!!))
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

    class MediaMusicHolder(containerView: View) : MoreViewHolder<Music>(containerView) {
        override fun bindData(data: Music, payloads: List<Any>) {
            imv_media_music.loadImage(containerView.context, data.thumbnail!!, pv_loading_pic_admin)
            tv_media_music_name.text = data.title

            if (data.price == 0)
                tv_media_music_price.text = containerView.context.getString(R.string.free)
            else
                tv_media_music_price.text =
                        data.price.toString() + " " + containerView.context.getString(R.string.currency)


            ll_media_music.setOnClickListener {

                DataLoader.instance.music = data

                if (data.wallet!!.bought == false && data.price != 0) {
                    getWalletBalance(data)
//                    if (data.wallet!!.balance == "0") {
//                        activeWallet(data)
//                    } else {
//                        buy(data)
//                    }
                } else {
                    presentFragment(MusicPlayerFragment(""))
                }

            }
        }

        fun buy(data: Music) {
            BottomDialog.Builder(containerView.context!!)
                    .setContent(containerView.context.getString(R.string.wallet_message_dialog))
                    .setNegativeText(containerView.context.getString(R.string.yes))
                    .setNegativeTextColor(ContextCompat.getColor(containerView.context!!, R.color.colorAccent))
                    .setPositiveText(containerView.context.getString(R.string.no))
                    .autoDismiss(false)
                    .setCancelable(false)
                    .onNegative(object : BottomDialog.ButtonCallback {
                        override fun onClick(dialog: BottomDialog) {
                            if (data.price!! <= data.wallet!!.balance!!.toInt()) {
                                getWalletReduce(data)
                            } else {
                                VasniSchema.instance.showMessage(
                                        containerView.context!!,
                                        containerView.context.getString(R.string.wallet_no_balance),
                                        "",
                                        containerView.context.getString(R.string.ok)
                                )
                            }
                            dialog.dismiss()
                        }
                    })
                    .onPositive(object : BottomDialog.ButtonCallback {
                        override fun onClick(dialog: BottomDialog) {
                            dialog.dismiss()
                        }
                    })
                    .show()
        }

        fun getWalletReduce(data: Music) {
            EventBus.getDefault().post(Event(VasniSchema.instance.showLoading))
            ApiService.apiInterface.getWalletReduce(data.id!!, VasniSchema.instance.music_type!!).enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful && response.body() != null) {
                        EventBus.getDefault().post(Event(VasniSchema.instance.hideLoading))
                        if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                            presentFragment(MusicPlayerFragment(""))
                        } else {
                            VasniSchema.instance.showMessage(
                                    containerView.context,
                                    getError(response.body()!!).message.toString(),
                                    "",
                                    containerView.context.getString(R.string.ok)
                            )
                        }
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    VasniSchema.instance.showMessage(
                            containerView.context,
                            containerView.context.getString(R.string.server_error),
                            "",
                            containerView.context.getString(R.string.ok)
                    )
                }
            })
        }

        fun activeWallet(data: Music) {
            BottomDialog.Builder(containerView.context!!)
                    .setContent(containerView.context.getString(R.string.wallet_message_dialog_active))
                    .setNegativeText(containerView.context.getString(R.string.ok))
                    .setNegativeTextColor(ContextCompat.getColor(containerView.context!!, R.color.colorAccent))
                    .setPositiveText(containerView.context.getString(R.string.skip))
                    .autoDismiss(false)
                    .setCancelable(false)
                    .onNegative(object : BottomDialog.ButtonCallback {
                        override fun onClick(dialog: BottomDialog) {
                            activeWalletDialog(data)
                            dialog.dismiss()
                        }
                    })
                    .onPositive(object : BottomDialog.ButtonCallback {
                        override fun onClick(dialog: BottomDialog) {
                            dialog.dismiss()
                        }
                    })
                    .show()
        }

        fun activeWalletDialog(data: Music) {
            val inflater = containerView.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val customView = inflater.inflate(R.layout.view_wallet_dialog, null)

            var dialog: CenterDialog = CenterDialog.Builder(containerView.context!!)
                    .setCustomView(customView)
                    .autoDismiss(false)
                    .setCancelable(true)
                    .show()

            customView.tv_dialog_wallet_detail.setText(containerView.context.getString(R.string.wallet_message_dialog_active_desc), true)
            customView.btn_submit_dialog_wallet.setOnClickListener {
                if (customView.et_dialog_wallet_active_code.text.trim().isEmpty()) {
                    VasniSchema.instance.showMessage(
                            containerView.context!!,
                            containerView.context.getString(R.string.wallet_is_empty),
                            "",
                            containerView.context.getString(R.string.ok)
                    )
                } else {
                    customView.pv_loading_active_wallet!!.visibility = View.VISIBLE
                    customView.rl_active_wallet!!.visibility = View.GONE
                    ApiService.apiInterface.checkWalletVoucher(
                            customView.et_dialog_wallet_active_code.text.trim().toString()
                    ).enqueue(object : Callback<JsonObject> {
                        override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                            if (response.isSuccessful && response.body() != null) {
                                dialog.dismiss()
                                if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                                    buy(data)
                                } else {
                                    VasniSchema.instance.showMessage(
                                            containerView.context,
                                            getError(response.body()!!).message.toString(),
                                            "",
                                            containerView.context.getString(R.string.ok)
                                    )
                                }
                            }
                        }

                        override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                            VasniSchema.instance.showMessage(
                                    containerView.context,
                                    containerView.context.getString(R.string.server_error),
                                    "",
                                    containerView.context.getString(R.string.ok)
                            )
                        }
                    })
                }
            }

        }

        fun getWalletBalance(data: Music) {
            EventBus.getDefault().post(Event(VasniSchema.instance.showLoading))
            ApiService.apiInterface.getWalletBalance().enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    EventBus.getDefault().post(Event(VasniSchema.instance.hideLoading))
                    if (response.isSuccessful && response.body() != null) {
                        if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                            data.wallet!!.balance = getData(response.body()!!).get("balance").asString
                            if (data.wallet!!.balance == "0") {
                                activeWallet(data)
                            } else {
                                buy(data)
                            }

                        } else {
                            VasniSchema.instance.showMessage(
                                    containerView.context,
                                    getError(response.body()!!).message.toString(),
                                    "",
                                    containerView.context.getString(R.string.ok)
                            )
                        }
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    VasniSchema.instance.showMessage(
                            containerView.context,
                            containerView.context.getString(R.string.server_error),
                            "",
                            containerView.context.getString(R.string.ok)
                    )
                }
            })
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
                        DataLoader.instance.music.id = data.source_id
                        presentFragment(MusicPlayerFragment(""))
                    } else if (data.source_type == "1") {
                        DataLoader.instance.vitrinTag.id = data.source_id
                        presentFragment(MoreMusicFragment(DataLoader.instance.vitrinTag.title!!))
                    } else if (data.source_type == "3") {
                        presentFragment(MusicPageFragment(DataLoader.instance.vitrinTag.title!!, data.source_id!!))
                    }
                }
            }

        }
    }

}
