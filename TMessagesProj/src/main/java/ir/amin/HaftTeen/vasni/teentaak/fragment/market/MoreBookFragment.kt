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
import ir.amin.HaftTeen.vasni.adapter.Vitrin.BookMoreAdapter
import ir.amin.HaftTeen.vasni.api.ApiService
import ir.amin.HaftTeen.vasni.core.DataLoader
import ir.amin.HaftTeen.vasni.extention.getData
import ir.amin.HaftTeen.vasni.extention.getError
import ir.amin.HaftTeen.vasni.extention.getSuccess
import ir.amin.HaftTeen.vasni.model.teentaak.Vitrin.Book
import ir.amin.HaftTeen.vasni.model.teentaak.Vitrin.Tag
import ir.amin.HaftTeen.R

class MoreBookFragment : BaseFragment, NotificationCenter.NotificationCenterDelegate, Callback<JsonObject> {

    private var txt: String = ""
    private val adapter = MoreAdapter()
    private var tag = Tag()
    private var categoryId: String? = ""
    private lateinit var refresh_book_more: SwipeRefreshLayout
    private lateinit var rc_book_more: RecyclerView
    private lateinit var pv_book_more_loading: View

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
        fragmentView = factory.inflate(R.layout.activity_book_more, null)
        fragmentView.setOnTouchListener { v, event -> true }

        refresh_book_more = fragmentView.findViewById(R.id.refresh_book_more)
        rc_book_more = fragmentView.findViewById(R.id.rc_book_more)
        pv_book_more_loading = fragmentView.findViewById(R.id.pv_book_more_loading)

        tag = DataLoader.instance.vitrinTag
        categoryId = VasniSchema.instance.book_category_id
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
        VasniSchema.instance.show(true, pv_book_more_loading)

        ApiService.apiInterface.getVitrinTag("2", tag.id!!, VasniSchema.instance.vitrin_is_banner!!).enqueue(this)

        refresh_book_more.setOnRefreshListener {
            adapter.removeAllData()
            refresh_book_more.isRefreshing = true
            VasniSchema.instance.show(true, pv_book_more_loading)
            ApiService.apiInterface.getVitrinTag("2", tag.id!!, VasniSchema.instance.vitrin_is_banner!!
            ).enqueue(this)
        }
    }

    override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
        if (response.isSuccessful && response.body() != null) {
            try {
                refresh_book_more.isRefreshing = false
                VasniSchema.instance.show(false, pv_book_more_loading)
                if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                    val booksList: List<Book> =
                            Gson().fromJson(
                                    getData(response.body()!!).get("book").asJsonArray,
                                    Array<Book>::class.java
                            ).toList()

                    rc_book_more.layoutManager = RtlGrid(fragmentView.context, 3)
                    adapter.apply {
                        register(RegisterItem(R.layout.row_media_book_more, BookMoreAdapter::class.java))
                        startAnimPosition(1)
                    }
                    adapter.loadData(booksList)
                    adapter.attachTo(rc_book_more)
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

}
