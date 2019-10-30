package  ir.amin.HaftTeen.vasni.adapter.PlayerChart

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import kotlinx.android.synthetic.main.row_cat.*
import kotlinx.android.synthetic.main.row_player_chart.*
import me.himanshusoni.chatmessageview.Vasni.VasniSchema
import me.himanshusoni.chatmessageview.ui.BannerView
import me.himanshusoni.chatmessageview.ui.MoreView.MoreAdapter
import me.himanshusoni.chatmessageview.ui.MoreView.MoreViewHolder
import me.himanshusoni.chatmessageview.ui.MoreView.link.RegisterItem
import org.greenrobot.eventbus.EventBus
import ir.amin.HaftTeen.vasni.extention.consume
import ir.amin.HaftTeen.vasni.extention.loadImage
import ir.amin.HaftTeen.vasni.model.teentaak.Banner
import ir.amin.HaftTeen.vasni.model.teentaak.Event
import ir.amin.HaftTeen.vasni.model.teentaak.Player
import ir.amin.HaftTeen.vasni.model.teentaak.PlayerChart
import ir.amin.HaftTeen.BuildConfig
import ir.amin.HaftTeen.R
import java.util.*
import kotlin.collections.ArrayList


class ChartAdapter(containerView: View) : MoreViewHolder<PlayerChart>(containerView) {

    private val adapter: MoreAdapter by lazy {
        MoreAdapter().apply {
            register(RegisterItem(R.layout.row_player_chart, playerHolder::class.java))
            attachTo(category_list)
        }
    }

    override fun bindData(data: PlayerChart, payloads: List<Any>) {
        if (data.have_banners) {
            VasniSchema.instance.show(false, layout)
            VasniSchema.instance.show(true, category_banner_row)
            var bannerList: ArrayList<Banner> = data.banners
            var bannerUrl: ArrayList<String> = ArrayList()
            for (i in 0 until bannerList.size) {
                var banner = Banner()
                banner.name = bannerList.get(i).name
                banner.action = bannerList.get(i).action
                banner.content_action = bannerList.get(i).content_action
                banner.url = bannerList.get(i).url
                bannerUrl.add(bannerList.get(i).url)
                bannerList.add(banner)

            }
            category_banner_row.setImagesUrl(bannerUrl)
            category_banner_row.setOnItemClickListener(object : BannerView.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    when (bannerList.get(position).action) {
                        VasniSchema.instance.ev_browser -> consume {
                            VasniSchema.instance.openUrlInChrome(
                                    containerView.context,
                                    bannerList.get(position).content_action
                            )
                        }
                        VasniSchema.instance.ev_telegram -> consume {
                            VasniSchema.instance.intentMessageTelegram(
                                    containerView.context,
                                    bannerList.get(position).content_action
                            )
                        }
                        VasniSchema.instance.ev_msg -> consume {
                            VasniSchema.instance.showMessage(
                                    containerView.context!!,
                                    bannerList.get(position).content_action,
                                    "",
                                    containerView.context!!.getString(R.string.ok)
                            )
                        }

                    }

                }
            })


        } else {
            try {
                VasniSchema.instance.show(true, layout)
                VasniSchema.instance.show(false, category_banner_row)
                category_more.setOnClickListener {
                    try {
                        EventBus.getDefault().post(Event(VasniSchema.instance.playerMore, data.title, data.data))
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                category_title.setText(data.title)
                val layoutManager = LinearLayoutManager(itemView.context)
                layoutManager.orientation = LinearLayoutManager.HORIZONTAL
                layoutManager.reverseLayout = true
                category_list.setLayoutManager(layoutManager)
                category_list.setHasFixedSize(true)
                category_list.setNestedScrollingEnabled(false)
                adapter.removeAllData()
                adapter.loadData(data.data)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


    }

    class playerHolder(containerView: View) : MoreViewHolder<Player>(containerView) {

        override fun bindData(data: Player, payloads: List<Any>) {
            val list = ArrayList<Int>()
            list.add(R.drawable.orange_card)
            list.add(R.drawable.red_card)
            list.add(R.drawable.purple_card)
            list.add(R.drawable.pink_card)
            list.add(R.drawable.blue_card)
            val position = Random().nextInt(list.size)
            bg_card_chart.setImageResource(list.get(position))
            list.remove(position)
            if (data.pic == null || data.pic == "" || data.pic.equals(BuildConfig.SERVER_URL + "/"))
                imv_player_chart_icon.setImageResource(R.drawable.amir)
            else
                imv_player_chart_icon.loadImage(containerView.context, data.pic, pv_player_chart)
            tv_player_chart_province.setText(data.sum + " " + containerView.context.getString(R.string.scores))
            tv_player_chart_name.setText(data.name)
            tv_player_chart_ostan.setText(containerView.context.getString(R.string.province) + " " + data.province)
        }
    }
}