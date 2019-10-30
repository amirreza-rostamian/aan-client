package ir.amin.HaftTeen.vasni.teentaak.fragment.market

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
import ir.amin.HaftTeen.vasni.adapter.Vitrin.BookMediaAdapter
import ir.amin.HaftTeen.vasni.adapter.Vitrin.MarketFilterAdapter
import ir.amin.HaftTeen.vasni.api.ApiService
import ir.amin.HaftTeen.vasni.extention.getDataArray
import ir.amin.HaftTeen.vasni.extention.getError
import ir.amin.HaftTeen.vasni.extention.getSuccess
import ir.amin.HaftTeen.vasni.model.teentaak.Vitrin.PageFilter
import ir.amin.HaftTeen.vasni.model.teentaak.Vitrin.Tag
import ir.amin.HaftTeen.R

class BookMarketFragment : BaseFragment, NotificationCenter.NotificationCenterDelegate, Callback<JsonObject> {

    private var txt: String = ""
    private lateinit var refresh_vitrin_book: SwipeRefreshLayout
    private lateinit var pv_vitrin_book_loading: View
    private lateinit var empty_vitrin_book_data: View
    private lateinit var rc_vitrin_book_filter: RecyclerView
    private lateinit var rc_vitrin_book: RecyclerView

    private val adapter = MoreAdapter()
    private var bookList: List<Tag> = ArrayList()
    private val filterAdapter = MoreAdapter()
    private var filterList: List<PageFilter> = ArrayList()

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
        fragmentView = factory.inflate(R.layout.frg_book_market, null)
        fragmentView.setOnTouchListener { v, event -> true }

        refresh_vitrin_book = fragmentView.findViewById(R.id.refresh_vitrin_book)
        pv_vitrin_book_loading = fragmentView.findViewById(R.id.pv_vitrin_book_loading)
        empty_vitrin_book_data = fragmentView.findViewById(R.id.empty_vitrin_book_data)
        rc_vitrin_book_filter = fragmentView.findViewById(R.id.rc_vitrin_book_filter)
        rc_vitrin_book = fragmentView.findViewById(R.id.rc_vitrin_book)

        initView()
        refresh_vitrin_book.setOnRefreshListener {
            VasniSchema.instance.show(true, pv_vitrin_book_loading)
            refresh_vitrin_book.isRefreshing = true
            initView()
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
        adapter!!.removeAllData()
        filterAdapter.removeAllData()
        ApiService.apiInterface.getVitrinPage("2", "").enqueue(this)
    }


    override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
        if (response.isSuccessful && response.body() != null) {
            try {
                refresh_vitrin_book!!.isRefreshing = false
                VasniSchema.instance.show(false, pv_vitrin_book_loading!!)
                if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {


                    filterList =
                            Gson().fromJson(
                                    getDataArray(response.body()!!),
                                    Array<PageFilter>::class.java
                            ).toList()

                    if (filterList.size > 0) {
                        filterList.get(0).isSelected = true
                        filterAdapter.notifyDataSetChanged()
                        adapter.removeAllData()
                        Jzvd.releaseAllVideos()
                        VasniSchema.instance.book_category_id = filterList.get(0).id
                        getFilter(filterList.get(0).id!!)
                    }

                    val llayoutManager = LinearLayoutManager(fragmentView.context!!)
                    llayoutManager.orientation = LinearLayoutManager.HORIZONTAL
                    llayoutManager.reverseLayout = true
                    rc_vitrin_book_filter!!.setLayoutManager(llayoutManager)
                    rc_vitrin_book_filter!!.setHasFixedSize(true)
                    filterAdapter.apply {
                        register(
                                RegisterItem(
                                        R.layout.row_market_filter,
                                        MarketFilterAdapter::class.java,
                                        filterClick
                                )
                        )
                        startAnimPosition(1)
                    }
                    filterAdapter.loadData(filterList)
                    filterAdapter.attachTo(rc_vitrin_book_filter!!)


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


    private val filterClick = object : MoreClickListener() {
        override fun onItemClick(view: View, position: Int) {
            when (view.id) {
                R.id.tv_market_filter_title -> {
                    for (i in 0 until filterList.size) {
                        filterList.get(i).isSelected = false
                    }
                    filterList.get(position).isSelected = true
                    filterAdapter.notifyDataSetChanged()
                    adapter.removeAllData()
                    Jzvd.releaseAllVideos()
                    VasniSchema.instance.book_category_id = filterList.get(position).id
                    getFilter(filterList.get(position).id!!)
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

    fun getFilter(id: String) {
        VasniSchema.instance.show(true, pv_vitrin_book_loading!!)
        ApiService.apiInterface.getVitrinPage("2", id).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    try {
                        VasniSchema.instance.show(false, pv_vitrin_book_loading!!)
                        if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {

                            bookList = Gson().fromJson(
                                    getDataArray(response.body()!!),
                                    Array<Tag>::class.java
                            ).toList()

                            rc_vitrin_book!!.layoutManager = LinearLayoutManager(fragmentView.context!!)
                            adapter.apply {
                                register(RegisterItem(R.layout.row_cat, BookMediaAdapter::class.java))
                                startAnimPosition(1)
                            }
                            adapter.loadData(bookList)
                            adapter.attachTo(rc_vitrin_book!!)

                            if (bookList.isEmpty()) {
                                VasniSchema.instance.show(true, empty_vitrin_book_data!!)
                            } else {
                                VasniSchema.instance.show(false, empty_vitrin_book_data!!)
                            }

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
                try {
                    VasniSchema.instance.showMessage(
                            fragmentView.context!!,
                            fragmentView.context!!.getString(R.string.server_error),
                            "",
                            fragmentView.context!!.getString(R.string.ok)
                    )
                } catch (e: Exception) {

                }
            }
        })
    }

}
