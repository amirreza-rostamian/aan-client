package ir.amin.HaftTeen.vasni.teentaak.fragment.market

import android.content.Context
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import com.google.gson.Gson
import com.google.gson.JsonObject
import me.himanshusoni.chatmessageview.Vasni.VasniSchema
import me.himanshusoni.chatmessageview.VideoPlayer.Jzvd
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
import ir.amin.HaftTeen.vasni.adapter.Vitrin.VideoSectionAdapter
import ir.amin.HaftTeen.vasni.api.ApiService
import ir.amin.HaftTeen.vasni.extention.getDataArray
import ir.amin.HaftTeen.vasni.extention.getError
import ir.amin.HaftTeen.vasni.extention.getSuccess
import ir.amin.HaftTeen.vasni.model.teentaak.Vitrin.Video
import ir.amin.HaftTeen.R

class VideoSectioinFragment : BaseFragment, NotificationCenter.NotificationCenterDelegate, Callback<JsonObject> {

    private var txt: String = ""
    var parentId: String = ""
    private val adapter = MoreAdapter()
    private lateinit var refresh_video_section: SwipeRefreshLayout
    private lateinit var rc_video_section: RecyclerView
    private lateinit var pv_video_section_loading: View

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
        fragmentView = factory.inflate(R.layout.frg_video_section, null)
        fragmentView.setOnTouchListener { v, event -> true }

        refresh_video_section = fragmentView.findViewById(R.id.refresh_video_section)
        rc_video_section = fragmentView.findViewById(R.id.rc_video_section)
        pv_video_section_loading = fragmentView.findViewById(R.id.pv_video_section_loading)

        initView()
        refresh_video_section.setOnRefreshListener {
            adapter.removeAllData()
            VasniSchema.instance.show(true, pv_video_section_loading)
            refresh_video_section.isRefreshing = true
            initView()
        }

        return fragmentView
    }

    private fun initView() {
        adapter.removeAllData()
        ApiService.apiInterface.getVitrinVideo(parentId, "").enqueue(this)
    }


    override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
        if (response.isSuccessful && response.body() != null) {
            try {
                refresh_video_section.isRefreshing = false
                VasniSchema.instance.show(false, pv_video_section_loading)
                if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {

                    val videoList: List<Video> =
                            Gson().fromJson(
                                    getDataArray(response.body()!!).get(0).asJsonObject.get("items").asJsonArray,
                                    Array<Video>::class.java
                            ).toList()

                    rc_video_section.layoutManager = GridLayoutManager(fragmentView.context, 1)
                    adapter.apply {
                        register(RegisterItem(R.layout.row_section_video, VideoSectionAdapter::class.java))
                        startAnimPosition(1)
                    }
                    adapter.loadData(videoList)
                    adapter.attachTo(rc_video_section)


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
