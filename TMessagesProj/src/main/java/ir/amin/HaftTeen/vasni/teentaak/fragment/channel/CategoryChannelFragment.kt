package ir.amin.HaftTeen.vasni.teentaak.fragment.channel

import android.content.Context
import android.support.v4.widget.SwipeRefreshLayout
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
import me.himanshusoni.chatmessageview.ui.RtlGrid
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ir.amin.HaftTeen.messenger.MessagesController
import ir.amin.HaftTeen.messenger.NotificationCenter
import ir.amin.HaftTeen.ui.ActionBar.ActionBar
import ir.amin.HaftTeen.ui.ActionBar.BaseFragment
import ir.amin.HaftTeen.ui.ActionBar.Theme
import ir.amin.HaftTeen.ui.ActionBar.ThemeDescription
import ir.amin.HaftTeen.vasni.adapter.Vitrin.CategoryChannelAdapter
import ir.amin.HaftTeen.vasni.api.ApiService
import ir.amin.HaftTeen.vasni.extention.getDataArray
import ir.amin.HaftTeen.vasni.extention.getError
import ir.amin.HaftTeen.vasni.extention.getSuccess
import ir.amin.HaftTeen.vasni.model.teentaak.Channel
import ir.amin.HaftTeen.R

class CategoryChannelFragment : BaseFragment, NotificationCenter.NotificationCenterDelegate, Callback<JsonObject> {

    private var txt: String = ""
    private val adapter = MoreAdapter()
    private var categoryId: String? = ""
    private lateinit var channelList: List<Channel>
    private lateinit var refresh_category_channel: SwipeRefreshLayout
    private lateinit var rc_category_channel: RecyclerView
    private lateinit var pv_category_channel_loading: View

    constructor(txt: String, id: String) {
        this.txt = txt
        this.categoryId = id
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
        fragmentView = factory.inflate(R.layout.frg_category_channel, null)
        fragmentView.setOnTouchListener { v, event -> true }

        refresh_category_channel = fragmentView.findViewById(R.id.refresh_category_channel)
        rc_category_channel = fragmentView.findViewById(R.id.rc_category_channel)
        pv_category_channel_loading = fragmentView.findViewById(R.id.pv_category_channel_loading)

        initView()
        refresh_category_channel.setOnRefreshListener {
            refresh_category_channel.isRefreshing = true
            initView()
        }
        return fragmentView
    }


    fun initView() {
        adapter.removeAllData()
        VasniSchema.instance.show(true, pv_category_channel_loading)
        ApiService.apiInterface.getChannels(
                categoryId!!).enqueue(this)
    }

    override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
        if (response.isSuccessful && response.body() != null) {
            try {
                refresh_category_channel.isRefreshing = false
                VasniSchema.instance.show(false, pv_category_channel_loading)
                if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                    channelList =
                            Gson().fromJson(
                                    getDataArray(response.body()!!),
                                    Array<Channel>::class.java
                            ).toList()

                    rc_category_channel.layoutManager = RtlGrid(fragmentView.context, 3)
                    adapter.apply {
                        register(RegisterItem(R.layout.row_channel, CategoryChannelAdapter::class.java, onChannelClick))
                        startAnimPosition(1)
                    }
                    adapter.loadData(channelList)
                    adapter.attachTo(rc_category_channel)
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

    private val onChannelClick = object : MoreClickListener() {
        override fun onItemClick(view: View, position: Int) {
            when (view.id) {
                R.id.ll_channel -> {
                    MessagesController.getInstance(currentAccount).openByUserName(channelList.get(position).channelId, this@CategoryChannelFragment, 0)
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
