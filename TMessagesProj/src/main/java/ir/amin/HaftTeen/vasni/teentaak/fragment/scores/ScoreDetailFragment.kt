package ir.amin.HaftTeen.vasni.teentaak.fragment.scores

import android.content.Context
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
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
import ir.amin.HaftTeen.vasni.adapter.Score.ProgramScoreAdapter
import ir.amin.HaftTeen.vasni.api.ApiService
import ir.amin.HaftTeen.vasni.extention.getDataArray
import ir.amin.HaftTeen.vasni.extention.getError
import ir.amin.HaftTeen.vasni.extention.getSuccess
import ir.amin.HaftTeen.vasni.model.teentaak.ProgramScore
import ir.amin.HaftTeen.R

class ScoreDetailFragment : BaseFragment, NotificationCenter.NotificationCenterDelegate {
    private var txt: String = ""
    private var programId: String = ""
    private var rc_score_detail: RecyclerView? = null
    private var refresh_score_detail: SwipeRefreshLayout? = null
    private var pv_score_loading: View? = null
    private val adapter = MoreAdapter()
    private var scores: List<ProgramScore> = ArrayList()

    constructor(txt: String, id: String) {
        this.txt = txt
        this.programId = id
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
        fragmentView = factory.inflate(R.layout.frg_score_detail, null)
        fragmentView.setOnTouchListener { v, event -> true }
        rc_score_detail = fragmentView.findViewById(R.id.rc_score_detail)
        refresh_score_detail = fragmentView.findViewById(R.id.refresh_score_detail)
        pv_score_loading = fragmentView.findViewById(R.id.pv_score_loading)
        initView()
        refresh_score_detail!!.setOnRefreshListener {
            refresh_score_detail!!.isRefreshing = true
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

    private fun initView() {
        adapter.removeAllData()
        VasniSchema.instance.show(true, pv_score_loading!!)
        ApiService.apiInterface.getProgramScores(programId).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    VasniSchema.instance.show(false, pv_score_loading!!)
                    refresh_score_detail!!.isRefreshing = false
                    try {
                        if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                            scores = Gson().fromJson(getDataArray(response.body()!!), Array<ProgramScore>::class.java).toList()
                            val gridlayoutManager = GridLayoutManager(fragmentView.context!!, 1)
                            rc_score_detail!!.layoutManager = gridlayoutManager
                            adapter.apply {
                                register(RegisterItem(R.layout.row_program_score, ProgramScoreAdapter::class.java))
                                startAnimPosition(1)
                            }
                            adapter.loadData(scores)
                            adapter.attachTo(rc_score_detail!!)
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
                VasniSchema.instance.showMessage(
                        fragmentView.context!!,
                        fragmentView.context.getString(R.string.server_error),
                        "",
                        fragmentView.context.getString(R.string.ok)
                )
            }
        })
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
