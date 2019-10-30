package ir.amin.HaftTeen.vasni.teentaak.fragment.playerChart

import android.content.Context
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.google.gson.Gson
import com.google.gson.JsonObject
import me.himanshusoni.chatmessageview.Vasni.VasniSchema
import me.himanshusoni.chatmessageview.ui.CircleImageView
import me.himanshusoni.chatmessageview.ui.MTextViewBold
import me.himanshusoni.chatmessageview.ui.MoreView.MoreAdapter
import me.himanshusoni.chatmessageview.ui.MoreView.link.RegisterItem
import me.himanshusoni.chatmessageview.ui.ProgressView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ir.amin.HaftTeen.messenger.NotificationCenter
import ir.amin.HaftTeen.ui.ActionBar.ActionBar
import ir.amin.HaftTeen.ui.ActionBar.BaseFragment
import ir.amin.HaftTeen.ui.ActionBar.Theme
import ir.amin.HaftTeen.ui.ActionBar.ThemeDescription
import ir.amin.HaftTeen.vasni.adapter.PlayerChart.ChartAdapter
import ir.amin.HaftTeen.vasni.adapter.PlayerChart.MorePlayerAdapter
import ir.amin.HaftTeen.vasni.api.ApiService
import ir.amin.HaftTeen.vasni.extention.*
import ir.amin.HaftTeen.vasni.model.teentaak.*
import ir.amin.HaftTeen.R

class ChallengeChartFragment : BaseFragment, NotificationCenter.NotificationCenterDelegate {

    private var txt: String = ""
    private var eventHandler = EventHandler()
    private val adapter = MoreAdapter()
    private var playerList: List<PlayerChart> = ArrayList()
    private var player: List<Player> = ArrayList()
    private lateinit var refresh_top_player: SwipeRefreshLayout
    private lateinit var rc_top_player: RecyclerView
    private lateinit var pv_top_player: View
    private lateinit var ll_player_chart_user: LinearLayout
    private lateinit var tv_player_chart_user_name: MTextViewBold
    private lateinit var tv_player_chart_today_rank: MTextViewBold
    private lateinit var tv_player_chart_total_rank: MTextViewBold
    private lateinit var tv_player_chart_user_score: MTextViewBold
    private lateinit var imv_player_chart_pic: CircleImageView
    private lateinit var pv_player_chart: ProgressView

    constructor(txt: String, data: EventHandler) {
        this.txt = txt
        this.eventHandler = data
    }

    override fun createView(context: Context?): View {
        actionBar.setBackButtonImage(R.drawable.ic_ab_back)
        actionBar.setAllowOverlayTitle(true)
        actionBar.setTitle(txt)
        actionBar.setActionBarMenuOnItemClick(object : ActionBar.ActionBarMenuOnItemClick() {
            override fun onItemClick(id: Int) {
                if (id == -1) {
                    finishFragment()
                }
            }
        })
        val factory = LayoutInflater.from(context)
        fragmentView = factory.inflate(R.layout.frg_chalange_chart, null)
        fragmentView.setOnTouchListener { v, event -> true }

        refresh_top_player = fragmentView.findViewById(R.id.refresh_top_player)
        rc_top_player = fragmentView.findViewById(R.id.rc_top_player)
        pv_top_player = fragmentView.findViewById(R.id.pv_top_player)
        ll_player_chart_user = fragmentView.findViewById(R.id.ll_player_chart_user)
        tv_player_chart_user_name = fragmentView.findViewById(R.id.tv_player_chart_user_name)
        tv_player_chart_today_rank = fragmentView.findViewById(R.id.tv_player_chart_today_rank)
        tv_player_chart_total_rank = fragmentView.findViewById(R.id.tv_player_chart_total_rank)
        tv_player_chart_user_score = fragmentView.findViewById(R.id.tv_player_chart_user_score)
        imv_player_chart_pic = fragmentView.findViewById(R.id.imv_player_chart_pic)
        pv_player_chart = fragmentView.findViewById(R.id.pv_player_chart)

        adapter.removeAllData()
        if (eventHandler.type == VasniSchema.instance.competitionTable) {
            getCharts()
            refresh_top_player.setOnRefreshListener {
                adapter.removeAllData()
                VasniSchema.instance.show(true, pv_top_player)
                refresh_top_player.isRefreshing = true
                getCharts()
            }
        } else {
            getScoreBoard()
            refresh_top_player.setOnRefreshListener {
                adapter.removeAllData()
                VasniSchema.instance.show(true, pv_top_player)
                refresh_top_player.isRefreshing = true
                getScoreBoard()
            }
        }

        return fragmentView

    }


    override fun onFragmentCreate(): Boolean {
        return super.onFragmentCreate()
    }

    override fun onFragmentDestroy() {
        super.onFragmentDestroy()
    }

    override fun didReceivedNotification(id: Int, account: Int, vararg args: Any?) {
    }

    override fun getThemeDescriptions(): Array<ThemeDescription> {
        return arrayOf(ThemeDescription(fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null,
                null, Theme.key_windowBackgroundWhite), ThemeDescription(actionBar, ThemeDescription.FLAG_BACKGROUND,
                null, null, null, null,
                Theme.key_actionBarDefault), ThemeDescription(actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR,
                null, null, null, null,
                Theme.key_actionBarDefaultIcon), ThemeDescription(actionBar, ThemeDescription.FLAG_AB_TITLECOLOR,
                null, null, null, null,
                Theme.key_actionBarDefaultTitle), ThemeDescription(actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR,
                null, null, null, null,
                Theme.key_actionBarDefaultSelector))
    }

    fun getCharts() {
        ApiService.apiInterface.getData(20
        ).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    try {
                        VasniSchema.instance.show(false, pv_top_player)
                        if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                            refresh_top_player.isRefreshing = false
                            val players = Gson().fromJson(getData(response.body()!!), Competition::class.java)
                            playerList = Gson().fromJson(
                                    getData(response.body()!!).get("users").asJsonArray,
                                    Array<PlayerChart>::class.java
                            ).toList()
                            rc_top_player.layoutManager = LinearLayoutManager(fragmentView.context!!)
                            adapter.apply {
                                register(RegisterItem(R.layout.row_cat, ChartAdapter::class.java))
                                startAnimPosition(1)
                            }
                            adapter.loadData(playerList)
                            adapter.attachTo(rc_top_player)

                            VasniSchema.instance.show(true, ll_player_chart_user)
                            tv_player_chart_user_name.text = players.user.data.get(0).name
                            tv_player_chart_today_rank.text = fragmentView.context.getString(R.string.today_rank) + " " + players.user.data.get(0).userTodayRank
                            tv_player_chart_total_rank.text = fragmentView.context.getString(R.string.total_rank) + " " + players.user.data.get(0).userTotalRank
                            tv_player_chart_user_score.text = fragmentView.context.getString(R.string.user_point) + " " + players.user.data.get(0).userPoint
                            if (players.user.data.get(0).pic == null || players.user.data.get(0).pic == "")
                                imv_player_chart_pic.setImageResource(R.drawable.amir)
                            else
                                imv_player_chart_pic.loadImage(fragmentView.context!!, players.user.data.get(0).pic, pv_player_chart)

                        } else {
                            VasniSchema.instance.showMessage(
                                    fragmentView.context!!,
                                    getError(response.body()!!).message.toString(),
                                    "",
                                    fragmentView.context!!.getString(R.string.ok)
                            )
                        }
                    } catch (e: Exception) {

                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                VasniSchema.instance.show(false, pv_top_player)
                VasniSchema.instance.showMessage(
                        fragmentView.context!!,
                        fragmentView.context!!.getString(R.string.server_error),
                        "",
                        fragmentView.context!!.getString(R.string.ok)
                )
            }
        })
    }

    fun getScoreBoard() {
        ApiService.apiInterface.getScoreBoard(eventHandler.program, 20
        ).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    try {
                        VasniSchema.instance.show(false, pv_top_player)
                        if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {

                            refresh_top_player.isRefreshing = false
                            player = Gson().fromJson(
                                    getDataArray(response.body()!!).get(0).asJsonObject.get("data"),
                                    Array<Player>::class.java
                            ).toList()
                            adapter.removeAllData()
                            rc_top_player.layoutManager = LinearLayoutManager(fragmentView.context)
                            adapter.apply {
                                register(RegisterItem(R.layout.row_more_player, MorePlayerAdapter::class.java))
                                startAnimPosition(1)
                            }
                            adapter.loadData(player)
                            adapter.attachTo(rc_top_player)

                        } else {
                            VasniSchema.instance.showMessage(
                                    fragmentView.context!!,
                                    getError(response.body()!!).message.toString(),
                                    "",
                                    fragmentView.context!!.getString(R.string.ok)
                            )
                        }
                    } catch (e: Exception) {

                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                VasniSchema.instance.show(false, pv_top_player)
                VasniSchema.instance.showMessage(
                        fragmentView.context!!,
                        fragmentView.context!!.getString(R.string.server_error),
                        "",
                        fragmentView.context!!.getString(R.string.ok)
                )
            }
        })
    }

    override fun onPause() {
        super.onPause()
        EventBus.getDefault().unregister(this)
    }

    override fun onResume() {
        super.onResume()
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: Event) {
        val message = event.message
        val content = event.contents
        if (message!!.equals(VasniSchema.instance.playerMore)) {
            var playerList: ArrayList<Player> = event.player!!
            presentFragment(MorePlayerFragment(content!!, playerList))
        }
    }

}
