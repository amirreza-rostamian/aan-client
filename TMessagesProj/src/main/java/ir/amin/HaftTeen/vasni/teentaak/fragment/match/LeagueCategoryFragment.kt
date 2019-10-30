package ir.amin.HaftTeen.vasni.teentaak.fragment.match

import android.content.Context
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import com.google.gson.Gson
import com.google.gson.JsonObject
import me.himanshusoni.chatmessageview.Vasni.VasniSchema
import me.himanshusoni.chatmessageview.ui.MoreView.MoreAdapter
import me.himanshusoni.chatmessageview.ui.MoreView.action.MoreClickListener
import me.himanshusoni.chatmessageview.ui.MoreView.link.RegisterItem
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
import ir.amin.HaftTeen.ui.LaunchActivity
import ir.amin.HaftTeen.vasni.adapter.Match.LeagueCategoryAdapter
import ir.amin.HaftTeen.vasni.api.ApiService
import ir.amin.HaftTeen.vasni.extention.getData
import ir.amin.HaftTeen.vasni.extention.getError
import ir.amin.HaftTeen.vasni.extention.getSuccess
import ir.amin.HaftTeen.vasni.model.teentaak.Event
import ir.amin.HaftTeen.vasni.model.teentaak.EventHandler
import ir.amin.HaftTeen.vasni.model.teentaak.League
import ir.amin.HaftTeen.R

class LeagueCategoryFragment : BaseFragment, NotificationCenter.NotificationCenterDelegate, Callback<JsonObject> {
    private var txt: String = ""
    private var eventHandler = EventHandler()
    private val adapter = MoreAdapter()
    private var leagues: List<League> = ArrayList()
    private lateinit var refresh_league_category: SwipeRefreshLayout
    private lateinit var img_league_category: AppCompatImageView
    private lateinit var rc_league_category: RecyclerView
    private lateinit var pv_league_category_loading: View

    constructor(txt: String, Data: EventHandler) {
        this.txt = txt
        this.eventHandler = Data
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
        fragmentView = factory.inflate(R.layout.frg_league_category, null)
        fragmentView.setOnTouchListener { v, event -> true }

        refresh_league_category = fragmentView.findViewById(R.id.refresh_league_category)
        img_league_category = fragmentView.findViewById(R.id.img_league_category)
        rc_league_category = fragmentView.findViewById(R.id.rc_league_category)
        pv_league_category_loading = fragmentView.findViewById(R.id.pv_league_category_loading)

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
        VasniSchema.instance.show(true, pv_league_category_loading)
        ApiService.apiInterface.getMatchLeagues(eventHandler.category!!, eventHandler.match
        ).enqueue(this)


        refresh_league_category.setOnRefreshListener {
            adapter.removeAllData()
            refresh_league_category.isRefreshing = true
            VasniSchema.instance.show(true, pv_league_category_loading)
            ApiService.apiInterface.getMatchLeagues(eventHandler.category!!, eventHandler.match
            ).enqueue(this)
        }

    }

    override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
        if (response.isSuccessful && response.body() != null) {
            try {
                VasniSchema.instance.show(false, pv_league_category_loading)
                refresh_league_category.isRefreshing = false
                if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                    leagues =
                            Gson().fromJson(
                                    getData(response.body()!!).get("leagues").asJsonArray,
                                    Array<League>::class.java
                            ).toList()


                    rc_league_category.layoutManager = LinearLayoutManager(fragmentView.context!!)
                    adapter.apply {
                        register(RegisterItem(R.layout.row_cat_league, LeagueCategoryAdapter::class.java))
                        startAnimPosition(1)
                    }
                    adapter.loadData(leagues)
                    adapter.attachTo(rc_league_category)

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

    private val categoryClick = object : MoreClickListener() {
        override fun onItemClick(view: View, position: Int) {
            when (view.id) {
                R.id.imv_league_banner -> {
                    LaunchActivity.presentFragment(LeagueFragment(eventHandler.title, eventHandler, leagues.get(position).id!!))
                }
            }
        }

        override fun onItemTouch(view: View, event: MotionEvent, position: Int): Boolean {
            return true
        }

        override fun onItemLongClick(view: View, position: Int): Boolean {
            return false
        }
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
        val league = event.league
        if (message.equals(VasniSchema.instance.league_detail)) {
            LaunchActivity.presentFragment(LeagueFragment(eventHandler.title, eventHandler, league!!.id!!))
        } else if (message.equals(VasniSchema.instance.showLoading)) {
            VasniSchema.instance.show(true, pv_league_category_loading)
        } else if (message.equals(VasniSchema.instance.hideLoading)) {
            VasniSchema.instance.show(false, pv_league_category_loading)
            initView()
        }
    }

}
