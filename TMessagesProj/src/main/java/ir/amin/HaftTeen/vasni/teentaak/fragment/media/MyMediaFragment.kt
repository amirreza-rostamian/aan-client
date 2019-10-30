package ir.amin.HaftTeen.vasni.teentaak.fragment.media

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
import me.himanshusoni.chatmessageview.VideoPlayer.Jzvd
import me.himanshusoni.chatmessageview.ui.MTextView
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
import ir.amin.HaftTeen.vasni.adapter.Media.MyMediaAdapter
import ir.amin.HaftTeen.vasni.api.ApiService
import ir.amin.HaftTeen.vasni.extention.getDataArray
import ir.amin.HaftTeen.vasni.extention.getError
import ir.amin.HaftTeen.vasni.extention.getSuccess
import ir.amin.HaftTeen.vasni.model.teentaak.EventHandler
import ir.amin.HaftTeen.vasni.model.teentaak.Media
import ir.amin.HaftTeen.R

class MyMediaFragment : BaseFragment, NotificationCenter.NotificationCenterDelegate, Callback<JsonObject> {

    private var txt: String = ""
    private var eventHandler = EventHandler()
    private val adapter = MoreAdapter()
    private lateinit var refresh_my_media: SwipeRefreshLayout
    private lateinit var rc_my_media: RecyclerView
    private lateinit var pv_my_media_loading: View
    private lateinit var tv_upload_my_media: MTextView
    private lateinit var ll_upload_my_media: LinearLayout

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
        fragmentView = factory.inflate(R.layout.frg_my_media, null)
        fragmentView.setOnTouchListener { v, event -> true }

        refresh_my_media = fragmentView.findViewById(R.id.refresh_my_media)
        rc_my_media = fragmentView.findViewById(R.id.rc_my_media)
        pv_my_media_loading = fragmentView.findViewById(R.id.pv_my_media_loading)
        tv_upload_my_media = fragmentView.findViewById(R.id.tv_upload_my_media)
        ll_upload_my_media = fragmentView.findViewById(R.id.ll_upload_my_media)

        adapter.removeAllData()
        getMyMedia()
        refresh_my_media.setOnRefreshListener {
            adapter.removeAllData()
            Jzvd.releaseAllVideos()
            refresh_my_media.isRefreshing = true
            getMyMedia()
        }
        ll_upload_my_media.setOnClickListener {
            presentFragment(UploadMediaFragment(fragmentView.context.getString(R.string.upload_new_media), eventHandler))
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


    private fun getMyMedia() {
        ApiService.apiInterface.getMedia(eventHandler.tileId, eventHandler.category).enqueue(this)
    }

    override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
        if (response.isSuccessful && response.body() != null) {
            try {
                VasniSchema.instance.show(false, pv_my_media_loading)
                if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                    refresh_my_media.isRefreshing = false
                    val mediaList: List<Media> =
                            Gson().fromJson(getDataArray(response.body()!!), Array<Media>::class.java)
                                    .toList()
                    rc_my_media.layoutManager = LinearLayoutManager(fragmentView.context)
                    adapter.apply {
                        register(RegisterItem(R.layout.row_my_media, MyMediaAdapter::class.java))
                        startAnimPosition(1)
                    }
                    adapter.loadData(mediaList)
                    adapter.attachTo(rc_my_media)

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
