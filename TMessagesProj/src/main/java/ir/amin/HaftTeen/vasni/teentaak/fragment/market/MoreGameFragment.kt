package ir.amin.HaftTeen.vasni.teentaak.fragment.market

import android.content.Context
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import com.google.gson.Gson
import com.google.gson.JsonObject
import me.himanshusoni.chatmessageview.Vasni.VasniSchema
import me.himanshusoni.chatmessageview.ui.MoreView.MoreAdapter
import me.himanshusoni.chatmessageview.ui.MoreView.link.RegisterItem
import me.himanshusoni.chatmessageview.ui.RtlGrid
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ir.amin.HaftTeen.messenger.NotificationCenter
import ir.amin.HaftTeen.ui.ActionBar.ActionBar
import ir.amin.HaftTeen.ui.ActionBar.BaseFragment
import ir.amin.HaftTeen.ui.ActionBar.Theme
import ir.amin.HaftTeen.ui.ActionBar.ThemeDescription
import ir.amin.HaftTeen.vasni.adapter.Vitrin.GameMoreAdapter
import ir.amin.HaftTeen.vasni.api.ApiService
import ir.amin.HaftTeen.vasni.core.DataLoader
import ir.amin.HaftTeen.vasni.extention.getData
import ir.amin.HaftTeen.vasni.extention.getError
import ir.amin.HaftTeen.vasni.extention.getSuccess
import ir.amin.HaftTeen.vasni.model.teentaak.Vitrin.Game
import ir.amin.HaftTeen.vasni.model.teentaak.Vitrin.Tag
import ir.amin.HaftTeen.R

class MoreGameFragment : BaseFragment, NotificationCenter.NotificationCenterDelegate, Callback<JsonObject> {

    private var txt: String = ""
    private val adapter = MoreAdapter()
    private var tag = Tag()
    private var categoryId: String? = ""
    private lateinit var refresh_game_more: SwipeRefreshLayout
    private lateinit var rc_game_more: RecyclerView
    private lateinit var pv_game_more_loading: View

    constructor(txt: String) {
        this.txt = txt
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
        fragmentView = factory.inflate(R.layout.activity_game_more, null)
        fragmentView.setOnTouchListener { v, event -> true }

        refresh_game_more = fragmentView.findViewById(R.id.refresh_game_more)
        rc_game_more = fragmentView.findViewById(R.id.rc_game_more)
        pv_game_more_loading = fragmentView.findViewById(R.id.pv_game_more_loading)

        tag = DataLoader.instance.vitrinTag
        categoryId = VasniSchema.instance.game_category_id
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
        VasniSchema.instance.show(true, pv_game_more_loading)

        ApiService.apiInterface.getVitrinTag("1", tag.id!!, VasniSchema.instance.vitrin_is_banner!!
        ).enqueue(this)


        refresh_game_more.setOnRefreshListener {
            adapter.removeAllData()
            refresh_game_more.isRefreshing = true
            VasniSchema.instance.show(true, pv_game_more_loading)
            ApiService.apiInterface.getVitrinTag("1", tag.id!!, VasniSchema.instance.vitrin_is_banner!!
            ).enqueue(this)
        }

    }

    override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
        if (response.isSuccessful && response.body() != null) {
            try {
                VasniSchema.instance.show(false, pv_game_more_loading)
                refresh_game_more.isRefreshing = false
                if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                    val gamesList: List<Game> =
                            Gson().fromJson(
                                    getData(response.body()!!).get("game").asJsonArray,
                                    Array<Game>::class.java
                            ).toList()
                    rc_game_more.layoutManager = RtlGrid(fragmentView.context, 4)
                    adapter.apply {
                        register(RegisterItem(R.layout.row_media_game_more, GameMoreAdapter::class.java))
                        startAnimPosition(1)
                    }
                    adapter.loadData(gamesList)
                    adapter.attachTo(rc_game_more)
                } else {
                    VasniSchema.instance.showMessage(
                            fragmentView.context,
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
        try {
            VasniSchema.instance.showMessage(
                    fragmentView.context!!,
                    fragmentView.context!!.getString(R.string.server_error),
                    "",
                    fragmentView.context!!.getString(R.string.ok)
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}
