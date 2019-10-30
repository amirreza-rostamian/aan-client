package ir.amin.HaftTeen.vasni.teentaak.fragment.playerChart

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import me.himanshusoni.chatmessageview.ui.MoreView.MoreAdapter
import me.himanshusoni.chatmessageview.ui.MoreView.link.RegisterItem
import ir.amin.HaftTeen.messenger.NotificationCenter
import ir.amin.HaftTeen.ui.ActionBar.ActionBar
import ir.amin.HaftTeen.ui.ActionBar.BaseFragment
import ir.amin.HaftTeen.ui.ActionBar.Theme
import ir.amin.HaftTeen.ui.ActionBar.ThemeDescription
import ir.amin.HaftTeen.vasni.adapter.PlayerChart.MorePlayerAdapter
import ir.amin.HaftTeen.vasni.model.teentaak.Player
import ir.amin.HaftTeen.R

class MorePlayerFragment : BaseFragment, NotificationCenter.NotificationCenterDelegate {

    private var txt: String = ""
    private var playerData = ArrayList<Player>()
    private val adapter = MoreAdapter()
    private lateinit var rc_more_player: RecyclerView

    constructor(txt: String, data: ArrayList<Player>) {
        this.txt = txt
        this.playerData = data
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
        fragmentView = factory.inflate(R.layout.frg_more_player, null)
        fragmentView.setOnTouchListener { v, event -> true }

        rc_more_player = fragmentView.findViewById(R.id.rc_more_player)

        adapter.removeAllData()
        rc_more_player.layoutManager = LinearLayoutManager(context)
        adapter.apply {
            register(RegisterItem(R.layout.row_more_player, MorePlayerAdapter::class.java))
            startAnimPosition(1)
        }
        adapter.loadData(playerData)
        adapter.attachTo(rc_more_player)

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


}
