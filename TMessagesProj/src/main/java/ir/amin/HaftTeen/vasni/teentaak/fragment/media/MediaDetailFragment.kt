package ir.amin.HaftTeen.vasni.teentaak.fragment.media

import android.content.Context
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
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
import ir.amin.HaftTeen.vasni.adapter.Media.MediaAdminAdapter
import ir.amin.HaftTeen.vasni.adapter.Media.MediaUserAdapter
import ir.amin.HaftTeen.vasni.api.ApiService
import ir.amin.HaftTeen.vasni.extention.consume
import ir.amin.HaftTeen.vasni.extention.getDataArray
import ir.amin.HaftTeen.vasni.extention.getError
import ir.amin.HaftTeen.vasni.extention.getSuccess
import ir.amin.HaftTeen.vasni.model.teentaak.EventHandler
import ir.amin.HaftTeen.vasni.model.teentaak.Media
import ir.amin.HaftTeen.R
import java.util.*

class MediaDetailFragment : BaseFragment, NotificationCenter.NotificationCenterDelegate, Callback<JsonObject> {

    private var txt: String = ""
    private var mediaList: List<Media> = ArrayList<Media>()
    private var position: Int = 0
    private var eventHandler = EventHandler()
    private val adapter = MoreAdapter()
    private lateinit var rc_media_detail: RecyclerView
    private lateinit var refresh_media_detail: SwipeRefreshLayout
    private lateinit var pv_media_detail_loading: View

    constructor(txt: String, event: EventHandler, list: List<Media>, i: Int) {
        this.txt = txt
        this.eventHandler = event
        this.mediaList = list
        this.position = i
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
        fragmentView = factory.inflate(R.layout.frg_media_detail, null)
        fragmentView.setOnTouchListener { v, event -> true }

        rc_media_detail = fragmentView.findViewById(R.id.rc_media_detail)
        refresh_media_detail = fragmentView.findViewById(R.id.refresh_media_detail)
        pv_media_detail_loading = fragmentView.findViewById(R.id.pv_media_detail_loading)

        initView()
        refresh_media_detail.setOnRefreshListener {
            adapter.removeAllData()
            Jzvd.releaseAllVideos()
            refresh_media_detail.isRefreshing = true
            ApiService.apiInterface.getMedia(
                    eventHandler.tileId,
                    mediaList.get(0).category_id
            ).enqueue(this)
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


    private fun initView() {
        ApiService.apiInterface.getMedia(
                eventHandler.tileId,
                mediaList.get(0).category_id
        ).enqueue(this)
    }

    override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
        if (response.isSuccessful && response.body() != null) {
            try {
                VasniSchema.instance.show(false, pv_media_detail_loading)
                if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                    refresh_media_detail.isRefreshing = false
                    var mediaData: List<Media> =
                            Gson().fromJson(getDataArray(response.body()!!), Array<Media>::class.java)
                                    .toList()


                    when (eventHandler.source) {
                        VasniSchema.instance.multiMediaType_admin -> consume {
                            if (position != 0)
                                Collections.swap(mediaData, 0, position)
                            rc_media_detail.layoutManager = LinearLayoutManager(fragmentView.context)
                            adapter.apply {
                                register(RegisterItem(R.layout.row_media_admin, MediaAdminAdapter::class.java))
                                startAnimPosition(1)
                            }

                            adapter.loadData(mediaData)
                            adapter.attachTo(rc_media_detail)

                        }
                        VasniSchema.instance.multiMediaType_User -> consume {
                            if (position != 0)
                                Collections.swap(mediaData, 0, position)
                            rc_media_detail.layoutManager = LinearLayoutManager(fragmentView.context)
                            adapter.apply {
                                register(RegisterItem(R.layout.row_media_user, MediaUserAdapter::class.java))
                                startAnimPosition(1)
                            }
                            adapter.loadData(mediaData)
                            adapter.attachTo(rc_media_detail)

                        }
                        VasniSchema.instance.multiMediaType_users -> consume {
                            if (position != 0)
                                Collections.swap(mediaData, 0, position)
                            rc_media_detail.layoutManager = LinearLayoutManager(fragmentView.context)
                            adapter.apply {
                                register(RegisterItem(R.layout.row_media_user, MediaUserAdapter::class.java))
                                startAnimPosition(1)
                            }
                            adapter.loadData(mediaData)
                            adapter.attachTo(rc_media_detail)

                        }
                        VasniSchema.instance.multiMediaType_all -> consume {
                            if (position != 0)
                                Collections.swap(mediaData, 0, position)
                            rc_media_detail.layoutManager = LinearLayoutManager(fragmentView.context)
                            adapter.apply {
                                register(RegisterItem(R.layout.row_media_user, MediaUserAdapter::class.java))
                                startAnimPosition(1)
                            }
                            adapter.loadData(mediaData)
                            adapter.attachTo(rc_media_detail)

                        }
                    }

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
        VasniSchema.instance.showMessage(
                fragmentView.context,
                fragmentView.context.getString(R.string.server_error),
                "",
                fragmentView.context.getString(R.string.ok)
        )
    }

    override fun onPause() {
        super.onPause()
        Jzvd.releaseAllVideos()
    }


}
