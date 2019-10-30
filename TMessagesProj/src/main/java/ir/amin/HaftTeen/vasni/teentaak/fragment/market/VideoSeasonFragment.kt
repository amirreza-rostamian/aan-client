package ir.amin.HaftTeen.vasni.teentaak.fragment.market

import android.content.Context
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import com.google.gson.Gson
import com.google.gson.JsonObject
import me.himanshusoni.chatmessageview.Vasni.VasniSchema
import me.himanshusoni.chatmessageview.VideoPlayer.Jzvd
import me.himanshusoni.chatmessageview.ui.MoreView.MoreAdapter
import me.himanshusoni.chatmessageview.ui.MoreView.action.MoreClickListener
import me.himanshusoni.chatmessageview.ui.MoreView.link.RegisterItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ir.amin.HaftTeen.messenger.NotificationCenter
import ir.amin.HaftTeen.ui.ActionBar.ActionBar
import ir.amin.HaftTeen.ui.ActionBar.BaseFragment
import ir.amin.HaftTeen.ui.ActionBar.Theme
import ir.amin.HaftTeen.ui.ActionBar.ThemeDescription
import ir.amin.HaftTeen.vasni.adapter.Vitrin.VideoSeasonAdapter
import ir.amin.HaftTeen.vasni.api.ApiService
import ir.amin.HaftTeen.vasni.extention.getData
import ir.amin.HaftTeen.vasni.extention.getError
import ir.amin.HaftTeen.vasni.extention.getSuccess
import ir.amin.HaftTeen.vasni.model.teentaak.Vitrin.Video
import ir.amin.HaftTeen.R

class VideoSeasonFragment : BaseFragment, NotificationCenter.NotificationCenterDelegate, Callback<JsonObject> {

    private var txt: String = ""
    var parentId: String = ""
    private val adapter = MoreAdapter()
    private var videoList: List<Video> = ArrayList()
    private lateinit var refresh_video_season: SwipeRefreshLayout
    private lateinit var rc_video_season: RecyclerView
    private lateinit var pv_video_season_loading: View

    constructor(txt: String, id: String) {
        this.txt = txt
        this.parentId = id
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
        fragmentView = factory.inflate(R.layout.frg_video_season, null)
        fragmentView.setOnTouchListener { v, event -> true }

        refresh_video_season = fragmentView.findViewById(R.id.refresh_video_season)
        rc_video_season = fragmentView.findViewById(R.id.rc_video_season)
        pv_video_season_loading = fragmentView.findViewById(R.id.pv_video_season_loading)

        initView()
        refresh_video_season.setOnRefreshListener {
            adapter.removeAllData()
            VasniSchema.instance.show(true, pv_video_season_loading)
            refresh_video_season.isRefreshing = true
            initView()
        }

        return fragmentView
    }

    private fun initView() {
        adapter.removeAllData()
        ApiService.apiInterface.getSubCategoryVitrinVideo(parentId).enqueue(this)
    }

    override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
        if (response.isSuccessful && response.body() != null) {
            try {
                refresh_video_season.isRefreshing = false
                VasniSchema.instance.show(false, pv_video_season_loading)
                if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {

                    videoList =
                            Gson().fromJson(
                                    getData(response.body()!!).get("items").asJsonArray,
                                    Array<Video>::class.java
                            ).toList()

                    rc_video_season.layoutManager = GridLayoutManager(fragmentView.context, 2)
                    adapter.apply {
                        register(
                                RegisterItem(
                                        R.layout.row_season_video,
                                        VideoSeasonAdapter::class.java,
                                        subCategoryClick
                                )
                        )
                        startAnimPosition(1)
                    }
                    adapter.loadData(videoList)
                    adapter.attachTo(rc_video_season)


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

    private val subCategoryClick = object : MoreClickListener() {
        override fun onItemClick(view: View, position: Int) {
            when (view.id) {
                R.id.ll_season_video -> {
                    presentFragment(VideoSectioinFragment(videoList.get(position).title!!, videoList.get(position).id!!))
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

    override fun onFragmentCreate(): Boolean {
        return super.onFragmentCreate()
    }

    override fun onFragmentDestroy() {
        super.onFragmentDestroy()
        Jzvd.releaseAllVideos()
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
