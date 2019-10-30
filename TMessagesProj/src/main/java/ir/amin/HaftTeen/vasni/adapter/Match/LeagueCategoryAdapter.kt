package ir.amin.HaftTeen.vasni.adapter.Match

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.row_cat_league.*
import kotlinx.android.synthetic.main.row_league.*
import me.himanshusoni.chatmessageview.Vasni.VasniSchema
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
import ir.amin.HaftTeen.vasni.extention.getError
import ir.amin.HaftTeen.vasni.extention.getSuccess
import ir.amin.HaftTeen.vasni.extention.loadImage
import ir.amin.HaftTeen.vasni.model.teentaak.Event
import ir.amin.HaftTeen.vasni.model.teentaak.League
import ir.amin.HaftTeen.vasni.model.teentaak.LeagueSubmit
import ir.amin.HaftTeen.vasni.model.teentaak.Vitrin.Game
import ir.amin.HaftTeen.vasni.teentaak.fragment.market.GameDetailFragment
import ir.amin.HaftTeen.R


class LeagueCategoryAdapter(containerView: View) : MoreViewHolder<League>(containerView) {

    private val adapter: MoreAdapter by lazy {
        MoreAdapter().apply {
            register(RegisterItem(R.layout.row_league, LeagueCatHolder::class.java))
            attachTo(category_list)
        }
    }

    override fun bindData(data: League, payloads: List<Any>) {

        category_title.text = data.title
        val layoutManager = LinearLayoutManager(itemView.context)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        layoutManager.reverseLayout = true
        category_list.layoutManager = layoutManager
        category_list.setHasFixedSize(true)
        category_list.isNestedScrollingEnabled = false
        adapter.removeAllData()
        adapter.loadData(data.games!!)
        category_more.setOnClickListener {
            EventBus.getDefault().post(Event(VasniSchema.instance.league_detail, data!!))
        }

        tv_submit.setOnClickListener {
            if (data.user_participate) {
                VasniSchema.instance.showMessage(
                        containerView.context!!,
                        containerView.context!!.getString(R.string.league_submitted),
                        "",
                        containerView.context!!.getString(R.string.ok)
                )
            } else {
                leagueSubmit(data.id!!)
            }
        }

    }

    class LeagueCatHolder(containerView: View) : MoreViewHolder<Game>(containerView) {
        override fun bindData(data: Game, payloads: List<Any>) {
            imv_league_game.loadImage(containerView.context, data.thumbnail!!, pv_loading_league)
            tv_league_game_name.text = data.title
            if (data.price == 0)
                tv_league_game_price.text = containerView.context.getString(R.string.free)
            else
                tv_league_game_price.text =
                        data.price.toString() + " " + containerView.context.getString(R.string.currency)
            ll_league_game.setOnClickListener {
                DataLoader.instance.game = data
                presentFragment(GameDetailFragment(""))
            }

        }
    }

    fun leagueSubmit(id: String) {
        EventBus.getDefault().post(Event(VasniSchema.instance.showLoading))
        ApiService.apiInterface.leagueSubmit(DataLoader.instance.leagueSubmit(LeagueSubmit(id, "0"))
        ).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    try {
                        EventBus.getDefault().post(Event(VasniSchema.instance.hideLoading))
                        if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                            VasniSchema.instance.showMessage(
                                    containerView.context!!,
                                    containerView.context!!.getString(R.string.league_submit_success),
                                    "",
                                    containerView.context!!.getString(R.string.ok)
                            )
//                            tv_submit.visibility = View.GONE
                        } else {
                            VasniSchema.instance.showMessage(
                                    containerView.context!!,
                                    getError(response.body()!!).message.toString(),
                                    "",
                                    containerView.context!!.getString(R.string.ok)
                            )
                        }
                    } catch (e: Exception) {

                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                try {
                    VasniSchema.instance.showMessage(
                            containerView.context!!,
                            containerView.context!!.getString(R.string.server_error),
                            "",
                            containerView.context!!.getString(R.string.ok)
                    )
                } catch (e: Exception) {

                }
            }
        })
    }


}