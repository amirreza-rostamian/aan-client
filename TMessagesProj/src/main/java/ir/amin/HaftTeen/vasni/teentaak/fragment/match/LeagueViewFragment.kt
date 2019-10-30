package ir.amin.HaftTeen.vasni.teentaak.fragment.match

import android.content.Context
import android.graphics.Paint
import android.support.v4.view.ViewPager
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import me.himanshusoni.chatmessageview.Vasni.VasniSchema
import me.himanshusoni.chatmessageview.ui.JustifiedTextView
import me.himanshusoni.chatmessageview.ui.MTextViewBold
import me.himanshusoni.chatmessageview.ui.MoreView.MoreAdapter
import me.himanshusoni.chatmessageview.ui.MoreView.action.MoreClickListener
import me.himanshusoni.chatmessageview.ui.MoreView.link.RegisterItem
import me.himanshusoni.chatmessageview.ui.ProgressView
import me.himanshusoni.chatmessageview.ui.RtlGrid
import ir.amin.HaftTeen.messenger.NotificationCenter
import ir.amin.HaftTeen.ui.ActionBar.ActionBar
import ir.amin.HaftTeen.ui.ActionBar.BaseFragment
import ir.amin.HaftTeen.ui.ActionBar.Theme
import ir.amin.HaftTeen.ui.ActionBar.ThemeDescription
import ir.amin.HaftTeen.vasni.adapter.Match.LeagueUserAdapter
import ir.amin.HaftTeen.vasni.adapter.Match.TabLeagueAdapter
import ir.amin.HaftTeen.vasni.extention.loadImage
import ir.amin.HaftTeen.vasni.model.teentaak.League
import ir.amin.HaftTeen.R

class LeagueViewFragment : BaseFragment, NotificationCenter.NotificationCenterDelegate {
    private var txt: String = ""
    private var league = League()
    private var categoryList: ArrayList<League> = ArrayList()
    private val adapter = MoreAdapter()
    private val userAdapter = MoreAdapter()
    private lateinit var pv_league_view_loading: ProgressView
    private lateinit var imv_league_view_banner: AppCompatImageView
    private lateinit var vp_league_view_Item: ViewPager
    private lateinit var rc_league_view: RecyclerView
    private lateinit var pv_league_detail_loading: ProgressView
    private lateinit var imv_league_detail_banner: AppCompatImageView
    private lateinit var tv_league_detail_title: MTextViewBold
    private lateinit var tv_end_league_view: MTextViewBold
    private lateinit var tv_start_league_view: MTextViewBold
    private lateinit var tv_league_detail_desc: JustifiedTextView
    private lateinit var rc_league_user: RecyclerView
    private lateinit var empty_league_data: View
    private lateinit var ll_league_detail: LinearLayout

    constructor(txt: String, data: League) {
        this.txt = txt
        this.league = data
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
        fragmentView = factory.inflate(R.layout.frg_league_view, null)
        fragmentView.setOnTouchListener { v, event -> true }

        pv_league_view_loading = fragmentView.findViewById(R.id.pv_league_view_loading)
        vp_league_view_Item = fragmentView.findViewById(R.id.vp_league_view_Item)
        imv_league_view_banner = fragmentView.findViewById(R.id.imv_league_view_banner)
        rc_league_view = fragmentView.findViewById(R.id.rc_league_view)
        pv_league_detail_loading = fragmentView.findViewById(R.id.pv_league_detail_loading)
        imv_league_detail_banner = fragmentView.findViewById(R.id.imv_league_detail_banner)
        tv_league_detail_title = fragmentView.findViewById(R.id.tv_league_detail_title)
        tv_end_league_view = fragmentView.findViewById(R.id.tv_end_league_view)
        tv_start_league_view = fragmentView.findViewById(R.id.tv_start_league_view)
        tv_league_detail_desc = fragmentView.findViewById(R.id.tv_league_detail_desc)
        rc_league_user = fragmentView.findViewById(R.id.rc_league_user)
        empty_league_data = fragmentView.findViewById(R.id.empty_league_data)
        ll_league_detail = fragmentView.findViewById(R.id.ll_league_detail)

        configTabs()

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


    private fun configTabs() {

        imv_league_view_banner.loadImage(fragmentView.context!!, league.pic!!, pv_league_view_loading)

        categoryList.clear()
        var category = League()
        category.title = fragmentView.context.getString(R.string.league_detail)
        category.isSelected = true
        categoryList.add(category)

        category = League()
        category.title = fragmentView.context.getString(R.string.league_winner)
        category.isSelected = false
        categoryList.add(category)

        category = League()
        category.title = fragmentView.context.getString(R.string.league_participate)
        category.isSelected = false
        categoryList.add(category)

        category = League()
        category.title = fragmentView.context.getString(R.string.league_top_scores)
        category.isSelected = false
        categoryList.add(category)

        val llayoutManager = LinearLayoutManager(fragmentView.context!!)
        llayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        llayoutManager.reverseLayout = true
        rc_league_view!!.setLayoutManager(llayoutManager)
        rc_league_view!!.setHasFixedSize(true)
        adapter.apply {
            register(
                    RegisterItem(
                            R.layout.row_tab_league,
                            TabLeagueAdapter::class.java,
                            onTabClick
                    )
            )
            startAnimPosition(1)
        }
        adapter.loadData(categoryList)
        adapter.attachTo(rc_league_view!!)

        VasniSchema.instance.show(true, ll_league_detail)
        VasniSchema.instance.show(false, rc_league_user)
        VasniSchema.instance.show(false, empty_league_data)
        getDetail()
    }

    private val onTabClick = object : MoreClickListener() {
        override fun onItemClick(view: View, position: Int) {
            when (view.id) {
                R.id.tv_tab_league_title -> {
                    for (i in 0 until categoryList.size) {
                        categoryList.get(i).isSelected = false
                    }
                    categoryList.get(position).isSelected = true
                    adapter.notifyDataSetChanged()
                    when (position) {
                        0 -> {
                            VasniSchema.instance.show(true, ll_league_detail)
                            VasniSchema.instance.show(false, rc_league_user)
                            VasniSchema.instance.show(false, empty_league_data)
                            getDetail()
                        }
                        1 -> {
                            VasniSchema.instance.show(true, rc_league_user)
                            VasniSchema.instance.show(false, ll_league_detail)
                            VasniSchema.instance.show(false, empty_league_data)
                            getWinner()
                        }
                        2 -> {
                            VasniSchema.instance.show(true, rc_league_user)
                            VasniSchema.instance.show(false, ll_league_detail)
                            VasniSchema.instance.show(false, empty_league_data)
                            getUser()
                        }
                        3 -> {
                            VasniSchema.instance.show(true, rc_league_user)
                            VasniSchema.instance.show(false, ll_league_detail)
                            VasniSchema.instance.show(false, empty_league_data)
                            getTopScore()
                        }
                    }
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

    fun getDetail() {
        imv_league_detail_banner.loadImage(fragmentView.context!!, league.pic!!, pv_league_detail_loading)
        tv_start_league_view.text = league.start.toString()
        tv_end_league_view.text = league.end.toString()
        tv_league_detail_title.text = league.title
        tv_league_detail_desc.text = league.description
        tv_league_detail_desc.setAlignment(Paint.Align.RIGHT)
        tv_league_detail_desc.setLineSpacing(20)
        tv_league_detail_desc.setTextSize(1, 16.0f)
    }

    fun getWinner() {
        userAdapter.removeAllData()
        rc_league_user.layoutManager = RtlGrid(fragmentView.context!!, 2)

        userAdapter.apply {
            register(RegisterItem(R.layout.row_league_user, LeagueUserAdapter::class.java))
            startAnimPosition(1)
        }
        userAdapter.loadData(league.panel_winner)
        userAdapter.attachTo(rc_league_user)

        if (league.panel_winner.isEmpty()) {
            VasniSchema.instance.show(true, empty_league_data)
        } else {
            VasniSchema.instance.show(false, empty_league_data)
        }
    }

    fun getUser() {
        userAdapter.removeAllData()
        rc_league_user.layoutManager = RtlGrid(fragmentView.context!!, 2)
        userAdapter.apply {
            register(RegisterItem(R.layout.row_league_user, LeagueUserAdapter::class.java))
            startAnimPosition(1)
        }

        for (i in 0 until league.users.size)
            league.users.get(i).score = league.users.get(i).league_score

        userAdapter.loadData(league.users)
        userAdapter.attachTo(rc_league_user)

        if (league.users.isEmpty()) {
            VasniSchema.instance.show(true, empty_league_data)
        } else {
            VasniSchema.instance.show(false, empty_league_data)
        }
    }


    fun getTopScore() {
        userAdapter.removeAllData()
        rc_league_user.layoutManager = RtlGrid(fragmentView.context!!, 2)
        userAdapter.apply {
            register(RegisterItem(R.layout.row_league_user, LeagueUserAdapter::class.java))
            startAnimPosition(1)
        }
        userAdapter.loadData(league.top_scores)
        userAdapter.attachTo(rc_league_user)

        if (league.top_scores.isEmpty()) {
            VasniSchema.instance.show(true, empty_league_data)
        } else {
            VasniSchema.instance.show(false, empty_league_data)
        }
    }

}
