package ir.amin.HaftTeen.vasni.teentaak.fragment.scores

import android.content.Context
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
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
import ir.amin.HaftTeen.vasni.adapter.Score.ScoreAdapter
import ir.amin.HaftTeen.vasni.api.ApiService
import ir.amin.HaftTeen.vasni.extention.getDataArray
import ir.amin.HaftTeen.vasni.extention.getError
import ir.amin.HaftTeen.vasni.extention.getSuccess
import ir.amin.HaftTeen.vasni.model.api.profile.ResponseProfileData
import ir.amin.HaftTeen.vasni.model.teentaak.Score
import ir.amin.HaftTeen.vasni.webservice.GetProfile
import ir.amin.HaftTeen.R

class ScoreFragment : BaseFragment, NotificationCenter.NotificationCenterDelegate {

    private var txt: String = ""
    private val adapter = MoreAdapter()
    private var scores: List<Score> = ArrayList()
    private var rc_score: RecyclerView? = null
    private var pv_score_loading: View? = null
    private var refresh_score: SwipeRefreshLayout? = null
    private var tv_score: TextView? = null

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
        fragmentView = factory.inflate(R.layout.frg_score, null)
        fragmentView.setOnTouchListener { v, event -> true }
        rc_score = fragmentView.findViewById(R.id.rc_score)
        pv_score_loading = fragmentView.findViewById(R.id.pv_score_loading)
        refresh_score = fragmentView.findViewById(R.id.refresh_score)
        tv_score = fragmentView.findViewById(R.id.tv_score)
        initView()
        refresh_score!!.setOnRefreshListener {
            refresh_score!!.isRefreshing = true
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

    fun initView() {
        adapter.removeAllData()
        VasniSchema.instance.show(true, pv_score_loading!!)
        ApiService.apiInterface.getScores().enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    try {
                        if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                            scores = Gson().fromJson(getDataArray(response.body()!!), Array<Score>::class.java).toList()
                            val gridlayoutManager = GridLayoutManager(fragmentView.context, 1)
                            rc_score!!.layoutManager = gridlayoutManager
                            adapter.apply {
                                register(RegisterItem(R.layout.row_score, ScoreAdapter::class.java, onClick))
                                startAnimPosition(1)
                            }
                            adapter.loadData(scores)
                            adapter.attachTo(rc_score!!)
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
        var getProfile = GetProfile(object : GetProfile.OnResponse {
            override fun onSuccess(data: ResponseProfileData?) {
                VasniSchema.instance.show(false, pv_score_loading!!)
                refresh_score!!.isRefreshing = false
                tv_score!!.setText(" امتیاز کسب شده : " + data!!.userPoint)

            }

            override fun onError(message: String?) {

            }
        })
        getProfile.getProfile()
    }

    private val onClick = object : MoreClickListener() {
        override fun onItemClick(view: View, position: Int) {
            when (view.id) {
                R.id.ll_all_score -> {
                    if (scores.get(position).user_point == "0") {
                        VasniSchema.instance.showMessage(
                                fragmentView.context!!,
                                fragmentView.context.getString(R.string.program_score_empty),
                                "",
                                fragmentView.context.getString(R.string.ok)
                        )
                        //Toast.makeText(ApplicationLoader.applicationContext, ApplicationLoader.applicationContext!!.getString(R.string.program_score_empty), Toast.LENGTH_LONG).show()
                    } else {
                        presentFragment(ScoreDetailFragment(scores.get(position).program_name!!, "" + scores.get(position).program_id!!))
                    }

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
