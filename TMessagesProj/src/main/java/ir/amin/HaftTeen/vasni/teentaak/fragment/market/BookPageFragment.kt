package ir.amin.HaftTeen.vasni.teentaak.fragment.market

import android.content.Context
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import com.google.gson.Gson
import com.google.gson.JsonObject
import me.himanshusoni.chatmessageview.Vasni.VasniSchema
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
import ir.amin.HaftTeen.vasni.adapter.Vitrin.BookMediaAdapter
import ir.amin.HaftTeen.vasni.api.ApiService
import ir.amin.HaftTeen.vasni.extention.getDataArray
import ir.amin.HaftTeen.vasni.extention.getError
import ir.amin.HaftTeen.vasni.extention.getSuccess
import ir.amin.HaftTeen.vasni.model.teentaak.Vitrin.Tag
import ir.amin.HaftTeen.R

class BookPageFragment : BaseFragment, NotificationCenter.NotificationCenterDelegate, Callback<JsonObject> {

    private var txt: String = ""
    private var pageId: String = ""
    private val adapter = MoreAdapter()
    private var bookList: List<Tag> = ArrayList()
    private lateinit var refresh_book_page: SwipeRefreshLayout
    private lateinit var rc_book_page: RecyclerView
    private lateinit var empty_book_page_data: View
    private lateinit var pv_book_page_loading: View

    constructor(txt: String, id: String) {
        this.txt = txt
        this.pageId = id
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
        fragmentView = factory.inflate(R.layout.frg_game_page, null)
        fragmentView.setOnTouchListener { v, event -> true }

        refresh_book_page = fragmentView.findViewById(R.id.refresh_book_page)
        rc_book_page = fragmentView.findViewById(R.id.rc_book_page)
        empty_book_page_data = fragmentView.findViewById(R.id.empty_book_page_data)
        pv_book_page_loading = fragmentView.findViewById(R.id.pv_book_page_loading)

        try {
            initView()
            refresh_book_page.setOnRefreshListener {
                adapter.removeAllData()
                VasniSchema.instance.show(true, pv_book_page_loading)
                refresh_book_page.isRefreshing = true
                initView()
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
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
        try {
            adapter.removeAllData()
            ApiService.apiInterface.getVitrinPage("1", pageId).enqueue(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
        if (response.isSuccessful && response.body() != null) {
            try {
                refresh_book_page.isRefreshing = false
                VasniSchema.instance.show(false, pv_book_page_loading)
                if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {

                    bookList =
                            Gson().fromJson(
                                    getDataArray(response.body()!!),
                                    Array<Tag>::class.java
                            ).toList()

                    rc_book_page.layoutManager = LinearLayoutManager(fragmentView.context!!)
                    adapter.apply {
                        register(RegisterItem(R.layout.row_cat, BookMediaAdapter::class.java))
                        startAnimPosition(1)
                    }
                    adapter.loadData(bookList)
                    adapter.attachTo(rc_book_page)

                    if (bookList.isEmpty()) {
                        VasniSchema.instance.show(true, empty_book_page_data)
                    } else {
                        VasniSchema.instance.show(false, empty_book_page_data)
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
            e.printStackTrace()
        }
    }

}
