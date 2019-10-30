package ir.amin.HaftTeen.vasni.teentaak.fragment.news

import android.content.Context
import android.support.v4.widget.SwipeRefreshLayout
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

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ir.amin.HaftTeen.messenger.NotificationCenter
import ir.amin.HaftTeen.ui.ActionBar.ActionBar
import ir.amin.HaftTeen.ui.ActionBar.BaseFragment
import ir.amin.HaftTeen.ui.ActionBar.Theme
import ir.amin.HaftTeen.ui.ActionBar.ThemeDescription
import ir.amin.HaftTeen.vasni.adapter.NewsAdapter
import ir.amin.HaftTeen.vasni.api.ApiService
import ir.amin.HaftTeen.vasni.extention.getDataArray
import ir.amin.HaftTeen.vasni.extention.getError
import ir.amin.HaftTeen.vasni.extention.getSuccess
import ir.amin.HaftTeen.vasni.model.teentaak.EventHandler
import ir.amin.HaftTeen.vasni.model.teentaak.News
import ir.amin.HaftTeen.R

class NewsFragment : BaseFragment, NotificationCenter.NotificationCenterDelegate, Callback<JsonObject> {

    private var txt: String = ""
    private var eventData = EventHandler()
    private val adapter = MoreAdapter()
    private lateinit var newsList: List<News>
    private lateinit var refresh_news: SwipeRefreshLayout
    private lateinit var rc_news: RecyclerView
    private lateinit var pv_news: View

    constructor(txt: String, data: EventHandler) {
        this.txt = txt
        this.eventData = data
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
        fragmentView = factory.inflate(R.layout.frg_news, null)
        fragmentView.setOnTouchListener { v, event -> true }

        refresh_news = fragmentView.findViewById(R.id.refresh_news)
        rc_news = fragmentView.findViewById(R.id.rc_news)
        pv_news = fragmentView.findViewById(R.id.pv_news)

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
        ApiService.apiInterface.getNews(
                VasniSchema.instance.getPageData(
                        eventData.category
                )
        ).enqueue(this)
        refresh_news.setOnRefreshListener {
            adapter.removeAllData()
            refresh_news.isRefreshing = true
            ApiService.apiInterface.getNews(
                    VasniSchema.instance.getPageData(
                            eventData.category
                    )
            ).enqueue(this)
        }


    }

    override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
        if (response.isSuccessful && response.body() != null) {
            try {
                VasniSchema.instance.show(false, pv_news)
                refresh_news.isRefreshing = false
                if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                    newsList =
                            Gson().fromJson(getDataArray(response.body()!!), Array<News>::class.java).toList()
                    rc_news.layoutManager = LinearLayoutManager(fragmentView.context!!)
                    adapter.apply {
                        register(RegisterItem(R.layout.row_news, NewsAdapter::class.java, onNewsClick))
                        startAnimPosition(1)
                    }
                    adapter.loadData(newsList)
                    adapter.attachTo(rc_news)
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
        VasniSchema.instance.showMessage(
                fragmentView.context!!,
                fragmentView.context!!.getString(R.string.server_error),
                "",
                fragmentView.context!!.getString(R.string.ok)
        )
    }

    private val onNewsClick = object : MoreClickListener() {
        override fun onItemClick(view: View, position: Int) {
            when (view.id) {
                R.id.rl_news_row -> {
                    presentFragment(NewsDetailFragment(newsList.get(position).title!!, newsList.get(position)))
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


}
