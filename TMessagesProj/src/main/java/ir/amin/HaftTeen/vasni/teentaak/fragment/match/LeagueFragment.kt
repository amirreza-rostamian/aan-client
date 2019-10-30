package ir.amin.HaftTeen.vasni.teentaak.fragment.match

import android.content.Context
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import com.google.gson.Gson
import com.google.gson.JsonObject
import me.himanshusoni.chatmessageview.Vasni.VasniSchema
import me.himanshusoni.chatmessageview.ui.MTextViewBold
import me.himanshusoni.chatmessageview.ui.MoreView.MoreAdapter
import me.himanshusoni.chatmessageview.ui.MoreView.link.RegisterItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ir.amin.HaftTeen.messenger.NotificationCenter
import ir.amin.HaftTeen.ui.ActionBar.ActionBar
import ir.amin.HaftTeen.ui.ActionBar.BaseFragment
import ir.amin.HaftTeen.ui.ActionBar.Theme
import ir.amin.HaftTeen.ui.ActionBar.ThemeDescription
import ir.amin.HaftTeen.ui.LaunchActivity
import ir.amin.HaftTeen.vasni.adapter.Match.LeagueGameAdapter
import ir.amin.HaftTeen.vasni.api.ApiService
import ir.amin.HaftTeen.vasni.core.DataLoader
import ir.amin.HaftTeen.vasni.extention.getData
import ir.amin.HaftTeen.vasni.extention.getError
import ir.amin.HaftTeen.vasni.extention.getSuccess
import ir.amin.HaftTeen.vasni.extention.loadImage
import ir.amin.HaftTeen.vasni.model.teentaak.EventHandler
import ir.amin.HaftTeen.vasni.model.teentaak.League
import ir.amin.HaftTeen.vasni.model.teentaak.LeagueSubmit
import ir.amin.HaftTeen.R

class LeagueFragment : BaseFragment, NotificationCenter.NotificationCenterDelegate, Callback<JsonObject> {

    private var txt: String = ""
    private val adapter = MoreAdapter()
    private var league = League()
    private var eventHandler = EventHandler()
    private var leagueId = String()
    private lateinit var imv_league_icon: AppCompatImageView
    private lateinit var cv_league_details: CardView
    private lateinit var tv_end_league: MTextViewBold
    private lateinit var tv_start_league: MTextViewBold
    private lateinit var tv_league_submit: MTextViewBold
    private lateinit var tv_league_detail: MTextViewBold
    private lateinit var refresh_league: SwipeRefreshLayout
    private lateinit var rc_league: RecyclerView
    private lateinit var pv_league_loading: View

    constructor(txt: String, Data: EventHandler, id: String) {
        this.txt = txt
        this.eventHandler = Data
        this.leagueId = id
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
        fragmentView = factory.inflate(R.layout.frg_league, null)
        fragmentView.setOnTouchListener { v, event -> true }

        imv_league_icon = fragmentView.findViewById(R.id.imv_league_icon)
        cv_league_details = fragmentView.findViewById(R.id.cv_league_details)
        tv_end_league = fragmentView.findViewById(R.id.tv_end_league)
        tv_start_league = fragmentView.findViewById(R.id.tv_start_league)
        tv_league_submit = fragmentView.findViewById(R.id.tv_league_submit)
        tv_league_detail = fragmentView.findViewById(R.id.tv_league_detail)
        refresh_league = fragmentView.findViewById(R.id.refresh_league)
        rc_league = fragmentView.findViewById(R.id.rc_league)
        pv_league_loading = fragmentView.findViewById(R.id.pv_league_loading)

        initView()

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

    fun initView() {
        adapter.removeAllData()
        VasniSchema.instance.show(true, pv_league_loading)
        ApiService.apiInterface.getMatchLeagues(
                eventHandler.category!!, leagueId
        ).enqueue(this)
        refresh_league.setOnRefreshListener {
            adapter.removeAllData()
            refresh_league.isRefreshing = true
            VasniSchema.instance.show(true, pv_league_loading)
            ApiService.apiInterface.getMatchLeagues(
                    eventHandler.category!!, leagueId
            ).enqueue(this)
        }

        cv_league_details.setOnClickListener {
            LaunchActivity.presentFragment(LeagueViewFragment(eventHandler.title, league))
        }

        tv_league_detail.setOnClickListener {
            LaunchActivity.presentFragment(LeagueViewFragment(eventHandler.title, league))
        }

        tv_league_submit.setOnClickListener {
            leagueSubmit()
        }
    }

    override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
        if (response.isSuccessful && response.body() != null) {
            try {
                VasniSchema.instance.show(false, pv_league_loading)
                refresh_league.isRefreshing = false
                if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                    league = Gson().fromJson(
                            getData(response.body()!!).get("leagues").asJsonArray.get(0).asJsonObject,
                            League::class.java
                    )

                    VasniSchema.instance.leagueId = league.id!!
                    rc_league.layoutManager = LinearLayoutManager(fragmentView.context!!)
                    adapter.apply {
                        register(RegisterItem(R.layout.row_league_game, LeagueGameAdapter::class.java))
                        startAnimPosition(1)
                    }
                    adapter.loadData(league.games)
                    adapter.attachTo(rc_league)

                    imv_league_icon.loadImage(fragmentView.context!!, league.pic.toString())
                    tv_start_league.text = league.start
                    tv_end_league.text = league.end

                    if (league.dateDiff!!.get("day").asString == "0" || league.dateDiff!!.get("hour").asString == "0") {
                        tv_league_submit.text = fragmentView.context.getString(R.string.league_submit_expire)
                        tv_league_submit.isClickable = false
                        tv_league_submit.visibility = View.VISIBLE
                    } else {
                        tv_league_submit.text = fragmentView.context.getString(R.string.league_submit)
                        tv_league_submit.isClickable = true
                        if (league.user_participate)
                            tv_league_submit.visibility = View.GONE
                        else
                            tv_league_submit.visibility = View.VISIBLE
                    }


                } else {
                    VasniSchema.instance.showMessage(
                            fragmentView.context!!,
                            getError(response.body()!!).message.toString(),
                            "",
                            fragmentView.context.getString(R.string.ok)
                    )
                }

            } catch (e: Exception) {

            }

        }
    }

    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
        VasniSchema.instance.showMessage(
                fragmentView.context!!,
                fragmentView.context.getString(R.string.server_error),
                "",
                fragmentView.context.getString(R.string.ok)
        )
    }

    fun leagueSubmit() {
        VasniSchema.instance.show(true, pv_league_loading)
        ApiService.apiInterface.leagueSubmit(DataLoader.instance.leagueSubmit(LeagueSubmit(league.id!!, "0"))
        ).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    try {
                        VasniSchema.instance.show(false, pv_league_loading)
                        if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                            VasniSchema.instance.showMessage(
                                    fragmentView.context!!,
                                    fragmentView.context!!.getString(R.string.league_submit_success),
                                    "",
                                    fragmentView.context!!.getString(R.string.ok)
                            )
                            tv_league_submit.visibility = View.GONE
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
                try {
                    VasniSchema.instance.showMessage(
                            fragmentView.context!!,
                            fragmentView.context!!.getString(R.string.server_error),
                            "",
                            fragmentView.context!!.getString(R.string.ok)
                    )
                } catch (e: Exception) {

                }
            }
        })
    }

}
